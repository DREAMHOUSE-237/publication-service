package com.dreamhousesystem.dreamhouse.Messaging;

public class PaymentRequestMessage {
    private String proprietaireEmail;
    private String description;
    private Double prix;
    private String numeroPaiement;
    private Integer idPublication; // ← AJOUT

    public PaymentRequestMessage() {}

    public PaymentRequestMessage(String proprietaireEmail, String description,
                                  Double prix, String numeroPaiement, Integer idPublication) {
        this.proprietaireEmail = proprietaireEmail;
        this.description = description;
        this.prix = prix;
        this.numeroPaiement = numeroPaiement;
        this.idPublication = idPublication; // ← AJOUT
    }

    public String getProprietaireEmail() { return proprietaireEmail; }
    public void setProprietaireEmail(String proprietaireEmail) { this.proprietaireEmail = proprietaireEmail; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }

    public String getNumeroPaiement() { return numeroPaiement; }
    public void setNumeroPaiement(String numeroPaiement) { this.numeroPaiement = numeroPaiement; }

    public Integer getIdPublication() { return idPublication; } // ← AJOUT
    public void setIdPublication(Integer idPublication) { this.idPublication = idPublication; } // ← AJOUT
}