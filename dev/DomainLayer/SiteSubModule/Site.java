package DomainLayer.SiteSubModule;

public class Site {
    private int area;
    private Address address;

    public Site(int area, Address address) {
        this.area = area;
        this.address = address;
    }

    public int getArea() {return area;}
    public void setArea(int area) {this.area = area;}

    public Address getAddress() {return address;}
    public void setAddress(Address address) {this.address = address;}

    @Override
    public String toString() {
        return "";                           //TODO     <<------------------------
    }
}
