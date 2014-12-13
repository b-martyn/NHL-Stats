package connection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
		this(id, date, homeTeam, awayTeam, homeScore, awayScore, new Shot[0], new TimeEvent[0], new PlayerEvent[0]);
	}
	
	public Game(Date date, TeamName homeTeam, TeamName awayTeam, byte homeScore, byte awayScore){
		this(0, date, homeTeam, awayTeam, homeScore, awayScore);
	}
	
	public Game(){
		this(new Date(), null, null, (byte)0, (byte)0);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public TeamName getHomeTeam() {
		return homeTeam;
	}
	
	public void setHomeTeam(TeamName homeTeam) {
		this.homeTeam = homeTeam;
	}
	
	public TeamName getAwayTeam() {
		return awayTeam;
	}
	
	public void setAwayTeam(TeamName awayTeam) {
		this.awayTeam = awayTeam;
	}
	
	public byte getHomeScore() {
		return homeScore;
	}
	
	public void setHomeScore(byte homeScore) {
		this.homeScore = homeScore;
	}
	
	public byte getAwayScore() {
		return awayScore;
	}
	
	public void setAwayScore(byte awayScore) {
		this.awayScore = awayScore;
	}
	
	public Shot[] getShots() {
		return shots;
	}
	
	public void addShot(Shot shot){
		Shot[] newShotArray = new Shot[shots.length + 1];
		for(int i = 0; i < shots.length; i++){
			newShotArray[i] = shots[i];
		}
		newShotArray[newShotArray.length - 1] = shot;
		shots = newShotArray;
	}
	
	public void setShots(Shot[] shots) {
		this.shots = shots;
	}
	
	public TimeEvent[] getTimeEvents() {
		return timeEvents;
	}
	
	public void addTimeEvent(TimeEvent timeEvent){
		TimeEvent[] newTimeEventArray = new TimeEvent[timeEvents.length + 1];
		for(int i = 0; i < shots.length; i++){
			newTimeEventArray[i] = timeEvents[i];
		}
		newTimeEventArray[newTimeEventArray.length - 1] = timeEvent;
		timeEvents = newTimeEventArray;
	}
	
	public void setTimeEvents(TimeEvent[] timeEvents) {
		this.timeEvents = timeEvents;
	}
	
	public PlayerEvent[] getPlayerEvents() {
		return playerEvents;
	}
	
	public void addPlayerEvent(PlayerEvent playerEvent){
		PlayerEvent[] newPlayerEventArray = new PlayerEvent[playerEvents.length + 1];
		for(int i = 0; i < playerEvents.length; i++){
			newPlayerEventArray[i] = playerEvents[i];
		}
		newPlayerEventArray[newPlayerEventArray.length - 1] = playerEvent;
		playerEvents = newPlayerEventArray;
	}
	
	public void setPlayerEvents(PlayerEvent[] playerEvents) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + awayScore;
		result = prime * result	+ ((awayTeam == null) ? 0 : awayTeam.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + homeScore;
		result = prime * result	+ ((homeTeam == null) ? 0 : homeTeam.hashCode());
		if(shots.length > 0){
			int partResult = 1;
			for(Shot shot : shots){
				partResult += ((shot == null) ? 0 : shot.hashCode());
			}
			result = prime * result + partResult;
		}
		if(playerEvents.length > 0){
			int partResult = 1;
			for(PlayerEvent playerEvent : playerEvents){
				partResult += ((playerEvent == null) ? 0 : playerEvent.hashCode());
			}
			result = prime * result + partResult;
		}
		if(timeEvents.length > 0){
			int partResult = 1;
			for(TimeEvent timeEvent : timeEvents){
				partResult += ((timeEvent == null) ? 0 : timeEvent.hashCode());
			}
			result = prime * result + partResult;
		}
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
		
		
		Game other = (Game) obj;
		if (homeTeam != other.homeTeam || awayTeam != other.awayTeam || homeScore != other.homeScore || awayScore != other.awayScore){
			return false;
		}
		if (date == null) {
			if (other.date != null){
				return false;
			}
		} else if (!date.equals(other.date)){
			return false;
		}
		if (shots.length == other.shots.length && playerEvents.length == other.playerEvents.length && timeEvents.length == other.timeEvents.length){
			List<Shot> otherShots = new ArrayList<Shot>();
			for(Shot shot : other.shots){
				otherShots.add(shot);
			}
			for(Shot myShot : shots){
				boolean shotResult = false;
				for(Shot otherShot : otherShots){
					if(myShot.equals(otherShot)){
						shotResult = true;
					}
				}
				if(!shotResult){
					return false;
				}else{
					otherShots.remove(myShot);
				}
			}
			List<PlayerEvent> otherPlayerEvents = new ArrayList<PlayerEvent>();
			for(PlayerEvent playerEvent : other.playerEvents){
				otherPlayerEvents.add(playerEvent);
			}
			for(PlayerEvent myPlayerEvent : playerEvents){
				boolean playerEventsResult = false;
				for(PlayerEvent otherPlayerEvent : otherPlayerEvents){
					if(myPlayerEvent.equals(otherPlayerEvent)){
						playerEventsResult = true;
						break;
					}
				}
				if(!playerEventsResult){
					return false;
				}else{
					otherPlayerEvents.remove(myPlayerEvent);
				}
			}
			List<TimeEvent> otherTimeEvents = new ArrayList<TimeEvent>();
			for(TimeEvent timeEvent : other.timeEvents){
				otherTimeEvents.add(timeEvent);
			}
			for(TimeEvent myTimeEvent : timeEvents){
				boolean timeEventResult = false;
				for(TimeEvent otherTimeEvent : otherTimeEvents){
					if(myTimeEvent.equals(otherTimeEvent)){
						timeEventResult = true;
					}
				}
				if(!timeEventResult){
					return false;
				}else{
					otherTimeEvents.remove(myTimeEvent);
				}
			}
		}else{
			return false;
		}
		
		return true;
	}
	
	
}
