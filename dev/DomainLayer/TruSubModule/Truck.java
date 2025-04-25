package DomainLayer.TruSubModule;

import DomainLayer.TranSubModule.TransportDoc;
import DomainLayer.enumDriLicense;

public class Truck {
    private int truck_num;
    private String model;
    private int net_weight;
    private int max_carry_weight;
    private enumDriLicense valid_license;
    private int inTransportID;

    public Truck(int truck_num, String model, int net_weight, int max_carry_weight, enumDriLicense valid_license) {
        this.truck_num = truck_num;
        this.model = model;
        this.net_weight = net_weight;
        this.max_carry_weight = max_carry_weight;
        this.valid_license = valid_license;
        this.inTransportID = -1;  // not assigned yet
    }

    public void setTruck_num(int truck_num) {this.truck_num = truck_num;}
    public void setModel(String model) {this.model = model;}
    public void setNet_weight(int net_weight) {this.net_weight = net_weight;}
    public void setMax_carry_weight(int max_carry_weight) {this.max_carry_weight = max_carry_weight;}
    public void setValid_license(enumDriLicense valid_license) {this.valid_license = valid_license;}
    public int getTruck_num() {return truck_num;}
    public String getModel() {return model;}
    public int getNet_weight() {return net_weight;}
    public int getMax_carry_weight() {return max_carry_weight;}
    public enumDriLicense getValid_license() {return valid_license;}
    public int getInTransportID() {return inTransportID;}
    public void setInTransportID(int inTransportID) {this.inTransportID = inTransportID;}


    public String toString(){
        String res = "";
        res += "Truck Num: " + truck_num + ", Model: " + model + ", Net Weight: " + net_weight + ", Max Carry Weight: " + max_carry_weight + ", Valid License: " + valid_license.toString() + ", Availability: " + (this.inTransportID == -1 ? "Free" : "Occupied with Transport ID:" + this.inTransportID);
        return res;
    }

}
