package edu.duke.ece651.team14.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UnitMoverTest {
  @Test
  public void test_moveUnits() {
    Territory t1 = new BasicTerritory("t1");
    t1.setOwner(new BasicPlayer(new Color("yellow"), "yellow"));
    Territory t2 = new BasicTerritory("t2");
    t2.setOwner(new BasicPlayer(new Color("yellow"), "yellow"));
    addUnits(t1, t2);

    UnitMover.moveUnits(t1, t2, 4, "basic");
    assertEquals(t1.getUnits().size(), 3);
    assertEquals(t2.getUnits().size(), 11);

    UnitMover.moveUnits(t2, t1, 1, "other");
    assertEquals(t1.getUnits().size(), 4);
    assertEquals(t2.getUnits().size(), 10);

    UnitMover.moveUnits(t2, t1, 10, "basic");
    assertEquals(t1.getUnits().size(), 14);
    assertEquals(t2.getUnits().size(), 0);
  }

  // Helper method to add Units to two given Territories
  private void addUnits(Territory t1, Territory t2) {
    for (int i = 0; i < 3; i++) {
      Unit toadd1 = new BasicUnit();
      toadd1.setOwner(t1.getOwner());
      Unit toadd2 = new BasicUnit();
      toadd2.setOwner(t2.getOwner());
      t1.addUnits(toadd1);
      t2.addUnits(toadd2);
    }
    BasicUnit otherUnit = mock(BasicUnit.class);
    Mockito.when(otherUnit.getType()).thenReturn("other");
    Mockito.when(otherUnit.getOwner()).thenReturn(t1.getOwner());
    t1.addUnits(otherUnit);
    t2.addUnits(otherUnit);
    for (int i = 0; i < 3; i++) {
      Unit toadd1 = new BasicUnit();
      toadd1.setOwner(t1.getOwner());
      Unit toadd2 = new BasicUnit();
      toadd2.setOwner(t2.getOwner());
      t1.addUnits(toadd1);
      t2.addUnits(toadd2);
    }
  }
}
