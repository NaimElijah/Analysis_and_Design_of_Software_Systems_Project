package DomainLayer.TranSubModule;

public class Item {
    private int itemID;
    private String name;
    private int weight;
    private Boolean condition;

    public Item(int itemid, String name, int weight, Boolean condition){
        this.itemID = itemid;
        this.name = name;
        this.weight = weight;
        this.condition = condition;
    }

    public Item(int itemid, String name, int weight){
        this.itemID = itemid;
        this.name = name;
        this.weight = weight;
        this.condition = true;
    }

    public int getItemID() {return itemID;}
    public void setItemID(int itemID) {this.itemID = itemID;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public int getWeight() {return weight;}
    public void setWeight(int weight) {this.weight = weight;}
    public Boolean getCondition() {return condition;}
    public void setCondition(Boolean condition) {this.condition = condition;}

    @Override
    public String toString() {
        String res = "";
        res += "Item ID: " + itemID + ", Item Name: " + name + ", Item Weight: " + weight + ", Item Condition: " + (condition ? "Good" : "Bad");
        return res;
    }
}
