package PresentationLayer;

import ServiceLayer.EmployeeService;
import ServiceLayer.SiteService;
import ServiceLayer.TransportService;
import ServiceLayer.TruckService;
import java.util.Scanner;

public class TranManController {
    private TruckService tru_ser;
    private TransportService tra_ser;
    private SiteService site_ser;
    private EmployeeService emp_ser;
    private Scanner scanner;

    public TranManController(TruckService ts, TransportService trs, SiteService sis, EmployeeService es, Scanner sc) {
        this.tru_ser = ts;
        this.tra_ser = trs;
        this.site_ser = sis;
        this.emp_ser = es;
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
        System.out.println(" Select Options Menu: ");
        String choice = scanner.nextLine();
        switch (choice){
            case "1":
                transportsOptionsMenu();
                break;
            case "2":
                shippingAreasOptionsMenu();
                break;
            case "3":
                sitesOptionsMenu();
                break;
            case "4":
                driversOptionsMenu();
                break;
            case "5":
                trucksOptionsMenu();
                break;
            case "6":
                System.out.println("\nExiting The System, Goodbye.\n");
                break;
            default:
                System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
                transportManagerMainMenu();
                break;
        }
    }













    private void transportsOptionsMenu(){   ////////////////////////////////   Transports Menu   <<<--------------------------------------------
        System.out.println("   --------    Transport Options Menu    -------\n");
        System.out.println("(1)  View All Transports");
        System.out.println("(2)  Create a Transport");
        System.out.println("(3)  Delete a Transport");
        System.out.println("(4)  Edit a Transport");
        System.out.println("(5)  Back to Transport Manager Main Menu");
        System.out.println();
        System.out.println(" Select Option: ");
        String choice = scanner.nextLine();
        if(choice.equals("1")){
            showAllTransports();
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
        System.out.println("   --------    Showing All Transports    --------\n");
        System.out.println(tra_ser.showAllTransports());
        System.out.println();
        transportManagerMainMenu();
    }

    private void createaTransportMenu(){
        System.out.println("   --------    Transport Creation    --------\n");
        System.out.println("...");
        System.out.println(" Select Option: ");
        //TODO


        //TODO:   maybe also when a driver/truck are unavailable we can choose to put in waitqueue or try to choose another
    }


    private void deleteaTransportMenu(){
        System.out.println("   --------    Transport Deletion    --------\n");
        //TODO
    }

    private void editaTransportMenu(){
        System.out.println("   --------    Transport Edition Menu    --------\n");
        System.out.println("(1)  Edit a Transport's Status");
        System.out.println("(2)  Edit a Transport's Problems");
        System.out.println("(3)  Edit a Transport's Sites");    //TODO :  also add order to site edition here   <<<-----------------------
        System.out.println("(4)  Edit a Transport's Items");
        System.out.println("(5)  Back to Transports Options Menu");
        System.out.println();
        System.out.println(" Select Option: ");

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
        System.out.println("(3)  Edit a Shipping Area's Details");
        System.out.println("(4)  Delete a Shipping Area");
        System.out.println("(5)  Back to Transport Manager Main Menu");
        System.out.println();
        System.out.println(" Select Option: ");

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
        System.out.println("   --------    Showing All Shipping Areas    --------\n");
        System.out.println(site_ser.showAllShippingAreas());
        System.out.println();
        shippingAreasOptionsMenu();
    }

    private void addaShippingArea(){
        System.out.println("   --------    Adding a Shipping Area    -------\n");

        System.out.println("Enter Area Number: ");
        int areaNum = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter Area Name: ");
        String areaName = scanner.nextLine();

        String res = site_ser.addShippingArea(areaNum, areaName);
        if(res.equals("Success")){
            System.out.println("Successfully Added Shipping Area\n");
        } else if(res.equals("Exception")){
            System.out.println("Failed to add Shipping Area due to technical machine error\n");
        }else { System.out.println(res + "\n"); }  // printing error string given from Service Layer

        System.out.println();
        shippingAreasOptionsMenu();
    }



    private void deleteaShippingArea(){
        System.out.println("   --------    Deleting a Shipping Area    -------\n");
        System.out.println("Enter area number: ");
        int areaNum = Integer.parseInt(scanner.nextLine());

        String res = site_ser.deleteShippingArea(areaNum);

        if(res.equals("Success")){
            System.out.println("Successfully Deleted Shipping Area\n");
        } else if(res.equals("Exception")){
            System.out.println("Failed to Delete Shipping Area due to technical machine error\n");
        }else { System.out.println(res + "\n"); }  // printing error string given from Service Layer

        System.out.println();
        shippingAreasOptionsMenu();
    }

    private void editaShippingAreasDetails(){
        System.out.println("   --------    Editing a Shipping Area Menu    -------\n");
        System.out.println("Enter Data of Shipping Area to Edit:");
        System.out.println("Enter Area Number: ");
        int areaNum = Integer.parseInt(scanner.nextLine());

        System.out.println("\nWhat information would you like to edit?: ");
        System.out.println("1. Area Number");
        System.out.println("2. Area Name");
        System.out.println(" Select Option: ");

        int infoType = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter Updated Data: ");
        String res = "";
        if (infoType == 1){
            int Newareanum = Integer.parseInt(scanner.nextLine());
            res = site_ser.setShippingAreaNum(areaNum, Newareanum);
        } else if (infoType == 2) {
            String NewareaName = scanner.nextLine();
            res = site_ser.setShippingAreaName(areaNum, NewareaName);
        }

        if(res.equals("Success")){
            System.out.println("Successfully Edited Site\n");
        } else if(res.equals("Exception")){
            System.out.println("Failed to Edit Site due to technical machine error\n");
        }else { System.out.println(res + "\n"); }  // printing error string given from Service Layer

        System.out.println();
        shippingAreasOptionsMenu();
    }








    private void sitesOptionsMenu(){   ////////////////////////////////    Sites Menu   <<<--------------------------------------------
        System.out.println("   --------    Sites Options Menu    -------\n");
        System.out.println("(1)  View All Sites");
        System.out.println("(2)  Add a Site");
        System.out.println("(3)  Edit a Site's Details");
        System.out.println("(4)  Delete a Site");
        System.out.println("(5)  Back to Transport Manager Main Menu");
        System.out.println();
        System.out.println(" Select Option: ");

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
        System.out.println("   --------    Showing All Sites    --------\n");
        System.out.println(site_ser.showAllSites());
        System.out.println();
        sitesOptionsMenu();
    }

    private void addaSite(){
        System.out.println("   --------    Adding a Site    -------\n");
        System.out.println("Enter Area Number: ");
        int areaNum = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter Address: ");
        String address = scanner.nextLine();
        System.out.println("Enter contact name: ");
        String contName = scanner.nextLine();
        System.out.println("Enter contact number: ");
        long contNum = Long.parseLong(scanner.nextLine());

        String res = site_ser.addSite(areaNum, address, contName, contNum);
        if(res.equals("Success")){
            System.out.println("Successfully Added Site\n");
        } else if(res.equals("Exception")){
            System.out.println("Failed to add Site due to technical machine error\n");
        }else { System.out.println(res + "\n"); }  // printing error string given from Service Layer

        System.out.println();
        sitesOptionsMenu();
    }

    private void deleteaSite(){
        System.out.println("   --------    Deleting a Site    -------\n");
        System.out.println("Enter area number: ");
        int areaNum = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter address: ");
        String address = scanner.nextLine();

        String res = site_ser.deleteSite(areaNum, address);

        if(res.equals("Success")){
            System.out.println("Successfully Deleted Site\n");
        } else if(res.equals("Exception")){
            System.out.println("Failed to Delete Site due to technical machine error\n");
        }else { System.out.println(res + "\n"); }  // printing error string given from Service Layer

        System.out.println();
        sitesOptionsMenu();
    }

    private void editaSitesDetails(){
        System.out.println("   --------    Editing a Site Menu    -------\n");
        System.out.println("Enter Data of Site to Edit:");
        System.out.println("Enter Area Number: ");
        int areaNum = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter Address: ");
        String address = scanner.nextLine();

        System.out.println("\nWhat information would you like to edit?: ");
        System.out.println("1. Site Area Number");
        System.out.println("2. Site Address String");
        System.out.println("3. Site Contact Name");
        System.out.println("4. Site Contact Number");
        System.out.println(" Select Option: ");

        int infoType = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter Updated Data: ");
        String res = "";
        if (infoType == 1){
            int Newareanum = Integer.parseInt(scanner.nextLine());
            res = site_ser.setSiteAreaNum(areaNum, Newareanum, address);
        } else if (infoType == 2) {
            String Newaddress = scanner.nextLine();
            res = site_ser.setSiteAddress(areaNum, address, Newaddress);
        } else if (infoType == 3) {
            String NewcontName = scanner.nextLine();
            res = site_ser.setSiteContName(areaNum, address, NewcontName);
        } else if (infoType == 4) {
            long NewContnum = Long.parseLong(scanner.nextLine());
            res = site_ser.setSiteContNum(areaNum, address, NewContnum);
        }

        if(res.equals("Success")){
            System.out.println("Successfully Edited Site\n");
        } else if(res.equals("Exception")){
            System.out.println("Failed to Edit Site due to technical machine error\n");
        }else { System.out.println(res + "\n"); }  // printing error string given from Service Layer

        System.out.println();
        sitesOptionsMenu();
    }






















    private void driversOptionsMenu(){   ////////////////////////////////    Drivers Menu   <<<--------------------------------------------
        System.out.println("   --------    Drivers Options Menu    -------\n");
        System.out.println("(1)  View All Drivers");
        System.out.println("(2)  Add a Driver");
        System.out.println("(3)  Delete a Driver");  //TODO: I just copied to here the relavent Features for the Menu here
        System.out.println("(4)  Edit a Driver's Details");
        System.out.println("(5)  Back to Transport Manager Menu");
        System.out.println();
        System.out.println(" Select Option: ");

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
        System.out.println("   --------    Showing All Drivers    --------\n");
        System.out.println(emp_ser.showDrivers());
        System.out.println();
        driversOptionsMenu();
    }

    private void addaDriver(){
        System.out.println("   --------    Driver Addition    --------\n");
        //TODO

        System.out.println();
        driversOptionsMenu();
    }

    private void deleteaDriver(){
        System.out.println("   --------    Driver Deletion    --------\n");
        //TODO

        System.out.println();
        driversOptionsMenu();
    }

    private void editaDriversDetails(){
        System.out.println("   --------    Driver Details Edition Menu    --------\n");
        //TODO

        System.out.println();
        driversOptionsMenu();
    }



















    private void trucksOptionsMenu(){   ////////////////////////////////    Trucks Menu   <<<--------------------------------------------
        System.out.println("   --------    Trucks Options Menu    -------\n");
        System.out.println("(1)  View All Trucks");
        System.out.println("(2)  Add a Truck");
        System.out.println("(3)  Delete a Truck");  //TODO: I just copied to here the relavent Features for the Menu here
        System.out.println("(4)  Edit a Truck's Details");
        System.out.println("(5)  Back to Transport Manager Menu");
        System.out.println();
        System.out.println(" Select Option: ");

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
        System.out.println("   --------    Showing All Trucks    --------\n");
        System.out.println(tru_ser.showTrucks());
        System.out.println();
        trucksOptionsMenu();  //  returning to the menu after the action
    }


    private void addaTruck(){
        System.out.println("   --------    Truck Addition    --------\n");
        //TODO

        System.out.println();
        trucksOptionsMenu();  //  returning to the menu after the action
    }


    private void deleteaTruck(){
        System.out.println("   --------    Truck Deletion    --------\n");
        //TODO

        System.out.println();
        trucksOptionsMenu();  //  returning to the menu after the action
    }


    private void editaTrucksDetails(){
        System.out.println("   --------    Truck Details Edition Menu    --------\n");
        //TODO

        System.out.println();
        trucksOptionsMenu();  //  returning to the menu after the action
    }









}
