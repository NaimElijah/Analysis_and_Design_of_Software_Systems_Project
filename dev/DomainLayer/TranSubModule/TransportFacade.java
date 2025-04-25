package DomainLayer.TranSubModule;

import DomainLayer.EmpSubModule.Driver;
import DomainLayer.EmpSubModule.Employee;
import DomainLayer.EmpSubModule.EmployeeFacade;
import DomainLayer.SiteSubModule.Address;
import DomainLayer.SiteSubModule.Site;
import DomainLayer.SiteSubModule.SiteFacade;
import DomainLayer.TruSubModule.Truck;
import DomainLayer.TruSubModule.TruckFacade;
import PresentationLayer.DTOs.ItemDTO;
import PresentationLayer.DTOs.ItemsDocDTO;
import PresentationLayer.DTOs.TransportDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.naming.CommunicationException;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;

public class TransportFacade {
    private HashMap<Integer, TransportDoc> transports;
    private int transportIDCounter;     ///   <<<---------------------------------    for the transport Docs ID's
    private HashMap<Integer, ItemsDoc> itemsDocs;  // to know an ItemsDoc's num is unique like required.
    private ArrayList<TransportDoc> queuedTransports;

    private EmployeeFacade employeeFacade;
    private SiteFacade siteFacade;
    private TruckFacade truckFacade;

    private ObjectMapper objectMapper;

    public TransportFacade(EmployeeFacade eF, SiteFacade sF, TruckFacade tF) {
        this.transportIDCounter = 0;
        this.objectMapper = new ObjectMapper();
        this.transports = new HashMap<Integer, TransportDoc>();
        this.itemsDocs = new HashMap<Integer, ItemsDoc>();
        this.queuedTransports = new ArrayList<TransportDoc>();
        this.employeeFacade = eF;
        this.siteFacade = sF;
        this.truckFacade = tF;
    }

    public HashMap<Integer, TransportDoc> getTransports() {return transports;}
    public void setTransports(HashMap<Integer, TransportDoc> transports) {this.transports = transports;}
    public HashMap<Integer, ItemsDoc> getItemsDocs() {return itemsDocs;}
    public void setItemsDocs(HashMap<Integer, ItemsDoc> itemsDocs) {this.itemsDocs = itemsDocs;}





    public void createTransport(String DTO_OfTransport) throws JsonProcessingException {  // time is decided when the Transport departs
        ///  NOTE: I already did all of the checks beforehand, so if we get to here, then we can successfully and legitimately create the Transport

        TransportDTO transport_DTO = this.objectMapper.readValue(DTO_OfTransport, TransportDTO.class);
        Driver driver = this.employeeFacade.getDrivers().get(transport_DTO.getTransportDriverID());
        Truck truck = this.truckFacade.getTrucksWareHouse().get(transport_DTO.getTransportTruckNum());

        /// finding the srcSite
        Site srcSite = null;
        for (Site site : this.siteFacade.getShippingAreas().get(transport_DTO.getSrc_site().getSiteAreaNum()).getSites().values()){
            if (site.getAddress().getArea() == transport_DTO.getSrc_site().getSiteAreaNum() && site.getAddress().getAddress().equals(transport_DTO.getSrc_site().getAddressString())){
                srcSite = site;
            }
        }

        TransportDoc newTransportBeingCreated = new TransportDoc(enumTranStatus.BeingAssembled, this.transportIDCounter, truck, driver, srcSite);
        this.transportIDCounter++;

        /// add the ItemsDocs and the Items that should be in them from the itemsDocDTOs:
        for (ItemsDocDTO itemsDocDTO : transport_DTO.getDests_Docs()){
            /// finding the current destSite
            Site destSite = this.siteFacade.getShippingAreas().get(itemsDocDTO.getDest_siteDTO().getSiteAreaNum()).getSites().get(itemsDocDTO.getDest_siteDTO().getAddressString());

            ItemsDoc addition = newTransportBeingCreated.addDestSite(itemsDocDTO.getItemsDoc_num(), destSite);
            this.itemsDocs.put(itemsDocDTO.getItemsDoc_num(), addition);

            for (ItemDTO itemDTO : itemsDocDTO.getItemDTOs().keySet()){  /// add each item of that site from the DTO's data
                newTransportBeingCreated.addItem(itemsDocDTO.getItemsDoc_num(), itemDTO.getName(), itemDTO.getWeight(), itemsDocDTO.getItemDTOs().get(itemDTO), itemDTO.getCondition());
            }
        }

        /// finishing touches before adding Transport
        newTransportBeingCreated.getTransportTruck().setInTransportID(newTransportBeingCreated.getTran_Doc_ID());
        newTransportBeingCreated.getTransportDriver().setInTransportID(newTransportBeingCreated.getTran_Doc_ID());
        newTransportBeingCreated.setStatus(enumTranStatus.InTransit);
        newTransportBeingCreated.setTruck_Depart_Weight(newTransportBeingCreated.calculateTransportItemsWeight());
        for (ItemsDoc itemsDoc : newTransportBeingCreated.getDests_Docs()){
            this.itemsDocs.put(itemsDoc.getItemDoc_num(), itemsDoc);
        }
        //  the time is set already in the constructor of the Transport
        this.transports.put(newTransportBeingCreated.getTran_Doc_ID(), newTransportBeingCreated);
    }





