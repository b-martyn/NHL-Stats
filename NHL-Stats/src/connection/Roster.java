package connection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

	@Override
	public String toString() {
		return "Roster [id=" + id + ", teamName=" + teamName + ", startDate="
				+ startDate + ", endDate=" + endDate + ", players="
				+ Arrays.toString(players) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result	+ ((startDate == null) ? 0 : startDate.hashCode());
		if(teamName == null){
			result = prime * result;
		}else{
			for(char character : teamName.toString().toCharArray()){
				result = prime * result + character;
			}
		}
		if(players.length > 0){
			int partResult = 1;
			for(Player player : players){
				partResult += player.hashCode();
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
		
		Roster other = (Roster) obj;
		if (endDate == null) {
			if (other.endDate != null){
				return false;
			}
		} else if (!endDate.equals(other.endDate)){
			return false;
		}
		if (startDate == null) {
			if (other.startDate != null){
				return false;
			}
		} else if (!startDate.equals(other.startDate)){
			return false;
		}
		if (teamName != other.teamName){
			return false;
		}
		if(players.length == other.players.length){
			List<Player> otherPlayers = new ArrayList<Player>();
			for(Player player : other.players){
				otherPlayers.add(player);
			}
			for(Player myPlayer : players){
				boolean playersResult = false;
				for(Player otherPlayer : otherPlayers){
					if(myPlayer.equals(otherPlayer)){
						playersResult = true;
						break;
					}
				}
				if(!playersResult){
					return false;
				}else{
					otherPlayers.remove(myPlayer);
				}
			}
		}else{
			return false;
		}
		return true;
	}
	
	
}
