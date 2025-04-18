package DomainLayer.TruSubModule;

public class Truck {
    private int truck_num;
    private String model;
    private int net_weight;
    private int max_carry_weight;
    private String valid_license; //todo ENums;
    private boolean isFree;

    public Truck(int truck_num, String model, int net_weight, int max_carry_weight, String valid_license) {
        this.truck_num = truck_num;
        this.model = model;
        this.net_weight = net_weight;
        this.max_carry_weight = max_carry_weight;
        this.valid_license = valid_license;
    }

    public void setTruck_num(int truck_num) {this.truck_num = truck_num;}
    public void setModel(String model) {this.model = model;}
    public void setNet_weight(int net_weight) {this.net_weight = net_weight;}
    public void setMax_carry_weight(int max_carry_weight) {this.max_carry_weight = max_carry_weight;}
    public void setValid_license(String valid_license) {this.valid_license = valid_license;}
    public void setFree(boolean free) {isFree = free;}
    public int getTruck_num() {return truck_num;}
    public String getModel() {return model;}
    public int getNet_weight() {return net_weight;}
    public int getMax_carry_weight() {return max_carry_weight;}
    public String getValid_license() {return valid_license;}
    public boolean isFree() {return isFree;}

    public String toString(){
        return "";                 //TODO    <<-------------------
    }

}