    public void deleteTransport(int transportID) throws FileNotFoundException {
        boolean containedInQueued = false;
        int indexOfTransport = 0;
        // checking if the transport is in the queue
        for (TransportDoc transportDoc : this.queuedTransports) {
            if (transportDoc.getTran_Doc_ID() == transportID) {
                containedInQueued = true;
                break;
            }
            indexOfTransport++;
        }

        TransportDoc toRemoveDoc = null;
        if (transports.containsKey(transportID)){
            toRemoveDoc = transports.get(transportID);
        } else if (containedInQueued) {
            toRemoveDoc = this.queuedTransports.get(indexOfTransport);
        }else {
            throw new FileNotFoundException("No transport found with the Transport ID you've entered, so can't delete that Transport");
        }

        /// Transport's delete cleanups
        for (ItemsDoc itemsDocInRemovingOne : toRemoveDoc.getDests_Docs()){
            this.itemsDocs.remove(itemsDocInRemovingOne.getItemDoc_num());
        }
        if (toRemoveDoc.getTransportDriver().getInTransportID() != -1 && (toRemoveDoc.getStatus().equals(enumTranStatus.InTransit) || toRemoveDoc.getStatus().equals(enumTranStatus.BeingDelayed))){
            toRemoveDoc.getTransportDriver().setInTransportID(-1);  //TODO:  change up according to the NEW  format of the inTransportID new field  !!!!!!     <<<--------------------------------------   TODO
        }
        if (toRemoveDoc.getTransportTruck().getInTransportID() != -1 && (toRemoveDoc.getStatus().equals(enumTranStatus.InTransit) || toRemoveDoc.getStatus().equals(enumTranStatus.BeingDelayed))){
            toRemoveDoc.getTransportTruck().setInTransportID(-1);  //TODO:  change up according to the NEW  format of the inTransportID new field  !!!!!!     <<<--------------------------------------   TODO
        }

        transports.remove(transportID);
    }





