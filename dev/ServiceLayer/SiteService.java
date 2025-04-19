package ServiceLayer;

import DomainLayer.SiteSubModule.SiteFacade;

import javax.management.AttributeNotFoundException;
import javax.management.openmbean.KeyAlreadyExistsException;
import javax.naming.ContextNotEmptyException;

public class SiteService {
    private SiteFacade sf;

    public SiteService(SiteFacade ssff) {
        this.sf = ssff;
    }


    public String addShippingArea(int areaNum, String areaName){
        try {
            if(areaName.isEmpty() || areaName.isBlank()){
                return "It seems some values you've entered are Empty or Blank, Please Insert proper values";
            }
            sf.addShippingArea(areaNum, areaName);
        }catch (KeyAlreadyExistsException e){
            return "Shipping Area Number already exists";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }

    public String deleteShippingArea(int areaNum){
        try {
            sf.deleteShippingArea(areaNum);
        } catch (AttributeNotFoundException e){
            return "Can't delete a Shipping Area that Doesn't exist.";
        } catch (ContextNotEmptyException e){
            return "Can't Delete a shipping area that has Sites in it.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }



    public String setShippingAreaNum(int OldareaNum, int NewAreaNum){
        try {
            if(OldareaNum == NewAreaNum){
                return "The Edition process Finished because you set the same Area Number value as the value that is already there";
            }
            sf.setShippingAreaNum(OldareaNum, NewAreaNum);
        } catch (KeyAlreadyExistsException e){
            return "New Shipping Area Number already exists";
        } catch (ClassNotFoundException e){
            return "Old Shipping Area Number does not exist";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }


    public String setShippingAreaName(int areaNum, String NewareaName){
        try {
            if(NewareaName.isEmpty() || NewareaName.isBlank()){
                return "It seems some values you've entered are Empty or Blank, Please Insert proper values";
            }
            sf.setShippingAreaName(areaNum, NewareaName);
        } catch (ClassNotFoundException e){
            return "Can't set name of a non existent Area number";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";  //  if All Good
    }
















    public String addSite(int areaNum, String address, String cont_name, long Cont_Num){
        try {
            if (address.isEmpty() || cont_name.isEmpty() || address.isBlank() || cont_name.isBlank() || Cont_Num == 0) {
                return "It seems some values you've entered are Empty or Blank, Please Insert proper values";
            }
            sf.addSiteTOArea(areaNum, address, cont_name, Cont_Num);
        } catch (ClassNotFoundException e){
            return "Can't add a a site to a non existent Area number";
        } catch (KeyAlreadyExistsException e){
            return "Site Address already exists in that area number";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";
    }

    public String deleteSite(int areaNum, String address){
        try {
            if (address.isEmpty() || address.isBlank()) {
                return "It seems some values you've entered are Empty or Blank, Please Insert proper values";
            }
            sf.deleteSiteFromArea(areaNum, address);
        } catch (ClassNotFoundException e){
            return "Can't Delete a site with a non existent Area number or a non existent address string";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";
    }

    public String setSiteAddress(int areaNum, String Oldaddress, String NewAddress){
        try {
            if (NewAddress.isEmpty() || Oldaddress.isEmpty() || NewAddress.isBlank() || Oldaddress.isBlank()) {
                return "It seems some values you've entered are Empty or Blank, Please Insert proper values";
            } else if (Oldaddress.equals(NewAddress)){
                return "The Edition process Finished because you set the same Address String value as the value that is already there";
            }
            sf.setSiteAddress(areaNum, Oldaddress, NewAddress);
        } catch (ClassNotFoundException e){
            return "Can't Edit a site with a non existent Area number or a non existent address string";
        } catch (KeyAlreadyExistsException e){
            return "Site Address String already exists in that area number";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";
    }

    public String setSiteAreaNum(int OldareaNum, int NewAreaNum, String address){
        try {
            if (address.isEmpty() || address.isBlank()) {
                return "It seems some values you've entered are Empty or Blank, Please Insert proper values";
            }else if (OldareaNum == NewAreaNum) {
                return "The Edition process Finished because you set the same Area Number value as the value that is already there";
            }
            sf.setSiteAreaNum(OldareaNum, NewAreaNum, address);
        } catch (ClassNotFoundException e){
            return "Can't Edit a site with a non existent Area number(as the old one or the new one) or a non existent address string";
        } catch (KeyAlreadyExistsException e){
            return "The Address String of the Site you are trying to move already exists in the destination area number";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";
    }

    public String setSiteContName(int areaNum, String address, String contName){
        try {
            if (address.isEmpty() || contName.isEmpty() || address.isBlank() || contName.isBlank()) {
                return "It seems some values you've entered are Empty or Blank, Please Insert proper values";
            }
            sf.setSiteContName(areaNum, address, contName);
        } catch (ClassNotFoundException e){
            return "Can't Edit a site with a non existent Area number or a non existent address string";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";
    }

    public String setSiteContNum(int areaNum, String address, long contNum){
        try {
            if (address.isEmpty() || address.isBlank()) {
                return "It seems some values you've entered are Empty or Blank, Please Insert proper values";
            }
            sf.setSiteContNum(areaNum, address, contNum);
        } catch (ClassNotFoundException e){
            return "Can't Edit a site with a non existent Area number or a non existent address string";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return "Success";
    }















    public String showAllSites(){
        String res = "";
        try {
            res = sf.showAllSites();
        } catch (Exception e) {
            e.printStackTrace();  // or do nothing in this line
        }
        return res;
    }

    public String showAllShippingAreas(){
        String res = "";
        try {
            res = sf.showAllShippingAreas();
        } catch (Exception e) {
            e.printStackTrace();  // or do nothing in this line
        }
        return res;
    }


}
