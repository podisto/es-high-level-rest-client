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

    @SuppressWarnings("unchecked")
    @Override
    public IndexResponse index(String index, String type, T document, String id) throws IOException {
        log.info("=== {} create Index {} with index {}, type {} === ", CLASS_NAME, document, index, type);
        Map<String, Object> documentMapper = mapper.convertValue(document, Map.class);
        IndexRequest indexRequest = new IndexRequest(index, type, id).source(documentMapper);
        return esClient.getClient().index(indexRequest, RequestOptions.DEFAULT);
    }

    @Override
    public SearchResponse query(String index, String type, QueryBuilder queryBuilder) throws IOException {
        log.info("=== {} query === ", CLASS_NAME);
        SearchRequest searchRequest = buildSearchRequest(index, type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
        log.info("=== searchRequest built {} === ", searchRequest);
        return esClient.getClient().search(searchRequest, RequestOptions.DEFAULT);
    }

    /*public SearchResponse searchByTechnology(String technology) throws IOException {
        SearchRequest searchRequest = buildSearchRequest(index, type);
        SearchSourceBuilder source = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("technologies.name", technology));
        source.query(QueryBuilders.nestedQuery("technologies", queryBuilder, ScoreMode.None));
        searchRequest.source(source);
        log.info("{} searchRequest = {} ", CLASS_NAME, searchRequest);
       return esClient.getClient().search(searchRequest, RequestOptions.DEFAULT);
    }*/

    private SearchRequest buildSearchRequest(String index, String type) {
        return new SearchRequest()
                .indices(index)
                .types(type)
                .indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
    }
}
