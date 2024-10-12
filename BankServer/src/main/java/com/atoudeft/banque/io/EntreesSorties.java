package com.atoudeft.banque.io;

import com.atoudeft.banque.Banque;

import java.io.*;

public class EntreesSorties {
    public static final String NOM_FICHIER_BANQUE = "banque.data";

    /**
     * Sauvegarde un objet de type Banque dans le fichier dont le nom est dans l'attribut NOM_FICHIER_BANQUE.
     *
     * @param banque la banque
     * @return true si la sauvegarde réussit
     */
    public static boolean sauvegarder(Banque banque) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOM_FICHIER_BANQUE))) {
            oos.writeObject(banque);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Récupère un objet de type Banque du fichier dont le nom est dans l'attribut NOM_FICHIER_BANQUE.
     *
     * @return l'objet banque lu dans le fichier. Retourne null s'il y a un problème de lecture
     */
    public static Banque charger() {
        try (ObjectInputStream ios = new ObjectInputStream(new FileInputStream(NOM_FICHIER_BANQUE))) {
            return (Banque)ios.readObject();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}