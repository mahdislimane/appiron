package irongym.repository.search;

import irongym.domain.Abonnement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Abonnement} entity.
 */
public interface AbonnementSearchRepository extends ElasticsearchRepository<Abonnement, String> {
}
