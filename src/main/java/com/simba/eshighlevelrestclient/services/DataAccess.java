package com.simba.eshighlevelrestclient.services;

import com.simba.eshighlevelrestclient.domain.ProfileDocument;
import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;

/**
 * @author <a href="mailto:ElHadjiOmar.DIONE@orange-sonatel.com">podisto</a>
 * @since 2019-10-17
 */
public interface DataAccess {
    String createProfile(ProfileDocument document) throws IOException;

    SearchResponse query() throws IOException;
}
