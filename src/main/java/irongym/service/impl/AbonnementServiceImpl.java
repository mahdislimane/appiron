package irongym.service.impl;

import irongym.service.AbonnementService;
import irongym.domain.Abonnement;
import irongym.repository.AbonnementRepository;
import irongym.repository.search.AbonnementSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Abonnement}.
 */
@Service
public class AbonnementServiceImpl implements AbonnementService {

    private final Logger log = LoggerFactory.getLogger(AbonnementServiceImpl.class);

    private final AbonnementRepository abonnementRepository;

    private final AbonnementSearchRepository abonnementSearchRepository;

    public AbonnementServiceImpl(AbonnementRepository abonnementRepository, AbonnementSearchRepository abonnementSearchRepository) {
        this.abonnementRepository = abonnementRepository;
        this.abonnementSearchRepository = abonnementSearchRepository;
    }

    @Override
    public Abonnement save(Abonnement abonnement) {
        log.debug("Request to save Abonnement : {}", abonnement);
        Abonnement result = abonnementRepository.save(abonnement);
        abonnementSearchRepository.save(result);
        return result;
    }

    @Override
    public Page<Abonnement> findAll(Pageable pageable) {
        log.debug("Request to get all Abonnements");
        return abonnementRepository.findAll(pageable);
    }


    @Override
    public Optional<Abonnement> findOne(String id) {
        log.debug("Request to get Abonnement : {}", id);
        return abonnementRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Abonnement : {}", id);
        abonnementRepository.deleteById(id);
        abonnementSearchRepository.deleteById(id);
    }

    @Override
    public Page<Abonnement> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Abonnements for query {}", query);
        return abonnementSearchRepository.search(queryStringQuery(query), pageable);    }
}
