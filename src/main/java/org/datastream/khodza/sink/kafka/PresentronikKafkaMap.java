package org.datastream.khodza.sink.kafka;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.datastream.khodza.model.avro.p_presentronik_cache_kelas_sesi_perkuliahan_mahasiswa_peserta.Value;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;

public class PresentronikKafkaMap extends RichMapFunction<Tuple2<Tuple2<Value,
        JSONObject>, JSONObject>, String> {



    @Override
    public String map (Tuple2<Tuple2<Value,
            JSONObject>, JSONObject> s)  {

        Value absentFact = s.f0.f0;
        JSONObject dimSession = s.f0.f1;
        JSONObject dimClass = s.f1;

        HashMap<String, Object> map = new HashMap<>();

        map.put("npm", absentFact.getNpm().toString());
        map.put("id_kuliah", absentFact.getIdKuliah().toString());
        map.put("nama_mk", dimClass.getString("nama_mk"));
        map.put("aktif", dimClass.getString("aktif"));
        map.put("kd_org", absentFact.getKdOrg().toString());
        map.put("npm", absentFact.getNpm());
        map.put("id_kuliah", absentFact.getIdKuliah());
        map.put("nama_mahasiswa", absentFact.getNamaMahasiswa());
        map.put("status", absentFact.getStatusPresensi());
        map.put("kd_org", absentFact.getKdOrg());
        map.put("aktif", dimClass.getString("aktif"));
        map.put("kd_kelas", dimClass.getString("kd_kelas"));
        map.put("nama_mk", dimClass.getString("nama_mk"));
        map.put("term", dimClass.getString("term"));
        map.put("tahun", dimClass.getString("tahun"));
        map.put("kd_mk", dimClass.getString("kd_mk"));

        /* Date Fields */
        Long wkt_mulai = dimSession.getLong("wkt_mulai");
        Long wkt_selesai = dimSession.getLong("wkt_selesai");

        map.put("wkt_mulai", wkt_mulai == null ? null : new Date(wkt_mulai/1000).toString());
        map.put("wkt_selesai", wkt_selesai == null ? null : new Date(wkt_selesai/1000).toString());

        map.forEach((k, v) -> map.put(k, v == null ? "" : v.toString()));

        Gson gson = new Gson();
        Type gsonType = new TypeToken<HashMap>(){}.getType();
        String json = gson.toJson(map,gsonType);

        return json;
    };

}
