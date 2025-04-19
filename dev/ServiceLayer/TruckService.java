package ServiceLayer;

import DomainLayer.TruSubModule.TruckFacade;

public class TruckService {
    private TruckFacade tru_f;

    public TruckService(TruckFacade ttff) {
        this.tru_f = ttff;
    }

    public String showTrucks(){
        return "";
        //TODO
    }

    public void addTruck(int num, String model, int net_wei, int max_carry, char License){
        //TODO
    }

    public void removeTruck(int num){
        //TODO
    }

    public String truckToString(int num){
        return "";
        //TODO
    }


}
