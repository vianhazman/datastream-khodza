package org.datastream.khodza.job;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.formats.avro.registry.confluent.ConfluentRegistryAvroDeserializationSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink;
import org.apache.flink.streaming.connectors.influxdb.InfluxDBConfig;
import org.apache.flink.streaming.connectors.influxdb.InfluxDBPoint;
import org.apache.flink.streaming.connectors.influxdb.InfluxDBSink;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.datastream.khodza.model.avro.t_scele_public_mdl_course.Envelope;
import org.datastream.khodza.model.avro.t_scele_public_mdl_logstore_standard_log.LogEnvelope;
import org.datastream.khodza.model.avro.t_scele_public_mdl_logstore_standard_log.LogValue;
import org.datastream.khodza.model.avro.t_scele_public_mdl_user.UserEnvelope;
import org.datastream.khodza.sink.elastic.CourseElasticSearchSinkMap;
import org.datastream.khodza.sink.elastic.ElasticSearchSinkFactory;
import org.datastream.khodza.sink.elastic.UserElasticSearchSinkMap;
import org.datastream.khodza.sink.influx.InfluxDBConfigFactory;
import org.datastream.khodza.sink.influx.LogInfluxMap;
import org.datastream.khodza.sink.kafka.LogKafkaMap;
import org.datastream.khodza.source.CourseAsyncEsDataRequest;
import org.datastream.khodza.source.UserAsyncEsDataRequest;
import org.datastream.khodza.util.constants.Config;
import org.datastream.khodza.util.constants.Namespace;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Scele implements Job {

    public void run( Properties properties,
                     String schemaRegistryUrl,
                     StreamExecutionEnvironment env,
                     ParameterTool parameter
    ) throws Exception {

        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        /* Dimension ES sink initialization */
        ElasticsearchSink.Builder<UserEnvelope> UserESsink =  new ElasticSearchSinkFactory<UserEnvelope>(new UserElasticSearchSinkMap(), parameter.get("es.user.index"), "id", parameter)
                .getElasticSearchSinkClient();
        ElasticsearchSink.Builder<Envelope> CourseESsink = new ElasticSearchSinkFactory<Envelope>(new CourseElasticSearchSinkMap(), parameter.get("es.course.index"), "id", parameter)
                .getElasticSearchSinkClient();



        /* Kafka topic for SCELE event log initialization */
        DataStream<LogEnvelope> logStream = env.addSource(
                new FlinkKafkaConsumer<>(
                        parameter.get("kafka.log.topic"),
                        ConfluentRegistryAvroDeserializationSchema
                                .forSpecific(LogEnvelope.class,
                                        schemaRegistryUrl),
                        properties));

        /* Kafka topic for SCELE user dimension initialization */
        DataStream<UserEnvelope> userStream = env.addSource(
                new FlinkKafkaConsumer<>(
                        parameter.get("kafka.user.topic"),
                        ConfluentRegistryAvroDeserializationSchema
                                .forSpecific(UserEnvelope.class,
                                        schemaRegistryUrl),
                        properties));
        userStream.addSink(UserESsink.build()).name(Namespace.COURSE_ES_SINK);

        /* Kafka topic for SCELE course dimension initialization */
        DataStream<Envelope> courseStream = env.addSource(
                new FlinkKafkaConsumer<>(
                        parameter.get("kafka.course.topic"),
                        ConfluentRegistryAvroDeserializationSchema
                                .forSpecific(Envelope.class,
                                        schemaRegistryUrl),
                        properties));
        courseStream.addSink(CourseESsink.build()).name(Namespace.USER_ES_SINK);

        SingleOutputStreamOperator<LogValue> logTransform = logStream
                .assignTimestampsAndWatermarks(new AscendingTimestampExtractor<LogEnvelope>() {

                    @Override
                    public long extractAscendingTimestamp(LogEnvelope element) {
                        return element.getTsMs();
                    }
                })
                .map( k -> k.getAfter());

        /* Enrich log data with user dimension in Elastic Search */
        DataStream<Tuple2<LogValue, Tuple2<String,String>>> log_enriched_user = AsyncDataStream
                .unorderedWait(logTransform, new UserAsyncEsDataRequest(), 2, TimeUnit.SECONDS, 100).name(Namespace.LOG_USER_ENRICHMENT);

        DataStream<Tuple2<Tuple2<LogValue, Tuple2<String,String>>, Tuple2<String,String>>> log_enriched_course = AsyncDataStream
                .unorderedWait(log_enriched_user, new CourseAsyncEsDataRequest(), 2, TimeUnit.SECONDS, 100).name(Namespace.LOG_USER_COURSE_ENRICHMENT);

        DataStream<InfluxDBPoint> log_influx = log_enriched_course.map(new LogInfluxMap(parameter.get("namespace"))).name(Namespace.INFLUX_MAP);

        Properties producerProperties = new Properties();
        producerProperties.setProperty(Config.BOOTSTRAP_SERVERS, parameter.get(Config.PRODUCER_BOOTSTRAP_SERVERS));
        producerProperties.setProperty(Config.MAX_REQUEST_SIZE, parameter.get(Config.MAX_REQUEST_SIZE));

        FlinkKafkaProducer producer = new FlinkKafkaProducer<String>(
                parameter.get("kafka.sink.topic"),
                new SimpleStringSchema(),producerProperties);
        producer.setWriteTimestampToKafka(true);

        log_enriched_course.map(new LogKafkaMap(parameter.get("namespace"))).addSink(producer).name(Namespace.KAFKA_SINK);

        InfluxDBConfig logInfluxDBConfig = new InfluxDBConfigFactory(parameter).getInfluxConfig();

        log_influx.addSink(new InfluxDBSink(logInfluxDBConfig)).name(Namespace.INFLUX_SINK);

        // execute program
        env.execute(Namespace.SCELE_STREAM_JOB+"-"+parameter.get("namespace"));
    }
}
