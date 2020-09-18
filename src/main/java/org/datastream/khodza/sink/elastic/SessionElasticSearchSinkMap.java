package org.datastream.khodza.sink.elastic;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.datastream.khodza.model.avro.p_presentronik_cache_kelas_sesi_perkuliahan.Value;

import java.util.HashMap;
import java.util.Map;

public class SessionElasticSearchSinkMap extends RichMapFunction<Object, Map> {
    @Override
    public Map map(Object obj) {
        Value val = (Value) obj;
        Map<String, Object> json = new HashMap<>();

        json.put("id_kuliah", val.getIdKuliah());
        json.put("kd_kelas", val.getKdKelas());
        json.put("tanggal", val.getTanggal());
        json.put("wkt_mulai", val.getWktMulai());
        json.put("wkt_selesai", val.getWktSelesai());
        json.put("ts_update", val.getTsUpdate());

        json.forEach((k, v) -> json.put(k, v == null ? "" : v.toString()));
        Iterables.removeIf(json.values(), Predicates.isNull());

        return json;
    }
}
