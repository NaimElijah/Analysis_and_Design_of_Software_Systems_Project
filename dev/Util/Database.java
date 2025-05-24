package Util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {
    // SQLite database properties
    private static final String DB_URL = "jdbc:sqlite:superLee.db";
    private static Connection conn;

    // Tables creation
    private static final String EmployeesTable = 
        "CREATE TABLE IF NOT EXISTS Employees (" +
        "israeliId BIGINT PRIMARY KEY, " +
        "firstName TEXT NOT NULL, " +
        "lastName TEXT NOT NULL, " +
        "salary BIGINT NOT NULL, " +
        "startOfEmployment TEXT NOT NULL, " +
        "isActive BOOLEAN NOT NULL, " +
        "creationDate TEXT NOT NULL, " +
        "updateDate TEXT NOT NULL, " +
        "branchId BIGINT, " +
        "FOREIGN KEY (branchId) REFERENCES Branches(branchId)" +
        ")";

    private static final String EmployeeRolesTable = 
        "CREATE TABLE IF NOT EXISTS EmployeeRoles (" +
        "israeliId BIGINT NOT NULL, " +
        "role TEXT NOT NULL, " +
        "PRIMARY KEY (israeliId, role), " +
        "FOREIGN KEY (israeliId) REFERENCES Employees(israeliId), " +
        "FOREIGN KEY (role) REFERENCES Roles(roleName)" +
        ")";

    private static final String EmployeeTermsTable = 
        "CREATE TABLE IF NOT EXISTS EmployeeTerms (" +
        "israeliId BIGINT NOT NULL, " +
        "termKey TEXT NOT NULL, " +
        "termValue TEXT NOT NULL, " +
        "PRIMARY KEY (israeliId, termKey), " +
        "FOREIGN KEY (israeliId) REFERENCES Employees(israeliId)" +
        ")";

    private static final String BranchesTable = 
        "CREATE TABLE IF NOT EXISTS Branches (" +
        "branchId BIGINT PRIMARY KEY, " +
        "branchName TEXT NOT NULL, " +
        "areaCode INTEGER NOT NULL, " +
        "branchAddress TEXT NOT NULL, " +
        "managerID TEXT" +
        ")";

    private static final String RolesTable = 
        "CREATE TABLE IF NOT EXISTS Roles (" +
        "roleName TEXT PRIMARY KEY" +
        ")";

    private static final String PermissionsTable = 
        "CREATE TABLE IF NOT EXISTS Permissions (" +
        "permissionName TEXT PRIMARY KEY" +
        ")";

    private static final String RolePermissionsTable = 
        "CREATE TABLE IF NOT EXISTS RolePermissions (" +
        "roleName TEXT NOT NULL, " +
        "permissionName TEXT NOT NULL, " +
        "PRIMARY KEY (roleName, permissionName), " +
        "FOREIGN KEY (roleName) REFERENCES Roles(roleName), " +
        "FOREIGN KEY (permissionName) REFERENCES Permissions(permissionName)" +
        ")";

    private static final String ShiftType =
            "CREATE TYPE IF NOT EXISTS ShiftTypeEnum as ENUM ('MORNING', 'EVENING')";

    private static final String ShiftsTable =
            "CREATE TABLE IF NOT EXISTS Shifts (" +
                    "id BIGINT PRIMARY KEY, " +
                    "shiftType ShiftTypeEnum NOT NULL, " +
                    "shiftDate TEXT NOT NULL, " +
                    "isAssignedShiftManager BOOLEAN NOT NULL, " +
                    "isOpen BOOLEAN NOT NULL, " +
                    "startHour TEXT NOT NULL, " +
                    "endHour TEXT NOT NULL, " +
                    "creationDate TEXT NOT NULL, " +
                    "updateDate TEXT NOT NULL, " +
                    "branchId BIGINT NOT NULL, " +
                    "FOREIGN KEY (branchId) REFERENCES Branches(branchId)" +
                    ")";

    private static final String RoleRequiredTable =
            "CREATE TABLE IF NOT EXISTS RoleRequired (" +
                    "shiftId BIGINT NOT NULL, " +
                    "roleName TEXT NOT NULL, " +
                    "requiredCount INTEGER NOT NULL, " +
                    "PRIMARY KEY (shiftId, roleName), " +
                    "FOREIGN KEY (id) REFERENCES Shifts(id), " +
                    "FOREIGN KEY (roleName) REFERENCES Roles(roleName)" +
                    ")";

    private static final String AssignedEmployeesTable =
            "CREATE TABLE IF NOT EXISTS AssignedEmployees (" +
                    "shiftId BIGINT NOT NULL, " +
                    "roleName TEXT NOT NULL, " +
                    "employeeId BIGINT NOT NULL, " +
                    "PRIMARY KEY (shiftId, roleName, employeeId), " +
                    "FOREIGN KEY (shiftId) REFERENCES Shifts(id), " +
                    "FOREIGN KEY (roleName) REFERENCES Roles(roleName), " +
                    "FOREIGN KEY (employeeId) REFERENCES Employees(israeliId)" +
                    ")";

    private static final String AvailableEmployeesTable =
            "CREATE TABLE IF NOT EXISTS AvailableEmployees (" +
                    "shiftId BIGINT NOT NULL, " +
                    "employeeId BIGINT NOT NULL, " +
                    "PRIMARY KEY (shiftId, employeeId), " +
                    "FOREIGN KEY (shiftId) REFERENCES Shifts(id), " +
                    "FOREIGN KEY (employeeId) REFERENCES Employees(israeliId)" +
                    ")";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);

            try (Statement st = conn.createStatement()) {
                // Create table's
                // order is IMPORTANT DO NOT CHANGE !!! (foreign key constraints)
                st.executeUpdate(BranchesTable);
                st.executeUpdate(RolesTable);
                st.executeUpdate(PermissionsTable);
                st.executeUpdate(EmployeesTable);
                st.executeUpdate(EmployeeRolesTable);
                st.executeUpdate(EmployeeTermsTable);
                st.executeUpdate(RolePermissionsTable);
                st.executeUpdate(ShiftType);
                st.executeUpdate(ShiftsTable);
                st.executeUpdate(RoleRequiredTable);
                st.executeUpdate(AssignedEmployeesTable);
                st.executeUpdate(AvailableEmployeesTable);
                // ***ADD YOUR TABLES HERE***

            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Database() {}

    public static Connection getConnection() throws SQLException {
        return conn;
    }
}
