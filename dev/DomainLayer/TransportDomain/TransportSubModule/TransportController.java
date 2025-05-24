package DomainLayer.TransportDomain.TransportSubModule;

import DTOs.TransportModuleDTOs.*;
import DomainLayer.TransportDomain.SiteSubModule.Address;
import DomainLayer.TransportDomain.SiteSubModule.Site;
import DomainLayer.TransportDomain.SiteSubModule.SiteFacade;
import DomainLayer.TransportDomain.TruckSubModule.Truck;
import DomainLayer.TransportDomain.TruckSubModule.TruckFacade;
import DomainLayer.enums.enumDriLicense;
import DomainLayer.enums.enumTranProblem;
import DomainLayer.enums.enumTranStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.management.AttributeNotFoundException;
import javax.management.openmbean.KeyAlreadyExistsException;
import javax.naming.CommunicationException;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;

public class TransportController {
    private HashMap<Integer, TransportDoc> transports;
    private int transportIDCounter;
    private HashMap<Integer, ItemsDoc> itemsDocs; // to know an ItemsDoc's num is unique like required, and to address an ItemsDoc specifiically.
    private ArrayList<TransportDoc> queuedTransports;
    private HashMap<Long, Integer> driverIdToInTransportID;  //  employee is added to here if he is an active driver in an active Transport.

    private SiteFacade siteFacade;
    private TruckFacade truckFacade;

    private ObjectMapper objectMapper;

    public TransportController(SiteFacade sF, TruckFacade tF) {
        this.transportIDCounter = 0;
        this.objectMapper = new ObjectMapper();
        this.transports = new HashMap<Integer, TransportDoc>();
        this.itemsDocs = new HashMap<Integer, ItemsDoc>();
        this.queuedTransports = new ArrayList<TransportDoc>();
        this.driverIdToInTransportID = new HashMap<>();
        this.siteFacade = sF;
        this.truckFacade = tF;
    }

    public HashMap<Integer, TransportDoc> getTransports() {return transports;}
    public void setTransports(HashMap<Integer, TransportDoc> transports) {this.transports = transports;}
    public HashMap<Integer, ItemsDoc> getItemsDocs() {return itemsDocs;}
    public void setItemsDocs(HashMap<Integer, ItemsDoc> itemsDocs) {this.itemsDocs = itemsDocs;}
    public ArrayList<TransportDoc> getQueuedTransports() {return queuedTransports;}
    public void setQueuedTransports(ArrayList<TransportDoc> queuedTransports) {this.queuedTransports = queuedTransports;}

    public void createTransport(String DTO_OfTransport, int queuedIndexIfWasQueued) throws JsonProcessingException {  // time is decided when the Transport departs
        ///  NOTE: I already did all of the checks beforehand, so if we get to here, then we can successfully and legitimately create the Transport

        TransportDTO transport_DTO = this.objectMapper.readValue(DTO_OfTransport, TransportDTO.class);
        long driverId = transport_DTO.getTransportDriverID();  ///  NEW
        Truck truck = this.truckFacade.getTrucksWareHouse().get(transport_DTO.getTransportTruckNum());

        /// finding the srcSite
        Site srcSite = null;
        for (Site site : this.siteFacade.getShippingAreas().get(transport_DTO.getSrc_site().getSiteAreaNum()).getSites().values()){
            if (site.getAddress().getArea() == transport_DTO.getSrc_site().getSiteAreaNum() && site.getAddress().getAddress().equals(transport_DTO.getSrc_site().getSiteAddressString())){
                srcSite = site;
            }
        }

        int tra_id = -98; // just init
        if (queuedIndexIfWasQueued != -100){   // if was queued
            tra_id = this.queuedTransports.get(queuedIndexIfWasQueued-1).getTran_Doc_ID();  // getting his already allocated Transport ID
            this.queuedTransports.remove(queuedIndexIfWasQueued - 1);   // removing him from the queue
        } else {   // if new Transport
            this.transportIDCounter++;
            tra_id = this.transportIDCounter;
        }

        TransportDoc newTransportBeingCreated = new TransportDoc(enumTranStatus.BeingAssembled, tra_id, truck, driverId, srcSite, transport_DTO.getDeparture_dt());

        /// add the ItemsDocs and the Items that should be in them from the itemsDocDTOs:
        for (ItemsDocDTO itemsDocDTO : transport_DTO.getDests_Docs()){
            /// finding the current destSite
            Site destSite = this.siteFacade.getShippingAreas().get(itemsDocDTO.getDest_siteDTO().getSiteAreaNum()).getSites().get(itemsDocDTO.getDest_siteDTO().getSiteAddressString());

            ItemsDoc addition = newTransportBeingCreated.addDestSite(itemsDocDTO.getItemsDoc_num(), destSite);

            for (ItemQuantityDTO itemQuantityDTO : itemsDocDTO.getItemQuantityDTOs()){  /// add each item of that site from the DTO's data
                newTransportBeingCreated.addItem(itemsDocDTO.getItemsDoc_num(), itemQuantityDTO.getItem().getName(), itemQuantityDTO.getItem().getWeight(), itemQuantityDTO.getQuantity(), itemQuantityDTO.getItem().getCondition());
            }
        }

        /// finishing touches before adding Transport
        newTransportBeingCreated.setStatus(enumTranStatus.InTransit);
        newTransportBeingCreated.getTransportTruck().setInTransportID(newTransportBeingCreated.getTran_Doc_ID());
        this.driverIdToInTransportID.put(driverId, newTransportBeingCreated.getTran_Doc_ID());   ///  NEW
        newTransportBeingCreated.setTruck_Depart_Weight(newTransportBeingCreated.calculateTransportItemsWeight());
        newTransportBeingCreated.setDeparture_dt(LocalDateTime.now());  //  the time is set already in the constructor of the Transport, but just to be accurate :)
        newTransportBeingCreated.calculateItemsDocsArrivalTimesInTransport();

        for (ItemsDoc itemsDoc : newTransportBeingCreated.getDests_Docs()){
            this.itemsDocs.put(itemsDoc.getItemDoc_num(), itemsDoc); // if this is a queued transport, the ItemsDocs will override themselves so ok.
        }     //TODO: if this doesn't work well with a queued transport being sent then just do this "for" if it's a new transport.
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

        if (this.driverIdToInTransportID.containsKey(toRemoveDoc.getTransportDriverId())){
            if (this.driverIdToInTransportID.get(toRemoveDoc.getTransportDriverId()) == toRemoveDoc.getTran_Doc_ID()){
                this.driverIdToInTransportID.remove(toRemoveDoc.getTransportDriverId());
            }
        }
        if (toRemoveDoc.getTransportTruck().getInTransportID() == toRemoveDoc.getTran_Doc_ID()){
            toRemoveDoc.getTransportTruck().setInTransportID(-1);  //  releasing the Truck if it's with this Transport
        }
        toRemoveDoc.setStatus(enumTranStatus.Canceled);

        if (containedInQueued){
            this.queuedTransports.remove(indexOfTransport);
        } else {
            transports.remove(transportID);
        }
    }





