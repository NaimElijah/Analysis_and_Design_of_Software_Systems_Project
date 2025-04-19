package DomainLayer.SiteSubModule;

import javax.management.AttributeNotFoundException;
import javax.management.openmbean.KeyAlreadyExistsException;
import javax.naming.ContextNotEmptyException;
import java.util.HashMap;

public class SiteFacade {
    private HashMap<Integer, ShippingArea> shippingAreas;

    public SiteFacade() {
        shippingAreas = new HashMap<Integer, ShippingArea>();
    }

    public HashMap<Integer, ShippingArea> getShippingAreas() {return shippingAreas;}
    public void setShippingAreas(HashMap<Integer, ShippingArea> shippingAreas) {this.shippingAreas = shippingAreas;}

    public void addShippingArea(int areaNum, String areaName) throws KeyAlreadyExistsException {
        if (!shippingAreas.containsKey(areaNum)) {
            shippingAreas.put(areaNum, new ShippingArea(areaNum, areaName));
        }else {
            throw new KeyAlreadyExistsException("Shipping Area Number already exists");   //TODO:  Remember to catch(Service) this upwards and print(Presentation) approppriately
        }
    }

    public void deleteShippingArea(int areaNum) throws AttributeNotFoundException, ContextNotEmptyException {
        if(!shippingAreas.containsKey(areaNum)){
            throw new AttributeNotFoundException("Can't delete a Shipping Area that Doesn't exist.\n");
        } else if (!shippingAreas.get(areaNum).getSites().isEmpty()) {
            throw new ContextNotEmptyException("Can't Delete a shipping area that has Sites in it.\n");
        }
        shippingAreas.remove(areaNum);  ///  regular removal if everything is GOODA
    }

    public void setShippingAreaNum(int oldAreaNum, int newAreaNum) throws ClassNotFoundException {
        if (shippingAreas.containsKey(newAreaNum)) {
            throw new KeyAlreadyExistsException("New Shipping Area Number already exists");   //TODO: catch in upper layer
        } else if (!shippingAreas.containsKey(oldAreaNum)) {
            throw new ClassNotFoundException("Old Shipping Area Number does not exist");   //TODO: catch in upper layer
        }else{  //  now changing the area's number
            for (String site_address_string : shippingAreas.get(oldAreaNum).getSites().keySet()) {
                shippingAreas.get(oldAreaNum).getSites().get(site_address_string).getAddress().setArea(newAreaNum);
            }
            ShippingArea temp = shippingAreas.get(oldAreaNum);
            shippingAreas.put(newAreaNum, temp);
            shippingAreas.remove(oldAreaNum);
            shippingAreas.get(newAreaNum).setArea_number(newAreaNum);
        }
    }


    public void setShippingAreaName(int areaName, String newAreaName){
        //TODO
    }

    public void addSiteTOArea(int areaNum, String address, String cName, int cNumber){
        //TODO
    }

    public void deleteSiteFromArea(String address){
        //TODO
    }

    public void setSiteAddress(int areaNum, String oldAddress, String newAddress){
        //TODO
    }

    public void setSiteAreaNum(int oldAreaNum, int newAreaNum, String address){
        //TODO
    }

    public String showAllSites(){
        String res = "All Sites:\n";
        for (ShippingArea shippingArea : shippingAreas.values()) {
            res += shippingArea.toString();   //  they already have \n between them
        }
        return res;
    }

    public String showAllShippingAreas(){
        String res = "All Shipping Areas:\n";
        for (ShippingArea shippingArea : shippingAreas.values()) {
            res += shippingArea.toString();
        }
        res += "\n";
        return res;
    }


}
