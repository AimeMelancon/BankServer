package com.atoudeft.banque;

import java.io.Serializable;

public abstract class CompteBancaire implements Serializable {
    private String numero;
    private TypeCompte type;
    private double solde;

    /**
     * Génère un numéro de compte bancaire aléatoirement avec le format CCC00C, où C est un caractère alphabétique
     * majuscule et 0 est un chiffre entre 0 et 9.
     * @return
     */
    public static String genereNouveauNumero() {
        char[] t = new char[6];
        for (int i=0;i<3;i++) {
            t[i] = (char)((int)(Math.random()*26)+'A');
        }
        for (int i=3;i<5;i++) {
            t[i] = (char)((int)(Math.random()*10)+'0');
        }
        t[5] = (char)((int)(Math.random()*26)+'A');
        return new String(t);
    }

    /**
     * Crée un compte bancaire.
     * @param numero numéro du compte
     * @param type type du compte
     */
    public CompteBancaire(String numero, TypeCompte type) {
        this.numero = numero;
        this.type = type;
        this.solde = 0;
    }
    public String getNumero() {
        return numero;
    }
    public TypeCompte getType() {
        return type;
    }
    public double getSolde() {
        return solde;
    }
    public abstract boolean crediter(double montant);
    public abstract boolean debiter(double montant);
    public abstract boolean payerFacture(String numeroFacture, double montant, String description);
    public abstract boolean transferer(double montant, String numeroCompteDestinataire);
}