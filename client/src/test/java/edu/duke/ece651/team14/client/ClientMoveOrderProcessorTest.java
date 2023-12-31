package edu.duke.ece651.team14.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team14.shared.BasicPlayer;
import edu.duke.ece651.team14.shared.BasicTerritory;
import edu.duke.ece651.team14.shared.BasicUnit;
import edu.duke.ece651.team14.shared.Color;
import edu.duke.ece651.team14.shared.Map;
import edu.duke.ece651.team14.shared.MapFactory;
import edu.duke.ece651.team14.shared.Player;
import edu.duke.ece651.team14.shared.Territory;
import edu.duke.ece651.team14.shared.Unit;

public class ClientMoveOrderProcessorTest {

  String intro = "Time to place all your move orders for this turn\n";
  String doneOrNot = "Type 'D' if you're done entering move orders for this turn. Type anything else to continue entering orders\n";
  String originTerr = "Origin territory for order:\n";
  String destTerr = "Destination territory for order:\n";
  String num = "Number of units to send:\n";
  String terrErr = "Territory does not exist in Map\n";
  //
  String introAtt = "Time to place all your attack orders for this turn\n";
  String doneAtt = "Type 'D' if you're done entering attack orders for this turn. Type anything else to continue entering orders\n";

  public TextClientPlayer createTextClientPlayer(String inputData, Color c, OutputStream bytes) throws IOException {
    Player p = new BasicPlayer(c, "A");
    BufferedReader in = new BufferedReader(new StringReader(inputData));
    PrintStream out = new PrintStream(bytes, true);
    TextClientPlayer tcp = new TextClientPlayer(null, null, in, out);
    tcp.myPlayer = p;
    return tcp;
  }

