package ServiceLayer;

import DomainLayer.TranSubModule.TransportFacade;
import PresentationLayer.DTOs.TransportDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;

public class TransportService {
    private TransportFacade tran_f;
    public TransportService(TransportFacade tf) { this.tran_f = tf; }


    public String createTransport(String transportDTO){
        try {
            this.tran_f.createTransport(transportDTO);
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
            return "The problem you are trying to add already exists in this Transport";
        } catch (ClassNotFoundException e) {
            return "cannot change status to InTransit because the driver or the truck aren't free, maybe change the, and try again.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }





    public String setTransportTruck(int TranDocID, int truckNum){
        // TODO: check basic validity
        try {
            //TODO
        } catch (FileAlreadyExistsException e) {
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }



    public String setTransportDriver(int TranDocID, int DriverID){
        // TODO: check basic validity
        try {
            //TODO
        } catch (FileAlreadyExistsException e) {
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }







    public String checkTransportValidity(String transportDTO) {  ///  returns: "Valid", "BadLicenses", "<overallWeight-truckMaxCarryWeight>", "Queue"
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





    public String checkIfFirstQueuedTransportsCanGo(){
        // TODO: check basic validity
        try {
            //TODO
        } catch (FileAlreadyExistsException e) {
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }









    public String setSiteArrivalIndexInTransport(int transportID, int siteArea, String siteAddress, int index){
        if (index < 0){    //  index should be 1, 2, ....
            return "The Site Index in the arrival order cannot be negative";
        }
        if(transportID < 0 || siteArea < 0){
            return "The Transport ID and the Site Area cannot be negative";
        }
        try {
            this.tran_f.setSiteArrivalIndexInTransport(transportID, siteArea, siteAddress, index);
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











    public String addTransportProblem(int TransportID, String menu_Problem_option){
        // TODO: check basic validity
        try {
            //TODO
        } catch (FileAlreadyExistsException e) {
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }

    public String removeTransportProblem(int TransportID, String menu_Problem_option){
        // TODO: check basic validity
        try {
            //TODO
        } catch (FileAlreadyExistsException e) {
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }









    public String addDestSite(int tran_ID, int siteArea, String siteAddress) {  // (throw msg exce if in new Area and give option)
        // TODO: check basic validity
        try {
            //TODO
        } catch (FileAlreadyExistsException e) {
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }

    public String removeDestSite(int tran_ID, int siteArea, String siteAddress){
        // TODO: check basic validity
        try {
            //TODO
        } catch (FileAlreadyExistsException e) {
            return "";
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








    public String addItem(int tranDocID, int siteArea, String siteAddress, String itemName, int itemWeight, int amount, boolean cond){
        // TODO: check basic validity
        try {
            //TODO
        } catch (FileAlreadyExistsException e) {
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }

    public String removeItem(int tranDocID, int siteArea, String siteAddress, String ItemName, int itemWeight, int amount, boolean cond){
        // TODO: check basic validity
        try {
            //TODO
        } catch (FileAlreadyExistsException e) {
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }

    public String setItemCond(int tranDocID, int siteArea, String siteAddress, String ItemName, int itemWeight, int amount, boolean cond){
        // TODO: check basic validity
        try {
            //TODO
        } catch (FileAlreadyExistsException e) {
            return "";
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