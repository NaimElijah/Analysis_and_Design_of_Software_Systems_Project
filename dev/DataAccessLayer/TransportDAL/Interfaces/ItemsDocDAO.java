package DataAccessLayer.TransportDAL.Interfaces;

import DTOs.TransportModuleDTOs.ItemsDocDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface ItemsDocDAO {
    public ItemsDocDTO getItemsDoc(int itemsDocNum);
    public ArrayList<ItemsDocDTO> getAllItemsDocs();
    public ArrayList<ItemsDocDTO> getItemsDocsOfTransportId(int TransportId);
    public boolean insertItemsDoc(int ItemsDocInTransportID, int itemsDocNum, int srcSiteArea, String srcSiteString,
                                  int destSiteArea, String destSiteString, LocalDateTime estimatedArrivalTime);
    public boolean deleteItem(int itemsDocNum, String itemName, double itemWeight, boolean itemCond);
    public boolean updateItemCond(int itemsDocNum, String itemName, double itemWeight, boolean newCond);
}
