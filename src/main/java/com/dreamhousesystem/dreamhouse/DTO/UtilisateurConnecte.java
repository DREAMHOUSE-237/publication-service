package com.dreamhousesystem.dreamhouse.DTO;

import java.util.Arrays;

/**
 * Utilisateur à l'origine de la requête, tel que le gateway l'a identifié à
 * partir du JWT (en-têtes X-User-Email / X-User-Roles). Le gateway supprime
 * ces en-têtes quand ils viennent du client : ils sont donc fiables ici.
 */
public record UtilisateurConnecte(String email, String roles) {

    public boolean estConnecte() {
        return email != null && !email.isBlank();
    }

    public boolean estAdmin() {
        return roles != null && Arrays.stream(roles.split(","))
                .anyMatch(role -> role.trim().equalsIgnoreCase("admin"));
    }
}
