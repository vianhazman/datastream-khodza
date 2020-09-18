package org.datastream.khodza.source;

import org.apache.flink.api.java.tuple.Tuple2;
import org.datastream.khodza.model.avro.t_scele_public_mdl_logstore_standard_log.LogValue;

public class UserAsyncEsDataRequest extends MyAsyncEsDataRequest<LogValue, Tuple2<String,String>>{

    public UserAsyncEsDataRequest() {
        super();
        super.setTypeOfOut(Tuple2.class.getSimpleName());
        super.setEsIndexName("es.user.index");
        super.setFieldName("userid");
        super.setLookupKeyAttrName("uid");
        super.setLookupResultAttrName("npm");
        super.setQueryTerm("id");
    }

}