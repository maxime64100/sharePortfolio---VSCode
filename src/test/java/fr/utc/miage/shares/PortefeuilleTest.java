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

import java.util.HashMap;


public class PortefeuilleTest {

    @Test
    void testAcheterActionComposeeNonDetenue() {
        Portefeuille portefeuille = new Portefeuille();
        Action action = new ActionComposee("Action composée", new HashMap<>());
        portefeuille.acheter(action, 10);
        assert (portefeuille.getQuantite(action) == 10);
    }


    @Test
    void testAcheterActionComposeeDetenue() {
        Portefeuille portefeuille = new Portefeuille();
        Action action1 = new ActionComposee("Action composée 1", new HashMap<>());
        Action action2 = new ActionComposee("Action composée 2", new HashMap<>());
        portefeuille.acheter(action1, 10);
        portefeuille.acheter(action2, 20);
        portefeuille.acheter(action1, 30);

        assert (portefeuille.getQuantite(action1) == 40);
        assert (portefeuille.getQuantite(action2) == 20);
    }
}
