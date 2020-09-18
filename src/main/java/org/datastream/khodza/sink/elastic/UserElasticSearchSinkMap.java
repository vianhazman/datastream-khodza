package org.datastream.khodza.sink.elastic;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.datastream.khodza.model.avro.t_scele_public_mdl_user.UserValue;

import java.util.HashMap;
import java.util.Map;

public class UserElasticSearchSinkMap extends RichMapFunction<Object, Map> {
    @Override
    public Map map(Object obj) {
        UserValue val = (UserValue) obj;
        Map<String, Object> json = new HashMap<>();
        json.put("id", val.getId().toString());
        json.put("uid", val.getUsername().toString());
        json.put("npm", val.getIdnumber().toString());
        json.put("@timestamp", new java.util.Date());
        return json;
    }
}
