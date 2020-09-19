package irongym.web.rest;

import irongym.domain.Caisse;
import irongym.service.CaisseService;
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
 * REST controller for managing {@link irongym.domain.Caisse}.
 */
@RestController
@RequestMapping("/api")
public class CaisseResource {

    private final Logger log = LoggerFactory.getLogger(CaisseResource.class);

    private static final String ENTITY_NAME = "caisse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CaisseService caisseService;

    public CaisseResource(CaisseService caisseService) {
        this.caisseService = caisseService;
    }

    /**
     * {@code POST  /caisses} : Create a new caisse.
     *
     * @param caisse the caisse to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new caisse, or with status {@code 400 (Bad Request)} if the caisse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/caisses")
    public ResponseEntity<Caisse> createCaisse(@Valid @RequestBody Caisse caisse) throws URISyntaxException {
        log.debug("REST request to save Caisse : {}", caisse);
        if (caisse.getId() != null) {
            throw new BadRequestAlertException("A new caisse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Caisse result = caisseService.save(caisse);
        return ResponseEntity.created(new URI("/api/caisses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /caisses} : Updates an existing caisse.
     *
     * @param caisse the caisse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caisse,
     * or with status {@code 400 (Bad Request)} if the caisse is not valid,
     * or with status {@code 500 (Internal Server Error)} if the caisse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/caisses")
    public ResponseEntity<Caisse> updateCaisse(@Valid @RequestBody Caisse caisse) throws URISyntaxException {
        log.debug("REST request to update Caisse : {}", caisse);
        if (caisse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Caisse result = caisseService.save(caisse);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, caisse.getId()))
            .body(result);
    }

    /**
     * {@code GET  /caisses} : get all the caisses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of caisses in body.
     */
    @GetMapping("/caisses")
    public ResponseEntity<List<Caisse>> getAllCaisses(Pageable pageable) {
        log.debug("REST request to get a page of Caisses");
        Page<Caisse> page = caisseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /caisses/:id} : get the "id" caisse.
     *
     * @param id the id of the caisse to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the caisse, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/caisses/{id}")
    public ResponseEntity<Caisse> getCaisse(@PathVariable String id) {
        log.debug("REST request to get Caisse : {}", id);
        Optional<Caisse> caisse = caisseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(caisse);
    }

    /**
     * {@code DELETE  /caisses/:id} : delete the "id" caisse.
     *
     * @param id the id of the caisse to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/caisses/{id}")
    public ResponseEntity<Void> deleteCaisse(@PathVariable String id) {
        log.debug("REST request to delete Caisse : {}", id);
        caisseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/caisses?query=:query} : search for the caisse corresponding
     * to the query.
     *
     * @param query the query of the caisse search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/caisses")
    public ResponseEntity<List<Caisse>> searchCaisses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Caisses for query {}", query);
        Page<Caisse> page = caisseService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
