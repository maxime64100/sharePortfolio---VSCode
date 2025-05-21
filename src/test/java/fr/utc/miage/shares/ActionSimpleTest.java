package fr.utc.miage.shares;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionSimpleTest {

    private static final ActionSimple action = new ActionSimple("APPL");

    @Test
    void valeurActionSimpleDatePrecise() {
        // Arrange
        action.enrgCours(new Jour(2023, 122), 150.0f);

        // Assert
        assertEquals(150.0f, action.valeur(new Jour(2023, 122)));
    }

    @Test
    void valeurActionSimpleJourFerieMarcheFerme() {
        // Arrange
        action.enrgCours(new Jour(2024, 366), 200.0f); // December 31, 2024 (leap year)
        action.enrgCours(new Jour(2025, 1), 0);   // January 1, 2025

        // Assert
        assertEquals(200.0f, action.valeur(new Jour(2025, 1)));
    }

    @Test
    void dateFutureSansValeurDisponible() {
        // Arrange
        action.enrgCours(new Jour(2023, 122), 150.0f);

        // Assert
        assertEquals(0.0f, action.valeur(new Jour(2024, 1)));
    }



}