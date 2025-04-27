package PresentationLayer;

import ServiceLayer.*;

import java.util.Scanner;

public class MainTranSysController {
    private DriController drCont;
    private SysAdController sysAdCont;
    private TranManController tranManCont;
    private StartUpController startUpCont;
    private Scanner scanner;
    private int currLoggedID;

    public MainTranSysController(TruckService ts, TransportService trs, SiteService sis, EmployeeService es, StartUpStateService starUpStService) {
        this.scanner = new Scanner(System.in);
        this.drCont = new DriController(trs, es, this.scanner);
        this.sysAdCont = new SysAdController(trs, es, this.scanner);
        this.tranManCont = new TranManController(ts, trs, sis, es, this.scanner);
        this.startUpCont = new StartUpController(starUpStService, this.scanner);
        this.currLoggedID = -1;
    }

    public void transportModuleStartup(){
        startUpCont.startUpData();
        MainMenu();
    }

    private boolean idAuth(int tryingTologInAs) {
        System.out.println("    --------    ID Authentication Screen    -------    (Secure Login)");
        System.out.println("  Enter Your ID:");
        this.currLoggedID = scanner.nextInt();

        int permission = this.drCont.getEmployeePermissionsRank(this.currLoggedID);
        if (permission == tryingTologInAs){  // System Admin
            return true;
        } else {
            System.out.println("ID not in the System for that Role.\n");
            return false;
        }
    }

    void MainMenu(){
        System.out.println("         --------    Main Program Menu    -------");
        System.out.println("             Welcome to the Transport System !");
        System.out.println("   What would you like to login as ?\n");
        System.out.println("(1)  System Admin");
        System.out.println("(2)  Transport Manager");
        System.out.println("(3)  Transport Driver");
        System.out.println("(4)  Exit The System");
        System.out.println();

        String choice = scanner.nextLine();

        if(choice.equals("1")){
            if (idAuth(0)){ sysAdCont.systemAdminMainMenu(); }
            MainMenu();
        }else if(choice.equals("2")){
            if (idAuth(1)){ tranManCont.transportManagerMainMenu(); }
            MainMenu();
        }else if(choice.equals("3")){
            if (idAuth(2)){ drCont.driverMainMenu(this.currLoggedID); }
            MainMenu();
        } else if (choice.equals("4")) {
            System.out.println("\nExiting The System, Goodbye.\n");
            System.exit(0);  // exit with code 0
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
            MainMenu();
        }

    }


}
