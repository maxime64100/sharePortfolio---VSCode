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

import java.util.HashMap;
import java.util.Map;

/**
 * Objet ActionComposee : permet de créer des actions composées à partir
 *
 * @author Mathys Alzuria, Tom Montbord, Colas Naudi, Mathis Lague, Maxime Fallek
 */
public class ActionComposee extends Action {

    /**
     * Map des proportions des actions simples qui composent l'action.
     */
    private Map<ActionSimple, Float> mapProportion = new HashMap<>();

    /**
     * Cours de l'action : valeur pour un jour donné.
     */
    private final Map<Jour, Float> mapCours;

    /**
     * Constructeur de la classe ActionComposee.
     *
     * @param libelle le libellé de l'action
     * @param mapProportion la map des proportions des actions simples qui composent l'action
     */
    public ActionComposee(final String libelle, Map<ActionSimple, Float> mapProportion) {
        // Action simple initialisée comme 1 action
        super(libelle);
        // init spécifique
        this.mapProportion = mapProportion;
        this.mapCours = new HashMap<>();
    }

    /**
     * Récupération de la valeur de l'action pour un jour donné.
     *
     * @param j le jour donné
     * @return la valeur de l'action
     */
    @Override
    public float valeur(final Jour j) {
        float valeurActionComposee = 0;
        for (Map.Entry<ActionSimple, Float> entry : mapProportion.entrySet()) {
            ActionSimple actionSimple = entry.getKey();
            Float proportion = entry.getValue();
            valeurActionComposee += actionSimple.valeur(j) * proportion;
        }
        return valeurActionComposee;
    }
}
