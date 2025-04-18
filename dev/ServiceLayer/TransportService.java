package ServiceLayer;

import DomainLayer.TranSubModule.TransportFacade;
import com.sun.jdi.connect.Transport;
import java.time.*;

public class TransportService {
    private TransportFacade tran_f;

    public TransportService(TransportFacade tf) {
        this.tran_f = tf;
    }

    public String showAllTransports() {
        String resOfAllTransports = "";
        try {
            resOfAllTransports = tran_f.showAllTransports();
        }catch (Exception e){
            e.printStackTrace();
        }
        return resOfAllTransports;
    }
    public void createTransport(LocalDateTime dt, int truckNum, int driverId, int src_areaNum, String src_address) {}

    public void deleteTransport(int transportId) {}

    public void setTransportStatus(int transDocId, String status) {}

    public void setTransportTruck(int tranDocId, int truckNum){}

    public void setTransportDriver(int tranDocId, int driverId){}

    public void addTransportProblem(int transportId, String problem){}

    public void addDestSite(int site_menu_index){}

    public void removeDestSite(int site_menu_index){}

    public void addItem(String itemName, int amount, String address,int areaNum){}

    public void removeItem(String itemName, int amount, String address,int areaNum, boolean cond){}

    public void setItemCond(String itemName,int amount, String address , int areaNum, boolean cond){}

    public String transportToString(int transportDocId) {
        //todo
        return"";
    }



}