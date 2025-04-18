package PresentationLayer;

import ServiceLayer.EmployeeService;
import ServiceLayer.TransportService;

import java.util.Scanner;

public class SysAdController {
    private TransportService as;
    private EmployeeService es;
    private Scanner scanner;

    public SysAdController(TransportService as, EmployeeService es, Scanner sc) {
        this.as = as;
        this.es = es;
        this.scanner = sc;
    }


    void systemAdminMainMenu(){
        System.out.println("   --------    Welcome, System Administrator.    -------");
        System.out.println("   Select Action:\n");
        System.out.println("(1)  Show All Employees");
        System.out.println("(2)  Change Employees Permissions");
        System.out.println("(3)  ...");
        System.out.println("\n(4)  Exit The System");
        System.out.println();

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            showAllEmployees();
        }else if(choice.equals("2")){
            changeEmployeesPermissions();
        }else if(choice.equals("3")){
            // TODO if needed
        } else if (choice.equals("4")) {
            System.out.println("\nExiting The System, Goodbye.\n");
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
            systemAdminMainMenu();
        }
    }


    private void showAllEmployees() {
        //TODO
    }

    private void changeEmployeesPermissions() {
        //TODO
    }


}