  public void putUnitsOnTerr(Territory t, int n) {
    ArrayList<Unit> units = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      Unit toadd = new BasicUnit();
      toadd.setOwner(t.getOwner());
      units.add(toadd);
    }
    t.addUnits(units);
  }

  public String helper(ByteArrayOutputStream bytes, String input, String type) throws IOException {
    MapFactory f = new MapFactory();
    TextClientPlayer tcp = createTextClientPlayer(input, new Color("blue"), bytes);
    Player p1 = tcp.myPlayer;
    Player p2 = new BasicPlayer(new Color("red"), "p2");
    ArrayList<Player> players = new ArrayList<>();
    players.add(p1);
    players.add(p2);
    Map testMap = f.makeMap("test", players);
    // I'm going to add one more territory to test map owned by p1 so that I can
    // test what happens when a player tries to move units along an invalid path
    // (there are no invalid paths in testMap right now)
    Territory t = new BasicTerritory("7");
    assertEquals("7", t.getName());
    t.addAdjacentTerritories(testMap.getTerritoryByName("5"));
    testMap.getTerritoryByName("5").addAdjacentTerritories(t);
    t.setOwner(p1);
    testMap.getMap().put("7", t);
    // Put 5 units on each territory of the map
    for (int i = 0; i < 8; i++) {
      Territory terr = testMap.getTerritoryByName(String.valueOf(i));
      putUnitsOnTerr(terr, 5);
    }
    ClientOrderProcessor processor;
    if (type.equals("move")) {
      processor = new ClientMoveOrderProcessor(tcp, testMap);
      processor.processAllOrdersForOneTurn("move");
    }
    if (type.equals("attack")) {
      processor = new ClientAttackOrderProcessor(tcp, testMap);
      processor.processAllOrdersForOneTurn("attack");
    }
    return bytes.toString();
  }

  /*
  @Test
  public void test_simpleOrder() throws IOException {
    // Move
    ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
    String input1 = "c\n0\n1\n5\nd\n";
    String expected1 = intro + doneOrNot + originTerr + "Setting Origin: 0\n" + destTerr + "Setting Destination: 1\n"
        + num + "Sending 5 units\n" + doneOrNot;
    String actual1 = helper(bytes1, input1, "move");
    assertEquals(expected1, actual1);
    // Attack
    ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();
    String input2 = "c\n0\n1\n5\nd\n";
    String expected2 = introAtt + doneAtt + originTerr + "Setting Origin: 0\n" + destTerr + "Setting Destination: 1\n"
        + num + "Sending 5 units\n" + "Player owns the destination territory\n" + doneAtt;
    String actual2 = helper(bytes2, input2, "attack");
    assertEquals(expected2, actual2);
    // Attack #2
    ByteArrayOutputStream bytes3 = new ByteArrayOutputStream();
    String input3 = "c\n2\n4\n5\nc\n7\n5\n5\nd\n";
    String expected3 = introAtt + doneAtt + originTerr + "Setting Origin: 2\n" + destTerr + "Setting Destination: 4\n"
        + num + "Sending 5 units\n" + doneAtt + originTerr + "Setting Origin: 7\n" + destTerr
        + "Setting Destination: 5\n" + num + "Sending 5 units\n" + doneAtt;
    String actual3 = helper(bytes3, input3, "attack");
    assertEquals(expected3, actual3);
    // Attack #3
    ByteArrayOutputStream bytes4 = new ByteArrayOutputStream();
    String input4 = "c\n2\n5\n5\nd\n";
    String expected4 = introAtt + doneAtt + originTerr + "Setting Origin: 2\n" + destTerr + "Setting Destination: 5\n"
        + num + "Sending 5 units\n" + "Territories are not adjacent\n" + doneAtt;
    String actual4 = helper(bytes4, input4, "attack");
    assertEquals(expected4, actual4);
    // Attack #4
    ByteArrayOutputStream bytes5 = new ByteArrayOutputStream();
    String input5 = "c\n2\n4\n3\nc\n2\n4\n3\nd\n";
    String expected5 = introAtt + doneAtt + originTerr + "Setting Origin: 2\n" + destTerr + "Setting Destination: 4\n"
        + num + "Sending 3 units\n" + doneAtt + originTerr + "Setting Origin: 2\n" + destTerr
        + "Setting Destination: 4\n" + num + "Sending 3 units\n"
        + "Not enough units of given type in origin territory\n" + doneAtt;
    String actual5 = helper(bytes5, input5, "attack");
    assertEquals(expected5, actual5);
  }
  */

  /*
  @Test
  public void test_terrNotOnMap() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input = "c\n8\n0\n8\n1\n1\nd\n";
    String expected = intro + doneOrNot + originTerr + terrErr + originTerr + "Setting Origin: 0\n" + destTerr + terrErr
        + destTerr + "Setting Destination: 1\n" + num + "Sending 1 units\n" + doneOrNot;
    String actual = helper(bytes, input, "move");
    assertEquals(expected, actual);
  }
  */

  @Test
  public void test_notOwned() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input = "c\n6\n1\n3\nc\n0\n4\n3\nd\n";
    String expected = intro + doneOrNot + originTerr + "Setting Origin: 6\n" + destTerr + "Setting Destination: 1\n"
        + num + "Sending 3 units\n" + "Player or its ally does not own origin territory\n" + doneOrNot + originTerr
        + "Setting Origin: 0\n" + destTerr + "Setting Destination: 4\n" + num + "Sending 3 units\n"
        + "Player or its ally does not own destination territory\n" + doneOrNot;
    String actual = helper(bytes, input, "move");
    assertEquals(expected, actual);
  }

  @Test
  public void test_invalidPath() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input = "c\n0\n7\n3\nd\n";
    String expected = intro + doneOrNot + originTerr + "Setting Origin: 0\n" + destTerr + "Setting Destination: 7\n"
        + num + "Sending 3 units\n" + "Player does not have a path from origin to destination\n" + doneOrNot;
    String actual = helper(bytes, input, "move");
    assertEquals(expected, actual);
  }

  /*
  @Test
  public void test_badNums() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input = "c\n0\n1\n-1\n0\nthree\n4\nd\n";
    String expected = intro + doneOrNot + originTerr + "Setting Origin: 0\n" + destTerr + "Setting Destination: 1\n"
        + num + "Must send at least one unit\n" + num + "Must send at least one unit\n" + num + "Invalid number\n" + num
        + "Sending 4 units\n" + doneOrNot;
    String actual = helper(bytes, input, "move");
    assertEquals(expected, actual);
  }
  */

  @Test
  public void test_tooManySingleOrder() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input = "c\n0\n1\n6\nd\n";
    String expected = intro + doneOrNot + originTerr + "Setting Origin: 0\n" + destTerr + "Setting Destination: 1\n"
        + num + "Sending 6 units\n" + "Player does not own enough units of given type in origin territory\n" + doneOrNot;
    String actual = helper(bytes, input, "move");
    assertEquals(expected, actual);
  }

  /*
  @Test
  public void test_tooManyTotal() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input = "c\n0\n1\n3\nc\n0\n1\n3\nd\n";
    String expected = intro + doneOrNot + originTerr + "Setting Origin: 0\n" + destTerr + "Setting Destination: 1\n"
        + num + "Sending 3 units\n" + doneOrNot + originTerr + "Setting Origin: 0\n" + destTerr
        + "Setting Destination: 1\n" + num + "Sending 3 units\n"
        + "Not enough units of given type in origin territory\n" + doneOrNot;
    String actual = helper(bytes, input, "move");
    assertEquals(expected, actual);
  }
  */

}
