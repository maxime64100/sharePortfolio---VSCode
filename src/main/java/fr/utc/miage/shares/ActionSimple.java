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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Allows the creation of simple Action objects.
 *
 * @author David Navarre &lt;David.Navarre at irit.fr&gt;
 */
public class ActionSimple extends Action {

    private static final int DEFAULT_ACTION_VALUE = 0;

    // attribut lien
    private final Map<Jour, Float> mapCours;

    // constructeur
    public ActionSimple(final String libelle) {
        // Action simple initialisée comme 1 action
        super(libelle);
        // init spécifique
        this.mapCours = new HashMap<>();
    }

    // enrg possible si pas de cours pour ce jour
    public void enrgCours(final Jour j, final float v) {
        if (!this.mapCours.containsKey(j)) {
            this.mapCours.put(j, v);
        }
    }

    @Override
    public float valeur(final Jour j) {
        Jour currentDay = j;

        // Loop until the market is open
        while (!isMarketOpen(currentDay)) {
            LocalDate previousDate = currentDay.toDate().minusDays(1);
            currentDay = new Jour(previousDate.getYear(), previousDate.getDayOfYear());
        }

        // Return the value for the first open market day
        if (this.mapCours.containsKey(currentDay)) {
            return this.mapCours.get(currentDay);
        } else {
            return DEFAULT_ACTION_VALUE;
        }
    }

    /**
     * Check if the market is open on a given day.
     * @param j: the day to check
     * @return true if the market is open, false otherwise
     */
    private boolean isMarketOpen(Jour j) {
        LocalDate date = j.toDate();

        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return false;
        }

        Set<LocalDate> holidays = Set.of(
                LocalDate.of(j.getYear(), 1, 1),  // New Year's Day
                LocalDate.of(j.getYear(), 12, 25), // Christmas
                LocalDate.of(j.getYear(), 7, 14) // Bastille Day
        );

        return !holidays.contains(date);
    }
}
