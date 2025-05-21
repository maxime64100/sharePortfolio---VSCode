/*
 * Copyright 2024 David Navarre &lt;David.Navarre at irit.fr&gt;.
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

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


import java.util.Map;

public class PortefeuilleTest {

    private final Integer DEFAULT_QUANTITE = 10;

    private static final String FOO_SHARE1 = "Foo Share 1";
    private static final String FOO_SHARE2 = "Foo Share 2";
    private static final int CURRENT_YEAR = 2025;
    private static final int CURRENT_DAY = 140;
    private static final float SHARE_VALUE1 = 25.0f;
    private static final float SHARE_VALUE2 = 35.0f;
    private static final float SHARE_VALUE3 = 45.0f;
    private static final int QUANTITY_VALUE1 = 1;
    private static final int QUANTITY_VALUE2 = 2;

    private static ActionSimple actionSimple1;
    private static ActionSimple actionSimple2;
    private static final Jour jour1 = new Jour(CURRENT_YEAR, CURRENT_DAY);
    private static final Jour jour2 = new Jour(CURRENT_YEAR, CURRENT_DAY+1);
    private static final Jour jour3 = new Jour(CURRENT_YEAR, CURRENT_DAY+2);

    private static Portefeuille portefeuille;


    private void setupActions() {
        actionSimple1 = new ActionSimple(FOO_SHARE1);
        actionSimple2 = new ActionSimple(FOO_SHARE2);

        actionSimple1.enrgCours(jour1, SHARE_VALUE1);
        actionSimple1.enrgCours(jour2, SHARE_VALUE2);
        actionSimple1.enrgCours(jour3, SHARE_VALUE3);

        actionSimple2.enrgCours(jour1, SHARE_VALUE1);
        actionSimple2.enrgCours(jour2, SHARE_VALUE2);
        actionSimple2.enrgCours(jour3, SHARE_VALUE3);
    }

    @Test
    void testAcheterActionSimpleNonDetennue() {
        Action action = new ActionSimple("Action1");

        Portefeuille portefeuille = new Portefeuille();
        portefeuille.acheter(action, 10);

        assert portefeuille.getActions().containsKey(action);
        assert portefeuille.getQuantite(action) == 10;
    }

    @Test
    void testAcheterActionSimpleDetennue() {
        Action action = new ActionSimple("Action1");
        Action action2 = new ActionSimple("Action2");

        Portefeuille portefeuille = new Portefeuille();
        portefeuille.acheter(action, 10);
        portefeuille.acheter(action2, 5);
        portefeuille.acheter(action, 5);

        assert portefeuille.getActions().containsKey(action);
        assert portefeuille.getQuantite(action) == 15;
        assert portefeuille.getActions().containsKey(action2);
        assert portefeuille.getQuantite(action2) == 5;
    }

    @Test
    void testAcheterActionSimpleQuantiteNulle() {
        Action action = new ActionSimple("Action1");
        Portefeuille portefeuille = new Portefeuille();

        try {
            portefeuille.acheter(action, 0);
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("La quantité doit être positive.");
        }
    }

    @Test
    void testAcheterActionSimpleQuantiteNegative() {
        Action action = new ActionSimple("Action1");
        Portefeuille portefeuille = new Portefeuille();

        try {
            portefeuille.acheter(action, -5);
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("La quantité doit être positive.");
        }
    }

    @Test
    void testAcheterActionComposeeNonDetenue() {
        ActionSimple france2 = new ActionSimple("France 2");
        ActionSimple france3 = new ActionSimple("France 3");
        ActionSimple france5 = new ActionSimple("France 5");

        Map<ActionSimple, Float> composition = new HashMap<>();
        composition.put(france2, 0.35f);
        composition.put(france3, 0.50f);
        composition.put(france5, 0.15f);

        ActionComposee franceTelevision = new ActionComposee("France télévision", composition);

        Portefeuille portefeuille = new Portefeuille();
        portefeuille.acheter(franceTelevision, 2);

        assertEquals(2, portefeuille.getQuantite(franceTelevision));
    }

    @Test
    void testAcheterActionComposeeDetenue() {
        ActionSimple france2 = new ActionSimple("France 2");
        ActionSimple france3 = new ActionSimple("France 3");
        ActionSimple france5 = new ActionSimple("France 5");

        Map<ActionSimple, Float> composition = new HashMap<>();
        composition.put(france2, 0.35f);
        composition.put(france3, 0.50f);
        composition.put(france5, 0.15f);

        ActionComposee franceTelevision = new ActionComposee("France télévision", composition);

        Portefeuille portefeuille = new Portefeuille();
        portefeuille.acheter(franceTelevision, 2);
        portefeuille.acheter(franceTelevision, 3);

        assertEquals(5, portefeuille.getQuantite(franceTelevision));
    }

    @Test
    void testAcheterActionAvecQuantiteNulle() {
        ActionSimple france2 = new ActionSimple("France 2");
        Map<ActionSimple, Float> composition = new HashMap<>();
        composition.put(france2, 1.0f);
        ActionComposee franceTelevision = new ActionComposee("France télévision", composition);

        Portefeuille portefeuille = new Portefeuille();

        try {
            portefeuille.acheter(franceTelevision, 0);
            fail("Une exception aurait dû être levée pour une quantité nulle.");
        } catch (IllegalArgumentException e) {
            assertEquals("La quantité doit être positive.", e.getMessage());
        }
    }

    @Test
    void testVendreActionSimplePossedeDoitMarcher() {
        setupActions();
        portefeuille = new Portefeuille();
        portefeuille.acheter(actionSimple1, QUANTITY_VALUE1);
        portefeuille.acheter(actionSimple2, QUANTITY_VALUE2);
        portefeuille.vendreUne(actionSimple2);
        Assertions.assertAll(
                ()->Assertions.assertEquals(portefeuille.getActions().get(actionSimple1), QUANTITY_VALUE1),
                ()->Assertions.assertEquals(portefeuille.getActions().get(actionSimple2), QUANTITY_VALUE1)
        );
    }

    @Test
    void testVendreActionSimpeNonPossedeDoitEchoue() {
        setupActions();
        portefeuille = new Portefeuille();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> portefeuille.vendreUne(actionSimple1));
    }

    @Test
    void testVendreUneActionComposee() {
        //Given un portefeuille d'action
        Portefeuille portefeuille = new Portefeuille();
        ActionSimple france2 = new ActionSimple("France 2");
        ActionSimple france3 = new ActionSimple("France 3");
        Action tisseo = new ActionSimple("Tisseo");
        Map<ActionSimple, Float> props = Map.of(
                france2, 0.4f,
                france3, 0.6f
        );
        Action franceTelevision = new ActionComposee("France Television", props);
        portefeuille.acheter(france2, DEFAULT_QUANTITE);
        portefeuille.acheter(france3, DEFAULT_QUANTITE);
        portefeuille.acheter(tisseo, DEFAULT_QUANTITE);
        portefeuille.acheter(franceTelevision, DEFAULT_QUANTITE);

        //When je vends une action composée
        portefeuille.vendreUne(franceTelevision);

        // Then je possède un exemplaire de moins de cette action dans mon portefeuille
        Map<Action, Integer> actions = portefeuille.getActions();
        Assertions.assertAll(
                () -> Assertions.assertEquals(DEFAULT_QUANTITE-1, actions.get(franceTelevision)),
                () -> Assertions.assertEquals(DEFAULT_QUANTITE, actions.get(france2)),
                () -> Assertions.assertEquals(DEFAULT_QUANTITE, actions.get(france3)),
                () -> Assertions.assertEquals(DEFAULT_QUANTITE, actions.get(tisseo))
        );
    }

    @Test
    void testVendreUneActionComposeeNonPossedee() {
        //Given un portefeuille d'action
        Portefeuille portefeuille = new Portefeuille();
        ActionSimple france2 = new ActionSimple("France 2");
        ActionSimple france3 = new ActionSimple("France 3");
        Action tisseo = new ActionSimple("Tisseo");
        Map<ActionSimple, Float> props = Map.of(
                france2, 0.4f,
                france3, 0.6f
        );
        Action franceTelevision = new ActionComposee("France Television", props);
        portefeuille.acheter(france2, DEFAULT_QUANTITE);
        portefeuille.acheter(france3, DEFAULT_QUANTITE);
        portefeuille.acheter(tisseo, DEFAULT_QUANTITE);

        // When je vends une action composée
        // Then levée d'une exception, car non possédée
        Assertions.assertThrows(IllegalArgumentException.class, () -> portefeuille.vendreUne(franceTelevision));
    }
}
