package irongym.service.impl;

import irongym.service.CaisseService;
import irongym.domain.Caisse;
import irongym.repository.CaisseRepository;
import irongym.repository.search.CaisseSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Caisse}.
 */
@Service
public class CaisseServiceImpl implements CaisseService {

    private final Logger log = LoggerFactory.getLogger(CaisseServiceImpl.class);

    private final CaisseRepository caisseRepository;

    private final CaisseSearchRepository caisseSearchRepository;

    public CaisseServiceImpl(CaisseRepository caisseRepository, CaisseSearchRepository caisseSearchRepository) {
        this.caisseRepository = caisseRepository;
        this.caisseSearchRepository = caisseSearchRepository;
    }

    @Override
    public Caisse save(Caisse caisse) {
        log.debug("Request to save Caisse : {}", caisse);
        Caisse result = caisseRepository.save(caisse);
        caisseSearchRepository.save(result);
        return result;
    }

    @Override
    public Page<Caisse> findAll(Pageable pageable) {
        log.debug("Request to get all Caisses");
        return caisseRepository.findAll(pageable);
    }


    @Override
    public Optional<Caisse> findOne(String id) {
        log.debug("Request to get Caisse : {}", id);
        return caisseRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Caisse : {}", id);
        caisseRepository.deleteById(id);
        caisseSearchRepository.deleteById(id);
    }

    @Override
    public Page<Caisse> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Caisses for query {}", query);
        return caisseSearchRepository.search(queryStringQuery(query), pageable);    }
}
