package DomainLayer.EmpSubModule;

import java.util.ArrayList;

public class Driver extends Employee{
    private ArrayList<Character> licenses;

    public Driver(int id, String fname, String lname, int permissions, ArrayList<Character> licenses) {
        super(id, fname, lname, permissions);
        this.licenses = licenses;
    }

    private ArrayList<Character> getLicenses() {return licenses;}
    private void setLicenses(ArrayList<Character> licenses) {this.licenses = licenses;}

    private void addLicense(Character c){
        if(!licenses.contains(c)){
            licenses.add(c);
        }
    }

    private void removeLicense(Character c){licenses.remove(c);}

    @Override
    public String toString() {
        return "";                                  //TODO       <<-------------------
    }
}
