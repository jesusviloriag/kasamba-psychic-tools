package com.kasamba.psychictools.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kasamba.psychictools.IntegrationTest;
import com.kasamba.psychictools.domain.App;
import com.kasamba.psychictools.repository.AppRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link AppResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODENAME = "AAAAAAAAAA";
    private static final String UPDATED_CODENAME = "BBBBBBBBBB";

    private static final String DEFAULT_BANID_ANDROID = "AAAAAAAAAA";
    private static final String UPDATED_BANID_ANDROID = "BBBBBBBBBB";

    private static final String DEFAULT_BANID_IOS = "AAAAAAAAAA";
    private static final String UPDATED_BANID_IOS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/apps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppMockMvc;

    private App app;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static App createEntity(EntityManager em) {
        App app = new App()
            .name(DEFAULT_NAME)
            .codename(DEFAULT_CODENAME)
            .banidAndroid(DEFAULT_BANID_ANDROID)
            .banidIos(DEFAULT_BANID_IOS)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE);
        return app;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static App createUpdatedEntity(EntityManager em) {
        App app = new App()
            .name(UPDATED_NAME)
            .codename(UPDATED_CODENAME)
            .banidAndroid(UPDATED_BANID_ANDROID)
            .banidIos(UPDATED_BANID_IOS)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE);
        return app;
    }

    @BeforeEach
    public void initTest() {
        app = createEntity(em);
    }

    @Test
    @Transactional
    void createApp() throws Exception {
        int databaseSizeBeforeCreate = appRepository.findAll().size();
        // Create the App
        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(app)))
            .andExpect(status().isCreated());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeCreate + 1);
        App testApp = appList.get(appList.size() - 1);
        assertThat(testApp.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApp.getCodename()).isEqualTo(DEFAULT_CODENAME);
        assertThat(testApp.getBanidAndroid()).isEqualTo(DEFAULT_BANID_ANDROID);
        assertThat(testApp.getBanidIos()).isEqualTo(DEFAULT_BANID_IOS);
        assertThat(testApp.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testApp.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createAppWithExistingId() throws Exception {
        // Create the App with an existing ID
        app.setId(1L);

        int databaseSizeBeforeCreate = appRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(app)))
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = appRepository.findAll().size();
        // set the field null
        app.setName(null);

        // Create the App, which fails.

        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(app)))
            .andExpect(status().isBadRequest());

        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodenameIsRequired() throws Exception {
        int databaseSizeBeforeTest = appRepository.findAll().size();
        // set the field null
        app.setCodename(null);

        // Create the App, which fails.

        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(app)))
            .andExpect(status().isBadRequest());

        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApps() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList
        restAppMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(app.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].codename").value(hasItem(DEFAULT_CODENAME)))
            .andExpect(jsonPath("$.[*].banidAndroid").value(hasItem(DEFAULT_BANID_ANDROID)))
            .andExpect(jsonPath("$.[*].banidIos").value(hasItem(DEFAULT_BANID_IOS)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))));
    }

    @Test
    @Transactional
    void getApp() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get the app
        restAppMockMvc
            .perform(get(ENTITY_API_URL_ID, app.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(app.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.codename").value(DEFAULT_CODENAME))
            .andExpect(jsonPath("$.banidAndroid").value(DEFAULT_BANID_ANDROID))
            .andExpect(jsonPath("$.banidIos").value(DEFAULT_BANID_IOS))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)));
    }

    @Test
    @Transactional
    void getNonExistingApp() throws Exception {
        // Get the app
        restAppMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApp() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        int databaseSizeBeforeUpdate = appRepository.findAll().size();

        // Update the app
        App updatedApp = appRepository.findById(app.getId()).get();
        // Disconnect from session so that the updates on updatedApp are not directly saved in db
        em.detach(updatedApp);
        updatedApp
            .name(UPDATED_NAME)
            .codename(UPDATED_CODENAME)
            .banidAndroid(UPDATED_BANID_ANDROID)
            .banidIos(UPDATED_BANID_IOS)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE);

        restAppMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApp.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApp))
            )
            .andExpect(status().isOk());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
        App testApp = appList.get(appList.size() - 1);
        assertThat(testApp.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApp.getCodename()).isEqualTo(UPDATED_CODENAME);
        assertThat(testApp.getBanidAndroid()).isEqualTo(UPDATED_BANID_ANDROID);
        assertThat(testApp.getBanidIos()).isEqualTo(UPDATED_BANID_IOS);
        assertThat(testApp.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testApp.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().size();
        app.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(
                put(ENTITY_API_URL_ID, app.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(app))
            )
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().size();
        app.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(app))
            )
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().size();
        app.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(app)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppWithPatch() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        int databaseSizeBeforeUpdate = appRepository.findAll().size();

        // Update the app using partial update
        App partialUpdatedApp = new App();
        partialUpdatedApp.setId(app.getId());

        partialUpdatedApp.banidIos(UPDATED_BANID_IOS).logo(UPDATED_LOGO).logoContentType(UPDATED_LOGO_CONTENT_TYPE);

        restAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApp))
            )
            .andExpect(status().isOk());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
        App testApp = appList.get(appList.size() - 1);
        assertThat(testApp.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApp.getCodename()).isEqualTo(DEFAULT_CODENAME);
        assertThat(testApp.getBanidAndroid()).isEqualTo(DEFAULT_BANID_ANDROID);
        assertThat(testApp.getBanidIos()).isEqualTo(UPDATED_BANID_IOS);
        assertThat(testApp.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testApp.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAppWithPatch() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        int databaseSizeBeforeUpdate = appRepository.findAll().size();

        // Update the app using partial update
        App partialUpdatedApp = new App();
        partialUpdatedApp.setId(app.getId());

        partialUpdatedApp
            .name(UPDATED_NAME)
            .codename(UPDATED_CODENAME)
            .banidAndroid(UPDATED_BANID_ANDROID)
            .banidIos(UPDATED_BANID_IOS)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE);

        restAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApp))
            )
            .andExpect(status().isOk());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
        App testApp = appList.get(appList.size() - 1);
        assertThat(testApp.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApp.getCodename()).isEqualTo(UPDATED_CODENAME);
        assertThat(testApp.getBanidAndroid()).isEqualTo(UPDATED_BANID_ANDROID);
        assertThat(testApp.getBanidIos()).isEqualTo(UPDATED_BANID_IOS);
        assertThat(testApp.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testApp.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().size();
        app.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, app.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(app))
            )
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().size();
        app.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(app))
            )
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().size();
        app.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(app)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApp() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        int databaseSizeBeforeDelete = appRepository.findAll().size();

        // Delete the app
        restAppMockMvc.perform(delete(ENTITY_API_URL_ID, app.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
