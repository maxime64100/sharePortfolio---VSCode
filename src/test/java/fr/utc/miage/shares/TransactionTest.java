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

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void testEnregistrerTransactionAchat() {
        Portefeuille portefeuille = new Portefeuille();
        ActionSimple action = new ActionSimple("France 2");
        Jour jour = new Jour(LocalDate.now().getYear(), LocalDate.now().getDayOfYear());

        portefeuille.acheter(action, 10);

        List<Transaction> historique = portefeuille.getHistorique();
        assertEquals(1, historique.size());

        Transaction t = historique.get(0);
        assertEquals(Transaction.Type.ACHAT, t.getType());
        assertEquals("France 2", t.getAction().getLibelle());
        assertEquals(10, t.getQuantite());
        assertEquals(jour, t.getJour());
    }

    @Test
    void testEnregistrerTransactionVente() {
        Portefeuille portefeuille = new Portefeuille();
        ActionSimple action = new ActionSimple("France 5");
        Jour jour = new Jour(LocalDate.now().getYear(), LocalDate.now().getDayOfYear());

        portefeuille.acheter(action, 5);
        portefeuille.vendre(action, 3);

        List<Transaction> historique = portefeuille.getHistorique();
        assertEquals(2, historique.size());

        Transaction vente = historique.get(1);
        assertEquals(Transaction.Type.VENTE, vente.getType());
        assertEquals("France 5", vente.getAction().getLibelle());
        assertEquals(3, vente.getQuantite());
        assertEquals(jour, vente.getJour());
    }

    @Test
    void testVenteSuperieureAQuantiteDetenueDoitLeverException() {
        Portefeuille portefeuille = new Portefeuille();
        ActionSimple action = new ActionSimple("France 2");

        portefeuille.acheter(action, 5);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            portefeuille.vendre(action, 10);
        });

        assertEquals("Quantité à vendre supérieure à la quantité détenue.", exception.getMessage());

        List<Transaction> historique = portefeuille.getHistorique();
        assertEquals(1, historique.size());
        assertEquals(Transaction.Type.ACHAT, historique.get(0).getType());
    }

}