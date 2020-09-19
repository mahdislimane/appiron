package irongym.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import irongym.domain.enumeration.Departement;

import irongym.domain.enumeration.AbType;

/**
 * A Abonnement.
 */
@Document(collection = "abonnement")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "abonnement")
public class Abonnement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("departement")
    private Departement departement;

    @NotNull
    @Field("ab_type")
    private AbType abType;

    @NotNull
    @Field("date")
    private Instant date;

    @NotNull
    @Field("price")
    private Double price;

    @NotNull
    @Field("pay")
    private Double pay;

    @DBRef
    @Field("abonne")
    private Set<Abonne> abonnes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Departement getDepartement() {
        return departement;
    }

    public Abonnement departement(Departement departement) {
        this.departement = departement;
        return this;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public AbType getAbType() {
        return abType;
    }

    public Abonnement abType(AbType abType) {
        this.abType = abType;
        return this;
    }

    public void setAbType(AbType abType) {
        this.abType = abType;
    }

    public Instant getDate() {
        return date;
    }

    public Abonnement date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public Abonnement price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPay() {
        return pay;
    }

    public Abonnement pay(Double pay) {
        this.pay = pay;
        return this;
    }

    public void setPay(Double pay) {
        this.pay = pay;
    }

    public Set<Abonne> getAbonnes() {
        return abonnes;
    }

    public Abonnement abonnes(Set<Abonne> abonnes) {
        this.abonnes = abonnes;
        return this;
    }

    public Abonnement addAbonne(Abonne abonne) {
        this.abonnes.add(abonne);
        abonne.setAbonnements(this);
        return this;
    }

    public Abonnement removeAbonne(Abonne abonne) {
        this.abonnes.remove(abonne);
        abonne.setAbonnements(null);
        return this;
    }

    public void setAbonnes(Set<Abonne> abonnes) {
        this.abonnes = abonnes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Abonnement)) {
            return false;
        }
        return id != null && id.equals(((Abonnement) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Abonnement{" +
            "id=" + getId() +
            ", departement='" + getDepartement() + "'" +
            ", abType='" + getAbType() + "'" +
            ", date='" + getDate() + "'" +
            ", price=" + getPrice() +
            ", pay=" + getPay() +
            "}";
    }
}
