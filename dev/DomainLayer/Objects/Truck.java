package DomainLayer.Objects;

public class Truck {
    private int truck_num;
    private String model;
    private int net_weight;
    private int max_carry_weight;
    private Character valid_license;

    public Truck(int truck_num, String model, int net_weight, int max_carry_weight, Character valid_license) {
        this.truck_num = truck_num;
        this.model = model;
        this.net_weight = net_weight;
        this.max_carry_weight = max_carry_weight;
        this.valid_license = valid_license;
    }

    public String toString(){
        return "";                 //TODO    <<-------------------
    }

}
