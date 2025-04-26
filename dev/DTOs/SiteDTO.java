package DTOs;

public class SiteDTO {
    private int siteAreaNum;
    private String siteAddressString;

    public SiteDTO() {}
    public SiteDTO(int siteAreaNum, String addressString) {
        this.siteAreaNum = siteAreaNum;
        this.siteAddressString = addressString;
    }

    public int getSiteAreaNum() {return siteAreaNum;}
    public void setSiteAreaNum(int siteAreaNum) {this.siteAreaNum = siteAreaNum;}
    public String getAddressString() {return siteAddressString;}
    public void setAddressString(String addressString) {this.siteAddressString = addressString;}

    @Override
    public String toString() {
        String res = "Site Area: " + this.siteAreaNum + ", Site String Address: " + this.siteAddressString;
        return res;
    }

}
