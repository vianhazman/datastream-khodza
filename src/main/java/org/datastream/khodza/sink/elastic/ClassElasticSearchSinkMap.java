package org.datastream.khodza.sink.elastic;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.datastream.khodza.model.avro.p_presentronik_konfigurasi_kelas_jadwal_rutin.Value;

import java.util.HashMap;
import java.util.Map;

public class ClassElasticSearchSinkMap extends RichMapFunction<Object, Map> {
    @Override
    public Map map(Object obj) {
        Value val = (Value) obj;
        Map<String, Object> json = new HashMap<>();
        json.put("aktif", val.getAktif());
        json.put("kd_kelas", val.getKdKelas());
        json.put("kd_mk", val.getKdMk());
        json.put("kd_org", val.getKdOrg());
        json.put("nama_kelas", val.getNamaKelas());
        json.put("nama_mk", val.getNamaMk());
        json.put("term", val.getTerm());
        json.put("tahun",val.getThn());
        json.put("ts_update", val.getTsUpdate());

        json.forEach((k, v) -> json.put(k, v == null ? "" : v.toString()));
        Iterables.removeIf(json.values(), Predicates.isNull());

        return json;
    }
}