    public void setTransportStatus(int TranDocID, int menu_status_option, boolean isActiveHelper) throws FileNotFoundException, FileAlreadyExistsException, CommunicationException, CloneNotSupportedException, IndexOutOfBoundsException {
        enumTranStatus newStatus = null;
        if (menu_status_option == 1){
            newStatus = enumTranStatus.BeingAssembled;
        } else if (menu_status_option == 2) {
            newStatus = enumTranStatus.Queued;
        } else if (menu_status_option == 3) {
            newStatus = enumTranStatus.InTransit;
        } else if (menu_status_option == 4) {
            newStatus = enumTranStatus.Completed;
        } else if (menu_status_option == 5) {
            newStatus = enumTranStatus.Canceled;
        } else if (menu_status_option == 6) {
            newStatus = enumTranStatus.BeingDelayed;
        }

        for (TransportDoc queuedTransportDoc : this.queuedTransports) {
            if (queuedTransportDoc.getTran_Doc_ID() == TranDocID){
                if (newStatus != enumTranStatus.Queued && newStatus != enumTranStatus.Canceled){
                    throw new StringIndexOutOfBoundsException("You cannot set a queued Transport's status as something other than Queued or Canceled.");
                } else {
                    queuedTransportDoc.setStatus(newStatus);
                }
                return;
            }
        }

        if(!transports.containsKey(TranDocID)){ throw new FileNotFoundException("The Transport ID you have entered doesn't exist in the Transports."); }
        TransportDoc transport = transports.get(TranDocID);

        enumTranStatus currStatus = transports.get(TranDocID).getStatus();

        if (currStatus == newStatus) { throw new FileAlreadyExistsException("The status you are trying to set already is the status of this Transport"); }


        ///   scenario 1
        if (currStatus == enumTranStatus.Canceled || currStatus == enumTranStatus.Completed || currStatus == enumTranStatus.Queued) {  // if currStatus is Not Active
            if (newStatus == enumTranStatus.BeingDelayed || newStatus == enumTranStatus.BeingAssembled || newStatus == enumTranStatus.InTransit) {  // if newStatus is Active

                // if the Truck or/and the Driver have been deleted // keep here, just use send down the this.employeeController.isActive(transport.getTransportDriverId()) var and use here.
                if (!isActiveHelper || transport.getTransportTruck().getIsDeleted()) {
                    throw new IndexOutOfBoundsException("the Truck or/and Driver of this Transport have been Deleted, you can view available Trucks or/and Drivers using the menu and set appropriately");
                }

                if (this.driverIdToInTransportID.containsKey(transport.getTransportDriverId())) {
                    if (this.driverIdToInTransportID.get(transport.getTransportDriverId()) != TranDocID){  // if it belongs to another Transport
                        TransportDoc otherTransport = transports.get(this.driverIdToInTransportID.get(transport.getTransportDriverId()));
                        if (otherTransport.getStatus() == enumTranStatus.BeingDelayed || otherTransport.getStatus() == enumTranStatus.BeingAssembled || otherTransport.getStatus() == enumTranStatus.InTransit) {  // if the other Transport is Active
                            throw new CommunicationException("cannot change Transport Status because it wants to change to an active one, but the Driver is already active in another Transport.");
                        }
                    }
                }

                if (transport.getTransportTruck().getInTransportID() != -1 && transport.getTransportTruck().getInTransportID() != TranDocID) {  // if it belongs to another Transport
                    TransportDoc otherTransport = transports.get(transport.getTransportTruck().getInTransportID());
                    if (otherTransport.getStatus() == enumTranStatus.BeingDelayed || otherTransport.getStatus() == enumTranStatus.BeingAssembled || otherTransport.getStatus() == enumTranStatus.InTransit) {  // if the other Transport is Active
                        throw new CloneNotSupportedException("cannot change Transport Status because it wants to change to an active one, but the Truck is already active in another Transport.");
                    }
                }

                // if got to here in this case (the first 2 outer if's), then we can make these actions:
                transport.getTransportTruck().setInTransportID(TranDocID);
                this.driverIdToInTransportID.put(transport.getTransportDriverId(), TranDocID);
                transport.setStatus(newStatus);
                return;
            }
        }

        //   seems that also checking if the Driver is an eligible site for him to drive that Transport's Truck is not necessary becasue when we set the transport we check this.

        /// scenario 2 & 3
        if (newStatus == enumTranStatus.Canceled || newStatus == enumTranStatus.Completed || newStatus == enumTranStatus.Queued) {  // if newStatus is Not Active
            if (currStatus == enumTranStatus.BeingDelayed || currStatus == enumTranStatus.BeingAssembled || currStatus == enumTranStatus.InTransit) {  // if currStatus is Active
                if (this.driverIdToInTransportID.containsKey(transport.getTransportDriverId())) {
                    if (this.driverIdToInTransportID.get(transport.getTransportDriverId()) == TranDocID) {   // if he is active in the transport
                        this.driverIdToInTransportID.remove(transport.getTransportDriverId());  //  release him
                    }
                }
                if (transport.getTransportTruck().getInTransportID() == TranDocID) {   // if it is active in the transport
                    transport.getTransportTruck().setInTransportID(-1);  //  release it
                }
            }
        }

        transport.setStatus(newStatus);  //  if it wants to change from Active to Active OR from Non Active to Non Active.
    }














