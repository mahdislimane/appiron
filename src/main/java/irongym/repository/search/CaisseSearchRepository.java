package irongym.repository.search;

import irongym.domain.Caisse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Caisse} entity.
 */
public interface CaisseSearchRepository extends ElasticsearchRepository<Caisse, String> {
}
