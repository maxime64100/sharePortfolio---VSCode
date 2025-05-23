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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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


    //test d'affichage des actions
    @Test
    void testConsulterPortefeuilleActionSimple() {
        //Arrange
        Portefeuille portefeuille = new Portefeuille();
        Action action1 = new ActionSimple("GOOGLE");
        Action action2 = new ActionSimple("FACEBOOK");

        //Action
        portefeuille.acheter(action1, 10);
        portefeuille.acheter(action2, 5);

        //Assert
        assert portefeuille.getActions().size() == 2;
        assert portefeuille.getQuantite(action1) == 10;
        assert portefeuille.getQuantite(action2) == 5;

    }

    @Test
    void testConsulterPortefeuilleActionComposee() {
        //Arrange
        ActionSimple facebookStock = new ActionSimple("FB1");
        ActionSimple metaStock = new ActionSimple("META1");
        Portefeuille portefeuille = new Portefeuille();
        Map<ActionSimple, Float> proportions = new HashMap<>();

        //Action
        proportions.put(facebookStock, 0.6f);
        proportions.put(metaStock, 0.4f);
        Action actionComposee = new ActionComposee("FACEBOOK", proportions);
        portefeuille.acheter(actionComposee, 10);
        portefeuille.acheter(actionComposee, 5);

        //Assert
        assert portefeuille.getActions().containsKey(actionComposee);
        assert portefeuille.getQuantite(actionComposee) == 15;
    }

    @Test
    void testAfficherDetailsPortefeuille() {
        //Arrange
        ActionSimple google = new ActionSimple("GOOGLE");
        ActionSimple fb1 = new ActionSimple("FB1");
        Map<ActionSimple, Float> proportions = new HashMap<>();
        Portefeuille portefeuille = new Portefeuille();

        //Action
        proportions.put(fb1, 0.6f);
        proportions.put(google, 0.4f);
        ActionComposee composedFb = new ActionComposee("FACEBOOK", proportions);
        portefeuille.acheter(google, 10);
        portefeuille.acheter(composedFb, 5);
        String affichage = portefeuille.afficherPortefeuille();
        System.out.println(affichage); // utile pour visualiser pendant le dev

        //Assert
        assert affichage.contains("GOOGLE");
        assert affichage.contains("FACEBOOK");
        assert affichage.contains("Quantité : 10");
        assert affichage.contains("Quantité : 5");
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
    void testVendreUneActionSimplePossedeDoitMarcher() {
        setupActions();
        portefeuille = new Portefeuille();
        portefeuille.acheter(actionSimple1, QUANTITY_VALUE1);
        portefeuille.acheter(actionSimple2, QUANTITY_VALUE2);
        portefeuille.vendreUne(actionSimple2);
        Assertions.assertAll(
                ()->Assertions.assertEquals(QUANTITY_VALUE1, portefeuille.getActions().get(actionSimple1)),
                ()->Assertions.assertEquals(QUANTITY_VALUE1, portefeuille.getActions().get(actionSimple2))
        );
    }

    @Test
    void testVendrePlusDActionSimpleQuePossedeeDoitEchouer() {
        setupActions();
        portefeuille = new Portefeuille();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> portefeuille.vendreUne(actionSimple1));
    }

    @Test
    void testVendreActionSimpleQuantiteNonPossedeDoitEchoue() {
        setupActions();
        portefeuille = new Portefeuille();
        portefeuille.acheter(actionSimple1, QUANTITY_VALUE1);
        portefeuille.acheter(actionSimple2, QUANTITY_VALUE2);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> portefeuille.vendre(actionSimple1, QUANTITY_VALUE2));
    }

    @Test
    void testVendrePlusieursActionsSimplesNonPossedeeDoitEchoue() {
        setupActions();
        portefeuille = new Portefeuille();
        portefeuille.acheter(actionSimple1, QUANTITY_VALUE1);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> portefeuille.vendre(actionSimple2, QUANTITY_VALUE2));
    }

    @Test
    void testVendreActionSimpleQuantiteEgaleDoitSupprimerAction() {
        setupActions();
        portefeuille = new Portefeuille();
        portefeuille.acheter(actionSimple1, QUANTITY_VALUE1);
        portefeuille.acheter(actionSimple2, QUANTITY_VALUE2);
        portefeuille.vendre(actionSimple2, QUANTITY_VALUE2);
        Assertions.assertFalse(portefeuille.getActions().containsKey(actionSimple2));
    }

    @Test
    void testVendreQuantiteMaxActionSimplePossedeeDoitFonctionnerEtSupprimerAction() {
        setupActions();
        portefeuille = new Portefeuille();
        portefeuille.acheter(actionSimple1, QUANTITY_VALUE1);
        portefeuille.acheter(actionSimple2, QUANTITY_VALUE2);
        portefeuille.vendreQuantiteMax(actionSimple2);
        Assertions.assertFalse(portefeuille.getActions().containsKey(actionSimple2));
    }

    @Test
    void testVendreQuantiteMaxActionSimpleNonPossedeeDoitEchouer() {
        setupActions();
        portefeuille = new Portefeuille();
        portefeuille.acheter(actionSimple1, QUANTITY_VALUE1);
        Assertions.assertThrows(IllegalArgumentException.class, () -> portefeuille.vendreQuantiteMax(actionSimple2));
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

    @Test
    void testVendrePlusieursActionsComposees() {
        // Given un portefeuille d'action
        Portefeuille portefeuille = new Portefeuille();
        ActionSimple france2 = new ActionSimple("France 2");
        ActionSimple france3 = new ActionSimple("France 3");
        Map<ActionSimple, Float> props = Map.of(
                france2, 0.4f,
                france3, 0.6f
        );
        Action franceTelevision = new ActionComposee("France Television", props);
        portefeuille.acheter(france2, DEFAULT_QUANTITE);
        portefeuille.acheter(france3, DEFAULT_QUANTITE);
        portefeuille.acheter(franceTelevision, DEFAULT_QUANTITE);

        // When je vends une action composée
        portefeuille.vendre(franceTelevision, 2);

        // Then je possède un exemplaire de moins de cette action dans mon portefeuille
        Map<Action, Integer> actions = portefeuille.getActions();
        Assertions.assertAll(
                () -> Assertions.assertEquals(DEFAULT_QUANTITE-2, actions.get(franceTelevision)),
                () -> Assertions.assertEquals(DEFAULT_QUANTITE, actions.get(france2)),
                () -> Assertions.assertEquals(DEFAULT_QUANTITE, actions.get(france3))
        );
    }

    @Test
    void testVendrePlusieursActionsComposeesNonPossedees() {
        // Given un portefeuille d'action
        Portefeuille portefeuille = new Portefeuille();
        ActionSimple france2 = new ActionSimple("France 2");
        ActionSimple france3 = new ActionSimple("France 3");
        Map<ActionSimple, Float> props = Map.of(
                france2, 0.4f,
                france3, 0.6f
        );
        Action franceTelevision = new ActionComposee("France Television", props);
        portefeuille.acheter(france2, DEFAULT_QUANTITE);
        portefeuille.acheter(france3, DEFAULT_QUANTITE);

        // When je vends une action composée que je ne possède pas
        // Then levée d'une exception, car non possédée
        Assertions.assertThrows(IllegalArgumentException.class, () -> portefeuille.vendre(franceTelevision, 2));
    }

    @Test
    void testVendrePlusDActionsComposeesQuePossedees() {
        // Given un portefeuille d'action
        Portefeuille portefeuille = new Portefeuille();
        ActionSimple france2 = new ActionSimple("France 2");
        ActionSimple france3 = new ActionSimple("France 3");
        Map<ActionSimple, Float> props = Map.of(
                france2, 0.4f,
                france3, 0.6f
        );
        Action franceTelevision = new ActionComposee("France Television", props);
        portefeuille.acheter(france2, DEFAULT_QUANTITE);
        portefeuille.acheter(france3, DEFAULT_QUANTITE);
        portefeuille.acheter(franceTelevision, DEFAULT_QUANTITE);

        // When je vends plus d'action composée que possédées
        // Then levée d'une exception, car je ne possède pas autant d'actions que je veux vendre
        Assertions.assertThrows(IllegalArgumentException.class, () -> portefeuille.vendre(franceTelevision, DEFAULT_QUANTITE + 1));
    }

    @Test
    void testVendreAutantDActionsComposeesQuePossedeeDoitSupprimer() {
        // Given un portefeuille d'action
        Portefeuille portefeuille = new Portefeuille();
        ActionSimple france2 = new ActionSimple("France 2");
        ActionSimple france3 = new ActionSimple("France 3");
        Map<ActionSimple, Float> props = Map.of(
                france2, 0.4f,
                france3, 0.6f
        );
        Action franceTelevision = new ActionComposee("France Television", props);
        portefeuille.acheter(france2, DEFAULT_QUANTITE);
        portefeuille.acheter(france3, DEFAULT_QUANTITE);
        portefeuille.acheter(franceTelevision, DEFAULT_QUANTITE);

        // When je vends autant d'actions composées que possédées
        portefeuille.vendre(franceTelevision, DEFAULT_QUANTITE);

        // Then je ne possède plus cette action dans mon portefeuille
        Map<Action, Integer> actions = portefeuille.getActions();
        Assertions.assertAll(
                () -> Assertions.assertFalse(actions.containsKey(franceTelevision)),
                () -> Assertions.assertTrue(actions.containsKey(france2)),
                () -> Assertions.assertTrue(actions.containsKey(france3))
        );
    }

    @Test
    void testVendreQuantiteMaxActionComposeeDoitFonctionnerEtSupprimerAction() {
        // Given un portefeuille d'action
        Portefeuille portefeuille = new Portefeuille();
        ActionSimple france2 = new ActionSimple("France 2");
        ActionSimple france3 = new ActionSimple("France 3");
        Map<ActionSimple, Float> props = Map.of(
                france2, 0.4f,
                france3, 0.6f
        );
        Action franceTelevision = new ActionComposee("France Television", props);
        portefeuille.acheter(france2, DEFAULT_QUANTITE);
        portefeuille.acheter(france3, DEFAULT_QUANTITE);
        portefeuille.acheter(franceTelevision, DEFAULT_QUANTITE);

        // When je vends toutes mes actions composées franceTelevision
        portefeuille.vendreQuantiteMax(franceTelevision);

        // Then je ne possède plus cette action dans mon portefeuille
        Map<Action, Integer> actions = portefeuille.getActions();
        Assertions.assertAll(
                () -> Assertions.assertFalse(actions.containsKey(franceTelevision)),
                () -> Assertions.assertTrue(actions.containsKey(france2)),
                () -> Assertions.assertTrue(actions.containsKey(france3))
        );
    }

    @Test
    void testVendreQuantiteMaxActionComposeeNonPossederDoitEchouer() {
        // Given un portefeuille d'action
        Portefeuille portefeuille = new Portefeuille();
        ActionSimple france2 = new ActionSimple("France 2");
        ActionSimple france3 = new ActionSimple("France 3");
        Map<ActionSimple, Float> props = Map.of(
                france2, 0.4f,
                france3, 0.6f
        );
        Action franceTelevision = new ActionComposee("France Television", props);
        portefeuille.acheter(france2, DEFAULT_QUANTITE);
        portefeuille.acheter(france3, DEFAULT_QUANTITE);

        // When je vends toutes mes actions composées franceTelevision (que je ne possède pas)
        // Then levée d'une exception, car non possédée
        Assertions.assertThrows(IllegalArgumentException.class, () -> portefeuille.vendreQuantiteMax(franceTelevision));
    }
}
