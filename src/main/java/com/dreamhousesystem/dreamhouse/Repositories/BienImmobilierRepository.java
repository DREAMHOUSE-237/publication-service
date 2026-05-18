package com.dreamhousesystem.dreamhouse.Repositories;

import com.dreamhousesystem.dreamhouse.Entities.BienImmobilier;
import com.dreamhousesystem.dreamhouse.Entities.CategorieBien;
import com.dreamhousesystem.dreamhouse.Entities.StatutPublication;
import com.dreamhousesystem.dreamhouse.Entities.TypePublication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BienImmobilierRepository extends JpaRepository<BienImmobilier, Integer> {

    // ✅ Toutes les queries publiques filtrent sur ACTIVE uniquement
    List<BienImmobilier> findByStatutPublication(StatutPublication statut);

    List<BienImmobilier> findByCategorieAndStatutPublication(
            @Param("categorie") CategorieBien categorie,
            @Param("statut") StatutPublication statut);

    List<BienImmobilier> findByTypePublicationAndStatutPublication(
            @Param("typePublication") TypePublication typePublication,
            @Param("statut") StatutPublication statut);

    List<BienImmobilier> findByPrixLessThanEqualAndStatutPublication(
            @Param("prix") Double prix,
            @Param("statut") StatutPublication statut);

    List<BienImmobilier> findByNbrePieceGreaterThanEqualAndStatutPublication(
            @Param("nbrePiece") int nbrePiece,
            @Param("statut") StatutPublication statut);

    List<BienImmobilier> findByAdresse_VilleIgnoreCaseAndStatutPublication(
            @Param("ville") String ville,
            @Param("statut") StatutPublication statut);

    List<BienImmobilier> findByAdresse_RegionAndStatutPublication(
            @Param("region") String region,
            @Param("statut") StatutPublication statut);

    List<BienImmobilier> findByAdresse_QuartierAndStatutPublication(
            @Param("quartier") String quartier,
            @Param("statut") StatutPublication statut);

    List<BienImmobilier> findByAdresse_VilleAndPrixLessThanEqualAndStatutPublication(
            @Param("ville") String ville,
            @Param("prix") Double prix,
            @Param("statut") StatutPublication statut);

    // Sans filtre statut → pour les biens du propriétaire (il voit tous ses biens, y compris EN_ATTENTE)
    List<BienImmobilier> findByProprietaireEmailIgnoreCase(@Param("email") String email);

    @Query("SELECT b FROM BienImmobilier b WHERE b.statutPublication = 'ACTIVE' ORDER BY b.datePublication DESC")
    List<BienImmobilier> findRecentPublications();

    // Stats (inchangées)
    @Query("SELECT b.adresse.ville, COUNT(b) FROM BienImmobilier b GROUP BY b.adresse.ville")
    List<Object[]> countBiensByVille();

    @Query("SELECT b.adresse.region, COUNT(b) FROM BienImmobilier b GROUP BY b.adresse.region")
    List<Object[]> countBiensByRegion();

    @Query("SELECT b.adresse.quartier, COUNT(b) FROM BienImmobilier b GROUP BY b.adresse.quartier")
    List<Object[]> countBiensByQuartier();
}