package PresentationLayer;

//import ServiceLayer.TranEmployeeService;
import ServiceLayer.StartUpStateService;

public class StartUpController {
    private StartUpStateService startUpStateService;

//    public StartUpController(StartUpStateService startUpStateService, TranEmployeeService tranEmployeeService) {
//        this.startUpStateService = startUpStateService;
//    }

    public void startUpData(){
        System.out.println("     --------     StartUp     -------");
        System.out.println("\nLoading starting Data Into The System...\n");
        String res = startUpStateService.loadData();
        if(res.equals("SUCCESS")){
            System.out.println("\nSuccessfully Finished Loading Starting Data.\n");
        }else{
            System.out.println("\nFailed Loading Starting Data\n\n");
        }
        System.out.println("\nStarting the System...\n\n");
    }


//    public int getEmployeePermissionsRank(int loginIDGiven) {   //  return the permission of that ID (0, 1, 2)
//        return this.startUpStateService.getEmployeePermissionsRank(loginIDGiven);
//    }


}



