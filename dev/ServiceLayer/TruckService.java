package ServiceLayer;

import DomainLayer.TruSubModule.TruckFacade;

public class TruckService {
    private TruckFacade tru_f;

    public TruckService(TruckFacade ttff) {
        this.tru_f = ttff;
    }

    private String showTrucks(){
        return "";
        //TODO
    }

    private void addTruck(int num, String model, int net_wei, int max_carry, char License){
        //TODO
    }

    private void removeTruck(int num){
        //TODO
    }

    private String truckToString(int num){
        return "";
        //TODO
    }


    //TODO more if needed
}
