package irongym.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Abonne.
 */
@Document(collection = "abonne")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "abonne")
public class Abonne implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("first_name")
    private String firstName;

    @NotNull
    @Field("last_name")
    private String lastName;

    @NotNull
    @Field("phone_number")
    private String phoneNumber;

    @Field("cin")
    private String cin;

    @Field("card")
    private String card;

    @DBRef
    @Field("abonnements")
    @JsonIgnoreProperties(value = "abonnes", allowSetters = true)
    private Abonnement abonnements;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Abonne firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Abonne lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Abonne phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCin() {
        return cin;
    }

    public Abonne cin(String cin) {
        this.cin = cin;
        return this;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getCard() {
        return card;
    }

    public Abonne card(String card) {
        this.card = card;
        return this;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public Abonnement getAbonnements() {
        return abonnements;
    }

    public Abonne abonnements(Abonnement abonnement) {
        this.abonnements = abonnement;
        return this;
    }

    public void setAbonnements(Abonnement abonnement) {
        this.abonnements = abonnement;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Abonne)) {
            return false;
        }
        return id != null && id.equals(((Abonne) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Abonne{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", cin='" + getCin() + "'" +
            ", card='" + getCard() + "'" +
            "}";
    }
}
