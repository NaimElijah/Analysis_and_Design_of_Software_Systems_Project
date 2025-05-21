package DTOs.TransportModuleDTOs;

public class ItemQuantityDTO {
    private ItemDTO item;
    private int quantity;

    public ItemQuantityDTO(){}
    public ItemQuantityDTO(ItemDTO item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public ItemDTO getItem() {return item;}
    public void setItem(ItemDTO item) {this.item = item;}
    public int getQuantity() {return quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

    @Override
    public String toString() {
        return "ItemQuantityDTO [item=" + item.toString() + ", quantity=" + quantity + "]";
    }
}

