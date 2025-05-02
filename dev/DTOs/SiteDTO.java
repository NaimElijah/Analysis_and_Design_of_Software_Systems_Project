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
    public String getSiteAddressString() {return siteAddressString;}
    public void setSiteAddressString(String siteAddressString) {this.siteAddressString = siteAddressString;}

    @Override
    public String toString() {
        String res = "Site Area: " + this.siteAreaNum + ", Site String Address: " + this.siteAddressString;
        return res;
    }

}
