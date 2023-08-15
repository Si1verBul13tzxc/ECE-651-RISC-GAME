package edu.duke.ece651.team14.client.controller;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import edu.duke.ece651.team14.client.App;
import edu.duke.ece651.team14.client.GUIClientPlayer;
import edu.duke.ece651.team14.shared.AllianceOrder;
import edu.duke.ece651.team14.shared.AttackOrder;
import edu.duke.ece651.team14.shared.GUIOrderprocessor;
import edu.duke.ece651.team14.shared.GameModel;
import edu.duke.ece651.team14.shared.MoveOrder;
import edu.duke.ece651.team14.shared.Order;
import edu.duke.ece651.team14.shared.Player;
import edu.duke.ece651.team14.shared.ResearchOrder;
import edu.duke.ece651.team14.shared.Territory;
import edu.duke.ece651.team14.shared.UpgradeOrder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class InputButtonsController implements Initializable {
  ArrayList<String> terrs;

  GameModel model;

  GUIClientPlayer client;

  GUIOrderprocessor processor;

  @FXML
  TextArea gameLogText;

  @FXML
  Button upgrade;

  @FXML
  Button move;

  @FXML
  Button attack;

  @FXML
  Button research;

  @FXML
  Button confirm;

  @FXML
  Button formAlliance;

  AudioClip carEngine;
  AudioClip anvil;
  AudioClip powerup;
  AudioClip sword;
  AudioClip trumpets;

  /**
   * state to indicate which button should be disabled
   */
  int state;

  public InputButtonsController(GameModel model, GUIClientPlayer clientPlayer) throws URISyntaxException {
    terrs = new ArrayList<>();
    this.model = model;
    client = clientPlayer;
    carEngine = new AudioClip(App.class.getResource("/audio/car_engine.mp3").toURI().toString());
    anvil = new AudioClip(App.class.getResource("/audio/anvil.mp3").toURI().toString());
    powerup = new AudioClip(App.class.getResource("/audio/powerup.mp3").toURI().toString());
    sword = new AudioClip(App.class.getResource("/audio/sword.mp3").toURI().toString());
    trumpets = new AudioClip(App.class.getResource("/audio/royal_trumpets.mp3").toURI().toString());
  }

  public void playSound(AudioClip soundEffect) {
    if (model.sfxOn.get()) {
      soundEffect.play();
      System.out.println("Played sound effect");
    }
  }
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    switchState(1);
    processor = new GUIOrderprocessor(model.getMap());
  }

  /**
   * Manipulate state
   * button state:
   * 1. upgrade and confirm
   * 2. move and confirm
   * 3. attack and confirm
   * 4. research and confirm
   * 5. alliance
   * 6. finish
   */
  private void switchState(int new_state) {
    switch (new_state) {
      case 1:
        upgrade.setDisable(false);
        formAlliance.setDisable(true);
        move.setDisable(true);
        attack.setDisable(true);
        research.setDisable(true);
        confirm.setText("Finish Upgrade");
        state = 1;
        break;
      case 2:
        upgrade.setDisable(true);
        confirm.setText("Finish Move");
        move.setDisable(false);
        attack.setDisable(true);
        research.setDisable(true);
        formAlliance.setDisable(true);
        state = 2;
        break;
      case 3:
        upgrade.setDisable(true);
        confirm.setText("Finish Attack");
        move.setDisable(true);
        attack.setDisable(false);
        research.setDisable(true);
        formAlliance.setDisable(true);
        state = 3;
        break;
      case 4:
        upgrade.setDisable(true);
        confirm.setText("Finish Research");
        move.setDisable(true);
        attack.setDisable(true);
        research.setDisable(false);
        formAlliance.setDisable(true);
        state = 4;
        break;
      case 5:// form alliance
        upgrade.setDisable(true);
        confirm.setText("Finish Alliance");
        move.setDisable(true);
        attack.setDisable(true);
        research.setDisable(true);
        formAlliance.setDisable(false);
        state = 5;
        break;
      case 6:
        upgrade.setDisable(true);
        confirm.setText("Finish Turn");
        move.setDisable(true);
        attack.setDisable(true);
        research.setDisable(true);
        formAlliance.setDisable(true);
        state = 6;
    }
  }

  @FXML
  public void onMove(ActionEvent e) {
    resetOwnedTerrs();
    String origin = getChoice("Choose origin territory", terrs);
    String destination = getChoice("Choose destination territory", terrs);
    int numUnits = getInteger("Enter number of units to move");
    Territory origin_terr = this.model.getMap().getTerritoryByName(origin);
    Territory dest_terr = this.model.getMap().getTerritoryByName(destination);
    Order move = new MoveOrder(origin_terr, dest_terr, numUnits, this.client.getPlayer());
    try {
      int food = processor.processMove(this.model.getMap(), move, this.client.getPlayer());
      processor.addOrder(new MoveOrder(origin_terr, dest_terr, numUnits, this.client.getBasicPlayer()));
      gameLogText.appendText("Valid move order with food cost: " + food + "\n\n");
      playSound(carEngine);
      // gameLogShowMap();
      // gameLogshowPlayer();
    } catch (Exception ee) {
      gameLogText.appendText(ee.getMessage());
    }
  }

  @FXML
  public void onAttack(ActionEvent e) {
    resetOwnedTerrs();
    String origin = getChoice("Choose origin territory", terrs);
    setAllTerrs();
    String destination = getChoice("Choose territory you want to attack", terrs);
    int numUnits = getInteger("Enter number of units to attack");
    Territory origin_terr = this.model.getMap().getTerritoryByName(origin);
    Territory dest_terr = this.model.getMap().getTerritoryByName(destination);
    Order attackOrder = new AttackOrder(origin_terr, dest_terr, numUnits, this.client.getPlayer());
    try {
      int food = processor.processAttack(this.model.getMap(), attackOrder, this.client.getPlayer());
      processor.addOrder(new AttackOrder(origin_terr, dest_terr, numUnits, this.client.getBasicPlayer()));
      gameLogText.appendText("Valid attack order with food cost: " + food + "\n\n");
      playSound(sword);
      // gameLogShowMap();
      // gameLogshowPlayer();
    } catch (Exception ee) {
      gameLogText.appendText(ee.getMessage());
    }
  }

  @FXML
  public void onResearch(ActionEvent e) {
    resetOwnedTerrs();
    Order ro = new ResearchOrder(this.client.getPlayer());
    try {
      int tech = processor.processResearch(this.model.getMap(), ro, this.client.getPlayer());
      gameLogText.appendText("Valid research order with tech cost: " + tech + "\n\n");
      playSound(anvil);
    } catch (Exception ee) {
      gameLogText.appendText(ee.getMessage());
    }
    switchState(5);
  }

  @FXML
  public void onUpgrade(ActionEvent e) {
    resetOwnedTerrs();
    String origin = getChoice("Choose origin territory", terrs);
    int cur_level = getInteger("Enter the current level of units you want to upgrade");
    int new_level = getInteger("Enter the level you want to upgrade to");
    int numUnits = getInteger("Enter number of units to upgrade");
    Territory origin_terr = model.getMap().getTerritoryByName(origin);
    Order o = new UpgradeOrder(origin_terr, null, numUnits, client.getPlayer(), cur_level, new_level);
    try {
      int tech = processor.processUpgrade(model.getMap(), o, client.getPlayer());
      processor.addOrder(new UpgradeOrder(origin_terr, null, numUnits, client.getBasicPlayer(), cur_level, new_level));
      // model.gameLogText.set("Valid upgrade order with cost:");
      gameLogText.appendText("Valid upgrade order with tech cost: " + tech + "\n\n");
      playSound(powerup);
      // gameLogShowMap();
      // gameLogshowPlayer();
    } catch (Exception exp) {
      // model.gameLogText.set(exp.getMessage());
      gameLogText.appendText(exp.getMessage());
    }
  }

  @FXML
  public void onFormAlliance(ActionEvent e) {
    ArrayList<String> names = this.model.getMap().getAllPlayerNames();
    String alliance_name = getChoice("Form Alliance With", names);
    Player p1 = this.client.getBasicPlayer();
    Player p2 = this.model.getMap().getPlayerByName(alliance_name);
    Order o = new AllianceOrder(p1, p2);
    try {
      processor.processAlliance(model.getMap(), o, client.getPlayer());
      gameLogText.appendText(
          "Valid Alliance Order: Player " + p1.getName() + " wants to form alliance with " + p2.getName() + "\n\n");
      playSound(trumpets);
      processor.addOrder(o);
      switchState(6);
    } catch (Exception exp) {
      gameLogText.appendText(exp.getMessage());
    }
  }

  @FXML
  public void onFinishTurn(ActionEvent e) {
    switch (state) {
      case 1:
        switchState(2);
        break;
      case 2:
        switchState(3);
        break;
      case 3:
        switchState(4);
        break;
      case 4:
        if (model.getMap().getPlayerNums() >= 3) {
          System.out.println(model.getMap().getPlayerNums());
          switchState(5);
        } else {
          switchState(6);
        }
        break;
      case 5:
        switchState(6);
        break;
      case 6:
        // gameLogText.appendText("Waiting for other players...\n\n");
        finishOneTurn();
    }
  }

  /*
   * Finishes one turn and prints out results from server as well as agression points
   */
  private void finishOneTurn() {
    try {
      this.client.getCommunicator().sendObject(processor.getVerifiedOrders());
      processor.clearVerified();
      String result = this.client.getCommunicator().recvString();
      gameLogText.appendText("---------------------------------------------------------------------------------\n\n");
      gameLogText.appendText("Turn Updates:\n\n");
      if (result.length() > 0) {
        gameLogText.appendText(result + "\n");
      }
      int aggPts = this.client.handleAggPts();
      gameLogText.appendText("Current Aggression Points: " + aggPts + "\n\n");
      if (aggPts == 3) {
        gameLogText.appendText(
            "\nYou have captured territories 3 turns in a row! You have received 5 new units for free. You will receive another reward if you make it to 5 turns in a row.\n\n");
      } else if (aggPts == 5) {
        gameLogText.appendText(
            "\nYou have captured territories 5 turns in a row! Your max tech level has been increased. You will receive another reward if you make it to 8 turns in a row.\n\n");
      } else if (aggPts > 7 && (aggPts % 2 == 0)) {
        gameLogText.appendText(
            "\nYou have captured territories " + aggPts
                + " turns in a row! All of your troops have been upgraded to the next level. Keep going! You will receive this same reward every 2 turns that you keep the current streak alive.\n\n");
      }
      gameLogText.appendText("---------------------------------------------------------------------------------\n\n");

      String gameresult = this.client.getCommunicator().recvString();
      if (gameresult.equals("Gameover")) {// one global winner occurs, exit game.
        this.model.setMap(client.recvMap());
        String wininfo = this.client.displayWinInfo(this.model.getMap());
        ConfirmBox.display(gameresult, wininfo, "Good game well played", "bad game");
        Stage window = (Stage) gameLogText.getScene().getWindow();
        window.close();
      } else {
        startAnotherTurn();
      }
    } catch (Exception exp) {
      System.out.println(exp.getMessage());
    }
  }

  private void startAnotherTurn() {
    try {
      this.model.setMap(client.recvMap());
      if (this.client.getPlayer().hasLost(this.model.getMap())) {// has lost
        boolean disconnect = ConfirmBox.display("You lose!", "Do you want to disconnect?", "Disconnect", "Watch Game");
        if (disconnect) {
          Stage window = (Stage) gameLogText.getScene().getWindow();
          window.close();
        } else {
          processor.clearVerified();
          switchState(5);
        }
      } else {// not lost yet
        this.client.getPlayer().updateResourcesInTurn(this.model.getMap());
        resetOwnedTerrs();
        setGUIPlayerAlliance();
        // gameLogShowMap();
        // gameLogshowPlayer();
        switchState(1);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private void setGUIPlayerAlliance() {
    String myName = this.client.getPlayer().getName();
    Player playerInMap = this.model.getMap().getPlayerByName(myName);
    this.client.getPlayer().allies = playerInMap.allies;
  }

  public void printSource(ActionEvent e) {
    System.out.println(e.getSource());
  }

  /**
   * reset owned and allied territories.
   */
  private void resetOwnedTerrs() {
    terrs.clear();
    for (Territory t : model.getMap().groupTerritoriesByPlayer().get(client.getPlayer())) {
      terrs.add(t.getName());
    }
    String playerName = this.client.getBasicPlayer().getName();
    Player thisPlayer = model.getMap().getPlayerByName(playerName);// the actual player pointer in map.
    if (!thisPlayer.isNotAllied()) {
      Player ally = thisPlayer.allies.get(0);
      for (Territory t : model.getMap().groupTerritoriesByPlayer().get(ally)) {
        terrs.add(t.getName());
      }
    }
  }

  private void setAllTerrs() {
    terrs.clear();
    for (String t : model.getMap().getMap().keySet()) {
      terrs.add(t);
    }
  }

  public void gameLogshowPlayer() {
    StringBuilder sb = new StringBuilder();
    Player p = this.client.getPlayer();
    sb.append("Player: " + p.getName() + "\n");
    sb.append("Maximum Technology Level: " + p.getMaxTechLevel() + "\n");
    sb.append("Total Food Resources: " + p.getFoodAmt() + "\n");
    sb.append("Total Technology Resources: " + p.getTechAmt() + "\n");
    sb.append("Current Aggression Points: " + p.getAggPt() + "\n");
    gameLogText.appendText(sb.toString());
  }

  // private void gameLogShowMap() {
  // MapTextView view = new MapTextView(this.model.getMap());
  // gameLogText.appendText(view.displayMap());
  // }

  /**
   * Calls tryGetInteger until the user enters a valid integer
   *
   * @param prompt is the prompt to display to the user
   *
   * @return int the integer entered by the user
   */
  public int getInteger(String prompt) {
    while (true) {
      Optional<Integer> o = tryGetInteger(prompt);
      if (o.isPresent()) {
        return o.get();
      }
    }
  }

  /**
   * Prompts the user for an integer. Allows the user to close box, in which case
   * the Optional<Integer> is empty.
   *
   * @param prompt is the prompt to display to the user
   *
   * @return Optional<Integer>: empty if user cancels/hits x, containing integer
   *         otherwise.
   */
  public Optional<Integer> tryGetInteger(String prompt) {
    boolean isValid = false;
    boolean enteredInvalidInput = false;
    int num = 0;

    while (!isValid) {
      TextInputDialog dialog = new TextInputDialog();
      dialog.setTitle("Enter Number");
      if (!enteredInvalidInput) {
        dialog.setHeaderText(prompt);
      } else {
        dialog.setHeaderText(prompt + "\n\nPlease enter valid integer");
      }
      dialog.setGraphic(null);
      Optional<String> result = dialog.showAndWait();
      System.out.println("Waited");
      if (result.isPresent()) {
        try {
          System.out.println("here");
          num = Integer.parseInt(result.get());
          return (Optional<Integer>) Optional.of(num);
        } catch (Exception e) {
          System.out.println("invalid");
          enteredInvalidInput = true;
        }
      } else {
        Optional<Integer> o = Optional.empty();
        return o;
      }
    }
    return Optional.of(0);
  }

  /**
   * Gets a choice from the user among specified options
   * 
   * @param prompt  is the prompt to display to the user
   * @param options is the list of options to diplay to the user
   *
   * @return the option the user chooses
   */
  public <T> T getChoice(String prompt, List<T> options) {
    while (true) {
      Optional<T> o = tryGetChoice(prompt, options);
      if (o.isPresent()) {
        return o.get();
      }
    }
  }

  /**
   * Prompts the user to choose from a list of specified options
   *
   * @param prompt  is the prompt to show the user
   * @param options is the list of options to display
   *
   * @return an Optional<String>: empty if user hits cancel/x button, otherwise
   *         contains user selection
   */
  public <T> Optional<T> tryGetChoice(String prompt, List<T> options) {
    ChoiceDialog<T> dialog = new ChoiceDialog<>(options.get(0), options);
    dialog.setHeaderText(prompt);
    dialog.setTitle("Enter Choice");
    dialog.setGraphic(null);
    return dialog.showAndWait();
  }
}
