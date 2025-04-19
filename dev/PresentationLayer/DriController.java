package PresentationLayer;

import ServiceLayer.EmployeeService;
import ServiceLayer.TransportService;

import java.util.Scanner;

public class DriController {
    private TransportService tran_s;
    private EmployeeService emp_s;
    private Scanner scanner;

    public DriController(TransportService trs, EmployeeService es, Scanner sc) {
        this.tran_s = trs;
        this.emp_s = es;
        this.scanner = sc;
    }


    void driverMainMenu(){
        System.out.println("   --------    Welcome, Transport Driver.    -------");
        System.out.println("   Select Action:\n");
        System.out.println("(1)  View All Transports Related to me");
        System.out.println("(2)  Edit Item's Condition in my Transport");
        System.out.println("(3)  ...");
        System.out.println("\n(4)  Exit The System");
        System.out.println();

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            viewAllTransportsRelatedtome();
        }else if(choice.equals("2")){
            editItemsConditioninmyTransport();
        }else if(choice.equals("3")){
            // TODO if needed
        } else if (choice.equals("4")) {
            System.out.println("\nExiting The System, Goodbye.\n");
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
            driverMainMenu();
        }
    }

    private void viewAllTransportsRelatedtome(){
        //TODO (I think that if we log in as a Role, then here first ask for the id and then do the rest according to that Id, so now we'll know who is the driver)
    }

    private void editItemsConditioninmyTransport(){
        //TODO (I think that if we log in as a Role, then here first ask for the id and then do the rest according to that Id, so now we'll know who is the driver)
    }






}
