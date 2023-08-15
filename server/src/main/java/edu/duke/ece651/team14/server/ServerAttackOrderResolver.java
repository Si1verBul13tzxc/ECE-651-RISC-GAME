package edu.duke.ece651.team14.server;

import java.util.ArrayList;
import java.util.HashMap;

import edu.duke.ece651.team14.shared.AdjacentTerritoryRuleChecker;
import edu.duke.ece651.team14.shared.AttackOrder;
import edu.duke.ece651.team14.shared.BattleField;
import edu.duke.ece651.team14.shared.CombatResolver;
import edu.duke.ece651.team14.shared.DestinationNotOwnedRuleChecker;
import edu.duke.ece651.team14.shared.Map;
import edu.duke.ece651.team14.shared.NumberOfUnitsRuleChecker;
import edu.duke.ece651.team14.shared.Order;
import edu.duke.ece651.team14.shared.OrderRuleChecker;
import edu.duke.ece651.team14.shared.OriginOwnershipRuleChecker;
import edu.duke.ece651.team14.shared.Player;
import edu.duke.ece651.team14.shared.Territory;
import edu.duke.ece651.team14.shared.Unit;
import edu.duke.ece651.team14.shared.UnitMover;

public class ServerAttackOrderResolver {
  private final Map map;
  private final CombatResolver resolver;

  /*
   * Constructor to resolve attack orders sent to server
   */
  public ServerAttackOrderResolver(Map map, CombatResolver resolver) {
    this.map = map;
    this.resolver = resolver;
  }

  /*
   * Resolves all attack orders for one turn
   *
   * @param orders: stores attack orders
   */
  public String resolveAllAttackOrders(ArrayList<Order> orders) {
    StringBuilder sb = new StringBuilder();
    // run attack order checker again
    ArrayList<Order> badOrders = new ArrayList<Order>();
    OrderRuleChecker checker = new OriginOwnershipRuleChecker(
        new DestinationNotOwnedRuleChecker(new AdjacentTerritoryRuleChecker(
            new NumberOfUnitsRuleChecker(null))));
    for (Order o : orders) {
      String checkerResult = checker.checkOrder(this.map, o);
      if (checkerResult != null) {
        System.out.println("Bad attack order :" + o + checkerResult);
        badOrders.add(o);
      }
    }
    for (Order b : badOrders) {
      orders.remove(b);
    }
    // initialize all battlefields
    HashMap<String, BattleField> battleFields = new HashMap<>();
    for (Order o : orders) {
      String locationName = o.getDestination().getName();
      if (!battleFields.containsKey(locationName)) {// battle field not created yet
        Territory combatLocation = map.getTerritoryByName(locationName);
        BattleField field = new BattleField(resolver, combatLocation, this.map);
        battleFields.put(locationName, field);
      }
    }

    // If a player attacks an ally, the alliance is dissolved
    for (Order o : orders) {
      Player A = map.getPlayerByName(o.getOrigin().getOwner().getName());
      Player B = map.getPlayerByName(o.getDestination().getOwner().getName());
      // Player A = o.getOrigin().getOwner();
      // Player B = o.getDestination().getOwner();

      if (A.isAlliedWith(B) || B.isAlliedWith(A)) {
        HashMap<String, Territory> mapCopy = map.getMap();
        ArrayList<Unit> unitsToReturnToPlayerA = new ArrayList<>();
        ArrayList<Unit> unitsToReturnToPlayerB = new ArrayList<>();

        // Cycle through all territories in map
        for (Territory t : mapCopy.values()) {

          // Move B's units stationed in A's territory to closest territory owned by B
          if (t.getOwner().equals(A)) {
            for (Unit u : t.getUnits()) {
              if (u.getOwner().equals(B)) {
                unitsToReturnToPlayerB.add(u);
              }
            }
            if (unitsToReturnToPlayerB.size() > 0) {
              Territory dest = A.findClosestTerritoryByPlayer(t, B);
              System.out.println("shortest Territory: " + dest.getName());
              UnitMover.sendUnitArray(t, dest, unitsToReturnToPlayerB);
            }
          }

          // Move A's units stationed in B's territory to closest territory owned by A
          else if (t.getOwner().equals(B)) {
            for (Unit u : t.getUnits()) {
              if (u.getOwner().equals(A)) {
                unitsToReturnToPlayerA.add(u);
              }
            }
            if (unitsToReturnToPlayerA.size() > 0) {
              Territory dest = B.findClosestTerritoryByPlayer(t, A);
              System.out.println("shortest Territory: " + dest.getName());
              UnitMover.sendUnitArray(t, dest, unitsToReturnToPlayerA);
            }
          }

          unitsToReturnToPlayerB.clear();
          unitsToReturnToPlayerA.clear();
        }
        sb.append("Player " + A.getName() + " breaks the alliance with Player " + B.getName() + "\n");
        A.removeAlly(B);
        B.removeAlly(A);
      }
    }
    // process attackers
    for (Order o : orders) {
      String locationName = o.getDestination().getName();
      battleFields.get(locationName).addAttackerArmy((AttackOrder) o);
    }
    // combat start
    for (BattleField field : battleFields.values()) {
      field.resolve();
      sb.append(field.getResult());
      sb.append("\n");
    }
    return sb.toString();
  }
}
