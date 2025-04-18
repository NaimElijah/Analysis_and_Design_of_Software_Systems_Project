package ServiceTests;

import DomainLayer.TranSubModule.TransportFacade;
import ServiceLayer.TruckService;
import org.junit.jupiter.api.Test;

class TruckServiceTests {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        TransportFacade tf = new TransportFacade();
        TruckService TF = new TruckService(tf);

    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    void testFunc1(){
        //
    }

}