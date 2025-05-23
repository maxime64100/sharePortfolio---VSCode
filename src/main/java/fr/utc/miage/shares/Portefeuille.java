/*
 * Copyright 2025 Mathys Alzuria, Tom Montbord, Colas Naudi, Mathis Lague, Maxime Fallek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.utc.miage.shares;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Objet Portefeuille : permet de gérer un portefeuille d'actions.
 */
public class Portefeuille {

    /**
     * Map d'actions et de quantités : clé = action, valeur = quantité.
     */
    private final Map<Action, Integer> actions;
    private final List<Transaction> historique = new ArrayList<>();


    /**
     * Constructeur de la classe Portefeuille.
     * Initialise le portefeuille avec une map vide d'actions.
     */
    public Portefeuille() {
        actions = new HashMap<>();
    }

    /**
     * Achète une certaine quantité d'action et l'ajoute au portefeuille.
     *
     * @param action l'action à acheter
     * @param quantite la quantité d'action à acheter
     */
    public void acheter(Action action, int quantite) {
        if (quantite <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive.");
        }
        Jour jour = new Jour(LocalDate.now().getYear(), LocalDate.now().getDayOfYear());
        actions.put(action, actions.getOrDefault(action, 0) + quantite);
        historique.add(new Transaction(Transaction.Type.ACHAT, action, quantite, jour));
    }

    public void vendreQuantiteMax(Action action) {
        if (!actions.containsKey(action)) {
            throw new IllegalArgumentException("Action non détenue dans le portefeuille.");
        }
        vendre(action, actions.get(action));
    }

    /**
     * Vend une seule action du portefeuille.
     *
     * @param action l'action à vendre
     */
    public void vendreUne(Action action) {
        vendre(action, 1);
    }

    /**
     * Vend une certaine quantité d'action du portefeuille.
     *
     * @param action l'action à vendre
     * @param quantite la quantité d'action à vendre
     */
    public void vendre(Action action, int quantite) {
        if (!actions.containsKey(action)) {
            throw new IllegalArgumentException("Action non détenue dans le portefeuille.");
        }
        int quantiteActuelle = actions.get(action);
        if (quantite > quantiteActuelle) {
            throw new IllegalArgumentException("Quantité à vendre supérieure à la quantité détenue.");
        }
        if (quantite == quantiteActuelle) {
            actions.remove(action);
        } else {
            Jour jour = new Jour(LocalDate.now().getYear(), LocalDate.now().getDayOfYear());
            actions.put(action, quantiteActuelle - quantite);
            historique.add(new Transaction(Transaction.Type.VENTE, action, quantite, jour));
        }
    }

    /**
     * Calcule la valeur totale du portefeuille à une date donnée.
     *
     * @param jour la date pour laquelle on veut calculer la valeur
     * @return la valeur totale du portefeuille
     */
    public double valeur(Jour jour) {
        double valeurTotale = 0.0;
        for (Map.Entry<Action, Integer> entry : actions.entrySet()) {
            Action action = entry.getKey();
            int quantite = entry.getValue();
            valeurTotale += quantite * action.valeur(jour);
        }
        return valeurTotale;
    }


    public Map<Action, Integer> getActions() {
        return new HashMap<>(actions);
    }

    public int getQuantite(Action action) {
        return actions.getOrDefault(action, 0);
    }

    public String afficherPortefeuille() {
        StringBuilder sb = new StringBuilder("Contenu du portefeuille :\n");
        for (Map.Entry<Action, Integer> entry : actions.entrySet()) {
            Action action = entry.getKey();
            int quantite = entry.getValue();
            sb.append(action.toString())
                    .append(" - Quantité : ")
                    .append(quantite)
                    .append("\n");
        }
        return sb.toString();
    }

    public List<Transaction> getHistorique() {
        return new ArrayList<>(historique); // copie défensive
    }
}
