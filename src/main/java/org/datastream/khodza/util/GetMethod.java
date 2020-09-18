package org.datastream.khodza.util;

public class GetMethod {

    public static final String GET = "get";
    public static final String F0 = "f0";
    public static final String TUPLE = "Tuple";

    public static Object getModelValue(Object obj, String fieldName) throws Exception{
        java.lang.reflect.Method method;
        java.lang.reflect.Field field;
        try {
            if (obj.getClass().getSimpleName().contains(TUPLE)) {
                obj = obj.getClass().getField(F0).get(obj);
            }
            method = obj.getClass().getMethod(GET, String.class);
            return method.invoke(obj, fieldName);
        } catch (NullPointerException e) {
            return null;
        }


    }

}
