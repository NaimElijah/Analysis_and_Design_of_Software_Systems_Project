package PresentationLayer;

import ServiceLayer.TranEmployeeService;

import java.util.Scanner;

public class SysAdController {
    private MainTranSysController main;
    private TranEmployeeService emp_s;
    private Scanner scanner;

    public SysAdController(MainTranSysController m, TranEmployeeService es, Scanner sc) {
        this.main = m;
        this.emp_s = es;
        this.scanner = sc;
    }


    void systemAdminMainMenu(){
        System.out.println("\n           --------    System Admin Menu    -------");
        System.out.println("(1)  View All Employees");
        System.out.println("(2)  View All Managers");
        System.out.println("(3)  View All Drivers");
        System.out.println("(4)  Add a New Manager");
        System.out.println("(5)  Remove an Employee");
        System.out.println("(6)  Give a Driver a Manager's Permission Rank (Rank Promotion)");
        System.out.println("(7)  Disconnect");
        System.out.println();
        System.out.println("   Select Action:\n");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            viewAllEmployees();
        }else if(choice.equals("2")){
            viewAllManagers();
        }else if(choice.equals("3")){
            viewAllDrivers();


        } else if (choice.equals("4")) {
            System.out.println("Let's add a New Manager.");
            System.out.println("Enter New Manager's Employee ID:");
            int empId = -2;
            try {
                empId = scanner.nextInt();
            } catch (Exception e) {}
            scanner.nextLine(); // consume the leftover newline
            System.out.println("Enter New Manager's First Name:");
            String fname = scanner.nextLine();
            System.out.println("Enter New Manager's Last Name:");
            String lname = scanner.nextLine();

            String res = this.emp_s.addManager(empId, fname, lname);
            if(res.equals("Success")){
                System.out.println("Successfully Added new Manager.\n");
            } else if(res.equals("Exception")){
                System.out.println("Failed to Add new Manager due to technical machine error.\n");
            }else { System.out.println(res + "\n"); }




        } else if (choice.equals("5")) {
            System.out.println("Let's remove an Employee.");
            System.out.println("Enter Employee ID of the Employee you want to remove:");
            int empId = -2;
            try {
                empId = scanner.nextInt();
            } catch (Exception e) {}
            scanner.nextLine(); // consume the leftover newline

            String res = this.emp_s.removeEmployeeByAdmin(empId);
            if(res.equals("Success")){
                System.out.println("Successfully Removed Employee.\n");
            } else if(res.equals("Exception")){
                System.out.println("Failed to Remove Employee due to technical machine error.\n");
            }else { System.out.println(res + "\n"); }



        } else if (choice.equals("6")) {
            System.out.println("Enter the Driver's ID:");
            int driverId = scanner.nextInt();
            scanner.nextLine(); // consume the leftover newline

            String res = this.emp_s.giveADriverAManagersPermissionRank(driverId);
            if(res.equals("Success")){
                System.out.println("Successfully Gave the Driver a Manager's Permission Rank.\n");
            } else if(res.equals("Exception")){
                System.out.println("Failed to Give the Driver a Manager's Permission Rank due to technical machine error.\n");
            }else { System.out.println(res + "\n"); }


        } else if (choice.equals("7")) {
            System.out.println("\nGoing Back to Main Program Authentication Screen.\n");
            this.main.idAuthAccess();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
        }
        systemAdminMainMenu();
    }



    private void viewAllEmployees(){
        System.out.println("   --------    Showing All Employees    --------\n");
        System.out.println(this.emp_s.showEmployees());
        System.out.println();
    }

    private void viewAllManagers(){
        System.out.println("   --------    Showing All Managers    --------\n");
        System.out.println(this.emp_s.showManagers());
        System.out.println();
    }

    private void viewAllDrivers(){
        System.out.println("   --------    Showing All Drivers    --------\n");
        System.out.println(this.emp_s.showDrivers());
        System.out.println();
    }






}
