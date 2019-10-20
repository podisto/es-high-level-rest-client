package com.simba.eshighlevelrestclient.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simba.eshighlevelrestclient.domain.ProfileDocument;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author <a href="mailto:ElHadjiOmar.DIONE@orange-sonatel.com">podisto</a>
 * @since 2019-10-17
 */
@Service
@Slf4j
public class ProfileService {

    private static final String CLASS_NAME = ProfileService.class.getSimpleName();

    private final DataAccess dataAccess;
    private final ObjectMapper mapper;

    @Value("${es.indexes.profile.index}")
    private String index;
    @Value("${es.indexes.profile.type}")
    private String type;

    public ProfileService(DataAccess dataAccess, ObjectMapper mapper) {
        this.dataAccess = dataAccess;
        this.mapper = mapper;
    }

    @SuppressWarnings("unchecked")
    public String createProfileDocument(ProfileDocument document) throws IOException {
        log.info("=== {} with request {} === ", CLASS_NAME, document);
        UUID uuid = UUID.randomUUID();
        document.setId(uuid.toString());
        IndexResponse indexResponse = dataAccess.index(this.index, type, document, document.getId());
        log.info("=== {} IndexResponse = {} === ", CLASS_NAME, indexResponse);
        return indexResponse.getResult().name();
    }

    public List<ProfileDocument> findAll() throws IOException {
        log.info("=== {} get all profile with index = {} type = {} === ", CLASS_NAME, index, type);
        SearchResponse searchResponse = dataAccess.query(index, type, QueryBuilders.matchAllQuery());
        log.info("=== {} response {} === ", CLASS_NAME, searchResponse);
        return getSearchResult(searchResponse);
    }

    public List<ProfileDocument> findByTechnology(String technology) throws IOException {
        log.info("=== {} search profiles by {} === ", CLASS_NAME);
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("technologies.name", technology));
        NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("technologies", queryBuilder, ScoreMode.None);
        SearchResponse searchResponse = dataAccess.query(index, type, nestedQuery);
        log.info("=== response = {} === ", searchResponse);
        return getSearchResult(searchResponse);
    }

    private List<ProfileDocument> getSearchResult(SearchResponse response) {
        SearchHit[] searchHit = response.getHits().getHits();
        List<ProfileDocument> profileDocuments = new ArrayList<>();
        if (searchHit.length > 0) {
            Arrays.stream(searchHit)
                    .forEach(hit -> profileDocuments.add(mapper.convertValue(hit.getSourceAsMap(), ProfileDocument.class)));
        }
        return profileDocuments;
    }
}
