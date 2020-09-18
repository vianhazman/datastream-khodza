package org.datastream.khodza.job;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Properties;

public interface Job {
    public void run(
            Properties properties,
            String schemaRegistryUrl,
            StreamExecutionEnvironment env,
            ParameterTool parameter) throws Exception;

}
