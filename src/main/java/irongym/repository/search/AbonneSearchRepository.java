package irongym.repository.search;

import irongym.domain.Abonne;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Abonne} entity.
 */
public interface AbonneSearchRepository extends ElasticsearchRepository<Abonne, String> {
}
