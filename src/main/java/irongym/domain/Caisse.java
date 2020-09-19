package irongym.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

import irongym.domain.enumeration.DaylyType;

/**
 * A Caisse.
 */
@Document(collection = "caisse")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "caisse")
public class Caisse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("date")
    private Instant date;

    @NotNull
    @Field("valeur")
    private Double valeur;

    @NotNull
    @Field("dayly_type")
    private DaylyType daylyType;

    @NotNull
    @Field("operateur")
    private String operateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public Caisse date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Double getValeur() {
        return valeur;
    }

    public Caisse valeur(Double valeur) {
        this.valeur = valeur;
        return this;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public DaylyType getDaylyType() {
        return daylyType;
    }

    public Caisse daylyType(DaylyType daylyType) {
        this.daylyType = daylyType;
        return this;
    }

    public void setDaylyType(DaylyType daylyType) {
        this.daylyType = daylyType;
    }

    public String getOperateur() {
        return operateur;
    }

    public Caisse operateur(String operateur) {
        this.operateur = operateur;
        return this;
    }

    public void setOperateur(String operateur) {
        this.operateur = operateur;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Caisse)) {
            return false;
        }
        return id != null && id.equals(((Caisse) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Caisse{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", valeur=" + getValeur() +
            ", daylyType='" + getDaylyType() + "'" +
            ", operateur='" + getOperateur() + "'" +
            "}";
    }
}
