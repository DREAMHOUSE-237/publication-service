package com.dreamhousesystem.dreamhouse.strategie;

public class ContextePrix {
    private StrategiePrix strategie;

    public void definirStrategie(StrategiePrix strategie) {
        this.strategie = strategie;
    }

    public double appliquerStrategie(Double prixBase) {
        if (strategie == null) {
            throw new IllegalStateException("Aucune strategie de prix definie !");
        }
        return strategie.calculerPrix(prixBase);
    }
}
