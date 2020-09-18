package org.datastream.khodza.source;

import com.alibaba.fastjson.JSONObject;
import org.datastream.khodza.model.avro.p_presentronik_cache_kelas_sesi_perkuliahan_mahasiswa_peserta.Value;

public class SessionAsyncEsDataRequest extends MyAsyncEsDataRequest<Value, JSONObject>{

    public SessionAsyncEsDataRequest() {
        super();
        super.setTypeOfOut(JSONObject.class.getSimpleName());
        super.setEsIndexName("es.session.index");
        super.setFieldName("idkuliah");
        super.setLookupKeyAttrName("id_kuliah");
        super.setLookupResultAttrName("kd_kelas");
        super.setQueryTerm("id_kuliah");
    }

}