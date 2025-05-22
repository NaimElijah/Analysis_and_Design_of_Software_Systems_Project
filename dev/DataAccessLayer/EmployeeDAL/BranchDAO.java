package DataAccessLayer.EmployeeDAL;

import DomainLayer.EmployeeSubModule.Branch;
import DTOs.BranchDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Branch entities.
 * This class provides methods to interact with the Branch table in the database.
 */
public class BranchDAO {
    private Connection connection;

    public BranchDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserts a new branch into the database.
     *
     * @param branch The branch to insert
     * @return true if the insertion was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean insert(Branch branch) throws SQLException {
        String sql = "INSERT INTO Branches (branchId, branchName, areaCode, branchAddress, managerID) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, branch.getBranchId());
            pstmt.setString(2, branch.getBranchName());
            pstmt.setInt(3, branch.getAreaCode());
            pstmt.setString(4, branch.getBranchAddress());
            pstmt.setString(5, branch.getManagerID());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Updates an existing branch in the database.
     *
     * @param branch The branch to update
     * @return true if the update was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean update(Branch branch) throws SQLException {
        String sql = "UPDATE Branches SET branchName = ?, areaCode = ?, branchAddress = ?, managerID = ? " +
                "WHERE branchId = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, branch.getBranchName());
            pstmt.setInt(2, branch.getAreaCode());
            pstmt.setString(3, branch.getBranchAddress());
            pstmt.setString(4, branch.getManagerID());
            pstmt.setLong(5, branch.getBranchId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Deletes a branch from the database.
     *
     * @param branchId The ID of the branch to delete
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean delete(long branchId) throws SQLException {
        String sql = "DELETE FROM Branches WHERE branchId = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, branchId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Retrieves a branch from the database by ID.
     *
     * @param branchId The ID of the branch to retrieve
     * @return The branch if found, null otherwise
     * @throws SQLException if a database access error occurs
     */
    public Branch getById(long branchId) throws SQLException {
        String sql = "SELECT * FROM Branches WHERE branchId = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, branchId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBranch(rs);
                }
                return null;
            }
        }
    }

    /**
     * Retrieves all branches from the database.
     *
     * @return A list of all branches
     * @throws SQLException if a database access error occurs
     */
    public List<Branch> getAll() throws SQLException {
        String sql = "SELECT * FROM Branches";
        List<Branch> branches = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                branches.add(mapResultSetToBranch(rs));
            }

            return branches;
        }
    }

    /**
     * Retrieves all branches in a specific area.
     *
     * @param areaCode The area code to filter by
     * @return A list of branches in the specified area
     * @throws SQLException if a database access error occurs
     */
    public List<Branch> getByAreaCode(int areaCode) throws SQLException {
        String sql = "SELECT * FROM Branches WHERE areaCode = ?";
        List<Branch> branches = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, areaCode);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    branches.add(mapResultSetToBranch(rs));
                }

                return branches;
            }
        }
    }

    /**
     * Retrieves a branch by manager ID.
     *
     * @param managerID The ID of the manager
     * @return The branch managed by the specified manager, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public Branch getByManager(String managerID) throws SQLException {
        String sql = "SELECT * FROM Branches WHERE managerID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, managerID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBranch(rs);
                }
                return null;
            }
        }
    }

    /**
     * Maps a ResultSet to a Branch object.
     *
     * @param rs The ResultSet containing branch data
     * @return The mapped Branch object
     * @throws SQLException if a database access error occurs
     */
    private Branch mapResultSetToBranch(ResultSet rs) throws SQLException {
        long branchId = rs.getLong("branchId");
        String branchName = rs.getString("branchName");
        int areaCode = rs.getInt("areaCode");
        String branchAddress = rs.getString("branchAddress");
        String managerID = rs.getString("managerID");

        return new Branch(branchId, branchName, areaCode, branchAddress, managerID);
    }
}