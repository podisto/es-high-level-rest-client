package com.simba.eshighlevelrestclient.services;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;

import java.io.IOException;

/**
 * @author <a href="mailto:ElHadjiOmar.DIONE@orange-sonatel.com">podisto</a>
 * @since 2019-10-17
 */
public interface DataAccess<T> {
    IndexResponse index(String index, String type, T document, String id) throws IOException;

    SearchResponse query(String index, String type, QueryBuilder queryBuilder) throws IOException;
}
