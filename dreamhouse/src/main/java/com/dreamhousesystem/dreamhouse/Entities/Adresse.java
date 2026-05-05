package com.dreamhousesystem.dreamhouse.Entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


@Embeddable
public class Adresse {
    @Enumerated(EnumType.STRING)
    private Region region;

    private String ville;

    private String quartier;
    private double longitude;
    private  double lattitude;



    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

    public double getLattitude(){
        return lattitude;
    }
    public double getLongitude(){ return longitude;}

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
