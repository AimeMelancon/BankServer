package com.atoudeft.serveur;

import com.atoudeft.banque.Banque;
import com.atoudeft.banque.serveur.ConnexionBanque;
import com.atoudeft.banque.serveur.ServeurBanque;
import com.atoudeft.commun.evenement.Evenement;
import com.atoudeft.commun.evenement.GestionnaireEvenement;
import com.atoudeft.commun.net.Connexion;

/**
 * Cette classe représente un gestionnaire d'événement d'un serveur. Lorsqu'un serveur reçoit un texte d'un client,
 * il crée un événement à partir du texte reçu et alerte ce gestionnaire qui réagit en gérant l'événement.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementServeur implements GestionnaireEvenement {
    private Serveur serveur;

    /**
     * Construit un gestionnaire d'événements pour un serveur.
     *
     * @param serveur Serveur Le serveur pour lequel ce gestionnaire gère des événements
     */
    public GestionnaireEvenementServeur(Serveur serveur) {
        this.serveur = serveur;
    }

    /**
     * Méthode de gestion d'événements. Cette méthode contiendra le code qui gère les réponses obtenues d'un client.
     *
     * @param evenement L'événement à gérer.
     */
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();
        ServeurBanque serveurBanque = (ServeurBanque)serveur;
        Banque banque;
        ConnexionBanque cnx;
        String msg, typeEvenement, argument, numCompteClient, nip;
        String[] t;

        if (source instanceof Connexion) {
            cnx = (ConnexionBanque) source;
            System.out.println("SERVEUR: Recu : " + evenement.getType() + " " + evenement.getArgument());
            typeEvenement = evenement.getType();
            cnx.setTempsDerniereOperation(System.currentTimeMillis());
            switch (typeEvenement) {
                /******************* COMMANDES GÉNÉRALES *******************/
                case "EXIT": //Ferme la connexion avec le client qui a envoyé "EXIT":
                    cnx.envoyer("END");
                    serveurBanque.enlever(cnx);
                    cnx.close();
                    break;
                case "LIST": //Envoie la liste des numéros de comptes-clients connectés :
                    cnx.envoyer("LIST " + serveurBanque.list());
                    break;
                /******************* COMMANDES DE GESTION DE COMPTES *******************/
                case "NOUVEAU": //Crée un nouveau compte-client :
                    if (cnx.getNumeroCompteClient()!=null) {
                        cnx.envoyer("NOUVEAU NO deja connecte");
                        break;
                    }
                    argument = evenement.getArgument();
                    t = argument.split(":");
                    if (t.length<2) {
                        cnx.envoyer("NOUVEAU NO");
                    }
                    else {
                        numCompteClient = t[0];
                        nip = t[1];
                        banque = serveurBanque.getBanque();
                        if (banque.ajouter(numCompteClient,nip)) {
                            cnx.setNumeroCompteClient(numCompteClient);
                            cnx.setNumeroCompteActuel(banque.getNumeroCompteParDefaut(numCompteClient));
                            cnx.envoyer("NOUVEAU OK " + t[0] + " cree");
                        }
                        else
                            cnx.envoyer("NOUVEAU NO "+t[0]+" existe");
                    }
                    break;

                case "CONNECT": // Se connecte à un compte client

                    // récuperer login info
                    argument = evenement.getArgument();
                    t = argument.split(":");
                    String input_num_compte;
                    String input_nip;

                    try {
                        input_num_compte = t[0];
                        input_nip = t[1];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        cnx.envoyer("CONNECT NO");
                        break;
                    }

                    banque = serveurBanque.getBanque();

                    // Vérifier si déjà connecté au compte
                    if (cnx.getNumeroCompteClient()!=null) {
                        cnx.envoyer("CONNECT NO deja connecte");
                        break;
                    }

                    // Vérifier si le numéro de compte entré existe
                    if (banque.getCompteClient(input_num_compte) == null) {
                        cnx.envoyer("CONNECT NO compte non trouvé");
                        break;
                    }

                    // Vérifier nip
                    if (!banque.getCompteClient(input_num_compte).getNip().equals(input_nip)) {
                        cnx.envoyer("CONNECT NO nip invalide");
                        break;
                    }

                    // Ajouter le numéro du compte-client et du compte-chèque dans l'objet ConnexionBanque
                    cnx.setNumeroCompteClient(input_num_compte);

                    // Obtenir le numéro de compte-chèque par défaut, et le set.
                    String numCompteCheque = banque.getNumeroCompteParDefaut(input_num_compte);
                    cnx.setNumeroCompteActuel(numCompteCheque);

                    // Envoyer la confirmation de connexion au client
                    cnx.envoyer("CONNECT OK");
                    break;


                /******************* TRAITEMENT PAR DÉFAUT *******************/
                default: //Renvoyer le texte recu convertit en majuscules :
                    msg = (evenement.getType() + " " + evenement.getArgument()).toUpperCase();
                    cnx.envoyer(msg);
            }
        }
    }
}