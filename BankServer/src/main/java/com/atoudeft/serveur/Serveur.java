package com.atoudeft.serveur;

import java.util.*;

import com.atoudeft.banque.serveur.ConnexionBanque;
import com.atoudeft.commun.net.Connexion;
import com.atoudeft.commun.thread.Lecteur;
import com.atoudeft.commun.thread.ThreadEcouteurDeTexte;
import com.atoudeft.commun.evenement.Evenement;
import com.atoudeft.commun.evenement.EvenementUtil;
import com.atoudeft.commun.evenement.GestionnaireEvenement;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

/**
 * Cette classe représente un serveur sur lequel des clients peuvent se connecter.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class Serveur implements Lecteur {

    //Liste des connectés au serveur :
    protected final Vector<Connexion> connectes = new Vector<>();

    //Nouveaux clients qui ne se sont pas encore "identifiés" :
    private final Vector<Connexion> nouveaux = new Vector<>();
    //Ce thread s'occupe d'interagir avec les nouveaux pour valider leur connexion :
    private Thread threadNouveaux;
    private int port = 8888;
    //Thread qui attend de nouvelles connexions :
    private ThreadEcouteurDeConnexions ecouteurConnexions;
    //Thread qui écoute l'arrivée de texte des clients connectés :
    private ThreadEcouteurDeTexte ecouteurTexte;
    //Le serveur-socket utilisé par le serveur pour attendre que les clients se connectent :
    private ServerSocket serverSocket;
    //Indique si le serveur est déjà démarré ou non :
    private boolean demarre;
    //Écouteur qui gère les événements correspondant à l'arrivée de texte de clients :
    protected GestionnaireEvenement gestionnaireEvenement;

    /**
     * Crée un serveur qui va écouter sur le port spécifié.
     *
     * @param port int Port d'écoute du serveur
     */
    public Serveur(int port) {
        this.port = port;
    }
    /**
     * Permet de fournir un autre gestionnaire d'événements en remplacement de celui construit par défaut.
     *
     * @param gestionnaireEvenement GestionnaireEvenement le nouveau gestionnaire d'événements
     */
    public void setGestionnaireEvenement(GestionnaireEvenement gestionnaireEvenement) {
        this.gestionnaireEvenement = gestionnaireEvenement;
    }
    /**
     * Démarre le serveur, s'il n'a pas déjà été démarré. Démarre le thread qui écoute l'arrivée de clients et le
     * qui écoute l'arrivée de texte. Mets en place le gestionnaire des événements du serveur.
     *
     * @return boolean true, si le serveur a été démarré correctement, false, si le serveur a déjà été démarré ou s'il
     * y a un problème de connexion
     */
    public boolean demarrer() {
        if (demarre) //Serveur deja demarre.
            return false;
        try {
            serverSocket = new ServerSocket(port);
            ecouteurConnexions = new ThreadEcouteurDeConnexions(this);
            ecouteurConnexions.start();
            ecouteurTexte = new ThreadEcouteurDeTexte(this);
            ecouteurTexte.start();
            gestionnaireEvenement = new GestionnaireEvenementServeur(this);
            demarre = true;
            return true;
        } catch (IOException e) {
            System.out.println("serveurSocket erreur : " + e.getMessage());
        }
        return false;
    }

    /**
     * Arrête le serveur en arrêtant les threads qui écoutent l'arrivée de client, l'arrivée de texte et le traitement
     * des nouveaux clients.
     */
    public void arreter() {
        ListIterator<Connexion> iterateur;
        Connexion cnx;

        if (!demarre)
            return;
        ecouteurConnexions.interrupt();
        ecouteurTexte.interrupt();
        if (threadNouveaux!=null) threadNouveaux.interrupt();
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("serveurSocket erreur : " + e.getMessage());
        }
        //On ferme toutes les connexions après avoir envoer "END." à chacun des clients :
        iterateur = connectes.listIterator();
        while (iterateur.hasNext()) {
            cnx = iterateur.next();
            cnx.envoyer("END");
            cnx.close();
        }
        demarre = false;
    }

    /**
     * Cette méthode bloque sur le ServerSocket du serveur jusqu'à ce qu'un client s'y connecte. Dans ce cas, elle
     * crée la connexion vers ce client et l'ajoute à la liste des nouveaux connectés.
     */
    public void attendConnexion() {
        try {
            Socket sock = serverSocket.accept();
            Connexion cnx = new ConnexionBanque(sock);
            nouveaux.add(cnx);
            System.out.println("Nouveau connecte");
            if (threadNouveaux == null) {
                threadNouveaux = new Thread() {
                    @Override
                    public void run() {
                        int i;
                        Connexion connexion;
                        ListIterator<Connexion> it;
                        boolean verifOK = true;

                        while (!interrupted()) {
                            it = Serveur.this.nouveaux.listIterator();
                            while (it.hasNext()) {
                                connexion = it.next();
                                //Vérifier ici si le client s'est bien identifié, si nécessaire
                                verifOK = validerConnexion(connexion);
                                if (verifOK) {
                                    connexion.envoyer("OK");
                                    it.remove();
                                    Serveur.this.ajouter(connexion);
                                }
                            }
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                break;
                            }
                        }
                    }
                };
                threadNouveaux.start();
            }
        } catch (IOException e) {

        }
    }
    /**
     * Valide l'arrivée d'un nouveau client sur le serveur. L'implémentation dépend des besoins de
     * l'application. Par exemple, cette méthode peut vérifier si le nouveau client a envoyé les bons identifiants
     * (nom et mot de passe).
     * Dans ce TP, cette méthode ne fait rien. Elle se contente de returner true.
     *
     * @param connexion Connexion la connexion représentant le client
     * @return boolean true, si le client a validé correctement son arrivée, false, sinon
     */
    protected boolean validerConnexion(Connexion connexion) {
        return true;
    }
    /**
     * Ajoute la connexion d'un nouveau client à la liste des connectés.
     * @param connexion Connexion la connexion représentant le client
     * @return boolean true, si l'ajout a été effectué avec succès, false, sinon
     */
    protected synchronized boolean ajouter(Connexion connexion) {
        return this.connectes.add(connexion);
    }
    /**
     * Supprime la connexion de la liste des connectés.
     * @param connexion Connexion la connexion représentant le client
     * @return boolean true, si la suppression a été effectuée avec succès, false, sinon
     */
    protected synchronized boolean enlever(Connexion connexion) {
        return this.connectes.remove(connexion);
    }
    /**
     * Cette méthode scanne tous les clients actuellement connectés à ce serveur pour vérifie s'il y a du texte qui
     * arrive. Pour chaque texte qui arrive, elle crée un événement contenant les données du texte et demande au
     * gestionnaire d'événement serveur de traiter l'événement.
     *
     * @author Abdelmoumène Toudeft
     * @version 1.0
     * @since 2023-09-20
     */
    public synchronized void lire() {
        Connexion cnx;
        String[] t;
        Evenement evenement;
        int taille = connectes.size();
        for (int i=0;i<taille;i++) {
            cnx = connectes.get(i);
            String texte = cnx.getAvailableText();
            if (!"".equals(texte)) {
                t = EvenementUtil.extraireInfosEvenement(texte);
                evenement = new Evenement(cnx, t[0], t[1]);
                gestionnaireEvenement.traiter(evenement);
            }
        }
    }
    /**
     * Retourne le port d'écoute de ce serveur
     *
     * @return int Le port d'écoute
     */
    public int getPort() {
        return port;
    }

    /**
     * Spécifie le port d'écoute du serveur.
     *
     * @param port int Le port d'écoute
     */
    public void setPort(int port) {
        this.port = port;
    }
    /**
     * Indique si le serveur a été démarré.
     *
     * @return boolean true si le serveur est démarré et false sinon
     */
    public boolean isDemarre() {
        return demarre;
    }
}