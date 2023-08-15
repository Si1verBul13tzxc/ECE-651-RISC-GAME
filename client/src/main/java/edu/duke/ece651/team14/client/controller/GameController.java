package edu.duke.ece651.team14.client.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import edu.duke.ece651.team14.client.App;
import edu.duke.ece651.team14.client.GUIClientPlayer;
import edu.duke.ece651.team14.shared.GameModel;
import edu.duke.ece651.team14.shared.Territory;
import edu.duke.ece651.team14.shared.Unit;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameController implements Initializable {
  @FXML
  Label howToPlay;

  @FXML
  Label exitGame;

  @FXML
  TextArea territoryStatsText;

  @FXML
  TextArea userStatsText;

  @FXML
  TextArea gameLogText;

  @FXML
  GUIController guiController;

  @FXML
  GridPane gameGridPane;

  Stage settingsStage;

  Media adventureSong;

  MediaPlayer songPlayer;

  @FXML
  InputButtonsController inputButtonsController;

  HashMap<String, String> terrNames = new HashMap<String, String>();

  // @FXML
  // ListView<String> gameLogList;

  GameModel model;

  GUIClientPlayer client;

  public GameController(GameModel model, GUIClientPlayer client) throws URISyntaxException {
    settingsStage = new Stage();
    // System.out.println(App.class.getResource("/audio/cinematic_adventure").toExternalForm());
    adventureSong = new Media(App.class.getResource("/audio/tech_beat.mp3").toURI().toString());
    // adventureSong = new
    // Media(App.class.getResource("/audio/sample-3s.wav").toExternalForm());
    // adventureSong = new
    // Media("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3");
    // System.out.println(adventureSong.;
    songPlayer = new MediaPlayer(adventureSong);
    // When the techno background music ends, loop to the beginning and play again
    // CREDIT: StackOverflow
    songPlayer.setOnEndOfMedia(new Runnable() {
        @Override
        public void run() {
          songPlayer.seek(Duration.ZERO);
          songPlayer.play();
        }
      });
    // settingsStage.initStyle(StageStyle.UTILITY);
    // hello

    this.model = model;
    this.client = client;
    terrNames.put("midkemia_l", "midkemia");
    terrNames.put("gondor_l", "gondor");
    terrNames.put("oz_l", "oz");
    terrNames.put("neverland_l", "neverland");
    terrNames.put("narnia_l", "narnia");
    terrNames.put("mordor_l", "mordor");
    terrNames.put("scadrial_l", "scadrial");
    terrNames.put("elantris_l", "elantris");
    terrNames.put("olympus_l", "mt olympus");
    terrNames.put("roshar_l", "roshar");
    terrNames.put("othrys_l", "mt othrys");
    terrNames.put("camp_half_blood_l", "camp half-blood");
    terrNames.put("gotham_city_l", "gotham city");
    terrNames.put("diagon_alley_l", "diagon alley");
    terrNames.put("hogwarts_l", "hogwarts");
    terrNames.put("platform_l", "platform 9 and 3/4");
    terrNames.put("jurassic_park_l", "jurassic park");
    terrNames.put("wakanda_l", "wakanda");
    terrNames.put("district12_l", "district twelve");
    terrNames.put("duke_l", "duke");
    terrNames.put("north_pole_l", "north pole");
    terrNames.put("wonka_l", "wonka chocolate factory");
    terrNames.put("atlantis_l", "atlantis");
    terrNames.put("capitol_l", "the capitol");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      initializeSettingsStage(settingsStage);
    } catch (Exception e) {
      System.out.println("Error initializing settings stage");
      System.out.println(e);
    }
    setPlayerText();
    initializeSettingsListeners();
    model.selectedTerritory.addListener((obs, oldValue, newValue) -> {
      System.out.println("In GameModel printing newValue: " + newValue);
      setTerrText(newValue);
    });
    // model.gameLogText.addListener((obs, oldValue, newValue) -> {
    // gameLogText.setText(newValue);
    // });
    model.foodResources.addListener((obs, oldValue, newValue) -> {
      setPlayerText();
    });
    model.techResources.addListener((obs, oldValue, newValue) -> {
      setPlayerText();
    });
    model.maxTechLevel.addListener((obs, oldValue, newValue) -> {
      setPlayerText();
    });
    inputButtonsController.gameLogText = gameLogText;
    guiController.gameLogText = gameLogText;
    try {
      model.setMap(client.recvMap());
      // MapTextView view = new MapTextView(model.getMap());
      // gameLogText.setText(view.displayMap());
      this.client.getPlayer().updateResourcesInTurn(model.getMap());
      // inputButtonsController.gameLogshowPlayer();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    songPlayer.play();
  }

  protected void initializeSettingsListeners() {
    model.musicOn.addListener((obs, oldValue, newValue) -> {
        if (!newValue) {
          model.volume.set(0.0);
        };
    });

    /**
    model.sfxOn.addListener((obs, oldValue, newValue) -> {
      System.out.println(newValue);
    });
    */

    model.volume.addListener((obs, oldValue, newValue) -> {
        songPlayer.setVolume((double) newValue);
        System.out.println((double) newValue);
        System.out.println(songPlayer.getVolume());
    });

    model.bgColor.addListener((obs, oldValue, newValue) -> {
      String c = "#" + getHexString(newValue.getRed()) + getHexString(newValue.getGreen())
          + getHexString(newValue.getBlue());
      gameGridPane.setStyle("-fx-background-color: " + c);
      System.out.println(newValue);
    });

    System.out.println("initialized settings listeners");
  }

  /**
   * Returns two-digit hex String representation of given color value in range 0.0
   * to 1.0
   *
   * @param colorValue is the color value from the Color class
   *
   * @return the hex String representation of the color
   */
  public String getHexString(double colorValue) {
    String hexVal = Integer.toHexString((int) Math.round(colorValue * 255));
    if (hexVal.length() == 2) {
      return hexVal;
    }
    return "0" + hexVal;
  }

  protected void initializeSettingsStage(Stage stage) throws IOException {
    // Code adapted from OnChat code
    stage.setTitle("Settings");
    System.out.println("Set window title");
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/ui/settings.fxml"));
    System.out.println("Got loader");
    loader.setControllerFactory((c) -> {
      return client.getControllerInitializer().get(c);
    });
    System.out.println("Set controller factory");
    Parent root = loader.load();
    System.out.println("Got root");
    Scene scene = new Scene(root, 300, 300);
    System.out.println("Set the scene");
    stage.setScene(scene);
  }

  /*
   * public void initialize() {
   * setPlayerText();
   * model.selectedTerritory.addListener((obs, oldValue, newValue) -> {
   * setTerrText(newValue);
   * });
   * model.gameLogText.addListener((obs, oldValue, newValue) -> {
   * gameLogText.setText(newValue);
   * // Platform.runLater(() ->
   * // gameLogText.scrollTopProperty().set(Double.MAX_VALUE));
   * // gameLogList.getItems().add(newValue);
   * // gameLogList.scrollTo(gameLogList.getItems().size() - 1);
   * // gameLogList.getSelectionModel().select(gameLogList.getItems().size() - 1);
   * // gameLogList.getFocusModel().focus(gameLogList.getItems().size() - 1);
   * // System.out.println(gameLogText.getScrollTop());
   * });
   * }
   */

  public void setTerrText(String newValue) {
    StringBuilder sb = new StringBuilder();
    Territory t = model.getMap().getTerritoryByName(terrNames.get(newValue));
    System.out.println(model.getSelectedTerritory());
    sb.append("Territory: " + t.getName() + "\n");
    sb.append("Owner: " + t.getOwner() + "\n");
    sb.append("Food Production Rate: " + t.getFoodProductionRate() + "\n");
    sb.append("Technology Production Rate: " + t.getTechProductionRate() + "\n");
    sb.append("\nAdjacent territories are each 1 distance away\n");
    String unitInfo = unitTechLevels(t.getUnits());
    sb.append(unitInfo);
    territoryStatsText.setText(sb.toString());
  }

  protected String unitTechLevels(ArrayList<Unit> units) {
    StringBuilder sb = new StringBuilder();
    HashMap<String, ArrayList<Unit>> unitownerInfo = new HashMap<>();
    for (Unit u : units) {
      String owner = u.getOwner().getName();
      ArrayList<Unit> owner_units = unitownerInfo.getOrDefault(owner, new ArrayList<Unit>());
      owner_units.add(u);
      unitownerInfo.put(owner, owner_units);
    }
    for (String owner : unitownerInfo.keySet()) {
      ArrayList<Unit> owner_units = unitownerInfo.get(owner);
      String capName = owner.substring(0, 1).toUpperCase() + owner.substring(1);
      if (owner_units.size() == 1) {
        sb.append("\nPlayer " + capName + " owns " + owner_units.size() + " unit:\n");
      } else {
        sb.append("\nPlayer " + capName + " owns " + owner_units.size() + " units:\n");
      }
      HashMap<Integer, Integer> unitlevelInfo = new HashMap<>();
      for (Unit u : owner_units) {
        int techLevel = u.getTechLevel();
        unitlevelInfo.put(techLevel, unitlevelInfo.getOrDefault(techLevel, 0) + 1);
      }
      for (int i = 0; i < 7; i++) {
        if (unitlevelInfo.getOrDefault(i, 0) > 0) {
          sb.append("   Level " + i + " Units: " + unitlevelInfo.get(i) + "\n");
        }
      }
    }
    return sb.toString();
  }

  public void setPlayerText() {
    StringBuilder sb = new StringBuilder();
    sb.append("Player: " + model.getPlayerName() + "\n");
    sb.append("Maximum Technology Level: " + model.getMaxTechLevel() + "\n");
    sb.append("Total Food Resources: " + model.getFoodResources() + "\n");
    sb.append("Total Technology Resources: " + model.getTechResources() + "\n");
    sb.append("Current Aggression Points: " + model.getAggPts() + "\n");
    userStatsText.setText(sb.toString());
  }

  @FXML
  /**
   * Displays Alert Box with information on how to play game. Gets text from
   * howToPlay.txt file
   *
   * @throws IOException
   * @throws URISyntaxException
   */
  private void onHowToPlay() throws IOException, URISyntaxException {
    Alert howToPlayAlert = new Alert(AlertType.INFORMATION);
    String helpText = new String(Files.readAllBytes(Paths.get(getClass().getResource("/ui/howToPlay.txt").toURI())));
    howToPlayAlert.setContentText(helpText);
    howToPlayAlert.setHeaderText("How to Play");
    howToPlayAlert.setTitle("How To Play");
    howToPlayAlert.show();
  }

  @FXML
  private void OnExit() {
    Stage stage = (Stage) exitGame.getScene().getWindow();
    boolean exit = ConfirmBox.display("Exit the game", "Are you sure you want to exit?", "Yes, I want to exit", "No");
    if (exit) {
      stage.close();
    }
  }

  @FXML
  private void OnChat() throws IOException {
    Stage window = new Stage();
    window.setTitle("RISC IN-GAME CHAT ROOM");
    URL url = App.class.getResource("/ui/chat.fxml");
    FXMLLoader loader = new FXMLLoader(url);
    loader.setControllerFactory((c) -> {
      return client.getControllerInitializer().get(c);
    });
    Parent root = loader.load();
    Scene scene = new Scene(root, 550, 350);
    window.setScene(scene);
    window.show();
  }

  /**
   * Opens settings window, should be called when settings label is clicked
   */
  @FXML
  private void onSettings() {
    System.out.println("Trying to show window");
    settingsStage.show();
  }
}
