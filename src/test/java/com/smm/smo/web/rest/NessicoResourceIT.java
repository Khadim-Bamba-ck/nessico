package com.smm.smo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.smm.smo.IntegrationTest;
import com.smm.smo.domain.Nessico;
import com.smm.smo.repository.NessicoRepository;
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
 * Integration tests for the {@link NessicoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NessicoResourceIT {

    private static final String DEFAULT_NOM_APPLICATION = "AAAAAAAAAA";
    private static final String UPDATED_NOM_APPLICATION = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEMANDE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEMANDE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/nessicos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NessicoRepository nessicoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNessicoMockMvc;

    private Nessico nessico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nessico createEntity(EntityManager em) {
        Nessico nessico = new Nessico()
            .nom_application(DEFAULT_NOM_APPLICATION)
            .action(DEFAULT_ACTION)
            .password(DEFAULT_PASSWORD)
            .status(DEFAULT_STATUS)
            .message(DEFAULT_MESSAGE)
            .date_demande(DEFAULT_DATE_DEMANDE)
            .userId(DEFAULT_USER_ID);
        return nessico;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nessico createUpdatedEntity(EntityManager em) {
        Nessico nessico = new Nessico()
            .nom_application(UPDATED_NOM_APPLICATION)
            .action(UPDATED_ACTION)
            .password(UPDATED_PASSWORD)
            .status(UPDATED_STATUS)
            .message(UPDATED_MESSAGE)
            .date_demande(UPDATED_DATE_DEMANDE)
            .userId(UPDATED_USER_ID);
        return nessico;
    }

    @BeforeEach
    public void initTest() {
        nessico = createEntity(em);
    }

    @Test
    @Transactional
    void createNessico() throws Exception {
        int databaseSizeBeforeCreate = nessicoRepository.findAll().size();
        // Create the Nessico
        restNessicoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nessico))
            )
            .andExpect(status().isCreated());

        // Validate the Nessico in the database
        List<Nessico> nessicoList = nessicoRepository.findAll();
        assertThat(nessicoList).hasSize(databaseSizeBeforeCreate + 1);
        Nessico testNessico = nessicoList.get(nessicoList.size() - 1);
        assertThat(testNessico.getNom_application()).isEqualTo(DEFAULT_NOM_APPLICATION);
        assertThat(testNessico.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testNessico.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testNessico.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testNessico.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testNessico.getDate_demande()).isEqualTo(DEFAULT_DATE_DEMANDE);
        assertThat(testNessico.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void createNessicoWithExistingId() throws Exception {
        // Create the Nessico with an existing ID
        nessico.setId(1L);

        int databaseSizeBeforeCreate = nessicoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNessicoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nessico))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nessico in the database
        List<Nessico> nessicoList = nessicoRepository.findAll();
        assertThat(nessicoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActionIsRequired() throws Exception {
        int databaseSizeBeforeTest = nessicoRepository.findAll().size();
        // set the field null
        nessico.setAction(null);

        // Create the Nessico, which fails.

        restNessicoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nessico))
            )
            .andExpect(status().isBadRequest());

        List<Nessico> nessicoList = nessicoRepository.findAll();
        assertThat(nessicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = nessicoRepository.findAll().size();
        // set the field null
        nessico.setStatus(null);

        // Create the Nessico, which fails.

        restNessicoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nessico))
            )
            .andExpect(status().isBadRequest());

        List<Nessico> nessicoList = nessicoRepository.findAll();
        assertThat(nessicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = nessicoRepository.findAll().size();
        // set the field null
        nessico.setMessage(null);

        // Create the Nessico, which fails.

        restNessicoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nessico))
            )
            .andExpect(status().isBadRequest());

        List<Nessico> nessicoList = nessicoRepository.findAll();
        assertThat(nessicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNessicos() throws Exception {
        // Initialize the database
        nessicoRepository.saveAndFlush(nessico);

        // Get all the nessicoList
        restNessicoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nessico.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom_application").value(hasItem(DEFAULT_NOM_APPLICATION)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].date_demande").value(hasItem(DEFAULT_DATE_DEMANDE.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)));
    }

    @Test
    @Transactional
    void getNessico() throws Exception {
        // Initialize the database
        nessicoRepository.saveAndFlush(nessico);

        // Get the nessico
        restNessicoMockMvc
            .perform(get(ENTITY_API_URL_ID, nessico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nessico.getId().intValue()))
            .andExpect(jsonPath("$.nom_application").value(DEFAULT_NOM_APPLICATION))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.date_demande").value(DEFAULT_DATE_DEMANDE.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID));
    }

    @Test
    @Transactional
    void getNonExistingNessico() throws Exception {
        // Get the nessico
        restNessicoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNessico() throws Exception {
        // Initialize the database
        nessicoRepository.saveAndFlush(nessico);

        int databaseSizeBeforeUpdate = nessicoRepository.findAll().size();

        // Update the nessico
        Nessico updatedNessico = nessicoRepository.findById(nessico.getId()).get();
        // Disconnect from session so that the updates on updatedNessico are not directly saved in db
        em.detach(updatedNessico);
        updatedNessico
            .nom_application(UPDATED_NOM_APPLICATION)
            .action(UPDATED_ACTION)
            .password(UPDATED_PASSWORD)
            .status(UPDATED_STATUS)
            .message(UPDATED_MESSAGE)
            .date_demande(UPDATED_DATE_DEMANDE)
            .userId(UPDATED_USER_ID);

        restNessicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNessico.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNessico))
            )
            .andExpect(status().isOk());

        // Validate the Nessico in the database
        List<Nessico> nessicoList = nessicoRepository.findAll();
        assertThat(nessicoList).hasSize(databaseSizeBeforeUpdate);
        Nessico testNessico = nessicoList.get(nessicoList.size() - 1);
        assertThat(testNessico.getNom_application()).isEqualTo(UPDATED_NOM_APPLICATION);
        assertThat(testNessico.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testNessico.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testNessico.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testNessico.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testNessico.getDate_demande()).isEqualTo(UPDATED_DATE_DEMANDE);
        assertThat(testNessico.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void putNonExistingNessico() throws Exception {
        int databaseSizeBeforeUpdate = nessicoRepository.findAll().size();
        nessico.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNessicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nessico.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nessico))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nessico in the database
        List<Nessico> nessicoList = nessicoRepository.findAll();
        assertThat(nessicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNessico() throws Exception {
        int databaseSizeBeforeUpdate = nessicoRepository.findAll().size();
        nessico.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNessicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nessico))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nessico in the database
        List<Nessico> nessicoList = nessicoRepository.findAll();
        assertThat(nessicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNessico() throws Exception {
        int databaseSizeBeforeUpdate = nessicoRepository.findAll().size();
        nessico.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNessicoMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nessico))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nessico in the database
        List<Nessico> nessicoList = nessicoRepository.findAll();
        assertThat(nessicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNessicoWithPatch() throws Exception {
        // Initialize the database
        nessicoRepository.saveAndFlush(nessico);

        int databaseSizeBeforeUpdate = nessicoRepository.findAll().size();

        // Update the nessico using partial update
        Nessico partialUpdatedNessico = new Nessico();
        partialUpdatedNessico.setId(nessico.getId());

        partialUpdatedNessico.nom_application(UPDATED_NOM_APPLICATION);

        restNessicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNessico.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNessico))
            )
            .andExpect(status().isOk());

        // Validate the Nessico in the database
        List<Nessico> nessicoList = nessicoRepository.findAll();
        assertThat(nessicoList).hasSize(databaseSizeBeforeUpdate);
        Nessico testNessico = nessicoList.get(nessicoList.size() - 1);
        assertThat(testNessico.getNom_application()).isEqualTo(UPDATED_NOM_APPLICATION);
        assertThat(testNessico.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testNessico.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testNessico.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testNessico.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testNessico.getDate_demande()).isEqualTo(DEFAULT_DATE_DEMANDE);
        assertThat(testNessico.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdateNessicoWithPatch() throws Exception {
        // Initialize the database
        nessicoRepository.saveAndFlush(nessico);

        int databaseSizeBeforeUpdate = nessicoRepository.findAll().size();

        // Update the nessico using partial update
        Nessico partialUpdatedNessico = new Nessico();
        partialUpdatedNessico.setId(nessico.getId());

        partialUpdatedNessico
            .nom_application(UPDATED_NOM_APPLICATION)
            .action(UPDATED_ACTION)
            .password(UPDATED_PASSWORD)
            .status(UPDATED_STATUS)
            .message(UPDATED_MESSAGE)
            .date_demande(UPDATED_DATE_DEMANDE)
            .userId(UPDATED_USER_ID);

        restNessicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNessico.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNessico))
            )
            .andExpect(status().isOk());

        // Validate the Nessico in the database
        List<Nessico> nessicoList = nessicoRepository.findAll();
        assertThat(nessicoList).hasSize(databaseSizeBeforeUpdate);
        Nessico testNessico = nessicoList.get(nessicoList.size() - 1);
        assertThat(testNessico.getNom_application()).isEqualTo(UPDATED_NOM_APPLICATION);
        assertThat(testNessico.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testNessico.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testNessico.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testNessico.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testNessico.getDate_demande()).isEqualTo(UPDATED_DATE_DEMANDE);
        assertThat(testNessico.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingNessico() throws Exception {
        int databaseSizeBeforeUpdate = nessicoRepository.findAll().size();
        nessico.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNessicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nessico.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nessico))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nessico in the database
        List<Nessico> nessicoList = nessicoRepository.findAll();
        assertThat(nessicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNessico() throws Exception {
        int databaseSizeBeforeUpdate = nessicoRepository.findAll().size();
        nessico.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNessicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nessico))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nessico in the database
        List<Nessico> nessicoList = nessicoRepository.findAll();
        assertThat(nessicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNessico() throws Exception {
        int databaseSizeBeforeUpdate = nessicoRepository.findAll().size();
        nessico.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNessicoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nessico))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nessico in the database
        List<Nessico> nessicoList = nessicoRepository.findAll();
        assertThat(nessicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNessico() throws Exception {
        // Initialize the database
        nessicoRepository.saveAndFlush(nessico);

        int databaseSizeBeforeDelete = nessicoRepository.findAll().size();

        // Delete the nessico
        restNessicoMockMvc
            .perform(delete(ENTITY_API_URL_ID, nessico.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Nessico> nessicoList = nessicoRepository.findAll();
        assertThat(nessicoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
