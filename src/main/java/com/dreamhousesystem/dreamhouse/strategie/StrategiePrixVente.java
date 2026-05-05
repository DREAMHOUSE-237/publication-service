package com.dreamhousesystem.dreamhouse.strategie;

public class StrategiePrixVente implements StrategiePrix {
    @Override
    public double calculerPrix(Double prixBase) {
        return prixBase + (prixBase * 0.05); // 5% de commission
    }
}
