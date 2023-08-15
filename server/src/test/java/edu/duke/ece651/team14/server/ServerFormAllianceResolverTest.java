package edu.duke.ece651.team14.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team14.shared.AllianceOrder;
import edu.duke.ece651.team14.shared.BasicPlayer;
import edu.duke.ece651.team14.shared.Color;
import edu.duke.ece651.team14.shared.Map;
import edu.duke.ece651.team14.shared.MapFactory;
import edu.duke.ece651.team14.shared.Order;
import edu.duke.ece651.team14.shared.Player;

public class ServerFormAllianceResolverTest {
  MapFactory f;
  ArrayList<Player> players;
  Map test_map;
  Player p1;
  Player p2;
  
  @BeforeEach
  public void init(){
    f = new MapFactory();
    players = new ArrayList<>();
    p1 = new BasicPlayer(new Color("yellow"), "yellow");
    p2 = new BasicPlayer(new Color("blue"), "blue");
    players.add(p1);
    players.add(p2);
    test_map = f.makeMap("test", players);
  }
  
  @Test
  public void test_form_alliance() {
    AllianceOrder a1 = new AllianceOrder(p1, p2);
    AllianceOrder a2 = new AllianceOrder(p2, p1);
    String result = "";
    ServerFormAllianceResolver sfar = new ServerFormAllianceResolver(test_map);
    ArrayList<Order> orders = new ArrayList<>();
    orders.add(a1);
    orders.add(a2);
    String res = sfar.resolveAllianceOrders(orders, result);
    assertEquals("Player yellow forms an alliance with the blue Player!\n", res);
  }

  @Test
  public void test_form_alliance2() {
    AllianceOrder a1 = new AllianceOrder(p1, p2);
    Player p3 = new BasicPlayer(new Color("green"), "green");
    AllianceOrder a2 = new AllianceOrder(p2, p3);
    String result = "";
    ServerFormAllianceResolver sfar = new ServerFormAllianceResolver(test_map);
    ArrayList<Order> orders = new ArrayList<>();
    orders.add(a1);
    orders.add(a2);
    String res = sfar.resolveAllianceOrders(orders, result);
    assertEquals("", res);
  }

}
