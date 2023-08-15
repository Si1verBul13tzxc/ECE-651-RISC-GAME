package edu.duke.ece651.team14.server;

import java.util.ArrayList;

import edu.duke.ece651.team14.shared.AllianceOrder;
import edu.duke.ece651.team14.shared.Map;
import edu.duke.ece651.team14.shared.Order;
import edu.duke.ece651.team14.shared.Player;

public class ServerFormAllianceResolver {
  private Map map;

  public ServerFormAllianceResolver(Map map) {
    this.map = map;
  }

  public String resolveAllianceOrders(ArrayList<Order> allianceOrders, String result) {
    for (int i = 0; i < allianceOrders.size(); i++) {
      AllianceOrder ao = (AllianceOrder) allianceOrders.get(i);
      Player p = ao.getPlayer();
      Player new_ally = ao.getNewAlly();
      for (int j = 0; j < allianceOrders.size(); j++) {
        AllianceOrder ao2 = (AllianceOrder) allianceOrders.get(j);
        Player p2 = ao2.getPlayer();
        Player new_ally2 = ao2.getNewAlly();
        if(p.getName().equals(new_ally2.getName())&&p2.getName().equals(new_ally.getName())){
          Player player1 = map.getPlayerByName(p.getName());
          Player player2 = map.getPlayerByName(p2.getName());
          player1.addAlly(player2);
          player2.addAlly(player1);
          result += "Player "+p.getName()+" forms an alliance with the "+ p2.getName()+" Player!\n";
          allianceOrders.remove(ao2);
          allianceOrders.remove(ao);
          break;
        }
      }
    }
    return result;
  }
}