    public void setTransportStatus(int TranDocID, int menu_status_option) throws FileNotFoundException, FileAlreadyExistsException, ClassNotFoundException {
        if(!transports.containsKey(TranDocID)){
            throw new FileNotFoundException("The Transport ID you have entered doesn't exist.");
        }
        enumTranStatus status = null;
        if (menu_status_option == 1){
            status = enumTranStatus.BeingAssembled;
        } else if (menu_status_option == 2) {
            status = enumTranStatus.Queued;
        } else if (menu_status_option == 3) {
            status = enumTranStatus.InTransit;
        } else if (menu_status_option == 4) {
            status = enumTranStatus.Completed;
        } else if (menu_status_option == 5) {
            status = enumTranStatus.Canceled;
        } else if (menu_status_option == 6) {
            status = enumTranStatus.BeingDelayed;
        }

        if (transports.get(TranDocID).getStatus().equals(status)) {
            throw new FileAlreadyExistsException("The status you are trying to set already is the status of this Transport");
        }

        if(status.equals(enumTranStatus.InTransit)){
            if(this.transports.get(TranDocID).getStatus() == enumTranStatus.Canceled || this.transports.get(TranDocID).getStatus() == enumTranStatus.Completed){
                if((this.transports.get(TranDocID).getTransportTruck().getInTransportID() != -1) || (this.transports.get(TranDocID).getTransportDriver().getInTransportID() != -1)){
                    throw new ClassNotFoundException("cannot change status to InTransit because the driver or the truck aren't free, maybe change them, and try again.");
                }       //TODO:  change up according to the NEW  format of the inTransportID new field  !!!!!!     <<<--------------------------------------   TODO
            }     //TODO:  take care of the situation where we set to InTransit, but Driver or Truck are not free
            this.transports.get(TranDocID).getTransportDriver().setInTransportID(TranDocID);
            this.transports.get(TranDocID).getTransportTruck().setInTransportID(TranDocID);     //TODO:  take care of the Driver, and the Truck's Statuses
        } else if (status.equals(enumTranStatus.Canceled) || status.equals(enumTranStatus.Completed)) {
            this.transports.get(TranDocID).getTransportDriver().setInTransportID(-1);
            this.transports.get(TranDocID).getTransportTruck().setInTransportID(-1);
        }
        this.transports.get(TranDocID).setStatus(status);
    }














    public void setTransportTruck(int TranDocID, int truckNum) throws FileNotFoundException, ArrayIndexOutOfBoundsException, CommunicationException, FileAlreadyExistsException, CloneNotSupportedException {
        if(!transports.containsKey(TranDocID)){
            throw new FileNotFoundException("The Transport ID you have entered doesn't exist.");
        } else if (!this.truckFacade.getTrucksWareHouse().containsKey(truckNum)){
            throw new ArrayIndexOutOfBoundsException("The Truck number you have entered doesn't exist.");
        } else if (this.transports.get(TranDocID).getTransportTruck().getTruck_num() == truckNum) {
            throw new FileAlreadyExistsException("This Truck is already the Truck of this Transport");
        }

        /// Note:  a Truck cannot be in more than 1 active Transport

        Truck truck = this.truckFacade.getTrucksWareHouse().get(truckNum);  // the truck in contention to be set in the Transport
        if(truck.getInTransportID() != -1){    // so it belong to another Transport Active right now, We take care of the Transport's status in the setTransportStatus() function
            if (this.transports.get(TranDocID).getStatus() == enumTranStatus.InTransit || this.transports.get(TranDocID).getStatus() == enumTranStatus.BeingDelayed || this.transports.get(TranDocID).getStatus() == enumTranStatus.BeingAssembled) {
                throw new CloneNotSupportedException("The Transport you are trying to set to is Active and The Truck you are trying to set is already Occupied with another Active Transport right now");
            }
        }

        // if we got to here so the truck is not in an active transport OR/AND the Transport we are trying to set is not active
        // check NewTruck - Driver Compatability
        if (!this.transports.get(TranDocID).getTransportDriver().getLicenses().contains(truck.getValid_license())){
            throw new CommunicationException("The transport's driver doesn't have the fitting license for the new Truck you want to set.");
        }

        this.transports.get(TranDocID).setTransportTruck(truck);

        //TODO: also, DON'T UPDATE ANY STATUSES !!!!           <<<---------------------------------------     <<------------------------------
    }




