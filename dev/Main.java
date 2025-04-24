import DomainLayer.AssignmentController;
import DomainLayer.AuthorisationController;
import DomainLayer.DataInitializer;
import DomainLayer.Employee;
import DomainLayer.EmployeeController;
import DomainLayer.ShiftController;
import PresentationLayer.AssignmentCLI;
import PresentationLayer.AvailabillityCLI;
import PresentationLayer.EmployeeCLI;
import PresentationLayer.MainCLI;
import ServiceLayer.EmployeeService;
import ServiceLayer.ShiftService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        System.out.println("Initializing Employee Module System...");

        AuthorisationController authController;
        EmployeeController employeeController;
        EmployeeService employeeService;
        ShiftService shiftService = null;
        AssignmentController assignmentController;

        boolean minimalMode = true;
        System.out.print("Do you want to load data? (y/n) ==> ");
        String input = System.console().readLine();
        if (input.toLowerCase().equals("y")) {
            minimalMode = false;
            System.out.println("System will load with out data. only with Admin user.");
        }


        try {
            DataInitializer initializer = new DataInitializer("./data");
            DataInitializer.SystemInitData initData;

            if (minimalMode) {
                initData = initializer.initializeMinimal();
                System.out.println("System initialized with minimal data successfully.");
            } else {
                initData = initializer.initializeFromFiles();
                System.out.println("System initialized from data files successfully.");
            }

            authController = initData.getAuthController();
            employeeController = initData.getEmployeeController();
            ShiftController shiftController = initData.getShiftController();
            assignmentController = new AssignmentController(employeeController);

            employeeService = new EmployeeService(employeeController, authController);
            shiftService = new ShiftService(shiftController, assignmentController, null);
        } catch (IOException e) {
            System.err.println("Error initializing system: " + e.getMessage());
            System.err.println("Cannot start the system. Please check data files and try again.");
            return;
        }

        long loginId = 0;

        if (minimalMode) {
            System.out.println("Starting with Admin user (only available user in minimal mode)");
            loginId = 123456789;
        } else {
            System.out.println("Pick an employee to start the CLI with: ");
            System.out.println("0. Admin");
            System.out.println("1. Shira Steinbuch");
            System.out.println("2. Ramzi Abd Rabo");
            System.out.println("3. Kochava Shavit");
            System.out.print("Enter your choice: ");

            String choice = System.console().readLine();
            switch (choice) {
                case "0":
                    loginId = 123456789;
                    break;
                case "1":
                    loginId = 111111111;
                    break;
                case "2":
                    loginId = 222222222;
                    break;
                case "3":
                    loginId = 333333333;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    return;
            }
        }

        // Start the main CLI
        MainCLI mainCLI = new MainCLI(employeeService, shiftService, loginId);
        mainCLI.start();
    }

}
