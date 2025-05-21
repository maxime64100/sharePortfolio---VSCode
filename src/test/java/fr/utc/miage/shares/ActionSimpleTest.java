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
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActionSimpleTest {

    private static final ActionSimple action = new ActionSimple("APPL");
    private static final String FOO_SHARE1 = "Foo Share 1";
    private static final String FOO_SHARE2 = "Foo Share 2";
    private static final int LAST_YEAR = 2024;
    private static final int CURRENT_YEAR = 2025;
    private static final int CURRENT_DAY = 140;
    private static final int LAST_DAY = 366;
    private static final int FIRST_DAY = 1;
    private static final float SHARE_VALUE1 = 25.0f;
    private static final float SHARE_VALUE2 = 35.0f;
    private static final float SHARE_VALUE3 = 45.0f;

    private static ActionSimple actionSimple1;
    private static ActionSimple actionSimple2;
    private static final Jour jour1 = new Jour(CURRENT_YEAR, CURRENT_DAY);
    private static final Jour jour2 = new Jour(CURRENT_YEAR, CURRENT_DAY+1);
    private static final Jour jour3 = new Jour(CURRENT_YEAR, CURRENT_DAY+2);

    private void setupActions() {
        actionSimple1 = new ActionSimple(FOO_SHARE1);
        actionSimple2 = new ActionSimple(FOO_SHARE2);

        actionSimple1.enrgCours(jour1, SHARE_VALUE1);
        actionSimple1.enrgCours(jour2, SHARE_VALUE2);
        actionSimple1.enrgCours(jour3, SHARE_VALUE3);

        actionSimple2.enrgCours(jour1, SHARE_VALUE1);
        actionSimple2.enrgCours(jour2, SHARE_VALUE2);
    }

    @Test
    void valeurActionSimpleDatePrecise() {
        // Arrange
        action.enrgCours(new Jour(CURRENT_YEAR, CURRENT_DAY), SHARE_VALUE1);

        // Assert
        Assertions.assertEquals(SHARE_VALUE1, action.valeur(new Jour(CURRENT_YEAR, CURRENT_DAY)));
    }

    @Test
    void valeurActionSimpleJourFerieMarcheFerme() {
        // Arrange
        action.enrgCours(new Jour(LAST_YEAR, LAST_DAY), SHARE_VALUE1); // December 31, 2024 (leap year)
        action.enrgCours(new Jour(CURRENT_YEAR, FIRST_DAY), ActionSimple.DEFAULT_ACTION_VALUE);   // January 1, 2025

        // Assert
        Assertions.assertEquals(SHARE_VALUE1, action.valeur(new Jour(CURRENT_YEAR, FIRST_DAY)));
    }

    @Test
    void valeurActionSimpleDateFutureSansValeurDisponible() {
        // Arrange
        action.enrgCours(new Jour(CURRENT_YEAR, CURRENT_DAY), SHARE_VALUE1);

        // Assert
        Assertions.assertEquals(ActionSimple.DEFAULT_ACTION_VALUE, action.valeur(new Jour(LAST_YEAR, FIRST_DAY)));
    }

    @Test
    void testConstructorWithGoodValuesShouldWork() {
        Assertions.assertDoesNotThrow(() -> new ActionSimple(FOO_SHARE1));
    }

    @Test
    void testConstructorWithBadValuesShouldFail() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ActionSimple(null));
    }

    @Test
    void testEnrgCoursAlreadySet() {
        setupActions();
        actionSimple1.enrgCours(jour1, SHARE_VALUE2);
        Assertions.assertNotEquals(SHARE_VALUE2, actionSimple1.valeur(jour1));
    }

    @Test
    void testGetValeurWithGoodValue() {
        setupActions();
        Assertions.assertAll(
                ()-> assertEquals(SHARE_VALUE1, actionSimple1.valeur(jour1)),
                ()-> assertEquals(SHARE_VALUE2, actionSimple2.valeur(jour2))
        );
    }

    @Test
    void testGetValeurWithBadValue() {
        setupActions();
        assertEquals(ActionSimple.DEFAULT_ACTION_VALUE, actionSimple2.valeur(jour3));
    }

}

