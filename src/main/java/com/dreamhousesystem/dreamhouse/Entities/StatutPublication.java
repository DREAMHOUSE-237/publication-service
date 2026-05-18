package com.dreamhousesystem.dreamhouse.Entities;

public enum StatutPublication {
    EN_ATTENTE,  // bien créé, paiement pas encore confirmé → non visible publiquement
    ACTIVE,      // paiement SUCCESS → visible publiquement
    REJETEE      // paiement FAILED → non visible
}