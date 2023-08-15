package edu.duke.ece651.team14.client;

import javafx.application.Platform;

/**
 * The receving thread of the chat room
 */
public class ChatRecv implements Runnable {
  ClientChatRoom room;

  public ChatRecv(ClientChatRoom room) {
    this.room = room;
  }

  @Override
  public void run() {
    try {
      while (true) {
        this.room.recvMessage();
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

}
