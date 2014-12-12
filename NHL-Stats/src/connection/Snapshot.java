package connection;

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
}
