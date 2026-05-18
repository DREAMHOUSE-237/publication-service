package com.dreamhousesystem.dreamhouse.Services;

import com.dreamhousesystem.dreamhouse.DTO.BienImmobilierDTO;
import com.dreamhousesystem.dreamhouse.Entities.BienImmobilier;
import com.dreamhousesystem.dreamhouse.Entities.CategorieBien;
import com.dreamhousesystem.dreamhouse.Entities.TypePublication;
import com.dreamhousesystem.dreamhouse.Mappers.BienImmobilierMapper;
import com.dreamhousesystem.dreamhouse.Messaging.PaymentProducer;
import com.dreamhousesystem.dreamhouse.Messaging.PaymentStatusConsumer;
import com.dreamhousesystem.dreamhouse.Messaging.UserEmailConsumer;
import com.dreamhousesystem.dreamhouse.Repositories.BienImmobilierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dreamhousesystem.dreamhouse.strategie.ContextePrix;
import com.dreamhousesystem.dreamhouse.strategie.StrategiePrixVente;
import com.dreamhousesystem.dreamhouse.strategie.StrategiePrixLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BienImmobilierServiceImpl implements BienImmobilierService {

    private final BienImmobilierRepository repository;
    private final BienImmobilierMapper mapper;
    private final UserEmailConsumer userEmailConsumer;
    private final PaymentProducer paymentProducer;
    private final PaymentStatusConsumer paymentStatusConsumer;

    public BienImmobilierServiceImpl(BienImmobilierRepository repository,
                                     BienImmobilierMapper mapper,
                                     UserEmailConsumer userEmailConsumer,
                                     PaymentProducer paymentProducer,
                                     PaymentStatusConsumer paymentStatusConsumer) {
        this.repository = repository;
        this.mapper = mapper;
        this.userEmailConsumer = userEmailConsumer;
        this.paymentProducer = paymentProducer;
        this.paymentStatusConsumer = paymentStatusConsumer;
    }

    @Override
    @Transactional
    public BienImmobilierDTO createBien(BienImmobilier bien) {
        if (bien.getImages() == null) bien.setImages(new ArrayList<>());
        if (bien.getDocuements() == null) bien.setDocuements(new ArrayList<>());

        String email = userEmailConsumer.getCurrentUserEmail();
        bien.setProprietaireEmail(email);

        ContextePrix contexte = new ContextePrix();
        switch (bien.getTypePublication()) {
            case VENTE -> contexte.definirStrategie(new StrategiePrixVente());
            case LOCATION -> contexte.definirStrategie(new StrategiePrixLocation());
            default -> throw new IllegalArgumentException("Type de publication non supporte");
        }

        double prixFinal = contexte.appliquerStrategie(bien.getPrix());
        System.out.println("Prix calcule avec strategie : " + prixFinal);

        // ✅ FIX : sauvegarder D'ABORD pour obtenir l'id généré en base
        BienImmobilier bienEnregistre = repository.save(bien);

        // ✅ FIX : envoyer le message APRÈS la sauvegarde avec l'id réel
        paymentProducer.sendPaymentRequest(
                bienEnregistre.getProprietaireEmail(),
                bienEnregistre.getDescription(),
                prixFinal,
                bienEnregistre.getNumeroPaiement(),
                bienEnregistre.getId() // ← idPublication maintenant disponible
        );

        return mapper.toDTO(bienEnregistre);
    }

    @Override
    @Transactional
    public BienImmobilierDTO updateBien(int id, BienImmobilier bien) {
        BienImmobilier existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bien non trouvé"));

        existing.setSuperfie(bien.getSuperfie());
        existing.setNbrePiece(bien.getNbrePiece());
        existing.setPrix(bien.getPrix());
        existing.setDescription(bien.getDescription());
        existing.setCategorie(bien.getCategorie());
        existing.setTypePublication(bien.getTypePublication());
        existing.setAdresse(bien.getAdresse());
        existing.setImages(bien.getImages() != null ? bien.getImages() : new ArrayList<>());
        existing.setDocuements(bien.getDocuements() != null ? bien.getDocuements() : new ArrayList<>());
        existing.setProprietaireEmail(userEmailConsumer.getCurrentUserEmail());

        return mapper.toDTO(repository.save(existing));
    }

    @Override
    public void deleteBien(int id) {
        repository.deleteById(id);
    }

    @Override
    public BienImmobilierDTO getBienById(int id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Bien non trouvé"));
    }

    @Override
    public List<BienImmobilierDTO> getAllBiens() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> findByCategorie(CategorieBien categorie) {
        return repository.findByCategorie(categorie)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> findByTypePublication(TypePublication typePublication) {
        return repository.findByTypePublication(typePublication)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> findByPrixMax(Double prix) {
        return repository.findByPrixLessThanEqual(prix)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> findByNbrePieceMin(int nbrePiece) {
        return repository.findByNbrePieceGreaterThanEqual(nbrePiece)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> findByVille(String ville) {
        return repository.findByAdresse_VilleIgnoreCase(ville)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> finByQuartier(String quartier) {
        return repository.findByAdresse_Quartier(quartier)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> getRecentPublications() {
        return repository.findRecentPublications()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> findByRegion(String region) {
        return repository.findByAdresse_Region(region)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> findByRegionCurrent() {
        String region = userEmailConsumer.getCurrentRegionDisplay();
        return findByRegion(region);
    }

    @Override
    public List<BienImmobilierDTO> findByVilleAndPrix(String ville, Double prix) {
        return repository.findByAdresse_VilleAndPrixLessThanEqual(ville, prix)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> findByProprietaireEmail(String email) {
        return repository.findByProprietaireEmailIgnoreCase(email)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> findMesBiens() {
        String userEmail = userEmailConsumer.getCurrentUserEmail();

        if (userEmail == null) {
            throw new RuntimeException("Veillez vous connecter pour visualiser vos publications");
        }

        return repository.findByProprietaireEmailIgnoreCase(userEmail)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}