package PresentationLayer.DTOs;

import java.util.HashMap;

public class ItemsDocDTO {
    private int itemsDoc_num;
    /// we want to take to here
    private SiteDTO src_siteDTO;
    ///  we want to take from there
    private SiteDTO dest_siteDTO;
    //    private boolean isDestaStore;
    private HashMap<ItemDTO, Integer> itemDTOs;

    public ItemsDocDTO(int itemsDoc_num, SiteDTO src_siteDTO, SiteDTO dest_siteDTO, HashMap<ItemDTO, Integer> itemDTOs) {
        this.itemsDoc_num = itemsDoc_num;
        this.src_siteDTO = src_siteDTO;
        this.dest_siteDTO = dest_siteDTO;
        this.itemDTOs = itemDTOs;
    }

    public int getItemsDoc_num() {return itemsDoc_num;}
    public void setItemsDoc_num(int itemsDoc_num) {this.itemsDoc_num = itemsDoc_num;}
    public SiteDTO getSrc_siteDTO() {return src_siteDTO;}
    public void setSrc_siteDTO(SiteDTO src_siteDTO) {this.src_siteDTO = src_siteDTO;}
    public SiteDTO getDest_siteDTO() {return dest_siteDTO;}
    public void setDest_siteDTO(SiteDTO dest_siteDTO) {this.dest_siteDTO = dest_siteDTO;}
    public HashMap<ItemDTO, Integer> getItemDTOs() {return itemDTOs;}
    public void setItemDTOs(HashMap<ItemDTO, Integer> itemDTOs) {this.itemDTOs = itemDTOs;}

}
