package com.simba.eshighlevelrestclient.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simba.eshighlevelrestclient.client.ElasticSearchClient;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author <a href="mailto:ElHadjiOmar.DIONE@orange-sonatel.com">podisto</a>
 * @since 2019-10-17
 */
@Component
@Slf4j
public class ESDataAccess<T> implements DataAccess<T> {

    private static final String CLASS_NAME = ESDataAccess.class.getSimpleName();

    private final ElasticSearchClient esClient;
    private final ObjectMapper mapper;

    public ESDataAccess(ElasticSearchClient esClient, ObjectMapper mapper) {
        this.esClient = esClient;
        this.mapper = mapper;
    }

    @Override
    public IndexResponse index(String index, String type, T document, String id) throws IOException {
        log.info("=== {} create Index {} with index {}, type {} === ", CLASS_NAME, document, index, type);
        IndexRequest indexRequest = new IndexRequest(index, type, id).source(convertProfileDocumentToMap(document), XContentType.JSON);
        return esClient.getClient().index(indexRequest, RequestOptions.DEFAULT);
    }

    @Override
    public SearchResponse query(String index, String type, QueryBuilder queryBuilder) throws IOException {
        log.info("=== {} query === ", CLASS_NAME);
        SearchRequest searchRequest = buildSearchRequest(index, type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
        log.info("=== searchRequest built {} === ", searchRequest);
        return esClient.getClient().search(searchRequest, RequestOptions.DEFAULT);
    }

    private SearchRequest buildSearchRequest(String index, String type) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);
        searchRequest.types(type);
        searchRequest.indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
        return searchRequest;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> convertProfileDocumentToMap(T document) {
        return mapper.convertValue(document, Map.class);
    }
}
