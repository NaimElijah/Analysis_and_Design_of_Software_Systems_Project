package PresentationLayer;

import ServiceLayer.*;

import java.util.Scanner;

public class MainTranSysController {
    DriController drCont;
    SysAdController sysAdCont;
    TranManController tranManCont;
    StartUpController startUpCont;
    private Scanner scanner;

    public MainTranSysController(TruckService ts, TransportService trs, SiteService sis, EmployeeService es, StartUpStateService starUpStService) {
        this.scanner = new Scanner(System.in);
        this.drCont = new DriController(trs, es, this.scanner);
        this.sysAdCont = new SysAdController(trs, es, this.scanner);
        this.tranManCont = new TranManController(ts, trs, sis, es, this.scanner);
        this.startUpCont = new StartUpController(starUpStService, this.scanner);
    }

    public void transportModuleStartup(){
        startUpCont.startUpDataMenu();
        MainMenu();
    }

    void MainMenu(){
        System.out.println("         --------    Main Menu    -------");
        System.out.println("   Welcome to the Transport System.");
        System.out.println("   What would you like to login as ?\n");
        System.out.println("(1)  System Admin");
        System.out.println("(2)  Transport Manager");
        System.out.println("(3)  Transport Driver");
        System.out.println("\n(4)  Exit The System");
        System.out.println();

        String choice = scanner.nextLine();

        if(choice.equals("1")){
            sysAdCont.systemAdminMainMenu();
        }else if(choice.equals("2")){
            tranManCont.transportManagerMainMenu();
        }else if(choice.equals("3")){
            drCont.driverMainMenu();
        } else if (choice.equals("4")) {
            System.out.println("\nExiting The System, Goodbye.\n");
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
            MainMenu();
        }

    }


}