    public void setTransportDriver(int TranDocID, int DriverID) throws FileNotFoundException, ArrayIndexOutOfBoundsException, FileAlreadyExistsException, CloneNotSupportedException, CommunicationException {
        if(!transports.containsKey(TranDocID)){
            throw new FileNotFoundException("The Transport ID you have entered doesn't exist.");
        } else if (!this.employeeFacade.getDrivers().containsKey(DriverID)){
            throw new ArrayIndexOutOfBoundsException("The Driver ID you have entered doesn't exist.");
        } else if (this.transports.get(TranDocID).getTransportDriver().getId() == DriverID) {
            throw new FileAlreadyExistsException("This Driver is already the Driver of this Transport");
        }

        /// Note:  a Driver cannot be in more than 1 active Transport

        Driver driver = this.employeeFacade.getDrivers().get(DriverID);  // the driver in contention to be set in the Transport
        if(driver.getInTransportID() != -1){    // so it belong to another Transport Active right now, We take care of the Transport's status in the setTransportStatus() function
            if (this.transports.get(TranDocID).getStatus() == enumTranStatus.InTransit || this.transports.get(TranDocID).getStatus() == enumTranStatus.BeingDelayed || this.transports.get(TranDocID).getStatus() == enumTranStatus.BeingAssembled) {
                throw new CloneNotSupportedException("The Transport you are trying to set to is Active and The Driver you are trying to set is already Occupied with another Active Transport right now");
            }
        }

        // if we got to here so the driver is not in an active transport OR/AND the Transport we are trying to set is not active
        // check Truck - NewDriver Compatability
        if (!driver.getLicenses().contains(this.transports.get(TranDocID).getTransportTruck().getValid_license())){
            throw new CommunicationException("The New Driver you are trying to set doesn't have the fitting license for the Truck that is in the Transport.");
        }

        this.transports.get(TranDocID).setTransportDriver(driver);

        //TODO: also, DON'T UPDATE ANY STATUSES !!!!           <<<---------------------------------------     <<------------------------------
    }






























    public void addTransportToWaitQueue(TransportDoc tempTransport){
        tempTransport.setTran_Doc_ID(this.transportIDCounter);
        this.transportIDCounter++;
        tempTransport.setStatus(enumTranStatus.Queued);
        this.queuedTransports.add(tempTransport);
    }





