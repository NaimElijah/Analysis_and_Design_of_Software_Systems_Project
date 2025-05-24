package DataAccessLayer.TransportDAL.Interfaces;

import DTOs.TransportModuleDTOs.ItemDTO;

import java.util.ArrayList;

public interface ItemDAO {
    public ItemDTO getItem(int ItemsDocId, String itemName, double itemWeight, boolean item);
    public ArrayList<ItemDTO> getAllItems();
    public ArrayList<ItemDTO> getItemsOfItemsDocId(int ItemsDocId);
    public boolean insertItem(int ItemsDocId, String itemName, double itemWeight, boolean item);
    public boolean deleteItem(int ItemsDocId, String itemName, double itemWeight, boolean item);
    public boolean insertItem(int ItemsDocId, String itemName, double itemWeight, boolean item);
}
