package org.datastream.khodza.job;

import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.formats.avro.registry.confluent.ConfluentRegistryAvroDeserializationSchema;
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
import org.datastream.khodza.sink.elastic.ClassElasticSearchSinkMap;
import org.datastream.khodza.sink.elastic.ElasticSearchSinkFactory;
import org.datastream.khodza.sink.elastic.SessionElasticSearchSinkMap;
import org.datastream.khodza.sink.influx.InfluxDBConfigFactory;
import org.datastream.khodza.sink.influx.PresentronikInfluxMap;
import org.datastream.khodza.sink.kafka.PresentronikKafkaMap;
import org.datastream.khodza.source.ClassAsyncEsDataRequest;
import org.datastream.khodza.source.SessionAsyncEsDataRequest;
import org.datastream.khodza.util.constants.Config;
import org.datastream.khodza.util.constants.Namespace;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Presentronik implements Job {
    public void run( Properties properties,
                     String schemaRegistryUrl,
                     StreamExecutionEnvironment env,
                     ParameterTool parameter)
            throws Exception {

        /* Dimension ES sink initialization */
        ElasticsearchSink.Builder<org.datastream.khodza.model.avro.
                p_presentronik_konfigurasi_kelas_jadwal_rutin.Envelope> ClassESsink = new ElasticSearchSinkFactory<org.datastream.khodza.model.avro.
                p_presentronik_konfigurasi_kelas_jadwal_rutin.Envelope>(new ClassElasticSearchSinkMap(), parameter.get("es.class.index"),"kdkelas", parameter)
                .getElasticSearchSinkClient();

        ElasticsearchSink.Builder<org.datastream.khodza.model.avro.
                p_presentronik_cache_kelas_sesi_perkuliahan.Envelope> SessionESsink = new ElasticSearchSinkFactory<org.datastream.khodza.model.avro.
                p_presentronik_cache_kelas_sesi_perkuliahan.Envelope>(new SessionElasticSearchSinkMap(), parameter.get("es.session.index"),"idkuliah", parameter)
                .getElasticSearchSinkClient();

        /* Kafka topic for PRESENTRONIK class configuration initialization */
        DataStream<org.datastream.khodza.model.avro.
                p_presentronik_konfigurasi_kelas_jadwal_rutin.Envelope> classStream = env.addSource(
                new FlinkKafkaConsumer<>(
                        parameter.get("kafka.class.topic"),
                        ConfluentRegistryAvroDeserializationSchema
                                .forSpecific(org.datastream.khodza.model.avro.
                                                p_presentronik_konfigurasi_kelas_jadwal_rutin.Envelope.class,
                                        schemaRegistryUrl),
                        properties));

        classStream.addSink(ClassESsink.build()).name(Namespace.CLASS_ES_SINK);

        /* Kafka topic for PRESENTRONIK session cache initialization */
        DataStream<org.datastream.khodza.model.avro.
                p_presentronik_cache_kelas_sesi_perkuliahan.Envelope> sessionStream = env.addSource(
                new FlinkKafkaConsumer<>(
                        parameter.get("kafka.session.topic"),
                        ConfluentRegistryAvroDeserializationSchema
                                .forSpecific(org.datastream.khodza.model.avro.
                                                p_presentronik_cache_kelas_sesi_perkuliahan.Envelope.class,
                                        schemaRegistryUrl),
                        properties));

        sessionStream.addSink(SessionESsink.build()).name(Namespace.SESSION_ES_SINK);


        /* Kafka topic for PRESENTRONIK absent log initialization */
        DataStream<org.datastream.khodza.model.avro.
                p_presentronik_cache_kelas_sesi_perkuliahan_mahasiswa_peserta.Envelope> absentStream = env.addSource(
                new FlinkKafkaConsumer<>(
                        parameter.get("kafka.absent.topic"),
                        ConfluentRegistryAvroDeserializationSchema
                                .forSpecific(org.datastream.khodza.model.avro.
                                                p_presentronik_cache_kelas_sesi_perkuliahan_mahasiswa_peserta.Envelope.class,
                                        schemaRegistryUrl),
                        properties));

        SingleOutputStreamOperator<org.datastream.khodza.model.avro.
                p_presentronik_cache_kelas_sesi_perkuliahan_mahasiswa_peserta.Value> absentTransform = absentStream
                .assignTimestampsAndWatermarks(new AscendingTimestampExtractor<org.datastream.khodza.model.avro.
                        p_presentronik_cache_kelas_sesi_perkuliahan_mahasiswa_peserta.Envelope>() {

                    @Override
                    public long extractAscendingTimestamp(org.datastream.khodza.model.avro.p_presentronik_cache_kelas_sesi_perkuliahan_mahasiswa_peserta.Envelope element) {
                        return element.getTsMs();
                    }
                })
                .map( k -> k.getAfter());

        /* Enrich log data with user dimension in Elastic Search */
        DataStream<Tuple2<org.datastream.khodza.model.avro.p_presentronik_cache_kelas_sesi_perkuliahan_mahasiswa_peserta.Value,
                JSONObject>> sessionEnriched = AsyncDataStream
                .unorderedWait(absentTransform, new SessionAsyncEsDataRequest(), 2, TimeUnit.SECONDS, 100);

        DataStream<Tuple2<Tuple2<org.datastream.khodza.model.avro.p_presentronik_cache_kelas_sesi_perkuliahan_mahasiswa_peserta.Value,
                JSONObject>, JSONObject>> classEnriched = AsyncDataStream
                .unorderedWait(sessionEnriched, new ClassAsyncEsDataRequest(), 2, TimeUnit.SECONDS, 100);


        DataStream<InfluxDBPoint> influxStream = classEnriched.map(new PresentronikInfluxMap());

        Properties producerProperties = new Properties();
        producerProperties.setProperty(Config.BOOTSTRAP_SERVERS,parameter.get(Config.PRODUCER_BOOTSTRAP_SERVERS));
        producerProperties.setProperty(Config.MAX_REQUEST_SIZE,parameter.get(Config.MAX_REQUEST_SIZE));

        FlinkKafkaProducer producer = new FlinkKafkaProducer<String>(
                parameter.get("kafka.sink.topic"),
                new SimpleStringSchema(),producerProperties);
        producer.setWriteTimestampToKafka(true);

        classEnriched.map(new PresentronikKafkaMap()).addSink(producer);

        InfluxDBConfig influxDBConfig = new InfluxDBConfigFactory(parameter).getInfluxConfig();
        influxStream.addSink(new InfluxDBSink(influxDBConfig));



        // execute program
        env.execute(Namespace.PRESENTRONIK_STREAM_JOB);

    }
}
