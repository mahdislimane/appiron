package irongym.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link AbonnementSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class AbonnementSearchRepositoryMockConfiguration {

    @MockBean
    private AbonnementSearchRepository mockAbonnementSearchRepository;

}
