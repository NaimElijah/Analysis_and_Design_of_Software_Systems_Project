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



    private static final String TransportsTable =
            "CREATE TABLE IF NOT EXISTS Transports (" +
                    "tranDocId BIGINT PRIMARY KEY, " +
                    "status TEXT NOT NULL, " +
                    "departure_dt TEXT NOT NULL, " +
                    "transportTruckNumber BIGINT NOT NULL, " +
                    "transportDriverId TEXT NOT NULL, " +
                    "truck_Depart_Weight BOOLEAN NOT NULL, " +
                    "srcSiteArea TEXT NOT NULL, " +
                    "srcSiteString BIGINT NOT NULL, " +
                    "branchId BIGINT, " +
                    "FOREIGN KEY (branchId) REFERENCES Branches(branchId)" +
                    ")";

    private static final String TransportsProblemsTable =
            "CREATE TABLE IF NOT EXISTS TransportsProblems (" +
                    "problemOfTranDocId BIGINT PRIMARY KEY, " +
                    "problem TEXT NOT NULL, " +
                    "FOREIGN KEY (problemOfTranDocId) REFERENCES Transports(tranDocId)" +
                    ")";

    private static final String ItemsDocsTable =
            "CREATE TABLE IF NOT EXISTS ItemsDocs (" +
                    "ItemsDocInTransportID BIGINT PRIMARY KEY, " +
                    "itemsDocNum TEXT NOT NULL, " +
                    "srcSiteArea TEXT NOT NULL, " +
                    "srcSiteString BIGINT NOT NULL, " +
                    "destSiteArea TEXT NOT NULL, " +
                    "destSiteString BIGINT NOT NULL, " +
                    "estimatedArrivalTime TEXT NOT NULL, " +
                    "FOREIGN KEY (ItemsDocInTransportID) REFERENCES Transports(tranDocId)" +
                    ")";

    private static final String ItemsTable =
            "CREATE TABLE IF NOT EXISTS Items (" +
                    "itemInItemsDocId BIGINT PRIMARY KEY, " +
                    "name TEXT NOT NULL, " +
                    "weight BIGINT NOT NULL, " +
                    "condition BOOLEAN NOT NULL, " +
                    "FOREIGN KEY (itemInItemsDocId) REFERENCES ItemsDocs(itemsDocNum)" +
                    ")";

    private static final String TrucksTable =
            "CREATE TABLE IF NOT EXISTS Trucks (" +
                    "truckNum BIGINT PRIMARY KEY, " +
                    "model TEXT NOT NULL, " +
                    "netWeight BIGINT NOT NULL, " +
                    "maxCarryWeight BIGINT NOT NULL, " +
                    "validLicense TEXT NOT NULL, " +
                    "inTransportID BIGINT NOT NULL, " +
                    "isDeleted BOOLEAN NOT NULL, " +
                    "FOREIGN KEY (inTransportID) REFERENCES Transports(tranDocId)" +
                    ")";

    private static final String ShippingAreasTable =
            "CREATE TABLE IF NOT EXISTS ShippingAreas (" +
                    "areaNumber BIGINT PRIMARY KEY, " +
                    "areaName TEXT NOT NULL, " +
                    ")";

    private static final String SitesTable =
            "CREATE TABLE IF NOT EXISTS Sites (" +
                    "areaNum BIGINT PRIMARY KEY, " +
                    "addressStr TEXT PRIMARY KEY, " +
                    "contName TEXT NOT NULL, " +
                    "contNumber BIGINT NOT NULL, " +
                    "FOREIGN KEY (areaNum) REFERENCES ShippingAreas(areaNumber)" +
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

                st.executeUpdate(TransportsTable);
                st.executeUpdate(TransportsProblemsTable);
                st.executeUpdate(ItemsDocsTable);
                st.executeUpdate(ItemsTable);
                st.executeUpdate(TrucksTable);
                st.executeUpdate(ShippingAreasTable);
                st.executeUpdate(SitesTable);
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
