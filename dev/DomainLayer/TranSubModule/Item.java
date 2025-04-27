package DomainLayer.TranSubModule;

public class Item {
    private String name;
    private double weight;   // In Kilos
    private Boolean condition;

    public Item(String name, double weight, Boolean condition){
        this.name = name;
        this.weight = weight;
        this.condition = condition;
    }

    public Item(String name, double weight){
        this.name = name;
        this.weight = weight;
        this.condition = true;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public double getWeight() {return weight;}
    public void setWeight(double weight) {this.weight = weight;}
    public Boolean getCondition() {return condition;}
    public void setCondition(Boolean condition) {this.condition = condition;}

    @Override
    public String toString() {
        String res = "";
        res += "Item Name: " + name + ", Item Weight: " + weight + ", Item Condition: " + (condition ? "Good" : "Bad");
        return res;
    }
}
