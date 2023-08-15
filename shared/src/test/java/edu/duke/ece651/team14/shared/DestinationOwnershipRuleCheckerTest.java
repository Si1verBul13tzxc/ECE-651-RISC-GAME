package edu.duke.ece651.team14.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class DestinationOwnershipRuleCheckerTest {
  @Test
  // Tests checkMyRule() method (player or its ally owns destination territory)
  public void test_checkMyRule() {
    Territory origin = new BasicTerritory("t1");
    Territory destination = new BasicTerritory("t2");
    Territory allyDest = new BasicTerritory("t3");

    Player p = new BasicPlayer(new Color("red"), "p");
    Player ally = new BasicPlayer(new Color("yellow"), "ally");
    p.addAlly(ally);

    destination.setOwner(p);
    allyDest.setOwner(ally);

    ArrayList<Territory> terrs = new ArrayList<>();
    terrs.add(origin);
    terrs.add(destination);
    terrs.add(allyDest);
    Map map = new Map(terrs, "map");

    DestinationOwnershipRuleChecker o = new DestinationOwnershipRuleChecker(null);
    
    MoveOrder m = new MoveOrder(origin, destination, 2, p);
    assertNull(o.checkMyRule(map, m));

    Territory destination2 = new BasicTerritory("t3");
    destination2.setOwner(new BasicPlayer(new Color("blue"), "p2"));
    MoveOrder m2 = new MoveOrder(origin, destination2, 2, p);
    assertEquals("Player or its ally does not own destination territory", o.checkMyRule(map, m2));

    MoveOrder m3 = new MoveOrder(origin, allyDest, 2, p);
    assertNull(o.checkMyRule(map, m3));
  }
}
