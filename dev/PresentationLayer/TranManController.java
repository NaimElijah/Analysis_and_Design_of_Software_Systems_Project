package PresentationLayer;

import PresentationLayer.DTOs.ItemDTO;
import PresentationLayer.DTOs.ItemsDocDTO;
import PresentationLayer.DTOs.SiteDTO;
import PresentationLayer.DTOs.TransportDTO;
import ServiceLayer.EmployeeService;
import ServiceLayer.SiteService;
import ServiceLayer.TransportService;
import ServiceLayer.TruckService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TranManController {
    private TruckService tru_ser;
    private TransportService tra_ser;
    private SiteService site_ser;
    private EmployeeService emp_ser;
    private Scanner scanner;
    private ObjectMapper objectMapper;

    public TranManController(TruckService ts, TransportService trs, SiteService sis, EmployeeService es, Scanner sc) {
        this.tru_ser = ts;
        this.tra_ser = trs;
        this.site_ser = sis;
        this.emp_ser = es;
        this.scanner = sc;
        this.objectMapper = new ObjectMapper();
    }

    void transportManagerMainMenu(){   ////////////////////////////////   Main Menu   <<<--------------------------------------------
        System.out.println("   --------    Transport Manager Menu    -------\n");
        System.out.println("(1)  Transports Options Menu");
        System.out.println("(2)  Shipping Areas Options Menu");
        System.out.println("(3)  Sites Options Menu");
        System.out.println("(4)  Drivers Options Menu");
        System.out.println("(5)  Trucks Options Menu");
        System.out.println("(6)  Disconnect and Go Back to Main Program Menu");
        System.out.println();
        System.out.println(" Select Options Menu: ");
        String choice = scanner.nextLine();
        switch (choice){
            case "1":
                transportsOptionsMenu();
                break;
            case "2":
                shippingAreasOptionsMenu();
                break;
            case "3":
                sitesOptionsMenu();
                break;
            case "4":
                driversOptionsMenu();
                break;
            case "5":
                trucksOptionsMenu();
                break;
            case "6":
                System.out.println("\nGoing Back to Main Program Menu.\n");
                break;
            default:
                System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
                transportManagerMainMenu();
                break;
        }
    }













    private void transportsOptionsMenu(){   ////////////////////////////////   Transports Menu   <<<--------------------------------------------
        System.out.println("   --------    Transports Options Menu    -------\n");
        System.out.println("(1)  View All Transports");
        System.out.println("(2)  Create a Transport");
        System.out.println("(3)  Check if a Queued Transport Can Be Sent");
        System.out.println("(4)  Delete a Transport");
        System.out.println("(5)  Edit a Transport");
        System.out.println("(6)  Back to Transport Manager Main Menu");
        System.out.println();
        System.out.println(" Select Option: ");
        String choice = scanner.nextLine();
        if(choice.equals("1")){
            showAllTransports();
        }else if(choice.equals("2")){
            createaTransportMenu();
        }else if(choice.equals("3")){
            checkIfQueuedTransportCanBeSent();
        } else if (choice.equals("4")) {
            deleteaTransportMenu();
        } else if (choice.equals("5")) {
            editaTransportMenu();
        } else if (choice.equals("6")) {
            System.out.println("\n\n");
            transportManagerMainMenu();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
            transportsOptionsMenu();
        }
    }

    //////////////////////          HELPER FUNCTIONS FOR THE Transports Options Menu

    private void showAllTransports(){
        System.out.println("   --------    Showing All Transports    --------\n");
        System.out.println(tra_ser.showAllTransports());
        System.out.println();
        transportManagerMainMenu();
    }





    private void createaTransportMenu() {

        System.out.println("   --------    Transport Creation    --------\n");
        System.out.println("Ok, let's start creating your new Transport :)");
        System.out.println("\n- Enter the following information:");
        System.out.println("Enter Source Area Number: ");
        int sourceAreaNum = scanner.nextInt();
        System.out.println("Enter Source Address String: ");
        String sourceAddressString = scanner.next();
        SiteDTO srcSitedto = new SiteDTO(sourceAreaNum, sourceAddressString);
        System.out.println("Enter Desired Truck Number: ");
        int truckNum = scanner.nextInt();
        System.out.println("Enter Desired Driver ID: ");
        int driverID = scanner.nextInt();

        ArrayList<ItemsDocDTO> dests_Docs_for_Transport = new ArrayList<ItemsDocDTO>();  //  for the Transport's field

        System.out.println("We'll now add the Sites(and the items from each site).");
        System.out.println("\nThe Transport's Site Arrival Order will be based on the order of Sites you add (first added -> first arrived to)");
        System.out.println("\n- Now Enter Each Site and for Each Site enter the Items Desired from there: ");

        boolean continueAnotherSite = true, continueAnotherItem = true, continueAskingDifferentAreaNum = true, siteExists = false;
        Integer currSiteAreaNum = -99;
        String currDestinationAddress = "";
        ArrayList<Integer> areasNumsUptoNow = new ArrayList<Integer>();  //  for the area numbers in this Transport


        while (continueAnotherSite){   ///   Sites WHILE(TRUE) LOOP

            siteExists = false;
            while (!siteExists){
                while (continueAskingDifferentAreaNum){
                    System.out.println("Enter Destination Site Area Number: ");
                    currSiteAreaNum = scanner.nextInt();
                    if(!areasNumsUptoNow.contains(currSiteAreaNum)){
                        if(areasNumsUptoNow.isEmpty()){   //  if first site
                            areasNumsUptoNow.add(currSiteAreaNum);
                            continueAskingDifferentAreaNum = false;
                            continue;  // will break
                        }
                        System.out.println("The destination's area number is not within the area numbers in this Transport's destinations, continue with it ? ( Enter 'Y' / 'N'(or any other key) )");
                        String ifAnotherSiteChoice = scanner.nextLine();
                        if(!ifAnotherSiteChoice.equals("Y")){
                            continue;  ///  continue this Loop
                        }
                        areasNumsUptoNow.add(currSiteAreaNum);
                        continueAskingDifferentAreaNum = false;  ///  breaks from this Loop
//                    break;
                        // and then a choice of continuing adding this dest_site or not (because it's in another shipping area)
                    }else {
                        continueAskingDifferentAreaNum = false;  ///  breaks from this Loop
                    }
                }

                System.out.println("Enter Destination Site Address String: ");
                currDestinationAddress = scanner.next();

                siteExists = this.site_ser.doesSiteExist(currSiteAreaNum, currDestinationAddress);
                if (!siteExists){
                    areasNumsUptoNow.remove(currSiteAreaNum);
                    System.out.println("Site Doesn't Exist, please choose a site that actually exists.\n");
                }
            }


            SiteDTO destSitedto = new SiteDTO(currSiteAreaNum, currDestinationAddress);

            System.out.println("\n- Now let's add the Items you want to get from this destination Site back to the Source Site:\n");

            HashMap<ItemDTO, Integer> itemsListFromCurrDestSite = new HashMap<ItemDTO, Integer>();  // for the ItemsDoc's field


            System.out.println("Enter Unique Items Document Number: ");
            int currItemsDocNum = scanner.nextInt();
            if(!tra_ser.checkValidItemsDocID(currItemsDocNum)){
                System.out.println("Please Enter a *Unique* and Valid Items Document Number: ");
                currItemsDocNum = scanner.nextInt();
            }
            System.out.println("Valid Items Document Number :)\n");

            System.out.println("Now let's add the Items you want from this destination Site, one by one:");
            while (continueAnotherItem){   ///   site's Items WHILE(TRUE) LOOP
                System.out.println("Enter Item Name: ");
                String itemName = scanner.next();
                System.out.println("Enter Item Weight: ");
                int itemWeight = scanner.nextInt();
                System.out.println("Enter Item Amount: ");
                int itemAmount = scanner.nextInt();
                System.out.println("Enter these Items Condition: ( Enter 'Good' / 'Bad'(or any other key) )");
                String condition = scanner.next();

                ItemDTO itemAddition = new ItemDTO(itemName, itemWeight, condition.equals("Good"));

                if(itemsListFromCurrDestSite.containsKey(itemAddition)){  // so items numbers won't get overridden if we add the same Item.
                    itemsListFromCurrDestSite.put(itemAddition, itemsListFromCurrDestSite.get(itemAddition) + itemAmount);  ///  adding new Item to the items list
                }else {
                    itemsListFromCurrDestSite.put(itemAddition, itemAmount);  ///  adding new Item to the items list
                }

                System.out.println("Item Added to listed items associated with current Site, do you want to add another Item ? ( Enter 'Y' / 'N'(or any other key) )");
                String ifAnotherItemChoice = scanner.nextLine();
                if(!ifAnotherItemChoice.equals("Y")){ continueAnotherItem = false; }  ///  breaks from this Item Addition Loop
            }





            ItemsDocDTO itemsDocAddition = new ItemsDocDTO(currItemsDocNum, srcSitedto, destSitedto, itemsListFromCurrDestSite);
            System.out.println("Ok, Finished adding the current destination Site's items");
            dests_Docs_for_Transport.add(itemsDocAddition);   //  adding new ItemsDoc to the destSitesDocs

            System.out.println("Items Wanted from that Site added.\nDo you want to Add another Site and it's Items ? ( Enter 'Y' / 'N'(or any other key) )");
            String choice = scanner.nextLine();
            if(!choice.equals("Y")){
                continueAnotherSite = false;  ///  breaks from this Site Addition Loop
            }
        }




        System.out.println("Ok, Finished adding the Sites & Items to the Transport");

        // And create the DTO object (The Package to send downwards):
        TransportDTO transportDTO = new TransportDTO(truckNum, driverID, srcSitedto, dests_Docs_for_Transport);

        ////////////////////////////////////////////////    NOW WE HAVE THE WHOLE TRANSPORT's DTO     <<<-----------------------------------------


        System.out.println("Checking Transport Validity...");


        /// ////////////////////////////////////////////TODO:    NOW WE'LL DO THE CHECKINGS          <<<-----------------------------------------

        //TODO:    the below function now      <<<-----------------------------------------
        String resOfTransportCheck = "";
        try {
            resOfTransportCheck = this.tra_ser.checkTransportValidity(objectMapper.writeValueAsString(transportDTO)); // TODO <<------   HERE RIGHT NOW
            ///  returns: "Valid", "BadLicenses", "overallWeight-truckMaxCarryWeight", "Queue"
        } catch (Exception e) {
            System.out.println("Serialization's fault");
            e.printStackTrace();
        }
        //TODO:    the above function now      <<<-----------------------------------------

        while (!resOfTransportCheck.equals("Valid")){
            if (resOfTransportCheck.equals("Queue")){
                System.out.println("This Transport doesn't have a proper Truck-Driver matching available at all right now, so this Transport will now go automatically into the Queued Transports.");
                System.out.println("We will save the Queued Transport in order of creation, when you want, you can go to (Transport Options Menu)->() ");
                System.out.println("The Queued Transports are being sent when they can really be sent, starting with the first Transport in the Queue.");
                break;
            }

            System.out.println("It seems There is a problem with The Transport you're trying to create: ");
            transportRePlanning(transportDTO, resOfTransportCheck); /// "BadLicenses" & "overallWeight-truckMaxCarryWeight" Cases

            try {
                resOfTransportCheck = this.tra_ser.checkTransportValidity(objectMapper.writeValueAsString(transportDTO));  //  check Transport Validity again
                ///  returns: "Valid", "BadLicenses", "overallWeight-truckMaxCarryWeight", "Queue"
            } catch (Exception e) {
                System.out.println("Serialization's fault");
                e.printStackTrace();
            }
        }


        System.out.println("Okay, Transport is Valid :)");

        String resOfNewTransportAddition = "";
        try {
            resOfNewTransportAddition = this.tra_ser.createTransport(objectMapper.writeValueAsString(transportDTO));  ///  <<------  HERE WE CREATE THE TRANSPORT AFTER THE CHECKS
        } catch (Exception e) {
            System.out.println("Serialization's fault");
            e.printStackTrace();
        }

        if(resOfNewTransportAddition.equals("Success")){
            System.out.println("Successfully Added Transport\n");
        } else if(resOfNewTransportAddition.equals("Exception")){
            System.out.println("Failed to add Transport due to technical machine error\n");
        }else { System.out.println(resOfNewTransportAddition + "\n"); }  // printing error string given from Service Layer

        System.out.println();
        transportsOptionsMenu();
    }











    private void transportRePlanning(TransportDTO transportDTO, String issue){

        if (issue.equals("BadLicenses")){  ///  Truck and Driver aren't compatible
            System.out.println("The problem seems to be that the Driver's Driving License indicates that he cannot Drive the selected Truck for this Transport.");
            //  Note: if we got to here it seems there is a possible pairing in the system (because we didn't automatically go to the "Queue")
            System.out.println("These are the available Trucks and the available Drivers, from them, let's choose a new Truck-Driver pairing for your Transport:\n");
            System.out.println("Available Trucks:\n" + this.tru_ser.showTrucks() + "\n");
            System.out.println("Available Drivers: \n" + this.emp_ser.showDrivers() + "\n");
            System.out.println("Please Enter the New Truck-Driver pairing you want:");
            System.out.println("Enter Truck number:");
            int truckNum = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter Driver ID:");
            int driverID = Integer.parseInt(scanner.nextLine());

            transportDTO.setTransportTruckNum(truckNum);
            transportDTO.setTransportDriverID(driverID);


        } else {   ///  overweight Transport, choose if to remove Items or to change Truck
            System.out.println("The problem seems to be that the Transport's overall weight exceeds the feasible possible weight that can travel on this Truck");

            String[] parts = issue.split("-");
            int overallWeight = Integer.parseInt(parts[0]);
            int truckMaxCarryWeight = Integer.parseInt(parts[1]);

            boolean stillLoopingInReplanningChiceLoop = true;
            while (stillLoopingInReplanningChiceLoop){
                System.out.println("The current overall weight of the truck is: " + overallWeight + ", and the truck's Max. Carry Weight is: " + truckMaxCarryWeight);
                System.out.println("Please Choose an Option from the Possible RePlanning Options below:");
                System.out.println(".1. Remove Items from a specific destination Site in the Transport");
                System.out.println(".2. Remove a Destination Site from the Transport (and all of it's Items)");
                System.out.println(".3. Choose a Different Truck");
                System.out.println(" Enter your choice: ");
                String choice = scanner.nextLine();

                if(choice.equals("1")){
                    System.out.println("Here are all the Site Items in the Transport:");
                    System.out.println(transportDTO.showAllTransportItemsDocs());

                    boolean stillSearching = true;
                    while (stillSearching){
                        System.out.println("\nLet's now Remove an Item from an existing Transport:");
                        System.out.println("Enter the Item's Site's area number: ");
                        int itemsSiteArea = Integer.parseInt(scanner.nextLine());
                        System.out.println("Enter the Item's Site's address string: ");
                        String itemsSiteAddress = scanner.nextLine();
                        System.out.println("Enter the Item's name:");
                        String itemsName = scanner.nextLine();
                        System.out.println("Enter the Item's weight:");
                        int itemsWeight = Integer.parseInt(scanner.nextLine());
                        System.out.println("Enter the condition of the Item you want to remove: ( ( 'Good' ) / ( 'Bad'(or any other key) )");
                        boolean condition = scanner.nextLine().equals("Good");
                        System.out.println("Enter the Item's amount you want to remove:");
                        int itemsAmountToRemove = Integer.parseInt(scanner.nextLine());

                        ItemDTO itemToRemove = null;
                        boolean searchingItemToRemove = true;
                        for (ItemsDocDTO itemsDocDTO : transportDTO.getDests_Docs()){
                            if (itemsDocDTO.getDest_siteDTO().getSiteAreaNum() == itemsSiteArea && itemsDocDTO.getDest_siteDTO().getAddressString().equals(itemsSiteAddress)){
                                // found the Site from which we will deduct the item's amount
                                for (ItemDTO itemDTO : itemsDocDTO.getItemDTOs().keySet()){
                                    if (itemDTO.getName().equals(itemsName) && itemDTO.getWeight() == itemsWeight && itemDTO.getCondition() == condition){
                                        stillSearching = false;  // because found
                                        if(itemsDocDTO.getItemDTOs().get(itemDTO) <= itemsAmountToRemove){
                                            itemToRemove = itemDTO;  // remove after the break
                                            searchingItemToRemove = false;  //  because found
                                        }else {
                                            itemsDocDTO.getItemDTOs().put(itemDTO, itemsDocDTO.getItemDTOs().get(itemDTO) - itemsAmountToRemove);
                                        }
                                        break;
                                    }
                                }
                                if (!stillSearching){
                                    if(!searchingItemToRemove){  // because was found
                                        itemsDocDTO.getItemDTOs().remove(itemToRemove);
                                    }
                                    break;  //  becasue found already
                                }
                            }
                        }

                        if (stillSearching){
                            System.out.println("couldn't find any item matching the Details you've put in, try again");
                        }
                    }

                    System.out.println("Removed Entered Item's desired quantity from this Transport.");
                    stillLoopingInReplanningChiceLoop = false;


                }else if(choice.equals("2")){
                    System.out.println("Here are all the Sites and their Items in the Transport:");
                    System.out.println(transportDTO.showAllTransportItemsDocs());

                    System.out.println("Ok, let's remove a destination Site from this Transport.");
                    System.out.println("Enter the area number of the Destination Site to remove: ");
                    int areaNum = Integer.parseInt(scanner.nextLine());
                    System.out.println("Enter the address string of the Destination Site to remove: ");
                    String address = scanner.nextLine();

                    int indexToRemoveFromItemsDocs = 0;
                    for (ItemsDocDTO itemsDocDTO : transportDTO.getDests_Docs()){
                        if (itemsDocDTO.getDest_siteDTO().getSiteAreaNum() == areaNum && itemsDocDTO.getDest_siteDTO().getAddressString().equals(address)){ break ;}
                        indexToRemoveFromItemsDocs++;
                    }
                    transportDTO.getDests_Docs().remove(indexToRemoveFromItemsDocs);
                    System.out.println("Removed Entered Destination Site from this Transport (and all of it's items).");
                    stillLoopingInReplanningChiceLoop = false;


                }else if(choice.equals("3")){
                    System.out.println("These are the available Trucks in the WareHouse, let's choose a new Truck for your Transport:\n");
                    System.out.println("Available Trucks:\n" + this.tru_ser.showTrucks() + "\n");
                    System.out.println("Please Enter the New Truck-Driver pairing you want:");
                    System.out.println("Please Enter desired Truck number:");
                    int truckNum = Integer.parseInt(scanner.nextLine());
                    transportDTO.setTransportTruckNum(truckNum);
                    System.out.println("Changed Transport Truck");
                    stillLoopingInReplanningChiceLoop = false;


                } else {
                    System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
                    //  not changing stillLoopingInReplanningChiceLoop (boolean) so it remains true and will ask for an option choice again.
                }

            }

        }

        // and then it will check again in the caller function
        System.out.println("Okay, Let's Check Transport Validity again...");

    }








    private void checkIfQueuedTransportCanBeSent(){
        System.out.println("   --------    Transport Queue Checkup    --------\n");
        System.out.println("(1)  View All Queued Transports");
        System.out.println("(2)  Try Sending Head Queued Transport");
        System.out.println("(3)  Back to Transports Options Menu");
        System.out.println();
        System.out.println(" Select Option: ");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            System.out.println(this.tra_ser.showAllQueuedTransports());
        }else if(choice.equals("2")){
            this.tra_ser.checkIfFirstQueuedTransportsCanGo();
        }else if (choice.equals("3")) {
            System.out.println("\n\n");
            transportsOptionsMenu();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
        }
        System.out.println();
        checkIfQueuedTransportCanBeSent();
    }





    private void deleteaTransportMenu(){
        System.out.println("   --------    Transport Deletion    --------\n");
        //TODO
    }







    private void editaTransportMenu(){
        System.out.println("   --------    Transport Edition Menu    --------\n");
        System.out.println("(1)  Edit a Transport's Status");
        System.out.println("(2)  Edit a Transport's Problems");
        System.out.println("(3)  Edit a Transport's Sites");    //TODO :  also add: arrival order to sites here (inside this option's(3's) menu)   <<<------------
        System.out.println("(4)  Edit a Transport's Items");
        System.out.println("(5)  Back to Transports Options Menu");
        System.out.println();
        System.out.println(" Select Option: ");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            // TODO: send down to the Service Layer
        }else if(choice.equals("2")){
            // TODO: send down to the Service Layer
        }else if(choice.equals("3")){
            // TODO: send down to the Service Layer
        } else if (choice.equals("4")) {
            // TODO
        } else if (choice.equals("5")) {
            System.out.println("\n\n");
            transportsOptionsMenu();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
            editaTransportMenu();
        }

    }




































    









    private void shippingAreasOptionsMenu(){   ////////////////////////////////   Shipping Areas Menu   <<<--------------------------------------------
        System.out.println("   --------    Shipping Areas Options Menu    -------\n");
        System.out.println("(1)  View All Shipping Areas");
        System.out.println("(2)  Add a Shipping Area");
        System.out.println("(3)  Edit a Shipping Area's Details");
        System.out.println("(4)  Delete a Shipping Area");
        System.out.println("(5)  Back to Transport Manager Main Menu");
        System.out.println();
        System.out.println(" Select Option: ");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            viewAllShippingAreas();
        }else if(choice.equals("2")){
            addaShippingArea();
        }else if(choice.equals("3")){
            editaShippingAreasDetails();
        } else if (choice.equals("4")) {
            deleteaShippingArea();
        } else if (choice.equals("5")) {
            System.out.println("\n\n");
            transportManagerMainMenu();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
            shippingAreasOptionsMenu();
        }

    }

    //////////////////////          HELPER FUNCTIONS FOR THE Shipping Areas Options Menu

    private void viewAllShippingAreas(){
        System.out.println("   --------    Showing All Shipping Areas    --------\n");
        System.out.println(site_ser.showAllShippingAreas());
        System.out.println();
        shippingAreasOptionsMenu();
    }

    private void addaShippingArea(){
        System.out.println("   --------    Adding a Shipping Area    -------\n");

        System.out.println("Enter Area Number: ");
        int areaNum = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter Area Name: ");
        String areaName = scanner.nextLine();

        String res = site_ser.addShippingArea(areaNum, areaName);
        if(res.equals("Success")){
            System.out.println("Successfully Added Shipping Area\n");
        } else if(res.equals("Exception")){
            System.out.println("Failed to add Shipping Area due to technical machine error\n");
        }else { System.out.println(res + "\n"); }  // printing error string given from Service Layer

        System.out.println();
        shippingAreasOptionsMenu();
    }



    private void deleteaShippingArea(){
        System.out.println("   --------    Deleting a Shipping Area    -------\n");
        System.out.println("Enter area number: ");
        int areaNum = Integer.parseInt(scanner.nextLine());

        String res = site_ser.deleteShippingArea(areaNum);

        if(res.equals("Success")){
            System.out.println("Successfully Deleted Shipping Area\n");
        } else if(res.equals("Exception")){
            System.out.println("Failed to Delete Shipping Area due to technical machine error\n");
        }else { System.out.println(res + "\n"); }  // printing error string given from Service Layer

        System.out.println();
        shippingAreasOptionsMenu();
    }

    private void editaShippingAreasDetails(){
        System.out.println("   --------    Editing a Shipping Area Menu    -------\n");
        System.out.println("Enter Data of Shipping Area to Edit:");
        System.out.println("Enter Area Number: ");
        int areaNum = Integer.parseInt(scanner.nextLine());

        System.out.println("\nWhat information would you like to edit?: ");
        System.out.println("1. Area Number");
        System.out.println("2. Area Name");
        System.out.println(" Select Option: ");

        int infoType = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter Updated Data: ");
        String res = "";
        if (infoType == 1){
            int Newareanum = Integer.parseInt(scanner.nextLine());
            res = site_ser.setShippingAreaNum(areaNum, Newareanum);
        } else if (infoType == 2) {
            String NewareaName = scanner.nextLine();
            res = site_ser.setShippingAreaName(areaNum, NewareaName);
        }

        if(res.equals("Success")){
            System.out.println("Successfully Edited Site\n");
        } else if(res.equals("Exception")){
            System.out.println("Failed to Edit Site due to technical machine error\n");
        }else { System.out.println(res + "\n"); }  // printing error string given from Service Layer

        System.out.println();
        shippingAreasOptionsMenu();
    }








    private void sitesOptionsMenu(){   ////////////////////////////////    Sites Menu   <<<--------------------------------------------
        System.out.println("   --------    Sites Options Menu    -------\n");
        System.out.println("(1)  View All Sites");
        System.out.println("(2)  Add a Site");
        System.out.println("(3)  Edit a Site's Details");
        System.out.println("(4)  Delete a Site");
        System.out.println("(5)  Back to Transport Manager Main Menu");
        System.out.println();
        System.out.println(" Select Option: ");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            viewAllSites();
        }else if(choice.equals("2")){
            addaSite();
        }else if(choice.equals("3")){
            editaSitesDetails();
        } else if (choice.equals("4")) {
            deleteaSite();
        } else if (choice.equals("5")) {
            System.out.println("\n\n");
            transportManagerMainMenu();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
            sitesOptionsMenu();
        }
    }

    //////////////////////          HELPER FUNCTIONS FOR THE Sites Options Menu

    private void viewAllSites(){
        System.out.println("   --------    Showing All Sites    --------\n");
        System.out.println(site_ser.showAllSites());
        System.out.println();
        sitesOptionsMenu();
    }

    private void addaSite(){
        System.out.println("   --------    Adding a Site    -------\n");
        System.out.println("Enter Area Number: ");
        int areaNum = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter Address: ");
        String address = scanner.nextLine();
        System.out.println("Enter contact name: ");
        String contName = scanner.nextLine();
        System.out.println("Enter contact number: ");
        long contNum = Long.parseLong(scanner.nextLine());

        String res = site_ser.addSite(areaNum, address, contName, contNum);
        if(res.equals("Success")){
            System.out.println("Successfully Added Site\n");
        } else if(res.equals("Exception")){
            System.out.println("Failed to add Site due to technical machine error\n");
        }else { System.out.println(res + "\n"); }  // printing error string given from Service Layer

        System.out.println();
        sitesOptionsMenu();
    }

    private void deleteaSite(){
        System.out.println("   --------    Deleting a Site    -------\n");
        System.out.println("Enter area number: ");
        int areaNum = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter address: ");
        String address = scanner.nextLine();

        String res = site_ser.deleteSite(areaNum, address);

        if(res.equals("Success")){
            System.out.println("Successfully Deleted Site\n");
        } else if(res.equals("Exception")){
            System.out.println("Failed to Delete Site due to technical machine error\n");
        }else { System.out.println(res + "\n"); }  // printing error string given from Service Layer

        System.out.println();
        sitesOptionsMenu();
    }

    private void editaSitesDetails(){
        System.out.println("   --------    Editing a Site Menu    -------\n");
        System.out.println("Enter Data of Site to Edit:");
        System.out.println("Enter Area Number: ");
        int areaNum = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter Address: ");
        String address = scanner.nextLine();

        System.out.println("\nWhat information would you like to edit?: ");
        System.out.println("1. Site Area Number");
        System.out.println("2. Site Address String");
        System.out.println("3. Site Contact Name");
        System.out.println("4. Site Contact Number");
        System.out.println(" Select Option: ");

        int infoType = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter Updated Data: ");
        String res = "";
        if (infoType == 1){
            int Newareanum = Integer.parseInt(scanner.nextLine());
            res = site_ser.setSiteAreaNum(areaNum, Newareanum, address);
        } else if (infoType == 2) {
            String Newaddress = scanner.nextLine();
            res = site_ser.setSiteAddress(areaNum, address, Newaddress);
        } else if (infoType == 3) {
            String NewcontName = scanner.nextLine();
            res = site_ser.setSiteContName(areaNum, address, NewcontName);
        } else if (infoType == 4) {
            long NewContnum = Long.parseLong(scanner.nextLine());
            res = site_ser.setSiteContNum(areaNum, address, NewContnum);
        }

        if(res.equals("Success")){
            System.out.println("Successfully Edited Site\n");
        } else if(res.equals("Exception")){
            System.out.println("Failed to Edit Site due to technical machine error\n");
        }else { System.out.println(res + "\n"); }  // printing error string given from Service Layer

        System.out.println();
        sitesOptionsMenu();
    }






















    private void driversOptionsMenu(){   ////////////////////////////////    Drivers Menu   <<<--------------------------------------------
        System.out.println("   --------    Drivers Options Menu    -------\n");
        System.out.println("(1)  View All Drivers");
        System.out.println("(2)  Add a Driver");
        System.out.println("(3)  Delete a Driver");  //TODO: I just copied to here the relavent Features for the Menu here
        System.out.println("(4)  Edit a Driver's Details");
        System.out.println("(5)  Back to Transport Manager Menu");
        System.out.println();
        System.out.println(" Select Option: ");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            viewAllDrivers();
        }else if(choice.equals("2")){
            addaDriver();
        }else if(choice.equals("3")){
            deleteaDriver();
        } else if (choice.equals("4")) {
            editaDriversDetails();
        } else if (choice.equals("5")) {
            System.out.println("\n\n");
            transportManagerMainMenu();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
            driversOptionsMenu();
        }
    }

    //////////////////////          HELPER FUNCTIONS FOR THE Drivers Options Menu

    private void viewAllDrivers(){
        System.out.println("   --------    Showing All Drivers    --------\n");
        System.out.println(emp_ser.showDrivers());
        System.out.println();
        driversOptionsMenu();
    }

    private void addaDriver(){
        System.out.println("   --------    Driver Addition    --------\n");
        //TODO

        System.out.println();
        driversOptionsMenu();
    }

    private void deleteaDriver(){
        System.out.println("   --------    Driver Deletion    --------\n");
        //TODO

        System.out.println();
        driversOptionsMenu();
    }

    private void editaDriversDetails(){
        System.out.println("   --------    Driver Details Edition Menu    --------\n");
        //TODO

        System.out.println();
        driversOptionsMenu();
    }



















    private void trucksOptionsMenu(){   ////////////////////////////////    Trucks Menu   <<<--------------------------------------------
        System.out.println("   --------    Trucks Options Menu    -------\n");
        System.out.println("(1)  View All Trucks");
        System.out.println("(2)  Add a Truck");
        System.out.println("(3)  Delete a Truck");  //TODO: I just copied to here the relavent Features for the Menu here
        System.out.println("(4)  Edit a Truck's Details");
        System.out.println("(5)  Back to Transport Manager Menu");
        System.out.println();
        System.out.println(" Select Option: ");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            viewAllTrucks();
        }else if(choice.equals("2")){
            addaTruck();
        }else if(choice.equals("3")){
            deleteaTruck();
        } else if (choice.equals("4")) {
            editaTrucksDetails();
        } else if (choice.equals("5")) {
            System.out.println("\n\n");
            transportManagerMainMenu();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
            trucksOptionsMenu();
        }
    }

    //////////////////////          HELPER FUNCTIONS FOR THE Trucks Options Menu

    private void viewAllTrucks(){
        System.out.println("   --------    Showing All Trucks    --------\n");
        System.out.println(tru_ser.showTrucks());
        System.out.println();
        trucksOptionsMenu();  //  returning to the menu after the action
    }


    private void addaTruck(){
        System.out.println("   --------    Truck Addition    --------\n");
        //TODO

        System.out.println();
        trucksOptionsMenu();  //  returning to the menu after the action
    }


    private void deleteaTruck(){
        System.out.println("   --------    Truck Deletion    --------\n");
        //TODO

        System.out.println();
        trucksOptionsMenu();  //  returning to the menu after the action
    }


    private void editaTrucksDetails(){
        System.out.println("   --------    Truck Details Edition Menu    --------\n");
        //TODO

        System.out.println();
        trucksOptionsMenu();  //  returning to the menu after the action
    }









}
