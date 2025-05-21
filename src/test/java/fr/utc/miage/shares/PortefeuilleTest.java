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

import org.junit.jupiter.api.Test;

public class PortefeuilleTest {
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
}
