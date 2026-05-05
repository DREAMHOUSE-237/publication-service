package com.dreamhousesystem.dreamhouse.Mappers;

import com.dreamhousesystem.dreamhouse.DTO.BienImmobilierDTO;
import com.dreamhousesystem.dreamhouse.Entities.BienImmobilier;
import com.dreamhousesystem.dreamhouse.Entities.Adresse;
import com.dreamhousesystem.dreamhouse.utils.FileUrlBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BienImmobilierMapper {

    private final FileUrlBuilder fileUrlBuilder;

    public BienImmobilierMapper(FileUrlBuilder fileUrlBuilder) {
        this.fileUrlBuilder = fileUrlBuilder;
    }

    public BienImmobilierDTO toDTO(BienImmobilier bien) {
        List<String> imageUrls = bien.getImages() != null
                ? bien.getImages().stream()
                .map(fileUrlBuilder::buildUrl)
                .collect(Collectors.toList())
                : new ArrayList<>();

        List<String> documentUrls = bien.getDocuements() != null
                ? bien.getDocuements().stream()
                .map(fileUrlBuilder::buildUrl)
                .collect(Collectors.toList())
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
        bien.setImages(dto.images());
        bien.setDocuements(dto.docuements());
        bien.setNumeroPaiement(dto.numeroPaiement());

        if (bien.getAdresse() == null) {
            bien.setAdresse(new Adresse());
        }
        bien.getAdresse().setVille(dto.ville());
        bien.getAdresse().setRegion(dto.region());
        bien.getAdresse().setQuartier(dto.quartier());

        return bien;
    }
}
