package ServiceLayer;

import java.util.Scanner;

public class StartUpStateService {
    private TransportService tranSer;
    private TruckService truckSer;
    private EmployeeService empSer;
    private SiteService siteSer;

    public StartUpStateService(TransportService tranSer, TruckService truckSer, EmployeeService empSer, SiteService siteSer) {
        this.tranSer = tranSer;
        this.truckSer = truckSer;
        this.empSer = empSer;
        this.siteSer = siteSer;
    }

    public String loadData() {
        try {
            //TODO:  loading starting data with the other services services:
            //TODO: add shipping area:
            this.siteSer.addShippingArea(1, "Central District");

            //TODO: add sites:


            //TODO: add trucks:


            //TODO: add employees, drivers:


            //TODO: add some Transports


            //TODO:  loading starting data with the other services services
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
        return "SUCCESS";
    }



}
