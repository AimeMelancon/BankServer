package com.atoudeft.banque.serveur;

public class ThreadDesInactifs extends Thread {
    public static int DELAI_SLEEP = 1000;
    private ServeurBanque serveurBanque;

    public ThreadDesInactifs(ServeurBanque serveurBanque) {
        this.serveurBanque = serveurBanque;
    }

    /**
     * Demande au serveur de banque de supprimer les connexions inactives, à chaque DELAI_SLEEP millisecondes.
     */
    @Override
    public void run() {
        while (!interrupted()) {
            serveurBanque.supprimeInactifs();//Supprime toutes les connexions inactives.
            try {
                Thread.sleep(DELAI_SLEEP);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
