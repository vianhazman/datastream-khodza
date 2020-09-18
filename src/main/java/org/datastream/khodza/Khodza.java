package org.datastream.khodza;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.datastream.khodza.job.Job;
import org.datastream.khodza.util.constants.Config;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Khodza {

	public static void main(String[] args) throws Exception {

		final StreamExecutionEnvironment env = StreamExecutionEnvironment
				.getExecutionEnvironment();


		InputStream global = Khodza.class.getClassLoader()
				.getResourceAsStream(Config.ENVIRONMENT_PROPERTIES);
		InputStream job = Khodza.class.getClassLoader()
				.getResourceAsStream(args[1]);
		ParameterTool parameter = ParameterTool.fromPropertiesFile(global)
				.mergeWith(ParameterTool
						.fromPropertiesFile(job));

		env.getConfig().setGlobalJobParameters(parameter);

		Properties properties = new Properties();
		properties.put(Config.GROUP_ID, parameter.get(Config.GROUP_ID));
		properties.put(Config.BOOTSTRAP_SERVERS, parameter.get(Config.BOOTSTRAP_SERVERS));
		properties.put(Config.AUTO_OFFSET_RESET, parameter.get(Config.AUTO_OFFSET_RESET));

		String schemaRegistryUrl = parameter.get(Config.SCHEMA_REGISTRY_URL);



		env.enableCheckpointing();

		env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
				10, // number of restart attempts
				Time.of(30, TimeUnit.SECONDS) // delay
		));

		Job executionClass = (Job) Class.forName(args[0]).newInstance();



		executionClass.run(properties, schemaRegistryUrl, env, parameter); }
	}

