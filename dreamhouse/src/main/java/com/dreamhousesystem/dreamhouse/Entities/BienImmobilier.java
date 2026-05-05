package com.dreamhousesystem.dreamhouse.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;
import java.util.List;
// initialisation des index sur les champs qui seront trop consultes dans mon service
@Table(
        indexes = {
                @Index(name = "idx_region", columnList = "region"),
                @Index(name = "idx_ville", columnList = "ville"),
                @Index(name = "idx_typePublication", columnList = "typePublication")
        }
)

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BienImmobilier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = true)
    private String proprietaireEmail; // il s'agit l'a de l'email du publisher que je vais recevoir du service de Betty pour les statts

    private String titreBien;

    @Column(nullable = false)
    @jakarta.validation.constraints.Positive
    private Double superfie;

    @jakarta.validation.constraints.Min(1)
    private int nbrePiece;

    @Column(nullable = false)
    @jakarta.validation.constraints.NotBlank
    @jakarta.validation.constraints.Size(min = 1, max = 100000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private CategorieBien categorie;

    @Column(nullable = false)
    @jakarta.validation.constraints.PositiveOrZero
    private Double prix;

    @Embedded
    private Adresse adresse;

    private LocalDateTime datePublication;

    @PrePersist
    protected void onCreate() {
        this.datePublication = LocalDateTime.now();
    }

    @Enumerated(EnumType.STRING)
    private TypePublication typePublication;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "bien_images",
            joinColumns = @JoinColumn(name = "bien_id")
    )
    @Column(name = "image_url")
    @Fetch(FetchMode.JOIN)
    private List<String> images;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "bien_docuements",
            joinColumns = @JoinColumn(name = "bien_id")
    )
    @Column(name = "document_url")
    @Fetch(FetchMode.JOIN)
    private List<String> docuements;


    @Column(nullable = true)
    private String numeroPaiement; // florinda j'attend ..

    // Getters / Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getProprietaireEmail() { return proprietaireEmail; }
    public void setProprietaireEmail(String proprietaireEmail) { this.proprietaireEmail = proprietaireEmail; }

    public String getTitreBien() { return titreBien; }
    public void setTitreBien(String titreBien) { this.titreBien = titreBien; }

    public Double getSuperfie() { return superfie; }
    public void setSuperfie(Double superfie) { this.superfie = superfie; }

    public int getNbrePiece() { return nbrePiece; }
    public void setNbrePiece(int nbrePiece) { this.nbrePiece = nbrePiece; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public CategorieBien getCategorie() { return categorie; }
    public void setCategorie(CategorieBien categorie) { this.categorie = categorie; }

    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }

    public Adresse getAdresse() { return adresse; }
    public void setAdresse(Adresse adresse) { this.adresse = adresse; }

    public LocalDateTime getDatePublication() { return datePublication; }
    public void setDatePublication(LocalDateTime datePublication) { this.datePublication = datePublication; }

    public TypePublication getTypePublication() { return typePublication; }
    public void setTypePublication(TypePublication typePublication) { this.typePublication = typePublication; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }

    public List<String> getDocuements() { return docuements; }
    public void setDocuements(List<String> docuements) { this.docuements = docuements; }

    public String getNumeroPaiement() { return numeroPaiement; }
    public void setNumeroPaiement(String numeroPaiement) { this.numeroPaiement = numeroPaiement; }
}
