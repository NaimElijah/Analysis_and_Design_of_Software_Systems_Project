package DomainLayer.TransportDomain.SiteSubModule;

import javax.management.AttributeNotFoundException;
import javax.management.openmbean.KeyAlreadyExistsException;
import javax.naming.ContextNotEmptyException;
import java.util.HashMap;

public class SiteFacade {
    private HashMap<Integer, ShippingArea> shippingAreas;

    public SiteFacade() { shippingAreas = new HashMap<Integer, ShippingArea>(); }

    public HashMap<Integer, ShippingArea> getShippingAreas() {return shippingAreas;}
    public void setShippingAreas(HashMap<Integer, ShippingArea> shippingAreas) {
        this.shippingAreas = shippingAreas;
    }

    public void addShippingArea(int areaNum, String areaName) throws KeyAlreadyExistsException {
        if (shippingAreas.containsKey(areaNum)) {
            throw new KeyAlreadyExistsException("Shipping Area Number already exists");
        }
        shippingAreas.put(areaNum, new ShippingArea(areaNum, areaName));
    }

    public void deleteShippingArea(int areaNum) throws AttributeNotFoundException, ContextNotEmptyException {
        if(!shippingAreas.containsKey(areaNum)){
            throw new AttributeNotFoundException("Can't delete a Shipping Area that Doesn't exist.");
        } else if (!shippingAreas.get(areaNum).getSites().isEmpty()) {
            throw new ContextNotEmptyException("Can't Delete a shipping area that has Sites in it.");
        }
        shippingAreas.remove(areaNum);  ///  regular removal if everything is GOODA
    }


    public void setShippingAreaNum(int oldAreaNum, int newAreaNum) throws KeyAlreadyExistsException, ClassNotFoundException {
        if (shippingAreas.containsKey(newAreaNum)) {
            throw new KeyAlreadyExistsException("New Shipping Area Number already exists");
        } else if (!shippingAreas.containsKey(oldAreaNum)) {
            throw new ClassNotFoundException("Old Shipping Area Number does not exist");
        }
        for (String site_address_string : shippingAreas.get(oldAreaNum).getSites().keySet()) {
            shippingAreas.get(oldAreaNum).getSites().get(site_address_string).getAddress().setArea(newAreaNum);
        }
        ShippingArea temp = shippingAreas.get(oldAreaNum);
        shippingAreas.put(newAreaNum, temp);
        shippingAreas.remove(oldAreaNum);
        shippingAreas.get(newAreaNum).setArea_number(newAreaNum);
    }

    public void setShippingAreaName(int areaNum, String newAreaName) throws ClassNotFoundException {
        if (!shippingAreas.containsKey(areaNum)) {
            throw new ClassNotFoundException("Can't set name to a non existent Area number");
        }
        shippingAreas.get(areaNum).setArea_name(newAreaName);
    }












    public void addSiteTOArea(int areaNum, String address, String cName, long cNumber) throws ClassNotFoundException, KeyAlreadyExistsException {
        if (!shippingAreas.containsKey(areaNum)) {
            throw new ClassNotFoundException("Can't add a a site to a non existent Area number");
        } else if (shippingAreas.get(areaNum).getSites().containsKey(address)) {
            throw new KeyAlreadyExistsException("Site Address already exists in that area number");
        }
        shippingAreas.get(areaNum).addSite(address, cName, cNumber);
    }

    public void deleteSiteFromArea(int areaNum, String address) throws ClassNotFoundException {
        if (!shippingAreas.containsKey(areaNum)) {
            throw new ClassNotFoundException("Can't delete from a non existent Area number");
        }else if(!shippingAreas.get(areaNum).getSites().containsKey(address)){
            throw new ClassNotFoundException("Can't delete a non existent site address string");
        }
        shippingAreas.get(areaNum).getSites().remove(address);
    }



    public boolean doesSiteExist(Integer currSiteAreaNum, String currDestinationAddress) {
        if (shippingAreas.containsKey(currSiteAreaNum)) {
            if(shippingAreas.get(currSiteAreaNum).getSites().containsKey(currDestinationAddress)){
                return true;
            }
        }
        return false;
    }


    public void setSiteAddress(int areaNum, String oldAddress, String newAddress) throws ClassNotFoundException, KeyAlreadyExistsException {
        if (!shippingAreas.containsKey(areaNum)) {
            throw new ClassNotFoundException("Can't edit from a non existent Area number");
        } else if (!shippingAreas.get(areaNum).getSites().containsKey(oldAddress)) {
            throw new ClassNotFoundException("Can't edit from a non existent Address String");
        } else if (shippingAreas.get(areaNum).getSites().containsKey(newAddress)) {
            throw new KeyAlreadyExistsException("Site Address String already exists in that area number");
        }
        Site temp = shippingAreas.get(areaNum).getSites().get(oldAddress);
        temp.getAddress().setAddress(newAddress);
        shippingAreas.get(areaNum).getSites().put(newAddress, temp);
        shippingAreas.get(areaNum).getSites().remove(oldAddress);
    }


    public void setSiteAreaNum(int oldAreaNum, int newAreaNum, String address) throws ClassNotFoundException, KeyAlreadyExistsException {
        if (!shippingAreas.containsKey(oldAreaNum)) {
            throw new ClassNotFoundException("Can't edit from a non existent Area number");
        } else if (!shippingAreas.get(oldAreaNum).getSites().containsKey(address)) {
            throw new ClassNotFoundException("Can't edit from a non existent Address String");
        } else if (!shippingAreas.containsKey(newAreaNum)) {
            throw new ClassNotFoundException("Can't change to a non existent Area number");
        } else if (shippingAreas.get(newAreaNum).getSites().containsKey(address)) {
            throw new KeyAlreadyExistsException("The Address String of the Site you are trying to move already exists in the destination area number");
        }
        Site temp = shippingAreas.get(oldAreaNum).getSites().get(address);
        temp.getAddress().setArea(newAreaNum);
        shippingAreas.get(newAreaNum).getSites().put(address, temp);
        shippingAreas.get(oldAreaNum).getSites().remove(address);
    }



    public void setSiteContName(int areaNum, String address, String newContname) throws ClassNotFoundException {
        if (!shippingAreas.containsKey(areaNum)) {
            throw new ClassNotFoundException("Can't edit from a non existent Area number");
        } else if (!shippingAreas.get(areaNum).getSites().containsKey(address)) {
            throw new ClassNotFoundException("Can't edit from a non existent Address String");
        }
        shippingAreas.get(areaNum).getSites().get(address).setcName(newContname);
    }

    public void setSiteContNum(int areaNum, String address, long newContNum) throws ClassNotFoundException {
        if (!shippingAreas.containsKey(areaNum)) {
            throw new ClassNotFoundException("Can't edit from a non existent Area number");
        } else if (!shippingAreas.get(areaNum).getSites().containsKey(address)) {
            throw new ClassNotFoundException("Can't edit from a non existent Address String");
        }
        shippingAreas.get(areaNum).getSites().get(address).setcNumber(newContNum);
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
