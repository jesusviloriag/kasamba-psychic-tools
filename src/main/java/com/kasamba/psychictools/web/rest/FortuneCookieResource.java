package com.kasamba.psychictools.web.rest;

import com.kasamba.psychictools.domain.FortuneCookie;
import com.kasamba.psychictools.repository.FortuneCookieRepository;
import com.kasamba.psychictools.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
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
 * REST controller for managing {@link com.kasamba.psychictools.domain.FortuneCookie}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FortuneCookieResource {

    private final Logger log = LoggerFactory.getLogger(FortuneCookieResource.class);

    private static final String ENTITY_NAME = "fortuneCookie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FortuneCookieRepository fortuneCookieRepository;

    public FortuneCookieResource(FortuneCookieRepository fortuneCookieRepository) {
        this.fortuneCookieRepository = fortuneCookieRepository;
    }

    /**
     * {@code POST  /fortune-cookies} : Create a new fortuneCookie.
     *
     * @param fortuneCookie the fortuneCookie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fortuneCookie, or with status {@code 400 (Bad Request)} if the fortuneCookie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fortune-cookies")
    public ResponseEntity<FortuneCookie> createFortuneCookie(@Valid @RequestBody FortuneCookie fortuneCookie) throws URISyntaxException {
        log.debug("REST request to save FortuneCookie : {}", fortuneCookie);
        if (fortuneCookie.getId() != null) {
            throw new BadRequestAlertException("A new fortuneCookie cannot already have an ID", ENTITY_NAME, "idexists");
        }

        FortuneCookie result = null;

        fortuneCookie.setText(fortuneCookie.getText().replace("\n", ""));

        if (fortuneCookie.getText().contains("\",")) {
            String[] fortuneCookies = fortuneCookie.getText().split("\",");
            for (String text : fortuneCookies) {
                FortuneCookie fortuneCookie1 = new FortuneCookie();
                String processedText = text.replaceAll("^\"|\"$", "");
                try {
                    processedText = processedText.split("\"")[1];
                    fortuneCookie1.setText(processedText);
                    result = fortuneCookieRepository.save(fortuneCookie1);
                } catch (Exception ex) {
                    fortuneCookie1.setText(processedText);
                    result = fortuneCookieRepository.save(fortuneCookie1);
                }
            }
        } else {
            result = fortuneCookieRepository.save(fortuneCookie);
        }

        return ResponseEntity
            .created(new URI("/api/fortune-cookies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fortune-cookies/:id} : Updates an existing fortuneCookie.
     *
     * @param id the id of the fortuneCookie to save.
     * @param fortuneCookie the fortuneCookie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fortuneCookie,
     * or with status {@code 400 (Bad Request)} if the fortuneCookie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fortuneCookie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fortune-cookies/{id}")
    public ResponseEntity<FortuneCookie> updateFortuneCookie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FortuneCookie fortuneCookie
    ) throws URISyntaxException {
        log.debug("REST request to update FortuneCookie : {}, {}", id, fortuneCookie);
        if (fortuneCookie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fortuneCookie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fortuneCookieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FortuneCookie result = fortuneCookieRepository.save(fortuneCookie);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fortuneCookie.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fortune-cookies/:id} : Partial updates given fields of an existing fortuneCookie, field will ignore if it is null
     *
     * @param id the id of the fortuneCookie to save.
     * @param fortuneCookie the fortuneCookie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fortuneCookie,
     * or with status {@code 400 (Bad Request)} if the fortuneCookie is not valid,
     * or with status {@code 404 (Not Found)} if the fortuneCookie is not found,
     * or with status {@code 500 (Internal Server Error)} if the fortuneCookie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fortune-cookies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FortuneCookie> partialUpdateFortuneCookie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FortuneCookie fortuneCookie
    ) throws URISyntaxException {
        log.debug("REST request to partial update FortuneCookie partially : {}, {}", id, fortuneCookie);
        if (fortuneCookie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fortuneCookie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fortuneCookieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FortuneCookie> result = fortuneCookieRepository
            .findById(fortuneCookie.getId())
            .map(existingFortuneCookie -> {
                if (fortuneCookie.getText() != null) {
                    existingFortuneCookie.setText(fortuneCookie.getText());
                }

                return existingFortuneCookie;
            })
            .map(fortuneCookieRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fortuneCookie.getId().toString())
        );
    }

    /**
     * {@code GET  /fortune-cookies} : get all the fortuneCookies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fortuneCookies in body.
     */
    @GetMapping("/fortune-cookies")
    public List<FortuneCookie> getAllFortuneCookies() {
        log.debug("REST request to get all FortuneCookies");
        return fortuneCookieRepository.findAll();
    }

    /**
     * {@code GET  /fortune-cookies/:id} : get the "id" fortuneCookie.
     *
     * @param id the id of the fortuneCookie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fortuneCookie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fortune-cookies/{id}")
    public ResponseEntity<FortuneCookie> getFortuneCookie(@PathVariable Long id) {
        log.debug("REST request to get FortuneCookie : {}", id);
        Optional<FortuneCookie> fortuneCookie = fortuneCookieRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fortuneCookie);
    }

    /**
     * {@code DELETE  /fortune-cookies/:id} : delete the "id" fortuneCookie.
     *
     * @param id the id of the fortuneCookie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fortune-cookies/{id}")
    public ResponseEntity<Void> deleteFortuneCookie(@PathVariable Long id) {
        log.debug("REST request to delete FortuneCookie : {}", id);
        fortuneCookieRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /fortune-cookie} : get the "id" fortuneCookie.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fortuneCookie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fortune-cookie")
    public ResponseEntity<FortuneCookie> getFortuneCookieRandom() {
        log.debug("REST request to get Random FortuneCookie");
        Long qty = fortuneCookieRepository.count();
        int idx = (int) (Math.random() * qty);
        Page<FortuneCookie> fortunePage = fortuneCookieRepository.findAll(PageRequest.of(idx, 1));
        Optional<FortuneCookie> fortuneCookie = null;
        if (fortunePage.hasContent()) {
            fortuneCookie = Optional.ofNullable(fortunePage.getContent().get(0));
        }
        return ResponseUtil.wrapOrNotFound(fortuneCookie);
    }
}
