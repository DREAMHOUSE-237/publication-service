package com.dreamhousesystem.dreamhouse.strategie;

public class StrategiePrixLocation implements StrategiePrix {
    @Override
    public double calculerPrix(Double prixBase) {
        return prixBase + 10000; // frais fixe de gestion
    }
}
