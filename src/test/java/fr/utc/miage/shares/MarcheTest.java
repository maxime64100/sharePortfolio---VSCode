package fr.utc.miage.shares;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

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
        marche.ajouterAction(ACTION1);
        marche.ajouterAction(ACTION2);
        marche.ajouterAction(ACTION3);
        marche.ajouterAction(ACTION4);

        // Act
        List<Action> sortedActions = marche.listerActionsParNom();

        // Assert
        assertEquals(List.of(ACTION2, ACTION4, ACTION3, ACTION1), sortedActions);
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

}