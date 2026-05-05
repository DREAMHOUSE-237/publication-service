package com.dreamhousesystem.dreamhouse.Controllers;

import com.dreamhousesystem.dreamhouse.DTO.StatistiqueDTO;
import com.dreamhousesystem.dreamhouse.Services.StatistiqueService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class StatistiqueController {

    private final StatistiqueService service;

    public StatistiqueController(StatistiqueService service) {
        this.service = service;
    }

    @GetMapping("/ville")
    public List<StatistiqueDTO> statsByVille() {
        return service.getStatsByVille();
    }

    @GetMapping("/region")
    public List<StatistiqueDTO> statsByRegion() {
        return service.getStatsByRegion();
    }

    @GetMapping("/quartier")
    public List<StatistiqueDTO> statsByQuartier() {
        return service.getStatsByQuartier();
    }
}
