package DomainLayerTests;
import DomainLayer.EmpSubModule.Driver;
import DomainLayer.EmpSubModule.EmployeeFacade;
import DomainLayer.enums.enumDriLicense;
import DomainLayer.enums.enumPermissionRank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.xml.transform.TransformerException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeFacadeTest {

    private EmployeeFacade facade;

    @BeforeEach
    void setUp() {
        facade = new EmployeeFacade();
    }

    @Test
    void testAddDriverSuccessfully() throws KeyAlreadyExistsException {
        ArrayList<String> licenses = new ArrayList<>();
        licenses.add("A");
        licenses.add("B");

        facade.addDriver(1, "John", "Doe", licenses);
        assertTrue(facade.getEmployees().get(1) instanceof Driver);
        assertEquals(2, ((Driver) facade.getEmployees().get(1)).getLicenses().size());
    }

    @Test
    void testAddDriverDuplicateId() {
        ArrayList<String> licenses = new ArrayList<>();
        licenses.add("A");

        assertDoesNotThrow(() -> facade.addDriver(1, "John", "Doe", licenses));
        assertThrows(KeyAlreadyExistsException.class, () -> facade.addDriver(1, "Jane", "Smith", licenses));
    }

    @Test
    void testAddManagerSuccessfully() throws KeyAlreadyExistsException {
        facade.addManager(2, "Alice", "Brown");
        assertEquals(enumPermissionRank.Manager, facade.getEmployees().get(2).getPermissions_rank());
    }

    @Test
    void testAddManagerDuplicateId() throws KeyAlreadyExistsException {
        facade.addManager(2, "Alice", "Brown");
        assertThrows(KeyAlreadyExistsException.class, () -> facade.addManager(2, "Bob", "White"));
    }

    @Test
    void testInitializeAdminSuccessfully() throws Exception {
        facade.initializeAdmin(3, "Admin", "User");
        assertEquals(enumPermissionRank.Admin, facade.getEmployees().get(3).getPermissions_rank());
    }

    @Test
    void testInitializeAdminAlreadyExists() throws Exception {
        facade.initializeAdmin(3, "Admin", "User");
        assertThrows(IllegalAccessException.class, () -> facade.initializeAdmin(4, "Another", "Admin"));
    }

    @Test
    void testRemoveEmployeeByAdminSuccessfully() throws Exception {
        facade.addManager(5, "Manager", "One");
        assertThrows(ArithmeticException.class, () -> facade.removeEmployeeByAdmin(5));
    }

    @Test
    void testRemoveEmployeeByAdminNotFound() {
        assertThrows(ClassNotFoundException.class, () -> facade.removeEmployeeByAdmin(999));
    }

    @Test
    void testRemoveAdminFails() throws Exception {
        facade.initializeAdmin(10, "Admin", "Guy");
        assertThrows(AssertionError.class, () -> facade.removeEmployeeByAdmin(10));
    }

    @Test
    void testRemoveManagerWithNoOtherManagers() throws Exception {
        facade.addManager(20, "Only", "Manager");
        assertThrows(ArithmeticException.class, () -> facade.removeEmployeeByAdmin(20));
    }

    @Test
    void testRemoveEmployeeByManagerSuccessfully() throws Exception {
        ArrayList<String> licenses = new ArrayList<>();
        licenses.add("A");
        facade.addDriver(6, "Driver", "Test", licenses);
        facade.removeEmployeeByManager(6);
        assertFalse(facade.getEmployees().containsKey(6));
    }

    @Test
    void testRemoveEmployeeByManagerNotFound() {
        assertThrows(ClassNotFoundException.class, () -> facade.removeEmployeeByManager(999));
    }

    @Test
    void testRemoveEmployeeByManagerFailsOnManager() throws Exception {
        facade.addManager(7, "Manager", "Two");
        assertThrows(AssertionError.class, () -> facade.removeEmployeeByManager(7));
    }

    @Test
    void testGiveDriverManagersPermissionRankSuccessfully() throws Exception {
        ArrayList<String> licenses = new ArrayList<>();
        licenses.add("B");
        facade.addDriver(8, "Driver", "Smith", licenses);

        facade.giveADriverAManagersPermissionRank(8);
        assertEquals(enumPermissionRank.Manager, facade.getEmployees().get(8).getPermissions_rank());
    }

    @Test
    void testGiveDriverManagersPermissionRankFailsIfAlreadyManager() throws Exception {
        facade.addManager(9, "Manager", "Already");
        assertThrows(AssertionError.class, () -> facade.giveADriverAManagersPermissionRank(9));
    }

    @Test
    void testGiveDriverManagersPermissionRankFailsIfNotDriver() throws Exception {
        facade.addManager(11, "Manager", "Other");
        assertThrows(AssertionError.class, () -> facade.giveADriverAManagersPermissionRank(11));
    }

    @Test
    void testGetEmployeePermissionsRank() throws Exception {
        facade.initializeAdmin(100, "Admin", "One");
        assertEquals(0, facade.getEmployeePermissionsRank(100));

        facade.addManager(101, "Manager", "One");
        assertEquals(1, facade.getEmployeePermissionsRank(101));

        ArrayList<String> licenses = new ArrayList<>();
        licenses.add("C");
        facade.addDriver(102, "Driver", "One", licenses);
        assertEquals(2, facade.getEmployeePermissionsRank(102));

        assertEquals(-1, facade.getEmployeePermissionsRank(999));
    }

    @Test
    void testAddLicenseSuccessfully() throws Exception {
        ArrayList<String> licenses = new ArrayList<>();
        licenses.add("A");
        facade.addDriver(200, "Driver", "License", licenses);

        facade.addLicense(200, "B");
        assertTrue(((Driver) facade.getEmployees().get(200)).getLicenses().contains(enumDriLicense.B));
    }

    @Test
    void testAddLicenseAlreadyExists() throws Exception {
        ArrayList<String> licenses = new ArrayList<>();
        licenses.add("A");
        facade.addDriver(201, "Driver", "License", licenses);

        assertThrows(ArrayStoreException.class, () -> facade.addLicense(201, "A"));
    }

    @Test
    void testRemoveLicenseSuccessfully() throws Exception {
        ArrayList<String> licenses = new ArrayList<>();
        licenses.add("D");
        facade.addDriver(300, "Driver", "Remove", licenses);

        facade.removeLicense(300, "D");
        assertFalse(((Driver) facade.getEmployees().get(300)).getLicenses().contains(enumDriLicense.D));
    }

    @Test
    void testRemoveLicenseFailsIfNotExist() throws Exception {
        ArrayList<String> licenses = new ArrayList<>();
        licenses.add("D");
        facade.addDriver(301, "Driver", "Remove", licenses);

        assertThrows(ArrayStoreException.class, () -> facade.removeLicense(301, "A"));
    }

    @Test
    void testShowEmployeeSuccessfully() throws Exception {
        facade.addManager(400, "Employee", "Show");
        String output = facade.showEmployee(400);
        assertTrue(output.contains("Employee ID: 400"));
    }

    @Test
    void testShowEmployeeFailsIfNotExist() {
        assertThrows(ArrayStoreException.class, () -> facade.showEmployee(999));
    }

    @Test
    void testShowEmployees() throws Exception {
        facade.addManager(500, "Employee1", "Last1");
        facade.addManager(501, "Employee2", "Last2");

        String output = facade.showEmployees();
        assertTrue(output.contains("Employee1"));
        assertTrue(output.contains("Employee2"));
    }

    @Test
    void testShowDrivers() throws Exception {
        ArrayList<String> licenses = new ArrayList<>();
        licenses.add("B");
        facade.addDriver(600, "DriverOne", "LastOne", licenses);
        facade.addDriver(601, "DriverTwo", "LastTwo", licenses);

        String output = facade.showDrivers();
        assertTrue(output.contains("DriverOne"));
        assertTrue(output.contains("DriverTwo"));
    }

    @Test
    void testShowManagers() throws Exception {
        facade.addManager(700, "ManagerOne", "Last");
        facade.addManager(701, "ManagerTwo", "Last");

        String output = facade.showManagers();
        assertTrue(output.contains("ManagerOne"));
        assertTrue(output.contains("ManagerTwo"));
    }

}
