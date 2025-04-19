package PresentationLayer;

import ServiceLayer.EmployeeService;
import ServiceLayer.SiteService;
import ServiceLayer.TransportService;
import ServiceLayer.TruckService;
import java.util.Scanner;

public class TranManController {
    private TruckService sys;
    private TransportService as;
    private SiteService ms;
    private EmployeeService es;
    private Scanner scanner;

    public TranManController(TruckService sys, TransportService as, SiteService ms, EmployeeService es, Scanner sc) {
        this.sys = sys;
        this.as = as;
        this.ms = ms;
        this.es = es;
        this.scanner = sc;
    }

    void transportManagerMainMenu(){   ////////////////////////////////   Main Menu   <<<--------------------------------------------
        System.out.println("   --------    Transport Manager Menu    -------\n");
        System.out.println("(1)  Transports Options Menu");
        System.out.println("(2)  Shipping Areas Options Menu");
        System.out.println("(3)  Sites Options Menu");
        System.out.println("(4)  Drivers Options Menu");
        System.out.println("(5)  Trucks Options Menu");
        System.out.println("(6)  Exit The System");
        System.out.println();
        System.out.println(" Select Action: ");
        String choice = scanner.nextLine();
        switch (choice){
            case "1":
                transportsOptionsMenu();
                transportManagerMainMenu(); // return to the transportManagerMainMenu
                break;
            case "2":
                shippingAreasOptionsMenu();
                transportManagerMainMenu(); // return to the transportManagerMainMenu
                break;
            case "3":
                sitesOptionsMenu();
                transportManagerMainMenu(); // return to the transportManagerMainMenu
                break;
            case "4":
                driversOptionsMenu();
                transportManagerMainMenu(); // return to the transportManagerMainMenu
                break;
            case "5":
                trucksOptionsMenu();
                transportManagerMainMenu(); // return to the transportManagerMainMenu
                break;
            case "6":
                System.out.println("\nExiting The System, Goodbye.\n");
                break;
        }
        System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
        transportManagerMainMenu();
    }













    private void transportsOptionsMenu(){   ////////////////////////////////   Transports Menu   <<<--------------------------------------------
        System.out.println("   --------    Transport Options Menu    -------\n");
        System.out.println("(1)  View All Transports");
        System.out.println("(2)  Create a Transport");
        System.out.println("(3)  Delete a Transport");
        System.out.println("(4)  Edit a Transport");
        System.out.println("(5)  Back to Transport Manager Main Menu");
        System.out.println();
        System.out.println(" Select Action: ");
        String choice = scanner.nextLine();
        if(choice.equals("1")){
            showAllTransports();   //TODO: dive deeper here send JSONs between Layers.
        }else if(choice.equals("2")){
            createaTransportMenu();
        }else if(choice.equals("3")){
            deleteaTransportMenu();
        } else if (choice.equals("4")) {
            editaTransportMenu();
        } else if (choice.equals("5")) {
            System.out.println("\n\n");
            transportManagerMainMenu();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
            transportsOptionsMenu();
        }
    }

    //////////////////////          HELPER FUNCTIONS FOR THE Transports Options Menu

    private void showAllTransports(){
        System.out.println(as.showAllTransports());
        transportManagerMainMenu();
    }

    private void createaTransportMenu(){
        System.out.println("   --------    Transport Creation    -------");
        System.out.println();
        System.out.println(" Select Action: ");
    }


    private void deleteaTransportMenu(){
        //TODO
    }

