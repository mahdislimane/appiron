package irongym.web.rest;

import irongym.AppironApp;
import irongym.domain.Caisse;
import irongym.repository.CaisseRepository;
import irongym.repository.search.CaisseSearchRepository;
import irongym.service.CaisseService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import irongym.domain.enumeration.DaylyType;
/**
 * Integration tests for the {@link CaisseResource} REST controller.
 */
@SpringBootTest(classes = AppironApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CaisseResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_VALEUR = 1D;
    private static final Double UPDATED_VALEUR = 2D;

    private static final DaylyType DEFAULT_DAYLY_TYPE = DaylyType.OTHERS_INCOM;
    private static final DaylyType UPDATED_DAYLY_TYPE = DaylyType.ABONNEMENT;

    private static final String DEFAULT_OPERATEUR = "AAAAAAAAAA";
    private static final String UPDATED_OPERATEUR = "BBBBBBBBBB";

    @Autowired
    private CaisseRepository caisseRepository;

    @Autowired
    private CaisseService caisseService;

    /**
     * This repository is mocked in the irongym.repository.search test package.
     *
     * @see irongym.repository.search.CaisseSearchRepositoryMockConfiguration
     */
    @Autowired
    private CaisseSearchRepository mockCaisseSearchRepository;

    @Autowired
    private MockMvc restCaisseMockMvc;

    private Caisse caisse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caisse createEntity() {
        Caisse caisse = new Caisse()
            .date(DEFAULT_DATE)
            .valeur(DEFAULT_VALEUR)
            .daylyType(DEFAULT_DAYLY_TYPE)
            .operateur(DEFAULT_OPERATEUR);
        return caisse;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caisse createUpdatedEntity() {
        Caisse caisse = new Caisse()
            .date(UPDATED_DATE)
            .valeur(UPDATED_VALEUR)
            .daylyType(UPDATED_DAYLY_TYPE)
            .operateur(UPDATED_OPERATEUR);
        return caisse;
    }

    @BeforeEach
    public void initTest() {
        caisseRepository.deleteAll();
        caisse = createEntity();
    }

    @Test
    public void createCaisse() throws Exception {
        int databaseSizeBeforeCreate = caisseRepository.findAll().size();
        // Create the Caisse
        restCaisseMockMvc.perform(post("/api/caisses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(caisse)))
            .andExpect(status().isCreated());

        // Validate the Caisse in the database
        List<Caisse> caisseList = caisseRepository.findAll();
        assertThat(caisseList).hasSize(databaseSizeBeforeCreate + 1);
        Caisse testCaisse = caisseList.get(caisseList.size() - 1);
        assertThat(testCaisse.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testCaisse.getValeur()).isEqualTo(DEFAULT_VALEUR);
        assertThat(testCaisse.getDaylyType()).isEqualTo(DEFAULT_DAYLY_TYPE);
        assertThat(testCaisse.getOperateur()).isEqualTo(DEFAULT_OPERATEUR);

        // Validate the Caisse in Elasticsearch
        verify(mockCaisseSearchRepository, times(1)).save(testCaisse);
    }

    @Test
    public void createCaisseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = caisseRepository.findAll().size();

        // Create the Caisse with an existing ID
        caisse.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restCaisseMockMvc.perform(post("/api/caisses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(caisse)))
            .andExpect(status().isBadRequest());

        // Validate the Caisse in the database
        List<Caisse> caisseList = caisseRepository.findAll();
        assertThat(caisseList).hasSize(databaseSizeBeforeCreate);

        // Validate the Caisse in Elasticsearch
        verify(mockCaisseSearchRepository, times(0)).save(caisse);
    }


    @Test
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = caisseRepository.findAll().size();
        // set the field null
        caisse.setDate(null);

        // Create the Caisse, which fails.


        restCaisseMockMvc.perform(post("/api/caisses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(caisse)))
            .andExpect(status().isBadRequest());

        List<Caisse> caisseList = caisseRepository.findAll();
        assertThat(caisseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkValeurIsRequired() throws Exception {
        int databaseSizeBeforeTest = caisseRepository.findAll().size();
        // set the field null
        caisse.setValeur(null);

        // Create the Caisse, which fails.


        restCaisseMockMvc.perform(post("/api/caisses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(caisse)))
            .andExpect(status().isBadRequest());

        List<Caisse> caisseList = caisseRepository.findAll();
        assertThat(caisseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDaylyTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = caisseRepository.findAll().size();
        // set the field null
        caisse.setDaylyType(null);

        // Create the Caisse, which fails.


        restCaisseMockMvc.perform(post("/api/caisses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(caisse)))
            .andExpect(status().isBadRequest());

        List<Caisse> caisseList = caisseRepository.findAll();
        assertThat(caisseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkOperateurIsRequired() throws Exception {
        int databaseSizeBeforeTest = caisseRepository.findAll().size();
        // set the field null
        caisse.setOperateur(null);

        // Create the Caisse, which fails.


        restCaisseMockMvc.perform(post("/api/caisses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(caisse)))
            .andExpect(status().isBadRequest());

        List<Caisse> caisseList = caisseRepository.findAll();
        assertThat(caisseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllCaisses() throws Exception {
        // Initialize the database
        caisseRepository.save(caisse);

        // Get all the caisseList
        restCaisseMockMvc.perform(get("/api/caisses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caisse.getId())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR.doubleValue())))
            .andExpect(jsonPath("$.[*].daylyType").value(hasItem(DEFAULT_DAYLY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].operateur").value(hasItem(DEFAULT_OPERATEUR)));
    }
    
    @Test
    public void getCaisse() throws Exception {
        // Initialize the database
        caisseRepository.save(caisse);

        // Get the caisse
        restCaisseMockMvc.perform(get("/api/caisses/{id}", caisse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(caisse.getId()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR.doubleValue()))
            .andExpect(jsonPath("$.daylyType").value(DEFAULT_DAYLY_TYPE.toString()))
            .andExpect(jsonPath("$.operateur").value(DEFAULT_OPERATEUR));
    }
    @Test
    public void getNonExistingCaisse() throws Exception {
        // Get the caisse
        restCaisseMockMvc.perform(get("/api/caisses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateCaisse() throws Exception {
        // Initialize the database
        caisseService.save(caisse);

        int databaseSizeBeforeUpdate = caisseRepository.findAll().size();

        // Update the caisse
        Caisse updatedCaisse = caisseRepository.findById(caisse.getId()).get();
        updatedCaisse
            .date(UPDATED_DATE)
            .valeur(UPDATED_VALEUR)
            .daylyType(UPDATED_DAYLY_TYPE)
            .operateur(UPDATED_OPERATEUR);

        restCaisseMockMvc.perform(put("/api/caisses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCaisse)))
            .andExpect(status().isOk());

        // Validate the Caisse in the database
        List<Caisse> caisseList = caisseRepository.findAll();
        assertThat(caisseList).hasSize(databaseSizeBeforeUpdate);
        Caisse testCaisse = caisseList.get(caisseList.size() - 1);
        assertThat(testCaisse.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCaisse.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testCaisse.getDaylyType()).isEqualTo(UPDATED_DAYLY_TYPE);
        assertThat(testCaisse.getOperateur()).isEqualTo(UPDATED_OPERATEUR);

        // Validate the Caisse in Elasticsearch
        verify(mockCaisseSearchRepository, times(2)).save(testCaisse);
    }

    @Test
    public void updateNonExistingCaisse() throws Exception {
        int databaseSizeBeforeUpdate = caisseRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaisseMockMvc.perform(put("/api/caisses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(caisse)))
            .andExpect(status().isBadRequest());

        // Validate the Caisse in the database
        List<Caisse> caisseList = caisseRepository.findAll();
        assertThat(caisseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Caisse in Elasticsearch
        verify(mockCaisseSearchRepository, times(0)).save(caisse);
    }

    @Test
    public void deleteCaisse() throws Exception {
        // Initialize the database
        caisseService.save(caisse);

        int databaseSizeBeforeDelete = caisseRepository.findAll().size();

        // Delete the caisse
        restCaisseMockMvc.perform(delete("/api/caisses/{id}", caisse.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Caisse> caisseList = caisseRepository.findAll();
        assertThat(caisseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Caisse in Elasticsearch
        verify(mockCaisseSearchRepository, times(1)).deleteById(caisse.getId());
    }

    @Test
    public void searchCaisse() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        caisseService.save(caisse);
        when(mockCaisseSearchRepository.search(queryStringQuery("id:" + caisse.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(caisse), PageRequest.of(0, 1), 1));

        // Search the caisse
        restCaisseMockMvc.perform(get("/api/_search/caisses?query=id:" + caisse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caisse.getId())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR.doubleValue())))
            .andExpect(jsonPath("$.[*].daylyType").value(hasItem(DEFAULT_DAYLY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].operateur").value(hasItem(DEFAULT_OPERATEUR)));
    }
}
