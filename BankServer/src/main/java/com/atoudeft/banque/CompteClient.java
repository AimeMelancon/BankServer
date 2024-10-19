package com.atoudeft.banque;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CompteClient implements Serializable {
    private String numero;
    private String nip;
    private List<CompteBancaire> comptes;

    /**
     * Crée un compte-client avec un numéro et un nip.
     *
     * @param numero le numéro du compte-client
     * @param nip le nip
     */
    public CompteClient(String numero, String nip) {
        this.numero = numero;
        this.nip = nip;
        comptes = new ArrayList<>();
    }
    // Accesseur
    public String getNumero() { return this.numero; }
    public String getNip() { return this.nip; }

    /**
     * Ajoute un compte bancaire au compte-client.
     *
     * @param compte le compte bancaire
     * @return true si l'ajout est réussi
     */
    public boolean ajouter(CompteBancaire compte) {
        return this.comptes.add(compte);
    }

    /**
     * Vérifie si un compte-client a un compte-épargne
     * @return true si en possession d'un compte-épargne, false sinon.
     */
    public boolean possedeCompteEpargne() {
        for (CompteBancaire compte : comptes) {
            if (compte.getType() == TypeCompte.EPARGNE) {
                return true;  // Le client possède déjà un compte-épargne
            }
        }
        return false;  // Aucun compte-épargne trouvé
    }

    /**
     * Recherche un compte bancaire d'un compte-client par type
     * @param type
     * @return Un compte bancaire (épargne / chèque)
     */
    public CompteBancaire getCompteParType(TypeCompte type) {
        for (CompteBancaire compte : comptes) {
            if (compte.getType().equals(type)) {
                return compte;
            }
        }
        return null;  // Si aucun compte de ce type n'est trouvé
    }



}

