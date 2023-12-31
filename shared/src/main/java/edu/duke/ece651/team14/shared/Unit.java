package edu.duke.ece651.team14.shared;

public interface Unit {
  public String getType();
  public void setOwner(Player p);
  public Player getOwner();
  public boolean isAlive();
  public boolean tryToKill();
  public int getTechLevel();
  public void increaseTechLevel(int numLevels) throws MaxTechLevelException;
}
