package com.simba.eshighlevelrestclient.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static java.util.Optional.ofNullable;

/**
 * @author <a href="mailto:ElHadjiOmar.DIONE@orange-sonatel.com">podisto</a>
 * @since 2019-10-17
 */
@Component
@Slf4j
public class ElasticSearchClient {
    @Value("${es.host}")
    private String esHost;
    @Value("${es.port}")
    private int esPort;
    @Value("${es.protocol}")
    private String esProtocol;
    @Value("${es.timeout}")
    private int timeout;

    private RestHighLevelClient client;

    public RestHighLevelClient getClient() {
        client = ofNullable(client)
                .orElse(new RestHighLevelClient(
                        RestClient.builder(new HttpHost(esHost, esPort, esProtocol))
                                .setRequestConfigCallback(config -> config
                                        .setConnectTimeout(timeout)
                                        .setConnectionRequestTimeout(timeout)
                                        .setSocketTimeout(timeout))
                ));
        log.info("{} ES Client built {} ", ElasticSearchClient.class.getSimpleName(), client);
        return client;
    }
}
