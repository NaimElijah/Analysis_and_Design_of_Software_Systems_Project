package DomainLayer.TruSubModule;

import DomainLayer.EmpSubModule.EmployeeFacade;
import java.util.HashMap;

public class TruckFacade {
    private HashMap<Integer, Truck> trucksWareHouse;

    public TruckFacade() {
        trucksWareHouse = new HashMap<>();
    }

    public HashMap<Integer, Truck> getTrucksWareHouse() {return trucksWareHouse;}
    public void setTrucksWareHouse(HashMap<Integer, Truck> trucksWareHouse) {this.trucksWareHouse = trucksWareHouse;}
    public void addTruck(int num, String model, int max_weight, int max_carry, String license) {}
    public void removeTruck(int num) {}

    public String showAllTrucks(){
        String res = "Trucks Warehouse:\n";
        for (Truck truck : trucksWareHouse.values()) {
            res += truck.toString() + "\n";
        }
        return res;
    }

}
