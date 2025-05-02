package DomainLayerTests;

import DTOs.*;
import DomainLayer.EmpSubModule.Driver;
import DomainLayer.EmpSubModule.Employee;
import DomainLayer.EmpSubModule.EmployeeFacade;
import DomainLayer.SiteSubModule.Address;
import DomainLayer.SiteSubModule.ShippingArea;
import DomainLayer.SiteSubModule.Site;
import DomainLayer.SiteSubModule.SiteFacade;
import DomainLayer.TranSubModule.Item;
import DomainLayer.TranSubModule.ItemsDoc;
import DomainLayer.TranSubModule.TransportDoc;
import DomainLayer.TranSubModule.TransportFacade;
import DomainLayer.TruSubModule.Truck;
import DomainLayer.TruSubModule.TruckFacade;
import DomainLayer.enums.enumDriLicense;
import DomainLayer.enums.enumPermissionRank;
import DomainLayer.enums.enumTranProblem;
import DomainLayer.enums.enumTranStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.naming.CommunicationException;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TransportFacadeTest {
    private TransportFacade transportFacade;
    private EmployeeFacade employeeFacade;
    private SiteFacade siteFacade;
    private TruckFacade truckFacade;

    private ShippingArea area1;
    private ShippingArea area2;
    private Site site1;
    private Site site2;
    private Site site3;
    private Site site4;
    private Truck truck1;
    private Truck truck2;
    private Truck truck3;
    private Driver driver1;
    private Driver driver2;
    private Driver driver3;
    private Employee admin;
    private Employee manager1;
    private Employee manager2;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws JsonProcessingException, ClassNotFoundException, IllegalAccessException {
        employeeFacade = new EmployeeFacade();
        siteFacade = new SiteFacade();
        truckFacade = new TruckFacade();
        transportFacade = new TransportFacade(employeeFacade, siteFacade, truckFacade);
        objectMapper = new ObjectMapper();

        /// shipping area:
        area1 = new ShippingArea(1, "Central District");
        siteFacade.addShippingArea(1, "Central District");

        area2 = new ShippingArea(2, "South District");
        siteFacade.addShippingArea(2, "South District");

        /// sites:
        site1 = new Site(new Address(1, "Ramla"), "Bob Hanely", 524561234);
        siteFacade.addSiteTOArea(1, "Ramla", "Bob Hanely", 524561234);

        site2 = new Site(new Address(1, "Rishonim"), "Jack Graham", 534872456);
        siteFacade.addSiteTOArea(1, "Rishonim", "Jack Graham", 534872456);

        site3 = new Site(new Address(2, "Ashkelon"), "Alice Green", 553671958);
        siteFacade.addSiteTOArea(2, "Ashkelon", "Alice Green", 553671958);

        site4 = new Site(new Address(2, "Dimona"), "Daniel Greenberg", 586457912);
        siteFacade.addSiteTOArea(2, "Dimona", "Daniel Greenberg", 586457912);

        /// trucks:
        truck1 = new Truck(1010, "Toyota K8", 1200, 100, enumDriLicense.D);
        truckFacade.addTruck(1010, "Toyota K8", 1200, 100, "D");

        truck2 = new Truck(2020, "Hyundai R6", 1000, 100, enumDriLicense.C);
        truckFacade.addTruck(2020, "Hyundai R6", 1000, 100, "C");

        truck3 = new Truck(3030, "Yamaha Lite", 200, 20, enumDriLicense.A);
        truckFacade.addTruck(3030, "Yamaha Lite", 200, 20, "A");

        ///  Admin, Managers, Drivers:
        employeeFacade.initializeAdmin(111, "Cody", "Weber");
        admin = new Employee(111, "Cody", "Weber", enumPermissionRank.Admin);

        employeeFacade.addManager(222, "Naim", "Elijah");
        manager1 = new Employee(222, "Naim", "Elijah", enumPermissionRank.Manager);
        employeeFacade.addManager(333, "Bar", "Miyara");
        manager2 = new Employee(333, "Bar", "Miyara", enumPermissionRank.Manager);

        ArrayList<String> tomsLicenses = new ArrayList<>();
        tomsLicenses.add("A");
        tomsLicenses.add("C");
        tomsLicenses.add("E");
        ArrayList<enumDriLicense> tomsLicensesEnum = new ArrayList<>();
        tomsLicensesEnum.add(enumDriLicense.A);
        tomsLicensesEnum.add(enumDriLicense.C);
        tomsLicensesEnum.add(enumDriLicense.E);
        driver1 = new Driver(444, "Tom", "Hat", enumPermissionRank.Driver, tomsLicensesEnum);
        employeeFacade.addDriver(444, "Tom", "Hat", tomsLicenses);

        ArrayList<String> xaviersLicenses = new ArrayList<>();
        xaviersLicenses.add("B");
        xaviersLicenses.add("D");
        xaviersLicenses.add("E");
        ArrayList<enumDriLicense> xaviersLicensesEnum = new ArrayList<>();
        xaviersLicensesEnum.add(enumDriLicense.B);
        xaviersLicensesEnum.add(enumDriLicense.D);
        xaviersLicensesEnum.add(enumDriLicense.E);
        driver2 = new Driver(555, "Xavier", "Hernandez", enumPermissionRank.Driver, xaviersLicensesEnum);
        employeeFacade.addDriver(555, "Xavier", "Hernandez", xaviersLicenses);

        ArrayList<String> maxLicenses = new ArrayList<>();
        maxLicenses.add("A");
        maxLicenses.add("B");
        maxLicenses.add("C");
        maxLicenses.add("D");
        maxLicenses.add("E");
        ArrayList<enumDriLicense> maxLicensesEnum = new ArrayList<>();
        maxLicensesEnum.add(enumDriLicense.A);
        maxLicensesEnum.add(enumDriLicense.B);
        maxLicensesEnum.add(enumDriLicense.C);
        maxLicensesEnum.add(enumDriLicense.D);
        maxLicensesEnum.add(enumDriLicense.E);
        driver3 = new Driver(666, "Max", "Turner", enumPermissionRank.Driver, maxLicensesEnum);
        employeeFacade.addDriver(666, "Max", "Turner", maxLicenses);

        /// Transports
        ArrayList<ItemsDocDTO> itemsDocDTOs = new ArrayList<>();

        ArrayList<ItemQuantityDTO> itemQuantityDTOs1 = new ArrayList<>();
        itemQuantityDTOs1.add(new ItemQuantityDTO(new ItemDTO("Water", 0.5, true), 5));
        itemQuantityDTOs1.add(new ItemQuantityDTO(new ItemDTO("Rice", 1, true), 10));
        ItemsDocDTO itemsDocDTO1 = new ItemsDocDTO(1, new SiteDTO(1,"Ramla"), new SiteDTO(1, "Rishonim"), itemQuantityDTOs1);

        ArrayList<ItemQuantityDTO> itemQuantityDTOs2 = new ArrayList<>();
        itemQuantityDTOs2.add(new ItemQuantityDTO(new ItemDTO("Shampoo", 0.75, true), 10));
        itemQuantityDTOs2.add(new ItemQuantityDTO(new ItemDTO("Toothpaste", 0.2, true), 15));
        ItemsDocDTO itemsDocDTO2 = new ItemsDocDTO(2, new SiteDTO(1,"Ramla"), new SiteDTO(2, "Dimona"), itemQuantityDTOs2);

        itemsDocDTOs.add(itemsDocDTO1);
        itemsDocDTOs.add(itemsDocDTO2);

        TransportDTO transportDTO = new TransportDTO(1010, 555, new SiteDTO(1,"Ramla"), itemsDocDTOs);
        transportFacade.createTransport(objectMapper.writeValueAsString(transportDTO), -100);

    }






    @Test
    void testIsTruckActive_ActiveTruck() {
        assertTrue(transportFacade.isTruckActive(truckFacade.getTrucksWareHouse().get(1010)));
    }

    @Test
    void testIsTruckActive_InactiveTruck() {
        assertFalse(transportFacade.isTruckActive(truckFacade.getTrucksWareHouse().get(3030)));
    }

    @Test
    void testGetAQueuedTransportAsDTOJson_Success() throws Exception {
        // Assuming a queued transport exists
        transportFacade.getQueuedTransports().add(new TransportDoc(enumTranStatus.Queued, 99, truck2, driver1, site3));

        String result = transportFacade.getAQueuedTransportAsDTOJson(1);
        assertNotNull(result);
    }

    @Test
    void testGetAQueuedTransportAsDTOJson_IndexOutOfBounds() {
        transportFacade.getQueuedTransports().add(new TransportDoc(enumTranStatus.Queued, 99, truck2, driver1, site3));

        assertThrows(IndexOutOfBoundsException.class, () -> { transportFacade.getAQueuedTransportAsDTOJson(2); });
    }

    @Test
    void testAddTransportProblem_Success() throws Exception {
        transportFacade.addTransportProblem(1, 1); // Add a Puncture problem

        assertTrue(transportFacade.getTransports().get(1).getProblems().contains(enumTranProblem.Puncture));
    }

    @Test
    void testAddTransportProblem_AlreadyExists() throws FileAlreadyExistsException, FileNotFoundException {
        transportFacade.addTransportProblem(1, 1); // Add a Puncture problem

        // Try adding the same problem again
        assertThrows(FileAlreadyExistsException.class, () -> { transportFacade.addTransportProblem(1, 1); });
    }

    @Test
    void testRemoveTransportProblem_Success() throws Exception {
        transportFacade.addTransportProblem(1, 1); // Add a Puncture problem
        transportFacade.removeTransportProblem(1, 1); // Remove the Puncture problem

        assertFalse(transportFacade.getTransports().get(1).getProblems().contains(enumTranProblem.Puncture));
    }

    @Test
    void testRemoveTransportProblem_NotExist() {
        // Remove a non-existent problem
        assertThrows(FileAlreadyExistsException.class, () -> { transportFacade.removeTransportProblem(1, 1); });
    }

    @Test
    void testAddDestSiteToTransport_Success() throws Exception {
        transportFacade.addDestSiteToTransport(1, 3, 2, "Ashkelon", "New Contact", 536543210);
        assertEquals(3, transportFacade.getTransports().get(1).getDests_Docs().size());
    }

    @Test
    void testAddDestSiteToTransport_AlreadyExists() {
        assertThrows(CommunicationException.class, () -> {
            transportFacade.addDestSiteToTransport(1, 4, 2, "Dimona", "Contact", 556543210); // Site already exists
        });
    }

    @Test
    void testRemoveDestSiteFromTransport_Success() throws Exception {
        transportFacade.removeDestSiteFromTransport(1, 2); // Remove destination site
        assertEquals(1, transportFacade.getTransports().get(1).getDests_Docs().size());
    }

    @Test
    void testRemoveDestSiteFromTransport_NotExist() {
        // Removing a non-existent destination site
        assertThrows(CommunicationException.class, () -> { transportFacade.removeDestSiteFromTransport(1, 99); });
    }

    @Test
    void testSetSiteArrivalIndexInTransport_Success() throws Exception {
        transportFacade.addDestSiteToTransport(1, 4, 2, "Ashkelon", "Alice Green", 536719584);

        // Now change the index of the added site to be first in arrival order
        transportFacade.setSiteArrivalIndexInTransport(1, 2, "Ashkelon", 1);

        assertEquals("Ashkelon", transportFacade.getTransports().get(1).getDests_Docs().get(0).getDest_site().getAddress().getAddress());
    }



    @Test
    void testSetSiteArrivalIndexInTransport_InvalidIndex() throws FileAlreadyExistsException, CommunicationException, FileNotFoundException, ClassNotFoundException {
        transportFacade.addDestSiteToTransport(1, 4, 2, "Ashkelon", "Alice Green", 536719584);

        assertThrows(AbstractMethodError.class, () -> {
            transportFacade.setSiteArrivalIndexInTransport(1, 2, "Ashkelon", 99); // Invalid index
        });
    }

    @Test
    void testChangeAnItemsDocNum_Success() throws Exception {
        transportFacade.changeAnItemsDocNum(1, 3);
        assertTrue(transportFacade.getItemsDocs().containsKey(3));
        assertFalse(transportFacade.getItemsDocs().containsKey(1));
    }

    @Test
    void testChangeAnItemsDocNum_AlreadyExists() {
        assertThrows(KeyAlreadyExistsException.class, () -> { transportFacade.changeAnItemsDocNum(1, 2); });
    }


    @Test
    void testCheckValidItemsDocID() {
        assertTrue(transportFacade.checkValidItemsDocID(99)); // Checking non-existent ItemsDoc
        assertFalse(transportFacade.checkValidItemsDocID(1)); // Checking existing ItemsDoc
    }

    @Test
    void testCheckIfDriverDrivesThisItemsDoc_Success() throws Exception {
        transportFacade.checkIfDriverDrivesThisItemsDoc(555, 1); // Driver drives this ItemsDoc
    }

    @Test
    void testCheckIfDriverDrivesThisItemsDoc_Failure() {
        assertThrows(IllegalAccessException.class, () -> {
            transportFacade.checkIfDriverDrivesThisItemsDoc(444, 1); // Driver does not drive this ItemsDoc
        });
    }

    @Test
    void testAddItem_Success() throws Exception {
        transportFacade.addItem(1, "Item-1", 5.0, 10, true);
        assertTrue(transportFacade.getItemsDocs().get(1).getGoodItems().containsKey(new Item("Item-1", 5.0, true)));
    }

    @Test
    void testRemoveItem_Success() throws Exception {
        transportFacade.addItem(1, "Item-1", 5.0, 10, true);
        transportFacade.removeItem(1, "Item-1", 5.0, 5, true);
        assertEquals(5, transportFacade.getItemsDocs().get(1).getGoodItems().get(new Item("Item-1", 5.0, true)));
    }

    @Test
    void testRemoveItem_NotFound() {
        assertThrows(ClassNotFoundException.class, () -> {
            transportFacade.removeItem(1, "Item-NotFound", 5.0, 10, true); // Item not found
        });
    }

    @Test
    void testSetItemCond_Success() throws Exception {
        transportFacade.addItem(1, "Item-1", 5.0, 10, true); // Add item with initial quantity 10
        transportFacade.setItemCond(1, "Item-1", 5.0, 10, false); // Set item condition to false

        // Check if the quantity is zero or the condition is set as expected
        assertEquals(null, transportFacade.getItemsDocs().get(1).getGoodItems().get(new Item("Item-1", 5.0, true)));
        assertEquals(10, transportFacade.getItemsDocs().get(1).getBadItems().get(new Item("Item-1", 5.0, false)));
    }


    @Test
    void testSetItemCond_NotFound() {
        assertThrows(ClassNotFoundException.class, () -> {
            transportFacade.setItemCond(1, "Item-NotFound", 5.0, 10, false); // Item not found
        });
    }

    @Test
    void testShowTransportsOfDriver() throws Exception {
        String result = transportFacade.showTransportsOfDriver(555);
        assertNotNull(result);
    }

    @Test
    void testShowAllQueuedTransports() throws Exception {
        String result = transportFacade.showAllQueuedTransports();
        assertNotNull(result);
    }

    @Test
    void testShowAllTransports() throws Exception {
        String result = transportFacade.showAllTransports();
        assertNotNull(result);
    }

}

