package com.dreamhousesystem.dreamhouse.Repositories;

import com.dreamhousesystem.dreamhouse.Entities.BienImmobilier;
import com.dreamhousesystem.dreamhouse.Entities.CategorieBien;
import com.dreamhousesystem.dreamhouse.Entities.TypePublication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BienImmobilierRepository extends JpaRepository<BienImmobilier, Integer> {

    List<BienImmobilier> findByCategorie(@Param("categorie") CategorieBien categorie);

    List<BienImmobilier> findByTypePublication(@Param("typePublication") TypePublication typePublication);

    List<BienImmobilier> findByPrixLessThanEqual(@Param("prix") Double prix);

    List<BienImmobilier> findByNbrePieceGreaterThanEqual(@Param("nbrePiece") int nbrePiece);

    List<BienImmobilier> findByAdresse_VilleIgnoreCase(@Param("ville") String ville);

    List<BienImmobilier> findByAdresse_Region(@Param("region") String region);


    List<BienImmobilier> findByAdresse_Quartier(@Param("quartier") String quartier);


    List<BienImmobilier> findByProprietaireEmailIgnoreCase(@Param("email") String email);

    @Query("SELECT b FROM BienImmobilier b ORDER BY b.datePublication DESC")
    List<BienImmobilier> findRecentPublications();

    List<BienImmobilier> findByAdresse_VilleAndPrixLessThanEqual(@Param("ville") String ville,
                                                                 @Param("prix") Double prix);


    @Query("SELECT b.adresse.ville, COUNT(b) FROM BienImmobilier b GROUP BY b.adresse.ville")
    List<Object[]> countBiensByVille();

    @Query("SELECT b.adresse.region, COUNT(b) FROM BienImmobilier b GROUP BY b.adresse.region")
    List<Object[]> countBiensByRegion();

    @Query("SELECT b.adresse.quartier, COUNT(b) FROM BienImmobilier b GROUP BY b.adresse.quartier")
    List<Object[]> countBiensByQuartier();
}
