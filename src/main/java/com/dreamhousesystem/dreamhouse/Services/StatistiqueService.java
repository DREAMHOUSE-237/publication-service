package com.dreamhousesystem.dreamhouse.Services;

import com.dreamhousesystem.dreamhouse.DTO.StatistiqueDTO;
import com.dreamhousesystem.dreamhouse.Repositories.BienImmobilierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatistiqueService {

    private final BienImmobilierRepository repository;

    public StatistiqueService(BienImmobilierRepository repository) {
        this.repository = repository;
    }

    public List<StatistiqueDTO> getStatsByVille() {
        return repository.countBiensByVille()
                .stream()
                .map(obj -> new StatistiqueDTO((String) obj[0], (Long) obj[1]))
                .toList();
    }

    public List<StatistiqueDTO> getStatsByRegion() {
        return repository.countBiensByRegion()
                .stream()
                .map(obj -> new StatistiqueDTO((String) obj[0], (Long) obj[1]))
                .toList();
    }

    public List<StatistiqueDTO> getStatsByQuartier() {
        return repository.countBiensByQuartier()
                .stream()
                .map(obj -> new StatistiqueDTO((String) obj[0], (Long) obj[1]))
                .toList();
    }
}
