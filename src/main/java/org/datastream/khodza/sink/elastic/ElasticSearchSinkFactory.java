package org.datastream.khodza.sink.elastic;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink;
import org.apache.http.HttpHost;
import org.datastream.khodza.util.GetMethod;
import org.datastream.khodza.util.constants.Config;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Requests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElasticSearchSinkFactory<IN> {

    ElasticsearchSink.Builder<IN> esSinkBuilder;

    public ElasticSearchSinkFactory(MapFunction<Object, Map> mapper, String indexName, String idField, ParameterTool parameter) throws Exception {

        // Set ES HTTP Host
        List<HttpHost> httpHosts = new ArrayList<>();
        httpHosts.add(new HttpHost(parameter.get(Config.ES_HOSTNAME),
                Integer.parseInt(parameter.get(Config.ES_PORT)),
                parameter.get(Config.ES_SCHEME)));

        // use a ElasticsearchSink.Builder to create an ElasticsearchSink
        esSinkBuilder = new ElasticsearchSink.Builder<>(
                httpHosts,
                (IN element, RuntimeContext ctx, RequestIndexer indexer) -> {
                    try {
                        System.out.println(element);
                        Object obj = GetMethod.getModelValue(element, "after");
                        System.out.println(obj);
                        if (obj != null) {
                            indexer.add(createIndexRequest(obj, indexName, idField, mapper));
                            indexer.add(createUpdateRequest(obj, indexName, idField, mapper));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
        );

        // configuration for the bulk requests; this instructs the sink to emit after every element, otherwise they would be buffered
        esSinkBuilder.setBulkFlushMaxActions(1000);

    }


    public ElasticsearchSink.Builder<IN> getElasticSearchSinkClient() {
        return esSinkBuilder;
    }


    public static IndexRequest createIndexRequest(Object element, String indexName, String fieldName, MapFunction<Object, Map> mapper) throws Exception {

        Map json = mapper.map(element);

        return Requests.indexRequest()
                .index(indexName)
                .type("1")
                .id(GetMethod.getModelValue(element, fieldName).toString())
                .source(json);
    }

    public static UpdateRequest createUpdateRequest(Object element, String indexName, String fieldName, MapFunction<Object, Map> mapper) throws Exception {

        Map json = mapper.map(element);

        return new UpdateRequest(
                indexName,
                "1",
                GetMethod.getModelValue(element, fieldName).toString())
                .doc(json)
                .upsert(json);
    }

    public static Map requestMapper(Object element) {
        Map<String, Object> json = new HashMap<>();
        return json;
    }
}