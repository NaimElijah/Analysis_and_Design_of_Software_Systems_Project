package ServiceLayer.TransportSubModule;

import Util.TestDatabase;
import DTOs.BranchDTO;
import DTOs.EmployeeDTO;
import DTOs.ShiftDTO;
import DTOs.TransportModuleDTOs.*;
import DataAccessLayer.EmployeeDAL.AuthorisationDAO;
import DataAccessLayer.EmployeeDAL.BranchDAO;
import DataAccessLayer.EmployeeDAL.EmployeeDAO;
import DataAccessLayer.EmployeeDAL.ShiftDAO;
import DomainLayer.EmployeeSubModule.*;
import DomainLayer.EmployeeSubModule.Repository.AuthorisationRepositoryImpl;
import DomainLayer.EmployeeSubModule.Repository.BranchRepositoryImpl;
import DomainLayer.EmployeeSubModule.Repository.EmployeeRepositoryImpl;
import DomainLayer.EmployeeSubModule.Repository.ShiftRepositoryImpl;
import DomainLayer.EmployeeSubModule.Repository.interfaces.AuthorisationRepository;
import DomainLayer.EmployeeSubModule.Repository.interfaces.BranchRepository;
import DomainLayer.EmployeeSubModule.Repository.interfaces.EmployeeRepository;
import DomainLayer.EmployeeSubModule.Repository.interfaces.ShiftReposetory;
import DomainLayer.TransportDomain.SiteSubModule.SiteFacade;
import DomainLayer.TransportDomain.TransportSubModule.TransportController;
import DomainLayer.TransportDomain.TruckSubModule.TruckFacade;
import DomainLayer.enums.ShiftType;
import DomainLayer.enums.enumTranProblem;
import DomainLayer.enums.enumTranStatus;
import ServiceLayer.EmployeeIntegrationService;
import ServiceLayer.EmployeeSubModule.EmployeeService;
import ServiceLayer.EmployeeSubModule.ShiftService;
import ServiceLayer.TransportServices.SiteService;
import ServiceLayer.TransportServices.TransportService;
import ServiceLayer.TransportServices.TruckService;
import Util.Database;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TransportServiceTest {
    private static Connection connection;
    private static EmployeeDAO employeeDAO;
    private static BranchDAO branchDAO;
    private static AuthorisationDAO authorisationDAO;
    private static ShiftDAO shiftDAO;
    private static EmployeeRepository employeeRepository;
    private static BranchRepository branchRepository;
    private static AuthorisationRepository authorisationRepository;
    private static ShiftReposetory shiftRepository;
    private static AuthorisationController authorisationController;
    private static EmployeeController employeeController;
    private static ShiftController shiftController;
    private static AssignmentController assignmentController;
    private static AvailabilityController availabilityController;
    private static EmployeeService employeeService;
    private static ShiftService shiftService;
    private static Employee testEmployee;
    private static Employee driverEmployee;
    private static Branch testBranch;
    private static long shiftId;

    private static EmployeeIntegrationService employeeIntegrationService;
    private static TransportController tran_f;
    private static ObjectMapper objectMapper;
    private static SiteFacade siteFacade;
    private static TruckFacade truckFacade;
    private static TransportService transportService;


    @BeforeAll
    public static void setUpClass() throws SQLException {
        // Set up database connection using the Database utility class
        connection = TestDatabase.getConnection();

//        // Set autoCommit to false for tests
//        connection.setAutoCommit(false);

        ////    setting up HR related

        // Initialize DAOs
        employeeDAO = new EmployeeDAO(connection);
        branchDAO = new BranchDAO(connection);
        authorisationDAO = new AuthorisationDAO(connection);
        shiftDAO = new ShiftDAO(connection);

        // Initialize repositories
        employeeRepository = new EmployeeRepositoryImpl(employeeDAO);
        branchRepository = new BranchRepositoryImpl(branchDAO);
        authorisationRepository = new AuthorisationRepositoryImpl(authorisationDAO);
        shiftRepository = new ShiftRepositoryImpl();

        // Initialize controllers
        authorisationController = new AuthorisationController(authorisationRepository);
        employeeController = new EmployeeController(employeeRepository, branchRepository, authorisationRepository, authorisationController);
        shiftController = new ShiftController(employeeController, shiftRepository);
        assignmentController = new AssignmentController(employeeController, shiftController, shiftRepository);
        availabilityController = new AvailabilityController(employeeController, shiftController, shiftRepository);

        // Initialize services
        employeeService = new EmployeeService(employeeController, authorisationController);
        shiftService = new ShiftService(shiftController, assignmentController, availabilityController);









        // Initialize the service under test
        employeeIntegrationService = new EmployeeIntegrationService(employeeService, shiftService);

        ////    setting up Transports related

        objectMapper = new ObjectMapper();
        // Set up the ObjectMapper with JavaTimeModule to handle LocalDate and other Java 8 date types.
        objectMapper.registerModule(new JavaTimeModule());
        // Optional: Configure SerializationFeature to avoid exceptions when serializing dates to JSON
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);   //  if needed for LocalDateTime serialization

        siteFacade = new SiteFacade(connection);
        truckFacade = new TruckFacade(connection);
        tran_f = new TransportController(siteFacade, truckFacade, objectMapper, connection);

        // Initialize services
//        TruckService truckService = new TruckService(truckFacade, employeeIntegrationService);
//        SiteService siteService = new SiteService(siteFacade, employeeIntegrationService);
        transportService = new TransportService(tran_f, employeeIntegrationService, objectMapper);



    }

    @AfterAll
    public static void tearDownClass() throws SQLException {
        // Clean up database
        cleanupDatabase();

        // Close connection
        if (connection != null) {
            connection.close();
        }
    }

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException {
        // Clear tables and insert test data
        cleanupDatabase();
        insertTestData();

        truckFacade.loadDBData();         ///    added by me -db is empty right now though
        siteFacade.loadDBData();          ///    added by me -db is empty right now though
        transportService.loadDBData();    ///    added by me -db is empty right now though

        // Create a test branch
        testBranch = new Branch(14L, "Test Branch", 123, "Test Address", null);
        BranchDTO branchDTO = new BranchDTO(
                testBranch.getBranchId(),
                testBranch.getBranchName(),
                testBranch.getAreaCode(),
                testBranch.getBranchAddress(),
                testBranch.getManagerID()
        );
        branchDAO.insert(branchDTO);

        siteFacade.addShippingArea(123, "Test Area");  //  adding branch to sites
        siteFacade.addSiteTOArea(123, "Test Address", "Mike dude", 0512361242);  //  adding branch to sites

        // Create a test employee
        Set<String> roles = new HashSet<>();
        roles.add("WarehouseMan");
        Map<String, Object> termsOfEmployment = new HashMap<>();
        termsOfEmployment.put("contractType", "FullTime");

        testEmployee = new Employee(
                112112112, // israeliId
                "Test", // firstName
                "Employee", // lastName
                5000, // salary
                termsOfEmployment, // termsOfEmployment
                roles, // roles
                LocalDate.now(), // startOfEmployment
                true, // isActive
                LocalDate.now(), // creationDate
                LocalDate.now(), // updateDate
                testBranch.getBranchId() // branchId
        );

        // Insert test employee into database
        EmployeeDTO employeeDTO = new EmployeeDTO(
                testEmployee.getIsraeliId(),
                testEmployee.getFirstName(),
                testEmployee.getLastName(),
                testEmployee.getSalary(),
                testEmployee.getTermsOfEmployment(),
                testEmployee.getRoles(),
                testEmployee.getStartOfEmployment(),
                testEmployee.isActive(),
                testEmployee.getCreationDate(),
                testEmployee.getUpdateDate(),
                testEmployee.getBranchId()
        );
        employeeDAO.insert(employeeDTO);

        // Create a driver employee
        Set<String> driverRoles = new HashSet<>();
        driverRoles.add("DriverE");
        Map<String, Object> driverTerms = new HashMap<>();
        driverTerms.put("contractType", "FullTime");

        driverEmployee = new Employee(
                987654321, // israeliId
                "Driver", // firstName
                "Test", // lastName
                6000, // salary
                driverTerms, // termsOfEmployment
                driverRoles, // roles
                LocalDate.now(), // startOfEmployment
                true, // isActive
                LocalDate.now(), // creationDate
                LocalDate.now(), // updateDate
                testBranch.getBranchId() // branchId
        );

        // Insert driver employee into database
        EmployeeDTO driverDTO = new EmployeeDTO(
                driverEmployee.getIsraeliId(),
                driverEmployee.getFirstName(),
                driverEmployee.getLastName(),
                driverEmployee.getSalary(),
                driverEmployee.getTermsOfEmployment(),
                driverEmployee.getRoles(),
                driverEmployee.getStartOfEmployment(),
                driverEmployee.isActive(),
                driverEmployee.getCreationDate(),
                driverEmployee.getUpdateDate(),
                driverEmployee.getBranchId()
        );
        employeeDAO.insert(driverDTO);

        // Create a test shift
        LocalDate shiftDate = LocalDate.now();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 0);

        // Create roles required for the shift
        Map<String, Integer> rolesRequired = new HashMap<>();
        rolesRequired.put("DriverE", 1);
        rolesRequired.put("WarehouseMan", 1);

        // Create assigned employees for the shift
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> driverSet = new HashSet<>();
        driverSet.add(driverEmployee.getIsraeliId());
        assignedEmployees.put("DriverE", driverSet);

        Set<Long> WarehouseManSet = new HashSet<>();
        WarehouseManSet.add(testEmployee.getIsraeliId());
        assignedEmployees.put("WarehouseMan", WarehouseManSet);

        // Create available employees for the shift
        Set<Long> availableEmployees = new HashSet<>();
        availableEmployees.add(testEmployee.getIsraeliId());
        availableEmployees.add(driverEmployee.getIsraeliId());

        // Create the shift
        shiftId = 14L;
        ShiftDTO shiftDTO = new ShiftDTO(
                shiftId,
                ShiftType.MORNING,
                shiftDate,
                rolesRequired,
                assignedEmployees,
                availableEmployees,
                true, // isAssignedShiftManager
                true, // isOpen
                startTime,
                endTime,
                LocalDate.now(), // creationDate
                LocalDate.now(), // updateDate
                testBranch.getBranchId() // branchId
        );

        // Use the repository to create the shift instead of directly using the DAO
        // This ensures the shift is added to the in-memory collection
        shiftRepository.create(shiftDTO);
    }



    @AfterEach
    public void tearDown() throws SQLException {
        // Rollback transaction to clean up after each test
        connection.rollback();
    }


    /**
     * Cleans up all tables after all tests are done.
     * This ensures the database is left in a clean state.
     */
    public static void cleanupDatabase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Disable foreign key constraints temporarily
            stmt.execute("PRAGMA foreign_keys = OFF");

            // Delete all data from tables
            stmt.execute("DELETE FROM EmployeeTerms");
            stmt.execute("DELETE FROM EmployeeRoles");
            stmt.execute("DELETE FROM Employees");
            stmt.execute("DELETE FROM Branches");
            stmt.execute("DELETE FROM RolePermissions");
            stmt.execute("DELETE FROM Roles");
            stmt.execute("DELETE FROM Permissions");
            stmt.execute("DELETE FROM AssignedEmployees");
            stmt.execute("DELETE FROM AvailableEmployees");
            stmt.execute("DELETE FROM RoleRequired");
            stmt.execute("DELETE FROM Shifts");

            stmt.execute("DELETE FROM ItemsQ");
            stmt.execute("DELETE FROM ItemsDocs");
            stmt.execute("DELETE FROM TransportsProblems");
            stmt.execute("DELETE FROM DriverIdToInTransportID");
            stmt.execute("DELETE FROM Counters");
            stmt.execute("DELETE FROM Transports");
            stmt.execute("DELETE FROM Trucks");
            stmt.execute("DELETE FROM Sites");
            stmt.execute("DELETE FROM ShippingAreas");

            // Re-enable foreign key constraints
            stmt.execute("PRAGMA foreign_keys = ON");
        }
    }

    private void insertTestData() throws SQLException {
        // Insert test roles and permissions
        try (Statement stmt = connection.createStatement()) {

            stmt.execute("INSERT INTO Branches (branchId, branchName, areaCode, branchAddress, managerID) VALUES (1, 'Headquarters', 0, 'Tel Aviv', 111111111)");
            stmt.execute("INSERT INTO Branches (branchId, branchName, areaCode, branchAddress, managerID) VALUES (2, 'Beer Shave', 1, 'Ben Gurion Uni', 444444444)");

            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('CREATE_EMPLOYEE')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('UPDATE_EMPLOYEE')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('EDIT_EMPLOYEE')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('VIEW_EMPLOYEE')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('DEACTIVATE_EMPLOYEE')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('DELETE_EMPLOYEE')");

            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('CREATE_ROLE')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('EDIT_ROLE')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('ROLE_PERMISSION')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('ADD_PERMISSION_TO_ROLE')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('REMOVE_PERMISSION_FROM_ROLE')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('CREATE_PERMISSION')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('EDIT_PERMISSION')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('GET_ROLES')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('ROLE_REQUIRED')");

            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('CREATE_SHIFT')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('UPDATE_SHIFT')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('EDIT_SHIFT')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('REMOVE_SHIFT')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('GET_SHIFT')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('MANAGE_SHIFT')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('VIEW_SHIFT')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('ASSIGN_EMPLOYEE')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('UPDATE_AVAILABLE')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('CREATE_TRANSPORT')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('EDIT_TRANSPORT')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('DELETE_TRANSPORT')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('VIEW_TRANSPORT')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('VIEW_RELEVANT_TRANSPORTS')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('ADD_ITEM_TO_TRANSPORT')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('EDIT_ITEM_IN_TRANSPORT')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('DELETE_ITEM_FROM_TRANSPORT')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('EDIT_TRANSPORT_ITEM_CONDITION')");

            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('ADD_SITE')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('EDIT_SITE')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('DELETE_SITE')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('SHOW_SITES')");

            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('ADD_SHIPPING_AREA')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('EDIT_SHIPPING_AREA')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('DELETE_SHIPPING_AREA')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('SHOW_SHIPPING_AREAS')");

            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('ADD_TRUCK')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('DELETE_TRUCK')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('SHOW_TRUCKS')");

            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('MANAGE_HR')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('MANAGE_INVENTORY')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('HANDLE_CASH')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('DRIVE_VEHICLE')");
            stmt.execute("INSERT INTO Permissions (permissionName) VALUES ('STOCK_SHELVES')");

            stmt.execute("INSERT INTO Roles (roleName) VALUES ('Admin')");
            stmt.execute("INSERT INTO Roles (roleName) VALUES ('HR manager')");
            stmt.execute("INSERT INTO Roles (roleName) VALUES ('Transport Manager')");
            stmt.execute("INSERT INTO Roles (roleName) VALUES ('Shift Manager')");
            stmt.execute("INSERT INTO Roles (roleName) VALUES ('Cashier')");
            stmt.execute("INSERT INTO Roles (roleName) VALUES ('Stocker')");
            stmt.execute("INSERT INTO Roles (roleName) VALUES ('Cleaner')");

            stmt.execute("INSERT INTO Roles (roleName) VALUES ('DriverA')");
            stmt.execute("INSERT INTO Roles (roleName) VALUES ('DriverB')");
            stmt.execute("INSERT INTO Roles (roleName) VALUES ('DriverC')");
            stmt.execute("INSERT INTO Roles (roleName) VALUES ('DriverD')");
            stmt.execute("INSERT INTO Roles (roleName) VALUES ('DriverE')");
            stmt.execute("INSERT INTO Roles (roleName) VALUES ('WarehouseMan')");

            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'UPDATE_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'DELETE_SITE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'EDIT_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'EDIT_ITEM_IN_TRANSPORT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'ADD_ITEM_TO_TRANSPORT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'EDIT_EMPLOYEE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'GET_ROLES')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'EDIT_SHIPPING_AREA')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'CREATE_EMPLOYEE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'SHOW_TRUCKS')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'UPDATE_AVAILABLE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'VIEW_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'MANAGE_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'SHOW_SITES')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'EDIT_TRANSPORT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'CREATE_TRANSPORT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'ADD_PERMISSION_TO_ROLE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'MANAGE_HR')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'DELETE_SHIPPING_AREA')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'CREATE_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'CREATE_ROLE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'ROLE_REQUIRED')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'VIEW_EMPLOYEE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'MANAGE_INVENTORY')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'ADD_SHIPPING_AREA')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'EDIT_ROLE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'ASSIGN_EMPLOYEE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'DELETE_TRUCK')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'EDIT_SITE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'UPDATE_EMPLOYEE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'CREATE_PERMISSION')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'SHOW_SHIPPING_AREAS')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'EDIT_PERMISSION')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'STOCK_SHELVES')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'ROLE_PERMISSION')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'ADD_SITE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'DEACTIVATE_EMPLOYEE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'REMOVE_PERMISSION_FROM_ROLE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'GET_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'DELETE_TRANSPORT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'DELETE_EMPLOYEE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'REMOVE_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'HANDLE_CASH')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'VIEW_TRANSPORT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'DELETE_ITEM_FROM_TRANSPORT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'ADD_TRUCK')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'DRIVE_VEHICLE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Admin', 'EDIT_TRANSPORT_ITEM_CONDITION')");

            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('HR manager', 'VIEW_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('HR manager', 'UPDATE_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('HR manager', 'CREATE_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('HR manager', 'ROLE_REQUIRED')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('HR manager', 'VIEW_EMPLOYEE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('HR manager', 'DEACTIVATE_EMPLOYEE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('HR manager', 'GET_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('HR manager', 'ASSIGN_EMPLOYEE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('HR manager', 'EDIT_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('HR manager', 'REMOVE_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('HR manager', 'EDIT_EMPLOYEE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('HR manager', 'UPDATE_EMPLOYEE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('HR manager', 'GET_ROLES')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('HR manager', 'CREATE_EMPLOYEE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('HR manager', 'UPDATE_AVAILABLE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('HR manager', 'MANAGE_HR')");

            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'SHOW_SITES')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'ADD_SHIPPING_AREA')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'VIEW_TRANSPORT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'EDIT_TRANSPORT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'DELETE_TRANSPORT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'DELETE_SITE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'DELETE_ITEM_FROM_TRANSPORT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'EDIT_SITE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'ADD_ITEM_TO_TRANSPORT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'EDIT_ITEM_IN_TRANSPORT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'DELETE_TRUCK')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'EDIT_SHIPPING_AREA')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'SHOW_TRUCKS')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'SHOW_SHIPPING_AREAS')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'ADD_TRUCK')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'CREATE_TRANSPORT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'ADD_SITE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'DELETE_SHIPPING_AREA')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'VIEW_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'UPDATE_AVAILABLE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'GET_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Transport Manager', 'EDIT_TRANSPORT_ITEM_CONDITION')");

            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Shift Manager', 'VIEW_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Shift Manager', 'UPDATE_AVAILABLE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Shift Manager', 'GET_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Shift Manager', 'MANAGE_SHIFT')");

            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Cashier', 'HANDLE_CASH')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Cashier', 'VIEW_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Cashier', 'UPDATE_AVAILABLE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Cashier', 'GET_SHIFT')");

            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Stocker', 'VIEW_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Stocker', 'MANAGE_INVENTORY')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Stocker', 'STOCK_SHELVES')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Stocker', 'GET_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Stocker', 'UPDATE_AVAILABLE')");

            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Cleaner', 'VIEW_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Cleaner', 'UPDATE_AVAILABLE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('Cleaner', 'GET_SHIFT')");

            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverA', 'VIEW_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverA', 'UPDATE_AVAILABLE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverA', 'GET_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverA', 'VIEW_RELEVANT_TRANSPORTS')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverA', 'EDIT_TRANSPORT_ITEM_CONDITION')");

            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverB', 'VIEW_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverB', 'UPDATE_AVAILABLE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverB', 'GET_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverB', 'VIEW_RELEVANT_TRANSPORTS')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverB', 'EDIT_TRANSPORT_ITEM_CONDITION')");

            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverC', 'VIEW_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverC', 'UPDATE_AVAILABLE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverC', 'GET_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverC', 'VIEW_RELEVANT_TRANSPORTS')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverC', 'EDIT_TRANSPORT_ITEM_CONDITION')");

            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverD', 'VIEW_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverD', 'UPDATE_AVAILABLE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverD', 'GET_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverD', 'VIEW_RELEVANT_TRANSPORTS')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverD', 'EDIT_TRANSPORT_ITEM_CONDITION')");

            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverE', 'VIEW_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverE', 'UPDATE_AVAILABLE')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverE', 'GET_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverE', 'VIEW_RELEVANT_TRANSPORTS')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('DriverE', 'EDIT_TRANSPORT_ITEM_CONDITION')");

            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('WarehouseMan', 'VIEW_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('WarehouseMan', 'MANAGE_INVENTORY')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('WarehouseMan', 'STOCK_SHELVES')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('WarehouseMan', 'GET_SHIFT')");
            stmt.execute("INSERT INTO RolePermissions (roleName, permissionName) VALUES ('WarehouseMan', 'UPDATE_AVAILABLE')");

            stmt.execute("INSERT INTO Employees (israeliId, firstName, lastName, salary, startOfEmployment, isActive, creationDate, updateDate, branchId) " +
                    "VALUES (123456789, 'Admin', 'User', 20000, '2020-01-01', TRUE, '2023-01-01', '2023-01-01', 1)");
            stmt.execute("INSERT INTO Employees (israeliId, firstName, lastName, salary, startOfEmployment, isActive, creationDate, updateDate, branchId) " +
                    "VALUES (111111111, 'Shira', 'Steinbuch', 18000, '2017-02-02', TRUE, '2023-01-01', '2023-01-01', 2)");
            stmt.execute("INSERT INTO Employees (israeliId, firstName, lastName, salary, startOfEmployment, isActive, creationDate, updateDate, branchId) " +
                    "VALUES (222222222, 'Ramzi', 'Abd Rabo', 9500, '2020-11-15', TRUE, '2023-01-01', '2023-01-01', 2)");
            stmt.execute("INSERT INTO Employees (israeliId, firstName, lastName, salary, startOfEmployment, isActive, creationDate, updateDate, branchId) " +
                    "VALUES (333333333, 'Kochava', 'Shavit', 8500, '2020-11-15', TRUE, '2023-01-01', '2023-01-01', 2)");
            stmt.execute("INSERT INTO Employees (israeliId, firstName, lastName, salary, startOfEmployment, isActive, creationDate, updateDate, branchId) " +
                    "VALUES (444444444, 'Moshe', 'Cohen', 12000, '2020-01-01', TRUE, '2023-01-01', '2023-01-01', 2)");
            stmt.execute("INSERT INTO Employees (israeliId, firstName, lastName, salary, startOfEmployment, isActive, creationDate, updateDate, branchId) " +
                    "VALUES (555555555, 'Yael', 'Levy', 9000, '2020-01-01', TRUE, '2023-01-01', '2023-01-01', 2)");
            stmt.execute("INSERT INTO Employees (israeliId, firstName, lastName, salary, startOfEmployment, isActive, creationDate, updateDate, branchId) " +
                    "VALUES (666666666, 'David', 'Mizrahi', 8000, '2017-02-02', TRUE, '2023-01-01', '2023-01-01', 2)");
            stmt.execute("INSERT INTO Employees (israeliId, firstName, lastName, salary, startOfEmployment, isActive, creationDate, updateDate, branchId) " +
                    "VALUES (777777777, 'Emmanuel', 'Macroni', 8000, '2020-11-15', TRUE, '2023-01-01', '2023-01-01', 2)");
            stmt.execute("INSERT INTO Employees (israeliId, firstName, lastName, salary, startOfEmployment, isActive, creationDate, updateDate, branchId) " +
                    "VALUES (888888888, 'Doron', 'Yakov', 8000, '2019-11-15', TRUE, '2023-01-01', '2023-01-01', 2)");

            stmt.execute("INSERT INTO EmployeeRoles (israeliId, role) VALUES (123456789, 'Admin')");
            stmt.execute("INSERT INTO EmployeeRoles (israeliId, role) VALUES (111111111, 'HR manager')");
            stmt.execute("INSERT INTO EmployeeRoles (israeliId, role) VALUES (222222222, 'Stocker')");
            stmt.execute("INSERT INTO EmployeeRoles (israeliId, role) VALUES (333333333, 'Cashier')");
            stmt.execute("INSERT INTO EmployeeRoles (israeliId, role) VALUES (444444444, 'Transport Manager')");
            stmt.execute("INSERT INTO EmployeeRoles (israeliId, role) VALUES (555555555, 'DriverE')");
            stmt.execute("INSERT INTO EmployeeRoles (israeliId, role) VALUES (666666666, 'Cleaner')");
            stmt.execute("INSERT INTO EmployeeRoles (israeliId, role) VALUES (777777777, 'DriverE')");
            stmt.execute("INSERT INTO EmployeeRoles (israeliId, role) VALUES (888888888, 'WarehouseMan')");

            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (123456789, 'WorkingHours', '9:00-17:00')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (123456789, 'WorkingDays', 'Sunday-Thursday')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (123456789, 'VacationDays', '22')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (123456789, 'SickDays', '18')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (123456789, 'PensionFund', 'Menora Mivtachim')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (123456789, 'PensionRate', '6.5%')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (123456789, 'HealthInsurance', 'Maccabi')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (123456789, 'Position', 'System Administrator')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (123456789, 'Department', 'IT')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (111111111, 'WorkingHours', '8:00-16:00')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (111111111, 'WorkingDays', 'Sunday-Thursday')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (111111111, 'VacationDays', '18')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (111111111, 'SickDays', '14')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (111111111, 'PensionFund', 'Harel')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (111111111, 'PensionRate', '6%')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (111111111, 'HealthInsurance', 'Clalit')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (111111111, 'Position', 'HR Manager')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (111111111, 'Department', 'Management')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (111111111, 'Store', 'Shefa Issachar')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (222222222, 'WorkingHours', '7:00-15:00')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (222222222, 'WorkingDays', 'Sunday-Thursday')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (222222222, 'VacationDays', '12')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (222222222, 'SickDays', '10')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (222222222, 'PensionFund', 'Migdal')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (222222222, 'PensionRate', '5.5%')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (222222222, 'HealthInsurance', 'Meuhedet')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (222222222, 'Position', 'Stocker')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (222222222, 'Department', 'Floor Operations')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (222222222, 'Store', 'Shefa Issachar')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (333333333, 'WorkingHours', '15:00-23:00')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (333333333, 'WorkingDays', 'Sunday-Thursday')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (333333333, 'VacationDays', '10')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (333333333, 'SickDays', '10')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (333333333, 'PensionFund', 'Phoenix')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (333333333, 'PensionRate', '5%')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (333333333, 'HealthInsurance', 'Leumit')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (333333333, 'Position', 'Cashier')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (333333333, 'Department', 'Front Checkout')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (333333333, 'Store', 'Shefa Issachar')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (444444444, 'WorkingHours', '8:00-16:00')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (444444444, 'WorkingDays', 'Sunday-Thursday')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (444444444, 'VacationDays', '15')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (444444444, 'SickDays', '12')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (444444444, 'PensionFund', 'Clal')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (444444444, 'PensionRate', '6%')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (444444444, 'HealthInsurance', 'Maccabi')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (444444444, 'Position', 'HR Manager')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (444444444, 'Department', 'Human Resources')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (444444444, 'Store', 'Shefa Issachar')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (555555555, 'WorkingHours', '7:00-15:00')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (555555555, 'WorkingDays', 'Sunday-Thursday')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (555555555, 'VacationDays', '12')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (555555555, 'SickDays', '10')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (555555555, 'PensionFund', 'Harel')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (555555555, 'PensionRate', '5.5%')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (555555555, 'HealthInsurance', 'Clalit')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (555555555, 'Position', 'Driver')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (555555555, 'Department', 'Logistics')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (555555555, 'Store', 'Shefa Issachar')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (666666666, 'WorkingHours', '6:00-14:00')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (666666666, 'WorkingDays', 'Sunday-Thursday')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (666666666, 'VacationDays', '10')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (666666666, 'SickDays', '10')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (666666666, 'PensionFund', 'Migdal')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (666666666, 'PensionRate', '5%')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (666666666, 'HealthInsurance', 'Meuhedet')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (666666666, 'Position', 'Cleaner')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (666666666, 'Department', 'Maintenance')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (666666666, 'Store', 'Shefa Issachar')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (888888888, 'WorkingHours', '6:00-14:00')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (888888888, 'WorkingDays', 'Sunday-Thursday')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (888888888, 'VacationDays', '10')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (888888888, 'SickDays', '10')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (888888888, 'PensionFund', 'Migdal')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (888888888, 'PensionRate', '5%')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (888888888, 'HealthInsurance', 'Meuhedet')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (888888888, 'Position', 'Cleaner')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (888888888, 'Department', 'Maintenance')");
            stmt.execute("INSERT INTO EmployeeTerms (israeliId, termKey, termValue) VALUES (888888888, 'Store', 'Shefa Issachar')");

            // ShippingAreas
            stmt.execute("INSERT INTO ShippingAreas (areaNumber, areaName) VALUES (0, 'Central District')");
            stmt.execute("INSERT INTO ShippingAreas (areaNumber, areaName) VALUES (1, 'South District')");
            stmt.execute("INSERT INTO ShippingAreas (areaNumber, areaName) VALUES (2, 'East District')");

            // Sites
            stmt.execute("INSERT INTO Sites (areaNum, addressStr, contName, contNumber) VALUES (0, 'Tel Aviv', 'Yossi Oren', 0542315421)");
            stmt.execute("INSERT INTO Sites (areaNum, addressStr, contName, contNumber) VALUES (1, 'Ben Gurion Uni', 'Meni Adler', 0526451234)");
            stmt.execute("INSERT INTO Sites (areaNum, addressStr, contName, contNumber) VALUES (2, 'Afula', 'Dani Hendler', 0535471594)");

            // Trucks
            stmt.execute("INSERT INTO Trucks (truckNum, model, netWeight, maxCarryWeight, validLicense, inTransportID) VALUES (401, 'Truck Model A', 1000, 20, 'A', -1)");
            stmt.execute("INSERT INTO Trucks (truckNum, model, netWeight, maxCarryWeight, validLicense, inTransportID) VALUES (402, 'Truck Model B', 15000, 120, 'E', -1)");
            stmt.execute("INSERT INTO Trucks (truckNum, model, netWeight, maxCarryWeight, validLicense, inTransportID) VALUES (4022, 'Truck Model B1', 12000, 100, 'E', -1)");
            stmt.execute("INSERT INTO Trucks (truckNum, model, netWeight, maxCarryWeight, validLicense, inTransportID) VALUES (403, 'Truck Model C', 12000, 100, 'C', -1)");

            // Counters
            stmt.execute("INSERT INTO Counters (CounterName, CounterValue) VALUES ('transportIDCounter', 0)");

        }
    }





    private void createTransportA() throws JsonProcessingException {
        ArrayList<ItemsDocDTO> itemsDocDTOs = new ArrayList<>();
        SiteDTO srcSiteDTO = new SiteDTO(2, "Afula");
        SiteDTO destSiteDTO = new SiteDTO(123, "Test Address");

        ArrayList<ItemQuantityDTO> itemQs = new ArrayList<>();
        itemQs.add(new ItemQuantityDTO(1, new ItemDTO("water", 1.5, true), 20));

        ItemsDocDTO itemsDocDTO1 = new ItemsDocDTO(1, srcSiteDTO, destSiteDTO, itemQs, LocalDateTime.now().plusHours(1), 1);
        itemsDocDTOs.add(itemsDocDTO1);
        TransportDTO transportDTO = new TransportDTO(1, 402, 555555555, new SiteDTO(2, "Afula"),
                itemsDocDTOs, LocalDateTime.now(), enumTranStatus.BeingAssembled, 20, new ArrayList<enumTranProblem>());

        transportService.createTransport(444444444, objectMapper.writeValueAsString(transportDTO), -1);
    }
















    @Test
    void createTransport() throws JsonProcessingException {
        ArrayList<ItemsDocDTO> itemsDocDTOs = new ArrayList<>();
        SiteDTO srcSiteDTO = new SiteDTO(2, "Afula");
        SiteDTO destSiteDTO = new SiteDTO(123, "Test Address");

        ArrayList<ItemQuantityDTO> itemQs = new ArrayList<>();
        itemQs.add(new ItemQuantityDTO(1, new ItemDTO("water", 1.5, true), 20));

        ItemsDocDTO itemsDocDTO1 = new ItemsDocDTO(1, srcSiteDTO, destSiteDTO, itemQs, LocalDateTime.now().plusHours(1), 1);
        itemsDocDTOs.add(itemsDocDTO1);
        TransportDTO transportDTO = new TransportDTO(1, 402, 555555555, new SiteDTO(2, "Afula"),
                itemsDocDTOs, LocalDateTime.now(), enumTranStatus.BeingAssembled, 20, new ArrayList<enumTranProblem>());

        assertEquals("Success", transportService.createTransport(444444444, objectMapper.writeValueAsString(transportDTO), -1));
    }

    @Test
    void deleteTransport() throws JsonProcessingException {
        createTransportA();
        assertEquals("Success", transportService.deleteTransport(444444444, 1));
    }

    @Test
    void setTransportStatus() throws JsonProcessingException {
        createTransportA();
        assertEquals("Success", transportService.setTransportStatus(444444444, 1, "6"));
    }

    @Test
    void setTransportTruck() throws JsonProcessingException {
        createTransportA();
        assertEquals("Success", transportService.setTransportTruck(444444444, 1, 4022));
    }

    @Test
    void setTransportDriver() throws JsonProcessingException {
        createTransportA();
        assertEquals("Success", transportService.setTransportDriver(444444444, 1, 777777777));
    }

    @Test
    void isTruckDriverPairingGood() {
        assertEquals("Success", transportService.isTruckDriverPairingGood(444444444, 402, 555555555));
    }

