package connection;

import java.util.Arrays;
import java.util.Date;

import connection.Franchise.TeamName;

public class Game {
	private int id;
	private Date date;
	private TeamName homeTeam;
	private TeamName awayTeam;
	private byte homeScore;
	private byte awayScore;
	
	private Shot[] shots;
	private TimeEvent[] timeEvents;
	private PlayerEvent[] playerEvents;
	
	public Game(int id, Date date, TeamName homeTeam, TeamName awayTeam, byte homeScore, byte awayScore, Shot[] shots, TimeEvent[] timeEvents, PlayerEvent[] playerEvents){
		this.id = id;
		this.date = date;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.homeScore = homeScore;
		this.awayScore = awayScore;
		this.shots = shots;
		this.timeEvents = timeEvents;
		this.playerEvents = playerEvents;
	}
	
	public Game(int id, Date date, TeamName homeTeam, TeamName awayTeam, byte homeScore, byte awayScore){
		this.id = id;
		this.date = date;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.homeScore = homeScore;
		this.awayScore = awayScore;
	}
	
	public Game(Date date, TeamName homeTeam, TeamName awayTeam){
		this(0, date, homeTeam, awayTeam, (byte)0, (byte)0);
	}
	
	public int getId() {
		return id;
	}
	
	void setId(int id) {
		this.id = id;
	}
	
	public Date getDate() {
		return date;
	}

	void setDate(Date date) {
		this.date = date;
	}

	public TeamName getHomeTeam() {
		return homeTeam;
	}
	
	void setHomeTeam(TeamName homeTeam) {
		this.homeTeam = homeTeam;
	}
	
	public TeamName getAwayTeam() {
		return awayTeam;
	}
	
	void setAwayTeam(TeamName awayTeam) {
		this.awayTeam = awayTeam;
	}
	
	public byte getHomeScore() {
		return homeScore;
	}
	
	void setHomeScore(byte homeScore) {
		this.homeScore = homeScore;
	}
	
	public byte getAwayScore() {
		return awayScore;
	}
	
	void setAwayScore(byte awayScore) {
		this.awayScore = awayScore;
	}
	
	public Shot[] getShots() {
		return shots;
	}
	
	void setShots(Shot[] shots) {
		this.shots = shots;
	}
	
	public TimeEvent[] getTimeEvents() {
		return timeEvents;
	}
	
	void setTimeEvents(TimeEvent[] timeEvents) {
		this.timeEvents = timeEvents;
	}
	
	public PlayerEvent[] getPlayerEvents() {
		return playerEvents;
	}
	
	void setPlayerEvents(PlayerEvent[] playerEvents) {
		this.playerEvents = playerEvents;
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", date=" + date + ", homeTeam=" + homeTeam
				+ ", awayTeam=" + awayTeam + ", homeScore=" + homeScore
				+ ", awayScore=" + awayScore + ", shots="
				+ Arrays.toString(shots) + ", timeEvents="
				+ Arrays.toString(timeEvents) + ", playerEvents="
				+ Arrays.toString(playerEvents) + "]";
	}
}
