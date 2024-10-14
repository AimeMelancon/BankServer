package com.atoudeft.banque;

public class CompteCheque extends CompteBancaire{
    private String numero;
    private TypeCompte type;
    private double solde;


    /**
     * Crée un compte bancaire.
     *
     * @param numero numéro du compte
     * @param type   type du compte
     */
    public CompteCheque(String numero, TypeCompte type) {
        super(numero, type);
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
     * Retire le montant au solde s'il est strictement positif et s'il y a assez de fonds
     * @param montant   Montant à débiter
     * @return True si opération réussie, sinon false.
     */
    @Override
    public boolean debiter(double montant) {
        if (montant > 0 && montant > this.solde) {
            this.solde -= montant;
            return true;
        }
        return false;
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
