package DomainLayer.TranSubModule;

import DomainLayer.EmpSubModule.Driver;
import DomainLayer.SiteSubModule.Site;
import DomainLayer.TruSubModule.Truck;
import DomainLayer.enums.enumTranProblem;
import DomainLayer.enums.enumTranStatus;

import java.util.ArrayList;
import java.time.LocalDateTime;

public class TransportDoc {
    private enumTranStatus status;
    private int tran_Doc_ID;
    private LocalDateTime departure_dt;
    private Truck transportTruck;
    private Driver transportDriver;
    private double truck_Depart_Weight;
    private Site src_site;
    private ArrayList<ItemsDoc> dests_Docs;  ///  <<<--------------  In Order of visit   <<<--------------------
    private ArrayList<enumTranProblem> problems;

    public TransportDoc(enumTranStatus status, int tran_Doc_ID, Truck transportTruck, Driver transportDriver, Site src_site) {
        this.status = status;
        this.tran_Doc_ID = tran_Doc_ID;
        this.departure_dt = LocalDateTime.now();   // In the code, if it doesn't depart immediately, this always updates right before genuine departure.
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


    public void setTransportTruck(Truck transportTruck) {
        if (this.transportTruck.getInTransportID() == this.tran_Doc_ID) {  // if another truck belongs to the transport already
            this.transportTruck.setInTransportID(-1);  // unassign old one
        }
        this.transportTruck = transportTruck;
        if (this.status == enumTranStatus.InTransit || this.status == enumTranStatus.BeingAssembled || this.status == enumTranStatus.BeingDelayed) {
            this.transportTruck.setInTransportID(this.tran_Doc_ID);   ///  only if the current Transport is active
        }
    }

    public Driver getTransportDriver() {return transportDriver;}

    public void setTransportDriver(Driver transportDriver) {
        if (this.transportDriver.getInTransportID() == this.tran_Doc_ID) {  // if another driver belongs to the transport already
            this.transportDriver.setInTransportID(-1);  // unassign old one
        }
        this.transportDriver = transportDriver;
        if (this.status == enumTranStatus.InTransit || this.status == enumTranStatus.BeingAssembled || this.status == enumTranStatus.BeingDelayed) {
            this.transportDriver.setInTransportID(this.tran_Doc_ID);   ///  only if the current Transport is active
        }
    }


    public double getTruck_Depart_Weight() {return truck_Depart_Weight;}
    public void setTruck_Depart_Weight(double truck_Depart_Weight) {this.truck_Depart_Weight = truck_Depart_Weight;}
    public Site getSrc_site() {return src_site;}
    public void setSrc_site(Site src_site) {this.src_site = src_site;}
    public ArrayList<ItemsDoc> getDests_Docs() {return dests_Docs;}
    public void setDests_Docs(ArrayList<ItemsDoc> dests_Docs) {this.dests_Docs = dests_Docs;}
    public ArrayList<enumTranProblem> getProblems() {return problems;}
    public void setProblems(ArrayList<enumTranProblem> problems_descriptions) {this.problems = problems_descriptions;}




    public ItemsDoc addDestSite(int itemsDoc_num, Site dest){
        for(ItemsDoc itemsDoc : dests_Docs){
            if (itemsDoc.getDest_site().getAddress().getArea() == dest.getAddress().getArea() && itemsDoc.getDest_site().getAddress().getAddress().equals(dest.getAddress().getAddress())){
                return null;  //  Destination Site already in this Transport, do add/remove item from that site instead. Every Site has a primary key which is the address.
            }
        }
        ItemsDoc addition = new ItemsDoc(itemsDoc_num, this.src_site, dest);
        dests_Docs.add(addition);
        return addition;  // all good
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







    public double calculateTransportItemsWeight(){
        double sum = 0;
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






    public int addItem(int itemsDoc_Num, String ItemName, double itemWeight, int amount, boolean cond){
        int res = 0;
        for (ItemsDoc itemsDoc : dests_Docs) {
            if (itemsDoc.getItemDoc_num() == itemsDoc_Num){
                res = itemsDoc.addItem(ItemName, itemWeight, cond, amount);    //Note:  this function is needed to do something with an Item using the transport in hand.
            }
        }
        return res;
    }

    public int removeItem(int itemsDoc_Num, String ItemName, double itemWeight, int amount, boolean cond){
        int res = 0;
        for (ItemsDoc itemsDoc : dests_Docs) {
            if (itemsDoc.getItemDoc_num() == itemsDoc_Num){
                res = itemsDoc.removeItem(ItemName, itemWeight, cond, amount);    //Note:  this function is needed to do something with an Item using the transport in hand.
            }
        }
        return res;
    }

    public int setItemCond(int itemsDoc_Num, String ItemName, double itemWeight, int amount, boolean newCond){
        int res = 0;
        for (ItemsDoc itemsDoc : dests_Docs) {
            if (itemsDoc.getItemDoc_num() == itemsDoc_Num){
                res = itemsDoc.setItemCond(ItemName, itemWeight, amount, newCond);    //Note:  this function is needed to do something with an Item using the transport in hand.
            }
        }
        return res;
    }






    @Override
    public String toString() {
        String res = "Transport Document ID #: " + tran_Doc_ID + ", Status: " + status + ", Departure Time: " + this.departure_dt.toString() + ", \n";
        res += "Truck: " + this.transportTruck.toString() + ".\nDriver: " + this.transportDriver.toString() + ",\nTruck departure weight: " + this.truck_Depart_Weight;
        res += ", Source Site: " + this.src_site.toString() + ", Transport Problems: (";

        for (enumTranProblem problem : problems) { res += problem.toString() + ", "; }
        res = res.substring(0, res.length() - 2);
        res += "), Destination Sites & Items (In order of arrival):\n";

        for (ItemsDoc itemsdoc : dests_Docs) { res += itemsdoc.toString() + "\n"; }

        return res;
    }


}

