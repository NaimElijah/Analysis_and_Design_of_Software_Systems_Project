package DataAccessLayer.TransportDAL.Interfaces;

import DTOs.TransportModuleDTOs.ItemDTO;

import java.util.ArrayList;

public interface ItemDAO {
    public ItemDTO getItem(int itemInItemsDocId, String itemName, double itemWeight, boolean itemCond);
    public ArrayList<ItemDTO> getAllItems();
    public ArrayList<ItemDTO> getItemsOfItemsDocId(int ItemsDocId);
    public boolean insertItem(int itemInItemsDocId, String itemName, double itemWeight, boolean itemCond);
    public boolean deleteItem(int itemInItemsDocId, String itemName, double itemWeight, boolean itemCond);
    public boolean updateItemCond(int itemInItemsDocId, String itemName, double itemWeight, boolean newCond);
}
