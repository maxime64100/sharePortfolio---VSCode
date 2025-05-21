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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PortefeuilleTest {

    @Test
    void testAcheterActionComposeeNonDetenue() {
        ActionSimple france2 = new ActionSimple("France 2");
        ActionSimple france3 = new ActionSimple("France 3");
        ActionSimple france5 = new ActionSimple("France 5");

        Map<ActionSimple, Double> composition = new HashMap<>();
        composition.put(france2, 0.35);
        composition.put(france3, 0.50);
        composition.put(france5, 0.15);

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

        Map<ActionSimple, Double> composition = new HashMap<>();
        composition.put(france2, 0.35);
        composition.put(france3, 0.50);
        composition.put(france5, 0.15);

        ActionComposee franceTelevision = new ActionComposee("France télévision", composition);

        Portefeuille portefeuille = new Portefeuille();
        portefeuille.acheter(franceTelevision, 2);
        portefeuille.acheter(franceTelevision, 3);

        assertEquals(5, portefeuille.getQuantite(franceTelevision));
    }
}
