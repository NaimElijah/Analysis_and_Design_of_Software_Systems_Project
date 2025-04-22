package DomainLayer.TranSubModule;

import DomainLayer.EmpSubModule.Driver;
import DomainLayer.EmpSubModule.EmployeeFacade;
import DomainLayer.SiteSubModule.Site;
import DomainLayer.SiteSubModule.SiteFacade;
import DomainLayer.TruSubModule.Truck;
import DomainLayer.TruSubModule.TruckFacade;
import PresentationLayer.DTOs.ItemsDocDTO;
import PresentationLayer.DTOs.TransportDTO;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;

public class TransportFacade {
    private HashMap<Integer, TransportDoc> transports;
    private int transportIDCounter;     ///   <<<---------------------------------    for the transport Docs ID's
    private HashMap<Integer, ItemsDoc> itemsDocs;  // to know a ItemsDoc's num is unique and also for connection.
    private ArrayList<TransportDoc> queuedTransports;    ///TODO:  <<-------------------------    implement this functionality

    private EmployeeFacade employeeFacade;
    private SiteFacade siteFacade;
    private TruckFacade truckFacade;

//    private Thread queueCheckingThread;
//    private final Object lock = new Object();  // The thread will wait on this lock


    public TransportFacade(EmployeeFacade eF, SiteFacade sF, TruckFacade tF) {
        this.transportIDCounter = 0;
        this.transports = new HashMap<Integer, TransportDoc>();
        this.itemsDocs = new HashMap<Integer, ItemsDoc>();
        this.queuedTransports = new ArrayList<TransportDoc>();    ///TODO:  <<-------------------------    implement this
        this.employeeFacade = eF;
        this.siteFacade = sF;
        this.truckFacade = tF;

//        this.queueCheckingThread = new Thread(new Runnable() {
//            public void run() {
//                synchronized (lock) {
//                    try {
//                        while (true) {
//                            checkIfQueuedTransportsCanGoWithThread();
//                            lock.wait();  // waits until it's notified
//                        }
//                    } catch (InterruptedException e) {
//                        Thread.currentThread().interrupt();
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        this.queueCheckingThread.start();

    }




//    synchronized (lock) {
///        lock.notify(); // or lock.notifyAll();                     TODO:   <<<--------------   when a driver or a truck are free after Transport completion/cancelation
//    }


    public HashMap<Integer, TransportDoc> getTransports() {return transports;}
    public void setTransports(HashMap<Integer, TransportDoc> transports) {this.transports = transports;}
    public HashMap<Integer, ItemsDoc> getItemsDocs() {return itemsDocs;}
    public void setItemsDocs(HashMap<Integer, ItemsDoc> itemsDocs) {this.itemsDocs = itemsDocs;}





    public void createTransport(TransportDTO transportDTO){  // time is decided when the Transport departs
        LocalDateTime now = LocalDateTime.now();

        //TODO    <<<<----------------------------------------   CONTINUE FROM HERE in the facade after DOING THE MENUS IN THE PRESENTATION LAYER !!!!!!   <<-----------
        //TODO    <<<<----------------------------------------   CONTINUE FROM HERE in the facade after DOING THE MENUS IN THE PRESENTATION LAYER !!!!!!   <<-----------
        //TODO    <<<<----------------------------------------   CONTINUE FROM HERE in the facade after DOING THE MENUS IN THE PRESENTATION LAYER !!!!!!   <<-----------

        //TODO

//        ///TODO:  check if added some Items for the same Site and add them together
//        for(ItemsDocDTO itemsDocDTO : dests_Docs_for_Transport){
//
//        }

        //TODO: also if good then change driver's and truck's isFrees
    }


    public void setTransportStatus(int TranDocID, int menu_status_option){
        //TODO
        //TODO:  according to the new status change everything relevant(like driver and truck isFree)
    }


    public void deleteTransport(int transportID) throws FileNotFoundException {
        if(!transports.containsKey(transportID)){
            throw new FileNotFoundException();
        }
        //TODO: delete everything inside of the transport and change statuses of
        transports.remove(transportID);
        //TODO
    }






    public void setTransportTruck(int TranDocID, int truckNum){
        //TODO
    }

    public void setTransportDriver(int TranDocID, int DriverID){
        //TODO
    }










    public String checkTransportValidityHelperFunction(int transportID){   //  <<<--------------------  only in this layer as a helper function
        return "";  //TODO //TODO:   maybe also when a driver/truck are unavailable we can choose to put in waitqueue or try to choose another
    }

    public String checkTransportValidity(int transportID) throws FileAlreadyExistsException{
        String validityRes = checkTransportValidityHelperFunction(transportID);
        return "";  //TODO //TODO:   maybe also when a driver/truck are unavailable we can choose to put in waitqueue or try to choose another
    }


//    public void checkIfQueuedTransportsCanGoWithThread(){   //  <<<------------------  only in this layer for the thread
//        if(!this.queuedTransports.isEmpty()){
//            try {
//                if(this.checkTransportValidity(queuedTransports.get(0)).equals("Valid")){
//                    queuedTransports.get(0).setDeparture_dt(LocalDateTime.now());
//                    //TODO: send the transport at index 0
//                }
//            } catch (FileAlreadyExistsException e) {
//                //  here are the things the checkTransportValidity function throws, we'll just do nothing if there's still a problem becuse it's in the queue still
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public void checkIfFirstQueuedTransportsCanGo(){
        if(!this.queuedTransports.isEmpty()){
            if(this.checkTransportValidityHelperFunction(queuedTransports.get(0).getTran_Doc_ID()).equals("Valid")){
                queuedTransports.get(0).setDeparture_dt(LocalDateTime.now());
                //TODO: send the transport at index 0 and return to upper layers that this happened

            }
        }
    }


    public void addTransportToWaitQueue(int transportID){
        //TODO
    }











    public void setSiteArrivalIndexInTransport(int transportID, int siteArea, String siteAddress, int index){
        //TODO  //TODO   <<<-------------  ADD OPTION FOR TRANSPORT SITES ORDER EDITION, THE ORDER IS THE ARRAYLIST's ORDER    <<<----------
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






    public void addTransportProblem(int TransportID, int menu_Problem_option){
        //TODO
    }

    public void removeTransportProblem(int TransportID, int menu_Problem_option){
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
        String resOfAllTransports = "";
        for (TransportDoc t : transports.values()){
            resOfAllTransports += t.toString() + "\n";
        }
        return resOfAllTransports;
    }




}