//    @Test
//    void addDestSite() {
//    }

//    @Test
//    void removeDestSite() {
//    }
//
//    @Test
//    void setSiteArrivalIndexInTransport() {
//    }

    @Test
    void changeAnItemsDocNum() throws JsonProcessingException {
        createTransportA();
        assertEquals("Success", transportService.changeAnItemsDocNum(444444444, 1, 2));
    }

    @Test
    void checkValidItemsDocID() {
        assertTrue(transportService.checkValidItemsDocID(444444444, 3));
    }

    @Test
    void checkIfDriverDrivesThisItemsDoc() throws JsonProcessingException {
        createTransportA();
        assertEquals("Success", transportService.checkIfDriverDrivesThisItemsDoc(555555555, 1));
    }

    @Test
    void addTransportProblem() throws JsonProcessingException {
        createTransportA();
        assertEquals("Success", transportService.addTransportProblem(444444444, 1, "2"));
    }

    @Test
    void removeTransportProblem() throws JsonProcessingException {
        createTransportA();
        transportService.addTransportProblem(444444444, 1, "2");
        assertEquals("Success", transportService.removeTransportProblem(444444444, 1, "2"));
    }

    @Test
    void addItem() throws JsonProcessingException {
        createTransportA();
        assertEquals("Success", transportService.addItem(444444444, 1, "Bread", 0.75, 20, true));
    }

    @Test
    void removeItem() throws JsonProcessingException {
        createTransportA();
        transportService.addItem(444444444, 1, "Bread", 0.75, 20, true);
        assertEquals("Success", transportService.removeItem(444444444, 1, "Bread", 0.75, 20, true));
    }

    @Test
    void setItemCond() throws JsonProcessingException {
        createTransportA();
        transportService.addItem(444444444, 1, "Bread", 0.75, 20, true);
        assertEquals("Success", transportService.setItemCond(444444444, 1, "Bread", 0.75, 10, false));
    }




}
