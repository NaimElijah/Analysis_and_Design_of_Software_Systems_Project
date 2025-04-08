package DomainLayer;

import java.util.ArrayList;

public class Truck {
    private int truck_num;
    private String model;
    private int net_weight;
    private int max_carry_weight;
    private ArrayList<Character> valid_licenses;

    public Truck(int truck_num, String model, int net_weight, int max_carry_weight, ArrayList<Character> valid_licenses) {
        this.truck_num = truck_num;
        this.model = model;
        this.net_weight = net_weight;
        this.max_carry_weight = max_carry_weight;
        this.valid_licenses = valid_licenses;
    }

    String toString(){
        String s = "";
        //TODO
        return s;
    }
}
