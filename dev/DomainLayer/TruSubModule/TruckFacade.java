package DomainLayer.TruSubModule;

import DomainLayer.EmpSubModule.EmployeeFacade;
import java.util.HashMap;

public class TruckFacade {
    private HashMap<Integer, Truck> trucksWareHouse;

    public TruckFacade() {
        trucksWareHouse = new HashMap<>();
    }

    public void addTruck(int num, String model, int max_weight, int max_carry, String license) {}
    public void removeTruck(int num) {}

    public String showAllTrucks(){
        String res = "Trucks Warehouse Capacity:\n";
        for (Truck truck : trucksWareHouse.values()) {
            res += truck.toString() + "\n";
        }
        return res;
    }

}
