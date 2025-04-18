package DomainLayer.SiteSubModule;

import java.util.HashMap;

public class SiteFacade {
    private HashMap<String, ShippingArea> shippingAreas;
    public SiteFacade() {
        shippingAreas = new HashMap<>();
    }
    public String showSites(){
        //todo
        return "";
    }

    public void addShippingArea(int areaNum, String areaName){}
    public void deleteShippingArea(int areaNum){}
    public void setShippingAreaNum(int oldAreaNum, int newAreaNum){}
    public void setShippingAreaName(int areaName, String newAreaName){}
    public void addSite(int areaName , String address, String cName, int cNumber){}
    public void deleteSite(String address){}
    public void setSiteAddress(int areaNum, String oldAddress, String newAddress){}
    public void setSiteNum(int oldAreaNum, int newAreaNum, String address){}
    public void shippingAreaToString(int areaNum){}


}
