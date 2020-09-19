package irongym.repository;

import irongym.domain.Abonnement;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Abonnement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AbonnementRepository extends MongoRepository<Abonnement, String> {
}
