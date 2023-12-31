package edu.duke.ece651.team14.shared;

/**
 * Class that checks to make sure player or its ally owns origin territory in
 * order
 */
public class OriginOwnershipRuleChecker extends OrderRuleChecker {
  public OriginOwnershipRuleChecker(OrderRuleChecker next) {
    super(next);
  }

  /**
   * Checks that player or its ally owns origin territory
   *
   * @param map   is the game map
   * @param order is the Order
   *
   * @return null if the player or its ally owns the origin territory, a String
   *         explaining broken rule otherwise
   */
  public String checkMyRule(Map map, Order order) {
    if (order.getPlayer().equals(order.getOrigin().getOwner())) {
      return null;
    } else if (order.getPlayer().isAlliedWith(order.getOrigin().getOwner())) {
      return null;
    } else {
      return "Player or its ally does not own origin territory";
    }
  }
}
