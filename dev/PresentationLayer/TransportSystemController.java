package PresentationLayer;

import ServiceLayer.EmployeeService;
import ServiceLayer.TransportService;
import ServiceLayer.SiteService;
import ServiceLayer.TruckService;

import java.util.Scanner;
import com.fasterxml.jackson.*;

public class TransportSystemController {
    private TruckService sys;
    private TransportService as;
    private SiteService ms;
    private EmployeeService es;
    private Scanner scanner;
    private int loggedInMode;

    public TransportSystemController(TruckService sys, TransportService as, SiteService ms, EmployeeService es) {
        this.sys = sys;
        this.as = as;
        this.ms = ms;
        this.es = es;
        this.scanner = new Scanner(System.in);
        loggedInMode = 0;
    }

    public void transportModuleStartup(){ MainMenu(); }


    void MainMenu(){
        System.out.println("   --------    Main Menu    -------");
        System.out.println("   Welcome to the Transport System.");
        System.out.println("   What would you like to login as ?\n");
        System.out.println("1. System Admin");
        System.out.println("2. Transport Manager");
        System.out.println("3. Transport Driver");

        String choice = scanner.nextLine();

        if(choice.equals("1")){
            loggedInMode = 1;
            MainAdminMenu();     //  start the program for the system admin
        }else if(choice.equals("2")){
            loggedInMode = 2;
            MainManagerMenu();     //  start the program for the transport manager
        }else if(choice.equals("3")){
            loggedInMode = 3;
            MainDriverMenu();     //  start the program for the transport driver
        }

    }


    void MainAdminMenu(){
        System.out.println("   --------    Welcome, System Administrator.    -------");
        System.out.println("   Select Action:\n");
        System.out.println("1. ...");
        System.out.println("2. ...");
        System.out.println("3. ...");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            // TODO: send down to the TransportService
        }else if(choice.equals("2")){
            // TODO: send down to the TransportService
        }else if(choice.equals("3")){
            // TODO: send down to the TransportService
        }

        //TODO
    }

    void MainManagerMenu(){
        System.out.println("   --------    Welcome, Transport Manager.    -------");
        System.out.println("   Select Action:\n");
        System.out.println("1. ...");
        System.out.println("2. ...");
        System.out.println("3. ...");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            // TODO: send down to the SiteService
        }else if(choice.equals("2")){
            // TODO: send down to the SiteService
        }else if(choice.equals("3")){
            // TODO: send down to the SiteService
        }

        //TODO
    }


    void MainDriverMenu(){
        System.out.println("   --------    Welcome, Transport Driver.    -------");
        System.out.println("   Select Action:\n");
        System.out.println("1. ...");
        System.out.println("2. ...");
        System.out.println("3. ...");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            // TODO: send down to the DriverService
        }else if(choice.equals("2")){
            // TODO: send down to the DriverService
        }else if(choice.equals("3")){
            // TODO: send down to the DriverService
        }

        //TODO
    }


    void transportOptionsMenu(){
        //TODO: validate that it really is a Manager, check the loggedInMode
        System.out.println("--------    Transport Options Menu    -------");
        System.out.println("1. Add Transport");
        System.out.println("2. Remove Transport");
        System.out.println("3. Edit Transport");
        System.out.println("4. View Transport");
        System.out.println("5. Go Back");
    }

    //TODO more menus
}
