package org.datastream.khodza.sink.influx;

import org.apache.flink.api.java.utils.ParameterTool;
import org.datastream.khodza.util.constants.Config;

import java.util.concurrent.TimeUnit;

public class InfluxDBConfigFactory {

    org.apache.flink.streaming.connectors.influxdb.InfluxDBConfig influxClient;

    public InfluxDBConfigFactory(ParameterTool parameter) throws Exception
    {
        this.influxClient = org.apache.flink.streaming.connectors.influxdb.InfluxDBConfig.builder(
                parameter.get(Config.INFLUX_URL),
                parameter.get(Config.INFLUX_USERNAME),
                parameter.get(Config.INFLUX_PASSWORD),
                parameter.get("influx.sink.database")
                 )
                .batchActions(1000)
                .flushDuration(100, TimeUnit.MILLISECONDS)
                .enableGzip(true)
                .build();
    }

    public org.apache.flink.streaming.connectors.influxdb.InfluxDBConfig getInfluxConfig() {
        return influxClient;
    }

}
