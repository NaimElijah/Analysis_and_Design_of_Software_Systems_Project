package DomainLayer.EmployeeSubModule.Repository;

import DataAccessLayer.EmployeeDAL.EmployeeDALFactory;
import DataAccessLayer.EmployeeDAL.EmployeeDAO;
import DomainLayer.EmployeeSubModule.Employee;
import DomainLayer.exception.InvalidInputException;
import DTOs.EmployeeDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the EmployeeRepository interface.
 * This class uses the EmployeeDAO to interact with the database.
 */
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final EmployeeDAO employeeDAO;

    /**
     * Constructor that initializes the repository with the EmployeeDAO.
     */
    public EmployeeRepositoryImpl() {
        try {
            this.employeeDAO = EmployeeDALFactory.getInstance().getEmployeeDAO();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize EmployeeRepository", e);
        }
    }

    /**
     * Constructor that accepts an EmployeeDAO for testing purposes.
     *
     * @param employeeDAO The EmployeeDAO to use
     */
    public EmployeeRepositoryImpl(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    /**
     * Converts an Employee domain object to an EmployeeDTO.
     *
     * @param employee The Employee domain object to convert
     * @return The corresponding EmployeeDTO
     */
    private EmployeeDTO convertToDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        return new EmployeeDTO(
            employee.getIsraeliId(),
            employee.getFirstName(),
            employee.getLastName(),
            employee.getSalary(),
            employee.getTermsOfEmployment(),
            employee.getRoles(),
            employee.getStartOfEmployment(),
            employee.isActive(),
            employee.getCreationDate(),
            employee.getUpdateDate(),
            employee.getBranchId()
        );
    }

    /**
     * Converts an EmployeeDTO to an Employee domain object.
     *
     * @param dto The EmployeeDTO to convert
     * @return The corresponding Employee domain object
     */
    private Employee convertToEntity(EmployeeDTO dto) {
        if (dto == null) {
            return null;
        }

        return new Employee(
            dto.getIsraeliId(),
            dto.getFirstName(),
            dto.getLastName(),
            dto.getSalary(),
            dto.getTermsOfEmployment(),
            dto.getRoles(),
            dto.getStartOfEmployment(),
            dto.isActive(),
            dto.getCreationDate(),
            dto.getUpdateDate(),
            dto.getBranchId()
        );
    }

    @Override
    public EmployeeDTO getById(long israeliId) {
        try {
            Employee employee = employeeDAO.getById(israeliId);
            return convertToDTO(employee);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get employee by ID: " + israeliId, e);
        }
    }

    @Override
    public List<EmployeeDTO> getAll() {
        try {
            List<Employee> employees = employeeDAO.getAll();
            return employees.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all employees", e);
        }
    }

    @Override
    public List<EmployeeDTO> getByBranch(long branchId) {
        try {
            List<Employee> employees = employeeDAO.getByBranch(branchId);
            return employees.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get employees by branch: " + branchId, e);
        }
    }

    @Override
    public boolean create(EmployeeDTO employeeDTO) {
        try {
            Employee employee = convertToEntity(employeeDTO);
            return employeeDAO.insert(employee);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create employee: " + employeeDTO.getIsraeliId(), e);
        }
    }

    @Override
    public boolean update(EmployeeDTO employeeDTO) {
        try {
            Employee employee = convertToEntity(employeeDTO);
            return employeeDAO.update(employee);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update employee: " + employeeDTO.getIsraeliId(), e);
        }
    }

    @Override
    public boolean delete(long israeliId) {
        try {
            return employeeDAO.delete(israeliId);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete employee: " + israeliId, e);
        }
    }

    @Override
    public boolean exists(long israeliId) {
        return getById(israeliId) != null;
    }

    @Override
    public boolean isActive(long israeliId) {
        EmployeeDTO employeeDTO = getById(israeliId);
        return employeeDTO != null && employeeDTO.isActive();
    }

    @Override
    public boolean hasRole(long israeliId, String role) {
        EmployeeDTO employeeDTO = getById(israeliId);
        return employeeDTO != null && employeeDTO.getRoles().contains(role);
    }

    @Override
    public List<EmployeeDTO> getByRole(String role) {
        try {
            List<Employee> employees = employeeDAO.getAll();
            return employees.stream()
                    .filter(e -> e.getRoles().contains(role))
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get employees by role: " + role, e);
        }
    }
}
