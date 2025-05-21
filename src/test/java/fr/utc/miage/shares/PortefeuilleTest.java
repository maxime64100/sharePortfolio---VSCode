package fr.utc.miage.shares;

import fr.utc.miage.shares.Action;
import fr.utc.miage.shares.ActionSimple;
import fr.utc.miage.shares.ActionComposee;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class PortefeuilleTest {

    //test d'affichage des actions
    @Test
    void testConsulterPortefeuilleActionSimple() {
        //Arrange
        Portefeuille portefeuille = new Portefeuille();
        Action action1 = new ActionSimple("GOOGLE");
        Action action2 = new ActionSimple("FACEBOOK");

        //Action
        portefeuille.acheter(action1, 10);
        portefeuille.acheter(action2, 5);

        //Assert
        assert portefeuille.getActions().size() == 2;
        assert portefeuille.getQuantite(action1) == 10;
        assert portefeuille.getQuantite(action2) == 5;

    }

    @Test
    void testConsulterPortefeuilleActionComposee() {
        //Arrange
        ActionSimple facebookStock = new ActionSimple("FB1");
        ActionSimple metaStock = new ActionSimple("META1");
        Portefeuille portefeuille = new Portefeuille();
        Map<ActionSimple, Float> proportions = new HashMap<>();

        //Action
        proportions.put(facebookStock, 0.6f);
        proportions.put(metaStock, 0.4f);
        Action actionComposee = new ActionComposee("FACEBOOK", proportions);
        portefeuille.acheter(actionComposee, 10);
        portefeuille.acheter(actionComposee, 5);

        //Assert
        assert portefeuille.getActions().containsKey(actionComposee);
        assert portefeuille.getQuantite(actionComposee) == 15;
    }

    @Test
    void testAfficherDetailsPortefeuille() {
        //Arrange
        ActionSimple google = new ActionSimple("GOOGLE");
        ActionSimple fb1 = new ActionSimple("FB1");
        Map<ActionSimple, Float> proportions = new HashMap<>();
        Portefeuille portefeuille = new Portefeuille();

        //Action
        proportions.put(fb1, 0.6f);
        proportions.put(google, 0.4f);
        ActionComposee composedFb = new ActionComposee("FACEBOOK", proportions);
        portefeuille.acheter(google, 10);
        portefeuille.acheter(composedFb, 5);
        String affichage = portefeuille.afficherPortefeuille();
        System.out.println(affichage); // utile pour visualiser pendant le dev

        //Assert
        assert affichage.contains("GOOGLE");
        assert affichage.contains("FACEBOOK");
        assert affichage.contains("Quantité : 10");
        assert affichage.contains("Quantité : 5");
    }
    
    @Test
    void testAcheter() {

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
