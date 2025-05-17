import DomainLayer.*;
import DomainLayer.EmpSubModule.EmployeeFacade;
import DomainLayer.SiteSubModule.SiteFacade;
import DomainLayer.TranSubModule.TransportFacade;
import DomainLayer.TruSubModule.TruckFacade;
import PresentationLayer.MainTranSysController;
import PresentationLayer.MainCLI;
import ServiceLayer.EmployeeService;
import ServiceLayer.ShiftService;
import java.io.IOException;
import java.util.Scanner;
import ServiceLayer.*;

public class Main {
   public static void main(String[] args) {

      TruckFacade tru_f = new TruckFacade();
      EmployeeFacade eff = new EmployeeFacade();
      SiteFacade sf = new SiteFacade();
      TransportFacade tran_f = new TransportFacade(eff, sf, tru_f);

      TransportService tran_s = new TransportService(tran_f);
      TruckService tru_s = new TruckService(tru_f);
      TranEmployeeService es = new TranEmployeeService(eff);
      SiteService site_s = new SiteService(sf);

      StartUpStateService start = new StartUpStateService(tran_s, tru_s, es, site_s);

      MainTranSysController mtsc = new MainTranSysController(tru_s, tran_s, site_s, es, start);

      mtsc.transportModuleStartup();      ///         <<<-----------------------   starts the whole Transport Module System





       /// ////////    <<<--------------------------------------   from here it's the HR's code      <<<--------------------     <<<------------------------------

       System.out.println("Initializing Employee Module System...");

       // (1) INITIAL DATA LOADING
       AuthorisationController authController;
       EmployeeController employeeController;
       ShiftService shiftService = null;
       AssignmentController assignmentController;
       AvailabilityController availabilityController;
       EmployeeService employeeService;

       Scanner scanner = new Scanner(System.in);
       boolean minimalMode = true;
       System.out.print("Do you want to load data? (y/n) ==> ");
       if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
           minimalMode = false;
           System.out.println("System will load from data files.");
       } else {
           System.out.println("System will load with only Admin user.");
       }

       DataInitializer.SystemInitData initData;
       try {
           DataInitializer initializer = new DataInitializer("./data");
           initData = minimalMode
                   ? initializer.initializeMinimal()
                   : initializer.initializeFromFiles();
           System.out.println("System initialized " +
                   (minimalMode ? "with minimal data." : "from data files."));
       } catch (IOException e) {
           System.err.println("Error initializing system: " + e.getMessage());
           return;
       }

       authController       = initData.getAuthController();
       employeeController   = initData.getEmployeeController();
       ShiftController shc   = initData.getShiftController();
       assignmentController = new AssignmentController(employeeController);
       availabilityController = new AvailabilityController(employeeController);
       employeeService      = new EmployeeService(employeeController, authController);
       shiftService         = new ShiftService(shc, assignmentController, availabilityController);

       // (2) APP LOOP
       boolean appRunning = true;
       while (appRunning) {
           //  LOGIN
           long loginId = 0;
           boolean validLogin = false;

           do {
               if (minimalMode) {
                   System.out.println("Starting with Admin user (only available user in minimal mode)");
                   loginId = 123456789L;
                   validLogin = true;
               } else {
                   System.out.println("\nPick an employee to start the CLI with:");
                   System.out.println("  0. Admin");
                   System.out.println("  1. Shira Steinbuch");
                   System.out.println("  2. Ramzi Abd Rabo");
                   System.out.println("  3. Kochava Shavit");
                   System.out.print("Enter your choice: ");
                   String choice = scanner.nextLine().trim();

                   switch (choice) {
                       case "0": loginId = 123456789L; validLogin = true; break;
                       case "1": loginId = 111111111L; validLogin = true; break;
                       case "2": loginId = 222222222L; validLogin = true; break;
                       case "3": loginId = 333333333L; validLogin = true; break;
                       default:
                           System.out.println("Invalid choice. Please try again.");
                   }
               }
           } while (!validLogin);

           // ----- START THE CLI -----
           MainCLI mainCLI = new MainCLI(employeeService, shiftService, loginId);
           mainCLI.start();

           // ----- AFTER USER EXITS MAIN MENU -----
           System.out.println();
           System.out.print("Do you want to (1) Log in as a different user or (2) Exit the application? ");
           String again = scanner.nextLine().trim();
           if (!again.equals("1")) {
               appRunning = false;
               System.out.println("Shutting down. Goodbye!");
           }
       }

   }
}
