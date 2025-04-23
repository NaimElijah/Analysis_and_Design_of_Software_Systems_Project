package DomainLayer.TranSubModule;

import DomainLayer.EmpSubModule.Driver;
import DomainLayer.EmpSubModule.Employee;
import DomainLayer.EmpSubModule.EmployeeFacade;
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
    private HashMap<Integer, ItemsDoc> itemsDocs;  // to know a ItemsDoc's num is unique and also for connection.
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

        TransportDTO transport_DTO = this.objectMapper.readValue(DTO_OfTransport, TransportDTO.class);
        LocalDateTime now = LocalDateTime.now();

        //TODO    <<<<----------------------------------------   CONTINUE FROM HERE in the facade after DOING THE MENUS IN THE PRESENTATION LAYER !!!!!!   <<-----------

        //TODO

//        ///TODO:  check if added some ItemsDocs are for the same Site and add them up together so there won't be duplicates for the same site.
//        for(ItemsDocDTO itemsDocDTO : dests_Docs_for_Transport){
//
//        }

        //TODO: change driver's and truck's isFrees and TransportDoc Status
        //TODO: also make the datetime of departure up to date (to now).
    }





    public void deleteTransport(int transportID) throws FileNotFoundException {
        if(!transports.containsKey(transportID)){  //TODO also check the queuedTransports  !!!
            throw new FileNotFoundException();
        }
        //TODO also check the queuedTransports  !!!
        //TODO: delete everything inside of the transport and change statuses of what's needed, driver, truck
        transports.remove(transportID);
        //TODO
    }







    public void setTransportStatus(int TranDocID, int menu_status_option) throws FileNotFoundException, FileAlreadyExistsException, ClassNotFoundException {
        if(!transports.containsKey(TranDocID)){
            throw new FileNotFoundException("The Transport ID you have entered doesn't exist.");
        }
        enumTranStatus status = enumTranStatus.Else;
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
            status = enumTranStatus.Delayed;
        }else if (menu_status_option == 7) {
            status = enumTranStatus.Else;
        }
        if (transports.get(TranDocID).getProblems().contains(status)) {
            throw new FileAlreadyExistsException("The status you are trying to set already is the status of this Transport");
        }
        if(status.equals(enumTranStatus.InTransit)){
            if(this.transports.get(TranDocID).getStatus() == enumTranStatus.Canceled || this.transports.get(TranDocID).getStatus() == enumTranStatus.Completed){
                if((!this.transports.get(TranDocID).getTransportTruck().isFree()) || (!this.transports.get(TranDocID).getTransportDriver().isFree())){
                    throw new ClassNotFoundException("cannot change status to InTransit because the driver or the truck aren't free, maybe change the, and try again.");
                }
            }
            this.transports.get(TranDocID).getTransportDriver().setFree(false);
            this.transports.get(TranDocID).getTransportTruck().setFree(false);
        } else if (status.equals(enumTranStatus.Canceled) || status.equals(enumTranStatus.Completed)) {
            this.transports.get(TranDocID).getTransportDriver().setFree(true);
            this.transports.get(TranDocID).getTransportTruck().setFree(true);
        }
        this.transports.get(TranDocID).setStatus(status);
    }





    public void setTransportTruck(int TranDocID, int truckNum) throws FileNotFoundException, ArrayIndexOutOfBoundsException, CommunicationException {
        if(!transports.containsKey(TranDocID)){
            throw new FileNotFoundException("The Transport ID you have entered doesn't exist.");
        } else if (!this.truckFacade.getTrucksWareHouse().containsKey(truckNum)){
            throw new ArrayIndexOutOfBoundsException("The Truck number you have entered doesn't exist.");
        }

        Truck tNEW = truckFacade.getTrucksWareHouse().get(truckNum);  // the new truck we want to set
        if (transports.get(TranDocID).getTransportTruck() != null && tNEW.isFree()) {
            transports.get(TranDocID).getTransportTruck().setFree(true);  // if there was a truck and we're setting another truck, free the before truck
        } else if (!tNEW.isFree()) {

        }

        this.transports.get(TranDocID).setTransportTruck(tNEW);
        this.transports.get(TranDocID).getTransportTruck().setFree(false);  // taking the new truck

        // availability issues
        throw new AbstractMethodError("There are availability issues ");

        // Driver - Truck Compatability
        throw new CommunicationException("The transport's driver doesn't have the fitting license for the new Truck you want to set.");
        // TODO  and then duplicate same structure to the setTransportDriver Function    <<<<-----------------------------------------------------  TODO
    }




    public void setTransportDriver(int TranDocID, int DriverID) throws FileNotFoundException, ArrayIndexOutOfBoundsException {
        if(!transports.containsKey(TranDocID)){
            throw new FileNotFoundException("The Transport ID you have entered doesn't exist.");
        } else if (!this.employeeFacade.getDrivers().containsKey(DriverID)){
            throw new ArrayIndexOutOfBoundsException("The Driver ID you have entered doesn't exist.");
        }

        // TODO
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
            tempTransport.addDestSite(destSiteTemp, itemsDocDTO.getItemsDoc_num());

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
                    if (truc.getValid_license().equals(drivers_license) && driv.isFree() && truc.isFree()){
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


        /// /////////////////////////////////    <<-------------------------------------   checking if the Driver-Truck pairing is compatible

        boolean driver_has_correct_license = false;
        for (String drivers_license : driver.getLicenses()){
            if (truck.getValid_license().equals(drivers_license) && driver.isFree() && truck.isFree()){
                driver_has_correct_license = true;
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
        //TODO: also make the datetime of departure up to date (to now).
    }














    public void setSiteArrivalIndexInTransport(int transportID, int siteArea, String siteAddress, int index) throws FileNotFoundException, ClassNotFoundException {
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
            throw new ClassNotFoundException("Site not found inside that transport");
        } else if (index >= this.transports.get(transportID).getDests_Docs().size()) {
            throw new AbstractMethodError("the Index entered is bigger than the bounds");
        }

        this.transports.get(transportID).getDests_Docs().remove(itemsDocToMove);

        //TODO
    }








    public boolean checkValidItemsDocID(int currItemsDocNum) {
        if (this.itemsDocs.containsKey(currItemsDocNum)) {
            return false;
        }
        return true;
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






    public void addTransportProblem(int TransportID, int menu_Problem_option) throws FileNotFoundException {
        if (!this.transports.containsKey(TransportID)) {
            throw new FileNotFoundException();
        }
        if ()
        //TODO
    }

    public void removeTransportProblem(int TransportID, int menu_Problem_option) throws FileNotFoundException {
        if (!this.transports.containsKey(TransportID)) {
            throw new FileNotFoundException();
        }
        if ()
        //TODO
    }







    // TODO: (throw msg exce if in new Area and give option)
    public void addDestSiteToTransport(int tran_ID, int itemsDoc_num, int destSiteArea, String destSiteAddress) {  // TODO: (throw msg exce if in new Area and give option)
        //TODO:  use site facade to send the Site downwards
        //TODO:  also add site/itemsDoc to hashmap here
    }

    public void removeDestSiteFromTransport(int tran_ID, int itemsDoc_num){
        //TODO:  also remove site/itemsDoc from hashmap here
    }








    public void addItem(int itemsDoc_num, String itemName, int itemWeight, int amount, boolean cond){
        int res = this.itemsDocs.get(itemsDoc_num).addItem(itemName, itemWeight, cond, amount);
        //TODO according to the return value  --->  throw
    }

    public void removeItem(int itemsDoc_num, String ItemName, int itemWeight, int amount, boolean cond){
        int res = this.itemsDocs.get(itemsDoc_num).removeItem(ItemName, itemWeight, cond, amount);
        //TODO according to the return value  --->  throw
    }

    public void setItemCond(int itemsDoc_num, String ItemName, int itemWeight, int amount, boolean newCond){
        boolean res = this.itemsDocs.get(itemsDoc_num).setItemCond(ItemName, itemWeight, amount, newCond);
        //TODO according to the return value  --->  throw
    }








    public String showAllTransports(){
        String resOfAllTransports = "All Transports:\n";
        for (TransportDoc t : transports.values()){
            resOfAllTransports += t.toString() + "\n";
        }
        resOfAllTransports += "Queued Transports (Values of Drivers/Trucks in the Queued Transports are not final):\n";
        for (TransportDoc t : this.queuedTransports){
            resOfAllTransports += t.toString() + "\n";
        }
        resOfAllTransports += "\n";
        return resOfAllTransports;
    }




}
