package PresentationLayer.TransportPresentation;

import ServiceLayer.*;
import ServiceLayer.TransportServices.SiteService;
import ServiceLayer.TransportServices.StartUpStateService;
import ServiceLayer.TransportServices.TransportService;
import ServiceLayer.TransportServices.TruckService;

import java.util.Scanner;

public class MainTranSysCLI {
    private TranDriverCLI drCont;
    private TranManCLI tranManCont;
    private StartUpController startUpCont;
    private EmployeeIntegrationService employeeIntegrationService;
    private Scanner scanner;

    public MainTranSysCLI(TruckService ts, TransportService trs, SiteService sis, EmployeeIntegrationService eis, StartUpStateService starUpStService) {
        this.scanner = new Scanner(System.in);
//        this.drCont = new TranDriverCLI(this, trs, es, this.scanner);
//        this.sysAdCont = new TranSysAdCLI(this, es, this.scanner);
//        this.tranManCont = new TranManCLI(this, ts, trs, sis, es, this.scanner);
//        this.startUpCont = new StartUpController(starUpStService, es);
    }

    public void transportModuleStartup(){
        startUpCont.startUpData();
        idAuthAccess();
    }

    void idAuthAccess(){
        System.out.println("              Welcome to the Transport System !\n");
        System.out.println("What would you like to do ?\n");
        System.out.println("(1)  Log In");
        System.out.println("(2)  Exit The System");
        System.out.println("Enter your choice : ");
        String choice = scanner.nextLine();

        if(choice.equals("1")){
            System.out.println("\n    --------    ID Authentication Screen    -------    (Secure Login)");  //TODO:  give to all functions below the ID of the user using the system.
            System.out.println("Enter Your ID:");  //TODO:  give to all functions below the ID of the user using the system.      <<<------------------
            long loginID = scanner.nextLong();
            scanner.nextLine(); // consume the leftover newline

            boolean isDriver = this.employeeIntegrationService.hasRole(loginID, "DriverA") || this.employeeIntegrationService.hasRole(loginID, "DriverB") || this.employeeIntegrationService.hasRole(loginID, "DriverC") || this.employeeIntegrationService.hasRole(loginID, "DriverD") || this.employeeIntegrationService.hasRole(loginID, "DriverE");

            if (!this.employeeIntegrationService.isActive(loginID)){   //  if not in the System
                System.out.println("ID not in the System, Access Denied.\n");
            } else if (this.employeeIntegrationService.hasRole(loginID, "Transport manager")) {
                System.out.println("\n   --------    Welcome, Transport Manager    -------\n");  // welcome message upon login
                tranManCont.transportManagerMainMenu(loginID);
            } else if (isDriver) {
                System.out.println("\n   --------    Welcome, Transport Driver.    -------\n");  // welcome message upon login
                drCont.driverMainMenu(loginID);
            }

        } else if (choice.equals("2")){
            System.out.println("\nExiting The System, Goodbye.\n");
            System.exit(0);  // exiting the Transport Module Program

        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
        }
        idAuthAccess();

    }



}
