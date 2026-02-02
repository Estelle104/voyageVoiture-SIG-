package model;

public class TypeMatiere {
    int id;
    String typeMatiere;
    String descriptions;

    public TypeMatiere(int id, String typeMatiere, String descriptions) {
        this.id = id;
        this.typeMatiere = typeMatiere;
        this.descriptions = descriptions;
    }
    public TypeMatiere() {
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTypeMatiere() {
        return typeMatiere;
    }
    public void setTypeMatiere(String typeMatiere) {
        this.typeMatiere = typeMatiere;
    }
    public String getDescriptions() {
        return descriptions;
    }
    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public String toString() {
        return typeMatiere;  
    }
    
}
