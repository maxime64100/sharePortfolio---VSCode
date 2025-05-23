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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MarcheTest {

    private static final String ID_MARCHE = "EUNXT";
    private static final String NOM_MARCHE = "Euronext";
    private static final String PAYS = "France";
    private static final String DEVISE = "EUR";
    private static final ZoneId FUSEAU = ZoneId.of("Europe/Paris");
    private static final LocalTime OUVERTURE = LocalTime.of(9, 0);
    private static final LocalTime FERMETURE = LocalTime.of(17, 30);

    private static final Action ACTION = new ActionSimple("APPL");
    private static final Action ACTION1 = new ActionSimple("AHLP");
    private static final Action ACTION2 = new ActionSimple("MICR");
    private static final Action ACTION3 = new ActionSimple("GOOGL");
    private static final Action ACTION4 = new ActionSimple("AMZN");



    @Test
    void testEstOuvert() {
        // Arrange
        Marche marche = new Marche("EUNXT", "Euronext", "France", "EUR", ZoneId.of("Europe/Paris"),
                LocalTime.of(9, 0), LocalTime.of(17, 30));

        // Act & Assert
        assertTrue(marche.estOuvert(LocalTime.of(10, 0))); // During open hours
        assertFalse(marche.estOuvert(LocalTime.of(8, 59))); // Before opening
        assertFalse(marche.estOuvert(LocalTime.of(17, 31))); // After closing
    }

    @Test
    void testEstOuvertAvantOuverture() {
        // Arrange
        Marche marche = new Marche(ID_MARCHE, NOM_MARCHE, PAYS, DEVISE, FUSEAU, OUVERTURE, FERMETURE);

        // Act & Assert
        assertFalse(marche.estOuvert(LocalTime.of(8, 59))); // Before opening time
    }

    @Test
    void testEstOuvertApresFermeture() {
        // Arrange
        Marche marche = new Marche(ID_MARCHE, NOM_MARCHE, PAYS, DEVISE, FUSEAU, OUVERTURE, FERMETURE);

        // Act & Assert
        assertFalse(marche.estOuvert(LocalTime.of(17, 31))); // After closing time
    }

    @Test
    void testAjouterAction() {
        // Arrange
        Marche marche = new Marche(ID_MARCHE, NOM_MARCHE, PAYS, DEVISE, FUSEAU, OUVERTURE, FERMETURE);

        // Act
        marche.ajouterAction(ACTION);

        // Assert
        assertTrue(marche.listerActionsParNom().contains(ACTION));
    }

    @Test
    void testRetirerAction() {
        // Arrange
        Marche marche = new Marche(ID_MARCHE, NOM_MARCHE, PAYS, DEVISE, FUSEAU, OUVERTURE, FERMETURE);
        marche.ajouterAction(ACTION);

        // Act
        marche.retirerAction(ACTION);

        // Assert
        assertFalse(marche.listerActionsParNom().contains(ACTION));
    }

    @Test
    void testToStringFormat() {
        // Arrange
        Marche marche = new Marche(ID_MARCHE, NOM_MARCHE, PAYS, DEVISE, FUSEAU, OUVERTURE, FERMETURE);

        // Act
        String result = marche.toString();

        // Assert
        assertEquals("Euronext (France, EUR)", result);
    }

    @Test
    void testListerActionsParNom() {
        // Arrange
        Marche marche = new Marche(ID_MARCHE, NOM_MARCHE, PAYS, DEVISE, FUSEAU, OUVERTURE, FERMETURE);
        marche.ajouterAction(ACTION1); // AHLP
        marche.ajouterAction(ACTION2); // MICR
        marche.ajouterAction(ACTION3); // GOOGL
        marche.ajouterAction(ACTION4); // AMZN

        // Act
        List<Action> sortedActions = marche.listerActionsParNom();

        // Assert
        assertEquals(List.of(ACTION1, ACTION4, ACTION3, ACTION2), sortedActions); // Alphabetical order
    }

    @Test
    void testAjouterActionDuplicate() {
        // Arrange
        Marche marche = new Marche(ID_MARCHE, NOM_MARCHE, PAYS, DEVISE, FUSEAU, OUVERTURE, FERMETURE);

        // Act
        marche.ajouterAction(ACTION);
        marche.ajouterAction(ACTION); // Add the same action again

        // Assert
        assertEquals(1, marche.listerActionsParNom().size());
    }

    @Test
    void testListerActionsParNomEmpty() {
        // Arrange
        Marche marche = new Marche(ID_MARCHE, NOM_MARCHE, PAYS, DEVISE, FUSEAU, OUVERTURE, FERMETURE);

        // Act
        List<Action> actions = marche.listerActionsParNom();

        // Assert
        assertTrue(actions.isEmpty());
    }

    @Test
    void testEstOuvertBoundaryTimes() {
        // Arrange
        Marche marche = new Marche(ID_MARCHE, NOM_MARCHE, PAYS, DEVISE, FUSEAU, OUVERTURE, FERMETURE);

        // Act & Assert
        assertTrue(marche.estOuvert(OUVERTURE)); // Exactly at opening time
        assertTrue(marche.estOuvert(FERMETURE)); // Exactly at closing time
    }

    @Test
    void testEstOuvertNullTime() {
        // Arrange
        Marche marche = new Marche(ID_MARCHE, NOM_MARCHE, PAYS, DEVISE, FUSEAU, OUVERTURE, FERMETURE);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> marche.estOuvert(null));
    }

    @Test
    void testRetirerActionNonExistent() {
        // Arrange
        Marche marche = new Marche(ID_MARCHE, NOM_MARCHE, PAYS, DEVISE, FUSEAU, OUVERTURE, FERMETURE);

        // Act
        marche.retirerAction(ACTION);

        // Assert
        assertTrue(marche.listerActionsParNom().isEmpty());
    }

    @Test
    void testVerifierSiActionsDansActionComposeeSontSurLaPlaceDeMarche() {
    // Arrange
    Marche marche = new Marche(ID_MARCHE, NOM_MARCHE, PAYS, DEVISE, FUSEAU, OUVERTURE, FERMETURE);
    
    ActionSimple a1 = new ActionSimple("A1");
    ActionSimple a2 = new ActionSimple("A2");
    ActionSimple a3 = new ActionSimple("A3");

    marche.ajouterAction(a1);
    marche.ajouterAction(a2);

    // Crée une ActionComposee avec A1 et A2 (présentes)
    Map<ActionSimple, Float> propsOk = new HashMap<>();
    propsOk.put(a1, 0.5f);
    propsOk.put(a2, 0.5f);
    ActionComposee actionComposeeOk = new ActionComposee("COMPOSEE_OK", propsOk);

    // Crée une autre ActionComposee avec une action absente (A3)
    Map<ActionSimple, Float> propsKo = new HashMap<>();
    propsKo.put(a1, 0.5f);
    propsKo.put(a3, 0.5f);
    ActionComposee actionComposeeKo = new ActionComposee("COMPOSEE_KO", propsKo);

    // Act & Assert
    // Toutes les actions de actionComposeeOk sont bien dans le marché
    boolean toutesPresentesOk = actionComposeeOk.getMapProportion().keySet().stream()
        .allMatch(marche::contient);
    assertTrue(toutesPresentesOk);

    // Une des actions de actionComposeeKo est absente du marché
    boolean toutesPresentesKo = actionComposeeKo.getMapProportion().keySet().stream()
        .allMatch(marche::contient);
    assertFalse(toutesPresentesKo);
}


}