    public void setTransportTruck(int TranDocID, int truckNum, boolean hasRoleHelper) throws FileNotFoundException, ArrayIndexOutOfBoundsException, CommunicationException, FileAlreadyExistsException, CloneNotSupportedException, AbstractMethodError, ClassNotFoundException {
        // check NewTruck - Driver Licenses Compatability
        if (!hasRoleHelper){
            throw new CommunicationException("The transport's driver doesn't have the fitting license for the new Truck you want to set.");
        } else if (!this.truckFacade.getTrucksWareHouse().containsKey(truckNum)){
            throw new ArrayIndexOutOfBoundsException("The Truck number you have entered doesn't exist.");
        } else if (truckFacade.getTrucksWareHouse().get(truckNum).getIsDeleted()) {   // if the Truck has been deleted
            throw new ClassNotFoundException("the Truck of this Transport have been Deleted, you can view available Trucks using the menu and set appropriately");
        }
        Truck truck = this.truckFacade.getTrucksWareHouse().get(truckNum);  // the truck in contention to be set in the Transport

        for (TransportDoc queuedTransportDoc : this.queuedTransports) {   // checking the queued Transports first
            if (TranDocID == queuedTransportDoc.getTran_Doc_ID()){
                //  also CHECK IF THE TRUCK CAN CARRY THE WEIGHT OF THAT TRANSPORT   <<-----------------------
                if (queuedTransportDoc.calculateTransportItemsWeight() > truck.getMax_carry_weight()){
                    throw new AbstractMethodError("The Truck you are trying to set to this Transport can't carry this Transport's Weight.");
                }
                queuedTransportDoc.setTransportTruck(truck);
                return;
            }
        }
        if(!transports.containsKey(TranDocID)){
            throw new FileNotFoundException("The Transport ID you have entered doesn't exist.");
        } else if (this.transports.get(TranDocID).getTransportTruck().getTruck_num() == truckNum) {
            throw new FileAlreadyExistsException("This Truck is already the Truck of this Transport");
        }

        /// Note:  a Truck cannot be in more than 1 active Transport

        if(truck.getInTransportID() != -1){    // so it belong to another Transport Active right now, ----->>  We take care of the Transport's status in the setTransportStatus() function
            if (this.transports.get(TranDocID).getStatus() == enumTranStatus.InTransit || this.transports.get(TranDocID).getStatus() == enumTranStatus.BeingDelayed || this.transports.get(TranDocID).getStatus() == enumTranStatus.BeingAssembled) {
                throw new CloneNotSupportedException("The Transport you are trying to set to is Active and The Truck you are trying to set is already Occupied with another Active Transport right now");
            }
        }
        // if we got to here so the truck is not in an active transport OR/AND the Transport we are trying to set is not active

        //  CHECK IF THE TRUCK CAN CARRY THE WEIGHT OF THAT TRANSPORT   <<-----------------------
        if (this.transports.get(TranDocID).calculateTransportItemsWeight() > truck.getMax_carry_weight()){
            throw new AbstractMethodError("The Truck you are trying to set to this Transport can't carry this Transport's Weight.");
        }

        this.transports.get(TranDocID).setTransportTruck(truck);  // more logic inside this function
    }




    public void setTransportDriver(int TranDocID, long DriverID, boolean isNotDriver, boolean isActive, boolean hasRole) throws FileNotFoundException, ArrayIndexOutOfBoundsException, FileAlreadyExistsException, CloneNotSupportedException, CommunicationException, ClassNotFoundException {
        if (isNotDriver){
            throw new ArrayIndexOutOfBoundsException("The Driver ID you have entered doesn't exist.");
        } else if (!isActive) {   // if the Driver has been deleted
            throw new ClassNotFoundException("the Driver of this Transport have been Deleted, you can view available Drivers using the menu and set appropriately");
        } else if (!hasRole){   // Check Truck - NewDriver Licenses Compatability
            throw new CommunicationException("The New Driver you are trying to set doesn't have the fitting license for the Truck that is in the Transport.");
        }

        for (TransportDoc queuedTransportDoc : this.queuedTransports) {   // checking the queued Transports first
            if (TranDocID == queuedTransportDoc.getTran_Doc_ID()){
                queuedTransportDoc.setTransportDriverId(DriverID);
                return;
            }
        }

        if(!transports.containsKey(TranDocID)){
            throw new FileNotFoundException("The Transport ID you have entered doesn't exist.");
        } else if (this.transports.get(TranDocID).getTransportDriverId() == DriverID) {
            throw new FileAlreadyExistsException("This Driver is already the Driver of this Transport");
        }

        // Note:  a Driver cannot be in more than 1 active Transport
        if(this.driverIdToInTransportID.containsKey(DriverID)){    // so it belong to another Transport Active right now, We take care of the Transport's status in the setTransportStatus() function
            if (this.transports.get(TranDocID).getStatus() == enumTranStatus.InTransit || this.transports.get(TranDocID).getStatus() == enumTranStatus.BeingDelayed || this.transports.get(TranDocID).getStatus() == enumTranStatus.BeingAssembled) {
                throw new CloneNotSupportedException("The Transport you are trying to set to is Active and The Driver you are trying to set is already Occupied with another Active Transport right now");
            }
        }
        // if we got to here so the driver is not in an active transport OR/AND the Transport we are trying to set is not active

        long DriverIDToRemoveFromTran = -1;
        for (long driverID : this.driverIdToInTransportID.keySet()){ // changes the old driver to InTransportID = -1
            if (this.driverIdToInTransportID.get(driverID) == TranDocID){ DriverIDToRemoveFromTran = driverID; break; }
        }
        if (DriverIDToRemoveFromTran != -1){ this.driverIdToInTransportID.remove(DriverIDToRemoveFromTran); }

        this.transports.get(TranDocID).setTransportDriverId(DriverID);

        if (this.transports.get(TranDocID).getStatus() == enumTranStatus.InTransit || this.transports.get(TranDocID).getStatus() == enumTranStatus.BeingAssembled || this.transports.get(TranDocID).getStatus() == enumTranStatus.BeingDelayed) {
            this.driverIdToInTransportID.put(DriverID, TranDocID);   ///  only if the current Transport is active
        }
    }


