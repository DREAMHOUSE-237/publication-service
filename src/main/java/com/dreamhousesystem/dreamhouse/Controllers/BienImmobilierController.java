package com.dreamhousesystem.dreamhouse.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dreamhousesystem.dreamhouse.DTO.BienImmobilierDTO;
import com.dreamhousesystem.dreamhouse.Entities.BienImmobilier;
import com.dreamhousesystem.dreamhouse.Entities.CategorieBien;
import com.dreamhousesystem.dreamhouse.Entities.TypePublication;
import com.dreamhousesystem.dreamhouse.Services.BienImmobilierService;
import com.dreamhousesystem.dreamhouse.Services.CloudinaryService;
import com.dreamhousesystem.dreamhouse.exception.UnsupportedFileTypeException;

@RestController
@RequestMapping("/api/biens")
//@CrossOrigin(origins = "*")
public class BienImmobilierController {

    private final BienImmobilierService service;
    private final CloudinaryService cloudinaryService;

    public BienImmobilierController(BienImmobilierService service, CloudinaryService cloudinaryService) {
        this.service = service;
        this.cloudinaryService = cloudinaryService;
    }


    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<BienImmobilierDTO> createBien(
            @RequestPart("bien") BienImmobilier bien,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "documents", required = false) List<MultipartFile> documents) {

        List<String> imageUrls = new ArrayList<>();
        List<String> documentUrls = new ArrayList<>();

        try {
            if (images != null) {
                for (MultipartFile img : images) {
                    imageUrls.add(cloudinaryService.uploadFile(img));
                }
            }
            if (documents != null) {
                for (MultipartFile doc : documents) {
                    documentUrls.add(cloudinaryService.uploadFile(doc));
                }
            }

            bien.setImages(imageUrls);
            bien.setDocuements(documentUrls);

            BienImmobilierDTO savedBien = service.createBien(bien);
            return ResponseEntity.ok(savedBien);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Update avec fichiers si besoin
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<BienImmobilierDTO> updateBien(
            @PathVariable int id,
            @RequestPart("bien") BienImmobilier bien,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "documents", required = false) List<MultipartFile> documents) {

        List<String> imageNames = new ArrayList<>();
        List<String> documentNames = new ArrayList<>();

        try {
            if (images != null) {
                for (MultipartFile img : images) {
                    imageNames.add(cloudinaryService.uploadFile(img)
                    );
                }
            }
            if (documents != null) {
                for (MultipartFile doc : documents) {
                    documentNames.add(cloudinaryService.uploadFile(doc)
                    );
                }
            }

            bien.setImages(imageNames);
            bien.setDocuements(documentNames);

            BienImmobilierDTO updatedBien = service.updateBien(id, bien);
            return ResponseEntity.ok(updatedBien);

        } catch (IOException | UnsupportedFileTypeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBien(@PathVariable int id) {
        service.deleteBien(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BienImmobilierDTO> getBienById(@PathVariable int id) {
        return ResponseEntity.ok(service.getBienById(id));
    }

    @GetMapping
    public ResponseEntity<List<BienImmobilierDTO>> getAllBiens() {
        return ResponseEntity.ok(service.getAllBiens());
    }

    // Filtres
    @GetMapping("/search/categorie")
    public ResponseEntity<List<BienImmobilierDTO>> findByCategorie(@RequestParam CategorieBien categorie) {
        return ResponseEntity.ok(service.findByCategorie(categorie));
    }

    @GetMapping("/search/type-publication")
    public ResponseEntity<List<BienImmobilierDTO>> findByTypePublication(@RequestParam TypePublication typePublication) {
        return ResponseEntity.ok(service.findByTypePublication(typePublication));
    }

    @GetMapping("/search/prix-max")
    public ResponseEntity<List<BienImmobilierDTO>> findByPrixMax(@RequestParam Double prix) {
        return ResponseEntity.ok(service.findByPrixMax(prix));
    }

    @GetMapping("/search/nbre-piece-min")
    public ResponseEntity<List<BienImmobilierDTO>> findByNbrePieceMin(@RequestParam int nbrePiece) {
        return ResponseEntity.ok(service.findByNbrePieceMin(nbrePiece));
    }

    @GetMapping("/search/ville")
    public ResponseEntity<List<BienImmobilierDTO>> findByVille(@RequestParam String ville) {
        return ResponseEntity.ok(service.findByVille(ville));
    }

    @GetMapping("/search/quartier")
    public ResponseEntity<List<BienImmobilierDTO>> findByQuatier(@RequestParam String quartier) {
        return ResponseEntity.ok(service.finByQuartier(quartier));
    }

    @GetMapping("/search/region")
    public ResponseEntity<List<BienImmobilierDTO>> findByRegion(@RequestParam String region) {
        return ResponseEntity.ok(service.findByRegion(region));
    }

    @GetMapping("/search/ville-prix")
    public ResponseEntity<List<BienImmobilierDTO>> findByVilleAndPrix(@RequestParam String ville,
                                                                      @RequestParam Double prix) {
        return ResponseEntity.ok(service.findByVilleAndPrix(ville, prix));
    }

    @GetMapping("/search/recent")
    public ResponseEntity<List<BienImmobilierDTO>> findyPublicationRecente() {
        return ResponseEntity.ok(service.getRecentPublications());
    }

    // ROUTE POUR LES BIENS D'UN PROPRIÉTAIRE SPÉCIFIQUE
    @GetMapping("/proprietaire/{email}")
    public ResponseEntity<List<BienImmobilierDTO>> findByProprietaireEmail(@PathVariable String email) {
        List<BienImmobilierDTO> biens = service.findByProprietaireEmail(email);
        return ResponseEntity.ok(biens);
    }

    // ROUTE POUR LES BIENS DE L'UTILISATEUR CONNECTE (via RabbitMQ)
    @GetMapping("/mes-publications")
    public ResponseEntity<List<BienImmobilierDTO>> findMesBiens() {
        try {
            List<BienImmobilierDTO> mesBiens = service.findMesBiens();
            return ResponseEntity.ok(mesBiens);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ArrayList<>());
        }
    }
}