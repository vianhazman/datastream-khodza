package org.datastream.khodza.sink.influx;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.connectors.influxdb.InfluxDBPoint;
import org.datastream.khodza.model.avro.p_presentronik_cache_kelas_sesi_perkuliahan_mahasiswa_peserta.Value;

import java.util.Date;
import java.util.HashMap;

public class PresentronikInfluxMap extends RichMapFunction<Tuple2<Tuple2<Value,
        JSONObject>, JSONObject>, InfluxDBPoint> {
    @Override
    public InfluxDBPoint map(
            Tuple2<Tuple2<Value,
                    JSONObject>, JSONObject> s) throws Exception {
        Value absentFact = s.f0.f0;
        JSONObject dimSession = s.f0.f1;
        JSONObject dimClass = s.f1;

        String measurement = "t-presentronik-main";
        long timestamp = absentFact.getTsUpdate()/1000;

        HashMap<String, String> tags = new HashMap<>();
        tags.put("npm", absentFact.getNpm().toString());
        tags.put("id_kuliah", absentFact.getIdKuliah().toString());
        tags.put("nama_mk", dimClass.getString("nama_mk"));
        tags.put("aktif", dimClass.getString("aktif"));
        tags.put("kd_org", absentFact.getKdOrg().toString());

        HashMap<String, Object> fields = new HashMap<>();
        fields.put("npm", absentFact.getNpm());
        fields.put("id_kuliah", absentFact.getIdKuliah());
        fields.put("nama_mahasiswa", absentFact.getNamaMahasiswa());
        fields.put("status", absentFact.getStatusPresensi());
        fields.put("kd_org", absentFact.getKdOrg());
        fields.put("aktif", dimClass.getString("aktif"));
        fields.put("kd_kelas", dimClass.getString("kd_kelas"));
        fields.put("nama_mk", dimClass.getString("nama_mk"));
        fields.put("term", dimClass.getString("term"));
        fields.put("tahun", dimClass.getString("tahun"));
        fields.put("kd_mk", dimClass.getString("kd_mk"));

         /* Date Fields */
        Long wkt_mulai = dimSession.getLong("wkt_mulai");
        Long wkt_selesai = dimSession.getLong("wkt_selesai");

        fields.put("wkt_mulai", wkt_mulai == null ? null : new Date(wkt_mulai/1000).toString());
        fields.put("wkt_selesai", wkt_selesai == null ? null : new Date(wkt_selesai/1000).toString());


        fields.forEach((k, v) -> fields.put(k, v == null ? "" : v.toString()));
        Iterables.removeIf(tags.values(), Predicates.isNull());
        Iterables.removeIf(fields.values(), Predicates.isNull());







        return new InfluxDBPoint(measurement, timestamp, tags, fields);
    }
}
