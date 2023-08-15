package edu.duke.ece651.team14.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class NumberOfUnitsRuleCheckerTest {

  @Test
  // Tests checkMyRule method (enough units in origin territory)
  public void test_checkMyRule() {
    Territory origin = new BasicTerritory("t1");
    Territory destination = new BasicTerritory("t2");
    Player p = new BasicPlayer(new Color("red"), "p");

    ArrayList<Territory> terrs = new ArrayList<>();
    terrs.add(origin);
    terrs.add(destination);

    Map map = new Map(terrs, "map");

    NumberOfUnitsRuleChecker o = new NumberOfUnitsRuleChecker(null);

    MoveOrder negativeUnits = new MoveOrder(origin, destination, -5, p);
    assertEquals("Order must have positive value for numUnits", o.checkMyRule(map, negativeUnits));

    MoveOrder m = new MoveOrder(origin, destination, 2, p);
    assertEquals("Player does not own enough units of given type in origin territory", o.checkMyRule(map, m));

    BasicUnit unit1 = new BasicUnit();
    unit1.setOwner(p);
    origin.addUnits(unit1);

    Unit otherTypeUnit = Mockito.mock(BasicUnit.class);
    Mockito.when(otherTypeUnit.getType()).thenReturn("other");

    origin.addUnits(otherTypeUnit);

    assertEquals("Player does not own enough units of given type in origin territory", o.checkMyRule(map, m));

    BasicUnit unit2 = new BasicUnit();
    unit2.setOwner(p);
    origin.addUnits(unit2);

    assertNull(o.checkMyRule(map, m));
  }

  @Test
  // Tests OrderRuleChecker chain (OriginOwnershipRuleChecker then
  // NumberOfUnitsRuleChecker)
  public void test_numUnitsOwnershipChain() {
    NumberOfUnitsRuleChecker n = new NumberOfUnitsRuleChecker(null);
    OriginOwnershipRuleChecker o = new OriginOwnershipRuleChecker(n);

    Player p = new BasicPlayer(new Color("red"), "p");
    Player ally = new BasicPlayer(new Color("yellow"), "ally");

    BasicUnit unit1 = new BasicUnit();
    unit1.setOwner(p);

    BasicUnit unit2 = new BasicUnit();
    unit2.setOwner(ally);

    Territory origin = new BasicTerritory("t1");
    Territory destination = new BasicTerritory("t2");
    origin.setOwner(p);
    origin.addUnits(unit1);
    origin.addUnits(unit2);

    ArrayList<Territory> terrs = new ArrayList<>();
    terrs.add(origin);
    terrs.add(destination);
    Map map = new Map(terrs, "map");

    MoveOrder m = new MoveOrder(origin, destination, 1, p);
    assertNull(o.checkOrder(map, m));

    MoveOrder notEnoughUnits = new MoveOrder(origin, destination, 2, p);
    assertEquals("Player does not own enough units of given type in origin territory",
        o.checkOrder(map, notEnoughUnits));

  }

}
