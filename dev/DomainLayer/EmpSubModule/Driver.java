package DomainLayer.EmpSubModule;

import java.util.ArrayList;

public class Driver extends Employee{
    private ArrayList<String> licenses;
    private boolean isFree;  // to know his current status


    public Driver(int id, String fname, String lname, int permissions, ArrayList<String> licenses) {
        super(id, fname, lname, permissions);
        this.licenses = licenses;
        this.isFree = true;
    }

    private ArrayList<String> getLicenses() {return licenses;}
    private void setLicenses(ArrayList<String> licenses) {this.licenses = licenses;}
    public boolean isFree() {return isFree;}
    public void setFree(boolean free) {isFree = free;}

    private void addLicense(String s){
        if(!licenses.contains(s)){
            licenses.add(s);
        }
    }

    private void removeLicense(Character c){licenses.remove(c);}

    @Override
    public String toString() {
        String res = super.toString() + ", Availability: " + (isFree ? "Free" : "Occupied");
        res += "\nDriver Driving Licenses: ";
        for(int i = 0; i<licenses.size(); i++){
            res += licenses.get(i);
            if(i < licenses.size()-1){
                res += ", ";
            }
        }
        res += ".";
        return res;
    }
}
