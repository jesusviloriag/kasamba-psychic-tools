package com.kasamba.psychictools.web.rest;

import com.kasamba.psychictools.domain.AppPromo;
import com.kasamba.psychictools.repository.AppPromoRepository;
import com.kasamba.psychictools.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.kasamba.psychictools.domain.AppPromo}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AppPromoResource {

    private final Logger log = LoggerFactory.getLogger(AppPromoResource.class);

    private static final String ENTITY_NAME = "appPromo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppPromoRepository appPromoRepository;

    public AppPromoResource(AppPromoRepository appPromoRepository) {
        this.appPromoRepository = appPromoRepository;
    }

    /**
     * {@code POST  /app-promos} : Create a new appPromo.
     *
     * @param appPromo the appPromo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appPromo, or with status {@code 400 (Bad Request)} if the appPromo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/app-promos")
    public ResponseEntity<AppPromo> createAppPromo(@Valid @RequestBody AppPromo appPromo) throws URISyntaxException {
        log.debug("REST request to save AppPromo : {}", appPromo);
        if (appPromo.getId() != null) {
            throw new BadRequestAlertException("A new appPromo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppPromo result = appPromoRepository.save(appPromo);
        return ResponseEntity
            .created(new URI("/api/app-promos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /app-promos/:id} : Updates an existing appPromo.
     *
     * @param id the id of the appPromo to save.
     * @param appPromo the appPromo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appPromo,
     * or with status {@code 400 (Bad Request)} if the appPromo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appPromo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/app-promos/{id}")
    public ResponseEntity<AppPromo> updateAppPromo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppPromo appPromo
    ) throws URISyntaxException {
        log.debug("REST request to update AppPromo : {}, {}", id, appPromo);
        if (appPromo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appPromo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appPromoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppPromo result = appPromoRepository.save(appPromo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, appPromo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /app-promos/:id} : Partial updates given fields of an existing appPromo, field will ignore if it is null
     *
     * @param id the id of the appPromo to save.
     * @param appPromo the appPromo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appPromo,
     * or with status {@code 400 (Bad Request)} if the appPromo is not valid,
     * or with status {@code 404 (Not Found)} if the appPromo is not found,
     * or with status {@code 500 (Internal Server Error)} if the appPromo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/app-promos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppPromo> partialUpdateAppPromo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppPromo appPromo
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppPromo partially : {}, {}", id, appPromo);
        if (appPromo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appPromo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appPromoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppPromo> result = appPromoRepository
            .findById(appPromo.getId())
            .map(existingAppPromo -> {
                if (appPromo.getTitle() != null) {
                    existingAppPromo.setTitle(appPromo.getTitle());
                }
                if (appPromo.getText() != null) {
                    existingAppPromo.setText(appPromo.getText());
                }
                if (appPromo.getDate() != null) {
                    existingAppPromo.setDate(appPromo.getDate());
                }
                if (appPromo.getBanid() != null) {
                    existingAppPromo.setBanid(appPromo.getBanid());
                }
                if (appPromo.getRate() != null) {
                    existingAppPromo.setRate(appPromo.getRate());
                }

                return existingAppPromo;
            })
            .map(appPromoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, appPromo.getId().toString())
        );
    }

    /**
     * {@code GET  /app-promos} : get all the appPromos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appPromos in body.
     */
    @GetMapping("/app-promos")
    public ResponseEntity<List<AppPromo>> getAllAppPromos(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of AppPromos");
        Page<AppPromo> page;
        if (eagerload) {
            page = appPromoRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = appPromoRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /app-promos/:id} : get the "id" appPromo.
     *
     * @param id the id of the appPromo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appPromo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/app-promos/{id}")
    public ResponseEntity<AppPromo> getAppPromo(@PathVariable Long id) {
        log.debug("REST request to get AppPromo : {}", id);
        Optional<AppPromo> appPromo = appPromoRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(appPromo);
    }

    /**
     * {@code DELETE  /app-promos/:id} : delete the "id" appPromo.
     *
     * @param id the id of the appPromo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/app-promos/{id}")
    public ResponseEntity<Void> deleteAppPromo(@PathVariable Long id) {
        log.debug("REST request to delete AppPromo : {}", id);
        appPromoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /app-promos} : get all the appPromos.
     *
     * @param codename App codename
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appPromos in body.
     */
    @GetMapping("/app-promos/today/{codename}")
    public ResponseEntity<List<AppPromo>> getTodayPromos(@PathVariable String codename) {
        log.debug("REST request to get AppPromos of " + codename);
        List<AppPromo> page = appPromoRepository.findByApp_CodenameLikeAndDateEquals(codename, LocalDate.now());
        if (page.size() < 1) {
            page = null;
        }
        return ResponseEntity.ok().body(page);
    }
}
