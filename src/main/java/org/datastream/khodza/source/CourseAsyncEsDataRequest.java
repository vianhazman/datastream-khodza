package org.datastream.khodza.source;

import org.apache.flink.api.java.tuple.Tuple2;
import org.datastream.khodza.model.avro.t_scele_public_mdl_logstore_standard_log.LogValue;

public class CourseAsyncEsDataRequest extends MyAsyncEsDataRequest<Tuple2<LogValue, Tuple2<String,String>>,
        Tuple2<String,String>>{

    public CourseAsyncEsDataRequest() {
        super();
        super.setTypeOfOut(Tuple2.class.getSimpleName());
        super.setEsIndexName("es.course.index");
        super.setFieldName("courseid");
        super.setLookupKeyAttrName("id");
        super.setLookupResultAttrName("fullname");
        super.setQueryTerm("id");
    }

}
