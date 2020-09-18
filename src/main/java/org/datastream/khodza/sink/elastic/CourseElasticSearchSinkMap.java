package org.datastream.khodza.sink.elastic;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.datastream.khodza.model.avro.t_scele_public_mdl_course.Value;

import java.util.HashMap;
import java.util.Map;

public class CourseElasticSearchSinkMap  extends RichMapFunction<Object, Map> {
    @Override
    public Map map(Object obj) {
        Value val = (Value) obj;
        Map<String, Object> json = new HashMap<>();

        json.put("id", val.getId());
        json.put("fullname", val.getFullname().toString());
        json.put("time_modified", val.getTimemodified());
        json.put("@timestamp", new java.util.Date());
        return json;
    }
}
