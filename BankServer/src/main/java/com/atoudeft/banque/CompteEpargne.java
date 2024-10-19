package com.atoudeft.banque;

public class CompteEpargne extends CompteBancaire{
    private String numero;
    private TypeCompte type;
    private double solde;

    private final double tauxInteret;
    private double limite = 1000.0;
    private double frais = 2.0;


    /**
     * Crée un compte bancaire.
     *
     * @param numero numéro du compte
     * @param type   type du compte
     */
    public CompteEpargne(String numero, TypeCompte type, double tauxInteret) {
        super(numero, type);
        this.tauxInteret = tauxInteret;
    }

    // Accesseurs
    public String getNumero() { return this.numero; }
    public double getSolde() { return this.solde; }

    /**
     * Ajoute le montant s'il est strictement positif
     * @param montant   Montant à créditer
     * @return True si opération réussie, sinon false.
     */
    @Override
    public boolean crediter(double montant) {
        if (montant > 0) {
            this.solde += montant;
            return true;
        }

        return false;
    }

    /**
     * Retire le montant au solde s'il est strictement positif et s'il y a assez de fonds. Des frais peuvent être prélevés.
     * @param montant   Montant à débiter
     * @return True si opération réussie, sinon false.
     */
    @Override
    public boolean debiter(double montant) {
        if (montant > 0 && this.solde >= montant) {
            this.solde -= montant;
            if (this.solde < limite) {
                this.solde -= frais; // Prélèvement de frais si le solde est inférieur à 1000$
            }
            return true;
        }
        return false;
    }

    // Méthode pour ajouter les intérêts
    public void ajouterInterets() {
        this.solde += this.solde * tauxInteret;
    }

    /**
     * @param numeroFacture
     * @param montant
     * @param description
     * @return
     */
    @Override
    public boolean payerFacture(String numeroFacture, double montant, String description) {
        return false;
    }

    /**
     * @param montant
     * @param numeroCompteDestinataire
     * @return
     */
    @Override
    public boolean transferer(double montant, String numeroCompteDestinataire) {
        return false;
    }
}
