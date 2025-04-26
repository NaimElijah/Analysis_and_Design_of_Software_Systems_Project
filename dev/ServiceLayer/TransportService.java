package ServiceLayer;

import DomainLayer.TranSubModule.TransportFacade;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.management.AttributeNotFoundException;
import javax.management.openmbean.KeyAlreadyExistsException;
import javax.naming.CommunicationException;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;

public class TransportService {
    private TransportFacade tran_f;
    public TransportService(TransportFacade tf) { this.tran_f = tf; }


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
            this.tran_f.setTransportStatus(TranDocID, intMenuStatusOption);
        } catch (FileNotFoundException e) {
            return "The Transport ID you have entered doesn't exist.";
        } catch (FileAlreadyExistsException e) {
            return "The status you are trying to set already is the status of this Transport";
        } catch (CommunicationException e) {
            return "cannot change Transport Status because it wants to change to an active one, but the Driver is already active in another Transport.";
        } catch (CloneNotSupportedException e) {
            return "cannot change Transport Status because it wants to change to an active one, but the Truck is already active in another Transport.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }






    public String setTransportTruck(int TranDocID, int truckNum){
        if (TranDocID < 0 || truckNum < 0){ return "Transport Document number, Truck number values cannot be negative."; }
        try {
            this.tran_f.setTransportTruck(TranDocID, truckNum);
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
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }



    public String setTransportDriver(int TranDocID, int DriverID){
        if (TranDocID < 0 || DriverID < 0){ return "Transport Document number, Driver ID values cannot be negative."; }
        try {
            this.tran_f.setTransportDriver(TranDocID, DriverID);
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
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }







    public String checkTransportValidity(String transportDTO) {  ///  returns: "Valid", "BadLicenses", "<overallWeight-truckMaxCarryWeight>", "Queue", "Occupied"
        String res = "";
        try {
            res = this.tran_f.checkTransportValidity(transportDTO);
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
        } catch (Exception e) {
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
        if (currItemsDocNum < 0){  // very basic check
            return false;
        }
        return this.tran_f.checkValidItemsDocID(currItemsDocNum);  // return what the business layer said
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




























    public String addItem(int itemsDocNum, String itemName, int itemWeight, int amount, boolean cond){
        if (itemName.isEmpty() || itemName.isBlank()){ return "Item's name cannot be empty"; }
        if (itemsDocNum < 0 || itemWeight < 0 || amount < 0){ return "Item's document number/weight/amount cannot be negative"; }
        try {
            this.tran_f.addItem(itemsDocNum, itemName, itemWeight, amount, cond);
        } catch (FileNotFoundException e) {
            return "Item's Document ID not found";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }


    public String removeItem(int itemsDocNum, String itemName, int itemWeight, int amount, boolean cond){
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


    public String setItemCond(int itemsDocNum, String itemName, int itemWeight, int amount, boolean cond){
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