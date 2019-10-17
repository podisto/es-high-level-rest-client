package com.simba.eshighlevelrestclient.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simba.eshighlevelrestclient.client.ElasticSearchClient;
import com.simba.eshighlevelrestclient.domain.ProfileDocument;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @author <a href="mailto:ElHadjiOmar.DIONE@orange-sonatel.com">podisto</a>
 * @since 2019-10-17
 */
@Service
@Slf4j
public class ESDataAccess implements DataAccess {
    @Value("${es.index}")
    private String index;
    @Value("${es.type}")
    private String type;

    private final ElasticSearchClient esClient;
    private final ObjectMapper mapper;

    public ESDataAccess(ElasticSearchClient esClient, ObjectMapper mapper) {
        this.esClient = esClient;
        this.mapper = mapper;
    }

    @Override
    public String createProfile(ProfileDocument document) throws IOException {
        log.info("{} create Index {} with index {}, type {} ", ESDataAccess.class.getSimpleName(), document, index, type);
        Map<String, Object> documentMapper = mapper.convertValue(document, Map.class);
        IndexRequest indexRequest = new IndexRequest(index, type, document.getId()).source(documentMapper);
        IndexResponse indexResponse = esClient.getClient().index(indexRequest, RequestOptions.DEFAULT);
        log.info("response {} ", indexResponse);
        return indexResponse.getResult().name();
    }

    @Override
    public SearchResponse query() throws IOException {
        log.info("query all profiles");
        SearchRequest searchRequest = buildSearchRequest(index, type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        return esClient.getClient().search(searchRequest, RequestOptions.DEFAULT);
    }

    private SearchRequest buildSearchRequest(String index, String type) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);
        searchRequest.types(type);
        return searchRequest;
    }
}
