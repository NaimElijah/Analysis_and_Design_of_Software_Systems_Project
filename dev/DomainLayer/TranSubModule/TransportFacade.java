package DomainLayer.TranSubModule;

import DomainLayer.EmpSubModule.Driver;
import DomainLayer.SiteSubModule.Site;
import DomainLayer.TruSubModule.Truck;
import com.sun.jdi.connect.Transport;

import java.lang.ref.PhantomReference;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PrimitiveIterator;

public class TransportFacade {
    private HashMap<Integer, TransportDoc> transports;
    private int transportsAmount;
    private HashMap<Integer, ItemsDoc> itemsDocs;

    public TransportFacade() {
        this.transports = new HashMap<Integer, TransportDoc>();
        this.transportsAmount = transportsAmount;
        this.itemsDocs = new HashMap<Integer, ItemsDoc>();
    }

    public HashMap<Integer, TransportDoc> getTransports() {return transports;}
    public void setTransports(HashMap<Integer, TransportDoc> transports) {this.transports = transports;}
    public int getTransportsAmount() {return transportsAmount;}
    public void setTransportsAmount(int transportsAmount) {this.transportsAmount = transportsAmount;}
    public HashMap<Integer, ItemsDoc> getItemsDocs() {return itemsDocs;}
    public void setItemsDocs(HashMap<Integer, ItemsDoc> itemsDocs) {this.itemsDocs = itemsDocs;}


    public void createTransport(LocalDateTime d, Truck t, Driver driver, Site src){
        //TODO
    }

    public void deleteTransport(int transportID){
        //TODO
    }

    public void setTransportStatus(int TranDocID, char status){
        //TODO
    }

    public void setTransportTruck(int TranDocID, int truckNum){
        //TODO
    }

    public void setTransportDriver(int TranDocID, int DriverID){
        //TODO
    }

    public String checkTransportValidity(TransportDoc transport){
        return "";  //TODO
    }

    public void addTransportProblem(int TransportID, String problem){
        //TODO
    }

    public void addDestSite(int tran_ID, int site_menu_index) {  // (throw msg exce if in new Area and give option)
        //TODO
    }

    public void removeDestSite(int tran_ID, int site_menu_index){
        //TODO
    }

    public void addItem(Site s, Item item, int amount){
        //TODO
    }

    public void removeItem(Site s, String ItemName, int amount, boolean cond){
        //TODO
    }

    public void setItemCond(Site s, String ItemName, int amount, boolean cond){
        //TODO
    }

    public String itemsToString(int ItemsDocID){
        return "";  //TODO
    }

    public String transportToString(int TranDocID){
        return "";  //TODO
    }

    public String showAllTransports(){
        String resOfAllTransports = "";
        for (TransportDoc t : transports.values()){
            resOfAllTransports += t.toString() + "\n";
        }
        return resOfAllTransports;
    }







}
