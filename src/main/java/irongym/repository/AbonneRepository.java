package irongym.repository;

import irongym.domain.Abonne;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Abonne entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AbonneRepository extends MongoRepository<Abonne, String> {
}
