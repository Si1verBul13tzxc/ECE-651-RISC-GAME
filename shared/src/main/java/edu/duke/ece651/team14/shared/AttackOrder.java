package edu.duke.ece651.team14.shared;

import java.util.ArrayList;

/**
 * Class to represent an attack order
 */
public class AttackOrder extends Order {
  private ArrayList<Unit> unitsPicked;

  /**
   * Creates an AttackOrder
   *
   * @param origin      is the origin Territory
   * @param destination is the destination Territory
   * @param numUnits    is the number of units to send
   * @param player      is the player making the order
   * @param unitType    is the type of unit to send
   */
  public AttackOrder(Territory origin, Territory destination, int numUnits, Player player, String unitType) {
    super(origin, destination, numUnits, player, unitType, "attack");
    this.unitsPicked = new ArrayList<Unit>();
  }

  /**
   * Creates an AttackOrder
   *
   * @param origin      is the origin Territory
   * @param destination is the destination Territory
   * @param numUnits    is the number of units to send
   * @param player      is the player making the order
   */
  public AttackOrder(Territory origin, Territory destination, int numUnits, Player player) {
    super(origin, destination, numUnits, player, "attack");
    this.unitsPicked = new ArrayList<Unit>();
  }

  public int calculateCost() {
    unitsPicked.clear();
    int cost = 0;
    ArrayList<Unit> allUnits = new ArrayList<Unit>();
    ArrayList<Unit> playerOwnUnits = new ArrayList<Unit>();
    for (Unit u : getOrigin().getUnits()) {
      allUnits.add(u);
    }
    for (Unit n : allUnits) {
      if (n.getOwner().equals(getPlayer())) {
        playerOwnUnits.add(n);
      }
    }
    // sort player's units by ascending order
    playerOwnUnits.sort((u0, u1) -> u0.getTechLevel() - u1.getTechLevel());
    for (int i = 0; i < getNumUnits(); i++) {
      Unit attackUnit = playerOwnUnits.remove(0);
      unitsPicked.add(attackUnit);
      // int distance = origin.getDistToAdjacentTerr(dest);
      int distance = 1;
      cost += 3 * distance * attackUnit.getTechLevel() + 1; 
    }
    return cost;
  }

  public ArrayList<Unit> getUnitsPicked() {
    return unitsPicked;
  }

}
