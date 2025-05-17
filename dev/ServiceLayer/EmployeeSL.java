package ServiceLayer;

import DomainLayer.Employee;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EmployeeSL {

    private final Employee employee;

    public EmployeeSL(Employee employee) {
        this.employee = employee;
    }

    public long getId() {
        return employee.getIsraeliId();
    }

    public String getFirstName() {
        return employee.getFirstName();
    }

    public String getLastName() {
        return employee.getLastName();
    }

    public String getFullName() {
        return employee.getFirstName() + " " + employee.getLastName();
    }

    public boolean isActive() {
        return employee.isActive();
    }

    public long getSalary() {
        return employee.getSalary();
    }
    public String getIsraeliId() {
        return String.valueOf(employee.getIsraeliId());
    }
    public String getStartOfEmployment() {
        return employee.getStartOfEmployment().toString();
    }
    public Map<String, String> getEmploymentDetails() {
        Map<String, String> details = new HashMap<>();
        for (Map.Entry<String, Object> entry : employee.getTermsOfEmployment().entrySet()) {
            details.put(entry.getKey(), entry.getValue().toString());
        }
        return details;
    }

    public Map<String, Object> getTermsOfEmployment() {
        Map<String, Object> terms = new HashMap<>();
        for (Map.Entry<String, Object> entry : employee.getTermsOfEmployment().entrySet()) {
            terms.put(entry.getKey(), entry.getValue());
        }
        return terms;
    }

    /**
     * Gets all roles assigned to the employee.
     * 
     * @return A set of role names assigned to the employee
     */
    public Set<String> getRoles() {
        return new HashSet<>(employee.getRoles());
    }

    /**
     * Checks if the employee has a specific role.
     * 
     * @param roleName The name of the role to check
     * @return true if the employee has the role, false otherwise
     */
    public boolean hasRole(String roleName) {
        return employee.getRoles().contains(roleName);
    }
}
