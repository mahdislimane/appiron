package irongym.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

import irongym.domain.enumeration.JobTitle;

/**
 * A Employee.
 */
@Document(collection = "employee")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("full_name")
    private String fullName;

    @NotNull
    @Field("job_title")
    private JobTitle jobTitle;

    @NotNull
    @Field("phone_number")
    private String phoneNumber;

    @NotNull
    @Field("cin")
    private String cin;

    @NotNull
    @Field("hire_date")
    private Instant hireDate;

    @NotNull
    @Field("salary")
    private Double salary;

    @Field("end_date")
    private Instant endDate;

    @DBRef
    @Field("payments")
    @JsonIgnoreProperties(value = "employees", allowSetters = true)
    private Payment payments;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public Employee fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public JobTitle getJobTitle() {
        return jobTitle;
    }

    public Employee jobTitle(JobTitle jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    public void setJobTitle(JobTitle jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Employee phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCin() {
        return cin;
    }

    public Employee cin(String cin) {
        this.cin = cin;
        return this;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public Instant getHireDate() {
        return hireDate;
    }

    public Employee hireDate(Instant hireDate) {
        this.hireDate = hireDate;
        return this;
    }

    public void setHireDate(Instant hireDate) {
        this.hireDate = hireDate;
    }

    public Double getSalary() {
        return salary;
    }

    public Employee salary(Double salary) {
        this.salary = salary;
        return this;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public Employee endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Payment getPayments() {
        return payments;
    }

    public Employee payments(Payment payment) {
        this.payments = payment;
        return this;
    }

    public void setPayments(Payment payment) {
        this.payments = payment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", cin='" + getCin() + "'" +
            ", hireDate='" + getHireDate() + "'" +
            ", salary=" + getSalary() +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
