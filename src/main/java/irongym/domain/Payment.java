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

import irongym.domain.enumeration.Month;

/**
 * A Payment.
 */
@Document(collection = "payment")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("date")
    private Instant date;

    @NotNull
    @Field("avance")
    private Double avance;

    @NotNull
    @Field("month")
    private Month month;

    @NotNull
    @Field("year")
    private Integer year;

    @DBRef
    @Field("employee")
    private Set<Employee> employees = new HashSet<>();

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

    public Payment date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Double getAvance() {
        return avance;
    }

    public Payment avance(Double avance) {
        this.avance = avance;
        return this;
    }

    public void setAvance(Double avance) {
        this.avance = avance;
    }

    public Month getMonth() {
        return month;
    }

    public Payment month(Month month) {
        this.month = month;
        return this;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public Payment year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public Payment employees(Set<Employee> employees) {
        this.employees = employees;
        return this;
    }

    public Payment addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setPayments(this);
        return this;
    }

    public Payment removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.setPayments(null);
        return this;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", avance=" + getAvance() +
            ", month='" + getMonth() + "'" +
            ", year=" + getYear() +
            "}";
    }
}
