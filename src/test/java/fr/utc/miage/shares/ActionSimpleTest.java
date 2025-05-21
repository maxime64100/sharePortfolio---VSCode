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

//import static org.junit.jupiter.api.Assertions.*;

class ActionSimpleTest {

    private static final ActionSimple action = new ActionSimple("APPL");
  private static final String FOO_SHARE1 = "Foo Share 1";
    private static final String FOO_SHARE2 = "Foo Share 2";
    private static final int CURRENT_YEAR = 2025;
    private static final int CURRENT_DAY = 140;
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
  
  @Test
    public void testConstructorWithGoodValuesShouldWork() {
        Assertions.assertDoesNotThrow(() -> new ActionSimple(FOO_SHARE1));
    }

    @Test
    public void testConstructorWithBadValuesShouldFail() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ActionSimple(null));
    }

    @Test
    public void testEnrgCoursAlreadySet() {
        setupActions();
        actionSimple1.enrgCours(jour1, SHARE_VALUE2);
        Assertions.assertNotEquals(SHARE_VALUE2, actionSimple1.valeur(jour1));
    }

    @Test
    public void testGetValeurWithGoodValue() {
        setupActions();
        Assertions.assertAll(
                ()-> Assertions.assertEquals(SHARE_VALUE1, actionSimple1.valeur(jour1)),
                ()-> Assertions.assertEquals(SHARE_VALUE2, actionSimple2.valeur(jour2))
        );
    }

    @Test
    public void testGetValeurWithBadValue() {
        setupActions();
        Assertions.assertEquals(ActionSimple.DEFAULT_ACTION_VALUE, actionSimple2.valeur(jour3));
    }
}

