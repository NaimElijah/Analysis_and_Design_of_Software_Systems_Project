package PresentationLayer;

import ServiceLayer.StartUpStateService;

import java.util.Scanner;

public class StartUpController {
    private StartUpStateService startUpStateService;
    private Scanner sc;

    public StartUpController(StartUpStateService startUpStateService, Scanner sc) {
        this.startUpStateService = startUpStateService;
        this.sc = sc;
    }

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

}



