package irongym.service;

import irongym.domain.Abonne;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Abonne}.
 */
public interface AbonneService {

    /**
     * Save a abonne.
     *
     * @param abonne the entity to save.
     * @return the persisted entity.
     */
    Abonne save(Abonne abonne);

    /**
     * Get all the abonnes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Abonne> findAll(Pageable pageable);


    /**
     * Get the "id" abonne.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Abonne> findOne(String id);

    /**
     * Delete the "id" abonne.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * Search for the abonne corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Abonne> search(String query, Pageable pageable);
}
