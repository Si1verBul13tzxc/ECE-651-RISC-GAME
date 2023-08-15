package edu.duke.ece651.team14.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Top level class for the chat room.
 */
public class ServerChatRoom implements Runnable {
  private static int port = 7777;
  ServerSocket chatRoomListenSocket;
  ConcurrentHashMap<UserChatChannel,Integer> map;
  Set<UserChatChannel> chatChannelSet;
    
  public ServerChatRoom() throws IOException {
    this.chatRoomListenSocket = new ServerSocket(port);
    this.map = new ConcurrentHashMap<>();
    this.chatChannelSet = map.keySet(-1);//with default value -1
  }

  /**
     Broad the message within the same game.
   */
  public void broadcast(UserChatChannel msgChannel, String message){
    int game_id = msgChannel.get_gameID();
    for(UserChatChannel c:this.chatChannelSet){
      if(c.get_gameID()==game_id){
        try{
          c.sendMsg(message);
        }catch(IOException ioe){
          c.releaseResource();//close socket
          this.chatChannelSet.remove(c);//remove from set
        }
      }
    }
  }

  /** 
   * Use to remove a disconnected user from set
   * @param channel
   */
  public void removeChannel(UserChatChannel channel){
    this.chatChannelSet.remove(channel);
  }
  
  @Override
  public void run() {
    try {
      while (true) {
        Socket clientSocket = chatRoomListenSocket.accept();
        UserChatChannel ucc = new UserChatChannel(clientSocket, this);
        this.chatChannelSet.add(ucc);
        Thread chatThread = new Thread(ucc,"chat Thread");
        chatThread.start();
      }
    } catch (IOException ioe) {
      System.out.println(ioe.getMessage());
    }

  }

}
