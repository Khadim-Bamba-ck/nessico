package com.smm.smo.service;

import com.smm.smo.domain.Nessico;
import com.smm.smo.repository.NessicoRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Nessico}.
 */
@Service
@Transactional
public class NessicoService {

    private final Logger log = LoggerFactory.getLogger(NessicoService.class);

    private final NessicoRepository nessicoRepository;

    public NessicoService(NessicoRepository nessicoRepository) {
        this.nessicoRepository = nessicoRepository;
    }

    /**
     * Save a nessico.
     *
     * @param nessico the entity to save.
     * @return the persisted entity.
     */
    public Nessico save(Nessico nessico) {
        log.debug("Request to save Nessico : {}", nessico);
        return nessicoRepository.save(nessico);
    }

    /**
     * Update a nessico.
     *
     * @param nessico the entity to save.
     * @return the persisted entity.
     */
    public Nessico update(Nessico nessico) {
        log.debug("Request to save Nessico : {}", nessico);
        return nessicoRepository.save(nessico);
    }

    /**
     * Partially update a nessico.
     *
     * @param nessico the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Nessico> partialUpdate(Nessico nessico) {
        log.debug("Request to partially update Nessico : {}", nessico);

        return nessicoRepository
            .findById(nessico.getId())
            .map(existingNessico -> {
                if (nessico.getNom_application() != null) {
                    existingNessico.setNom_application(nessico.getNom_application());
                }
                if (nessico.getAction() != null) {
                    existingNessico.setAction(nessico.getAction());
                }
                if (nessico.getPassword() != null) {
                    existingNessico.setPassword(nessico.getPassword());
                }
                if (nessico.getStatus() != null) {
                    existingNessico.setStatus(nessico.getStatus());
                }
                if (nessico.getMessage() != null) {
                    existingNessico.setMessage(nessico.getMessage());
                }
                if (nessico.getDate_demande() != null) {
                    existingNessico.setDate_demande(nessico.getDate_demande());
                }
                if (nessico.getUserId() != null) {
                    existingNessico.setUserId(nessico.getUserId());
                }

                return existingNessico;
            })
            .map(nessicoRepository::save);
    }

    /**
     * Get all the nessicos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Nessico> findAll(Pageable pageable) {
        log.debug("Request to get all Nessicos");
        return nessicoRepository.findAll(pageable);
    }

    /**
     * Get one nessico by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Nessico> findOne(Long id) {
        log.debug("Request to get Nessico : {}", id);
        return nessicoRepository.findById(id);
    }

    /**
     * Delete the nessico by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Nessico : {}", id);
        nessicoRepository.deleteById(id);
    }
}
