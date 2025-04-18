package DomainLayer.SiteSubModule;

import java.util.HashMap;

public class ShippingArea {
    private HashMap<String, Site> sites;
    private int area_number;
    private String area_name;

    public ShippingArea(HashMap<String,Site> sites, int area_number, String area_name) {
        this.area_number = area_number;
        this.area_name = area_name;
        this.sites = sites;
    }

    public int getArea_number() {return area_number;}
    public void setArea_number(int area_number) {this.area_number = area_number;}

    public String getArea_name() {return area_name;}
    public void setArea_name(String area_name) {this.area_name = area_name;}
    public HashMap<String, Site> getSites() {return sites;}
    public void setSites(HashMap<String, Site> sites) {this.sites = sites;}

    public void addSite(String address, String cName, long cNumber ){}
    public void deleteSite(String address){}
    public void setSiteAddress(String oldAddress, String newAddress){}
    public void setAreaNum(int newAreaNum, String address){}

    @Override
    public String toString() {
        return "";                           //TODO     <<------------------------
    }
}