    public HashMap<Long, Integer> getDriverIdToInTransportID() {return driverIdToInTransportID;}
    public TruckFacade getTruckFacade() {return truckFacade;}


    public void isTruckDriverPairingGood(int truckNum, long driverID, boolean isNotDriver, boolean hasRole22) throws FileNotFoundException, ArrayIndexOutOfBoundsException, ClassNotFoundException, CloneNotSupportedException, CommunicationException {
        if (!this.truckFacade.getTrucksWareHouse().containsKey(truckNum)) {
            throw new FileNotFoundException("Truck Number entered doesn't exist");
        } else if (isNotDriver){
            throw new ArrayIndexOutOfBoundsException("The Driver ID you have entered doesn't exist");
        } else if (this.truckFacade.getTrucksWareHouse().get(truckNum).getInTransportID() != -1){
            throw new CloneNotSupportedException("The Truck you chose is partaking in another Active Transport right now");
        }
        // check if the driver has a license matching the truck's license
        if (!hasRole22){
            throw new CommunicationException("The Driver you chose doesn't have the fitting license for the Truck you chose");
        }
    }



    public String getTruckLicenseAsStringRole(int trucknum){
        String lice = "";
        enumDriLicense lic = this.truckFacade.getTrucksWareHouse().get(trucknum).getValid_license();
        if (lic.equals(enumDriLicense.A)){
            lice = "DriverA";
        } else if (lic.equals(enumDriLicense.B)){
            lice = "DriverB";
        } else if (lic.equals(enumDriLicense.C)){
            lice = "DriverC";
        } else if (lic.equals(enumDriLicense.D)){
            lice = "DriverD";
        } else if (lic.equals(enumDriLicense.E)){
            lice = "DriverE";
        }
        return lice;
    }





    public void addTransportToWaitQueue(TransportDoc tempTransport){
        if (tempTransport.getTran_Doc_ID() == -99){  // so it will add only new ones, not ones that have gotten checked again and were already added to the queue
            this.transportIDCounter++;
            tempTransport.setTran_Doc_ID(this.transportIDCounter);
            tempTransport.setStatus(enumTranStatus.Queued);
            for (ItemsDoc itemsDoc : tempTransport.getDests_Docs()){
                this.itemsDocs.put(itemsDoc.getItemDoc_num(), itemsDoc);
            }
            this.queuedTransports.add(tempTransport);
        }
    }



    public void addFromTransportDTOStringToWaitQueue(String DTO_OfTransport) throws JsonProcessingException {
        TransportDTO transport_DTO = this.objectMapper.readValue(DTO_OfTransport, TransportDTO.class);
        TransportDoc tempTransport = convertTransportDTOToTransportDoc(transport_DTO);
        this.addTransportToWaitQueue(tempTransport);
    }






    public String checkTransportValidity(String DTO_OfTransport, boolean hasRole11, boolean isThereMatchAtAllBetweenLicenses) throws JsonProcessingException {  ///  returns: "Valid", "BadLicenses", "overallWeight-truckMaxCarryWeight", "Queue", "Occupied"
        TransportDTO transport_DTO = this.objectMapper.readValue(DTO_OfTransport, TransportDTO.class);
        TransportDoc tempTransport = convertTransportDTOToTransportDoc(transport_DTO);
        String res = "Valid";

        if(!isThereMatchAtAllBetweenLicenses){
            this.addTransportToWaitQueue(tempTransport);
            return "Queue";
        }
        // else: continue to check other stuff
        /// /////////////////////////////////    <<-------------------------------------   checking if the Driver-Truck pairing is compatible
        if (!hasRole11){
            return "BadLicenses";
        }
        // else: continue to check another thing
        /// /////////////////////////////////    <<-------------------------------------   checking if the Driver-Truck pairing are both free
        if (isDriverActive(tempTransport.getTransportDriverId())){ return "Occupied"; }
        if (isTruckActive(tempTransport.getTransportTruck().getTruck_num())){ return "Occupied"; }
        // else: continue to check another thing
        /// /////////////////////////////////    <<-------------------------------------   checking Overall Weight

        double overallTransportWeight = tempTransport.calculateTransportItemsWeight();
        if (tempTransport.getTransportTruck().getMax_carry_weight() < overallTransportWeight){
            res = "" + overallTransportWeight + "-" + tempTransport.getTransportTruck().getMax_carry_weight();  // "overallWeight-truckMaxCarryWeight" format
        }
        return res;
    }




    public boolean isDriverActive(long driverID){
        if (this.driverIdToInTransportID.containsKey(driverID)) {  // if driver is in another transport
            TransportDoc otherTransport = transports.get(this.driverIdToInTransportID.get(driverID));
            if(otherTransport.getStatus() == enumTranStatus.BeingAssembled || otherTransport.getStatus() == enumTranStatus.InTransit || otherTransport.getStatus() == enumTranStatus.BeingDelayed){  // if other Transport is Active
                return true;   // because the driver is Occupied in another Active Transport
            }
        }
        return false;
    }


