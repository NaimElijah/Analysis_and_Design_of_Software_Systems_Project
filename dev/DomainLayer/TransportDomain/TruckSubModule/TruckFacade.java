package DomainLayer.TransportDomain.TruckSubModule;

import DomainLayer.enums.enumDriLicense;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.HashMap;

public class TruckFacade {
    private HashMap<Integer, Truck> trucksWareHouse;

    public TruckFacade() { trucksWareHouse = new HashMap<>(); }

    public HashMap<Integer, Truck> getTrucksWareHouse() {return trucksWareHouse;}
    public void setTrucksWareHouse(HashMap<Integer, Truck> trucksWareHouse) {this.trucksWareHouse = trucksWareHouse;}


    public void addTruck(int num, String model, double net_wei, double max_carry, String license) throws KeyAlreadyExistsException {
        if (this.trucksWareHouse.containsKey(num)){ throw new KeyAlreadyExistsException("Truck Number already exists in the Warehouse"); }
        enumDriLicense val_license = null;
        if (license.equals("A")){
            val_license = enumDriLicense.A;
        } else if (license.equals("B")){
            val_license = enumDriLicense.B;
        } else if (license.equals("C")){
            val_license = enumDriLicense.C;
        } else if (license.equals("D")){
            val_license = enumDriLicense.D;
        } else if (license.equals("E")){
            val_license = enumDriLicense.E;
        }
        this.trucksWareHouse.put(num, new Truck(num, model, net_wei, max_carry, val_license));
    }


    public void removeTruck(int num) throws ClassNotFoundException, ArrayStoreException {
        if (!this.trucksWareHouse.containsKey(num)){ 
            throw new ClassNotFoundException("Truck Number doesn't exist in the Warehouse");
        } else if (this.trucksWareHouse.get(num).getInTransportID() != -1) {   //  if Truck is busy in an active Transport
            throw new ArrayStoreException("Truck cannot be deleted because it is written in an Active Transport, if you want you can remove it from its transports or remove the Transports");
        }
        this.trucksWareHouse.get(num).setIsDeleted(true);
        this.trucksWareHouse.remove(num);
    }


    public String showAllTrucks(){
        String res = "Trucks Warehouse:\n";
        for (Truck truck : trucksWareHouse.values()) {
            res += truck.toString() + "\n";
        }
        return res;
    }

}
