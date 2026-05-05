package com.dreamhousesystem.dreamhouse.DTO;

import com.dreamhousesystem.dreamhouse.Entities.CategorieBien;
import com.dreamhousesystem.dreamhouse.Entities.Region;
import com.dreamhousesystem.dreamhouse.Entities.TypePublication;
import java.time.LocalDateTime;
import java.util.List;

public record BienImmobilierDTO(
        int id,
        Double superficie,
        String titreBien,
        int nbrePiece,
        String description,
        CategorieBien categorie,
        Double prix,
        String ville,
        Region region,
        String quartier,
        Double longitue,
        Double Longitude,
        TypePublication typePublication,
        LocalDateTime datePublication,
        List<String> images,
        List<String> docuements,
        String numeroPaiement
) {}
