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

import java.util.Map;

public class PortefeuilleTest {

    private final Integer DEFAULT_QUANTITE = 10;


    @Test
    void testAcheter() {

    }

    @Test
    void testGetActions() {

    }

    @Test
    void testGetQuantite() {

    }

    @Test
    void testValeur() {

    }

    @Test
    void testVendre() {

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
