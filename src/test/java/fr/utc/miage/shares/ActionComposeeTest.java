package fr.utc.miage.shares;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ActionComposeeTest {

    private static final String FRANCE2 = "France2";
    private static final String FRANCE3 = "France3";
    private static final String TISSEO = "Tisseo";
    private static final String FRANCETELEVISION = "FranceTelevision";
    private static final int CURRENT_YEAR = 2025;
    private static final int CURRENT_DAY = 140;
    private static final float SHARE_VALUE1 = 25.0f;
    private static final float SHARE_VALUE2 = 35.0f;
    private static final float SHARE_VALUE3 = 45.0f;

    private static final Map<ActionSimple, Float> PROPS_SHARE_VALUES = new HashMap<>();
    private static final Float PROP_FRANCE2 = 0.4f;
    private static final Float PROP_FRANCE3 = 0.6f;

    private static ActionSimple france2;
    private static ActionSimple france3;
    private static ActionSimple tisseo;
    private static ActionComposee franceTelevision;
    private static final Jour jour1 = new Jour(CURRENT_YEAR, CURRENT_DAY);
    private static final Jour jour2 = new Jour(CURRENT_YEAR, CURRENT_DAY+1);
    private static final Jour jour3 = new Jour(CURRENT_YEAR, CURRENT_DAY+2);


    private void setupActions() {
        france2 = new ActionSimple(FRANCE2);
        france3 = new ActionSimple(FRANCE3);
        tisseo = new ActionSimple(TISSEO);

        PROPS_SHARE_VALUES.put(france2, PROP_FRANCE2);
        PROPS_SHARE_VALUES.put(france3, PROP_FRANCE3);
        franceTelevision = new ActionComposee(FRANCETELEVISION, PROPS_SHARE_VALUES);

        france2.enrgCours(jour1, SHARE_VALUE1);
        france2.enrgCours(jour2, SHARE_VALUE2);
        france2.enrgCours(jour3, SHARE_VALUE3);

        france3.enrgCours(jour1, SHARE_VALUE1);
        france3.enrgCours(jour2, SHARE_VALUE2);
        france3.enrgCours(jour3, SHARE_VALUE3);

        tisseo.enrgCours(jour1, SHARE_VALUE1);
        tisseo.enrgCours(jour2, SHARE_VALUE2);
        tisseo.enrgCours(jour3, SHARE_VALUE3);
    }

    @Test
    void constructeurNeDoitPasLeverDErreur() {
        Assertions.assertDoesNotThrow(() -> new ActionComposee(FRANCETELEVISION, PROPS_SHARE_VALUES));
    }

    @Test
    void testGetValeurWithGoodValue() {
        setupActions();
        Assertions.assertAll(
                () -> Assertions.assertEquals(SHARE_VALUE1 * PROP_FRANCE2 + SHARE_VALUE1 * PROP_FRANCE3, franceTelevision.valeur(jour1)),
                () -> Assertions.assertEquals(SHARE_VALUE2 * PROP_FRANCE2 + SHARE_VALUE2 * PROP_FRANCE3, franceTelevision.valeur(jour2)),
                () -> Assertions.assertEquals(SHARE_VALUE3 * PROP_FRANCE2 + SHARE_VALUE3 * PROP_FRANCE3, franceTelevision.valeur(jour3))
        );
    }
}
