package DomainLayerTests;

import DTOs.ItemDTO;
import DTOs.ItemsDocDTO;
import DTOs.SiteDTO;
import DTOs.TransportDTO;
import DomainLayer.EmpSubModule.Driver;
import DomainLayer.EmpSubModule.EmployeeFacade;
import DomainLayer.SiteSubModule.Address;
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
    private TransportDoc transportDoc1;
    private TransportDoc transportDoc2;
    private Truck truck;
    private Driver driver;
    private ItemsDoc itemsDoc;
    private Site site;
    private Site destSite;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws JsonProcessingException, FileAlreadyExistsException, CommunicationException, FileNotFoundException, ClassNotFoundException, CloneNotSupportedException {
        employeeFacade = new EmployeeFacade();
        siteFacade = new SiteFacade();
        truckFacade = new TruckFacade();
        transportFacade = new TransportFacade(employeeFacade, siteFacade, truckFacade);
        objectMapper = new ObjectMapper();

        // Create a new TransportDoc
        truck = new Truck(1, "Truck-001", 1000, 100, enumDriLicense.D);
        ArrayList<enumDriLicense> driversLicenses = new ArrayList<>();
        driversLicenses.add(enumDriLicense.D);
        driversLicenses.add(enumDriLicense.E);
        driversLicenses.add(enumDriLicense.B);
        driver = new Driver(1, "Driver-001", "Driver001LastN", enumPermissionRank.Driver, driversLicenses);

        ArrayList<String> driversLicensesStr = new ArrayList<>();
        driversLicensesStr.add("D");
        driversLicensesStr.add("D");
        driversLicensesStr.add("B");

        employeeFacade.addDriver(1, "Driver-001", "Driver001LastN", driversLicensesStr);
        truckFacade.addTruck(1, "Truck-001", 1000, 100, "D");

        site = new Site(new Address(1, "Address-1"), "ContactName", 1234567890L);
        destSite = new Site(new Address(2, "Address-2"), "ContactName", 1234567890L);

        transportDoc1 = new TransportDoc(enumTranStatus.InTransit, 1, truck, driver, site);
        transportDoc2 = new TransportDoc(enumTranStatus.InTransit, 2, truck, driver, site);

        // Add transport docs
        transportFacade.getTransports().put(1, transportDoc1);
        transportFacade.getTransports().put(2, transportDoc2);

        // Initialize ItemsDoc
        itemsDoc = new ItemsDoc(1, site, destSite);
        transportFacade.getItemsDocs().put(1, itemsDoc);



        ArrayList<ItemsDocDTO> itemsDocDTOS = new ArrayList<>();

        HashMap<ItemDTO, Integer> itemDTOs1 = new HashMap<>();
        itemDTOs1.put(new ItemDTO("Water", 0.5, true), 5);
        itemDTOs1.put(new ItemDTO("Rice", 1, true), 10);
        ItemsDocDTO itemsDocDTO1 = new ItemsDocDTO(1, new SiteDTO(1,"Tel Aviv"), new SiteDTO(1, "Rishonim"), itemDTOs1);
        HashMap< ItemDTO, Integer> itemDTOs2 = new HashMap<>();
        itemDTOs2.put(new ItemDTO("Shampoo", 0.75, true), 10);
        itemDTOs2.put(new ItemDTO("Toothpaste", 0.2, true), 15);
        ItemsDocDTO itemsDocDTO2 = new ItemsDocDTO(2, new SiteDTO(1,"Tel Aviv"), new SiteDTO(2, "Dimona"), itemDTOs2);

        itemsDocDTOS.add(itemsDocDTO1);
        itemsDocDTOS.add(itemsDocDTO2);

        TransportDTO transportDTO = new TransportDTO(1, 1, new SiteDTO(1,"Tel Aviv"), itemsDocDTOS);
        transportFacade.createTransport(objectMapper.writeValueAsString(transportDTO), -100);


    }

    @Test
    void testIsTruckActive_ActiveTruck() {
        // Set a truck as active in another transport
        truck.setInTransportID(1);
        transportDoc1.setStatus(enumTranStatus.BeingAssembled);

        assertTrue(transportFacade.isTruckActive(truck));
    }

    @Test
    void testIsTruckActive_InactiveTruck() {
        truck.setInTransportID(-1); // Not in transport

        assertFalse(transportFacade.isTruckActive(truck));
    }

    @Test
    void testGetAQueuedTransportAsDTOJson_Success() throws Exception {
        // Assuming a queued transport exists
        transportFacade.getQueuedTransports().add(transportDoc1);

        String result = transportFacade.getAQueuedTransportAsDTOJson(1);
        assertNotNull(result);
    }

    @Test
    void testGetAQueuedTransportAsDTOJson_IndexOutOfBounds() {
        transportFacade.getQueuedTransports().add(transportDoc1);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            transportFacade.getAQueuedTransportAsDTOJson(2);
        });
    }

    @Test
    void testAddTransportProblem_Success() throws Exception {
        transportFacade.addTransportProblem(1, 1); // Add a Puncture problem

        assertTrue(transportDoc1.getProblems().contains(enumTranProblem.Puncture));
    }

    @Test
    void testAddTransportProblem_AlreadyExists() throws FileAlreadyExistsException, FileNotFoundException {
        transportFacade.addTransportProblem(1, 1); // Add a Puncture problem

        assertThrows(FileAlreadyExistsException.class, () -> {
            transportFacade.addTransportProblem(1, 1); // Try adding the same problem again
        });
    }

    @Test
    void testRemoveTransportProblem_Success() throws Exception {
        transportFacade.addTransportProblem(1, 1); // Add a Puncture problem
        transportFacade.removeTransportProblem(1, 1); // Remove the Puncture problem

        assertFalse(transportDoc1.getProblems().contains(enumTranProblem.Puncture));
    }

    @Test
    void testRemoveTransportProblem_NotExist() {
        assertThrows(FileAlreadyExistsException.class, () -> {
            transportFacade.removeTransportProblem(1, 1); // Remove a non-existent problem
        });
    }

    @Test
    void testAddDestSiteToTransport_Success() throws Exception {
        transportFacade.addDestSiteToTransport(1, 2, 2, "New Address", "New Contact", 9876543210L);

        assertEquals(2, transportDoc1.getDests_Docs().size());
    }

    @Test
    void testAddDestSiteToTransport_AlreadyExists() {
        assertThrows(FileAlreadyExistsException.class, () -> {
            transportFacade.addDestSiteToTransport(1, 1, 1, "Address", "Contact", 9876543210L); // Site already exists
        });
    }

    @Test
    void testRemoveDestSiteFromTransport_Success() throws Exception {
        transportFacade.addDestSiteToTransport(1, 2, 2, "New Address", "New Contact", 9876543210L);
        transportFacade.removeDestSiteFromTransport(1, 2); // Remove destination site

        assertEquals(0, transportDoc1.getDests_Docs().size());
    }

    @Test
    void testRemoveDestSiteFromTransport_NotExist() {
        assertThrows(CommunicationException.class, () -> {
            transportFacade.removeDestSiteFromTransport(1, 99); // Removing a non-existent destination site
        });
    }

    @Test
    void testSetSiteArrivalIndexInTransport_Success() throws Exception {
        TransportDoc transportDoc1 = new TransportDoc(enumTranStatus.InTransit, 1, truck, driver, site);
        transportFacade.addDestSiteToTransport(1, 101, 10, "Site-1", "John Doe", 123456789L);
        transportFacade.addDestSiteToTransport(1, 102, 20, "Site-2", "Jane Doe", 987654321L);

        ItemsDoc itemsDoc1 = transportDoc1.getDests_Docs().get(0); // The first ItemsDoc added
        assertEquals(0, transportDoc1.getDests_Docs().indexOf(itemsDoc1));

        transportFacade.setSiteArrivalIndexInTransport(1, 10, "Site-1", 2);

        assertEquals(1, transportDoc1.getDests_Docs().indexOf(itemsDoc1));
    }


    @Test
    void testSetSiteArrivalIndexInTransport_InvalidIndex() throws FileAlreadyExistsException, CommunicationException, FileNotFoundException {
        transportFacade.addDestSiteToTransport(1, 2, 2, "New Address", "New Contact", 9876543210L);

        assertThrows(AbstractMethodError.class, () -> {
            transportFacade.setSiteArrivalIndexInTransport(1, 2, "New Address", 99); // Invalid index
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
        transportFacade.getItemsDocs().put(3, itemsDoc); // Adding item document with ID 3
        assertThrows(KeyAlreadyExistsException.class, () -> {
            transportFacade.changeAnItemsDocNum(1, 3); // Trying to change to an already existing ID
        });
    }

    @Test
    void testCheckValidItemsDocID() {
        assertTrue(transportFacade.checkValidItemsDocID(99)); // Checking non-existent ItemsDoc
        assertFalse(transportFacade.checkValidItemsDocID(1)); // Checking existing ItemsDoc
    }

    @Test
    void testCheckIfDriverDrivesThisItemsDoc_Success() throws Exception {
        transportFacade.checkIfDriverDrivesThisItemsDoc(1, 1); // Driver drives this ItemsDoc
    }

    @Test
    void testCheckIfDriverDrivesThisItemsDoc_Failure() {
        assertThrows(IllegalAccessException.class, () -> {
            transportFacade.checkIfDriverDrivesThisItemsDoc(99, 1); // Driver does not drive this ItemsDoc
        });
    }

    @Test
    void testAddItem_Success() throws Exception {
        transportFacade.addItem(1, "Item-1", 5.0, 10, true);

        assertTrue(itemsDoc.getGoodItems().containsKey(new Item("Item-1", 5.0, true)));
    }

    @Test
    void testRemoveItem_Success() throws Exception {
        transportFacade.addItem(1, "Item-1", 5.0, 10, true);
        transportFacade.removeItem(1, "Item-1", 5.0, 5, true);

        assertEquals(5, itemsDoc.getGoodItems().get(new Item("Item-1", 5.0, true)));
    }

    @Test
    void testRemoveItem_NotFound() {
        assertThrows(FileNotFoundException.class, () -> {
            transportFacade.removeItem(1, "Item-NotFound", 5.0, 10, true); // Item not found
        });
    }

    @Test
    void testSetItemCond_Success() throws Exception {
        transportFacade.addItem(1, "Item-1", 5.0, 10, true); // Add item with initial quantity 10
        transportFacade.setItemCond(1, "Item-1", 5.0, 10, false); // Set item condition to false

        // Check if the quantity is zero or the condition is set as expected
        assertEquals(0, (int) itemsDoc.getGoodItems().get(new Item("Item-1", 5.0, true)));
    }


    @Test
    void testSetItemCond_NotFound() {
        assertThrows(ClassNotFoundException.class, () -> {
            transportFacade.setItemCond(1, "Item-NotFound", 5.0, 10, false); // Item not found
        });
    }

    @Test
    void testShowTransportsOfDriver() throws Exception {
        String result = transportFacade.showTransportsOfDriver(1);
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

