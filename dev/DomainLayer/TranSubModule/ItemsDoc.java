package DomainLayer.TranSubModule;

import DomainLayer.SiteSubModule.Site;

import java.util.HashMap;

public class ItemsDoc {
    private int itemDoc_num;
    private Site dest_site;
//    private boolean isDestaStore;
    private HashMap<Item, Integer> items;

    public ItemsDoc(int itemDoc_num, Site dest_site) {
        this.itemDoc_num = itemDoc_num;
        this.dest_site = dest_site;
//        this.isDestaStore = arggg;
        this.items = new HashMap<Item, Integer>();
    }

    public int getItemDoc_num() {return itemDoc_num;}
    public void setItemDoc_num(int itemDoc_num) {this.itemDoc_num = itemDoc_num;}

    public Site getDest_site() {return dest_site;}
    public void setDest_site(Site dest_site) {this.dest_site = dest_site;}

    public HashMap<Item, Integer> getItems() {return items;}
    public void setItems(HashMap<Item, Integer> items) {this.items = items;}


    public void addItem(String itemName, int amount, boolean cond) {
        //TODO
    }

    public void removeItem(String itemName, int amount, boolean cond) {
        //TODO
    }

    public void removeBadItem(String ItemName, int amount, Site s, boolean cond){
        //TODO
    }

    public void setItemCond(String ItemName, int amount, boolean cond){
        //TODO
    }

    @Override
    public String toString() {
        return "";   //TODO
    }
}
