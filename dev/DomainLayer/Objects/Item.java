package DomainLayer.Objects;

public class Item {
    private String name;
    private int weight;       //  Maybe this will need to be Integer to be Serializable   <<-------------, CHECK
    private Boolean condition;

    public Item(String name, int weight, Boolean condition){
        this.name = name;
        this.weight = weight;
        this.condition = condition;
    }

    public Item(String name, int weight){
        this.name = name;
        this.weight = weight;
        this.condition = true;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public int getWeight() {return weight;}
    public void setWeight(int weight) {this.weight = weight;}

    public Boolean getCondition() {return condition;}
    public void setCondition(Boolean condition) {this.condition = condition;}
    private void switchCond(){condition = !condition;}

    @Override
    public String toString() {
        return "";                    //TODO    <<-------------------
    }
}
