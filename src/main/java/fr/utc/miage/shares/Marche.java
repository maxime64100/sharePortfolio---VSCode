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

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Marche {

    private String id; // UUID ou code unique
    private String nom; // ex : "Euronext"
    private String pays; // ex : "France"
    private String devise; // ex : "EUR"
    private ZoneId fuseauHoraire; // ex : ZoneId.of("Europe/Paris")
    private LocalTime heureOuverture;
    private LocalTime heureFermeture;
    private Set<Action> actions; // ou List si ordre important

    public Marche(String id, String nom, String pays, String devise, ZoneId fuseauHoraire,
                  LocalTime heureOuverture, LocalTime heureFermeture) {
        this.id = id;
        this.nom = nom;
        this.pays = pays;
        this.devise = devise;
        this.fuseauHoraire = fuseauHoraire;
        this.heureOuverture = heureOuverture;
        this.heureFermeture = heureFermeture;
        this.actions = new HashSet<>();
    }


    public boolean estOuvert(LocalTime maintenant) {
        return !maintenant.isBefore(heureOuverture) && !maintenant.isAfter(heureFermeture);
    }

    public void ajouterAction(Action action) {
        actions.add(action);
    }

    public void retirerAction(Action action) {
        actions.remove(action);
    }

    public List<Action> listerActionsParNom() {
        return actions.stream()
                .sorted(Comparator.comparing(Action::getLibelle))
                .collect(Collectors.toList());
    }

    public boolean contient(Action action) {
        return actions.contains(action);
    }
    



    @Override
    public String toString() {
        return nom + " (" + pays + ", " + devise + ")";
    }

}
