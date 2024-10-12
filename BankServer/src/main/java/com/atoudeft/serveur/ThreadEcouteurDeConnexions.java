package com.atoudeft.serveur;

/**
 * Cette classe permet de créer des threads capables d'écouter continuellement sur un objet de type Serveur
 * l'arrivée de nouveaux clients.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.1
 * @since 2024-06-01
 */
public class ThreadEcouteurDeConnexions extends Thread {
    /**
     * Délai de sleep dans la boucle d'écoute.
     */
    public static int DELAI_SLEEP = 100;
    private Serveur serveur;

    /**
     * Construit un thread sur un lecteur
     *
     * @param s Serveur Le serveur sur lequel le thread va écouter
     */
    public ThreadEcouteurDeConnexions(Serveur s) {
        serveur = s;
    }

    /**
     * Méthode principale du thread. Cette méthode appelle continuellement la méthode attendConnexion() du serveur.
     */
    public void run() {
        while (!interrupted()) {
            serveur.attendConnexion();
            try {
                Thread.sleep(DELAI_SLEEP);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}