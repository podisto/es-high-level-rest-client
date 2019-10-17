package com.simba.eshighlevelrestclient.resources;

import com.simba.eshighlevelrestclient.domain.ProfileDocument;
import com.simba.eshighlevelrestclient.services.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:ElHadjiOmar.DIONE@orange-sonatel.com">podisto</a>
 * @since 2019-10-17
 */
@RestController
@RequestMapping("/api/v1/profiles")
@Slf4j
public class ProfileResource {

    private final ProfileService service;
    private static final String CLASS_NAME = ProfileResource.class.getSimpleName();

    public ProfileResource(ProfileService profileService) {
        this.service = profileService;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody ProfileDocument document) {
        log.info("{} create document with request {} ", CLASS_NAME, document);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.createProfileDocument(document));
        } catch (Exception e) {
            return ResponseEntity.status( HttpStatus.NO_CONTENT).body("ERROR");
        }
    }

    @GetMapping
    public ResponseEntity<List<ProfileDocument>> findAll() {
        log.info("{} get all profiles ", CLASS_NAME);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ArrayList<>());
        }
    }
}
