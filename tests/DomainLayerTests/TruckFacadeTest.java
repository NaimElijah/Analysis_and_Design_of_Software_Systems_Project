package DomainLayerTests;

import DomainLayer.TransportDomain.TruckSubModule.Truck;
import DomainLayer.TransportDomain.TruckSubModule.TruckFacade;
import DomainLayer.enums.enumDriLicense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.management.openmbean.KeyAlreadyExistsException;
import java.sql.SQLException;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

class TruckFacadeTest {

    private TruckFacade truckFacade;

    @BeforeEach
    void setUp() throws SQLException {
        truckFacade = new TruckFacade();
        truckFacade.loadDBData();
    }

    @Test
    void testAddTruckSuccessfully() throws KeyAlreadyExistsException, SQLException, ClassNotFoundException {
        truckFacade.addTruck(1, "Volvo", 4000, 10000, "C");
        assertEquals(1, truckFacade.getTruckRepo().getTrucksWareHouse().size());
        Truck truck = truckFacade.getTruckRepo().getTrucksWareHouse().get(1);
        assertNotNull(truck);
        assertEquals(1, truck.getTruck_num());
        assertEquals("Volvo", truck.getModel());
        assertEquals(4000, truck.getNet_weight());
        assertEquals(10000, truck.getMax_carry_weight());
        assertEquals(enumDriLicense.C, truck.getValid_license());
        truckFacade.removeTruck(1);
    }

    @Test
    void testAddTruckAlreadyExists() throws KeyAlreadyExistsException, SQLException, ClassNotFoundException {
        truckFacade.addTruck(1, "Volvo", 4000, 10000, "C");
        assertThrows(KeyAlreadyExistsException.class, () -> {
            truckFacade.addTruck(1, "Mercedes", 3500, 9000, "B");
        });
        truckFacade.removeTruck(1);
    }

    @Test
    void testRemoveTruckSuccessfully() throws Exception {
        truckFacade.addTruck(1, "Volvo", 4000, 10000, "C");
        assertEquals(4, truckFacade.getTruckRepo().getTrucksWareHouse().size());
        truckFacade.removeTruck(1);
        assertEquals(3, truckFacade.getTruckRepo().getTrucksWareHouse().size());
    }

    @Test
    void testRemoveTruckNotExist() {
        assertThrows(ClassNotFoundException.class, () -> {
            truckFacade.removeTruck(999);
        });
    }

    @Test
    void testRemoveTruckBusyInTransport() throws Exception {
        truckFacade.addTruck(1, "Volvo", 4000, 10000, "C");
        truckFacade.getTruckRepo().getTrucksWareHouse().get(1).setInTransportID(5); // simulate busy truck
        assertThrows(ArrayStoreException.class, () -> {
            truckFacade.removeTruck(1);
        });
        truckFacade.getTruckRepo().getTrucksWareHouse().get(1).setInTransportID(-1); // simulate busy truck
        truckFacade.removeTruck(1);
    }

    @Test
    void testShowAllTrucksEmpty() {
        String output = truckFacade.showAllTrucks();
        assertEquals("Trucks Warehouse:\n", output);
    }

    @Test
    void testShowAllTrucksNonEmpty() throws KeyAlreadyExistsException, SQLException, ClassNotFoundException {
        truckFacade.addTruck(1, "Volvo", 4000, 10000, "C");
        truckFacade.addTruck(2, "Mercedes", 3500, 9000, "B");
        String output = truckFacade.showAllTrucks();
        assertTrue(output.contains("Truck Num: 1"));
        assertTrue(output.contains("Truck Num: 2"));
        assertTrue(output.contains("Model: Volvo"));
        assertTrue(output.contains("Model: Mercedes"));
        truckFacade.removeTruck(1);
        truckFacade.removeTruck(2);
    }

}
