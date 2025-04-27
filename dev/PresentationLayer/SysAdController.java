package PresentationLayer;

import ServiceLayer.EmployeeService;
import ServiceLayer.TransportService;

import java.util.Scanner;

public class SysAdController {
    private TransportService tra_s;
    private EmployeeService emp_s;
    private Scanner scanner;

    public SysAdController(TransportService ts, EmployeeService es, Scanner sc) {
        this.tra_s = ts;
        this.emp_s = es;
        this.scanner = sc;
    }


    void systemAdminMainMenu(){
        System.out.println("   --------    Welcome, System Administrator.    -------\n");
        System.out.println("        --------    System Admin Menu    -------");
        System.out.println("(1)  Show All Employees");
        System.out.println("(2)  Edit Employees Permissions");
        System.out.println("(3)  ");
        System.out.println("(4)  Disconnect and Go Back to Main Program Menu");
        System.out.println();
        System.out.println("   Select Action:\n");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            showAllEmployees();
            systemAdminMainMenu();
        }else if(choice.equals("2")){
            changeEmployeesPermissions();
            systemAdminMainMenu();
        }else if(choice.equals("3")){
            // TODO if needed
        } else if (choice.equals("4")) {
            System.out.println("\nGoing Back to Main Program Menu.\n");
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