    private void editaTransportMenu(){
        System.out.println("(1)  Edit a Transport's Status");
        System.out.println("(2)  Edit a Transport's Problems");  //TODO: I just copied to here the relavent Features for the Menu here
        System.out.println("(3)  Edit a Transport's Sites");
        System.out.println("(4)  Edit a Transport's Items");
        System.out.println("(5)  Back to Transports Options Menu");
        System.out.println();
        System.out.println(" Select Action: ");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            // TODO: send down to the Service Layer
        }else if(choice.equals("2")){
            // TODO: send down to the Service Layer
        }else if(choice.equals("3")){
            // TODO: send down to the Service Layer
        } else if (choice.equals("4")) {
            // TODO
        } else if (choice.equals("5")) {
            System.out.println("\n\n");
            transportsOptionsMenu();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
            editaTransportMenu();
        }

    }


















    private void shippingAreasOptionsMenu(){   ////////////////////////////////   Shipping Areas Menu   <<<--------------------------------------------
        System.out.println("   --------    Shipping Areas Options Menu    -------\n");
        System.out.println("(1)  View All Shipping Areas");
        System.out.println("(2)  Add a Shipping Area");
        System.out.println("(3)  Edit a Shipping Area's Details");  //TODO: I just copied to here the relavent Features for the Menu here
        System.out.println("(4)  Delete a Shipping Area");
        System.out.println("(5)  Back to Transport Manager Main Menu");
        System.out.println();
        System.out.println(" Select Action: ");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            viewAllShippingAreas();
        }else if(choice.equals("2")){
            addaShippingArea();
        }else if(choice.equals("3")){
            editaShippingAreasDetails();
        } else if (choice.equals("4")) {
            deleteaShippingArea();
        } else if (choice.equals("5")) {
            System.out.println("\n\n");
            transportManagerMainMenu();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
            shippingAreasOptionsMenu();
        }

    }

    //////////////////////          HELPER FUNCTIONS FOR THE Shipping Areas Options Menu

    private void viewAllShippingAreas(){
        //TODO
    }

    private void addaShippingArea(){
        //TODO     <<<------------------------------------------
    }

    private void editaShippingAreasDetails(){
        //TODO
    }

    private void deleteaShippingArea(){
        //TODO
    }


















    private void sitesOptionsMenu(){   ////////////////////////////////    Sites Menu   <<<--------------------------------------------
        System.out.println("   --------    Sites Options Menu    -------\n");
        System.out.println("(1)  View All Sites");
        System.out.println("(2)  Add a Site");
        System.out.println("(3)  Edit a Site's Details");  //TODO: I just copied to here the relavent Features for the Menu here
        System.out.println("(4)  Delete a Site");
        System.out.println("(5)  Back to Transport Manager Main Menu");
        System.out.println();
        System.out.println(" Select Action: ");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            viewAllSites();
        }else if(choice.equals("2")){
            addaSite();
        }else if(choice.equals("3")){
            editaSitesDetails();
        } else if (choice.equals("4")) {
            deleteaSite();
        } else if (choice.equals("5")) {
            System.out.println("\n\n");
            transportManagerMainMenu();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
            sitesOptionsMenu();
        }
    }

    //////////////////////          HELPER FUNCTIONS FOR THE Sites Options Menu

    private void viewAllSites(){
        //TODO
    }

    private void addaSite(){
        //TODO
    }

    private void editaSitesDetails(){
        //TODO
    }

    private void deleteaSite(){
        //TODO
    }




















    private void driversOptionsMenu(){   ////////////////////////////////    Drivers Menu   <<<--------------------------------------------
        System.out.println("   --------    Drivers Options Menu    -------\n");
        System.out.println("(1)  View All Drivers");
        System.out.println("(2)  Add a Driver");
        System.out.println("(3)  Delete a Driver");  //TODO: I just copied to here the relavent Features for the Menu here
        System.out.println("(4)  Edit a Driver's Details");
        System.out.println("(5)  Back to Transport Manager Menu");
        System.out.println();
        System.out.println(" Select Action: ");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            viewAllDrivers();
        }else if(choice.equals("2")){
            addaDriver();
        }else if(choice.equals("3")){
            deleteaDriver();
        } else if (choice.equals("4")) {
            editaDriversDetails();
        } else if (choice.equals("5")) {
            System.out.println("\n\n");
            transportManagerMainMenu();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
            driversOptionsMenu();
        }
    }

    //////////////////////          HELPER FUNCTIONS FOR THE Drivers Options Menu

    private void viewAllDrivers(){
        //TODO
    }

    private void addaDriver(){
        //TODO
    }

    private void deleteaDriver(){
        //TODO
    }

    private void editaDriversDetails(){
        //TODO
    }



















    private void trucksOptionsMenu(){   ////////////////////////////////    Trucks Menu   <<<--------------------------------------------
        System.out.println("   --------    Trucks Options Menu    -------\n");
        System.out.println("(1)  View All Trucks");
        System.out.println("(2)  Add a Truck");
        System.out.println("(3)  Delete a Truck");  //TODO: I just copied to here the relavent Features for the Menu here
        System.out.println("(4)  Edit a Truck's Details");
        System.out.println("(5)  Back to Transport Manager Menu");
        System.out.println();
        System.out.println(" Select Action: ");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            viewAllTrucks();
        }else if(choice.equals("2")){
            addaTruck();
        }else if(choice.equals("3")){
            deleteaTruck();
        } else if (choice.equals("4")) {
            editaTrucksDetails();
        } else if (choice.equals("5")) {
            System.out.println("\n\n");
            transportManagerMainMenu();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
            trucksOptionsMenu();
        }
    }

    //////////////////////          HELPER FUNCTIONS FOR THE Trucks Options Menu

    private void viewAllTrucks(){
        //TODO
    }
    private void addaTruck(){
        //TODO
    }
    private void deleteaTruck(){
        //TODO
    }
    private void editaTrucksDetails(){
        //TODO
    }









}
