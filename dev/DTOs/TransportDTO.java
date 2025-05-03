package DTOs;

import java.util.ArrayList;

public class TransportDTO {
    private int transport_ID;
    private int transportTruckNum;
    private int transportDriverID;
    private SiteDTO src_site;
    private ArrayList<ItemsDocDTO> dests_Docs;    ///  <<<--------------  In Order of visit   <<<--------------------

    public TransportDTO() {}
    public TransportDTO(int transport_ID, int transportTruckNum, int transportDriverID, SiteDTO src_site, ArrayList<ItemsDocDTO> dests_Docs) {
        this.transport_ID = transport_ID;
        this.transportTruckNum = transportTruckNum;
        this.transportDriverID = transportDriverID;
        this.src_site = src_site;
        this.dests_Docs = dests_Docs;
    }

    public int getTransport_ID() {return transport_ID;}
    public void setTransport_ID(int transport_ID) {this.transport_ID = transport_ID;}
    public int getTransportTruckNum() {return transportTruckNum;}
    public void setTransportTruckNum(int transportTruckNum) {this.transportTruckNum = transportTruckNum;}
    public int getTransportDriverID() {return transportDriverID;}
    public void setTransportDriverID(int transportDriverID) {this.transportDriverID = transportDriverID;}
    public SiteDTO getSrc_site() {return src_site;}
    public void setSrc_site(SiteDTO src_site) {this.src_site = src_site;}
    public ArrayList<ItemsDocDTO> getDests_Docs() {return dests_Docs;}
    public void setDests_Docs(ArrayList<ItemsDocDTO> dests_Docs) {this.dests_Docs = dests_Docs;}


    public String showAllTransportItemsDocs() {
        String res = "-- Transport Sites & Items: Source Site: " + this.src_site.toString() + "\n";
        for (ItemsDocDTO itemsDocDTO : dests_Docs) {
            res += itemsDocDTO.toString() + "\n";
        }
        return res;
    }

}
