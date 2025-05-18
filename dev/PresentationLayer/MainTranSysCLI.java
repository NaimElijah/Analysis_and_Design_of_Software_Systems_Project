package PresentationLayer;

import ServiceLayer.*;

import java.util.Scanner;

public class MainTranSysCLI {
    private DriverCLI drCont;
    private TranSysAdCLI sysAdCont;
    private TranManCLI tranManCont;
    private StartUpController startUpCont;
    private Scanner scanner;

    public MainTranSysCLI(TruckService ts, TransportService trs, SiteService sis, StartUpStateService starUpStService) {
        this.scanner = new Scanner(System.in);
//        this.drCont = new DriverCLI(this, trs, es, this.scanner);
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
            System.out.println("\n    --------    ID Authentication Screen    -------    (Secure Login)");
            System.out.println("Enter Your ID:");
            int loginID = scanner.nextInt();
            scanner.nextLine(); // consume the leftover newline

//            int permission = this.startUpCont.getEmployeePermissionsRank(loginID);
//
//            /// For each Permissions Rank there is a Different Options Menu
//            if(permission == 0){
//                System.out.println("\n   --------    Welcome, System Administrator.    -------\n");  // welcome message upon login
//                sysAdCont.systemAdminMainMenu();
//
//            } else if(permission == 1){
//                System.out.println("\n   --------    Welcome, Transport Manager    -------\n");  // welcome message upon login
//                tranManCont.transportManagerMainMenu();
//
//            } else if(permission == 2){
//                System.out.println("\n   --------    Welcome, Transport Driver.    -------\n");  // welcome message upon login
//                drCont.driverMainMenu(loginID);
//            } else {   // returns -1
//                System.out.println("ID not in the System, Access Denied.\n");
//            }

        } else if (choice.equals("2")){
            System.out.println("\nExiting The System, Goodbye.\n");
            System.exit(0);  // exiting the Transport Module Program

        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
        }
        idAuthAccess();

    }



}
