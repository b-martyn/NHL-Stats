package connection;

import java.util.Date;

import connection.Franchise.TeamName;

public class Roster {

	private int id;
	private TeamName teamName;
	private Date startDate;
	private Date endDate;
	private Player[] players;

	public Roster(int id, TeamName teamName, Date startDate, Date endDate, Player[] players) {
		this.id = id;
		this.teamName = teamName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.players = players;
	}
	
	public Roster(int id, TeamName teamName, Date startDate, Date endDate) {
		this(id, teamName, startDate, endDate, new Player[0]);
	}
	
	public Roster(){
		this(0, null, new Date(), new Date());
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TeamName getTeamName() {
		return teamName;
	}
	
	public void setTeamName(TeamName teamName) {
		this.teamName = teamName;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Player[] getPlayers() {
		return players;
	}
	
	public void setPlayers(Player[] players) {
		this.players = players;
	}
	
	public void addPlayer(Player player){
		Player[] newPlayerArray = new Player[players.length + 1];
		for(int i = 0; i < players.length; i++){
			newPlayerArray[i] = players[i];
		}
		newPlayerArray[newPlayerArray.length - 1] = player;
		players = newPlayerArray;
	}
}
