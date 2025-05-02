package DTOs;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemsDocDTO {
    private int itemsDoc_num;
    private SiteDTO src_siteDTO;
    private SiteDTO dest_siteDTO;
    private ArrayList<ItemQuantityDTO> itemQuantityDTOs;

    public ItemsDocDTO() {}
    public ItemsDocDTO(int itemsDoc_num, SiteDTO src_siteDTO, SiteDTO dest_siteDTO, ArrayList<ItemQuantityDTO> itemQuantityDTOs) {
        this.itemsDoc_num = itemsDoc_num;
        this.src_siteDTO = src_siteDTO;
        this.dest_siteDTO = dest_siteDTO;
        this.itemQuantityDTOs = itemQuantityDTOs;
    }

    public int getItemsDoc_num() {return itemsDoc_num;}
    public void setItemsDoc_num(int itemsDoc_num) {this.itemsDoc_num = itemsDoc_num;}
    public SiteDTO getSrc_siteDTO() {return src_siteDTO;}
    public void setSrc_siteDTO(SiteDTO src_siteDTO) {this.src_siteDTO = src_siteDTO;}
    public SiteDTO getDest_siteDTO() {return dest_siteDTO;}
    public void setDest_siteDTO(SiteDTO dest_siteDTO) {this.dest_siteDTO = dest_siteDTO;}
    public ArrayList<ItemQuantityDTO> getItemQuantityDTOs() {return itemQuantityDTOs;}
    public void setItemQuantityDTOs(ArrayList<ItemQuantityDTO> itemQuantityDTOs) {this.itemQuantityDTOs = itemQuantityDTOs;}

    @Override
    public String toString() {
        String res = "ItemsDocNum: " + this.itemsDoc_num + ", Source Site: " + this.src_siteDTO.toString() + ", Destination Site: " + this.dest_siteDTO.toString() + "\n";
        for (ItemQuantityDTO itemQuantityDTO : this.itemQuantityDTOs) {
            res += itemQuantityDTO.getItem().toString() + ", amount: "+ itemQuantityDTO.getQuantity() + ".\n";
        }
        return res;
    }

}
