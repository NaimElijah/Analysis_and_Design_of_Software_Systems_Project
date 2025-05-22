package DataAccessLayer.EmployeeDAL;

import DomainLayer.EmployeeSubModule.Employee;
import DTOs.EmployeeDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Data Access Object for Employee entities.
 * This class provides methods to interact with the Employee table in the database.
 */
public class EmployeeDAO {
    private Connection connection;

    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserts a new employee into the database.
     *
     * @param employee The employee to insert
     * @return true if the insertion was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean insert(Employee employee) throws SQLException {
        String sql = "INSERT INTO Employees (israeliId, firstName, lastName, salary, startOfEmployment, isActive, creationDate, updateDate, branchId) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, employee.getIsraeliId());
            pstmt.setString(2, employee.getFirstName());
            pstmt.setString(3, employee.getLastName());
            pstmt.setLong(4, employee.getSalary());
            pstmt.setString(5, employee.getStartOfEmployment().toString());
            pstmt.setBoolean(6, employee.isActive());
            pstmt.setString(7, employee.getCreationDate().toString());
            pstmt.setString(8, employee.getUpdateDate().toString());
            
            if (employee.getBranchId() != null) {
                pstmt.setLong(9, employee.getBranchId());
            } else {
                pstmt.setNull(9, java.sql.Types.BIGINT);
            }

            int affectedRows = pstmt.executeUpdate();
            
            // Insert employee roles
            if (affectedRows > 0) {
                insertEmployeeRoles(employee.getIsraeliId(), employee.getRoles());
                
                // Insert terms of employment as JSON
                insertTermsOfEmployment(employee.getIsraeliId(), employee.getTermsOfEmployment());
                
                return true;
            }
            return false;
        }
    }

    /**
     * Updates an existing employee in the database.
     *
     * @param employee The employee to update
     * @return true if the update was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean update(Employee employee) throws SQLException {
        String sql = "UPDATE Employees SET firstName = ?, lastName = ?, salary = ?, " +
                "startOfEmployment = ?, isActive = ?, updateDate = ?, branchId = ? " +
                "WHERE israeliId = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, employee.getFirstName());
            pstmt.setString(2, employee.getLastName());
            pstmt.setLong(3, employee.getSalary());
            pstmt.setString(4, employee.getStartOfEmployment().toString());
            pstmt.setBoolean(5, employee.isActive());
            pstmt.setString(6, employee.getUpdateDate().toString());
            
            if (employee.getBranchId() != null) {
                pstmt.setLong(7, employee.getBranchId());
            } else {
                pstmt.setNull(7, java.sql.Types.BIGINT);
            }
            
            pstmt.setLong(8, employee.getIsraeliId());

            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Update roles - delete existing and insert new ones
                deleteEmployeeRoles(employee.getIsraeliId());
                insertEmployeeRoles(employee.getIsraeliId(), employee.getRoles());
                
                // Update terms of employment
                updateTermsOfEmployment(employee.getIsraeliId(), employee.getTermsOfEmployment());
                
                return true;
            }
            return false;
        }
    }

    /**
     * Deletes an employee from the database.
     *
     * @param israeliId The ID of the employee to delete
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean delete(long israeliId) throws SQLException {
        // First delete related data
        deleteEmployeeRoles(israeliId);
        deleteTermsOfEmployment(israeliId);
        
        // Then delete the employee
        String sql = "DELETE FROM Employees WHERE israeliId = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, israeliId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Retrieves an employee from the database by ID.
     *
     * @param israeliId The ID of the employee to retrieve
     * @return The employee if found, null otherwise
     * @throws SQLException if a database access error occurs
     */
    public Employee getById(long israeliId) throws SQLException {
        String sql = "SELECT * FROM Employees WHERE israeliId = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, israeliId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmployee(rs);
                }
                return null;
            }
        }
    }

    /**
     * Retrieves all employees from the database.
     *
     * @return A list of all employees
     * @throws SQLException if a database access error occurs
     */
    public List<Employee> getAll() throws SQLException {
        String sql = "SELECT * FROM Employees";
        List<Employee> employees = new ArrayList<>();
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
            
            return employees;
        }
    }

    /**
     * Retrieves all employees assigned to a specific branch.
     *
     * @param branchId The ID of the branch
     * @return A list of employees assigned to the branch
     * @throws SQLException if a database access error occurs
     */
    public List<Employee> getByBranch(long branchId) throws SQLException {
        String sql = "SELECT * FROM Employees WHERE branchId = ?";
        List<Employee> employees = new ArrayList<>();
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, branchId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
                
                return employees;
            }
        }
    }

    /**
     * Maps a ResultSet to an Employee object.
     *
     * @param rs The ResultSet containing employee data
     * @return The mapped Employee object
     * @throws SQLException if a database access error occurs
     */
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        long israeliId = rs.getLong("israeliId");
        String firstName = rs.getString("firstName");
        String lastName = rs.getString("lastName");
        long salary = rs.getLong("salary");
        LocalDate startOfEmployment = LocalDate.parse(rs.getString("startOfEmployment"));
        boolean isActive = rs.getBoolean("isActive");
        LocalDate creationDate = LocalDate.parse(rs.getString("creationDate"));
        LocalDate updateDate = LocalDate.parse(rs.getString("updateDate"));
        Long branchId = rs.getLong("branchId");
        if (rs.wasNull()) {
            branchId = null;
        }
        
        // Get roles for this employee
        Set<String> roles = getEmployeeRoles(israeliId);
        
        // Get terms of employment for this employee
        Map<String, Object> termsOfEmployment = getTermsOfEmployment(israeliId);
        
        return new Employee(israeliId, firstName, lastName, salary, termsOfEmployment, 
                roles, startOfEmployment, isActive, creationDate, updateDate, branchId);
    }

    /**
     * Inserts roles for an employee.
     *
     * @param israeliId The ID of the employee
     * @param roles The roles to insert
     * @throws SQLException if a database access error occurs
     */
    private void insertEmployeeRoles(long israeliId, Set<String> roles) throws SQLException {
        String sql = "INSERT INTO EmployeeRoles (israeliId, role) VALUES (?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (String role : roles) {
                pstmt.setLong(1, israeliId);
                pstmt.setString(2, role);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    /**
     * Deletes all roles for an employee.
     *
     * @param israeliId The ID of the employee
     * @throws SQLException if a database access error occurs
     */
    private void deleteEmployeeRoles(long israeliId) throws SQLException {
        String sql = "DELETE FROM EmployeeRoles WHERE israeliId = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, israeliId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves all roles for an employee.
     *
     * @param israeliId The ID of the employee
     * @return A set of roles for the employee
     * @throws SQLException if a database access error occurs
     */
    private Set<String> getEmployeeRoles(long israeliId) throws SQLException {
        String sql = "SELECT role FROM EmployeeRoles WHERE israeliId = ?";
        Set<String> roles = new HashSet<>();
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, israeliId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    roles.add(rs.getString("role"));
                }
                
                return roles;
            }
        }
    }

    /**
     * Inserts terms of employment for an employee.
     *
     * @param israeliId The ID of the employee
     * @param termsOfEmployment The terms of employment to insert
     * @throws SQLException if a database access error occurs
     */
    private void insertTermsOfEmployment(long israeliId, Map<String, Object> termsOfEmployment) throws SQLException {
        String sql = "INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Map.Entry<String, Object> entry : termsOfEmployment.entrySet()) {
                pstmt.setLong(1, israeliId);
                pstmt.setString(2, entry.getKey());
                pstmt.setString(3, entry.getValue().toString());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    /**
     * Updates terms of employment for an employee.
     *
     * @param israeliId The ID of the employee
     * @param termsOfEmployment The terms of employment to update
     * @throws SQLException if a database access error occurs
     */
    private void updateTermsOfEmployment(long israeliId, Map<String, Object> termsOfEmployment) throws SQLException {
        // Delete existing terms
        deleteTermsOfEmployment(israeliId);
        
        // Insert new terms
        insertTermsOfEmployment(israeliId, termsOfEmployment);
    }

    /**
     * Deletes all terms of employment for an employee.
     *
     * @param israeliId The ID of the employee
     * @throws SQLException if a database access error occurs
     */
    private void deleteTermsOfEmployment(long israeliId) throws SQLException {
        String sql = "DELETE FROM EmployeeTerms WHERE israeliId = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, israeliId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves all terms of employment for an employee.
     *
     * @param israeliId The ID of the employee
     * @return A map of terms of employment for the employee
     * @throws SQLException if a database access error occurs
     */
    private Map<String, Object> getTermsOfEmployment(long israeliId) throws SQLException {
        String sql = "SELECT termKey, termValue FROM EmployeeTerms WHERE israeliId = ?";
        Map<String, Object> termsOfEmployment = new HashMap<>();
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, israeliId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    termsOfEmployment.put(rs.getString("termKey"), rs.getString("termValue"));
                }
                
                return termsOfEmployment;
            }
        }
    }
}