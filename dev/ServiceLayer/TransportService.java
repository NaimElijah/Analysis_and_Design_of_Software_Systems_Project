package ServiceLayer;

import DomainLayer.TranSubModule.ItemsDoc;
import DomainLayer.TranSubModule.TransportDoc;
import DomainLayer.TranSubModule.TransportFacade;
import com.sun.jdi.connect.Transport;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.time.*;

public class TransportService {
    private TransportFacade tran_f;
    public TransportService(TransportFacade tf) {
        this.tran_f = tf;
    }



    public String createTransport(int truckID, int driverID, int srcsiteAreaNum, String srcsiteAddress){
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

    public String deleteTransport(int transportID){
        // TODO: check basic validity
        try {
            //TODO
        } catch (FileNotFoundException e) {
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }







    public String setTransportStatus(int TranDocID, int menu_status_option){
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







    public String checkTransportValidity(int transportID){
        // TODO: check basic validity
        try {
            //TODO
        } catch (FileAlreadyExistsException e) {
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good   //TODO:   maybe also when a driver/truck are unavailable we can choose to put in waitqueue or try to choose another
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


    public String addTransportToWaitQueue(int transportID){
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
        //TODO   <<<-------------  ADD OPTION FOR TRANSPORT SITES ORDER EDITION, THE ORDER IS THE ARRAYLIST's ORDER    <<<----------
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











    public String addTransportProblem(int TransportID, int menu_Problem_option){
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

    public String removeTransportProblem(int TransportID, int menu_Problem_option){
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








    public String addItem(int tranDocID, int siteArea, String siteAddress, int itemId, String itemName, int itemWeight, int amount, boolean cond){
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

    public String removeItem(int tranDocID, int siteArea, String siteAddress, int itemId, String ItemName, int itemWeight, int amount, boolean cond){
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

    public String setItemCond(int tranDocID, int siteArea, String siteAddress, int itemId, String ItemName, int itemWeight, int amount, boolean cond){
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