package edu.duke.ece651.team14.shared;

import java.util.ArrayList;
import java.util.Iterator;

public class UnitMover {
  /**
   * Moves up to numUnits units of type unitType from Territory origin to
   * Territory destination
   *
   * @param origin      is the origin Territory
   * @param destination is the destination Territory
   * @param numUnits    is the number of units to move
   * @param unitType    is the unit type
   */
  public static void moveUnits(Territory origin, Territory destination, int numUnits, String unitType) {
    ArrayList<Unit> originUnits = origin.getUnits();
    originUnits.sort((u0, u1) -> u0.getTechLevel() - u1.getTechLevel());
    Iterator<Unit> iterator = originUnits.iterator();
    while (iterator.hasNext() && numUnits > 0) {
      Unit u = iterator.next();
      if (u.getType().equals(unitType) && u.getOwner().equals(origin.getOwner())) {
        iterator.remove();
        destination.addUnits(u);
        numUnits--;
      }
    }
  }


  /** 
   * Move the specific owner's units.
   * @param origin_units
   * @param destination_units
   * @param numUnits
   * @param owner
   */
  public static void moveUnitsByOwner(ArrayList<Unit> originUnits, ArrayList<Unit> destination_units, int numUnits, Player owner) {
    originUnits.sort((u0, u1) -> u0.getTechLevel() - u1.getTechLevel());
    Iterator<Unit> iterator = originUnits.iterator();
    while (iterator.hasNext() && numUnits > 0) {
      Unit u = iterator.next();
      if (u.getOwner().equals(owner)) {
        iterator.remove();
        destination_units.add(u);
        numUnits--;
      }
    }
  }

  public static void sendUnitArray(Territory origin, Territory destination, ArrayList<Unit> unitsToSend) {
    Iterator<Unit> iterator = unitsToSend.iterator();
    int count = unitsToSend.size();
    while (iterator.hasNext() && count > 0) {
      Unit u = iterator.next();
      if (origin.removeUnit(u)) {
        System.out.println("true");
        destination.addUnits(u);
        count--;
      }
    }
  }
}
