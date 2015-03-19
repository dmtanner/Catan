package shared.model;

import java.util.HashMap;

import shared.utils.IDGenerator;

public class RegisteredPlayers {
	private static RegisteredPlayers currentPlayers = null;
	private HashMap<String, String> registeredPlayers = new HashMap<String, String>();
	private HashMap<String,Integer> playerID = new HashMap<String,Integer>();
	private HashMap<Integer,String> IDPlayer = new HashMap<Integer,String>();
	
	
	public static RegisteredPlayers getSingleton(){
		if(currentPlayers == null){
			currentPlayers = new RegisteredPlayers();
		}
		return currentPlayers;
	}
	private RegisteredPlayers(){
		
	}
	
	public void addNewPlayer(String username, String password) {
			registeredPlayers.put(username, password);
			int playerID = IDGenerator.generatePlayerID();
			this.playerID.put(username,playerID);
			IDPlayer.put(playerID, username);
	}
	
	public boolean userExists(String username, String password){
		if (registeredPlayers.containsKey(username) && password.equals(registeredPlayers.get(username))) {
				return true;
		}
		return false;
	}
	public int getPlayerId(String username){
		return playerID.get(username);
	}
	public String getPlayerName(int id){
		return IDPlayer.get(id);
	}
	public boolean containsKey(String key){
		return registeredPlayers.containsKey(key);
	}
}
