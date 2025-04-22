package PresentationLayer.DTOs;

public class ItemDTO {
    private String name;
    private int weight;
    private Boolean condition;

    public ItemDTO(String name, int weight, Boolean condition){
        this.name = name;
        this.weight = weight;
        this.condition = condition;
    }

    public ItemDTO(String name, int weight){
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

}
