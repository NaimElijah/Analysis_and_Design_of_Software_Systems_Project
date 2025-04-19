package ServiceLayer;

import DomainLayer.TruSubModule.TruckFacade;

public class TruckService {
    private TruckFacade tru_f;

    public TruckService(TruckFacade ttff) {
        this.tru_f = ttff;
    }

    public String showTrucks(){
        String res = "";
        try {
            res = tru_f.showAllTrucks();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public void addTruck(int num, String model, int net_wei, int max_carry, String License){
        //TODO
    }

    public void removeTruck(int num){
        //TODO
    }

    public String truckToString(int num){   //TODO : seems like this function is not needed
        return "";
        //TODO
    }


}
