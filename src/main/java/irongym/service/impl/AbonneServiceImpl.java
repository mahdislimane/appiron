package irongym.service.impl;

import irongym.service.AbonneService;
import irongym.domain.Abonne;
import irongym.repository.AbonneRepository;
import irongym.repository.search.AbonneSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Abonne}.
 */
@Service
public class AbonneServiceImpl implements AbonneService {

    private final Logger log = LoggerFactory.getLogger(AbonneServiceImpl.class);

    private final AbonneRepository abonneRepository;

    private final AbonneSearchRepository abonneSearchRepository;

    public AbonneServiceImpl(AbonneRepository abonneRepository, AbonneSearchRepository abonneSearchRepository) {
        this.abonneRepository = abonneRepository;
        this.abonneSearchRepository = abonneSearchRepository;
    }

    @Override
    public Abonne save(Abonne abonne) {
        log.debug("Request to save Abonne : {}", abonne);
        Abonne result = abonneRepository.save(abonne);
        abonneSearchRepository.save(result);
        return result;
    }

    @Override
    public Page<Abonne> findAll(Pageable pageable) {
        log.debug("Request to get all Abonnes");
        return abonneRepository.findAll(pageable);
    }


    @Override
    public Optional<Abonne> findOne(String id) {
        log.debug("Request to get Abonne : {}", id);
        return abonneRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Abonne : {}", id);
        abonneRepository.deleteById(id);
        abonneSearchRepository.deleteById(id);
    }

    @Override
    public Page<Abonne> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Abonnes for query {}", query);
        return abonneSearchRepository.search(queryStringQuery(query), pageable);    }
}
