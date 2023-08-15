package edu.duke.ece651.team14.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import edu.duke.ece651.team14.client.ClientChatRoom;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;

public class ChatController implements Initializable {
  ClientChatRoom chatRoom_model;

  @FXML
  ListView<String> chat;

  @FXML
  TextArea message;

  public ChatController(ClientChatRoom room) {
    this.chatRoom_model = room;
  }

  @FXML
  public void OnSend(ActionEvent event) throws IOException {
    sendMessage();
  }

  /**
   * Sends message to server
   *
   * @throws IOException
   */
  public void sendMessage() throws IOException {
    chatRoom_model.sendMessage(message.getText());
    message.setText("");
  }

  /*
   * Initializes ListView with Observable list of messages, adds a listener that
   * scrolls almost to the bottom when list is updated (not sure how to scroll all
   * the way to the botoom)
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    chat.setItems(chatRoom_model.getObservableList());
    chat.getItems().addListener(new ListChangeListener<String>() {
      @Override
      public void onChanged(ListChangeListener.Change<? extends String> c) {
        chat.scrollTo(chat.getItems().size() - 1);
      }
    });

    message.setOnKeyPressed(e -> {
      if (e.getCode().equals(KeyCode.ENTER)) {
        try {
          sendMessage();
        } catch (Exception exception) {
          System.out.println(exception);
        }
      }
    });
  }

}
