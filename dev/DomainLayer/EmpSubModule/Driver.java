package DomainLayer.EmpSubModule;

import java.util.ArrayList;

public class Driver extends Employee{
    private ArrayList<String> licenses;

    public Driver(int id, String fname, String lname, int permissions, ArrayList<String> licenses) {
        super(id, fname, lname, permissions);
        this.licenses = licenses;
    }

    private ArrayList<String> getLicenses() {return licenses;}
    private void setLicenses(ArrayList<String> licenses) {this.licenses = licenses;}

    private void addLicense(String s){
        if(!licenses.contains(s)){
            licenses.add(s);
        }
    }

    private void removeLicense(Character c){licenses.remove(c);}

    @Override
    public String toString() {
        return "";                                  //TODO       <<-------------------
    }
}
