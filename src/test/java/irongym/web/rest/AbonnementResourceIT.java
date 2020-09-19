package irongym.web.rest;

import irongym.AppironApp;
import irongym.domain.Abonnement;
import irongym.repository.AbonnementRepository;
import irongym.repository.search.AbonnementSearchRepository;
import irongym.service.AbonnementService;

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

import irongym.domain.enumeration.Departement;
import irongym.domain.enumeration.AbType;
/**
 * Integration tests for the {@link AbonnementResource} REST controller.
 */
@SpringBootTest(classes = AppironApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class AbonnementResourceIT {

    private static final Departement DEFAULT_DEPARTEMENT = Departement.MUSCULATION;
    private static final Departement UPDATED_DEPARTEMENT = Departement.KICKBOXING;

    private static final AbType DEFAULT_AB_TYPE = AbType.SEANCE;
    private static final AbType UPDATED_AB_TYPE = AbType.SEMAINE1;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Double DEFAULT_PAY = 1D;
    private static final Double UPDATED_PAY = 2D;

    @Autowired
    private AbonnementRepository abonnementRepository;

    @Autowired
    private AbonnementService abonnementService;

    /**
     * This repository is mocked in the irongym.repository.search test package.
     *
     * @see irongym.repository.search.AbonnementSearchRepositoryMockConfiguration
     */
    @Autowired
    private AbonnementSearchRepository mockAbonnementSearchRepository;

    @Autowired
    private MockMvc restAbonnementMockMvc;

    private Abonnement abonnement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Abonnement createEntity() {
        Abonnement abonnement = new Abonnement()
            .departement(DEFAULT_DEPARTEMENT)
            .abType(DEFAULT_AB_TYPE)
            .date(DEFAULT_DATE)
            .price(DEFAULT_PRICE)
            .pay(DEFAULT_PAY);
        return abonnement;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Abonnement createUpdatedEntity() {
        Abonnement abonnement = new Abonnement()
            .departement(UPDATED_DEPARTEMENT)
            .abType(UPDATED_AB_TYPE)
            .date(UPDATED_DATE)
            .price(UPDATED_PRICE)
            .pay(UPDATED_PAY);
        return abonnement;
    }

    @BeforeEach
    public void initTest() {
        abonnementRepository.deleteAll();
        abonnement = createEntity();
    }

    @Test
    public void createAbonnement() throws Exception {
        int databaseSizeBeforeCreate = abonnementRepository.findAll().size();
        // Create the Abonnement
        restAbonnementMockMvc.perform(post("/api/abonnements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(abonnement)))
            .andExpect(status().isCreated());

        // Validate the Abonnement in the database
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeCreate + 1);
        Abonnement testAbonnement = abonnementList.get(abonnementList.size() - 1);
        assertThat(testAbonnement.getDepartement()).isEqualTo(DEFAULT_DEPARTEMENT);
        assertThat(testAbonnement.getAbType()).isEqualTo(DEFAULT_AB_TYPE);
        assertThat(testAbonnement.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAbonnement.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testAbonnement.getPay()).isEqualTo(DEFAULT_PAY);

        // Validate the Abonnement in Elasticsearch
        verify(mockAbonnementSearchRepository, times(1)).save(testAbonnement);
    }

    @Test
    public void createAbonnementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = abonnementRepository.findAll().size();

        // Create the Abonnement with an existing ID
        abonnement.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restAbonnementMockMvc.perform(post("/api/abonnements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(abonnement)))
            .andExpect(status().isBadRequest());

        // Validate the Abonnement in the database
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeCreate);

        // Validate the Abonnement in Elasticsearch
        verify(mockAbonnementSearchRepository, times(0)).save(abonnement);
    }


    @Test
    public void checkDepartementIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonnementRepository.findAll().size();
        // set the field null
        abonnement.setDepartement(null);

        // Create the Abonnement, which fails.


        restAbonnementMockMvc.perform(post("/api/abonnements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(abonnement)))
            .andExpect(status().isBadRequest());

        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkAbTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonnementRepository.findAll().size();
        // set the field null
        abonnement.setAbType(null);

        // Create the Abonnement, which fails.


        restAbonnementMockMvc.perform(post("/api/abonnements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(abonnement)))
            .andExpect(status().isBadRequest());

        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonnementRepository.findAll().size();
        // set the field null
        abonnement.setDate(null);

        // Create the Abonnement, which fails.


        restAbonnementMockMvc.perform(post("/api/abonnements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(abonnement)))
            .andExpect(status().isBadRequest());

        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonnementRepository.findAll().size();
        // set the field null
        abonnement.setPrice(null);

        // Create the Abonnement, which fails.


        restAbonnementMockMvc.perform(post("/api/abonnements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(abonnement)))
            .andExpect(status().isBadRequest());

        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkPayIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonnementRepository.findAll().size();
        // set the field null
        abonnement.setPay(null);

        // Create the Abonnement, which fails.


        restAbonnementMockMvc.perform(post("/api/abonnements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(abonnement)))
            .andExpect(status().isBadRequest());

        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllAbonnements() throws Exception {
        // Initialize the database
        abonnementRepository.save(abonnement);

        // Get all the abonnementList
        restAbonnementMockMvc.perform(get("/api/abonnements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(abonnement.getId())))
            .andExpect(jsonPath("$.[*].departement").value(hasItem(DEFAULT_DEPARTEMENT.toString())))
            .andExpect(jsonPath("$.[*].abType").value(hasItem(DEFAULT_AB_TYPE.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].pay").value(hasItem(DEFAULT_PAY.doubleValue())));
    }
    
    @Test
    public void getAbonnement() throws Exception {
        // Initialize the database
        abonnementRepository.save(abonnement);

        // Get the abonnement
        restAbonnementMockMvc.perform(get("/api/abonnements/{id}", abonnement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(abonnement.getId()))
            .andExpect(jsonPath("$.departement").value(DEFAULT_DEPARTEMENT.toString()))
            .andExpect(jsonPath("$.abType").value(DEFAULT_AB_TYPE.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.pay").value(DEFAULT_PAY.doubleValue()));
    }
    @Test
    public void getNonExistingAbonnement() throws Exception {
        // Get the abonnement
        restAbonnementMockMvc.perform(get("/api/abonnements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateAbonnement() throws Exception {
        // Initialize the database
        abonnementService.save(abonnement);

        int databaseSizeBeforeUpdate = abonnementRepository.findAll().size();

        // Update the abonnement
        Abonnement updatedAbonnement = abonnementRepository.findById(abonnement.getId()).get();
        updatedAbonnement
            .departement(UPDATED_DEPARTEMENT)
            .abType(UPDATED_AB_TYPE)
            .date(UPDATED_DATE)
            .price(UPDATED_PRICE)
            .pay(UPDATED_PAY);

        restAbonnementMockMvc.perform(put("/api/abonnements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAbonnement)))
            .andExpect(status().isOk());

        // Validate the Abonnement in the database
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeUpdate);
        Abonnement testAbonnement = abonnementList.get(abonnementList.size() - 1);
        assertThat(testAbonnement.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testAbonnement.getAbType()).isEqualTo(UPDATED_AB_TYPE);
        assertThat(testAbonnement.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAbonnement.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testAbonnement.getPay()).isEqualTo(UPDATED_PAY);

        // Validate the Abonnement in Elasticsearch
        verify(mockAbonnementSearchRepository, times(2)).save(testAbonnement);
    }

    @Test
    public void updateNonExistingAbonnement() throws Exception {
        int databaseSizeBeforeUpdate = abonnementRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbonnementMockMvc.perform(put("/api/abonnements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(abonnement)))
            .andExpect(status().isBadRequest());

        // Validate the Abonnement in the database
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Abonnement in Elasticsearch
        verify(mockAbonnementSearchRepository, times(0)).save(abonnement);
    }

    @Test
    public void deleteAbonnement() throws Exception {
        // Initialize the database
        abonnementService.save(abonnement);

        int databaseSizeBeforeDelete = abonnementRepository.findAll().size();

        // Delete the abonnement
        restAbonnementMockMvc.perform(delete("/api/abonnements/{id}", abonnement.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Abonnement in Elasticsearch
        verify(mockAbonnementSearchRepository, times(1)).deleteById(abonnement.getId());
    }

    @Test
    public void searchAbonnement() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        abonnementService.save(abonnement);
        when(mockAbonnementSearchRepository.search(queryStringQuery("id:" + abonnement.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(abonnement), PageRequest.of(0, 1), 1));

        // Search the abonnement
        restAbonnementMockMvc.perform(get("/api/_search/abonnements?query=id:" + abonnement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(abonnement.getId())))
            .andExpect(jsonPath("$.[*].departement").value(hasItem(DEFAULT_DEPARTEMENT.toString())))
            .andExpect(jsonPath("$.[*].abType").value(hasItem(DEFAULT_AB_TYPE.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].pay").value(hasItem(DEFAULT_PAY.doubleValue())));
    }
}
