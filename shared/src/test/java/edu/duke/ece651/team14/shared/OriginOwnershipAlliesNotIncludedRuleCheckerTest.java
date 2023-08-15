package edu.duke.ece651.team14.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class OriginOwnershipAlliesNotIncludedRuleCheckerTest {
  @Test
  public void test_checkMyRule() {
    Territory origin = new BasicTerritory("t1");
    Territory destination = new BasicTerritory("t2");
    Territory allyOrigin = new BasicTerritory("t3");

    Player p = new BasicPlayer(new Color("red"), "p");
    Player ally = new BasicPlayer(new Color("yellow"), "ally");
    p.addAlly(ally);

    origin.setOwner(p);
    allyOrigin.setOwner(ally);

    ArrayList<Territory> terrs = new ArrayList<>();
    terrs.add(origin);
    terrs.add(destination);
    terrs.add(allyOrigin);
    Map map = new Map(terrs, "map");

    OriginOwnershipAlliesNotIncludedRuleChecker o = new OriginOwnershipAlliesNotIncludedRuleChecker(null);
    
    MoveOrder m = new MoveOrder(origin, destination, 2, p);
    assertNull(o.checkMyRule(map, m));

    Territory origin2 = new BasicTerritory("t3");
    origin2.setOwner(new BasicPlayer(new Color("blue"), "p2"));
    MoveOrder m2 = new MoveOrder(origin2, destination, 2, p);
    assertEquals("Player does not own origin territory", o.checkMyRule(map, m2));

    MoveOrder m3 = new MoveOrder(allyOrigin, destination, 2, p);
    assertEquals("Player does not own origin territory", o.checkMyRule(map, m3));
  }

}
