package ServiceLayer;

import java.util.ArrayList;
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

    ///      This Service is Servicing us by just creating the starting data we put here for it to Create as Initial System Data.    <<-----------------------
    public String loadData() {
        try {
            // shipping area:
            this.siteSer.addShippingArea(1, "Central District");
            this.siteSer.addShippingArea(2, "South District");

            // sites:
            this.siteSer.addSite(1, "Rose St. 4, Tel Aviv", "Bob Hanely", 524561234);
            this.siteSer.addSite(1, "Hadas St. 29, Rishonim", "Jack Graham", 534872456);
            this.siteSer.addSite(2, "Rager St. 10, Beer Sheva", "Alice Green", 553671958);
            this.siteSer.addSite(2, "Moshe Sharet St. 7, Dimona", "Daniel Greenberg", 586457912);


            // trucks:
            this.truckSer.addTruck(1010, "Toyota K8", 1200, 100, "D");
            this.truckSer.addTruck(2020, "Hyundai R6", 1000, 100, "C");
            this.truckSer.addTruck(3030, "Yamaha Lite", 100, 10, "A");


            //  Admin, Managers, Drivers:
            this.empSer.initializeAdmin(111, "Cody", "Weber");
            this.empSer.addManager(222, "Naim", "Elijah");
            this.empSer.addManager(333, "Bar", "Miyara");

            ArrayList<String> tomsLicenses = new ArrayList<>();
            tomsLicenses.add("A");
            tomsLicenses.add("C");
            tomsLicenses.add("E");
            ArrayList<String> xaviersLicenses = new ArrayList<>();
            xaviersLicenses.add("B");
            xaviersLicenses.add("D");
            xaviersLicenses.add("E");
            ArrayList<String> maxLicenses = new ArrayList<>();
            maxLicenses.add("A");
            maxLicenses.add("B");
            maxLicenses.add("C");
            maxLicenses.add("D");
            maxLicenses.add("E");
            this.empSer.addDriver(444, "Tom", "Hat", tomsLicenses);
            this.empSer.addDriver(555, "Xavier", "Hernandez", xaviersLicenses);
            this.empSer.addDriver(444, "Max", "Turner", maxLicenses);


        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
        return "SUCCESS";
    }



}
