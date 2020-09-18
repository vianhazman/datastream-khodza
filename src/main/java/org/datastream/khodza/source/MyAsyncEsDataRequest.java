package org.datastream.khodza.source;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.apache.http.HttpHost;
import org.datastream.khodza.util.GetMethod;
import org.datastream.khodza.util.constants.Config;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class MyAsyncEsDataRequest<IN, OUT> extends RichAsyncFunction<IN,Tuple2<IN, OUT>> {

    public transient RestHighLevelClient restHighLevelClient;

    public transient volatile Cache<Object,OUT> cache;

    public String getTypeOfOut() {
        return typeOfOut;
    }

    public void setTypeOfOut(String typeOfOut) {
        this.typeOfOut = typeOfOut;
    }

    public String getLookupKeyAttrName() {
        return lookupKeyAttrName;
    }

    public void setLookupKeyAttrName(String lookupKeyAttrName) {
        this.lookupKeyAttrName = lookupKeyAttrName;
    }

    public String getLookupResultAttrName() {
        return lookupResultAttrName;
    }

    public void setLookupResultAttrName(String lookupResultAttrName) {
        this.lookupResultAttrName = lookupResultAttrName;
    }

    public String getEsIndexName() {
        return esIndexName;
    }

    public void setEsIndexName(String esIndexName) {
        this.esIndexName = esIndexName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getQueryTerm() {
        return queryTerm;
    }

    public void setQueryTerm(String queryTerm) {
        this.queryTerm = queryTerm;
    }

    public ParameterTool getParameterTool() {
        return parameterTool;
    }

    public void setParameterTool(ParameterTool parameterTool) {
        this.parameterTool = parameterTool;
    }

    private String lookupKeyAttrName;
    private String lookupResultAttrName;
    private String esIndexName;
    private String fieldName;
    private String queryTerm;
    private String typeOfOut;
    private ParameterTool parameterTool;

    @Override
    public void open(Configuration parameters) throws Exception {
        // Environment in memory static doesn't work when running in cluster (distributed)
        // Initialize ElasticSearch-Client
        ParameterTool parameter = (ParameterTool) getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
        setParameterTool(parameter);

        this.restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(getParameterTool().get(Config.ES_HOSTNAME),
                                Integer.parseInt(getParameterTool().get(Config.ES_PORT)),
                                getParameterTool().get(Config.ES_SCHEME))
                ));
        // Cache settings
        this.cache = CacheBuilder
                .newBuilder()
                .maximumSize(10)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();
    }



    @Override
    public void close() throws Exception {
        restHighLevelClient.close();
    }


    @Override
    public void asyncInvoke(IN input, ResultFuture<Tuple2<IN, OUT>> resultFuture)
            throws Exception {
        Object event = GetMethod.getModelValue(input, getFieldName());

        // If the cache exists, read the key directly from the cache
        OUT eventCache = cache.getIfPresent(event);
        if (eventCache != null) {
            resultFuture.complete(Collections.singleton(new Tuple2<IN, OUT>(input, eventCache)));
        } else {
            search(input, resultFuture);
        }

    }
         // Asynchronous to read the Es table

    private void search(IN input, ResultFuture<Tuple2<IN, OUT>> resultFuture) throws Exception {
        String OutClassName = this.typeOfOut;
        SearchRequest searchRequest = new SearchRequest(getParameterTool().get(getEsIndexName()));
        Object id = GetMethod.getModelValue(input, getFieldName());
        QueryBuilder builder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery(getQueryTerm(), id));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(builder);
        searchRequest.source(sourceBuilder);
        ActionListener<SearchResponse> listener = new ActionListener<SearchResponse>() {
            //success
            @Override
            @SuppressWarnings("unchecked")
            public void onResponse(SearchResponse searchResponse) {
                String firstAttr = null;
                String secondAttr = null;
                SearchHit[] searchHits = searchResponse.getHits().getHits();
                if (searchHits.length > 0) {
                    JSONObject jsonObject = JSONObject.parseObject(searchHits[0].getSourceAsString());
                    if (OutClassName.contains("Tuple")) {
                        firstAttr = jsonObject.getString(getLookupKeyAttrName());
                        secondAttr = jsonObject.getString(getLookupResultAttrName());
                        cache.put(id, (OUT) Tuple2.of(firstAttr,secondAttr));
                    } else {
                        cache.put(id, (OUT) jsonObject);
                    }

                }
                resultFuture.complete(Collections.singleton(new Tuple2<>(input, (OUT) Tuple2.of(firstAttr, secondAttr))));
            }

            //failure
            @Override
            @SuppressWarnings("unchecked")
            public void onFailure(Exception e) {
                if (OutClassName.contains("Tuple")) {
                    resultFuture.complete(Collections.singleton((Tuple2<IN, OUT>) new Tuple2<>(input, new Tuple2<>(null,null))));

                } else {
                    resultFuture.complete(Collections.singleton((Tuple2<IN, OUT>) new Tuple2<>(input, new JSONObject())));
                }
            }
        };
        restHighLevelClient.searchAsync(searchRequest, listener);
    }

}