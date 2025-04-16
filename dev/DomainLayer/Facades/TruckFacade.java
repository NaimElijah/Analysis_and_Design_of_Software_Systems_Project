package DomainLayer.Facades;

import com.fasterxml.jackson.*;
import DomainLayer.Objects.Truck;
import java.util.HashMap;

public class TruckFacade {
    private HashMap<Integer, Truck> trucksWareHouse;

    public TruckFacade() {
        trucksWareHouse = new HashMap<>();
    }

    //TODO
}