    public String checkTransportValidity(String DTO_OfTransport) throws JsonProcessingException {  ///  returns: "Valid", "BadLicenses", "overallWeight-truckMaxCarryWeight", "Queue"
        TransportDTO transport_DTO = this.objectMapper.readValue(DTO_OfTransport, TransportDTO.class);
        String res = "Valid";

        Driver driver = (Driver) this.employeeFacade.getEmployees().get(transport_DTO.getTransportDriverID());
        Truck truck = this.truckFacade.getTrucksWareHouse().get(transport_DTO.getTransportTruckNum());
        Site srcSite = this.siteFacade.getShippingAreas().get(transport_DTO.getSrc_site().getSiteAreaNum()).getSites().get(transport_DTO.getSrc_site().getAddressString());

        TransportDoc tempTransport = new TransportDoc(enumTranStatus.BeingAssembled, -1, truck, driver, srcSite);

        for (ItemsDocDTO itemsDocDTO : transport_DTO.getDests_Docs()){
            Site destSiteTemp = this.siteFacade.getShippingAreas().get(itemsDocDTO.getDest_siteDTO().getSiteAreaNum()).getSites().get(itemsDocDTO.getDest_siteDTO().getAddressString());
            String tempCName = destSiteTemp.getcName();
            long tempCNumber = destSiteTemp.getcNumber();
            tempTransport.addDestSite(itemsDocDTO.getItemsDoc_num(), destSiteTemp);     ///    parameters were switched      <<<----------------------------------

            for (ItemDTO itemDTO : itemsDocDTO.getItemDTOs().keySet()){
                tempTransport.addItem(itemsDocDTO.getItemsDoc_num(), itemDTO.getName(), itemDTO.getWeight(), itemsDocDTO.getItemDTOs().get(itemDTO), itemDTO.getCondition());
            }
        }    ///  adding every site and every item for each site

        int overallTransportWeight = tempTransport.calculateTransportItemsWeight();



        /// /////////////////////////////////    <<-------------------------------------   checking if there's a Driver-Truck Pairing At All

        ///  checking if there is a match at all
        boolean isThereMatchAtAllBetweenLicenses = false;
        for (Driver driv : this.employeeFacade.getDrivers().values()){
            for (String drivers_license : driv.getLicenses()){
                for (Truck truc : this.truckFacade.getTrucksWareHouse().values()){
                    if (truc.getValid_license().equals(drivers_license) && driv.getInTransportID() == -1 && truc.getInTransportID() == -1){
                        isThereMatchAtAllBetweenLicenses = true;  // if already found
                        break;
                    }
                }
                if (isThereMatchAtAllBetweenLicenses){break;}  // if already found
            }
            if (isThereMatchAtAllBetweenLicenses){break;}  // if already found
        }

        if(!isThereMatchAtAllBetweenLicenses){
            // send to Queue
            this.addTransportToWaitQueue(tempTransport);
            return "Queue";
            //TODO:  NOTE: it'll be a problem if when we can send it there will be a problem with the weight, so when checking if can go, then prompt the rePlanning
            //TODO:  NOTE: after checking with checkTransportValidity again.     (DO IN THE checkIfFirstQueuedTransportsCanGo FUNCTION BELOW !!!)
        }
        // else: continue to check other stuff


        /// /////////////////////////////////    <<-------------------------------------   checking if the Driver-Truck pairing is free





        //TODO   <<----------------------  add  "Occupied"
        add







        /// /////////////////////////////////    <<-------------------------------------   checking if the Driver-Truck pairing is compatible

        boolean driver_has_correct_license = false;
        for (String drivers_license : driver.getLicenses()){
            if (truck.getValid_license().equals(drivers_license) && driver.getInTransportID() == -1 && truck.getInTransportID() == -1){  // also checking if these driver are free
                driver_has_correct_license = true;                      //TODO split the if free ? and if compatible checks, and add another return string type "Occupied"
                break;
            }
        }
        if (!driver_has_correct_license){
            return "BadLicenses";
        }
        // else: continue to check another thing

        /// /////////////////////////////////    <<-------------------------------------   checking Overall Weight

        if (tempTransport.getTransportTruck().getMax_carry_weight() < overallTransportWeight){
            res = "" + overallTransportWeight + "-" + tempTransport.getTransportTruck().getMax_carry_weight();  // "overallWeight-truckMaxCarryWeight" format
        }

        return res;
    }













    public void checkIfFirstQueuedTransportsCanGo(){
        //TODO:  it'll be a problem if when we can send it there will be a problem with the weight, so when checking if can go, then prompt the rePlanning
        //TODO:  after checking with checkTransportValidity again.

        if(!this.queuedTransports.isEmpty()){
            if(this.checkTransportValidity(queuedTransports.get(0).getTran_Doc_ID()).equals("Valid")){  // build a DTO for the check
                queuedTransports.get(0).setDeparture_dt(LocalDateTime.now());
                //TODO: send the transport at index 0 and return to upper layers that this happened

            }
        }

        //TODO: if can be sent, then: change driver's and truck's isFrees and TransportDoc Status
        //TODO: also make the departure_datetime of the Transport up to date (to now).
        //TODO: also make the truck_depart_weight of the Transport up to date (with the calculation function I made).
    }














    public void addTransportProblem(int TransportID, int menu_Problem_option) throws FileNotFoundException, FileAlreadyExistsException {
        if (!this.transports.containsKey(TransportID)) {
            throw new FileNotFoundException("Transport ID doesn't exist.");
        }

        enumTranProblem probEnum = null;
        if(menu_Problem_option == 1){
            probEnum = enumTranProblem.Puncture;
        }else if (menu_Problem_option == 2){
            probEnum = enumTranProblem.HeavyTraffic;
        }else if (menu_Problem_option == 3){
            probEnum = enumTranProblem.RoadAccident;
        }else if (menu_Problem_option == 4){
            probEnum = enumTranProblem.UnresponsiveContact;
        }else if (menu_Problem_option == 5){
            probEnum = enumTranProblem.TruckVehicleProblem;
        }else if (menu_Problem_option == 6){
            probEnum = enumTranProblem.EmptyTruckGasTank;
        }

        if (this.transports.get(TransportID).addTransportProblem(probEnum) == -1){
            throw new FileAlreadyExistsException("The problem you entered already exists in this Transport");
        }
    }


