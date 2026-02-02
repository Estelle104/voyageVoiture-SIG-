package model;

public class ResultatReparationSimba {

    private Simba simba;
    private TypeMatiere typeMatiere;
    private double cout;

    public ResultatReparationSimba(Simba simba, TypeMatiere typeMatiere, double cout) {
        this.simba = simba;
        this.typeMatiere = typeMatiere;
        this.cout = cout;
    }

    public Simba getSimba() {
        return simba;
    }

    public TypeMatiere getTypeMatiere() {
        return typeMatiere;
    }

    public double getCout() {
        return cout;
    }
}
