package PresentationLayer.DTOs;

public class SiteDTO {
        private int siteAreaNum;
        private String siteAddressString;

    public SiteDTO(int siteAreaNum, String addressString) {
        this.siteAreaNum = siteAreaNum;
        this.siteAddressString = addressString;
    }

    public int getSiteAreaNum() {return siteAreaNum;}
    public void setSiteAreaNum(int siteAreaNum) {this.siteAreaNum = siteAreaNum;}
    public String getAddressString() {return siteAddressString;}
    public void setAddressString(String addressString) {this.siteAddressString = addressString;}

}
