package com.kasamba.psychictools.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kasamba.psychictools.IntegrationTest;
import com.kasamba.psychictools.domain.App;
import com.kasamba.psychictools.domain.AppPromo;
import com.kasamba.psychictools.repository.AppPromoRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AppPromoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppPromoResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_BANID = "AAAAAAAAAA";
    private static final String UPDATED_BANID = "BBBBBBBBBB";

    private static final Integer DEFAULT_RATE = 1;
    private static final Integer UPDATED_RATE = 2;

    private static final String ENTITY_API_URL = "/api/app-promos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppPromoRepository appPromoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppPromoMockMvc;

    private AppPromo appPromo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppPromo createEntity(EntityManager em) {
        AppPromo appPromo = new AppPromo()
            .title(DEFAULT_TITLE)
            .text(DEFAULT_TEXT)
            .date(DEFAULT_DATE)
            .banid(DEFAULT_BANID)
            .rate(DEFAULT_RATE);
        // Add required entity
        App app;
        if (TestUtil.findAll(em, App.class).isEmpty()) {
            app = AppResourceIT.createEntity(em);
            em.persist(app);
            em.flush();
        } else {
            app = TestUtil.findAll(em, App.class).get(0);
        }
        appPromo.setApp(app);
        return appPromo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppPromo createUpdatedEntity(EntityManager em) {
        AppPromo appPromo = new AppPromo()
            .title(UPDATED_TITLE)
            .text(UPDATED_TEXT)
            .date(UPDATED_DATE)
            .banid(UPDATED_BANID)
            .rate(UPDATED_RATE);
        // Add required entity
        App app;
        if (TestUtil.findAll(em, App.class).isEmpty()) {
            app = AppResourceIT.createUpdatedEntity(em);
            em.persist(app);
            em.flush();
        } else {
            app = TestUtil.findAll(em, App.class).get(0);
        }
        appPromo.setApp(app);
        return appPromo;
    }

    @BeforeEach
    public void initTest() {
        appPromo = createEntity(em);
    }

    @Test
    @Transactional
    void createAppPromo() throws Exception {
        int databaseSizeBeforeCreate = appPromoRepository.findAll().size();
        // Create the AppPromo
        restAppPromoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appPromo)))
            .andExpect(status().isCreated());

        // Validate the AppPromo in the database
        List<AppPromo> appPromoList = appPromoRepository.findAll();
        assertThat(appPromoList).hasSize(databaseSizeBeforeCreate + 1);
        AppPromo testAppPromo = appPromoList.get(appPromoList.size() - 1);
        assertThat(testAppPromo.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAppPromo.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testAppPromo.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAppPromo.getBanid()).isEqualTo(DEFAULT_BANID);
        assertThat(testAppPromo.getRate()).isEqualTo(DEFAULT_RATE);
    }

    @Test
    @Transactional
    void createAppPromoWithExistingId() throws Exception {
        // Create the AppPromo with an existing ID
        appPromo.setId(1L);

        int databaseSizeBeforeCreate = appPromoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppPromoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appPromo)))
            .andExpect(status().isBadRequest());

        // Validate the AppPromo in the database
        List<AppPromo> appPromoList = appPromoRepository.findAll();
        assertThat(appPromoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = appPromoRepository.findAll().size();
        // set the field null
        appPromo.setTitle(null);

        // Create the AppPromo, which fails.

        restAppPromoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appPromo)))
            .andExpect(status().isBadRequest());

        List<AppPromo> appPromoList = appPromoRepository.findAll();
        assertThat(appPromoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = appPromoRepository.findAll().size();
        // set the field null
        appPromo.setText(null);

        // Create the AppPromo, which fails.

        restAppPromoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appPromo)))
            .andExpect(status().isBadRequest());

        List<AppPromo> appPromoList = appPromoRepository.findAll();
        assertThat(appPromoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = appPromoRepository.findAll().size();
        // set the field null
        appPromo.setDate(null);

        // Create the AppPromo, which fails.

        restAppPromoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appPromo)))
            .andExpect(status().isBadRequest());

        List<AppPromo> appPromoList = appPromoRepository.findAll();
        assertThat(appPromoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBanidIsRequired() throws Exception {
        int databaseSizeBeforeTest = appPromoRepository.findAll().size();
        // set the field null
        appPromo.setBanid(null);

        // Create the AppPromo, which fails.

        restAppPromoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appPromo)))
            .andExpect(status().isBadRequest());

        List<AppPromo> appPromoList = appPromoRepository.findAll();
        assertThat(appPromoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = appPromoRepository.findAll().size();
        // set the field null
        appPromo.setRate(null);

        // Create the AppPromo, which fails.

        restAppPromoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appPromo)))
            .andExpect(status().isBadRequest());

        List<AppPromo> appPromoList = appPromoRepository.findAll();
        assertThat(appPromoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppPromos() throws Exception {
        // Initialize the database
        appPromoRepository.saveAndFlush(appPromo);

        // Get all the appPromoList
        restAppPromoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appPromo.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].banid").value(hasItem(DEFAULT_BANID)))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE)));
    }

    @Test
    @Transactional
    void getAppPromo() throws Exception {
        // Initialize the database
        appPromoRepository.saveAndFlush(appPromo);

        // Get the appPromo
        restAppPromoMockMvc
            .perform(get(ENTITY_API_URL_ID, appPromo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appPromo.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.banid").value(DEFAULT_BANID))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE));
    }

    @Test
    @Transactional
    void getNonExistingAppPromo() throws Exception {
        // Get the appPromo
        restAppPromoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAppPromo() throws Exception {
        // Initialize the database
        appPromoRepository.saveAndFlush(appPromo);

        int databaseSizeBeforeUpdate = appPromoRepository.findAll().size();

        // Update the appPromo
        AppPromo updatedAppPromo = appPromoRepository.findById(appPromo.getId()).get();
        // Disconnect from session so that the updates on updatedAppPromo are not directly saved in db
        em.detach(updatedAppPromo);
        updatedAppPromo.title(UPDATED_TITLE).text(UPDATED_TEXT).date(UPDATED_DATE).banid(UPDATED_BANID).rate(UPDATED_RATE);

        restAppPromoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAppPromo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAppPromo))
            )
            .andExpect(status().isOk());

        // Validate the AppPromo in the database
        List<AppPromo> appPromoList = appPromoRepository.findAll();
        assertThat(appPromoList).hasSize(databaseSizeBeforeUpdate);
        AppPromo testAppPromo = appPromoList.get(appPromoList.size() - 1);
        assertThat(testAppPromo.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAppPromo.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testAppPromo.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAppPromo.getBanid()).isEqualTo(UPDATED_BANID);
        assertThat(testAppPromo.getRate()).isEqualTo(UPDATED_RATE);
    }

    @Test
    @Transactional
    void putNonExistingAppPromo() throws Exception {
        int databaseSizeBeforeUpdate = appPromoRepository.findAll().size();
        appPromo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppPromoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appPromo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appPromo))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppPromo in the database
        List<AppPromo> appPromoList = appPromoRepository.findAll();
        assertThat(appPromoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppPromo() throws Exception {
        int databaseSizeBeforeUpdate = appPromoRepository.findAll().size();
        appPromo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppPromoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appPromo))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppPromo in the database
        List<AppPromo> appPromoList = appPromoRepository.findAll();
        assertThat(appPromoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppPromo() throws Exception {
        int databaseSizeBeforeUpdate = appPromoRepository.findAll().size();
        appPromo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppPromoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appPromo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppPromo in the database
        List<AppPromo> appPromoList = appPromoRepository.findAll();
        assertThat(appPromoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppPromoWithPatch() throws Exception {
        // Initialize the database
        appPromoRepository.saveAndFlush(appPromo);

        int databaseSizeBeforeUpdate = appPromoRepository.findAll().size();

        // Update the appPromo using partial update
        AppPromo partialUpdatedAppPromo = new AppPromo();
        partialUpdatedAppPromo.setId(appPromo.getId());

        partialUpdatedAppPromo.banid(UPDATED_BANID);

        restAppPromoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppPromo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppPromo))
            )
            .andExpect(status().isOk());

        // Validate the AppPromo in the database
        List<AppPromo> appPromoList = appPromoRepository.findAll();
        assertThat(appPromoList).hasSize(databaseSizeBeforeUpdate);
        AppPromo testAppPromo = appPromoList.get(appPromoList.size() - 1);
        assertThat(testAppPromo.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAppPromo.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testAppPromo.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAppPromo.getBanid()).isEqualTo(UPDATED_BANID);
        assertThat(testAppPromo.getRate()).isEqualTo(DEFAULT_RATE);
    }

    @Test
    @Transactional
    void fullUpdateAppPromoWithPatch() throws Exception {
        // Initialize the database
        appPromoRepository.saveAndFlush(appPromo);

        int databaseSizeBeforeUpdate = appPromoRepository.findAll().size();

        // Update the appPromo using partial update
        AppPromo partialUpdatedAppPromo = new AppPromo();
        partialUpdatedAppPromo.setId(appPromo.getId());

        partialUpdatedAppPromo.title(UPDATED_TITLE).text(UPDATED_TEXT).date(UPDATED_DATE).banid(UPDATED_BANID).rate(UPDATED_RATE);

        restAppPromoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppPromo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppPromo))
            )
            .andExpect(status().isOk());

        // Validate the AppPromo in the database
        List<AppPromo> appPromoList = appPromoRepository.findAll();
        assertThat(appPromoList).hasSize(databaseSizeBeforeUpdate);
        AppPromo testAppPromo = appPromoList.get(appPromoList.size() - 1);
        assertThat(testAppPromo.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAppPromo.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testAppPromo.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAppPromo.getBanid()).isEqualTo(UPDATED_BANID);
        assertThat(testAppPromo.getRate()).isEqualTo(UPDATED_RATE);
    }

    @Test
    @Transactional
    void patchNonExistingAppPromo() throws Exception {
        int databaseSizeBeforeUpdate = appPromoRepository.findAll().size();
        appPromo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppPromoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appPromo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appPromo))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppPromo in the database
        List<AppPromo> appPromoList = appPromoRepository.findAll();
        assertThat(appPromoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppPromo() throws Exception {
        int databaseSizeBeforeUpdate = appPromoRepository.findAll().size();
        appPromo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppPromoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appPromo))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppPromo in the database
        List<AppPromo> appPromoList = appPromoRepository.findAll();
        assertThat(appPromoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppPromo() throws Exception {
        int databaseSizeBeforeUpdate = appPromoRepository.findAll().size();
        appPromo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppPromoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(appPromo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppPromo in the database
        List<AppPromo> appPromoList = appPromoRepository.findAll();
        assertThat(appPromoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppPromo() throws Exception {
        // Initialize the database
        appPromoRepository.saveAndFlush(appPromo);

        int databaseSizeBeforeDelete = appPromoRepository.findAll().size();

        // Delete the appPromo
        restAppPromoMockMvc
            .perform(delete(ENTITY_API_URL_ID, appPromo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppPromo> appPromoList = appPromoRepository.findAll();
        assertThat(appPromoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
