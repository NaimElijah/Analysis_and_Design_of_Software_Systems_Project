package DomainLayer;

import DomainLayer.exception.InvalidInputException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Employee {
    private long israeliId; // Fixed capitalization for consistency
    private String firstName;
    private String lastName;
    private long salary;
    private Map<String, Object> termsOfEmployment;
    private Set<String> roles;
    private LocalDate startOfEmployment;
    private boolean isActive;
    private LocalDate creationDate; // Creation date of the employee record
    private LocalDate updateDate; // Last update date of the employee record


    public Employee(long israeliId, String firstName, String lastName, long salary, Map<String, Object> termsOfEmployment, Set<String> roles, LocalDate startOfEmployment, boolean isActive, LocalDate creationDate, LocalDate updateDate) {
        this.israeliId = israeliId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.termsOfEmployment = termsOfEmployment;
        this.roles = new HashSet<>(roles);
        this.startOfEmployment = startOfEmployment;
        this.isActive = isActive;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }

    public long getIsraeliId() {
        return israeliId;
    }

    public void setIsraeliId(long israeliId) {
        if (String.valueOf(israeliId).length() != 9) {
            throw new InvalidInputException("Israeli ID must be 9 digits");
        }
        this.israeliId = israeliId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new InvalidInputException("First name cannot be null or empty");
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new InvalidInputException("Last name cannot be null or empty");
        }
        this.lastName = lastName;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        if (salary <= 0) {
            throw new InvalidInputException("Salary must be greater than zero");
        }
        this.salary = salary;
    }

    public Map<String, Object> getTermsOfEmployment() {
        return termsOfEmployment;
    }

    public void setTermsOfEmployment(Map<String, Object> termsOfEmployment) {
        if (termsOfEmployment == null) {
            throw new InvalidInputException("Terms of employment cannot be null");
        }
        this.termsOfEmployment = termsOfEmployment;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        if (roles == null) {
            throw new InvalidInputException("Roles cannot be null");
        }
        this.roles = new HashSet<>(roles);
    }

    public LocalDate getStartOfEmployment() {
        return startOfEmployment;
    }

    public void setStartOfEmployment(LocalDate startOfEmployment) {
        if (startOfEmployment == null) {
            throw new InvalidInputException("Start of employment date cannot be null");
        }
        this.startOfEmployment = startOfEmployment;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    /**
     * Gets the creation date of the employee record.
     * This date is set when the employee is created and cannot be changed.
     * 
     * @return The creation date
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }

    /**
     * Gets the last update date of the employee record.
     * 
     * @return The last update date
     */
    public LocalDate getUpdateDate() {
        return updateDate;
    }

    /**
     * Sets the update date of the employee record.
     * This should be called whenever the employee record is modified.
     * 
     * @param updateDate The new update date
     * @throws InvalidInputException if updateDate is null
     */
    public void setUpdateDate(LocalDate updateDate) {
        if (updateDate == null) {
            throw new InvalidInputException("Update date cannot be null");
        }
        this.updateDate = updateDate;
    }
}
