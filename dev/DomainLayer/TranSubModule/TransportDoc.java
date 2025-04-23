package DomainLayer.TranSubModule;

import DomainLayer.EmpSubModule.Driver;
import DomainLayer.SiteSubModule.Site;
import DomainLayer.TruSubModule.Truck;

import java.sql.Time;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.*;
import java.util.HashMap;

public class TransportDoc {
    private enumTranStatus status;
    private int tran_Doc_ID;
    private LocalDateTime departure_dt;
    private Truck transportTruck;
    private Driver transportDriver;
    private int truck_Depart_Weight;
    private Site src_site;
    private ArrayList<ItemsDoc> dests_Docs;  ///  <<<--------------  In Order of visit   <<<--------------------
    private ArrayList<enumTranProblem> problems;
    private boolean isProvider;    // TODO  !!!!!!  if true then provider and roles in this clas change(src is gonna from where we get the items(provider itself)....)  <<--------

    public TransportDoc(enumTranStatus status, int tran_Doc_ID, Truck transportTruck, Driver transportDriver, Site src_site) {
        this.status = status;
        this.tran_Doc_ID = tran_Doc_ID;
        this.departure_dt = LocalDateTime.now();   //TODO:  look more into this   // when really departing after the check, set this to departure datetime
        this.transportTruck = transportTruck;
        this.transportDriver = transportDriver;
        this.truck_Depart_Weight = -1;   //  Initialization, calculated before being sent
        this.src_site = src_site;
        this.dests_Docs = new ArrayList<ItemsDoc>();
        this.problems = new ArrayList<enumTranProblem>();
    }

    public enumTranStatus getStatus() {return status;}
    public void setStatus(enumTranStatus status) {this.status = status;}
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
    public ArrayList<enumTranProblem> getProblems() {return problems;}
    public void setProblems(ArrayList<enumTranProblem> problems_descriptions) {this.problems = problems_descriptions;}
    public boolean isProvider() {return isProvider;}
    public void setProvider(boolean provider) {isProvider = provider;}

    public int addDestSite(Site dest, int itemsDoc_num){
        for(ItemsDoc itemsDoc : dests_Docs){
            if (itemsDoc.getDest_site().getAddress().getArea() == dest.getAddress().getArea() && itemsDoc.getDest_site().getAddress().getAddress().equals(dest.getAddress().getAddress())){
                return -1;  //  Destination Site already in this Transport, do add/remove item from that site instead.
            }
        }
        ItemsDoc addition = new ItemsDoc(itemsDoc_num, this.src_site, dest);
        dests_Docs.add(addition);
        return 0;  // all good
    }

    public int removeDestSite(int itemsDoc_num){
        ItemsDoc temp = null;
        for (ItemsDoc itemsDoc : dests_Docs) {
            if(itemsDoc.getItemDoc_num() == itemsDoc_num){
                temp = itemsDoc;
            }
        }
        if (temp == null) {
            return -1;  // cannot remove a dest site that isn't in this transport
        }
        dests_Docs.remove(temp);
        return 0;  //  all good
    }



    public void setSiteArrivalIndexInTransport(int siteArea, String siteAddress, int index) {
        ItemsDoc tempForReInsertionAtGivenIndex = null;
        for (ItemsDoc itemsDoc : dests_Docs) {
            if (itemsDoc.getDest_site().getAddress().getArea() == siteArea && itemsDoc.getDest_site().getAddress().getAddress().equals(siteAddress)) {
                tempForReInsertionAtGivenIndex = itemsDoc;
                break;  // found
            }
        }
        this.dests_Docs.remove(tempForReInsertionAtGivenIndex);
        this.dests_Docs.add(index - 1, tempForReInsertionAtGivenIndex);
    }







    public int calculateTransportItemsWeight(){
        int sum = 0;
        for (ItemsDoc itemsDoc : dests_Docs) {
            sum += itemsDoc.calculateItemsWeight();
        }
        return sum;
    }




    public int addTransportProblem(enumTranProblem problem){
        if (problems.contains(problem)) {
            return -1;  // already have that problem
        }
        this.problems.add(problem);
        return 0;  // all good
    }

    public int removeTransportProblem(enumTranProblem problem){
        if (!problems.contains(problem)) {
            return -1;  // can't delete a problem that didn't exist
        }
        this.problems.remove(problem);
        return 0;  // all good
    }






    public int addItem(int itemsDoc_Num, String ItemName, int itemWeight, int amount, boolean cond){
        int res = 0;
        for (ItemsDoc itemsDoc : dests_Docs) {
            if (itemsDoc.getItemDoc_num() == itemsDoc_Num){
                res = itemsDoc.addItem(ItemName, itemWeight, cond, amount);
            }
        }
        return res;
    }

    public int removeItem(int itemsDoc_Num, String ItemName, int itemWeight, int amount, boolean cond){
        int res = 0;
        for (ItemsDoc itemsDoc : dests_Docs) {
            if (itemsDoc.getItemDoc_num() == itemsDoc_Num){
                res = itemsDoc.removeItem(ItemName, itemWeight, cond, amount);
            }
        }
        return res;
    }

    public boolean setItemCond(int itemsDoc_Num, String ItemName, int itemWeight, int amount, boolean newCond){
        boolean res = true;
        for (ItemsDoc itemsDoc : dests_Docs) {
            if (itemsDoc.getItemDoc_num() == itemsDoc_Num){
                res = itemsDoc.setItemCond(ItemName, itemWeight, amount, newCond);
            }
        }
        return res;
    }






    @Override
    public String toString() {
        String res = "Transport Document ID: " + tran_Doc_ID + ", Status: " + status + ", Departure Time: " + this.departure_dt.toString() + ", \n";
        res += "Truck: " + this.transportTruck.toString() + ", Driver: " + this.transportDriver.toString() + ",\nTruck departure weight: " + this.truck_Depart_Weight;
        res += ", Source Site: " + this.src_site.toString() + ", Transport Problems: (";

        for (enumTranProblem problem : problems) { res += problem.toString() + ", "; }
        res = res.substring(0, res.length() - 2);
        res += "), Transport Sites & Items:\n";

        for (ItemsDoc itemsdoc : dests_Docs) {
            res += itemsdoc.toString() + "\n";
        }

        return res;
    }


}

