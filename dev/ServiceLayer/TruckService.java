package ServiceLayer;

import DomainLayer.TruSubModule.TruckFacade;

import javax.management.openmbean.KeyAlreadyExistsException;

public class TruckService {
    private EmployeeIntegrationService employeeIntegrationServiceService;
    private TruckFacade tru_f;

    public TruckService(TruckFacade ttff, EmployeeIntegrationService eis) {
        this.tru_f = ttff;
        this.employeeIntegrationServiceService = eis;
    }

    //TODO:  We need to add a Permission checking function to the EmployeeIntegrationService.
    //TODO:  We need to add a Permission checking function to the EmployeeIntegrationService.
    //TODO:  We need to add a Permission checking function to the EmployeeIntegrationService.
    //TODO:  We need to add a Permission checking function to the EmployeeIntegrationService.
    //TODO:  We need to add a Permission checking function to the EmployeeIntegrationService.

    public String addTruck(long loggedID, int num, String model, double net_wei, double max_carry, String license){
        if (num < 0 || net_wei < 0 || max_carry < 0){ return "The Truck's Number/Net.Weight/MaxCarryWeight you enter cannot be negative"; }
        if (model.isEmpty() || model.isBlank() || license.isEmpty() || license.isBlank()){ return "The Truck's Model/license you enter cannot be empty"; }
        if (!(license.equals("A") || license.equals("B") || license.equals("C") || license.equals("D") || license.equals("E"))){
            return "The Truck's license needs to be either A, B, C, or D or E";
        }
        try {
            this.tru_f.addTruck(num, model, net_wei, max_carry, license);
        } catch (KeyAlreadyExistsException e) {
            return "Truck Number already exists in the Warehouse";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }



    public String removeTruck(long loggedID, int num){
        if (num < 0){ return "The Truck Number you enter cannot be negative"; }
        try {
            this.tru_f.removeTruck(num);
        } catch (ClassNotFoundException e) {
            return "Truck Number doesn't exist in the Warehouse";
        } catch (ArrayStoreException e) {
            return "Truck cannot be deleted because it is written in an Active Transport, if you want you can remove it from its transports or remove the Transports";
        }catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }




    public String showTrucks(long loggedID){
        String res = "";
        try {
            res = tru_f.showAllTrucks();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }









    public TruckFacade getTru_f() {return tru_f;}


}
