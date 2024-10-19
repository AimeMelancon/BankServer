package com.atoudeft.banque;

//Cet import fonctionnait pas, donc j'ai remplacé sa fonctionnalité
//import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.atoudeft.banque.serveur.ServeurBanque;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Banque implements Serializable {
    private String nom;
    private List<CompteClient> comptes;

    public Banque(String nom) {
        this.nom = nom;
        this.comptes = new ArrayList<>();
    }

    /**
     * Recherche un compte-client à partir de son numéro.
     *
     * @param numeroCompteClient le numéro du compte-client
     * @return le compte-client s'il a été trouvé. Sinon, retourne null
     */

    // Commented-out car j'aime pas cette version de la méthode. J'ai plutôt crée la mienne plus bas.
//    public CompteClient getCompteClient(String numeroCompteClient) {
//        CompteClient cpt = new CompteClient(numeroCompteClient,"");
//        int index = this.comptes.indexOf(cpt);
//        if (index != -1)
//            return this.comptes.get(index);
//        else
//            return null;
//    }

    /**
     * Recherche un compte-client à partir de son numéro.
     *
     * @param numeroCompteClient le numéro du compte-client
     * @return le compte-client s'il a été trouvé. Sinon, retourne null
     */
    public CompteClient getCompteClient(String numeroCompteClient) {
        for (CompteClient compte : this.comptes) {
            if (compte.getNumero().equals(numeroCompteClient)) {
                return compte;
            }
        }
        return null;  // Si aucun compte n'a été trouvé
    }

    /**
     * Vérifier qu'un compte-bancaire appartient bien au compte-client.
     *
     * @param numeroCompteBancaire numéro du compte-bancaire
     * @param numeroCompteClient    numéro du compte-client
     * @return  true si le compte-bancaire appartient au compte-client
     */
    public boolean appartientA(String numeroCompteBancaire, String numeroCompteClient) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Effectue un dépot d'argent dans un compte-bancaire
     *
     * @param montant montant à déposer
     * @param numeroCompte numéro du compte
     * @return true si le dépot s'est effectué correctement
     */
    public boolean deposer(double montant, String numeroCompte) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Effectue un retrait d'argent d'un compte-bancaire
     *
     * @param montant montant retiré
     * @param numeroCompte numéro du compte
     * @return true si le retrait s'est effectué correctement
     */
    public boolean retirer(double montant, String numeroCompte) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Effectue un transfert d'argent d'un compte à un autre de la même banque
     * @param montant montant à transférer
     * @param numeroCompteInitial   numéro du compte d'où sera prélevé l'argent
     * @param numeroCompteFinal numéro du compte où sera déposé l'argent
     * @return true si l'opération s'est déroulée correctement
     */
    public boolean transferer(double montant, String numeroCompteInitial, String numeroCompteFinal) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Effectue un paiement de facture.
     * @param montant montant de la facture
     * @param numeroCompte numéro du compte bancaire d'où va se faire le paiement
     * @param numeroFacture numéro de la facture
     * @param description texte descriptif de la facture
     * @return true si le paiement s'est bien effectuée
     */
    public boolean payerFacture(double montant, String numeroCompte, String numeroFacture, String description) {throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Crée un nouveau compte-client avec un numéro et un nip et l'ajoute à la liste des comptes.
     *
     * @param numCompteClient numéro du compte-client à créer
     * @param nip nip du compte-client à créer
     * @return true si le compte a été créé correctement
     */
    public boolean ajouter(String numCompteClient, String nip) {

        //ASCII:
        //A-Z: 65 - 90
        //0-9: 48 - 57

        // vérifier A-Z et/ou 0-9 seulement pour le num de compte
        int[] ascii = numCompteClient.codePoints().toArray();
        IntStream filter = numCompteClient.codePoints().filter(i -> (i >= 48 && i <= 57) || (i >= 65 && i <= 90));
        int[] filtered_ascii = filter.toArray();

        if (filtered_ascii.length != ascii.length) { // somehow .equals fonctionne pas, donc on compare leur longueur
            return false;
        }

        // vérifier entre 6-8 charactères de longueur pour num de compte
        if (numCompteClient.length() < 6 || numCompteClient.length() > 8) {
            return false;
        }

        // vérifier 0-9 seulement pour le nip
        int[] nip_ascii = nip.codePoints().toArray();
        IntStream filter_nip = nip.codePoints().filter(i -> (i >= 48 && i <= 57));
        int[] filtered_nip = filter_nip.toArray();

        if (filtered_nip.length != nip_ascii.length) {
            return false;
        }

        // vérifier entre 4-5 charactères de longueur pour nip
        if (nip.length() < 4 || nip.length() > 5) {
            return false;
        }

        // ne pas créer compte-client s'il existe déjà
        if (getCompteClient(numCompteClient) != null) {
            return false;
        }
        // Créer nouveau compte-client
        CompteClient compte_client = new CompteClient(numCompteClient,nip);

        // générer numéro compte de banque **unique**
        String num_banque;

        do {
            num_banque = CompteBancaire.genereNouveauNumero();
        } while (this.getCompteClient(num_banque) != null);

        // créer compte chèque par défaut et ajouter au compte client
        CompteCheque compte_cheque = new CompteCheque(num_banque, TypeCompte.CHEQUE);
        compte_client.ajouter(compte_cheque);

        // ajouter compte-client à la liste de comptes
        this.comptes.add(compte_client);


        return true;
    }

    /**
     * Retourne le numéro du compte-chèque d'un client à partir de son numéro de compte-client.
     *
     * @param numCompteClient numéro de compte-client
     * @return numéro du compte-chèque du client ayant le numéro de compte-client
     */
    public String getNumeroCompteParDefaut(String numCompteClient) {
        //À compléter : retourner le numéro du compte-chèque du compte-client.
       CompteClient compte_client = getCompteClient(numCompteClient);

       return compte_client.getNumero();

    }
}