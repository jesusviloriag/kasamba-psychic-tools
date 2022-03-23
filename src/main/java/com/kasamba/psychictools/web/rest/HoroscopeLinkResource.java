package com.kasamba.psychictools.web.rest;

import com.kasamba.psychictools.domain.FortuneCookie;
import com.kasamba.psychictools.domain.HoroscopeLink;
import com.kasamba.psychictools.repository.HoroscopeLinkRepository;
import com.kasamba.psychictools.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.kasamba.psychictools.domain.HoroscopeLink}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HoroscopeLinkResource {

    private final Logger log = LoggerFactory.getLogger(HoroscopeLinkResource.class);

    private static final String ENTITY_NAME = "horoscopeLink";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HoroscopeLinkRepository horoscopeLinkRepository;

    public HoroscopeLinkResource(HoroscopeLinkRepository horoscopeLinkRepository) {
        this.horoscopeLinkRepository = horoscopeLinkRepository;
    }

    /**
     * {@code POST  /horoscope-links} : Create a new horoscopeLink.
     *
     * @param horoscopeLink the horoscopeLink to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new horoscopeLink, or with status {@code 400 (Bad Request)} if the horoscopeLink has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/horoscope-links")
    public ResponseEntity<HoroscopeLink> createHoroscopeLink(@Valid @RequestBody HoroscopeLink horoscopeLink) throws URISyntaxException {
        log.debug("REST request to save HoroscopeLink : {}", horoscopeLink);
        if (horoscopeLink.getId() != null) {
            throw new BadRequestAlertException("A new horoscopeLink cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HoroscopeLink result = horoscopeLinkRepository.save(horoscopeLink);
        return ResponseEntity
            .created(new URI("/api/horoscope-links/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /horoscope-links/:id} : Updates an existing horoscopeLink.
     *
     * @param id the id of the horoscopeLink to save.
     * @param horoscopeLink the horoscopeLink to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated horoscopeLink,
     * or with status {@code 400 (Bad Request)} if the horoscopeLink is not valid,
     * or with status {@code 500 (Internal Server Error)} if the horoscopeLink couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/horoscope-links/{id}")
    public ResponseEntity<HoroscopeLink> updateHoroscopeLink(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HoroscopeLink horoscopeLink
    ) throws URISyntaxException {
        log.debug("REST request to update HoroscopeLink : {}, {}", id, horoscopeLink);
        if (horoscopeLink.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, horoscopeLink.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!horoscopeLinkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HoroscopeLink result = horoscopeLinkRepository.save(horoscopeLink);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, horoscopeLink.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /horoscope-links/:id} : Partial updates given fields of an existing horoscopeLink, field will ignore if it is null
     *
     * @param id the id of the horoscopeLink to save.
     * @param horoscopeLink the horoscopeLink to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated horoscopeLink,
     * or with status {@code 400 (Bad Request)} if the horoscopeLink is not valid,
     * or with status {@code 404 (Not Found)} if the horoscopeLink is not found,
     * or with status {@code 500 (Internal Server Error)} if the horoscopeLink couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/horoscope-links/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HoroscopeLink> partialUpdateHoroscopeLink(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HoroscopeLink horoscopeLink
    ) throws URISyntaxException {
        log.debug("REST request to partial update HoroscopeLink partially : {}, {}", id, horoscopeLink);
        if (horoscopeLink.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, horoscopeLink.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!horoscopeLinkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HoroscopeLink> result = horoscopeLinkRepository
            .findById(horoscopeLink.getId())
            .map(existingHoroscopeLink -> {
                if (horoscopeLink.getUrl() != null) {
                    existingHoroscopeLink.setUrl(horoscopeLink.getUrl());
                }

                return existingHoroscopeLink;
            })
            .map(horoscopeLinkRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, horoscopeLink.getId().toString())
        );
    }

    /**
     * {@code GET  /horoscope-links} : get all the horoscopeLinks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of horoscopeLinks in body.
     */
    @GetMapping("/horoscope-links")
    public List<HoroscopeLink> getAllHoroscopeLinks() {
        log.debug("REST request to get all HoroscopeLinks");
        return horoscopeLinkRepository.findAll();
    }

    /**
     * {@code GET  /horoscope-links/:id} : get the "id" horoscopeLink.
     *
     * @param id the id of the horoscopeLink to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the horoscopeLink, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/horoscope-links/{id}")
    public ResponseEntity<HoroscopeLink> getHoroscopeLink(@PathVariable Long id) {
        log.debug("REST request to get HoroscopeLink : {}", id);
        Optional<HoroscopeLink> horoscopeLink = horoscopeLinkRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(horoscopeLink);
    }

    /**
     * {@code DELETE  /horoscope-links/:id} : delete the "id" horoscopeLink.
     *
     * @param id the id of the horoscopeLink to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/horoscope-links/{id}")
    public ResponseEntity<Void> deleteHoroscopeLink(@PathVariable Long id) {
        log.debug("REST request to delete HoroscopeLink : {}", id);
        horoscopeLinkRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /horoscope-daily/:sign : get the "sign" horoscope.
     *
     * @param sign the sign of the horoscope.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fortuneCookie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/horoscope-daily/{sign}")
    public ResponseEntity<FortuneCookie> getFortuneCookieRandom(@PathVariable String sign) throws Exception {
        log.debug("REST request to get Horoscope for : {}", sign);

        Optional<FortuneCookie> fortuneCookie = null;
        HoroscopeLink horoscopeLink = horoscopeLinkRepository.findAll().get(horoscopeLinkRepository.findAll().size() - 1);

        if (horoscopeLink != null) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                .newBuilder()
                .uri(new URI(horoscopeLink.getUrl().replace("@sign", sign)))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            FortuneCookie fortuneCookie1 = new FortuneCookie();
            fortuneCookie1.setText(response.body().split("\"description\": \"")[1].split("\", ")[0]);
            fortuneCookie = Optional.ofNullable(fortuneCookie1);
        }

        return ResponseUtil.wrapOrNotFound(fortuneCookie);
    }
}
