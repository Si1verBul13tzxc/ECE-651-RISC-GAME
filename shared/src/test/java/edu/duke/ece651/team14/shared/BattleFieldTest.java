package edu.duke.ece651.team14.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class BattleFieldTest {
  Territory t1;
  Territory t2;
  Territory t3;
  Map map;
  Player p1;
  Player p2;
  Player p3;

  @BeforeEach
  public void init() {
    t1 = new BasicTerritory("t1");
    t2 = new BasicTerritory("t2");
    t3 = new BasicTerritory("t3");
    p1 = new BasicPlayer(new Color("red"), "red");
    p2 = new BasicPlayer(new Color("yellow"), "yellow");
    p3 = new BasicPlayer(new Color("blue"), "blue");
    t1.setOwner(p1);
    t2.setOwner(p2);
    t3.setOwner(p3);
    UnitsFactory uf = new UnitsFactory();
    ArrayList<Unit> t1_units = uf.makeUnits(10, "basic");
    setUnitsOwner(t1_units, p1);
    ArrayList<Unit> t2_units = uf.makeUnits(10, "basic");
    setUnitsOwner(t2_units, p2);
    ArrayList<Unit> t3_units = uf.makeUnits(10, "basic");
    setUnitsOwner(t3_units, p3);
    t1.addUnits(t1_units);
    t2.addUnits(t2_units);
    t3.addUnits(t3_units);
    ArrayList<Territory> ts = new ArrayList<>();
    ts.add(t1);
    ts.add(t2);
    ts.add(t3);
    map = new Map(ts, "test");
  }

  private void setUnitsOwner(ArrayList<Unit> units, Player p) {
    for (Unit u : units) {
      u.setOwner(p);
    }
  }

  @Test
  public void test_not_allied() {
    CombatResolver mockResolver = mock(CombatResolver.class);
    Unit attacker = mock(Unit.class);
    Unit defender = mock(Unit.class);
    Mockito.when(mockResolver.getCombatResult(defender,
        attacker)).thenReturn(true);
    BattleField bf = new BattleField(mockResolver, t1, map);
    AttackOrder a1 = new AttackOrder(t2, t1, 5, p2);
    AttackOrder a2 = new AttackOrder(t3, t1, 2, p3);
    AttackOrder a3 = new AttackOrder(t3, t1, 3, p3);
    bf.addAttackerArmy(a1);
    bf.addAttackerArmy(a2);
    bf.addAttackerArmy(a3);
    assertEquals(3, bf.getNumArmies());
    assertEquals(10, bf.getArmyUnitsNum(p1));
    assertEquals(5, bf.getArmyUnitsNum(p2));
    assertEquals(5, bf.getArmyUnitsNum(p3));
    bf.resolve();
    assertEquals(
        "On Territory t1: \nThe red player defends with 10 units\nThe yellow player attacks with 5 units\nThe blue player attacks with 5 units\nThe red player defends Territory t1 successfully!",
        bf.getResult());
  }

  @Test
  public void test_not_allied2() {
    CombatResolver mockResolver = mock(CombatResolver.class);
    Unit attacker = mock(Unit.class);
    Unit defender = mock(Unit.class);
    Mockito.when(mockResolver.getCombatResult(defender,
        attacker)).thenReturn(true);
    BattleField bf = new BattleField(mockResolver, t1, map);
    AttackOrder a1 = new AttackOrder(t2, t1, 10, p2);
    AttackOrder a2 = new AttackOrder(t3, t1, 5, p3);
    AttackOrder a3 = new AttackOrder(t3, t1, 5, p3);
    bf.addAttackerArmy(a1);
    bf.addAttackerArmy(a2);
    bf.addAttackerArmy(a3);
    assertEquals(3, bf.getNumArmies());
    assertEquals(10, bf.getArmyUnitsNum(p1));
    assertEquals(10, bf.getArmyUnitsNum(p2));
    assertEquals(10, bf.getArmyUnitsNum(p3));
    bf.resolve();
    assertEquals(
        "On Territory t1: \nThe red player defends with 10 units\nThe yellow player attacks with 10 units\nThe blue player attacks with 10 units\nThe yellow player conquers Territory t1!",
        bf.getResult());
  }

  @Test
  public void test_allied1() {
    CombatResolver mockResolver = mock(CombatResolver.class);
    Unit attacker = mock(Unit.class);
    Unit defender = mock(Unit.class);
    Mockito.when(mockResolver.getCombatResult(defender,
        attacker)).thenReturn(true);
    p2.addAlly(p3);
    p3.addAlly(p2);
    BattleField bf = new BattleField(mockResolver, t1, map);
    AttackOrder a1 = new AttackOrder(t2, t1, 10, p2);
    AttackOrder a2 = new AttackOrder(t3, t1, 5, p3);
    AttackOrder a3 = new AttackOrder(t3, t1, 5, p3);
    bf.addAttackerArmy(a1);
    bf.addAttackerArmy(a2);
    bf.addAttackerArmy(a3);
    assertEquals(2, bf.getNumArmies());
    assertEquals(10, bf.getArmyUnitsNum(p1));
    assertEquals(20, bf.getArmyUnitsNum(p2));
    assertEquals(-1, bf.getArmyUnitsNum(p3));
    bf.resolve();
    assertEquals(
        "On Territory t1: \nThe red player defends with 10 units\nAlliance of yellow and blue attacks with 20 units\nAlliance of yellow and blue conquers Territory t1!",
        bf.getResult());
    assertEquals(t1.getOwner(), p2);
  }


  @Test
  public void test_random_resolver() {
    CombatResolver Resolver = new DiceResolver();
    p2.addAlly(p3);
    p3.addAlly(p2);
    BattleField bf = new BattleField(Resolver, t1, map);
    AttackOrder a1 = new AttackOrder(t2, t1, 10, p2);
    AttackOrder a2 = new AttackOrder(t3, t1, 5, p3);
    AttackOrder a3 = new AttackOrder(t3, t1, 5, p3);
    bf.addAttackerArmy(a1);
    bf.addAttackerArmy(a2);
    bf.addAttackerArmy(a3);
    assertEquals(2, bf.getNumArmies());
    assertEquals(10, bf.getArmyUnitsNum(p1));
    assertEquals(20, bf.getArmyUnitsNum(p2));
    assertEquals(-1, bf.getArmyUnitsNum(p3));
    bf.resolve();
  }
}
