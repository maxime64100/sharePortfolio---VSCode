package fr.utc.miage.shares;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionSimpleTest {

    private static final ActionSimple action = new ActionSimple("APPL");

    @Test
    void valeurActionSimpleDatePrecise() {
        // Enregistrement du cours de l'action pour une date précise
        action.enrgCours(new Jour(2023, 122), 150.0f);

        // Vérification de la valeur de l'action pour cette date
        assertEquals(150.0f, action.valeur(new Jour(2023, 122)));
    }

    @Test
    void valeurActionSimpleJourFerieMarcheFerme() {
        // Enregistrement du cours de l'action pour une date précise
        action.enrgCours(new Jour(2024, 366), 200.0f); // December 31, 2024 (leap year)
        action.enrgCours(new Jour(2025, 1), 0);   // January 1, 2025

        // Vérification de la valeur de l'action pour un jour férié (marché fermé)
        assertEquals(200.0f, action.valeur(new Jour(2025, 1)));
    }

    @Test
    void dateFutureSansValeurDisponible() {
        // Enregistrement du cours de l'action pour une date précise
        action.enrgCours(new Jour(2023, 122), 150.0f);

        // Vérification de la valeur de l'action pour une date future sans valeur disponible
        assertEquals(0.0f, action.valeur(new Jour(2024, 1)));
    }



}