package ServiceLayerTests;

import DBUtil.TestDatabase;
import DTOs.BranchDTO;
import DTOs.EmployeeDTO;
import DTOs.ShiftDTO;
import DTOs.TransportModuleDTOs.TransportDTO;
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
import DomainLayer.enums.enumTranStatus;
import ServiceLayer.EmployeeIntegrationService;
import ServiceLayer.EmployeeSubModule.EmployeeService;
import ServiceLayer.EmployeeSubModule.ShiftService;
import ServiceLayer.TransportServices.SiteService;
import ServiceLayer.TransportServices.TransportService;
import ServiceLayer.TransportServices.TruckService;
import Util.Database;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        // Close connection
        if (connection != null) {
            connection.close();
        }
    }

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException {

        truckFacade.loadDBData();      ///    added by me
        siteFacade.loadDBData();      ///    added by me
        transportService.loadDBData();      ///    added by me

        // Create a test branch
        testBranch = new Branch(1L, "Test Branch", 123, "Test Address", null);
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
        roles.add("TestRole");
        Map<String, Object> termsOfEmployment = new HashMap<>();
        termsOfEmployment.put("contractType", "FullTime");

        testEmployee = new Employee(
                123456789, // israeliId
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
        shiftId = 1L;
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











    @Test
    void createTransport() {
        TransportDTO transportDTO = new TransportDTO(1, enumTranStatus.BeingAssembled, 402, 555555555, );
        transportService.createTransport(444444444, )
    }

    @Test
    void deleteTransport() {
    }

    @Test
    void setTransportStatus() {
    }

    @Test
    void setTransportTruck() {
    }

    @Test
    void setTransportDriver() {
    }

    @Test
    void isTruckDriverPairingGood() {
    }

    @Test
    void addDestSite() {
    }

    @Test
    void removeDestSite() {
    }

    @Test
    void setSiteArrivalIndexInTransport() {
    }

    @Test
    void changeAnItemsDocNum() {
    }

    @Test
    void checkValidItemsDocID() {
    }

    @Test
    void checkIfDriverDrivesThisItemsDoc() {
    }

    @Test
    void addTransportProblem() {
    }

    @Test
    void removeTransportProblem() {
    }

    @Test
    void addItem() {
        //TODO                  <<<-----------------------------
    }

    @Test
    void removeItem() {
        //TODO                  <<<-----------------------------
    }

    @Test
    void setItemCond() {
        //TODO                  <<<-----------------------------
    }

    @Test
    void showAllQueuedTransports() {
        //TODO                  <<<-----------------------------
    }

    @Test
    void showAllTransports() {
        //TODO                  <<<-----------------------------
    }

    @Test
    void showAllDrivers() {
        //TODO                  <<<-----------------------------
    }




}
