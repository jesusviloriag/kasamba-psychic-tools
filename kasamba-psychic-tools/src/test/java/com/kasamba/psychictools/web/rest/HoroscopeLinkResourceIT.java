package com.kasamba.psychictools.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kasamba.psychictools.IntegrationTest;
import com.kasamba.psychictools.domain.HoroscopeLink;
import com.kasamba.psychictools.repository.HoroscopeLinkRepository;
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
 * Integration tests for the {@link HoroscopeLinkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HoroscopeLinkResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/horoscope-links";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HoroscopeLinkRepository horoscopeLinkRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHoroscopeLinkMockMvc;

    private HoroscopeLink horoscopeLink;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HoroscopeLink createEntity(EntityManager em) {
        HoroscopeLink horoscopeLink = new HoroscopeLink().url(DEFAULT_URL);
        return horoscopeLink;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HoroscopeLink createUpdatedEntity(EntityManager em) {
        HoroscopeLink horoscopeLink = new HoroscopeLink().url(UPDATED_URL);
        return horoscopeLink;
    }

    @BeforeEach
    public void initTest() {
        horoscopeLink = createEntity(em);
    }

    @Test
    @Transactional
    void createHoroscopeLink() throws Exception {
        int databaseSizeBeforeCreate = horoscopeLinkRepository.findAll().size();
        // Create the HoroscopeLink
        restHoroscopeLinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horoscopeLink)))
            .andExpect(status().isCreated());

        // Validate the HoroscopeLink in the database
        List<HoroscopeLink> horoscopeLinkList = horoscopeLinkRepository.findAll();
        assertThat(horoscopeLinkList).hasSize(databaseSizeBeforeCreate + 1);
        HoroscopeLink testHoroscopeLink = horoscopeLinkList.get(horoscopeLinkList.size() - 1);
        assertThat(testHoroscopeLink.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void createHoroscopeLinkWithExistingId() throws Exception {
        // Create the HoroscopeLink with an existing ID
        horoscopeLink.setId(1L);

        int databaseSizeBeforeCreate = horoscopeLinkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHoroscopeLinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horoscopeLink)))
            .andExpect(status().isBadRequest());

        // Validate the HoroscopeLink in the database
        List<HoroscopeLink> horoscopeLinkList = horoscopeLinkRepository.findAll();
        assertThat(horoscopeLinkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = horoscopeLinkRepository.findAll().size();
        // set the field null
        horoscopeLink.setUrl(null);

        // Create the HoroscopeLink, which fails.

        restHoroscopeLinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horoscopeLink)))
            .andExpect(status().isBadRequest());

        List<HoroscopeLink> horoscopeLinkList = horoscopeLinkRepository.findAll();
        assertThat(horoscopeLinkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHoroscopeLinks() throws Exception {
        // Initialize the database
        horoscopeLinkRepository.saveAndFlush(horoscopeLink);

        // Get all the horoscopeLinkList
        restHoroscopeLinkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(horoscopeLink.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @Test
    @Transactional
    void getHoroscopeLink() throws Exception {
        // Initialize the database
        horoscopeLinkRepository.saveAndFlush(horoscopeLink);

        // Get the horoscopeLink
        restHoroscopeLinkMockMvc
            .perform(get(ENTITY_API_URL_ID, horoscopeLink.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(horoscopeLink.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }

    @Test
    @Transactional
    void getNonExistingHoroscopeLink() throws Exception {
        // Get the horoscopeLink
        restHoroscopeLinkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHoroscopeLink() throws Exception {
        // Initialize the database
        horoscopeLinkRepository.saveAndFlush(horoscopeLink);

        int databaseSizeBeforeUpdate = horoscopeLinkRepository.findAll().size();

        // Update the horoscopeLink
        HoroscopeLink updatedHoroscopeLink = horoscopeLinkRepository.findById(horoscopeLink.getId()).get();
        // Disconnect from session so that the updates on updatedHoroscopeLink are not directly saved in db
        em.detach(updatedHoroscopeLink);
        updatedHoroscopeLink.url(UPDATED_URL);

        restHoroscopeLinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHoroscopeLink.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHoroscopeLink))
            )
            .andExpect(status().isOk());

        // Validate the HoroscopeLink in the database
        List<HoroscopeLink> horoscopeLinkList = horoscopeLinkRepository.findAll();
        assertThat(horoscopeLinkList).hasSize(databaseSizeBeforeUpdate);
        HoroscopeLink testHoroscopeLink = horoscopeLinkList.get(horoscopeLinkList.size() - 1);
        assertThat(testHoroscopeLink.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void putNonExistingHoroscopeLink() throws Exception {
        int databaseSizeBeforeUpdate = horoscopeLinkRepository.findAll().size();
        horoscopeLink.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHoroscopeLinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, horoscopeLink.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(horoscopeLink))
            )
            .andExpect(status().isBadRequest());

        // Validate the HoroscopeLink in the database
        List<HoroscopeLink> horoscopeLinkList = horoscopeLinkRepository.findAll();
        assertThat(horoscopeLinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHoroscopeLink() throws Exception {
        int databaseSizeBeforeUpdate = horoscopeLinkRepository.findAll().size();
        horoscopeLink.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoroscopeLinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(horoscopeLink))
            )
            .andExpect(status().isBadRequest());

        // Validate the HoroscopeLink in the database
        List<HoroscopeLink> horoscopeLinkList = horoscopeLinkRepository.findAll();
        assertThat(horoscopeLinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHoroscopeLink() throws Exception {
        int databaseSizeBeforeUpdate = horoscopeLinkRepository.findAll().size();
        horoscopeLink.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoroscopeLinkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horoscopeLink)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HoroscopeLink in the database
        List<HoroscopeLink> horoscopeLinkList = horoscopeLinkRepository.findAll();
        assertThat(horoscopeLinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHoroscopeLinkWithPatch() throws Exception {
        // Initialize the database
        horoscopeLinkRepository.saveAndFlush(horoscopeLink);

        int databaseSizeBeforeUpdate = horoscopeLinkRepository.findAll().size();

        // Update the horoscopeLink using partial update
        HoroscopeLink partialUpdatedHoroscopeLink = new HoroscopeLink();
        partialUpdatedHoroscopeLink.setId(horoscopeLink.getId());

        restHoroscopeLinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHoroscopeLink.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHoroscopeLink))
            )
            .andExpect(status().isOk());

        // Validate the HoroscopeLink in the database
        List<HoroscopeLink> horoscopeLinkList = horoscopeLinkRepository.findAll();
        assertThat(horoscopeLinkList).hasSize(databaseSizeBeforeUpdate);
        HoroscopeLink testHoroscopeLink = horoscopeLinkList.get(horoscopeLinkList.size() - 1);
        assertThat(testHoroscopeLink.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void fullUpdateHoroscopeLinkWithPatch() throws Exception {
        // Initialize the database
        horoscopeLinkRepository.saveAndFlush(horoscopeLink);

        int databaseSizeBeforeUpdate = horoscopeLinkRepository.findAll().size();

        // Update the horoscopeLink using partial update
        HoroscopeLink partialUpdatedHoroscopeLink = new HoroscopeLink();
        partialUpdatedHoroscopeLink.setId(horoscopeLink.getId());

        partialUpdatedHoroscopeLink.url(UPDATED_URL);

        restHoroscopeLinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHoroscopeLink.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHoroscopeLink))
            )
            .andExpect(status().isOk());

        // Validate the HoroscopeLink in the database
        List<HoroscopeLink> horoscopeLinkList = horoscopeLinkRepository.findAll();
        assertThat(horoscopeLinkList).hasSize(databaseSizeBeforeUpdate);
        HoroscopeLink testHoroscopeLink = horoscopeLinkList.get(horoscopeLinkList.size() - 1);
        assertThat(testHoroscopeLink.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void patchNonExistingHoroscopeLink() throws Exception {
        int databaseSizeBeforeUpdate = horoscopeLinkRepository.findAll().size();
        horoscopeLink.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHoroscopeLinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, horoscopeLink.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(horoscopeLink))
            )
            .andExpect(status().isBadRequest());

        // Validate the HoroscopeLink in the database
        List<HoroscopeLink> horoscopeLinkList = horoscopeLinkRepository.findAll();
        assertThat(horoscopeLinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHoroscopeLink() throws Exception {
        int databaseSizeBeforeUpdate = horoscopeLinkRepository.findAll().size();
        horoscopeLink.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoroscopeLinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(horoscopeLink))
            )
            .andExpect(status().isBadRequest());

        // Validate the HoroscopeLink in the database
        List<HoroscopeLink> horoscopeLinkList = horoscopeLinkRepository.findAll();
        assertThat(horoscopeLinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHoroscopeLink() throws Exception {
        int databaseSizeBeforeUpdate = horoscopeLinkRepository.findAll().size();
        horoscopeLink.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoroscopeLinkMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(horoscopeLink))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HoroscopeLink in the database
        List<HoroscopeLink> horoscopeLinkList = horoscopeLinkRepository.findAll();
        assertThat(horoscopeLinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHoroscopeLink() throws Exception {
        // Initialize the database
        horoscopeLinkRepository.saveAndFlush(horoscopeLink);

        int databaseSizeBeforeDelete = horoscopeLinkRepository.findAll().size();

        // Delete the horoscopeLink
        restHoroscopeLinkMockMvc
            .perform(delete(ENTITY_API_URL_ID, horoscopeLink.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HoroscopeLink> horoscopeLinkList = horoscopeLinkRepository.findAll();
        assertThat(horoscopeLinkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
