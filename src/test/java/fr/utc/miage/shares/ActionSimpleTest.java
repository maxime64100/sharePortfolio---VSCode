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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionSimpleTest {

    private static final ActionSimple action = new ActionSimple("APPL");

    @Test
    void valeurActionSimpleDatePrecise() {
        // Arrange
        action.enrgCours(new Jour(2023, 122), 150.0f);

        // Assert
        assertEquals(150.0f, action.valeur(new Jour(2023, 122)));
    }

    @Test
    void valeurActionSimpleJourFerieMarcheFerme() {
        // Arrange
        action.enrgCours(new Jour(2024, 366), 200.0f); // December 31, 2024 (leap year)
        action.enrgCours(new Jour(2025, 1), 0);   // January 1, 2025

        // Assert
        assertEquals(200.0f, action.valeur(new Jour(2025, 1)));
    }

    @Test
    void valeurActionSimpleDateFutureSansValeurDisponible() {
        // Arrange
        action.enrgCours(new Jour(2023, 122), 150.0f);

        // Assert
        assertEquals(0.0f, action.valeur(new Jour(2024, 1)));
    }



}