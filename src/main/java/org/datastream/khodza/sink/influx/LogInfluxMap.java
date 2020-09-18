package org.datastream.khodza.sink.influx;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.connectors.influxdb.InfluxDBPoint;
import org.datastream.khodza.model.avro.t_scele_public_mdl_logstore_standard_log.LogValue;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

public class  LogInfluxMap extends RichMapFunction<Tuple2<Tuple2<LogValue, Tuple2<String,String>>, Tuple2<String,String>>, InfluxDBPoint> {

    String namespace;

    public LogInfluxMap(String namespace) {
        super();
        this.namespace = namespace;
    }

    @Override
    public InfluxDBPoint map(
            Tuple2<Tuple2<LogValue, Tuple2<String,String>>, Tuple2<String,String>> s) throws Exception {
        LogValue log_fact = s.f0.f0;
        Tuple2<String, String> dim_user = s.f0.f1;
        Tuple2<String, String> dim_course = s.f1;

        String measurement = "t-scele-log";
        long timestamp = log_fact.getTimecreated()*1000;

        HashMap<String, String> tags = new HashMap<>();
        tags.put("user_id", dim_user.f0);
        tags.put("user_name", dim_user.f1);
        tags.put("course_id", dim_course.f0);
        tags.put("course_name", dim_course.f1);
        tags.put("component", log_fact.getComponent().toString());
        tags.put("namespace",namespace);
        HashMap<String, Object> fields = new HashMap<>();
        fields.put("id", log_fact.getId());
        fields.put("name", log_fact.getEventname());
        fields.put("action", log_fact.getAction());
        fields.put("anonymous", log_fact.getAnonymous());
        fields.put("component", log_fact.getComponent());
        fields.put("context_id", log_fact.getContextid());
        fields.put("context_instance_id", log_fact.getContextinstanceid());
        fields.put("context_level", log_fact.getContextlevel());
        fields.put("crud", log_fact.getCrud().toString());
        fields.put("edu_level", log_fact.getEdulevel());
        fields.put("ip", log_fact.getIp());
        fields.put("object_id", log_fact.getObjectid());
        fields.put("object_table", log_fact.getObjecttable());
        fields.put("origin", log_fact.getOrigin());
        fields.put("other", log_fact.getOther());
        fields.put("related_user_id", log_fact.getRelateduserid());
        fields.put("time_created", new Date(timestamp));
        fields.put("target", log_fact.getTarget());
        fields.put("user_id", dim_user.f0);
        fields.put("user_name", dim_user.f1);
        fields.put("course_id", dim_course.f0);
        fields.put("course_name", dim_course.f1);
        fields.forEach((k, v) -> fields.put(k, v == null ? "" : v.toString()));
        Iterables.removeIf(tags.values(), Predicates.isNull());
        Iterables.removeIf(fields.values(), Predicates.isNull());

        //        Time offset metric
        Duration timeElapsed = Duration.between(Instant.ofEpochMilli(timestamp), Instant.now());
        fields.put("latency", timeElapsed.toMillis());

        return new InfluxDBPoint(measurement, timestamp, tags, fields);
    }
}
