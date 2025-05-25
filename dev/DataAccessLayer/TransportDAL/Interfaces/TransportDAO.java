package DataAccessLayer.TransportDAL.Interfaces;

import DTOs.TransportModuleDTOs.ItemQuantityDTO;
import DTOs.TransportModuleDTOs.ItemsDocDTO;
import DTOs.TransportModuleDTOs.TransportDTO;

import java.sql.SQLException;
import java.util.List;

public interface TransportDAO {

    ///  Usable   <<---------------  NEEDED SO IMPLEMENT THESE

    /// ItemQs
    public boolean insertItemQ(ItemQuantityDTO itemQForInsertion) throws SQLException;
    public List<ItemQuantityDTO> getItemQsOfItemsDocId(int ItemsDocId) throws SQLException;
    public boolean deleteItemQ(int itemInItemsDocId, String itemName, double itemWeight, boolean itemCond) throws SQLException;
    ///   to delete some of the item's amount, we'll use the updatedItemQ function.
    public boolean updateItemQ(ItemQuantityDTO updatedItemQ) throws SQLException;  ///  according to the primary keys in the DTO, updated/set everything else (GENERAL UPDATER)

    /// ItemsDocs
    public boolean insertItemDoc(ItemsDocDTO itemsDocForInsertion) throws SQLException;
    public List<ItemsDocDTO> getItemsDocOfTransportId(int TransportId) throws SQLException;
    public boolean deleteItemsDocById(int itemsDocId) throws SQLException;
    public boolean updateItemsDoc(ItemsDocDTO updatedItemsDoc) throws SQLException;  ///  according to the primary keys in the DTO, updated/set everything else (GENERAL UPDATER)

    /// Transports
    public boolean insertTransport(TransportDTO transportForInsertion) throws SQLException;
    public List<TransportDTO> getAllTransports() throws SQLException;  ///  this getting all function can be used for LOADING DATA   <<----------  because gets everything
    public boolean deleteTransportById(int transportId) throws SQLException;
    public boolean updateTransport(TransportDTO updatedTransport) throws SQLException;  ///  according to the primary keys in the DTO, updated/set everything else (GENERAL UPDATER)



    /// ///  Not Usable   <<---------------   SEE IF NEEDED LATER    <<------------------------
//    public void afterThisFunctionIsAllTheNotUsableMethods();
//
//    /// ItemQs
//    public ItemQuantityDTO getItemQ(int itemInItemsDocId, String itemName, double itemWeight, boolean itemCond) throws SQLException;
//    public List<ItemQuantityDTO> getAllItemsQ() throws SQLException;  //  might not need to get all
//
//    /// ItemsDocs
//    public ItemsDocDTO getItemsDocById(int itemsDocId) throws SQLException;
//    public List<ItemsDocDTO> getAllItemsDocs() throws SQLException;  //  might not need to get all
//
//    /// Transports
//    public TransportDTO getTransportById(int TransportId) throws SQLException;



}
