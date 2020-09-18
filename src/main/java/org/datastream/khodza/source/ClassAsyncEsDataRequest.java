package org.datastream.khodza.source;

import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.java.tuple.Tuple2;
import org.datastream.khodza.model.avro.p_presentronik_cache_kelas_sesi_perkuliahan_mahasiswa_peserta.Value;

public class ClassAsyncEsDataRequest extends MyAsyncEsDataRequest<Tuple2<Value,
        JSONObject>, JSONObject>{

    public ClassAsyncEsDataRequest() {
        super();
        super.setTypeOfOut(JSONObject.class.getSimpleName());
        super.setEsIndexName("es.class.index");
        super.setFieldName("idkuliah");
        super.setLookupKeyAttrName("kd_kelas");
        super.setLookupResultAttrName("kd_kelas");
        super.setQueryTerm("kd_kelas");
    }

}