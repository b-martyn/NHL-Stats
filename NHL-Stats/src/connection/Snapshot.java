package connection;

import java.util.Arrays;

public class Snapshot {
	
	private int id;
	private int gameId;
	private TimeStamp timeStamp;
	private Player[] homePlayersOnIce;
	private Player[] awayPlayersOnIce;
	
	public Snapshot(int id, int gameId, TimeStamp timeStamp, Player[] homePlayersOnIce, Player[] awayPlayersOnIce) {
		super();
		this.id = id;
		this.gameId = gameId;
		this.timeStamp = timeStamp;
		this.homePlayersOnIce = homePlayersOnIce;
		this.awayPlayersOnIce = awayPlayersOnIce;
	}
	
	public Snapshot(int gameId, TimeStamp timeStamp, Player[] homePlayersOnIce, Player[] awayPlayersOnIce) {
		this(0, gameId, timeStamp, homePlayersOnIce, awayPlayersOnIce);
	}
	
	public int getId() {
		return id;
	}
	
	void setId(int id) {
		this.id = id;
	}
	
	public int getGameId() {
		return gameId;
	}
	
	void setGameId(int gameId) {
		this.gameId = gameId;
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
	
	void setHomePlayersOnIce(Player[] homePlayersOnIce) {
		this.homePlayersOnIce = homePlayersOnIce;
	}
	
	public Player[] getHwayPlayersOnIce() {
		return awayPlayersOnIce;
	}
	
	void setAwayPlayersOnIce(Player[] awayPlayersOnIce) {
		this.awayPlayersOnIce = awayPlayersOnIce;
	}

	@Override
	public String toString() {
		return "Snapshot [id=" + id + ", gameId=" + gameId + ", timeStamp="
				+ timeStamp + ", homePlayersOnIce="
				+ Arrays.toString(homePlayersOnIce) + ", awayPlayersOnIce="
				+ Arrays.toString(awayPlayersOnIce) + "]";
	}
}
