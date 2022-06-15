package com.smm.smo.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Nessico.
 */
@Entity
@Table(name = "nessico")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Nessico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_application")
    private String nom_application;

    @NotNull
    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @NotNull
    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "date_demande")
    private LocalDate date_demande;

    @Column(name = "user_id")
    private String userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Nessico id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom_application() {
        return this.nom_application;
    }

    public Nessico nom_application(String nom_application) {
        this.setNom_application(nom_application);
        return this;
    }

    public void setNom_application(String nom_application) {
        this.nom_application = nom_application;
    }

    public String getAction() {
        return this.action;
    }

    public Nessico action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPassword() {
        return this.password;
    }

    public Nessico password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return this.status;
    }

    public Nessico status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public Nessico message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getDate_demande() {
        return this.date_demande;
    }

    public Nessico date_demande(LocalDate date_demande) {
        this.setDate_demande(date_demande);
        return this;
    }

    public void setDate_demande(LocalDate date_demande) {
        this.date_demande = date_demande;
    }

    public String getUserId() {
        return this.userId;
    }

    public Nessico userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Nessico)) {
            return false;
        }
        return id != null && id.equals(((Nessico) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Nessico{" +
            "id=" + getId() +
            ", nom_application='" + getNom_application() + "'" +
            ", action='" + getAction() + "'" +
            ", password='" + getPassword() + "'" +
            ", status='" + getStatus() + "'" +
            ", message='" + getMessage() + "'" +
            ", date_demande='" + getDate_demande() + "'" +
            ", userId='" + getUserId() + "'" +
            "}";
    }
}
