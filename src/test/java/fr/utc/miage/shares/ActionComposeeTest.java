/*
 * Copyright 2025 Mathys Alzuria, Tom Montbord, Colas Naudi, Mathis Lague, Maxime Fallek.
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

public class ActionComposeeTest {

    private static final String NAME_ACTION_FRANCE2 = "France2";
    private static final String NAME_ACTION_FRANCE3 = "France3";
    private static final String NAME_ACTION_TISSEO = "Tisseo";
    private static final String NAME_ACTION_FRANCETELEVISION = "FranceTelevision";
    private static final int CURRENT_YEAR = 2025;
    private static final int CURRENT_DAY = 140;
    private static final float SHARE_VALUE1 = 25.0f;
    private static final float SHARE_VALUE2 = 35.0f;
    private static final float SHARE_VALUE3 = 45.0f;

    private static final Map<ActionSimple, Float> PROPS_SHARE_VALUES = new HashMap<>();
    private static final Float PROP_ACTION_FRANCE2 = 0.4f;
    private static final Float PROP_ACTION_FRANCE3 = 0.6f;

    private static ActionComposee actionFranceTelevision;
    private static final Jour jour1 = new Jour(CURRENT_YEAR, CURRENT_DAY);
    private static final Jour jour2 = new Jour(CURRENT_YEAR, CURRENT_DAY+1);
    private static final Jour jour3 = new Jour(CURRENT_YEAR, CURRENT_DAY+2);

    private void setupActions() {
        ActionSimple actionFrance2 = new ActionSimple(NAME_ACTION_FRANCE2);
        ActionSimple actionFrance3 = new ActionSimple(NAME_ACTION_FRANCE3);

        PROPS_SHARE_VALUES.put(actionFrance2, PROP_ACTION_FRANCE2);
        PROPS_SHARE_VALUES.put(actionFrance3, PROP_ACTION_FRANCE3);
        actionFranceTelevision = new ActionComposee(NAME_ACTION_FRANCETELEVISION, PROPS_SHARE_VALUES);

        actionFrance2.enrgCours(jour1, SHARE_VALUE1);
        actionFrance2.enrgCours(jour2, SHARE_VALUE2);
        actionFrance2.enrgCours(jour3, SHARE_VALUE3);

        actionFrance3.enrgCours(jour1, SHARE_VALUE1);
        actionFrance3.enrgCours(jour2, SHARE_VALUE2);
        actionFrance3.enrgCours(jour3, SHARE_VALUE3);
    }

    @Test
    void constructeurNeDoitPasLeverDErreur() {
        Assertions.assertDoesNotThrow(() -> new ActionComposee(NAME_ACTION_FRANCETELEVISION, PROPS_SHARE_VALUES));
    }

    @Test
    void testGetValeurWithGoodValue() {
        setupActions();
        Assertions.assertAll(
                () -> Assertions.assertEquals(SHARE_VALUE1 * PROP_ACTION_FRANCE2 + SHARE_VALUE1 * PROP_ACTION_FRANCE3, actionFranceTelevision.valeur(jour1)),
                () -> Assertions.assertEquals(SHARE_VALUE2 * PROP_ACTION_FRANCE2 + SHARE_VALUE2 * PROP_ACTION_FRANCE3, actionFranceTelevision.valeur(jour2)),
                () -> Assertions.assertEquals(SHARE_VALUE3 * PROP_ACTION_FRANCE2 + SHARE_VALUE3 * PROP_ACTION_FRANCE3, actionFranceTelevision.valeur(jour3))
        );
    }
}
