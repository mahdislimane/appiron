package irongym.web.rest;

import irongym.AppironApp;
import irongym.domain.Abonne;
import irongym.domain.Abonnement;
import irongym.repository.AbonneRepository;
import irongym.repository.search.AbonneSearchRepository;
import irongym.service.AbonneService;

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

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AbonneResource} REST controller.
 */
@SpringBootTest(classes = AppironApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class AbonneResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CIN = "AAAAAAAAAA";
    private static final String UPDATED_CIN = "BBBBBBBBBB";

    private static final String DEFAULT_CARD = "AAAAAAAAAA";
    private static final String UPDATED_CARD = "BBBBBBBBBB";

    @Autowired
    private AbonneRepository abonneRepository;

    @Autowired
    private AbonneService abonneService;

    /**
     * This repository is mocked in the irongym.repository.search test package.
     *
     * @see irongym.repository.search.AbonneSearchRepositoryMockConfiguration
     */
    @Autowired
    private AbonneSearchRepository mockAbonneSearchRepository;

    @Autowired
    private MockMvc restAbonneMockMvc;

    private Abonne abonne;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Abonne createEntity() {
        Abonne abonne = new Abonne()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .cin(DEFAULT_CIN)
            .card(DEFAULT_CARD);
        // Add required entity
        Abonnement abonnement;
        abonnement = AbonnementResourceIT.createEntity();
        abonnement.setId("fixed-id-for-tests");
        abonne.setAbonnements(abonnement);
        return abonne;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Abonne createUpdatedEntity() {
        Abonne abonne = new Abonne()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .cin(UPDATED_CIN)
            .card(UPDATED_CARD);
        // Add required entity
        Abonnement abonnement;
        abonnement = AbonnementResourceIT.createUpdatedEntity();
        abonnement.setId("fixed-id-for-tests");
        abonne.setAbonnements(abonnement);
        return abonne;
    }

    @BeforeEach
    public void initTest() {
        abonneRepository.deleteAll();
        abonne = createEntity();
    }

    @Test
    public void createAbonne() throws Exception {
        int databaseSizeBeforeCreate = abonneRepository.findAll().size();
        // Create the Abonne
        restAbonneMockMvc.perform(post("/api/abonnes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(abonne)))
            .andExpect(status().isCreated());

        // Validate the Abonne in the database
        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeCreate + 1);
        Abonne testAbonne = abonneList.get(abonneList.size() - 1);
        assertThat(testAbonne.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAbonne.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAbonne.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testAbonne.getCin()).isEqualTo(DEFAULT_CIN);
        assertThat(testAbonne.getCard()).isEqualTo(DEFAULT_CARD);

        // Validate the Abonne in Elasticsearch
        verify(mockAbonneSearchRepository, times(1)).save(testAbonne);
    }

    @Test
    public void createAbonneWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = abonneRepository.findAll().size();

        // Create the Abonne with an existing ID
        abonne.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restAbonneMockMvc.perform(post("/api/abonnes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(abonne)))
            .andExpect(status().isBadRequest());

        // Validate the Abonne in the database
        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeCreate);

        // Validate the Abonne in Elasticsearch
        verify(mockAbonneSearchRepository, times(0)).save(abonne);
    }


    @Test
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonneRepository.findAll().size();
        // set the field null
        abonne.setFirstName(null);

        // Create the Abonne, which fails.


        restAbonneMockMvc.perform(post("/api/abonnes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(abonne)))
            .andExpect(status().isBadRequest());

        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonneRepository.findAll().size();
        // set the field null
        abonne.setLastName(null);

        // Create the Abonne, which fails.


        restAbonneMockMvc.perform(post("/api/abonnes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(abonne)))
            .andExpect(status().isBadRequest());

        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonneRepository.findAll().size();
        // set the field null
        abonne.setPhoneNumber(null);

        // Create the Abonne, which fails.


        restAbonneMockMvc.perform(post("/api/abonnes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(abonne)))
            .andExpect(status().isBadRequest());

        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllAbonnes() throws Exception {
        // Initialize the database
        abonneRepository.save(abonne);

        // Get all the abonneList
        restAbonneMockMvc.perform(get("/api/abonnes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(abonne.getId())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].cin").value(hasItem(DEFAULT_CIN)))
            .andExpect(jsonPath("$.[*].card").value(hasItem(DEFAULT_CARD)));
    }
    
    @Test
    public void getAbonne() throws Exception {
        // Initialize the database
        abonneRepository.save(abonne);

        // Get the abonne
        restAbonneMockMvc.perform(get("/api/abonnes/{id}", abonne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(abonne.getId()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.cin").value(DEFAULT_CIN))
            .andExpect(jsonPath("$.card").value(DEFAULT_CARD));
    }
    @Test
    public void getNonExistingAbonne() throws Exception {
        // Get the abonne
        restAbonneMockMvc.perform(get("/api/abonnes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateAbonne() throws Exception {
        // Initialize the database
        abonneService.save(abonne);

        int databaseSizeBeforeUpdate = abonneRepository.findAll().size();

        // Update the abonne
        Abonne updatedAbonne = abonneRepository.findById(abonne.getId()).get();
        updatedAbonne
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .cin(UPDATED_CIN)
            .card(UPDATED_CARD);

        restAbonneMockMvc.perform(put("/api/abonnes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAbonne)))
            .andExpect(status().isOk());

        // Validate the Abonne in the database
        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeUpdate);
        Abonne testAbonne = abonneList.get(abonneList.size() - 1);
        assertThat(testAbonne.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAbonne.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAbonne.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testAbonne.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testAbonne.getCard()).isEqualTo(UPDATED_CARD);

        // Validate the Abonne in Elasticsearch
        verify(mockAbonneSearchRepository, times(2)).save(testAbonne);
    }

    @Test
    public void updateNonExistingAbonne() throws Exception {
        int databaseSizeBeforeUpdate = abonneRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbonneMockMvc.perform(put("/api/abonnes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(abonne)))
            .andExpect(status().isBadRequest());

        // Validate the Abonne in the database
        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Abonne in Elasticsearch
        verify(mockAbonneSearchRepository, times(0)).save(abonne);
    }

    @Test
    public void deleteAbonne() throws Exception {
        // Initialize the database
        abonneService.save(abonne);

        int databaseSizeBeforeDelete = abonneRepository.findAll().size();

        // Delete the abonne
        restAbonneMockMvc.perform(delete("/api/abonnes/{id}", abonne.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Abonne in Elasticsearch
        verify(mockAbonneSearchRepository, times(1)).deleteById(abonne.getId());
    }

    @Test
    public void searchAbonne() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        abonneService.save(abonne);
        when(mockAbonneSearchRepository.search(queryStringQuery("id:" + abonne.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(abonne), PageRequest.of(0, 1), 1));

        // Search the abonne
        restAbonneMockMvc.perform(get("/api/_search/abonnes?query=id:" + abonne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(abonne.getId())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].cin").value(hasItem(DEFAULT_CIN)))
            .andExpect(jsonPath("$.[*].card").value(hasItem(DEFAULT_CARD)));
    }
}