    public boolean isTruckActive(int truck_num){
        Truck truck = this.truckFacade.getTrucksWareHouse().get(truck_num);
        if (truck.getInTransportID() != -1) {  // if truck is in another transport
            TransportDoc otherTransport = transports.get(truck.getInTransportID());
            if(otherTransport.getStatus() == enumTranStatus.BeingAssembled || otherTransport.getStatus() == enumTranStatus.InTransit || otherTransport.getStatus() == enumTranStatus.BeingDelayed){  // if other Transport is Active
                return true;   // because the truck is Occupied in another Active Transport
            }
        }
        return false;
    }










    public String getTransportAsDTOJson(int tranId) throws JsonProcessingException {
        for (TransportDoc transportDoc : this.transports.values()){
            if (transportDoc.getTran_Doc_ID() == tranId){
                return this.objectMapper.writeValueAsString(convertTransportDocToTransportDTO(transportDoc));
            }
        }
        for (TransportDoc queuedTransportDoc : this.queuedTransports){
            if (queuedTransportDoc.getTran_Doc_ID() == tranId){
                return this.objectMapper.writeValueAsString(convertTransportDocToTransportDTO(queuedTransportDoc));
            }
        }
        return null;
    }




    // used for the checkIfATransportCanGo function
    public String getAQueuedTransportAsDTOJson(int index) throws IndexOutOfBoundsException, AttributeNotFoundException, JsonProcessingException {
        if(!this.queuedTransports.isEmpty()){
            if (index > this.queuedTransports.size()) {   //  the index is going to be 1, 2...
                throw new IndexOutOfBoundsException("The index you've entered in invalid. (it's above the last index which is " + this.queuedTransports.size() + ")");
            }
            TransportDoc queuedTransport = queuedTransports.get(index-1);
            TransportDTO transportDTO = convertTransportDocToTransportDTO(queuedTransport);
            String transportDTOAsJson = objectMapper.writeValueAsString(transportDTO);

            return transportDTOAsJson;
        }else {
            throw new AttributeNotFoundException("There are no Queued Transports.");
        }
    }






    private TransportDTO convertTransportDocToTransportDTO(TransportDoc transportDoc) throws JsonProcessingException {
        ArrayList<ItemsDocDTO> listOfItemsDocDTOs = new ArrayList<ItemsDocDTO>();
        Site srcSite = transportDoc.getSrc_site();
        SiteDTO srcSiteDTO = new SiteDTO(srcSite.getAddress().getArea(), srcSite.getAddress().getAddress());

        for (ItemsDoc itemsDoc : transportDoc.getDests_Docs()){
            Site destSite = itemsDoc.getDest_site();
            SiteDTO destSiteDTO = new SiteDTO(destSite.getAddress().getArea(), destSite.getAddress().getAddress());

            ArrayList<ItemQuantityDTO> itemQuantityDTOS = new ArrayList<>();

            for (Item item : itemsDoc.getBadItems().keySet()){
                ItemDTO itemDTO = new ItemDTO(item.getName(), item.getWeight(), item.getCondition());
                itemQuantityDTOS.add(new ItemQuantityDTO(itemDTO, itemsDoc.getBadItems().get(item)));
            }
            for (Item item : itemsDoc.getGoodItems().keySet()){
                ItemDTO itemDTO = new ItemDTO(item.getName(), item.getWeight(), item.getCondition());
                itemQuantityDTOS.add(new ItemQuantityDTO(itemDTO, itemsDoc.getGoodItems().get(item)));
            }
            listOfItemsDocDTOs.add(new ItemsDocDTO(itemsDoc.getItemDoc_num(), srcSiteDTO, destSiteDTO, itemQuantityDTOS, itemsDoc.getEstimatedArrivalTime()));
        }

        TransportDTO transportDTO = new TransportDTO(transportDoc.getTran_Doc_ID(), transportDoc.getTransportTruck().getTruck_num(), transportDoc.getTransportDriverId(), srcSiteDTO, listOfItemsDocDTOs, transportDoc.getDeparture_dt());
        return transportDTO;
    }



    private TransportDoc convertTransportDTOToTransportDoc(TransportDTO transport_DTO){
        long driverID = transport_DTO.getTransportDriverID();
        Truck truck = this.truckFacade.getTrucksWareHouse().get(transport_DTO.getTransportTruckNum());
        Site srcSite = this.siteFacade.getShippingAreas().get(transport_DTO.getSrc_site().getSiteAreaNum()).getSites().get(transport_DTO.getSrc_site().getSiteAddressString());

        TransportDoc tempTransport = new TransportDoc(enumTranStatus.BeingAssembled, transport_DTO.getTransport_ID(), truck, driverID, srcSite, transport_DTO.getDeparture_dt());

        for (ItemsDocDTO itemsDocDTO : transport_DTO.getDests_Docs()){
            Site destSiteTemp = this.siteFacade.getShippingAreas().get(itemsDocDTO.getDest_siteDTO().getSiteAreaNum()).getSites().get(itemsDocDTO.getDest_siteDTO().getSiteAddressString());
            tempTransport.addDestSite(itemsDocDTO.getItemsDoc_num(), destSiteTemp);
            // calculates the arrival times of the itemsDocs accordingly

            for (ItemQuantityDTO itemQuantityDTO : itemsDocDTO.getItemQuantityDTOs()){
                tempTransport.addItem(itemsDocDTO.getItemsDoc_num(), itemQuantityDTO.getItem().getName(), itemQuantityDTO.getItem().getWeight(), itemQuantityDTO.getQuantity(), itemQuantityDTO.getItem().getCondition());
            }
        }    ///  adding every site and every item for each site

        return tempTransport;
    }





















