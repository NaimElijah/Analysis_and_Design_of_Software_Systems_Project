package DomainLayer.Objects;

public class ShippingArea {
    private int area_number;
    private String area_name;

    public ShippingArea(int area_number, String area_name) {
        this.area_number = area_number;
        this.area_name = area_name;
    }

    public int getArea_number() {return area_number;}
    public void setArea_number(int area_number) {this.area_number = area_number;}

    public String getArea_name() {return area_name;}
    public void setArea_name(String area_name) {this.area_name = area_name;}

    @Override
    public String toString() {
        return "";                           //TODO     <<------------------------
    }
}
