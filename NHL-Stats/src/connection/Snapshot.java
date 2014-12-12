package connection;

import java.util.Arrays;

public class Snapshot {
	
	private int id;
	private Game game;
	private TimeStamp timeStamp;
	private Player[] homePlayersOnIce = new Player[0];
	private Player[] awayPlayersOnIce = new Player[0];
	
	public Snapshot(int id, Game game, TimeStamp timeStamp) {
		super();
		this.id = id;
		this.game = game;
		this.timeStamp = timeStamp;
	}
	
	public Snapshot(Game game, TimeStamp timeStamp) {
		this(0, game, timeStamp);
	}
	
	public int getId() {
		return id;
	}
	
	void setId(int id) {
		this.id = id;
	}
	
	public Game getGame() {
		return game;
	}
	
	void setGame(Game gameId) {
		this.game = gameId;
	}
	
	public TimeStamp getTimeStamp() {
		return timeStamp;
	}
	
	void setTimeStamp(TimeStamp timeStamp) {
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
		return "Snapshot [id=" + id + ", game=" + game.getId() + ", timeStamp="
				+ timeStamp + ", homePlayersOnIce="
				+ Arrays.toString(homePlayersOnIce) + ", awayPlayersOnIce="
				+ Arrays.toString(awayPlayersOnIce) + "]";
	}
}
