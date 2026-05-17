package com.dreamhousesystem.dreamhouse.Mappers;

import com.dreamhousesystem.dreamhouse.DTO.BienImmobilierDTO;
import com.dreamhousesystem.dreamhouse.Entities.BienImmobilier;
import com.dreamhousesystem.dreamhouse.Entities.Adresse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BienImmobilierMapper {

    public BienImmobilierDTO toDTO(BienImmobilier bien) {
        List<String> imageUrls = bien.getImages() != null
                ? new ArrayList<>(bien.getImages())
                : new ArrayList<>();

        List<String> documentUrls = bien.getDocuements() != null
                ? new ArrayList<>(bien.getDocuements())
                : new ArrayList<>();

        return new BienImmobilierDTO(
                bien.getId(),
                bien.getSuperfie(),
                bien.getTitreBien(),
                bien.getNbrePiece(),
                bien.getDescription(),
                bien.getCategorie(),
                bien.getPrix(),
                bien.getAdresse() != null ? bien.getAdresse().getVille() : null,
                bien.getAdresse() != null ? bien.getAdresse().getRegion() : null,
                bien.getAdresse() != null ? bien.getAdresse().getQuartier() : null,
                bien.getAdresse()!= null ? bien.getAdresse().getLattitude(): null,
                bien.getAdresse()!=null ? bien.getAdresse().getLongitude():null,
                bien.getTypePublication(),
                bien.getTypeBienImmobilier(),
                bien.getDatePublication(),
                imageUrls,
                documentUrls,
                bien.getNumeroPaiement()
        );
    }

    public BienImmobilier toEntity(BienImmobilierDTO dto) {
        BienImmobilier bien = new BienImmobilier();

        bien.setId(dto.id());
        bien.setTitreBien(dto.titreBien());
        bien.setSuperfie(dto.superficie());
        bien.setNbrePiece(dto.nbrePiece());
        bien.setDescription(dto.description());
        bien.setCategorie(dto.categorie());
        bien.setPrix(dto.prix());
        bien.setTypePublication(dto.typePublication());
        bien.setTypeBienImmobilier(dto.typeBienImmobilier());
        bien.setImages(dto.images());
        bien.setDocuements(dto.docuements());
        bien.setNumeroPaiement(dto.numeroPaiement());

        if (bien.getAdresse() == null) {
            bien.setAdresse(new Adresse());
        }
        bien.getAdresse().setVille(dto.ville());
        bien.getAdresse().setRegion(dto.region());
        bien.getAdresse().setQuartier(dto.quartier());
        bien.getAdresse().setLattitude(dto.lattitude());
        bien.getAdresse().setLongitude(dto.longitude());

        return bien;
    }
}