    public void removeTransportProblem(int TransportID, int menu_Problem_option) throws FileNotFoundException, FileAlreadyExistsException {
        if (!this.transports.containsKey(TransportID)) {
            throw new FileNotFoundException("Transport ID doesn't exist.");
        }

        enumTranProblem probEnum = null;
        if(menu_Problem_option == 1){
            probEnum = enumTranProblem.Puncture;
        }else if (menu_Problem_option == 2){
            probEnum = enumTranProblem.HeavyTraffic;
        }else if (menu_Problem_option == 3){
            probEnum = enumTranProblem.RoadAccident;
        }else if (menu_Problem_option == 4){
            probEnum = enumTranProblem.UnresponsiveContact;
        }else if (menu_Problem_option == 5){
            probEnum = enumTranProblem.TruckVehicleProblem;
        }else if (menu_Problem_option == 6){
            probEnum = enumTranProblem.EmptyTruckGasTank;
        }

        if (this.transports.get(TransportID).removeTransportProblem(probEnum) == -1){
            throw new FileAlreadyExistsException("The problem you entered already doesn't exists in this Transport");
        }
    }

















    public void addDestSiteToTransport(int tran_ID, int itemsDoc_num, int destSiteArea, String destSiteAddress, String contName, long contNum) throws FileNotFoundException, FileAlreadyExistsException, CommunicationException {
        if (!transports.containsKey(tran_ID)) {
            throw new FileNotFoundException("The Transport ID you've entered doesn't exist.");
        } else if (this.itemsDocs.containsKey(itemsDoc_num)) {
            throw new FileAlreadyExistsException("The Site's Items Document Number you are trying to add already exists.");
        }

        ItemsDoc addition = this.transports.get(tran_ID).addDestSite(itemsDoc_num, new Site(new Address(destSiteArea, destSiteAddress), contName, contNum));
        if (addition == null){
            throw new CommunicationException("Destination Site already in this Transport, you can add items to that site instead.");
        }

        this.itemsDocs.put(itemsDoc_num, addition);
    }


    public void removeDestSiteFromTransport(int tran_ID, int itemsDoc_num) throws FileNotFoundException, CommunicationException, ClassNotFoundException {
        if (!transports.containsKey(tran_ID)) {
            throw new FileNotFoundException("The Transport ID you've entered doesn't exist.");
        } else if (!this.itemsDocs.containsKey(itemsDoc_num)) {
            throw new CommunicationException("The Site's Items Document Number you are trying to remove doesn't exist in the system.");
        }

        if (this.transports.get(tran_ID).removeDestSite(itemsDoc_num) == -1){ throw new ClassNotFoundException("The Site's Items Document Number is not in that Transport"); }
        this.itemsDocs.remove(itemsDoc_num);
    }




    public void setSiteArrivalIndexInTransport(int transportID, int siteArea, String siteAddress, int index) throws FileNotFoundException, ClassNotFoundException, AbstractMethodError {
        if (!this.transports.containsKey(transportID)){
            throw new FileNotFoundException("The transport ID given was not found");
        }

        boolean siteResidesInTransport = false;
        ItemsDoc itemsDocToMove = null;
        for (ItemsDoc itemsDoc : this.transports.get(transportID).getDests_Docs()){
            if (itemsDoc.getDest_site().getAddress().getArea() == siteArea && itemsDoc.getDest_site().getAddress().getAddress().equals(siteAddress)){
                siteResidesInTransport = true;
                itemsDocToMove = itemsDoc;
            }
        }

        if(!siteResidesInTransport){
            throw new ClassNotFoundException("Site not found inside of that transport");
        } else if (index > this.transports.get(transportID).getDests_Docs().size()) {   // if the index is valid    //  index should be 1, 2, ....
            throw new AbstractMethodError("The Index entered is bigger than the amount of sites in the Transport, so can't put that site in that bigger index");
        }

        this.transports.get(transportID).setSiteArrivalIndexInTransport(siteArea, siteAddress, index);
    }




