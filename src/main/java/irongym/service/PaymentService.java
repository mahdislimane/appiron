package irongym.service;

import irongym.domain.Payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Payment}.
 */
public interface PaymentService {

    /**
     * Save a payment.
     *
     * @param payment the entity to save.
     * @return the persisted entity.
     */
    Payment save(Payment payment);

    /**
     * Get all the payments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Payment> findAll(Pageable pageable);


    /**
     * Get the "id" payment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Payment> findOne(String id);

    /**
     * Delete the "id" payment.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * Search for the payment corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Payment> search(String query, Pageable pageable);
}
