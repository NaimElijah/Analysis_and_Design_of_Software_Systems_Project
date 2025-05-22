package DomainLayer.EmployeeSubModule.Repository;

import DataAccessLayer.EmployeeDAL.BranchDAO;
import DataAccessLayer.EmployeeDAL.EmployeeDALFactory;
import DomainLayer.EmployeeSubModule.Branch;
import DTOs.BranchDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the BranchRepository interface.
 * This class uses the BranchDAO to interact with the database.
 */
public class BranchRepositoryImpl implements BranchRepository {
    
    private final BranchDAO branchDAO;
    
    /**
     * Constructor that initializes the repository with the BranchDAO.
     */
    public BranchRepositoryImpl() {
        try {
            this.branchDAO = EmployeeDALFactory.getInstance().getBranchDAO();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize BranchRepository", e);
        }
    }
    
    /**
     * Constructor that accepts a BranchDAO for testing purposes.
     *
     * @param branchDAO The BranchDAO to use
     */
    public BranchRepositoryImpl(BranchDAO branchDAO) {
        this.branchDAO = branchDAO;
    }
    
    /**
     * Converts a Branch domain object to a BranchDTO.
     *
     * @param branch The Branch domain object to convert
     * @return The corresponding BranchDTO
     */
    private BranchDTO convertToDTO(Branch branch) {
        if (branch == null) {
            return null;
        }
        
        return new BranchDTO(
            branch.getBranchId(),
            branch.getBranchName(),
            branch.getAreaCode(),
            branch.getBranchAddress(),
            branch.getManagerID()
        );
    }
    
    /**
     * Converts a BranchDTO to a Branch domain object.
     *
     * @param dto The BranchDTO to convert
     * @return The corresponding Branch domain object
     */
    private Branch convertToEntity(BranchDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return new Branch(
            dto.getBranchId(),
            dto.getBranchName(),
            dto.getAreaCode(),
            dto.getBranchAddress(),
            dto.getManagerID()
        );
    }
    
    @Override
    public BranchDTO getById(long branchId) {
        try {
            Branch branch = branchDAO.getById(branchId);
            return convertToDTO(branch);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get branch by ID: " + branchId, e);
        }
    }
    
    @Override
    public List<BranchDTO> getAll() {
        try {
            List<Branch> branches = branchDAO.getAll();
            return branches.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all branches", e);
        }
    }
    
    @Override
    public List<BranchDTO> getByAreaCode(int areaCode) {
        try {
            List<Branch> branches = branchDAO.getByAreaCode(areaCode);
            return branches.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get branches by area code: " + areaCode, e);
        }
    }
    
    @Override
    public BranchDTO getByManager(String managerId) {
        try {
            Branch branch = branchDAO.getByManager(managerId);
            return convertToDTO(branch);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get branch by manager: " + managerId, e);
        }
    }
    
    @Override
    public boolean create(BranchDTO branchDTO) {
        try {
            Branch branch = convertToEntity(branchDTO);
            return branchDAO.insert(branch);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create branch: " + branchDTO.getBranchId(), e);
        }
    }
    
    @Override
    public boolean update(BranchDTO branchDTO) {
        try {
            Branch branch = convertToEntity(branchDTO);
            return branchDAO.update(branch);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update branch: " + branchDTO.getBranchId(), e);
        }
    }
    
    @Override
    public boolean delete(long branchId) {
        try {
            return branchDAO.delete(branchId);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete branch: " + branchId, e);
        }
    }
    
    @Override
    public boolean exists(long branchId) {
        return getById(branchId) != null;
    }
    
    @Override
    public BranchDTO getByAddressAndAreaCode(String address, int areaCode) {
        try {
            List<Branch> branches = branchDAO.getAll();
            Branch branch = branches.stream()
                    .filter(b -> b.getBranchAddress().equals(address) && b.getAreaCode() == areaCode)
                    .findFirst()
                    .orElse(null);
            return convertToDTO(branch);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get branch by address and area code: " + address + ", " + areaCode, e);
        }
    }
}