package DomainLayer.TranSubModule;

import DomainLayer.EmpSubModule.Driver;
import DomainLayer.EmpSubModule.EmployeeFacade;
import DomainLayer.SiteSubModule.Site;
import DomainLayer.SiteSubModule.SiteFacade;
import DomainLayer.TruSubModule.Truck;
import DomainLayer.TruSubModule.TruckFacade;

import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;

public class TransportFacade {
    private HashMap<Integer, TransportDoc> transports;
    private HashMap<Integer, ItemsDoc> itemsDocs;
    private ArrayList<TransportDoc> queuedTransports;    ///TODO:  <<-------------------------    implement this functionality

    private EmployeeFacade employeeFacade;
    private SiteFacade siteFacade;
    private TruckFacade truckFacade;

//    private Thread queueCheckingThread;
//    private final Object lock = new Object();  // The thread will wait on this lock


    public TransportFacade(EmployeeFacade eF, SiteFacade sF, TruckFacade tF) {
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



    public void createTransport(int truckID, int driverID, int srcsiteAreaNum, String srcsiteAddress){  // time is decided when the Transport departs
        LocalDateTime now = LocalDateTime.now();

        //TODO
    }

    public void deleteTransport(int transportID){
        //TODO
    }






    public void setTransportStatus(int TranDocID, int menu_status_option){
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











    public void addTransportProblem(int TransportID, int menu_Problem_option){
        //TODO
    }

    public void removeTransportProblem(int TransportID, int menu_Problem_option){
        //TODO
    }








    public void addDestSite(int tran_ID, int siteArea, String siteAddress) {  // (throw msg exce if in new Area and give option)
        //TODO
    }

    public void removeDestSite(int tran_ID, int siteArea, String siteAddress){
        //TODO
    }








    public void addItem(int tranDocID, int siteArea, String siteAddress, String itemName, int itemWeight, int amount, boolean cond){
        //TODO
    }

    public void removeItem(int tranDocID, int siteArea, String siteAddress, String ItemName, int amount, boolean cond){
        //TODO
    }

    public void setItemCond(int tranDocID, int siteArea, String siteAddress, String ItemName, int amount, boolean cond){
        //TODO
    }










    public String showAllTransports(){
        String resOfAllTransports = "";
        for (TransportDoc t : transports.values()){
            resOfAllTransports += t.toString() + "\n";
        }
        return resOfAllTransports;
    }


//    public String itemsToString(int ItemsDocID){
///        return "";  //   might not be needed
//    }
//
//    public String transportToString(int TranDocID){
///        return "";  //   might not be needed
//    }




}
