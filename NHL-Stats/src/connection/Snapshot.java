package connection;

import java.util.ArrayList;
import java.util.List;

public class Snapshot {
	
	private int id;
	private int gameId;
	private TimeStamp timeStamp;
	private Player[] homePlayersOnIce = new Player[0];
	private Player[] awayPlayersOnIce = new Player[0];
	
	public Snapshot(int id, int gameId, TimeStamp timeStamp) {
		this.id = id;
		this.gameId = gameId;
		this.timeStamp = timeStamp;
	}
	
	public Snapshot(int gameId, TimeStamp timeStamp) {
		this(0, gameId, timeStamp);
	}
	
	public Snapshot(){
		this(0, 0, null);
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getGameId() {
		return gameId;
	}
	
	public void setGame(int gameId) {
		this.gameId = gameId;
	}
	
	public TimeStamp getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(TimeStamp timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public Player[] getHomePlayersOnIce() {
		return homePlayersOnIce;
	}
	
	public void addHomePlayerOnIce(Player player){
		Player[] newPlayerArray = new Player[homePlayersOnIce.length + 1];
		for(int i = 0; i < homePlayersOnIce.length; i++){
			newPlayerArray[i] = homePlayersOnIce[i];
		}
		newPlayerArray[newPlayerArray.length - 1] = player;
		homePlayersOnIce = newPlayerArray;
	}
	
	public Player[] getAwayPlayersOnIce() {
		return awayPlayersOnIce;
	}
	
	public void addAwayPlayerOnIce(Player player){
		Player[] newPlayerArray = new Player[awayPlayersOnIce.length + 1];
		for(int i = 0; i < awayPlayersOnIce.length; i++){
			newPlayerArray[i] = awayPlayersOnIce[i];
		}
		newPlayerArray[newPlayerArray.length - 1] = player;
		awayPlayersOnIce = newPlayerArray;
	}

	@Override
	public String toString() {
		return "Snapshot [id=" + id + ", gameId=" + gameId + ", timeStamp="
				+ timeStamp + ", homePlayersOnIce="
				+ homePlayersOnIce.length + ", awayPlayersOnIce="
				+ awayPlayersOnIce.length + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if(homePlayersOnIce.length > 0){
			int partResult = 1;
			for(Player player : homePlayersOnIce){
				partResult += ((player == null) ? 0 : player.hashCode());
			}
			result = prime * result + partResult;
		}
		if(awayPlayersOnIce.length > 0){
			int partResult = 1;
			for(Player player : awayPlayersOnIce){
				partResult += ((player == null) ? 0 : player.hashCode());
			}
			result = prime * result + partResult;
		}
		result = prime * result + gameId;
		result = prime * result	+ ((timeStamp == null) ? 0 : timeStamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()){
			return false;
		}
		if (this == obj){
			return true;
		}
		
		Snapshot other = (Snapshot) obj;
		if(homePlayersOnIce.length == other.homePlayersOnIce.length && awayPlayersOnIce.length == other.awayPlayersOnIce.length){
			List<Player> otherHomePlayers = new ArrayList<Player>();
			for(Player player : other.homePlayersOnIce){
				otherHomePlayers.add(player);
			}
			for(Player myPlayer : homePlayersOnIce){
				boolean playersResult = false;
				for(Player otherPlayer : otherHomePlayers){
					if(myPlayer.equals(otherPlayer)){
						playersResult = true;
						break;
					}
				}
				if(!playersResult){
					return false;
				}else{
					otherHomePlayers.remove(myPlayer);
				}
			}
			List<Player> otherAwayPlayers = new ArrayList<Player>();
			for(Player player : other.awayPlayersOnIce){
				otherAwayPlayers.add(player);
			}
			for(Player myPlayer : awayPlayersOnIce){
				boolean playersResult = false;
				for(Player otherPlayer : otherAwayPlayers){
					if(myPlayer.equals(otherPlayer)){
						playersResult = true;
						break;
					}
				}
				if(!playersResult){
					return false;
				}else{
					otherAwayPlayers.remove(myPlayer);
				}
			}
		}else{
			return false;
		}
		if (gameId != other.gameId){
			return false;
		}else if (timeStamp == null) {
			if (other.timeStamp != null){
				return false;
			}
		} else if (!timeStamp.equals(other.timeStamp)){
			return false;
		}
		return true;
	}
}
