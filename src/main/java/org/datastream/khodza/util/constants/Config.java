package org.datastream.khodza.util.constants;

public class Config {
    public static final String ENVIRONMENT_PROPERTIES = "environment.properties";
    //    Kafka Constants
    public static final String BOOTSTRAP_SERVERS = "bootstrap.servers";
    public static final String GROUP_ID = "group.id";
    public static final String AUTO_OFFSET_RESET = "auto.offset.reset";
    public static final String SCHEMA_REGISTRY_URL = "schema.registry.url";
    public static final String MAX_REQUEST_SIZE = "max.request.size";
    public static final String PRODUCER_BOOTSTRAP_SERVERS = "producer.bootstrap.servers";

    //    Elasticsearch Constants
    public static final String ES_HOSTNAME = "es.hostname" ;
    public static final String ES_PORT = "es.port" ;
    public static final String ES_SCHEME = "es.scheme" ;
//    InfluxDB Constants
    public static final String INFLUX_URL = "influx.url" ;
    public static final String INFLUX_USERNAME = "influx.username" ;
    public static final String INFLUX_PASSWORD = "influx.password" ;
    public static final String INFLUX_DATABASE = "influx.database" ;

}