    public void addTransportProblem(int TransportID, int menu_Problem_option) throws FileNotFoundException, FileAlreadyExistsException {
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

        for (TransportDoc queuedTransport : this.queuedTransports){  // checking if in queuedTransports
            if (TransportID == queuedTransport.getTran_Doc_ID()){
                if (queuedTransport.addTransportProblem(probEnum) == -1){
                    throw new FileAlreadyExistsException("The problem you entered already exists in this Transport");
                }
                return;
            }
        }

        if (!this.transports.containsKey(TransportID)) {
            throw new FileNotFoundException("Transport ID doesn't exist in the Transports.");
        }

        if (this.transports.get(TransportID).addTransportProblem(probEnum) == -1){
            throw new FileAlreadyExistsException("The problem you entered already exists in this Transport");
        }
    }


    public void removeTransportProblem(int TransportID, int menu_Problem_option) throws FileNotFoundException, FileAlreadyExistsException {
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

        for (TransportDoc queuedTransport : this.queuedTransports){
            if (TransportID == queuedTransport.getTran_Doc_ID()){
                if (queuedTransport.removeTransportProblem(probEnum) == -1){
                    throw new FileAlreadyExistsException("The problem you entered already doesn't exists in this Transport");
                }
                return;
            }
        }

        if (!this.transports.containsKey(TransportID)) {
            throw new FileNotFoundException("Transport ID doesn't exist.");
        }

        if (this.transports.get(TransportID).removeTransportProblem(probEnum) == -1){
            throw new FileAlreadyExistsException("The problem you entered already doesn't exists in this Transport");
        }
    }






    public boolean doesTranIDExist(int TranID){
        boolean exists = this.transports.containsKey(TranID);
        for (TransportDoc queuedTransport : this.queuedTransports){
            if (TranID == queuedTransport.getTran_Doc_ID()){
                return true;
            }
        }
        return exists;
    }







    public void addDestSiteToTransport(int tran_ID, int itemsDoc_num, int destSiteArea, String destSiteAddress, String contName, long contNum) throws FileNotFoundException, FileAlreadyExistsException, CommunicationException, IndexOutOfBoundsException, ClassNotFoundException {
        if (this.itemsDocs.containsKey(itemsDoc_num)) {
            throw new FileAlreadyExistsException("The Site's Items Document Number you are trying to add already exists.");
        } else if (!this.siteFacade.getShippingAreas().containsKey(destSiteArea)) {
            throw new IndexOutOfBoundsException("Cannot add a Site with a non existent area number.");
        } else if (!this.siteFacade.getShippingAreas().get(destSiteArea).getSites().containsKey(destSiteAddress)) {
            throw new ClassNotFoundException("Cannot add a site with a not found address String in its area.");
        }

        for (TransportDoc queuedTransport : this.queuedTransports){   //  first checking in the queuedTransports
            if (tran_ID == queuedTransport.getTran_Doc_ID()){
                ItemsDoc addition = queuedTransport.addDestSite(itemsDoc_num, new Site(new Address(destSiteArea, destSiteAddress), contName, contNum));
                // ALSO recalculates arrival times within ItemsDocs of this Transport in the TransportDoc.
                if (addition == null){
                    throw new CommunicationException("Destination Site already in this Transport, you can add items to that site instead.");
                }
                this.itemsDocs.put(itemsDoc_num, addition);
                return;
            }
        }

        if (!transports.containsKey(tran_ID)) {
            throw new FileNotFoundException("The Transport ID you've entered doesn't exist in the Transports.");
        }
        ItemsDoc addition = this.transports.get(tran_ID).addDestSite(itemsDoc_num, new Site(new Address(destSiteArea, destSiteAddress), contName, contNum));
        // recalculates arrival times within ItemsDocs of this Transport in the TransportDoc.
        if (addition == null){
            throw new CommunicationException("Destination Site already in this Transport, you can add items to that site instead.");
        }

        this.itemsDocs.put(itemsDoc_num, addition);
    }


    public void removeDestSiteFromTransport(int tran_ID, int itemsDoc_num) throws FileNotFoundException, CommunicationException, ClassNotFoundException {
        if (!this.itemsDocs.containsKey(itemsDoc_num)) {
            throw new CommunicationException("The Site's Items Document Number you are trying to remove doesn't exist in the system.");
        }

        for (TransportDoc queuedTransport : this.queuedTransports){
            if (tran_ID == queuedTransport.getTran_Doc_ID()){
                if (queuedTransport.removeDestSite(itemsDoc_num) == -1){
                    throw new ClassNotFoundException("The Site's Items Document Number is not in that Transport");
                }
                // also recalculates arrival times within ItemsDocs of this Transport in the TransportDoc.
                this.itemsDocs.remove(itemsDoc_num);
                return;
            }
        }

        if (!transports.containsKey(tran_ID)) {
            throw new FileNotFoundException("The Transport ID you've entered doesn't exist.");
        }

        if (this.transports.get(tran_ID).removeDestSite(itemsDoc_num) == -1){
            throw new ClassNotFoundException("The Site's Items Document Number is not in that Transport");
        }
        // recalculates arrival times within ItemsDocs of this Transport in the TransportDoc.
        this.itemsDocs.remove(itemsDoc_num);
    }




