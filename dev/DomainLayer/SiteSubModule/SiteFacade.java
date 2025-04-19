package DomainLayer.SiteSubModule;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.ArrayList;
import java.util.HashMap;

public class SiteFacade {
    private HashMap<Integer, ShippingArea> shippingAreas;
    private ArrayList<Site> no_Area_sites;  ///  Sites with area_num = -1 here, ArrayList so address Strings won't clash
    public SiteFacade() {
        shippingAreas = new HashMap<Integer, ShippingArea>();
        no_Area_sites = new ArrayList<Site>();   //   in the case their Shipping Area is deleted // if this is needed, ASK ABOUT THIS
    }

    public HashMap<Integer, ShippingArea> getShippingAreas() {return shippingAreas;}
    public void setShippingAreas(HashMap<Integer, ShippingArea> shippingAreas) {this.shippingAreas = shippingAreas;}
    public ArrayList<Site> getNo_Area_sites() {return no_Area_sites;}
    public void setNo_Area_sites(ArrayList<Site> no_Area_sites) {this.no_Area_sites = no_Area_sites;}

    public void addShippingArea(int areaNum, String areaName) throws KeyAlreadyExistsException {
        if (!shippingAreas.containsKey(areaNum)) {
            shippingAreas.put(areaNum, new ShippingArea(areaNum, areaName));
        }else {
            throw new KeyAlreadyExistsException("Shipping Area Number already exists");   //TODO:  Remember to catch(Service) this upwards and print(Presentation) approppriately
        }
    }


    public void deleteShippingArea(int areaNum){       //TODO:  ASK if we can even delete a shipping area if there a sites in there or maybe we can
        if (shippingAreas.containsKey(areaNum)) {

            ShippingArea shippingArea = shippingAreas.get(areaNum);   ///  this is for if we can't delete the sites inside that area
            for (Site site : shippingArea.getSites().values()) {   ///  this is for if we can't delete the sites inside that area
                site.setAddress(new Address(-1, site.getAddress().getAddress())); ///  set area number to -1 so indicates that no area   ///  this is for if we can't delete the sites inside that area
                no_Area_sites.add(site);
            }   ///  this is for if we can't delete the sites inside that area

            shippingAreas.remove(areaNum);   ///   <<<-----------------------  this is the main thing
        }

    }


    public void setShippingAreaNum(int oldAreaNum, int newAreaNum) throws ClassNotFoundException {
        if (shippingAreas.containsKey(newAreaNum)) {
            throw new KeyAlreadyExistsException("Shipping Area Number already exists");   //TODO: catch in upper layer
        }
        if (!shippingAreas.containsKey(oldAreaNum)) {
            throw new ClassNotFoundException("Shipping Area Number does not exist");   //TODO: catch in upper layer
        }else{

        }
    }

    public void setShippingAreaName(int areaName, String newAreaName){}

    public void addSite(int areaName , String address, String cName, int cNumber){}

    public void deleteSite(String address){}

    public void setSiteAddress(int areaNum, String oldAddress, String newAddress){}

    public void setSiteAreaNum(int oldAreaNum, int newAreaNum, String address){}


    public String showAllSites(){
        String res = "All Sites:\n";
        for (ShippingArea shippingArea : shippingAreas.values()) {
            res += shippingArea.toString();   //  they already have \n between them
        }
        return res;
    }
}
