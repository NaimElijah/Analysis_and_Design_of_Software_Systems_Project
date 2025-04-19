package ServiceLayer;

import DomainLayer.SiteSubModule.SiteFacade;
import javax.management.openmbean.KeyAlreadyExistsException;

public class SiteService {
    private SiteFacade sf;

    public SiteService(SiteFacade ssff) {
        this.sf = ssff;
    }

    private String showSites(){
        return "";
        //TODO
    }

    private String addShippingArea(int areaNum, String areaName){
        try {
            sf.addShippingArea(areaNum, areaName);
        }catch (KeyAlreadyExistsException e){
            return "KeyAlreadyExistsException";   // serialize this
        } catch (Exception e) {
            e.printStackTrace();  // or do nothing in this line
        }
        return "";  //  if All Good
    }


    private void deleteShippingArea(int areaNum){
        //TODO
    }

    private void setShippingAreaNum(int OldareaNum, int NewAreaNum){
        //TODO
    }

    private void setShippingAreaName(int areaNum, String NewareaName){
        //TODO
    }

    private void addSite(int areaNum, String address, String cont_name, int Cont_Num){
        //TODO
    }

    private void deleteSite(int areaNum, String address){
        //TODO
    }

    private void setSiteAddress(int areaNum, String Oldaddress, String NewAddress){
        //TODO
    }

    private void setSiteAreaNum(int OldareaNum, int NewAreaNum, String address){
        //TODO
    }

    private void shippingAreaToString(int areaNum){
        //TODO
    }

    private String SiteToString(int areaNum, String address){
        return "";
        //TODO
    }

    //TODO if something more is needed
}
