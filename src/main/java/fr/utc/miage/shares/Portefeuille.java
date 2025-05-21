/*
 * Copyright 2025 David Navarre &lt;David.Navarre at irit.fr&gt;.
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

import java.util.HashMap;
import java.util.Map;

public class Portefeuille {
    private Map<Action, Integer> actions;

    public Portefeuille() {
        actions = new HashMap<>();
    }

    public void acheter(Action action, int quantite) {
        if (quantite <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive.");
        }
        actions.put(action, actions.getOrDefault(action, 0) + quantite);
    }

    /**
     * Surcharge de la méthode vendre, par défault la quantité d'action vendu est à 1
     * @param action
     */
    public void vendreUne(Action action) {
        vendre(action, 1);
    }

    /**
     * Supprime l'action du portefeuille si l'action est possédé en quantité suffisante dans le portefeuille.
     * @param action
     * @param quantite
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
            actions.put(action, quantiteActuelle - quantite);
        }
    }

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
}
