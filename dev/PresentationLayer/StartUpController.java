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

    public void startUpDataMenu(){
        System.out.println("         --------    Before StartUp Menu    -------");
        System.out.println("   First, would you like to load starting data into the system ?\n");
        System.out.println("(1)  Yes");
        System.out.println("(2)  No");
        System.out.println();
        String choice = sc.nextLine();
        if(choice.equals("1")){
            System.out.println("\nLoading starting Data Into The System...\n");
            String res = startUpStateService.loadData();
            if(res.equals("SUCCESS")){
                System.out.println("\nSuccessfully Finished Loading Starting Data.\n");
            }else{
                System.out.println("\nFailed Loading Starting Data\n\n");
            }
            System.out.println("\nStarting the System...\n\n");
        }else if(choice.equals("2")){
            System.out.println("\nStarting System without loading starting data...\n\n");
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
            startUpDataMenu();
        }

    }


}