    public void changeAnItemsDocNum(int oldItemsDocNum, int newItemsDocNum) throws FileNotFoundException, KeyAlreadyExistsException {
        if (!this.itemsDocs.containsKey(oldItemsDocNum)) {
            throw new FileNotFoundException();
        } else if (this.itemsDocs.containsKey(newItemsDocNum)) {
            throw new KeyAlreadyExistsException();
        }
        ItemsDoc temp = this.itemsDocs.get(oldItemsDocNum);
        this.itemsDocs.put(newItemsDocNum, temp);
        this.itemsDocs.get(newItemsDocNum).setItemDoc_num(newItemsDocNum);
        this.itemsDocs.remove(oldItemsDocNum);
        /// bonus function as well
    }




    public boolean checkValidItemsDocID(int currItemsDocNum) {
        if (this.itemsDocs.containsKey(currItemsDocNum)) {
            return false;
        }
        return true;
    }



























    public void addItem(int itemsDoc_num, String itemName, int itemWeight, int amount, boolean cond) throws FileNotFoundException {
        if (!this.itemsDocs.containsKey(itemsDoc_num)) { throw new FileNotFoundException("Item's Document ID not found"); }

        int res = this.itemsDocs.get(itemsDoc_num).addItem(itemName, itemWeight, cond, amount);
        //Note: Right now there is no other error when adding an Item
    }

    public void removeItem(int itemsDoc_num, String itemName, int itemWeight, int amount, boolean cond) throws FileNotFoundException, ClassNotFoundException {
        if (!this.itemsDocs.containsKey(itemsDoc_num)) { throw new FileNotFoundException("Item's Document ID not found"); }

        int res = this.itemsDocs.get(itemsDoc_num).removeItem(itemName, itemWeight, cond, amount);
        if (res == -1){
            throw new ClassNotFoundException("Item to remove not found in that Items Document");
        }
    }

    public void setItemCond(int itemsDoc_num, String itemName, int itemWeight, int amount, boolean newCond) throws FileNotFoundException, ClassNotFoundException {
        if (!this.itemsDocs.containsKey(itemsDoc_num)) { throw new FileNotFoundException("Item's Document ID not found"); }

        int res = this.itemsDocs.get(itemsDoc_num).setItemCond(itemName, itemWeight, amount, newCond);
        if (res == -1){
            throw new ClassNotFoundException("Item to change condition to was not found in that Items Document");
        }
    }










    public String showAllQueuedTransports() {
        String resOfQueuedTransports = "Queued Transports (Values of Drivers/Trucks/Time in the Queued Transports are not final, these are the values set when first trying to create the Transport):\n";
        for (TransportDoc transportDoc : this.queuedTransports) {
            resOfQueuedTransports += transportDoc.toString() + "\n";
        }
        resOfQueuedTransports += "\n";
        return resOfQueuedTransports;
    }


    public String showAllTransports(){
        String resOfAllTransports = "All Transports:\n";
        for (TransportDoc t : transports.values()){
            resOfAllTransports += t.toString() + "\n";
        }
        resOfAllTransports += "Queued Transports (Values of Drivers/Trucks/Time in the Queued Transports are not final, these are the values set when first trying to create the Transport):\n";
        for (TransportDoc t : this.queuedTransports){
            resOfAllTransports += t.toString() + "\n";
        }
        resOfAllTransports += "\n";
        return resOfAllTransports;
    }


}
