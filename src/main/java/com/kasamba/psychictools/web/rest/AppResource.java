package com.kasamba.psychictools.web.rest;

import com.kasamba.psychictools.domain.App;
import com.kasamba.psychictools.repository.AppRepository;
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
 * REST controller for managing {@link com.kasamba.psychictools.domain.App}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AppResource {

    private final Logger log = LoggerFactory.getLogger(AppResource.class);

    private static final String ENTITY_NAME = "app";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppRepository appRepository;

    public AppResource(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    /**
     * {@code POST  /apps} : Create a new app.
     *
     * @param app the app to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new app, or with status {@code 400 (Bad Request)} if the app has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apps")
    public ResponseEntity<App> createApp(@Valid @RequestBody App app) throws URISyntaxException {
        log.debug("REST request to save App : {}", app);
        if (app.getId() != null) {
            throw new BadRequestAlertException("A new app cannot already have an ID", ENTITY_NAME, "idexists");
        }
        App result = appRepository.save(app);
        return ResponseEntity
            .created(new URI("/api/apps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apps/:id} : Updates an existing app.
     *
     * @param id the id of the app to save.
     * @param app the app to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated app,
     * or with status {@code 400 (Bad Request)} if the app is not valid,
     * or with status {@code 500 (Internal Server Error)} if the app couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apps/{id}")
    public ResponseEntity<App> updateApp(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody App app)
        throws URISyntaxException {
        log.debug("REST request to update App : {}, {}", id, app);
        if (app.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, app.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        App result = appRepository.save(app);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, app.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /apps/:id} : Partial updates given fields of an existing app, field will ignore if it is null
     *
     * @param id the id of the app to save.
     * @param app the app to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated app,
     * or with status {@code 400 (Bad Request)} if the app is not valid,
     * or with status {@code 404 (Not Found)} if the app is not found,
     * or with status {@code 500 (Internal Server Error)} if the app couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apps/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<App> partialUpdateApp(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody App app)
        throws URISyntaxException {
        log.debug("REST request to partial update App partially : {}, {}", id, app);
        if (app.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, app.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<App> result = appRepository
            .findById(app.getId())
            .map(existingApp -> {
                if (app.getName() != null) {
                    existingApp.setName(app.getName());
                }
                if (app.getCodename() != null) {
                    existingApp.setCodename(app.getCodename());
                }
                if (app.getBanidAndroid() != null) {
                    existingApp.setBanidAndroid(app.getBanidAndroid());
                }
                if (app.getBanidIos() != null) {
                    existingApp.setBanidIos(app.getBanidIos());
                }
                if (app.getLogo() != null) {
                    existingApp.setLogo(app.getLogo());
                }
                if (app.getLogoContentType() != null) {
                    existingApp.setLogoContentType(app.getLogoContentType());
                }

                return existingApp;
            })
            .map(appRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, app.getId().toString())
        );
    }

    /**
     * {@code GET  /apps} : get all the apps.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apps in body.
     */
    @GetMapping("/apps")
    public ResponseEntity<List<App>> getAllApps(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Apps");
        Page<App> page = appRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /apps/:id} : get the "id" app.
     *
     * @param id the id of the app to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the app, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apps/{id}")
    public ResponseEntity<App> getApp(@PathVariable Long id) {
        log.debug("REST request to get App : {}", id);
        Optional<App> app = appRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(app);
    }

    /**
     * {@code DELETE  /apps/:id} : delete the "id" app.
     *
     * @param id the id of the app to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apps/{id}")
    public ResponseEntity<Void> deleteApp(@PathVariable Long id) {
        log.debug("REST request to delete App : {}", id);
        appRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
