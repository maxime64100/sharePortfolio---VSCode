package fr.utc.miage.shares;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionSimpleTest {
    private static final String FOO_SHARE1 = "Foo Share 1";
    private static final String FOO_SHARE2 = "Foo Share 2";
    private static final int CURRENT_YEAR = 2025;
    private static final int CURRENT_DAY = 140;
    private static final float SHARE_VALUE1 = 25.0f;
    private static final float SHARE_VALUE2 = 35.0f;
    private static final float SHARE_VALUE3 = 45.0f;

    private static ActionSimple actionSimple1;
    private static ActionSimple actionSimple2;
    private static Jour jour1 = new Jour(CURRENT_YEAR, CURRENT_DAY);
    private static Jour jour2 = new Jour(CURRENT_YEAR, CURRENT_DAY+1);
    private static Jour jour3 = new Jour(CURRENT_YEAR, CURRENT_DAY+2);


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
    public void testGetValeurWithGoodValue() {
        setupActions();
        Assertions.assertEquals(SHARE_VALUE1, actionSimple1.valeur(jour1));
    }

    @Test
    public void testVendreValeurWithGoodValue() {

    }
}