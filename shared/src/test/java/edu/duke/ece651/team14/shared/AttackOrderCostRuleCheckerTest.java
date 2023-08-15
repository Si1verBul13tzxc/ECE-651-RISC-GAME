package edu.duke.ece651.team14.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class AttackOrderCostRuleCheckerTest {
  @Test
  public void test_checkMyRule() {
    Territory start1 = new BasicTerritory("start1");
    Territory dest1 = new BasicTerritory("dest1");
    Player p1 = new BasicPlayer(new Color("yellow"), "p1");
    p1.addFoodResources(100);
    Unit u = new BasicUnit();
    u.setOwner(p1);
    start1.addUnits(u);

    Player otherPlayer = new BasicPlayer(new Color("green"), "otherPlayer");
    Unit other = new BasicUnit();
    other.setOwner(otherPlayer);
    start1.addUnits(other);

    Territory start2 = new BasicTerritory("start2");
    Territory dest2 = new BasicTerritory("dest2");
    Player p2 = new BasicPlayer(new Color("red"), "p2");
    p2.addFoodResources(0);
    Unit n = new BasicUnit();
    n.setOwner(p2);
    start2.addUnits(n);

    start1.setOwner(p1);
    dest1.setOwner(p2);
    start2.setOwner(p2);
    dest2.setOwner(p1);

    start1.addAdjacentTerritories(dest1);
    start2.addAdjacentTerritories(dest2);
    
    dest1.addAdjacentTerritories(start1);
    dest2.addAdjacentTerritories(start2);
    
    ArrayList<Territory> terrs = new ArrayList<>();
    terrs.add(start1);
    terrs.add(start2);
    terrs.add(dest1);
    terrs.add(dest2);
    Map map = new Map(terrs, "map");
    AttackOrderCostRuleChecker a = new AttackOrderCostRuleChecker(null);
    
    AttackOrder o1 = new AttackOrder(start1, dest1, 1, p1);
    assertNull(a.checkMyRule(map, o1));

    ArrayList<Unit> toSend = new ArrayList<>();
    toSend.add(u);
    assertEquals(toSend, o1.getUnitsPicked());

    AttackOrder o2 = new AttackOrder(start2, dest2, 1, p2);
    assertEquals("Not enough food resources to make this attack", a.checkMyRule(map, o2));
  }

}
