package ServiceLayer;

import DomainLayer.SiteSubModule.SiteFacade;
import javax.management.openmbean.KeyAlreadyExistsException;

public class SiteService {
    private SiteFacade sf;

    public SiteService(SiteFacade ssff) {
        this.sf = ssff;
    }

    public String addShippingArea(int areaNum, String areaName){
        try {
            sf.addShippingArea(areaNum, areaName);
        }catch (KeyAlreadyExistsException e){
            return "KeyAlreadyExistsException";   // serialize this
        } catch (Exception e) {
            e.printStackTrace();  // or do nothing in this line
        }
        return "";  //  if All Good
    }


    public void deleteShippingArea(int areaNum){
        //TODO
    }

    public void setShippingAreaNum(int OldareaNum, int NewAreaNum){
        //TODO
    }

    public void setShippingAreaName(int areaNum, String NewareaName){
        //TODO
    }

    public void addSite(int areaNum, String address, String cont_name, int Cont_Num){
        //TODO
    }

    public void deleteSite(int areaNum, String address){
        //TODO
    }

    public void setSiteAddress(int areaNum, String Oldaddress, String NewAddress){
        //TODO
    }

    public void setSiteAreaNum(int OldareaNum, int NewAreaNum, String address){
        //TODO
    }

    public String showAllSites(){
        String res = "";
        try {
            res = sf.showAllSites();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public String showAllShippingAreas(){
        String res = "";
        try {
            res = sf.showAllShippingAreas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

//    public void shippingAreaToString(int areaNum){
//        //TODO
//    }

//    public String SiteToString(int areaNum, String address){
//        return "";
//        //TODO
//    }


}
