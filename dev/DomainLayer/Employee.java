package DomainLayer;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Employee {
    private long IsraeliId;
    private String firstName;
    private String lastName;
    private long salary;
    private Map<String, Object> termsOfEmployment;
    private Set<String> roles;
    private LocalDate startOfEmployment;
    private boolean isActive;
    private LocalDate creationDate;
    private LocalDate updateDate;


    public Employee( long israeliId, String firstName, String lastName, long salary, Map<String, Object> termsOfEmployment, Set<String> roles, LocalDate startOfEmployment, boolean isActive, LocalDate creationDate, LocalDate updateDate) {
        IsraeliId = israeliId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.termsOfEmployment = termsOfEmployment;
        this.roles = new HashSet<>(roles);
        this.startOfEmployment = startOfEmployment;
        this.isActive = isActive;
        this.creationDate = creationDate; //??
        this.updateDate = updateDate; //??
    }

    public long getIsraeliId() {
        return IsraeliId;
    }

    public void setIsraeliId(long israeliId) {
        IsraeliId = israeliId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public Map<String, Object> getTermsOfEmployment() {
        return termsOfEmployment;
    }

    public void setTermsOfEmployment(Map<String, Object> termsOfEmployment) {
        this.termsOfEmployment = termsOfEmployment;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public LocalDate getStartOfEmployment() {
        return startOfEmployment;
    }

    public void setStartOfEmployment(LocalDate startOfEmployment) {
        this.startOfEmployment = startOfEmployment;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }
}
