package com.simba.eshighlevelrestclient.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simba.eshighlevelrestclient.domain.ProfileDocument;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
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

    private final DataAccess dataAccess;
    private final ObjectMapper mapper;
    private static final String CLASS_NAME = ProfileService.class.getSimpleName();

    public ProfileService(DataAccess dataAccess, ObjectMapper mapper) {
        this.dataAccess = dataAccess;
        this.mapper = mapper;
    }

    public String createProfileDocument(ProfileDocument document) throws IOException {
        log.info("{} with request {} ", CLASS_NAME, document);
        UUID uuid = UUID.randomUUID();
        document.setId(uuid.toString());
        String response = dataAccess.createProfile(document);
        log.info("{} response = {} ", CLASS_NAME, response);
        return response;
    }

    public List<ProfileDocument> findAll() throws IOException {
        log.info("{} get all profile ", CLASS_NAME);
        SearchResponse searchResponse = dataAccess.query();
        log.info("{} response {} ", CLASS_NAME, searchResponse);
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
