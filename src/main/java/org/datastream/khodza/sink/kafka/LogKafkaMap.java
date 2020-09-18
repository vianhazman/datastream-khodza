package org.datastream.khodza.sink.kafka;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.datastream.khodza.model.avro.t_scele_public_mdl_logstore_standard_log.LogValue;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

public class LogKafkaMap extends RichMapFunction<Tuple2<Tuple2<LogValue, Tuple2<String,String>>, Tuple2<String,String>>, String> {

    String namespace;

    public LogKafkaMap(String namespace) {
        super();
        this.namespace = namespace;
    }

    @Override
    public String map (Tuple2<Tuple2<LogValue, Tuple2<String,String>>, Tuple2<String,String>> s) {

        LogValue log_fact = s.f0.f0;
        Tuple2<String, String> dim_user = s.f0.f1;
        Tuple2<String, String> dim_course = s.f1;

        HashMap<String, Object> map = new HashMap<>();
        long timestamp = log_fact.getTimecreated()*1000;

        map.put("namespace", this.namespace);
        map.put("user_id", dim_user.f0);
        map.put("user_name", dim_user.f1);
        map.put("course_id", dim_course.f0);
        map.put("course_name", dim_course.f1);
        map.put("id", log_fact.getId());
        map.put("name", log_fact.getEventname());
        map.put("action", log_fact.getAction());
        map.put("anonymous", log_fact.getAnonymous());
        map.put("component", log_fact.getComponent());
        map.put("context_id", log_fact.getContextid());
        map.put("context_instance_id", log_fact.getContextinstanceid());
        map.put("context_level", log_fact.getContextlevel());
        map.put("crud", log_fact.getCrud().toString());
        map.put("edu_level", log_fact.getEdulevel());
        map.put("ip", log_fact.getIp());
        map.put("object_id", log_fact.getObjectid());
        map.put("object_table", log_fact.getObjecttable());
        map.put("origin", log_fact.getOrigin());
        map.put("other", log_fact.getOther());
        map.put("related_user_id", log_fact.getRelateduserid());
        map.put("time_created", new Date(timestamp).toString());
        map.put("target", log_fact.getTarget());
        map.put("user_id", dim_user.f0);
        map.put("user_name", dim_user.f1);
        map.put("course_id", dim_course.f0);
        map.put("course_name", dim_course.f1);
        map.put("load_time", Instant.now().toEpochMilli());

        map.forEach((k, v) -> map.put(k, v == null ? "" : v.toString()));

        Gson gson = new Gson();
        Type gsonType = new TypeToken<HashMap>(){}.getType();
        String json = gson.toJson(map,gsonType);

        return json;
    };

}
