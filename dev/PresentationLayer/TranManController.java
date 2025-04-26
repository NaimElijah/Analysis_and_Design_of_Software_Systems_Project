package PresentationLayer;

import DTOs.ItemDTO;
import DTOs.ItemsDocDTO;
import DTOs.SiteDTO;
import DTOs.TransportDTO;
import ServiceLayer.EmployeeService;
import ServiceLayer.SiteService;
import ServiceLayer.TransportService;
import ServiceLayer.TruckService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

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




        /// ////////////////////////////////////////////    NOW WE'LL DO THE CHECKS          <<<-----------------------------------------


        checkIfTransportDTOIsValid(transportDTO);

        System.out.println("Okay, Transport is Valid :)");

        String resOfNewTransportAddition = "";
        try {
            resOfNewTransportAddition = this.tra_ser.createTransport(objectMapper.writeValueAsString(transportDTO));  /// <<------  HERE WE CREATE THE TRANSPORT AFTER THE CHECKS
        } catch (Exception e) {
            System.out.println("Serialization's fault");
            e.printStackTrace();
        }

        if(resOfNewTransportAddition.equals("Success")){
            System.out.println("Successfully Added Transport, You can view the Transport's Details (and it's given ID) using the Menu.\n");
        } else if(resOfNewTransportAddition.equals("Exception")){
            System.out.println("Failed to add Transport due to technical machine error\n");
        }else { System.out.println(resOfNewTransportAddition + "\n"); }

        System.out.println();
        transportsOptionsMenu();
    }





    private void transportPairingRePlanning(TransportDTO transportDTO) {
        //  Note: if we got to here it seems there is a possible pairing in the system right now. (because we didn't automatically go to the "Queue")
        System.out.println("The Good news is that we've Detected that a Driver-Truck Pairing for this Transport is Available, so let's try and choose a matching pair:");
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
        // and then it will check validity again in the caller function
    }




    private void transportWeightRePlanning(TransportDTO transportDTO, String issue){
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
        // and then it will check validity again in the caller function
    }









    private void checkIfTransportDTOIsValid(TransportDTO transportDTO){
        System.out.println("Checking Transport Validity...");

        String resOfTransportCheck = "";
        try {
            resOfTransportCheck = this.tra_ser.checkTransportValidity(objectMapper.writeValueAsString(transportDTO));  //  check Transport Validity
            ///  returns: "Valid", "BadLicenses", "overallWeight-truckMaxCarryWeight", "Queue", "Occupied"
        } catch (Exception e) {
            System.out.println("Serialization's fault");
            e.printStackTrace();
        }

        while (!resOfTransportCheck.equals("Valid")){
            System.out.println("There seems There is a problem with The Transport you're trying to create:\n");
            if (resOfTransportCheck.equals("Queue")){
                System.out.println("This Transport doesn't have a proper Truck-Driver matching available at all right now, so this Transport will now go automatically into the Queued Transports.");
                System.out.println("We save the Queued Transports in order of creation, so when you want to, you can go to (Transports Options Menu)->((3)  Check if a Queued Transport Can Be Sent) "); //TODO: add path
                System.out.println("And try to send out the next transport to be sent, in the Queued Transports order, you can of course also delete that transport Using the Menu.\n");
                break;

            } else if (resOfTransportCheck.equals("Occupied")){
                System.out.println("The Driver or/and the Truck you designated for this Transport are Occupied with another Active Transport/s");
                transportPairingRePlanning(transportDTO);

            } else if (resOfTransportCheck.equals("BadLicenses")) {
                System.out.println("The Driver you designated doesn't have a License that matches the License required for the Truck you selected");
                transportPairingRePlanning(transportDTO);

            } else {    ///  "overallWeight-truckMaxCarryWeight" Case
                transportWeightRePlanning(transportDTO, resOfTransportCheck);
            }

            System.out.println("Okay, Let's Check Transport Validity again...");
            try {
                resOfTransportCheck = this.tra_ser.checkTransportValidity(objectMapper.writeValueAsString(transportDTO));  //  check Transport Validity again
                ///  returns: "Valid", "BadLicenses", "overallWeight-truckMaxCarryWeight", "Queue", "Occupied"
            } catch (Exception e) {
                System.out.println("Serialization's fault");
                e.printStackTrace();
            }
        }


    }






    private void checkIfQueuedTransportCanBeSent(){
        System.out.println("   --------    Transport Queue Checkup    --------\n");
        System.out.println("(1)  View All Queued Transports (In Queue Order)");
        System.out.println("(2)  Try Sending Head (1) Queued Transport");
        System.out.println("(3)  Back to Transports Options Menu");
        System.out.println();
        System.out.println(" Select Option: ");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            System.out.println(this.tra_ser.showAllQueuedTransports());




        }else if(choice.equals("2")){

            String resOfcheckIfCanGo = this.tra_ser.checkIfFirstQueuedTransportsCanGo();

            //TODO:  so we basically do exactly as we did before when we created a Transport.
            //TODO: also use the checkIfTransportDTOIsValid function.

            TransportDTO transport_DTO = null;
            try {
                transport_DTO = this.objectMapper.readValue(resOfcheckIfCanGo, TransportDTO.class);   //  see what to do about the return value of the resOfcheckIfCanGo
            } catch (Exception e) {
                System.out.println("Serialization's fault");
                e.printStackTrace();
            }

            checkIfTransportDTOIsValid(transport_DTO);

            System.out.println("Okay, Transport is Valid :)");

            String resOfNewTransportAddition = "";
            try {
                resOfNewTransportAddition = this.tra_ser.createTransport(objectMapper.writeValueAsString(transport_DTO));  /// <<------  HERE WE CREATE THE TRANSPORT AFTER THE CHECKS
            } catch (Exception e) {
                System.out.println("Serialization's fault");
                e.printStackTrace();
            }

            if(resOfNewTransportAddition.equals("Success")){
                System.out.println("Successfully Added Transport, You can view the Transport's Details (and it's given ID) using the Menu.\n");
            } else if(resOfNewTransportAddition.equals("Exception")){
                System.out.println("Failed to add Transport due to technical machine error\n");
            }else { System.out.println(resOfNewTransportAddition + "\n"); }


//            if (resOfcheckIfCanGo.equals("Queue")){
//                System.out.println("Seems that the First Transport in the Queued Transports still can't go, you can keep it there, or you can edit and delete it if you'd like");
//            }else if (resOfcheckIfCanGo.equals("Valid")){
//                System.out.println("Hurray, The First ");
//            }




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
        System.out.println("Let's delete a Transport, Enter the Transport's ID (can be seen with the option, in the Menu, to show all Transports):");
        int transportId = Integer.parseInt(scanner.nextLine());

        String res = this.tra_ser.deleteTransport(transportId);
        if(res.equals("Success")){
            System.out.println("Successfully Deleted Transport.\n");
        } else if(res.equals("Exception")){
            System.out.println("Failed to delete Transport due to technical machine error\n");
        }else { System.out.println(res + "\n"); }

        System.out.println();
        transportsOptionsMenu();
    }












































    private void editaTransportMenu(){
        System.out.println("   --------    Transport Edition Menu    --------\n");
        System.out.println("(1)  Edit a Transport's Status");
        System.out.println("(2)  Edit a Transport's Problems");
        System.out.println("(3)  Edit a Transport's Sites");
        System.out.println("(4)  Edit a Transport's Items");
        System.out.println("(5)  Edit a Transport's Driver/Truck");
        System.out.println("(6)  Back to Transports Options Menu");
        System.out.println();
        System.out.println(" Select Option: ");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            editATransportsStatus();
        }else if(choice.equals("2")){
            editATransportsProblems();
        }else if(choice.equals("3")){
            editATransportsSites();
        } else if (choice.equals("4")) {
            editATransportsItems();
        } else if (choice.equals("5")) {
            editATransportsDriverOrTruck();
        } else if (choice.equals("6")) {
            System.out.println("\n\n");
            transportsOptionsMenu();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
        }
        System.out.println();
        editaTransportMenu();
    }






    private void editATransportsStatus(){
        System.out.println("   --------    Transport's Status Edition    --------\n");
        System.out.println("(1)  Set a Transport's Status");
        System.out.println("(2)  Back to Transport Edition Menu");
        System.out.println();
        System.out.println(" Select Option: ");

        String choice = scanner.nextLine();
        if(choice.equals("1")){

            System.out.println("Enter the Transport ID of the Transport you want to change it's status:");
            int transportId = Integer.parseInt(scanner.nextLine());
            System.out.println(" To which status do you want to change that Transport's Status ?");
            System.out.println(".1. Being Assembled");
            System.out.println(".2. Queued");
            System.out.println(".3. In Transit");
            System.out.println(".4. Completed");
            System.out.println(".5. Canceled");
            System.out.println(".6. Being Delayed");
            System.out.println(" Select Option: ");
            String statusChoice = scanner.nextLine();

            String res = this.tra_ser.setTransportStatus(transportId, statusChoice);
            if(res.equals("Success")){
                System.out.println("Successfully Changed Transport's Status.\n");
            } else if(res.equals("Exception")){
                System.out.println("Failed to change the Transport's status due to technical machine error.\n");
            }else { System.out.println(res + "\n"); }

        }else if (choice.equals("2")) {
            System.out.println("\n\n");
            editaTransportMenu();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
        }
        System.out.println();
        editATransportsStatus();
    }








    private void editATransportsProblems(){
        System.out.println("   --------    Transport's Problems Edition    --------\n");
        System.out.println("(1)  Add a Problem to a Transport");
        System.out.println("(2)  Remove a Problem from a Transport");
        System.out.println("(3)  Back to Transport Edition Menu");
        System.out.println();
        System.out.println(" Select Option: ");

        String choice = scanner.nextLine();
        if(choice.equals("1")){

            System.out.println("Enter the Transport ID of the Transport you want to add a problem to:");
            int transportId1 = Integer.parseInt(scanner.nextLine());
            System.out.println(" Which Problem do you want to add to that Transport's Document ?");
            System.out.println(".1. Puncture");
            System.out.println(".2. HeavyTraffic");
            System.out.println(".3. RoadAccident");
            System.out.println(".4. UnresponsiveContact");
            System.out.println(".5. TruckVehicleProblem");
            System.out.println(".6. EmptyTruckGasTank");
            System.out.println(" Select Option: ");
            String statusChoice1 = scanner.nextLine();

            String res1 = this.tra_ser.addTransportProblem(transportId1, statusChoice1);
            if(res1.equals("Success")){
                System.out.println("Successfully Added to Transport's Problems, I hope it gets solved as as fast as possible.\n");
            } else if(res1.equals("Exception")){
                System.out.println("Failed to add the Transport problem due to technical machine error.\n");
            }else { System.out.println(res1 + "\n"); }

        }else if(choice.equals("2")){

            System.out.println("Enter the Transport ID of the Transport you want to remove a problem from:");
            int transportId2 = Integer.parseInt(scanner.nextLine());
            System.out.println(" Which Problem do you want to remove from that Transport's Document ?");
            System.out.println(".1. Puncture");
            System.out.println(".2. HeavyTraffic");
            System.out.println(".3. RoadAccident");
            System.out.println(".4. UnresponsiveContact");
            System.out.println(".5. TruckVehicleProblem");
            System.out.println(".6. EmptyTruckGasTank");
            System.out.println(" Select Option: ");
            String statusChoice2 = scanner.nextLine();

            String res2 = this.tra_ser.removeTransportProblem(transportId2, statusChoice2);
            if(res2.equals("Success")){
                System.out.println("Successfully removed from Transport's Problems.\n");
            } else if(res2.equals("Exception")){
                System.out.println("Failed to remove the Transport problem due to technical machine error.\n");
            }else { System.out.println(res2 + "\n"); }

        }else if (choice.equals("3")) {
            System.out.println("\n\n");
            editaTransportMenu();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
        }
        System.out.println();
        editATransportsProblems();
    }








    private void editATransportsSites(){
        System.out.println("   --------    Transport's Sites Edition Menu    --------\n");
        System.out.println("(1)  Add a New Site's Items Document to a Transport");
        System.out.println("(2)  Remove a Site's Items Document from a Transport");
        System.out.println("(3)  Edit a Transport's Site's Items Document ID");
        System.out.println("(4)  Set a Site's Arrival order in it's Transport");
        System.out.println("(5)  Back to Transport Edition Menu");
        System.out.println();
        System.out.println(" Select Option: ");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            System.out.println("Enter the Transport ID of the Transport you want to add a New Site's Items Document to:");
            int transportId1 = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter the New Items Document ID number, for that site, you want to add:");
            int newItemsDocId1 = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter Destination Area number for that site:");
            int destAreaNumber1 = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter Destination Site Address String for that site:");
            String destSiteAddress1 = scanner.nextLine();
            System.out.println("Enter that Site's Contact Name:");
            String contactName1 = scanner.nextLine();
            System.out.println("Enter that contact's number:");
            long contactNumber1 = Long.parseLong(scanner.nextLine());

            String res1 = this.tra_ser.addDestSite(transportId1, newItemsDocId1, destAreaNumber1, destSiteAddress1, contactName1, contactNumber1);
            if(res1.equals("Success")){
                System.out.println("Successfully removed from Transport's Site's Items Documents.\n");
            } else if(res1.equals("Exception")){
                System.out.println("Failed to remove the Transport's Site's Items Document due to technical machine error.\n");
            }else { System.out.println(res1 + "\n"); }

        }else if(choice.equals("2")){
            System.out.println("Enter the Transport ID of the Transport you want to remove a Site's Items Document from:");
            int transportId2 = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter the Items Document ID number, for that site, you want to remove:");
            int oldItemsDocId2 = Integer.parseInt(scanner.nextLine());

            String res2 = this.tra_ser.removeDestSite(transportId2, oldItemsDocId2);
            if(res2.equals("Success")){
                System.out.println("Successfully removed from Transport's Site's Items Documents.\n");
            } else if(res2.equals("Exception")){
                System.out.println("Failed to remove the Transport's Site's Items Document due to technical machine error.\n");
            }else { System.out.println(res2 + "\n"); }

        }else if(choice.equals("3")){
            System.out.println("Let's change a Transport's Site's Items Document ID.");
            System.out.println("Enter Old Items Document ID number:");
            int oldItemsDocId3 = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter New Items Document ID number:");
            int newItemsDocId3 = Integer.parseInt(scanner.nextLine());

            String res3 = this.tra_ser.changeAnItemsDocNum(oldItemsDocId3, newItemsDocId3);
            if(res3.equals("Success")){
                System.out.println("Successfully changed Items Documents ID.\n");
            } else if(res3.equals("Exception")){
                System.out.println("Failed to change Items Documents ID due to technical machine error.\n");
            }else { System.out.println(res3 + "\n"); }

        } else if (choice.equals("4")) {
            System.out.println("Enter the Transport ID:");
            int transportID = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter the Site's Area number:");
            int areaNumber1 = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter the Site's Site Address String:");
            String siteAddress1 = scanner.nextLine();
            System.out.println("Enter the New index in the Sites Arrival Order, of that Transport, that you want to put that Site:");
            String newIndex1 = scanner.nextLine();

            String res4 = this.tra_ser.setSiteArrivalIndexInTransport(transportID, areaNumber1, siteAddress1, newIndex1);
            if(res4.equals("Success")){
                System.out.println("Successfully changed the Site's Arrival Order.\n");
            } else if(res4.equals("Exception")){
                System.out.println("Failed to change the Site's Arrival Order due to technical machine error.\n");
            }else { System.out.println(res4 + "\n"); }

        } else if (choice.equals("5")) {
            System.out.println("\n\n");
            editaTransportMenu();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
        }
        System.out.println();
        editATransportsSites();
    }



















    private void editATransportsItems(){
        System.out.println("   --------    Transport's Items Edition Menu    --------\n");
        System.out.println("(1)  Add an Item to a Transport");
        System.out.println("(2)  Remove an Item from a Transport");
        System.out.println("(3)  Set the condition of an Item in a Transport");
        System.out.println("(4)  Back to Transport Edition Menu");
        System.out.println();
        System.out.println(" Select Option: ");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            System.out.println("Let's add an Item to a Transport.");
            System.out.println("Enter The Items Document ID of the Items Document you want to add an Item to:");
            int itemsDocId = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter Item Name:");
            String itemName = scanner.nextLine();
            System.out.println("Enter Item Weight:");
            int itemWeight = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter Item Condition: ( ('Good') / ('Bad'/or any other key) )");
            boolean itemCondition = (scanner.nextLine().equals("Good") ? true : false);
            System.out.println("Enter Item Amount you want to add:");
            int itemAmount = Integer.parseInt(scanner.nextLine());

            String res1 = this.tra_ser.addItem(itemsDocId, itemName, itemWeight, itemAmount, itemCondition);
            if(res1.equals("Success")){
                System.out.println("Successfully added Item.\n");
            } else if(res1.equals("Exception")){
                System.out.println("Failed to add Item due to technical machine error.\n");
            }else { System.out.println(res1 + "\n"); }

        }else if(choice.equals("2")){

            System.out.println("Let's remove an Item from a Transport.");
            System.out.println("Enter The Items Document ID of the Items Document you want to remove an Item from:");
            int itemsDocId = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter Item Name:");
            String itemName = scanner.nextLine();
            System.out.println("Enter Item Weight:");
            int itemWeight = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter Item Condition: ( ('Good') / ('Bad'/or any other key) )");
            boolean itemCondition = (scanner.nextLine().equals("Good"));
            System.out.println("Enter Item Amount you want to remove:");
            int itemAmount = Integer.parseInt(scanner.nextLine());

            String res1 = this.tra_ser.removeItem(itemsDocId, itemName, itemWeight, itemAmount, itemCondition);
            if(res1.equals("Success")){
                System.out.println("Successfully removed Item.\n");
            } else if(res1.equals("Exception")){
                System.out.println("Failed to remove Item due to technical machine error.\n");
            }else { System.out.println(res1 + "\n"); }


        }else if(choice.equals("3")){

            System.out.println("Let's change the Condition of an Item in a Transport.");
            System.out.println("Enter The Items Document ID of the Items Document you want to change an Item's condition in:");
            int itemsDocId = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter Item Name:");
            String itemName = scanner.nextLine();
            System.out.println("Enter Item Weight:");
            int itemWeight = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter the New Item Condition you want to set to that Item: ( ('Good') / ('Bad'/or any other key) )");
            boolean itemCondition = (scanner.nextLine().equals("Good"));
            System.out.println("Enter Item Amount you want to change condition to:");
            int itemAmount = Integer.parseInt(scanner.nextLine());

            String res1 = this.tra_ser.setItemCond(itemsDocId, itemName, itemWeight, itemAmount, itemCondition);
            if(res1.equals("Success")){
                System.out.println("Successfully changed Item's Condition.\n");
            } else if(res1.equals("Exception")){
                System.out.println("Failed to change Item's Condition due to technical machine error.\n");
            }else { System.out.println(res1 + "\n"); }

        } else if (choice.equals("4")) {
            System.out.println("\n\n");
            editaTransportMenu();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
        }
        System.out.println();
        editATransportsItems();
    }
























    private void editATransportsDriverOrTruck(){
        System.out.println("   --------    Transport's Driver/Truck Edition    --------\n");
        System.out.println("(1)  Set a Transport's Truck");
        System.out.println("(2)  Set a Transport's Driver");
        System.out.println("(3)  Back to Transport Edition Menu");
        System.out.println();
        System.out.println(" Select Option: ");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            System.out.println("Enter the Transport ID:");
            int transportID = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter the Desired Truck's Number:");
            int truckID = Integer.parseInt(scanner.nextLine());

            String res1 = this.tra_ser.setTransportTruck(transportID, truckID);
            if(res1.equals("Success")){
                System.out.println("Successfully set Transport's Truck.\n");
            } else if(res1.equals("Exception")){
                System.out.println("Failed to set Transport's Truck due to technical machine error.\n");
            }else { System.out.println(res1 + "\n"); }

        }else if(choice.equals("2")){
            System.out.println("Enter the Transport ID:");
            int transportID = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter the Desired Driver's ID:");
            int driverID = Integer.parseInt(scanner.nextLine());

            String res2 = this.tra_ser.setTransportDriver(transportID, driverID);
            if(res2.equals("Success")){
                System.out.println("Successfully set Transport's Driver.\n");
            } else if(res2.equals("Exception")){
                System.out.println("Failed to set Transport's Driver due to technical machine error.\n");
            }else { System.out.println(res2 + "\n"); }

        }else if (choice.equals("3")) {
            System.out.println("\n\n");
            editaTransportMenu();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
        }
        System.out.println();
        editATransportsDriverOrTruck();
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
        }else { System.out.println(res + "\n"); }

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
        }else { System.out.println(res + "\n"); }

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
        }else { System.out.println(res + "\n"); }

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
        }else { System.out.println(res + "\n"); }

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
        }else { System.out.println(res + "\n"); }

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
        }else { System.out.println(res + "\n"); }

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
