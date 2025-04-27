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


    void driverMainMenu(int id){
        //TODO:  make a quick menu before this and ask for Driver ID, so we can see data related to a specified driver only
        //TODO:  also according to the ID we'll know what are his permmissions and acoording to that we will give him the appropriate menu options.
        System.out.println("   --------    Welcome, Transport Driver.    -------\n");
        System.out.println("     --------   Transport Manager Menu    -------");
        System.out.println("(1)  View All Transports Related to me");
        System.out.println("(2)  Edit Item's Condition in my Transport");
        System.out.println("(3)  View My Details");
        System.out.println("(4)  Disconnect and Go Back to Main Program Menu");
        System.out.println();
        System.out.println("   Select Action:\n");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            viewAllTransportsRelatedtome(id);
            driverMainMenu();
        }else if(choice.equals("2")){
            editItemsConditioninmyTransport(id);
            driverMainMenu();
        }else if(choice.equals("3")){
            this.emp_s.showEmployee(id);
        } else if (choice.equals("4")) {
            System.out.println("\nGoing Back to Main Program Menu.\n");
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
            driverMainMenu();
        }
    }

    private void viewAllTransportsRelatedtome(int id){
        //TODO
    }

    private void editItemsConditioninmyTransport(int id){
        //TODO
    }



    public int getEmployeePermissionsRank(int id){
        return this.emp_s.getEmployeePermissionsRank(id);
    }





}
