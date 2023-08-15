package edu.duke.ece651.team14.client;

import java.io.IOException;
import java.net.Socket;

import edu.duke.ece651.team14.shared.Communicator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientChatRoom implements Runnable {
  private static int port = 7777;
  private Socket chatSocket;
  private Communicator comm;
  private ObservableList<String> messages;
  private String player_name;

  public ClientChatRoom(String hostname) throws IOException {
    this.chatSocket = new Socket(hostname, port);
    this.comm = new Communicator(this.chatSocket.getOutputStream(), this.chatSocket.getInputStream());
    this.messages = FXCollections.observableArrayList();
  }

  public void setPlayerName(String name) {
    this.player_name = name;
  }

  /**
   * Receive message from server
   * 
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public void recvMessage() throws IOException, ClassNotFoundException {
    String message = this.comm.recvString();
    Platform.runLater(() -> {
      this.messages.add(message);
    });
  }

  /**
   * Send message to the server
   * 
   * @param message
   * @throws IOException
   */
  public void sendMessage(String message) throws IOException {
    this.comm.sendObject(player_name + ": " + message);
  }

  /**
   * Notify the server the game id.
   * 
   * @throws IOException
   */
  public void init_gameid(String game_id) throws IOException {
    this.comm.sendObject(game_id);
  }

  /**
   * Get the observable list of the chat room
   * 
   * @return
   */
  public ObservableList<String> getObservableList() {
    return this.messages;
  }

  @Override
  public void run() {
    ChatRecv cr_thread = new ChatRecv(this);
    Thread t = new Thread(cr_thread, "chat receive thread");
    t.start();
  }
}
