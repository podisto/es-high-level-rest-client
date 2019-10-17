package com.simba.eshighlevelrestclient.domain;

import lombok.Data;

import java.util.List;

/**
 * @author <a href="mailto:ElHadjiOmar.DIONE@orange-sonatel.com">podisto</a>
 * @since 2019-10-17
 */
@Data
public class ProfileDocument {
    private String id;
    private String firstName;
    private String lastName;
    private List<Technologies> technologies;
    private List<String> emails;
}
