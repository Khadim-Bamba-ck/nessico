package com.smm.smo.web.rest;

import com.smm.smo.domain.Nessico;
import com.smm.smo.repository.NessicoRepository;
import com.smm.smo.service.NessicoService;
import com.smm.smo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.smm.smo.domain.Nessico}.
 */
@RestController
@RequestMapping("/api")
public class NessicoResource {

    private final Logger log = LoggerFactory.getLogger(NessicoResource.class);

    private static final String ENTITY_NAME = "nessicoNessico";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NessicoService nessicoService;

    private final NessicoRepository nessicoRepository;

    public NessicoResource(NessicoService nessicoService, NessicoRepository nessicoRepository) {
        this.nessicoService = nessicoService;
        this.nessicoRepository = nessicoRepository;
    }

    /**
     * {@code POST  /nessicos} : Create a new nessico.
     *
     * @param nessico the nessico to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nessico, or with status {@code 400 (Bad Request)} if the nessico has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nessicos")
    public ResponseEntity<Nessico> createNessico(@Valid @RequestBody Nessico nessico) throws URISyntaxException {
        log.debug("REST request to save Nessico : {}", nessico);
        if (nessico.getId() != null) {
            throw new BadRequestAlertException("A new nessico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Nessico result = nessicoService.save(nessico);
        return ResponseEntity
            .created(new URI("/api/nessicos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nessicos/:id} : Updates an existing nessico.
     *
     * @param id the id of the nessico to save.
     * @param nessico the nessico to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nessico,
     * or with status {@code 400 (Bad Request)} if the nessico is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nessico couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nessicos/{id}")
    public ResponseEntity<Nessico> updateNessico(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Nessico nessico
    ) throws URISyntaxException {
        log.debug("REST request to update Nessico : {}, {}", id, nessico);
        if (nessico.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nessico.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nessicoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Nessico result = nessicoService.update(nessico);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nessico.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /nessicos/:id} : Partial updates given fields of an existing nessico, field will ignore if it is null
     *
     * @param id the id of the nessico to save.
     * @param nessico the nessico to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nessico,
     * or with status {@code 400 (Bad Request)} if the nessico is not valid,
     * or with status {@code 404 (Not Found)} if the nessico is not found,
     * or with status {@code 500 (Internal Server Error)} if the nessico couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/nessicos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Nessico> partialUpdateNessico(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Nessico nessico
    ) throws URISyntaxException {
        log.debug("REST request to partial update Nessico partially : {}, {}", id, nessico);
        if (nessico.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nessico.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nessicoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Nessico> result = nessicoService.partialUpdate(nessico);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nessico.getId().toString())
        );
    }

    /**
     * {@code GET  /nessicos} : get all the nessicos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nessicos in body.
     */
    @GetMapping("/nessicos")
    public ResponseEntity<List<Nessico>> getAllNessicos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Nessicos");
        Page<Nessico> page = nessicoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nessicos/:id} : get the "id" nessico.
     *
     * @param id the id of the nessico to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nessico, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nessicos/{id}")
    public ResponseEntity<Nessico> getNessico(@PathVariable Long id) {
        log.debug("REST request to get Nessico : {}", id);
        Optional<Nessico> nessico = nessicoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nessico);
    }

    /**
     * {@code DELETE  /nessicos/:id} : delete the "id" nessico.
     *
     * @param id the id of the nessico to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nessicos/{id}")
    public ResponseEntity<Void> deleteNessico(@PathVariable Long id) {
        log.debug("REST request to delete Nessico : {}", id);
        nessicoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
