# Khodza: Real Time Stream Aggregator
The use-cases of real time data analytics  needs a real-time data processing service. Khodza is a real time stream aggregator built on top of Apache Flink. 
Currently Khodza accepts these data sources:
- Apache Kafka
- Elasticsearch 6+

Khodza sinks its aggregation results into these outputs:
- Apache Kafka
- InfluxDB
- Elasticsearch 6+

Environment Properties:
Global example
- `environment.properties`
Job specific examples
- `scele.namespace.properties.example`
- `presentronik.properties.example`

To compile:
```$xslt
mvn clean package
```

To run:
```
<target execution class> <target job properties>
```

for example:

```
org.datastream.khodza.job.Scele scele.s1.properties
```

