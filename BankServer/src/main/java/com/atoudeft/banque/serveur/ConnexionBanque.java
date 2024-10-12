package com.atoudeft.banque.serveur;

import com.atoudeft.banque.CompteBancaire;
import com.atoudeft.commun.net.Connexion;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.Socket;

public class ConnexionBanque extends Connexion {
    private String numeroCompteClient;
    private long tempsDerniereOperation;
    //Le numéro du compte sur lequel se font les opérations en ce moment :
    private String numeroCompteActuel;
    /**
     * Construit une connexion sur un socket, initialisant les flux de caractères utilisés par le socket et le moment
     * de la dernière opération effectuée par client utilisant cette connexion.
     *
     * @param s Socket Le socket sur lequel la connexion est créée
     */
    public ConnexionBanque(Socket s) {
        super(s);
        tempsDerniereOperation = System.currentTimeMillis();
    }

    /**
     * Indique si le client utilisant cette connexion est inactif pendant une durée au moins égale à delai en millisecondes.
     *
     * @param delai le délai en millisecondes
     * @return true la durée d'inactivité est supérieure à delai
     */
    public boolean estInactifDepuis(long delai) {
        //À définir :
        throw new NotImplementedException();//ligne à supprimer
    }

    /**
     * Retourne le moment en millisecondes de la dernière opération du client utilisant cette connexion.
     *
     * @return le moment en millisecondes de la dernière opération du client
     */
    public long getTempsDerniereOperation() {
        return tempsDerniereOperation;
    }

    /**
     * Enregistre le moment de la dernière opération du client utilisant cette connexion.
     *
     * @param tempsDerniereOperation le moment en millisecondes de la dernière opération du client
     */
    public void setTempsDerniereOperation(long tempsDerniereOperation) {
        this.tempsDerniereOperation = tempsDerniereOperation;
    }

    /**
     * Récupère le numéro du compte-client du client utilisant cette connexion.
     *
     * @return le numéro du compte-client du client
     */
    public String getNumeroCompteClient() {
        return numeroCompteClient;
    }

    /**
     * Modifie le numéro du compte-client du client utilisant cette connexion.
     *
     * @param numeroCompteClient le numéro du compte-client du client
     */
    public void setNumeroCompteClient(String numeroCompteClient) {
        this.numeroCompteClient = numeroCompteClient;
    }

    /**
     * Retourne le numéro du compte bancaire actuellement sélectionné par le client.
     * @return le numéro du compte bancaire actuellement sélectionné par le client
     */
    public String getNumeroCompteActuel() {
        return numeroCompteActuel;
    }

    /**
     * Spécifie le numéro du compte bancaire sélectionné par le client.
     * @param numeroCompteActuel le numéro du compte bancaire sélectionné par le client
     */
    public void setNumeroCompteActuel(String numeroCompteActuel) {
        this.numeroCompteActuel = numeroCompteActuel;
    }
}