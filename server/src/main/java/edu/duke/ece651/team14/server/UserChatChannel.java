package edu.duke.ece651.team14.server;

import java.io.IOException;
import java.net.Socket;

import edu.duke.ece651.team14.shared.Communicator;

/**
 * This class is responsible for receive message from one client, and broadcast
 * the message to other clients in the same game.
 */
public class UserChatChannel implements Runnable {
  private Socket userSocket;
  private Communicator userCommunicator;
  private ServerChatRoom chatRoom;
  private int game_id;

  public UserChatChannel(Socket s, ServerChatRoom room) throws IOException {
    this.userSocket = s;
    this.userCommunicator = new Communicator(userSocket.getOutputStream(), userSocket.getInputStream());
    this.chatRoom = room;
    this.game_id = -1;
  }

  public int get_gameID() {
    return this.game_id;
  }

  /**
   * This method is useful for ServerChatRoom to broadcast message.
   * 
   * @param msg
   * @throws IOException
   */
  public void sendMsg(String msg) throws IOException {
    this.userCommunicator.sendObject(msg);
  }

  public void releaseResource() {
    try {
      userSocket.close();
    } catch (IOException ioe) {
      System.out.println(ioe.getMessage());
    }
  }

  @Override
  public void run() {
    try {
      String game_id = this.userCommunicator.recvString();
      int recved_game_id = Integer.parseInt(game_id);
      this.game_id = recved_game_id;// set up chat channel's game id
      while(true){
        String message = this.userCommunicator.recvString();
        chatRoom.broadcast(this, message);
      }
    } catch (Exception ioe) {
      System.out.println(ioe.getMessage());
      chatRoom.removeChannel(this);
      this.releaseResource();
    }
  }
}