    public void setSiteArrivalIndexInTransport(int transportID, int siteArea, String siteAddress, int index) throws FileNotFoundException, ClassNotFoundException, AbstractMethodError, CommunicationException {
        if (!this.siteFacade.getShippingAreas().containsKey(siteArea)) {
            throw new IndexOutOfBoundsException("You entered a Site with a non existent area number.");
        } else if (!this.siteFacade.getShippingAreas().get(siteArea).getSites().containsKey(siteAddress)) {
            throw new CommunicationException("You entered a site address String that doesn't exist in that area.");
        }

        for (TransportDoc queuedTransport : this.queuedTransports){  //  checking the queuedTransports first here
            if (transportID == queuedTransport.getTran_Doc_ID()){

                boolean siteResidesInQueuedTransport = false;
                for (ItemsDoc itemsDoc : queuedTransport.getDests_Docs()){
                    if (itemsDoc.getDest_site().getAddress().getArea() == siteArea && itemsDoc.getDest_site().getAddress().getAddress().equals(siteAddress)){
                        siteResidesInQueuedTransport = true;
                    }
                }

                if(!siteResidesInQueuedTransport){
                    throw new ClassNotFoundException("Site not found inside of that transport");
                } else if (index > queuedTransport.getDests_Docs().size()) {   // if the index is valid    //  index should be 1, 2, ....
                    throw new AbstractMethodError("The Index entered is bigger than the amount of sites in the Transport, so can't put that site in that bigger index");
                }

                // also recalculates arrival times within ItemsDocs of this Transport in the TransportDoc.
                queuedTransport.setSiteArrivalIndexInTransport(siteArea, siteAddress, index);
                return;
            }
        }

        if (!this.transports.containsKey(transportID)){ throw new FileNotFoundException("The transport ID given was not found"); }

        boolean siteResidesInTransport = false;
        for (ItemsDoc itemsDoc : this.transports.get(transportID).getDests_Docs()){
            if (itemsDoc.getDest_site().getAddress().getArea() == siteArea && itemsDoc.getDest_site().getAddress().getAddress().equals(siteAddress)){
                siteResidesInTransport = true;
            }
        }

        if(!siteResidesInTransport){
            throw new ClassNotFoundException("Site not found inside of that transport");
        } else if (index > this.transports.get(transportID).getDests_Docs().size()) {   // if the index is valid    //  index should be 1, 2, ....
            throw new AbstractMethodError("The Index entered is bigger than the amount of sites in the Transport, so can't put that site in that bigger index");
        }

        this.transports.get(transportID).setSiteArrivalIndexInTransport(siteArea, siteAddress, index); // also recalculates arrival times within ItemsDocs of this Transport in the TransportDoc.
    }




    public boolean doesAddressExistInTransport(int transportID, int siteArea, String siteAddress) {
        for (TransportDoc queuedTransport : this.queuedTransports){
            if (transportID == queuedTransport.getTran_Doc_ID()){
                for (ItemsDoc itemsDoc : queuedTransport.getDests_Docs()){
                    if (itemsDoc.getDest_site().getAddress().getArea() == siteArea && itemsDoc.getDest_site().getAddress().getAddress().equals(siteAddress)){
                        return true;
                    }
                }
            }
        }
        //  now searching in the regular transports
        if (!this.transports.containsKey(transportID)) { return false; }
        for (ItemsDoc itemsDoc : this.transports.get(transportID).getDests_Docs()){
            if (itemsDoc.getDest_site().getAddress().getArea() == siteArea && itemsDoc.getDest_site().getAddress().getAddress().equals(siteAddress)){
                return true;
            }
        }
        return false;
    }




    public boolean doesItemsDocIDExistInTransport(int itemsDocNum, int tranId) {     ///   helper function
        for (TransportDoc queuedTransport : this.queuedTransports){
            if (queuedTransport.getTran_Doc_ID() == tranId){
                for (ItemsDoc itemsDoc : queuedTransport.getDests_Docs()){
                    if (itemsDocNum == itemsDoc.getItemDoc_num()){
                        return true;
                    }
                }
            }
        }
        if (this.transports.containsKey(tranId)){
            for (ItemsDoc itemsDoc : this.transports.get(tranId).getDests_Docs()){
                if (itemsDoc.getItemDoc_num() == itemsDocNum){
                    return true;
                }
            }
        }
        return false;
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
    }




    public boolean checkValidItemsDocID(int currItemsDocNum) {
        if (this.itemsDocs.containsKey(currItemsDocNum)) {
            return false;
        }
        return true;
    }



    //NOTE: only if he actively drives a transport, for: a driver can edit items condition in a transport he is driving now.
    public void checkIfDriverDrivesThisItemsDoc(long id, int itemsDocId, boolean isNotDriver) throws FileNotFoundException, IllegalAccessException, ClassNotFoundException {
        if (!this.itemsDocs.containsKey(itemsDocId)) { throw new FileNotFoundException("Items Document ID not found."); }
        if (isNotDriver){ throw new ClassNotFoundException("Driver ID doesn't exist."); }

        boolean driverDrivesThisItemsDoc = false;
        for (TransportDoc transportDoc : this.transports.values()) {
            if (transportDoc.getTransportDriverId() == id && this.driverIdToInTransportID.get(id) == transportDoc.getTran_Doc_ID()){
                // now here we know that he actively drives this transport.
                for (ItemsDoc itemsDoc : transportDoc.getDests_Docs()) {
                    if (itemsDoc.getItemDoc_num() == itemsDocId) {
                        driverDrivesThisItemsDoc = true;
                        break; // because found
                    }
                }
            }
            if (driverDrivesThisItemsDoc){ break;}   // because found
        }

        if (!driverDrivesThisItemsDoc) { throw new IllegalAccessException("Driver doesn't drive this Items Document's Transport"); }
    }



















