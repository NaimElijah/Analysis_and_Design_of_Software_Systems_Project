package DataAccessLayer.EmployeeDAL;

import DTOs.ShiftDTO;
import DomainLayer.EmployeeSubModule.Shift;
import DomainLayer.enums.ShiftType;

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

public class ShiftDAO {
    private Connection connection;

    public ShiftDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(ShiftDTO shift) throws SQLException {
        String sql = "INSERT INTO shifts (id, shiftType, shiftDate, isAssignedShiftManager, isOpen, startHour, endHour, createDate, updateDate, branchId)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, shift.getId());
            pstmt.setString(2, shift.getShiftType().toString());
            pstmt.setDate(3, java.sql.Date.valueOf(shift.getShiftDate()));
            pstmt.setBoolean(4, shift.isAssignedShiftManager());
            pstmt.setBoolean(5, shift.isOpen());
            pstmt.setTime(6, java.sql.Time.valueOf(shift.getStartHour()));
            pstmt.setTime(7, java.sql.Time.valueOf(shift.getEndHour()));
            pstmt.setDate(8, java.sql.Date.valueOf(shift.getCreateDate()));
            pstmt.setDate(9, java.sql.Date.valueOf(shift.getUpdateDate()));
            pstmt.setLong(10, shift.getBranchId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // Insert roles required, assigned employees, and available employees
                insertRolesRequired(shift.getId(), shift.getRolesRequired());
                insertAssignedEmployees(shift.getId(), shift.getAssignedEmployees());
                insertAvailableEmployees(shift.getId(), shift.getAvailableEmployees());
                return true;
            }

            return false;
        }

    }

    public boolean insertRolesRequired(long shiftId, Map<String, Integer> rolesRequired) throws SQLException {
        String sql = "INSERT INTO roles_required (shiftId, roleName, requiredCount) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Map.Entry<String, Integer> entry : rolesRequired.entrySet()) {
                pstmt.setLong(1, shiftId);
                pstmt.setString(2, entry.getKey());
                pstmt.setInt(3, entry.getValue());
                pstmt.addBatch();
            }
            int[] rowsAffected = pstmt.executeBatch();
            return rowsAffected.length > 0;
        }
    }

    public boolean insertAssignedEmployees(long shiftId, Map<String, Set<Long>> assignedEmployees) throws SQLException {
        String sql = "INSERT INTO assigned_employees (shiftId, roleName, employeeId)" +
                " VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Map.Entry<String, Set<Long>> entry : assignedEmployees.entrySet()) {
                String roleName = entry.getKey();
                for (Long employeeId : entry.getValue()) {
                    pstmt.setLong(1, shiftId);
                    pstmt.setString(2, roleName);
                    pstmt.setLong(3, employeeId);
                    pstmt.addBatch();
                }
            }
            int[] rowsAffected = pstmt.executeBatch();
            return rowsAffected.length > 0;
        }
    }

    public boolean insertAvailableEmployees(long shiftId, Set<Long> availableEmployees) throws SQLException {
        String sql = "INSERT INTO available_employees (shiftId, employeeId) " +
                "VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Long employeeId : availableEmployees) {
                pstmt.setLong(1, shiftId);
                pstmt.setLong(2, employeeId);
                pstmt.addBatch();
            }
            int[] rowsAffected = pstmt.executeBatch();
            return rowsAffected.length > 0;
        }
    }

    public boolean update(ShiftDTO shift) throws SQLException {
        String sql = "UPDATE shifts SET shiftType = ?, shiftDate = ?, isAssignedShiftManager = ?, isOpen = ?, startHour = ?, endHour = ?, updateDate = ? " +
                "WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, shift.getShiftType().toString());
            pstmt.setDate(2, java.sql.Date.valueOf(shift.getShiftDate()));
            pstmt.setBoolean(3, shift.isAssignedShiftManager());
            pstmt.setBoolean(4, shift.isOpen());
            pstmt.setTime(5, java.sql.Time.valueOf(shift.getStartHour()));
            pstmt.setTime(6, java.sql.Time.valueOf(shift.getEndHour()));
            pstmt.setDate(7, java.sql.Date.valueOf(shift.getUpdateDate()));
            pstmt.setLong(8, shift.getId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // Update roles required, assigned employees, and available employees
                updateRolesRequired(shift.getId(), shift.getRolesRequired());
                updateAssignedEmployees(shift.getId(), shift.getAssignedEmployees());
                updateAvailableEmployees(shift.getId(), shift.getAvailableEmployees());
                return true;
            }

            return false;
        }
    }

    public boolean updateRolesRequired(long shiftId, Map<String, Integer> rolesRequired) throws SQLException {
        String sql = "UPDATE roles_required SET requiredCount = ? WHERE shiftId = ? AND roleName = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Map.Entry<String, Integer> entry : rolesRequired.entrySet()) {
                pstmt.setInt(1, entry.getValue());
                pstmt.setLong(2, shiftId);
                pstmt.setString(3, entry.getKey());
                pstmt.addBatch();
            }
            int[] rowsAffected = pstmt.executeBatch();
            return rowsAffected.length > 0;
        }
    }

    public boolean updateAssignedEmployees(long shiftId, Map<String, Set<Long>> assignedEmployees) throws SQLException {
        String sql = "UPDATE assigned_employees SET employeeId = ? WHERE shiftId = ? AND roleName = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Map.Entry<String, Set<Long>> entry : assignedEmployees.entrySet()) {
                String roleName = entry.getKey();
                for (Long employeeId : entry.getValue()) {
                    pstmt.setLong(1, employeeId);
                    pstmt.setLong(2, shiftId);
                    pstmt.setString(3, roleName);
                    pstmt.addBatch();
                }
            }
            int[] rowsAffected = pstmt.executeBatch();
            return rowsAffected.length > 0;
        }
    }

    public boolean updateAvailableEmployees(long shiftId, Set<Long> availableEmployees) throws SQLException {
        String sql = "UPDATE available_employees SET employeeId = ? WHERE shiftId = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Long employeeId : availableEmployees) {
                pstmt.setLong(1, employeeId);
                pstmt.setLong(2, shiftId);
                pstmt.addBatch();
            }
            int[] rowsAffected = pstmt.executeBatch();
            return rowsAffected.length > 0;
        }
    }

    public boolean delete(long shiftId) throws SQLException {
        deleteRolesRequired(shiftId);
        deleteAssignedEmployees(shiftId);
        deleteAvailableEmployees(shiftId);

        String sql = "DELETE FROM shifts WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, shiftId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteRolesRequired(long shiftId) throws SQLException {
        String sql = "DELETE FROM roles_required WHERE shiftId = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, shiftId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteAssignedEmployees(long shiftId) throws SQLException {
        String sql = "DELETE FROM assigned_employees WHERE shiftId = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, shiftId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteAvailableEmployees(long shiftId) throws SQLException {
        String sql = "DELETE FROM available_employees WHERE shiftId = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, shiftId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public ShiftDTO getById(long shiftId) throws SQLException {
        String sql = "SELECT * FROM shifts WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, shiftId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                ShiftDTO shift = new ShiftDTO();
                shift.setId(rs.getLong("id"));
                shift.setShiftType(ShiftType.valueOf(rs.getString("shiftType")));
                shift.setShiftDate(rs.getDate("shiftDate").toLocalDate());
                shift.setAssignedShiftManager(rs.getBoolean("isAssignedShiftManager"));
                shift.setOpen(rs.getBoolean("isOpen"));
                shift.setStartHour(rs.getTime("startHour").toLocalTime());
                shift.setEndHour(rs.getTime("endHour").toLocalTime());
                shift.setCreateDate(rs.getDate("createDate").toLocalDate());
                shift.setUpdateDate(rs.getDate("updateDate").toLocalDate());
                shift.setBranchId(rs.getLong("branchId"));

                // Fetch roles required, assigned employees, and available employees
                shift.setRolesRequired(getRolesRequired(shiftId));
                shift.setAssignedEmployees(getAssignedEmployees(shiftId));
                shift.setAvailableEmployees(getAvailableEmployees(shiftId));

                return shift;
            }

            return null;
        }
    }

    public Map<String, Integer> getRolesRequired(long shiftId) throws SQLException {
        String sql = "SELECT * FROM roles_required WHERE shiftId = ?";
        Map<String, Integer> rolesRequired = new HashMap<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, shiftId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String roleName = rs.getString("roleName");
                int requiredCount = rs.getInt("requiredCount");
                rolesRequired.put(roleName, requiredCount);
            }
        }

        return rolesRequired;
    }

    public Map<String, Set<Long>> getAssignedEmployees(long shiftId) throws SQLException {
        String sql = "SELECT * FROM assigned_employees WHERE shiftId = ?";
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, shiftId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String roleName = rs.getString("roleName");
                long employeeId = rs.getLong("employeeId");

                assignedEmployees.computeIfAbsent(roleName, k -> new HashSet<>()).add(employeeId);
            }
        }

        return assignedEmployees;
    }

    public Set<Long> getAvailableEmployees(long shiftId) throws SQLException {
        String sql = "SELECT * FROM available_employees WHERE shiftId = ?";
        Set<Long> availableEmployees = new HashSet<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, shiftId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                long employeeId = rs.getLong("employeeId");
                availableEmployees.add(employeeId);
            }
        }
        return availableEmployees;
    }

    public List<ShiftDTO> getAll() throws SQLException {
        String sql = "SELECT * FROM shifts";
        List<ShiftDTO> shifts = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                ShiftDTO shift = new ShiftDTO();
                shift.setId(rs.getLong("id"));
                shift.setShiftType(ShiftType.valueOf(rs.getString("shiftType")));
                shift.setShiftDate(rs.getDate("shiftDate").toLocalDate());
                shift.setAssignedShiftManager(rs.getBoolean("isAssignedShiftManager"));
                shift.setOpen(rs.getBoolean("isOpen"));
                shift.setStartHour(rs.getTime("startHour").toLocalTime());
                shift.setEndHour(rs.getTime("endHour").toLocalTime());
                shift.setCreateDate(rs.getDate("createDate").toLocalDate());
                shift.setUpdateDate(rs.getDate("updateDate").toLocalDate());
                shift.setBranchId(rs.getLong("branchId"));

                // Fetch roles required, assigned employees, and available employees
                shift.setRolesRequired(getRolesRequired(shift.getId()));
                shift.setAssignedEmployees(getAssignedEmployees(shift.getId()));
                shift.setAvailableEmployees(getAvailableEmployees(shift.getId()));

                shifts.add(shift);
            }
        }

        return shifts;
    }

    public List<ShiftDTO> getAllByBranchId(long branchId) throws SQLException {
        String sql = "SELECT * FROM shifts WHERE branchId = ?";
        List<ShiftDTO> shifts = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, branchId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ShiftDTO shift = new ShiftDTO();
                shift.setId(rs.getLong("id"));
                shift.setShiftType(ShiftType.valueOf(rs.getString("shiftType")));
                shift.setShiftDate(rs.getDate("shiftDate").toLocalDate());
                shift.setAssignedShiftManager(rs.getBoolean("isAssignedShiftManager"));
                shift.setOpen(rs.getBoolean("isOpen"));
                shift.setStartHour(rs.getTime("startHour").toLocalTime());
                shift.setEndHour(rs.getTime("endHour").toLocalTime());
                shift.setCreateDate(rs.getDate("createDate").toLocalDate());
                shift.setUpdateDate(rs.getDate("updateDate").toLocalDate());
                shift.setBranchId(rs.getLong("branchId"));

                // Fetch roles required, assigned employees, and available employees
                shift.setRolesRequired(getRolesRequired(shift.getId()));
                shift.setAssignedEmployees(getAssignedEmployees(shift.getId()));
                shift.setAvailableEmployees(getAvailableEmployees(shift.getId()));

                shifts.add(shift);
            }
        }

        return shifts;
    }






}
