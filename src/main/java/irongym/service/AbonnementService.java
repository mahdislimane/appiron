package irongym.service;

import irongym.domain.Abonnement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Abonnement}.
 */
public interface AbonnementService {

    /**
     * Save a abonnement.
     *
     * @param abonnement the entity to save.
     * @return the persisted entity.
     */
    Abonnement save(Abonnement abonnement);

    /**
     * Get all the abonnements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Abonnement> findAll(Pageable pageable);


    /**
     * Get the "id" abonnement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Abonnement> findOne(String id);

    /**
     * Delete the "id" abonnement.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * Search for the abonnement corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Abonnement> search(String query, Pageable pageable);
}
