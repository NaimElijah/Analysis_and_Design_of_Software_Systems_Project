package ServiceLayer;

import DTOs.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class StartUpStateService {
    private TransportService tranSer;
    private TruckService truckSer;
    private EmployeeService empSer;
    private SiteService siteSer;
    private ObjectMapper objectMapper;

    public StartUpStateService(TransportService tranSer, TruckService truckSer, EmployeeService empSer, SiteService siteSer) {
        this.tranSer = tranSer;
        this.truckSer = truckSer;
        this.empSer = empSer;
        this.siteSer = siteSer;
        this.objectMapper = new ObjectMapper();
    }

    ///      This Service is Servicing us by just creating the starting data we put here for it to Create as Initial System Data.    <<-----------------------
    public String loadData() {
        try {
            // shipping area:
            this.siteSer.addShippingArea(1, "Central District");
            this.siteSer.addShippingArea(2, "South District");

            // sites:
            this.siteSer.addSite(1, "Ramla", "Bob Hanely", 524561234);
            this.siteSer.addSite(1, "Rishonim", "Jack Graham", 534872456);
            this.siteSer.addSite(2, "Ashkelon", "Alice Green", 553671958);
            this.siteSer.addSite(2, "Dimona", "Daniel Greenberg", 586457912);


            // trucks:
            this.truckSer.addTruck(1010, "Toyota K8", 1200, 100, "D");
            this.truckSer.addTruck(2020, "Hyundai R6", 1000, 100, "C");
            this.truckSer.addTruck(3030, "Yamaha Lite", 200, 20, "A");


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
            this.empSer.addDriver(666, "Max", "Turner", maxLicenses);

            // Transport
            ArrayList<ItemsDocDTO> itemsDocDTOs = new ArrayList<>();

            ArrayList<ItemQuantityDTO> itemQuantityDTOs1 = new ArrayList<>();
            itemQuantityDTOs1.add(new ItemQuantityDTO(new ItemDTO("Water", 0.5, true), 5));
            itemQuantityDTOs1.add(new ItemQuantityDTO(new ItemDTO("Rice", 1, true), 10));
            ItemsDocDTO itemsDocDTO1 = new ItemsDocDTO(1, new SiteDTO(1,"Ramla"), new SiteDTO(1, "Rishonim"), itemQuantityDTOs1);

            ArrayList<ItemQuantityDTO> itemQuantityDTOs2 = new ArrayList<>();
            itemQuantityDTOs2.add(new ItemQuantityDTO(new ItemDTO("Shampoo", 0.75, true), 10));
            itemQuantityDTOs2.add(new ItemQuantityDTO(new ItemDTO("Toothpaste", 0.2, true), 15));
            ItemsDocDTO itemsDocDTO2 = new ItemsDocDTO(2, new SiteDTO(1,"Ramla"), new SiteDTO(2, "Dimona"), itemQuantityDTOs2);

            itemsDocDTOs.add(itemsDocDTO1);
            itemsDocDTOs.add(itemsDocDTO2);

            TransportDTO transportDTO = new TransportDTO(-99, 1010, 555, new SiteDTO(1,"Ramla"), itemsDocDTOs);
            this.tranSer.createTransport(objectMapper.writeValueAsString(transportDTO), -100);

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
        return "SUCCESS";
    }


    public int getEmployeePermissionsRank(int loginIDGiven) {
        return empSer.getEmployeePermissionsRank(loginIDGiven);
    }



}
