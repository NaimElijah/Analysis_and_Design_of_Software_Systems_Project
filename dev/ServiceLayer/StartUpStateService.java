package ServiceLayer;

import DTOs.ItemDTO;
import DTOs.ItemsDocDTO;
import DTOs.SiteDTO;
import DTOs.TransportDTO;
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

            // Transport
            ArrayList<ItemsDocDTO> itemsDocDTOS = new ArrayList<>();

            HashMap< ItemDTO, Integer> itemDTOs1 = new HashMap<>();
            itemDTOs1.put(new ItemDTO("Water", 0.5, true), 5);
            itemDTOs1.put(new ItemDTO("Rice", 1, true), 10);
            ItemsDocDTO itemsDocDTO1 = new ItemsDocDTO(1, new SiteDTO(1,"Tel Aviv"), new SiteDTO(1, "Rishonim"), itemDTOs1);
            HashMap< ItemDTO, Integer> itemDTOs2 = new HashMap<>();
            itemDTOs2.put(new ItemDTO("Shampoo", 0.75, true), 10);
            itemDTOs2.put(new ItemDTO("Toothpaste", 0.2, true), 15);
            ItemsDocDTO itemsDocDTO2 = new ItemsDocDTO(2, new SiteDTO(1,"Tel Aviv"), new SiteDTO(2, "Dimona"), itemDTOs2);

            itemsDocDTOS.add(itemsDocDTO1);
            itemsDocDTOS.add(itemsDocDTO2);

            TransportDTO transportDTO = new TransportDTO(1010, 555, new SiteDTO(1,"Tel Aviv"), itemsDocDTOS);
            this.tranSer.createTransport(objectMapper.writeValueAsString(transportDTO), -100);

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
        return "SUCCESS";
    }



}