    public void addItem(int itemsDoc_num, String itemName, double itemWeight, int amount, boolean cond) throws FileNotFoundException, IndexOutOfBoundsException {
        if (!this.itemsDocs.containsKey(itemsDoc_num)) { throw new FileNotFoundException("Item's Document ID not found within sent Transports"); }

        for (TransportDoc queuedtransportDoc : this.queuedTransports) {   // checking in the queuedTransports first if there. // weight checking
            for (ItemsDoc itemsDoc : queuedtransportDoc.getDests_Docs()) {
                if (itemsDoc.getItemDoc_num() == itemsDoc_num) {
                    if (queuedtransportDoc.getTruck_Depart_Weight() + (itemWeight*amount) > queuedtransportDoc.getTransportTruck().getMax_carry_weight()){
                        throw new IndexOutOfBoundsException("Cannot add Item to transport because the new weight exceeds the maximum carry weight");
                    }
                }
            }
        }
        for (TransportDoc transportDoc : this.transports.values()) { // weight checking in the regular transports hashmap, if there
            for (ItemsDoc itemsDoc : transportDoc.getDests_Docs()) {
                if (itemsDoc.getItemDoc_num() == itemsDoc_num) {
                    if (transportDoc.getTruck_Depart_Weight() + (itemWeight*amount) > transportDoc.getTransportTruck().getMax_carry_weight()){
                        throw new IndexOutOfBoundsException("Cannot add Item to transport because the new weight exceeds the maximum carry weight");
                    }
                }
            }
        }

        // adding the item to the ItemsDoc
        int res = this.itemsDocs.get(itemsDoc_num).addItem(itemName, itemWeight, cond, amount);

        // setting the new weight of the Transport if in the queuedTransports
        for (TransportDoc queuedTransportDoc : this.queuedTransports) {
            for (ItemsDoc itemsDoc : queuedTransportDoc.getDests_Docs()) {
                if (itemsDoc.getItemDoc_num() == itemsDoc_num) {
                    queuedTransportDoc.setTruck_Depart_Weight(queuedTransportDoc.getTruck_Depart_Weight() + (itemWeight*amount));  // updating the weight
                    return;
                }
            }
        }
        // setting the new weight of the Transport if in the regular transports hashmap
        for (TransportDoc transportDoc : this.transports.values()) {
            for (ItemsDoc itemsDoc : transportDoc.getDests_Docs()) {
                if (itemsDoc.getItemDoc_num() == itemsDoc_num) {
                    transportDoc.setTruck_Depart_Weight(transportDoc.getTruck_Depart_Weight() + (itemWeight*amount));  // updating the weight
                    return;
                }
            }
        }

    }



    public void removeItem(int itemsDoc_num, String itemName, double itemWeight, int amount, boolean cond) throws FileNotFoundException, ClassNotFoundException {
        if (!this.itemsDocs.containsKey(itemsDoc_num)) { throw new FileNotFoundException("Item's Document ID not found within sent Transports"); }

        int amount_removed = this.itemsDocs.get(itemsDoc_num).removeItem(itemName, itemWeight, cond, amount);
        if (amount_removed == -1){ throw new ClassNotFoundException("Item to remove not found in that Items Document"); }

        // checking first in the queued transports, and decreasing weight
        for (TransportDoc queuedTransportDoc : this.queuedTransports) {
            for (ItemsDoc itemsDoc : queuedTransportDoc.getDests_Docs()) {
                if (itemsDoc.getItemDoc_num() == itemsDoc_num) {
                    queuedTransportDoc.setTruck_Depart_Weight(queuedTransportDoc.getTruck_Depart_Weight() - (itemWeight*amount_removed));  // updating the weight
                    return;
                }
            }
        }
        // checking in the regular transports, and decreasing weight
        for (TransportDoc transportDoc : this.transports.values()) {
            for (ItemsDoc itemsDoc : transportDoc.getDests_Docs()) {
                if (itemsDoc.getItemDoc_num() == itemsDoc_num) {
                    transportDoc.setTruck_Depart_Weight(transportDoc.getTruck_Depart_Weight() - (itemWeight*amount_removed));  // updating the weight
                    return;
                }
            }
        }

    }


    public void setItemCond(int itemsDoc_num, String itemName, double itemWeight, int amount, boolean newCond) throws FileNotFoundException, ClassNotFoundException {
        if (!this.itemsDocs.containsKey(itemsDoc_num)) { throw new FileNotFoundException("Item's Document ID not found within sent Transports"); }

        int res = this.itemsDocs.get(itemsDoc_num).setItemCond(itemName, itemWeight, amount, newCond);
        if (res == -1){ throw new ClassNotFoundException("Item to change condition to was not found in that Items Document"); }
    }










    public String showTransportsOfDriver(long id, boolean isNotDriver) throws ArrayStoreException {
        if (isNotDriver){ throw new ArrayStoreException("The Driver(ID) to show Transports for was not found"); }
        String res = "All Transports (all statuses) That Driver with id " + id + " is written in:\n";
        for (TransportDoc t : transports.values()){
            if (t.getTransportDriverId() == id){
                res += t.toString() + "\n";
            }
        }
        res += "\nQueued Transports of Driver(Values of Drivers/Trucks/Time here are the values that were set when first trying to create the Transport):\n";
        for (TransportDoc t : this.queuedTransports){
            if (t.getTransportDriverId() == id){
                res += "(Queued Transport) " + t.toString() + "\n";
            }
        }
        res += "\n";
        return res;
    }



    public String showAllQueuedTransports() {
        String resOfQueuedTransports = "Queued Transports (Values of Drivers/Trucks/Time here are the values that were set when first trying to create the Transport):\n";
        resOfQueuedTransports += "(1)-oldest entry ...... (" + this.queuedTransports.size() + ")-latest entry\n";
        int counter = 1;
        for (TransportDoc transportDoc : this.queuedTransports) {
            resOfQueuedTransports += "" + counter + ")" + transportDoc.toString() + "\n";
            counter++;
        }
        resOfQueuedTransports += "\n";
        return resOfQueuedTransports;
    }


    public String showAllTransports(){
        String resOfAllTransports = "All Transports:\n";
        for (TransportDoc t : transports.values()){
            resOfAllTransports += t.toString() + "\n";
        }
        resOfAllTransports += "Queued Transports (Values of Drivers/Trucks/Time here are the values set when first trying to create the Transport):\n";
        for (TransportDoc t : this.queuedTransports){
            resOfAllTransports += "(Queued Transport) " + t.toString() + "\n";
        }
        resOfAllTransports += "\n";
        return resOfAllTransports;
    }



}
