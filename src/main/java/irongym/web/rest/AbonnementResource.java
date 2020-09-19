package irongym.web.rest;

import irongym.domain.Abonnement;
import irongym.service.AbonnementService;
import irongym.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link irongym.domain.Abonnement}.
 */
@RestController
@RequestMapping("/api")
public class AbonnementResource {

    private final Logger log = LoggerFactory.getLogger(AbonnementResource.class);

    private static final String ENTITY_NAME = "abonnement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AbonnementService abonnementService;

    public AbonnementResource(AbonnementService abonnementService) {
        this.abonnementService = abonnementService;
    }

    /**
     * {@code POST  /abonnements} : Create a new abonnement.
     *
     * @param abonnement the abonnement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new abonnement, or with status {@code 400 (Bad Request)} if the abonnement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/abonnements")
    public ResponseEntity<Abonnement> createAbonnement(@Valid @RequestBody Abonnement abonnement) throws URISyntaxException {
        log.debug("REST request to save Abonnement : {}", abonnement);
        if (abonnement.getId() != null) {
            throw new BadRequestAlertException("A new abonnement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Abonnement result = abonnementService.save(abonnement);
        return ResponseEntity.created(new URI("/api/abonnements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /abonnements} : Updates an existing abonnement.
     *
     * @param abonnement the abonnement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated abonnement,
     * or with status {@code 400 (Bad Request)} if the abonnement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the abonnement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/abonnements")
    public ResponseEntity<Abonnement> updateAbonnement(@Valid @RequestBody Abonnement abonnement) throws URISyntaxException {
        log.debug("REST request to update Abonnement : {}", abonnement);
        if (abonnement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Abonnement result = abonnementService.save(abonnement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, abonnement.getId()))
            .body(result);
    }

    /**
     * {@code GET  /abonnements} : get all the abonnements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of abonnements in body.
     */
    @GetMapping("/abonnements")
    public ResponseEntity<List<Abonnement>> getAllAbonnements(Pageable pageable) {
        log.debug("REST request to get a page of Abonnements");
        Page<Abonnement> page = abonnementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /abonnements/:id} : get the "id" abonnement.
     *
     * @param id the id of the abonnement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the abonnement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/abonnements/{id}")
    public ResponseEntity<Abonnement> getAbonnement(@PathVariable String id) {
        log.debug("REST request to get Abonnement : {}", id);
        Optional<Abonnement> abonnement = abonnementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(abonnement);
    }

    /**
     * {@code DELETE  /abonnements/:id} : delete the "id" abonnement.
     *
     * @param id the id of the abonnement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/abonnements/{id}")
    public ResponseEntity<Void> deleteAbonnement(@PathVariable String id) {
        log.debug("REST request to delete Abonnement : {}", id);
        abonnementService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/abonnements?query=:query} : search for the abonnement corresponding
     * to the query.
     *
     * @param query the query of the abonnement search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/abonnements")
    public ResponseEntity<List<Abonnement>> searchAbonnements(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Abonnements for query {}", query);
        Page<Abonnement> page = abonnementService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
