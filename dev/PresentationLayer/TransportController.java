package PresentationLayer;

import ServiceLayer.AdminService;
import ServiceLayer.ManagerService;
import ServiceLayer.SystemService;

public class TransportController {
    private SystemService sys;
    private AdminService as;
    private ManagerService ms;

    public TransportController(SystemService sys, AdminService as, ManagerService ms) {
        this.sys = sys;
        this.as = as;
        this.ms = ms;
    }



    void transportModuleStartup(){
        //TODO
    }


    void MainMenu(){
        //TODO
    }

    //TODO more menus
}
