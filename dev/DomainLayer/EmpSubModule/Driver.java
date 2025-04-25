package DomainLayer.EmpSubModule;

import DomainLayer.enumDriLicense;

import java.util.ArrayList;

public class Driver extends Employee{
    private ArrayList<enumDriLicense> licenses;
    private int inTransportID;


    public Driver(int id, String fname, String lname, int permissions, ArrayList<enumDriLicense> licenses) {
        super(id, fname, lname, permissions);
        this.licenses = licenses;
        this.inTransportID = -1;  // not assigned yet
    }

    public ArrayList<enumDriLicense> getLicenses() {return licenses;}
    public void setLicenses(ArrayList<enumDriLicense> licenses) {this.licenses = licenses;}
    public int getInTransportID() {return inTransportID;}
    public void setInTransportID(int inTransportID) {this.inTransportID = inTransportID;}

    public void addLicense(enumDriLicense s){
        if(!licenses.contains(s)){ licenses.add(s); }
    }

    public void removeLicense(enumDriLicense c){licenses.remove(c);}

    @Override
    public String toString() {
        String res = super.toString() + ", Availability: " + (this.inTransportID == -1 ? "Free" : "Occupied with Transport ID:" + this.inTransportID);
        res += "\nDriver Driving Licenses: ";
        for(int i = 0; i<licenses.size(); i++){
            res += licenses.get(i).toString();
            if(i < licenses.size()-1){
                res += ", ";
            }
        }
        res += ".";
        return res;
    }
}
