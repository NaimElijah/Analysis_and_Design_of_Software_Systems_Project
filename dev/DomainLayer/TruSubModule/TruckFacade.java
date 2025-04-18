package DomainLayer.TruSubModule;

import java.util.HashMap;

public class TruckFacade {
    private HashMap<Integer, Truck> trucksWareHouse;

    public TruckFacade() {
        trucksWareHouse = new HashMap<>();
    }

    public String showTrucks(){
        //todo
        return "";
    }

    public void addTruck(int num, String model, int max_weight, int max_carry, String license) {}
    public void removeTruck(int num) {}
    public String truckToString(int num){
        //todo
        return "";
    }
}
