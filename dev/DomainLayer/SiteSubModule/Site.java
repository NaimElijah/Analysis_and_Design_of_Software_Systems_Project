package DomainLayer.SiteSubModule;

public class Site {
    private int site_id;
    //TODO: Add site ID so we can associate employees with site IDs according to which Site are they working for.
    private Address address;
    private String cName;
    private long cNumber;

    public Site(Address address, String cName, long cNumber) {
        this.address = address;
        this.cName = cName;
        this.cNumber = cNumber;
    }

    public Site(int site_id, Address address, String cName, long cNumber) {   //TODO:   new one
        this.site_id = site_id;
        this.address = address;
        this.cName = cName;
        this.cNumber = cNumber;
    }

    public int getSite_id() {return site_id;}
    public void setSite_id(int site_id) {this.site_id = site_id;}
    public void setAddress(Address address) {this.address = address;}
    public void setcName(String cName) {this.cName = cName;}
    public void setcNumber(long cNumber) {this.cNumber = cNumber;}
    public Address getAddress() {return address;}
    public String getcName() {return cName;}
    public long getcNumber() {return cNumber;}

    @Override
    public String toString() {
        String res = "";
        res += "Address: (" + address.toString() + "), Contact Name: " + cName + ", Contact Number: " + cNumber + ".";
        return res;
    }
}
