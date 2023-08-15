package edu.duke.ece651.team14.shared;

/**
 * Class that checks to make sure player or its ally owns destination territory
 * in order
 */
public class DestinationOwnershipRuleChecker extends OrderRuleChecker {
  public DestinationOwnershipRuleChecker(OrderRuleChecker next) {
    super(next);
  }

  /**
   * Checks that player or its ally owns destination territory
   *
   * @param map   is the game map
   * @param order is the Order
   *
   * @return null if the player or its ally owns the destination territory, a
   *         String
   *         explaining broken rule otherwise
   */
  public String checkMyRule(Map map, Order order) {
    if (order.getPlayer().equals(order.getDestination().getOwner())) {
      return null;
    } else if (order.getPlayer().isAlliedWith(order.getDestination().getOwner())) {
      return null;
    } else {
      return "Player or its ally does not own destination territory";
    }
  }
}
