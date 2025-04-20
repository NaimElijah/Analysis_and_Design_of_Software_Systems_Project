package ServiceLayer;

import DomainLayer.TranSubModule.TransportDoc;
import DomainLayer.TranSubModule.TransportFacade;
import com.sun.jdi.connect.Transport;
import java.time.*;

public class TransportService {
    private TransportFacade tran_f;
    public TransportService(TransportFacade tf) {
        this.tran_f = tf;
    }



    public String createTransport(int truckID, int driverID, int srcsiteAreaNum, String srcsiteAddress){
        //TODO
    }

    public String deleteTransport(int transportID){
        //TODO
    }







    public String setTransportStatus(int TranDocID, int menu_status_option){
        //TODO
    }

    public String setTransportTruck(int TranDocID, int truckNum){
        //TODO
    }

    public String setTransportDriver(int TranDocID, int DriverID){
        //TODO
    }







    public String checkTransportValidity(int transportID){

        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";  //TODO   //TODO:   maybe also when a driver/truck are unavailable we can choose to put in waitqueue or try to choose another
    }

    public String checkIfFirstQueuedTransportsCanGo(){
        String res = "";
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    public String addTransportToWaitQueue(int transportID){
        //TODO
    }









    public void setSiteArrivalIndexInTransport(int transportID, int siteArea, String siteAddress, int index){
        //TODO  //TODO   <<<-------------  ADD OPTION FOR TRANSPORT SITES ORDER EDITION, THE ORDER IS THE ARRAYLIST's ORDER    <<<----------
    }











    public String addTransportProblem(int TransportID, int menu_Problem_option){
        //TODO
    }

    public String removeTransportProblem(int TransportID, int menu_Problem_option){
        //TODO
    }









    public String addDestSite(int tran_ID, int siteArea, String siteAddress) {  // (throw msg exce if in new Area and give option)
        //TODO
    }

    public String removeDestSite(int tran_ID, int siteArea, String siteAddress){
        //TODO
    }








    public String addItem(int tranDocID, int siteArea, String siteAddress, String itemName, int itemWeight, int amount, boolean cond){
        //TODO
    }

    public String removeItem(int tranDocID, int siteArea, String siteAddress, String ItemName, int amount, boolean cond){
        //TODO
    }

    public String setItemCond(int tranDocID, int siteArea, String siteAddress, String ItemName, int amount, boolean cond){
        //TODO
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


//    public String transportToString(int transportDocId) {
//        //todo   //  don't think this is needed
//        return"";
//    }



}