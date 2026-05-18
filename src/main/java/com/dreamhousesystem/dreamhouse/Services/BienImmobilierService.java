package com.dreamhousesystem.dreamhouse.Services;

import com.dreamhousesystem.dreamhouse.DTO.BienImmobilierDTO;
import com.dreamhousesystem.dreamhouse.Entities.BienImmobilier;
import com.dreamhousesystem.dreamhouse.Entities.CategorieBien;
import com.dreamhousesystem.dreamhouse.Entities.TypePublication;

import java.util.List;

public interface BienImmobilierService {
    BienImmobilierDTO createBien(BienImmobilier bien);
    BienImmobilierDTO updateBien(int id, BienImmobilier bien);
    void deleteBien(int id);
    BienImmobilierDTO getBienById(int id);
    List<BienImmobilierDTO> getAllBiens();
    List<BienImmobilierDTO> getRecentPublications();
    List<BienImmobilierDTO> findByCategorie(CategorieBien categorie);
    List<BienImmobilierDTO> findByTypePublication(TypePublication typePublication);
    List<BienImmobilierDTO> findByPrixMax(Double prix);
    List<BienImmobilierDTO> findByNbrePieceMin(int nbrePiece);
    List<BienImmobilierDTO> findByVille(String ville);
    List<BienImmobilierDTO> findByRegion(String region);
    List<BienImmobilierDTO> findByRegionCurrent();
    public List<BienImmobilierDTO> finByQuartier(String quartier);
    List<BienImmobilierDTO> findByVilleAndPrix(String ville, Double prix);
    List<BienImmobilierDTO> findByProprietaireEmail(String email);
    List<BienImmobilierDTO> findMesBiens();
}
