package ServiceLayer;

import DTOs.EmployeeDTO;
import DTOs.ItemQuantityDTO;
import DTOs.ItemsDocDTO;
import DTOs.TransportDTO;
import DomainLayer.EmployeeSubModule.Employee;
import DomainLayer.SiteSubModule.Site;
import DomainLayer.TranSubModule.TransportController;
import DomainLayer.TranSubModule.TransportDoc;
import DomainLayer.TruSubModule.Truck;
import DomainLayer.enums.enumTranStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.management.AttributeNotFoundException;
import javax.management.openmbean.KeyAlreadyExistsException;
import javax.naming.CommunicationException;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;

public class TransportService {
//    private EmployeeService employeeService;
    private EmployeeIntegrationService employeeIntegrationServiceService;
    private TransportController tran_f;
    private ObjectMapper objectMapper;

    public TransportService(TransportController tf, EmployeeIntegrationService es) {
        this.employeeIntegrationServiceService = es;
        this.tran_f = tf;
        this.objectMapper = new ObjectMapper();
    }


    public String createTransport(String transportDTO, int queuedIndexIfWasQueued){
        try {
            this.tran_f.createTransport(transportDTO, queuedIndexIfWasQueued);
        } catch (JsonProcessingException e) {
            return "JSON's Error Exception";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }

    public String deleteTransport(int transportID){
        if (transportID < 0){ return "Can't Enter a negative Transport ID number"; }
        try {
            this.tran_f.deleteTransport(transportID);
        } catch (FileNotFoundException e) {
            return "No transport found with the Transport ID you've entered, so can't delete that Transport";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }







    public String setTransportStatus(int TranDocID, String menu_status_option){
        int intMenuStatusOption = Integer.parseInt(menu_status_option);
        if (intMenuStatusOption < 1 || intMenuStatusOption > 6){
            return "Invalid menu status option - enter a number between 1 and 6";
        }
        try {
            this.tran_f.setTransportStatus(TranDocID, intMenuStatusOption, this.employeeIntegrationServiceService.isActive(this.tran_f.getTransports().get(TranDocID).getTransportDriverId()));
        } catch (FileNotFoundException e) {
            return "The Transport ID you have entered doesn't exist.";
        } catch (FileAlreadyExistsException e) {
            return "The status you are trying to set already is the status of this Transport";
        } catch (CommunicationException e) {
            return "cannot change Transport Status because it wants to change to an active one, but the Driver is already active in another Transport.";
        } catch (CloneNotSupportedException e) {
            return "cannot change Transport Status because it wants to change to an active one, but the Truck is already active in another Transport.";
        } catch (IndexOutOfBoundsException e) {
            return "the Truck or/and Driver of this Transport have been Deleted, you can view available Trucks or/and Drivers using the menu and set appropriately";
        }catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }






    public String setTransportTruck(int TranDocID, int truckNum){
        if (TranDocID < 0 || truckNum < 0){ return "Transport Document number, Truck number values cannot be negative."; }
        try {
            this.tran_f.setTransportTruck(TranDocID, truckNum, this.employeeIntegrationServiceService.hasRole(this.tran_f.getTransports().get(TranDocID).getTransportDriverId(), this.tran_f.getTruckLicenseAsStringRole(truckNum)));
        } catch (FileNotFoundException e) {
            return "The Transport ID you have entered doesn't exist.";
        } catch (ArrayIndexOutOfBoundsException e) {
            return "The Truck number you have entered doesn't exist.";
        } catch (FileAlreadyExistsException e) {
            return "This Truck is already the Truck of this Transport";
        } catch (CloneNotSupportedException e) {
            return "The Transport you are trying to set to is Active and The Truck you are trying to set is already Occupied with another Active Transport right now";
        } catch (CommunicationException e) {
            return "The transport's driver doesn't have the fitting license for the new Truck you want to set.";
        } catch (AbstractMethodError e) {
            return "The Truck you are trying to set to this Transport can't carry this Transport's Weight.";
        } catch (ClassNotFoundException e) {
            return "the Truck of this Transport have been Deleted, you can view available Trucks using the menu and set appropriately";
        }catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }



    public String setTransportDriver(int TranDocID, int DriverID){
        if (TranDocID < 0 || DriverID < 0){ return "Transport Document number, Driver ID values cannot be negative."; }
        try {
            boolean isNotDriver = !this.employeeIntegrationServiceService.hasRole(DriverID, "DriverA") && !this.employeeIntegrationServiceService.hasRole(DriverID, "DriverB") && !this.employeeIntegrationServiceService.hasRole(DriverID, "DriverC") && !this.employeeIntegrationServiceService.hasRole(DriverID, "DriverD") && !this.employeeIntegrationServiceService.hasRole(DriverID, "DriverE");
            String lice = this.tran_f.getTruckLicenseAsStringRole(this.tran_f.getTransports().get(TranDocID).getTransportTruck().getTruck_num());
            this.tran_f.setTransportDriver(TranDocID, DriverID, isNotDriver, this.employeeIntegrationServiceService.isActive(DriverID), this.employeeIntegrationServiceService.hasRole(DriverID, lice));
        } catch (FileNotFoundException e) {
            return "The Transport ID you have entered doesn't exist.";
        } catch (ArrayIndexOutOfBoundsException e) {
            return "The Driver ID you have entered doesn't exist.";
        } catch (FileAlreadyExistsException e) {
            return "This Driver is already the Driver of this Transport";
        } catch (CloneNotSupportedException e) {
            return "The Transport you are trying to set to is Active and The Driver you are trying to set is already Occupied with another Active Transport right now";
        } catch (CommunicationException e) {
            return "The New Driver you are trying to set doesn't have the fitting license for the Truck that is in the Transport.";
        } catch (ClassNotFoundException e) {
            return "the Driver of this Transport have been Deleted, you can view available Drivers using the menu and set appropriately";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }






    //TODO: refactor to timing and specific site workers elements.  (also refactor other functions in here)
    public String isTruckDriverPairingGood(int truckNum, int driverID) {
        if (truckNum < 0 || driverID < 0){ return "Truck number/Driver ID values cannot be negative."; }
        try {
            ArrayList<EmployeeDTO> employeesDTOs = new ArrayList<>();
            for (String emp : this.employeeIntegrationServiceService.getAllDrivers()){
                employeesDTOs.add(this.objectMapper.readValue(emp, EmployeeDTO.class));
            }
            String lice = this.tran_f.getTruckLicenseAsStringRole(truckNum);
            boolean isThereAvailableDriverMatchingThisTruck = false;

            for(EmployeeDTO driver : employeesDTOs){
                if (this.employeeIntegrationServiceService.hasRole(driver.getIsraeliId(), lice) && !this.tran_f.getDriverIdToInTransportID().containsKey(driver.getIsraeliId())){  // if driver compatible and free
                    isThereAvailableDriverMatchingThisTruck = true;
                }
            }
            if (!isThereAvailableDriverMatchingThisTruck){
                throw new ClassNotFoundException("There isn't a Driver that is available right now and compatible, license wise, with the Truck you chose");
            }

            boolean isNotDriver = !this.employeeIntegrationServiceService.hasRole(driverID, "DriverA") && !this.employeeIntegrationServiceService.hasRole(driverID, "DriverB") && !this.employeeIntegrationServiceService.hasRole(driverID, "DriverC") && !this.employeeIntegrationServiceService.hasRole(driverID, "DriverD") && !this.employeeIntegrationServiceService.hasRole(driverID, "DriverE");
            boolean hasRole22 = this.employeeIntegrationServiceService.hasRole(driverID, lice);
            this.tran_f.isTruckDriverPairingGood(truckNum, driverID, isNotDriver, hasRole22);

        } catch (FileNotFoundException e) {
            return "Truck Number entered doesn't exist";
        } catch (ArrayIndexOutOfBoundsException e) {
            return "The Driver ID you have entered doesn't exist";
        } catch (CloneNotSupportedException e) {
            return "The Truck you chose is partaking in another Active Transport right now";
        } catch (ClassNotFoundException e) {
            return "There isn't a Driver that is available right now and compatible, license wise, with the Truck you chose";
        } catch (CommunicationException e) {
            return "The Driver you chose doesn't have the fitting license for the Truck you chose";
        }catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }








    //TODO: refactor to timing and specific site workers elements.  (also refactor other functions in here)
    public String checkTransportValidity(String DTO_OfTransport) {  ///  returns: "Valid", "BadLicenses", "<overallWeight-truckMaxCarryWeight>", "Queue", "Occupied"
        String res = "Valid";
        try {
            /// /////////////////////////////////    <<-------------------------------------   checking if there's a Driver-Truck Pairing At All Right Now, from the Free ones
            ArrayList<EmployeeDTO> employeesDTOs = new ArrayList<>();
            for (String emp : this.employeeIntegrationServiceService.getAllDrivers()){
                employeesDTOs.add(this.objectMapper.readValue(emp, EmployeeDTO.class));
            }
            ///  checking if there is a match at all, --> from those who are free right now
            boolean isThereMatchAtAllBetweenLicenses = false;
            for (EmployeeDTO employee : employeesDTOs){
                for (int trucNum : this.tran_f.getTruckFacade().getTrucksWareHouse().keySet()){
                    if (this.employeeIntegrationServiceService.hasRole(employee.getIsraeliId(), this.tran_f.getTruckLicenseAsStringRole(trucNum))){  //  if compatible
                        if ((!this.tran_f.isDriverActive(employee.getIsraeliId())) && (!this.tran_f.isTruckActive(trucNum))){   // searching only the free ones, like in the Requirements
                            isThereMatchAtAllBetweenLicenses = true;  // if found
                            break;   // because already found
                        }
                    }
                }
                if (isThereMatchAtAllBetweenLicenses){break;}  // if already found
            }
            // else: continue to check other stuff inside this function
            TransportDTO transport_DTO = this.objectMapper.readValue(DTO_OfTransport, TransportDTO.class);
            boolean hasRole11 = this.employeeIntegrationServiceService.hasRole(transport_DTO.getTransportDriverID(), this.tran_f.getTruckLicenseAsStringRole(transport_DTO.getTransportTruckNum()));
            res = this.tran_f.checkTransportValidity(DTO_OfTransport, hasRole11, isThereMatchAtAllBetweenLicenses);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "JsonProcessingException";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return res;  //  if All Good
    }





    public String getAQueuedTransportAsDTOJson(int index){
        String res = "";
        if (index < 1){ return "The index you've entered in invalid. (it's below the Starting index which is 1)"; }
        try {
            res = this.tran_f.getAQueuedTransportAsDTOJson(index);
        } catch (IndexOutOfBoundsException e) {
            return "index";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Json";
        } catch (AttributeNotFoundException e) {
            return "noQueued";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return res;
    }
















    public String addDestSite(int tran_ID, int itemsDoc_num, int destSiteArea, String destSiteAddress, String contName, long contNum) {
        if (tran_ID < 0 || itemsDoc_num < 0 || destSiteArea < 0 || contNum < 0){
            return "The info numbers you have entered cannot be negative";
        }
        if (destSiteAddress.isEmpty() || destSiteAddress.isBlank() || contName.isEmpty() || contName.isBlank()){
            return "The info strings you've entered cannot be empty";
        }
        try {
            this.tran_f.addDestSiteToTransport(tran_ID, itemsDoc_num, destSiteArea, destSiteAddress, contName, contNum);
        } catch (FileNotFoundException e) {
            return "The Transport ID you've entered doesn't exist.";
        } catch (FileAlreadyExistsException e) {
            return "The Site's Items Document Number you are trying to add already exists.";
        } catch (CommunicationException e) {
            return "Destination Site already in this Transport, you can add items to that site instead.";
        } catch (IndexOutOfBoundsException e) {
            return "Cannot add a Site with a non existent area number.";
        } catch (ClassNotFoundException e) {
            return "Cannot add a site with a not found address String in its area.";
        }catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }


    public String removeDestSite(int tran_ID, int itemsDoc_num){
        if (tran_ID < 0 || itemsDoc_num < 0){
            return "The info you entered cannot be negative";
        }
        try {
            this.tran_f.removeDestSiteFromTransport(tran_ID, itemsDoc_num);
        } catch (FileNotFoundException e) {
            return "The Transport ID you've entered doesn't exist.";
        } catch (CommunicationException e) {
            return "The Site's Items Document Number you are trying to remove doesn't exist in the system.";
        } catch (ClassNotFoundException e) {
            return "The Site's Items Document Number is not in that Transport";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }




    public String setSiteArrivalIndexInTransport(int transportID, int siteArea, String siteAddress, String index){
        int intIndex = Integer.parseInt(index);
        if (intIndex < 0){    //  index should be 1, 2, ....
            return "The Site Index in the arrival order cannot be negative";
        }
        if(transportID < 0 || siteArea < 0){
            return "The Transport ID and the Site Area cannot be negative";
        }
        try {
            this.tran_f.setSiteArrivalIndexInTransport(transportID, siteArea, siteAddress, intIndex);
        } catch (FileNotFoundException e) {
            return "The transport ID given was not found";
        } catch (ClassNotFoundException e) {
            return "Site not found inside of that transport";
        } catch (AbstractMethodError e) {
            return "The Index entered is bigger than the amount of sites in the Transport, so can't put that site in that bigger index";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }




    public String changeAnItemsDocNum(int oldItemsDocNum, int newItemsDocNum) {
        if (oldItemsDocNum == newItemsDocNum) {
            return "Changing Process finished because before and after values are the same";
        }
        try {
            tran_f.changeAnItemsDocNum(oldItemsDocNum, newItemsDocNum);
        } catch (FileNotFoundException e) {
            return "Old Items Document ID Non Existent";
        } catch (KeyAlreadyExistsException e) {
            return "New Items Document ID Already Exists !";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }


    public boolean checkValidItemsDocID(int currItemsDocNum) {  // very basic check
        if (currItemsDocNum < 0){ return false; }
        boolean res = false;
        try {
            res = this.tran_f.checkValidItemsDocID(currItemsDocNum);  // return what the business layer said
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }



    public String checkIfDriverDrivesThisItemsDoc(int id, int itemsDocId) {
        if (id < 0 || itemsDocId < 0){ return "The IDs you enter cannot be negative"; }
        try {
            boolean isNotDriver = !this.employeeIntegrationServiceService.hasRole(id, "DriverA") && !this.employeeIntegrationServiceService.hasRole(id, "DriverB") && !this.employeeIntegrationServiceService.hasRole(id, "DriverC") && !this.employeeIntegrationServiceService.hasRole(id, "DriverD") && !this.employeeIntegrationServiceService.hasRole(id, "DriverE");
            tran_f.checkIfDriverDrivesThisItemsDoc(id, itemsDocId, isNotDriver);
        } catch (FileNotFoundException e) {
            return "Items Document ID not found.";
        }catch (ClassNotFoundException e) {
            return "Driver ID doesn't exist.";
        }catch (IllegalAccessException e) {
            return "Driver doesn't drive this Items Document's Transport";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Yes";  //  if Yes
    }









    public String addTransportProblem(int TransportID, String menu_Problem_option){
        int intMenuProblemOption = Integer.parseInt(menu_Problem_option);
        if (intMenuProblemOption < 1 || intMenuProblemOption > 6){ return "The Problem option number you have entered is out of existing problem's numbers bounds"; }
        if (TransportID < 0){ return "The Transport ID you've entered is invalid (it's negative)"; }
        try {
            this.tran_f.addTransportProblem(TransportID, intMenuProblemOption);
        } catch (FileNotFoundException e) {
            return "Transport ID doesn't exist.";
        } catch (FileAlreadyExistsException e) {
            return "The problem you entered already exists in this Transport";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }

    public String removeTransportProblem(int TransportID, String menu_Problem_option){
        int intMenuProblemOption = Integer.parseInt(menu_Problem_option);
        if (intMenuProblemOption < 1 || intMenuProblemOption > 6){ return "The Problem option number you have entered is out of existing problem's numbers bounds"; }
        if (TransportID < 0){ return "The Transport ID you've entered is invalid (it's negative)"; }
        try {
            this.tran_f.removeTransportProblem(TransportID, intMenuProblemOption);
        } catch (FileNotFoundException e) {
            return "Transport ID doesn't exist.";
        } catch (FileAlreadyExistsException e) {
            return "The problem you entered already doesn't exists in this Transport";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }






























    public String addItem(int itemsDocNum, String itemName, double itemWeight, int amount, boolean cond){
        if (itemName.isEmpty() || itemName.isBlank()){ return "Item's name cannot be empty"; }
        if (itemsDocNum < 0 || itemWeight < 0 || amount < 0){ return "Item's document number/weight/amount cannot be negative"; }
        try {
            this.tran_f.addItem(itemsDocNum, itemName, itemWeight, amount, cond);
        } catch (FileNotFoundException e) {
            return "Item's Document ID not found";
        } catch (IndexOutOfBoundsException e) {
            return "Cannot add Item to transport because the new weight exceeds the maximum carry weight";
        }catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }


    public String removeItem(int itemsDocNum, String itemName, double itemWeight, int amount, boolean cond){
        if (itemName.isEmpty() || itemName.isBlank()){ return "Item's name cannot be empty"; }
        if (itemsDocNum < 0 || itemWeight < 0 || amount < 0){ return "Item's document number/weight/amount cannot be negative"; }
        try {
            this.tran_f.removeItem(itemsDocNum, itemName, itemWeight, amount, cond);
        } catch (FileNotFoundException e) {
            return "Item's Document ID not found";
        } catch (ClassNotFoundException e) {
            return "Item to remove not found in that Items Document";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }


    public String setItemCond(int itemsDocNum, String itemName, double itemWeight, int amount, boolean cond){
        if (itemName.isEmpty() || itemName.isBlank()){ return "Item's name cannot be empty"; }
        if (itemsDocNum < 0 || itemWeight < 0 || amount < 0){ return "Item's document number/weight/amount cannot be negative"; }
        try {
            this.tran_f.setItemCond(itemsDocNum, itemName, itemWeight, amount, cond);
        } catch (FileNotFoundException e) {
            return "Item's Document ID not found";
        } catch (ClassNotFoundException e) {
            return "Item to change condition to was not found in that Items Document";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }










    public String showTransportsOfDriver(int id) {
        if (id < 0){ return "The Driver(ID) you want to show is invalid (it's negative)"; }
        String res = "";
        try {
            boolean isNotDriver = !this.employeeIntegrationServiceService.hasRole(id, "DriverA") && !this.employeeIntegrationServiceService.hasRole(id, "DriverB") && !this.employeeIntegrationServiceService.hasRole(id, "DriverC") && !this.employeeIntegrationServiceService.hasRole(id, "DriverD") && !this.employeeIntegrationServiceService.hasRole(id, "DriverE");
            res = tran_f.showTransportsOfDriver(id, isNotDriver);
        } catch (ArrayStoreException e) {
            return "The Driver(ID) to show Transports for was not found";
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }


    public String showAllQueuedTransports() {
        String resOfAllQueuedTransports = "";
        try {
            resOfAllQueuedTransports = tran_f.showAllQueuedTransports();
        }catch (Exception e){
            e.printStackTrace();
        }
        return resOfAllQueuedTransports;
    }


    public String showAllTransports() {
        String resOfAllTransports = "";
        try {
            resOfAllTransports = tran_f.showAllTransports();
        }catch (Exception e){
            e.printStackTrace();
        }
        return resOfAllTransports;
    }


}