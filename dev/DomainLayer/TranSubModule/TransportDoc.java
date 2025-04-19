package DomainLayer.TranSubModule;

import DomainLayer.SiteSubModule.Site;
import DomainLayer.TruSubModule.Truck;

import java.sql.Driver;
import java.sql.Time;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.*;

public class TransportDoc {
    private char status; // 'C'-Canceled,'D'-Delayed,'F'-Finished,'IP'-InProgress
    private int tran_Doc_ID;
    private LocalDateTime departure_dt;
    private Truck transportTruck;
    private Driver transportDriver;
    private int truck_Depart_Weight;
    private Site src_site;
    private ArrayList<ItemsDoc> dests_Docs;  // In Order of visit
    private ArrayList<String> problems_descriptions;
//    private int problems_Level;     //  maybe not needed


    public TransportDoc(char status, int tran_Doc_ID, Truck transportTruck, Driver transportDriver, int truck_Depart_Weight, Site src_site) {
        this.status = status;
        this.tran_Doc_ID = tran_Doc_ID;
        this.departure_dt = LocalDateTime.now();   // when really departing after the check, set this to departure datetime
        this.transportTruck = transportTruck;
        this.transportDriver = transportDriver;
        this.truck_Depart_Weight = truck_Depart_Weight;
        this.src_site = src_site;
        this.dests_Docs = new ArrayList<ItemsDoc>();
        this.problems_descriptions = new ArrayList<String>();
    }

    public char getStatus() {return status;}
    public void setStatus(char status) {this.status = status;}
    public int getTran_Doc_ID() {return tran_Doc_ID;}
    public void setTran_Doc_ID(int tran_Doc_ID) {this.tran_Doc_ID = tran_Doc_ID;}
    public LocalDateTime getDeparture_dt() {return departure_dt;}
    public void setDeparture_dt(LocalDateTime departure_dt) {this.departure_dt = departure_dt;}
    public Truck getTransportTruck() {return transportTruck;}
    public void setTransportTruck(Truck transportTruck) {this.transportTruck = transportTruck;}
    public Driver getTransportDriver() {return transportDriver;}
    public void setTransportDriver(Driver transportDriver) {this.transportDriver = transportDriver;}
    public int getTruck_Depart_Weight() {return truck_Depart_Weight;}
    public void setTruck_Depart_Weight(int truck_Depart_Weight) {this.truck_Depart_Weight = truck_Depart_Weight;}
    public Site getSrc_site() {return src_site;}
    public void setSrc_site(Site src_site) {this.src_site = src_site;}
    public ArrayList<ItemsDoc> getDests_Docs() {return dests_Docs;}
    public void setDests_Docs(ArrayList<ItemsDoc> dests_Docs) {this.dests_Docs = dests_Docs;}
    public ArrayList<String> getProblems_descriptions() {return problems_descriptions;}
    public void setProblems_descriptions(ArrayList<String> problems_descriptions) {this.problems_descriptions = problems_descriptions;}


    public void addDestSite(int site_menu_index){   // throw msg if in new Area and give option
        //TODO
    }

    public void addTransportProblem(String problem){
        //TODO
    }

    public void removeDestSite(int site_menu_index){
        //TODO
    }

    public void addItem(Item item, int amount, Site s){
        //TODO
    }

    public void removeItem(String ItemName, int amount, Site s, boolean cond){
        //TODO
    }

    public void addTransportProblem(int TrNum, String problem){
        //TODO
    }

    public String checkTransportValidity(TransportDoc transport){    // throws different exceptions according to case
        return "";   //TODO
    }

    public int calculateTransportWeight(){
        return 0;  //TODO
    }

    @Override
    public String toString() {
        return "";  //TODO
    }
}
