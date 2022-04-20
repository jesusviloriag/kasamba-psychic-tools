package com.kasamba.psychictools.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kasamba.psychictools.IntegrationTest;
import com.kasamba.psychictools.domain.FortuneCookie;
import com.kasamba.psychictools.repository.FortuneCookieRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FortuneCookieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FortuneCookieResourceIT {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fortune-cookies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FortuneCookieRepository fortuneCookieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFortuneCookieMockMvc;

    private FortuneCookie fortuneCookie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FortuneCookie createEntity(EntityManager em) {
        FortuneCookie fortuneCookie = new FortuneCookie().text(DEFAULT_TEXT);
        return fortuneCookie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FortuneCookie createUpdatedEntity(EntityManager em) {
        FortuneCookie fortuneCookie = new FortuneCookie().text(UPDATED_TEXT);
        return fortuneCookie;
    }

    @BeforeEach
    public void initTest() {
        fortuneCookie = createEntity(em);
    }

    @Test
    @Transactional
    void createFortuneCookie() throws Exception {
        int databaseSizeBeforeCreate = fortuneCookieRepository.findAll().size();
        // Create the FortuneCookie
        restFortuneCookieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fortuneCookie)))
            .andExpect(status().isCreated());

        // Validate the FortuneCookie in the database
        List<FortuneCookie> fortuneCookieList = fortuneCookieRepository.findAll();
        assertThat(fortuneCookieList).hasSize(databaseSizeBeforeCreate + 1);
        FortuneCookie testFortuneCookie = fortuneCookieList.get(fortuneCookieList.size() - 1);
        assertThat(testFortuneCookie.getText()).isEqualTo(DEFAULT_TEXT);
    }

    @Test
    @Transactional
    void createFortuneCookieWithExistingId() throws Exception {
        // Create the FortuneCookie with an existing ID
        fortuneCookie.setId(1L);

        int databaseSizeBeforeCreate = fortuneCookieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFortuneCookieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fortuneCookie)))
            .andExpect(status().isBadRequest());

        // Validate the FortuneCookie in the database
        List<FortuneCookie> fortuneCookieList = fortuneCookieRepository.findAll();
        assertThat(fortuneCookieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = fortuneCookieRepository.findAll().size();
        // set the field null
        fortuneCookie.setText(null);

        // Create the FortuneCookie, which fails.

        restFortuneCookieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fortuneCookie)))
            .andExpect(status().isBadRequest());

        List<FortuneCookie> fortuneCookieList = fortuneCookieRepository.findAll();
        assertThat(fortuneCookieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFortuneCookies() throws Exception {
        // Initialize the database
        fortuneCookieRepository.saveAndFlush(fortuneCookie);

        // Get all the fortuneCookieList
        restFortuneCookieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fortuneCookie.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)));
    }

    @Test
    @Transactional
    void getFortuneCookie() throws Exception {
        // Initialize the database
        fortuneCookieRepository.saveAndFlush(fortuneCookie);

        // Get the fortuneCookie
        restFortuneCookieMockMvc
            .perform(get(ENTITY_API_URL_ID, fortuneCookie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fortuneCookie.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT));
    }

    @Test
    @Transactional
    void getNonExistingFortuneCookie() throws Exception {
        // Get the fortuneCookie
        restFortuneCookieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFortuneCookie() throws Exception {
        // Initialize the database
        fortuneCookieRepository.saveAndFlush(fortuneCookie);

        int databaseSizeBeforeUpdate = fortuneCookieRepository.findAll().size();

        // Update the fortuneCookie
        FortuneCookie updatedFortuneCookie = fortuneCookieRepository.findById(fortuneCookie.getId()).get();
        // Disconnect from session so that the updates on updatedFortuneCookie are not directly saved in db
        em.detach(updatedFortuneCookie);
        updatedFortuneCookie.text(UPDATED_TEXT);

        restFortuneCookieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFortuneCookie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFortuneCookie))
            )
            .andExpect(status().isOk());

        // Validate the FortuneCookie in the database
        List<FortuneCookie> fortuneCookieList = fortuneCookieRepository.findAll();
        assertThat(fortuneCookieList).hasSize(databaseSizeBeforeUpdate);
        FortuneCookie testFortuneCookie = fortuneCookieList.get(fortuneCookieList.size() - 1);
        assertThat(testFortuneCookie.getText()).isEqualTo(UPDATED_TEXT);
    }

    @Test
    @Transactional
    void putNonExistingFortuneCookie() throws Exception {
        int databaseSizeBeforeUpdate = fortuneCookieRepository.findAll().size();
        fortuneCookie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFortuneCookieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fortuneCookie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fortuneCookie))
            )
            .andExpect(status().isBadRequest());

        // Validate the FortuneCookie in the database
        List<FortuneCookie> fortuneCookieList = fortuneCookieRepository.findAll();
        assertThat(fortuneCookieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFortuneCookie() throws Exception {
        int databaseSizeBeforeUpdate = fortuneCookieRepository.findAll().size();
        fortuneCookie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFortuneCookieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fortuneCookie))
            )
            .andExpect(status().isBadRequest());

        // Validate the FortuneCookie in the database
        List<FortuneCookie> fortuneCookieList = fortuneCookieRepository.findAll();
        assertThat(fortuneCookieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFortuneCookie() throws Exception {
        int databaseSizeBeforeUpdate = fortuneCookieRepository.findAll().size();
        fortuneCookie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFortuneCookieMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fortuneCookie)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FortuneCookie in the database
        List<FortuneCookie> fortuneCookieList = fortuneCookieRepository.findAll();
        assertThat(fortuneCookieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFortuneCookieWithPatch() throws Exception {
        // Initialize the database
        fortuneCookieRepository.saveAndFlush(fortuneCookie);

        int databaseSizeBeforeUpdate = fortuneCookieRepository.findAll().size();

        // Update the fortuneCookie using partial update
        FortuneCookie partialUpdatedFortuneCookie = new FortuneCookie();
        partialUpdatedFortuneCookie.setId(fortuneCookie.getId());

        partialUpdatedFortuneCookie.text(UPDATED_TEXT);

        restFortuneCookieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFortuneCookie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFortuneCookie))
            )
            .andExpect(status().isOk());

        // Validate the FortuneCookie in the database
        List<FortuneCookie> fortuneCookieList = fortuneCookieRepository.findAll();
        assertThat(fortuneCookieList).hasSize(databaseSizeBeforeUpdate);
        FortuneCookie testFortuneCookie = fortuneCookieList.get(fortuneCookieList.size() - 1);
        assertThat(testFortuneCookie.getText()).isEqualTo(UPDATED_TEXT);
    }

    @Test
    @Transactional
    void fullUpdateFortuneCookieWithPatch() throws Exception {
        // Initialize the database
        fortuneCookieRepository.saveAndFlush(fortuneCookie);

        int databaseSizeBeforeUpdate = fortuneCookieRepository.findAll().size();

        // Update the fortuneCookie using partial update
        FortuneCookie partialUpdatedFortuneCookie = new FortuneCookie();
        partialUpdatedFortuneCookie.setId(fortuneCookie.getId());

        partialUpdatedFortuneCookie.text(UPDATED_TEXT);

        restFortuneCookieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFortuneCookie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFortuneCookie))
            )
            .andExpect(status().isOk());

        // Validate the FortuneCookie in the database
        List<FortuneCookie> fortuneCookieList = fortuneCookieRepository.findAll();
        assertThat(fortuneCookieList).hasSize(databaseSizeBeforeUpdate);
        FortuneCookie testFortuneCookie = fortuneCookieList.get(fortuneCookieList.size() - 1);
        assertThat(testFortuneCookie.getText()).isEqualTo(UPDATED_TEXT);
    }

    @Test
    @Transactional
    void patchNonExistingFortuneCookie() throws Exception {
        int databaseSizeBeforeUpdate = fortuneCookieRepository.findAll().size();
        fortuneCookie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFortuneCookieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fortuneCookie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fortuneCookie))
            )
            .andExpect(status().isBadRequest());

        // Validate the FortuneCookie in the database
        List<FortuneCookie> fortuneCookieList = fortuneCookieRepository.findAll();
        assertThat(fortuneCookieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFortuneCookie() throws Exception {
        int databaseSizeBeforeUpdate = fortuneCookieRepository.findAll().size();
        fortuneCookie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFortuneCookieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fortuneCookie))
            )
            .andExpect(status().isBadRequest());

        // Validate the FortuneCookie in the database
        List<FortuneCookie> fortuneCookieList = fortuneCookieRepository.findAll();
        assertThat(fortuneCookieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFortuneCookie() throws Exception {
        int databaseSizeBeforeUpdate = fortuneCookieRepository.findAll().size();
        fortuneCookie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFortuneCookieMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fortuneCookie))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FortuneCookie in the database
        List<FortuneCookie> fortuneCookieList = fortuneCookieRepository.findAll();
        assertThat(fortuneCookieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFortuneCookie() throws Exception {
        // Initialize the database
        fortuneCookieRepository.saveAndFlush(fortuneCookie);

        int databaseSizeBeforeDelete = fortuneCookieRepository.findAll().size();

        // Delete the fortuneCookie
        restFortuneCookieMockMvc
            .perform(delete(ENTITY_API_URL_ID, fortuneCookie.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FortuneCookie> fortuneCookieList = fortuneCookieRepository.findAll();
        assertThat(fortuneCookieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
