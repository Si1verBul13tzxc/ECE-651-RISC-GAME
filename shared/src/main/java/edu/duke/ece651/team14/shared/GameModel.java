package edu.duke.ece651.team14.shared;

import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class GameModel{

  Map map;
  // Code needs to change this in whoamiphase
  public String playerName;
  public IntegerProperty mapCount;
  public IntegerProperty foodResources;
  public IntegerProperty techResources;
  public IntegerProperty maxTechLevel;
  public IntegerProperty aggPts;
  public BooleanProperty hasLost;
  public StringProperty selectedTerritory;
  public StringProperty gameLogText;
  public ListProperty<Player> allies;

  public BooleanProperty musicOn;
  public BooleanProperty sfxOn;
  public DoubleProperty volume;
  public SimpleObjectProperty<javafx.scene.paint.Color> bgColor;
  

  public GameModel(Map map, int mapCount, int foodResources, int techResources, int maxTechLevel) {
    this.map = map;
    this.mapCount = new SimpleIntegerProperty(mapCount);
    this.foodResources = new SimpleIntegerProperty(foodResources);
    this.techResources = new SimpleIntegerProperty(techResources);
    this.maxTechLevel = new SimpleIntegerProperty(maxTechLevel);
    this.aggPts = new SimpleIntegerProperty(0);
    this.hasLost = new SimpleBooleanProperty(false);
    this.selectedTerritory = new SimpleStringProperty("midkemia_l");
    this.gameLogText = new SimpleStringProperty("");
    // Create empty observable list of allies
    ArrayList<Player> lst = new ArrayList<>();
    ObservableList<Player> observableLst = FXCollections.observableArrayList(lst);
    this.allies = new SimpleListProperty<Player>(observableLst);

    musicOn = new SimpleBooleanProperty(false);
    sfxOn = new SimpleBooleanProperty(true);
    volume = new SimpleDoubleProperty(0);
    bgColor = new SimpleObjectProperty<>();
  }

  public Map getMap() {
    return map;
  }

  public int getAggPts() {
    return aggPts.get();
  }

  /**
   * Reassigns the map to the given map. Increments mapCount by 1
   *
   * @param map is the new map
   */
  public void setMap(Map map) {
    this.map = map;
    mapCount.set(getMapCount() + 1);
  }

  public String getSelectedTerritory() {
    return selectedTerritory.get();
  }

  public String getPlayerName() {
    return playerName;
  }

  public int getFoodResources() {
    return foodResources.get();
  }

  public int getTechResources() {
    return techResources.get();
  }

  public int getMaxTechLevel() {
    return maxTechLevel.get();
  }

  public int getMapCount() {
    return mapCount.get();
  }

  public String getGameLogText() {
    return gameLogText.get();
  }

}
