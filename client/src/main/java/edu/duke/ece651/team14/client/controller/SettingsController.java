package edu.duke.ece651.team14.client.controller;

import java.net.URL;
import java.util.ResourceBundle;

import edu.duke.ece651.team14.shared.GameModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;

public class SettingsController implements Initializable {

  @FXML
  CheckBox musicCheckBox;

  @FXML
  CheckBox sfxCheckBox;

  @FXML
  Slider volumeSlider;

  @FXML
  ColorPicker bgColorColorPicker;

  GameModel model;

  public SettingsController(GameModel model) {
    this.model = model;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    System.out.println("In settings controller initializer");
    try {
      musicCheckBox.selectedProperty().bindBidirectional(model.musicOn);
    } catch (Exception e) {
      System.out.println(e);
    }
    System.out.println("Bound music");
    sfxCheckBox.selectedProperty().bindBidirectional(model.sfxOn);
    System.out.println("Bound sfx");
    volumeSlider.valueProperty().bindBidirectional(model.volume);
    System.out.println("Bound volume");
    bgColorColorPicker.setOnAction(e -> {
      Color chosenColor = bgColorColorPicker.getValue();
      model.bgColor.setValue(chosenColor);
    });
    System.out.println("Bound color");
  }

}
