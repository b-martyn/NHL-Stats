package admin;
/*package test;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import test.MissedShot.MissedLocation;
import test.Penalty.PenaltyType;
import test.PlayerEvent.PlayerEventType;
import test.Shot.ShotType;
import test.TimeEvent.TimeEventType;
import core.DbGamesConnection;
import core.DbPlayerConnection;
import core.Player;
import core.TeamName;

public class GameCreator {
	
	private static final String GAME_DETAILS_PAGE = "http://www.nhl.com/scores/htmlreports/20142015/";
	
	private DbGamesConnection dbGamesConnection;
	private DbPlayerConnection dbPlayerConnection;
	
	private GameCreator() throws SQLException{
		//dbGamesConnection = new DbGamesConnection();
		dbPlayerConnection = new DbPlayerConnection();
	}
	
	public static void main(String[] args) throws Exception {
		GameCreator gameCreator = new GameCreator();
		gameCreator.execute("PL020063");
	}
	
	Game execute(String summaryString) throws IOException, ParseException, SQLException{
		Connection connection = Jsoup.connect(GAME_DETAILS_PAGE + summaryString + ".HTM");
		connection.maxBodySize(0);
		Document doc = connection.get();
		
		Elements headerElements = doc.getElementsByAttributeValueMatching("style", "font-size: 10px;font-weight:bold");
		TeamName awayTeam = getTeamName(headerElements.get(0).text().substring(0, headerElements.get(0).text().indexOf("Game")));
		Date gameDate = createDate(headerElements.get(2).text(), headerElements.get(4).text());
		TeamName homeTeam = getTeamName(headerElements.get(7).text().substring(0, headerElements.get(7).text().indexOf("Game")));
		
		//Game game = new DbGamesConnection().getGame(gameDate, homeTeam, awayTeam);
		Game game = new Game(gameDate, homeTeam, awayTeam);
		
		Elements bodyElements = doc.select(".evenColor");
		for(Element element : bodyElements){
			List<Object> newData = new ArrayList<Object>();
			Elements children = element.children();
			
			TimeStamp timeStamp = createPeriod(children.get(1).text(), children.get(3).text());
			
			List<Player> awayPlayersOnIce = getPlayersOnIce(awayTeam, children.get(6));
			List<Player> homePlayersOnIce = getPlayersOnIce(homeTeam, children.get(7));
			OnIcePlayers onIce = new OnIcePlayers(homePlayersOnIce, awayPlayersOnIce);
			
			Snapshot snapshot = new Snapshot(1, timeStamp, onIce);
			
			String dataField = children.get(5).text();
			
			List<Player> players = getPlayers(dataField);
			
			switch(children.get(4).text()){
				case "PSTR":
					newData.add(new TimeEvent(true, snapshot, TimeEventType.PERIOD_START));
					break;
				case "PEND":
					newData.add(new TimeEvent(false, snapshot, TimeEventType.PERIOD_END));
					break;
				case "STOP":
					String[] stoppages = dataField.split(",");
					for(String stoppage : stoppages){
						TimeEventType stoppageType = TimeEventType.valueOf(stoppage.toUpperCase().trim().replaceAll(" ", "_"));
						newData.add(new TimeEvent(false, snapshot, stoppageType));
					}
					break;
				case "SHOT":
					ShotType shotType = ShotType.valueOf(dataField.substring(dataField.indexOf(",") + 2, dataField.indexOf(",", dataField.indexOf(",") + 1)).toUpperCase().replace("-", "_"));
					int shotDistanceOut = Integer.valueOf(dataField.substring(dataField.lastIndexOf(",") + 1, dataField.lastIndexOf(",") + 4).trim());
					if(snapshot.getTimeStamp().getElapsedSeconds() == 0){
						newData.add(new ShootoutShot(snapshot, players.get(0), shotType, shotDistanceOut, false));
					}else{
						newData.add(new Shot(snapshot, players.get(0), shotType, shotDistanceOut));
					}
					break;
				case "MISS":
					ShotType missedShotType = ShotType.valueOf(dataField.substring(dataField.indexOf(",") + 2, dataField.indexOf(",", dataField.indexOf(",") + 1)).toUpperCase().replace("-", "_"));
					MissedLocation missedLocation = MissedLocation.valueOf(dataField.substring(dataField.indexOf(",", dataField.indexOf(",") + 1) + 2, dataField.lastIndexOf(",", dataField.lastIndexOf(",", dataField.length()) - 1)).toUpperCase().replaceAll(" ", "_"));
					int missedShotDistanceOut = Integer.valueOf(dataField.substring(dataField.lastIndexOf(",") + 1, dataField.lastIndexOf(",") + 4).trim());
					newData.add(new MissedShot(snapshot, players.get(0), missedShotType, missedShotDistanceOut, missedLocation));
					break;
				case "BLOCK":
					ShotType blockedShotType = ShotType.valueOf(dataField.substring(dataField.indexOf(",") + 2, dataField.indexOf(",", dataField.indexOf(",") + 1)).toUpperCase().replace("-", "_"));
					newData.add(new BlockedShot(snapshot, players.get(0), blockedShotType, -1, players.get(1)));
					break;
				case "GOAL":
					ShotType goalShotType = ShotType.valueOf(dataField.substring(dataField.indexOf(",") + 2, dataField.indexOf(",", dataField.indexOf(",") + 1)).toUpperCase().replace("-", "_"));
					int goalShotDistanceOut = Integer.valueOf(dataField.substring(dataField.lastIndexOf(",") + 1, dataField.lastIndexOf(",") + 4).trim());
					if(snapshot.getTimeStamp().getElapsedSeconds() == 0){
						newData.add(new ShootoutShot(snapshot, players.get(0), goalShotType, goalShotDistanceOut, true));
					}else{
						switch(players.size()){
							case 2:
								newData.add(new Goal(snapshot, players.get(0), goalShotType, goalShotDistanceOut, players.get(1)));
								break;
							case 3:
								newData.add(new Goal(snapshot, players.get(0), goalShotType, goalShotDistanceOut, players.get(1), players.get(2)));
								break;
						}
					}
					break;
				case "FAC":
					TeamName winningTeam = getTeamFromAbbreviation(dataField.substring(0, 3));
					//System.out.println(dataField.indexOf("Zone"));
					Zone faceoffZone = getZone(snapshot, dataField.substring(dataField.indexOf("Zone") - 5, dataField.indexOf("Zone") - 2), winningTeam, game);
					PlayerEventType faceoffType = null;
					for(Player player : players){
						if(player.getFranchise().getTeamName().equals(winningTeam.toString())){
							faceoffType = PlayerEventType.FACEOFF_WIN;
						}else{
							faceoffType = PlayerEventType.FACEOFF_LOSS;
						}
						newData.add(new PlayerEvent(player, snapshot, faceoffZone, faceoffType));
					}
					break;
				case "HIT":
					for(int i = 0; i < players.size(); i++){
						PlayerEventType hitType = null;
						Zone hitZone = getZone(snapshot, dataField.substring(dataField.indexOf("Zone") - 5, dataField.indexOf("Zone") - 2), TeamName.valueOf(players.get(i).getFranchise().getTeamName().toUpperCase().replaceAll(" ", "_")), game);
						switch (i){
							case 0:
								hitType = PlayerEventType.HIT_GIVEN;
								break;
							case 1:
								hitType = PlayerEventType.HIT_RECEIVED;
								break;
						}
						
						newData.add(new PlayerEvent(players.get(i), snapshot, hitZone, hitType));
					}
					break;
				case "GIVE":
					Zone giveawayZone = getZone(snapshot, dataField.substring(dataField.indexOf("Zone") - 5, dataField.indexOf("Zone") - 2), TeamName.valueOf(players.get(0).getFranchise().getTeamName().toUpperCase().replaceAll(" ", "_")), game);
					newData.add(new PlayerEvent(players.get(0), snapshot, giveawayZone, PlayerEventType.GIVEAWAY));
					break;
				case "TAKE":
					Zone takeawayZone = getZone(snapshot, dataField.substring(dataField.indexOf("Zone") - 5, dataField.indexOf("Zone") - 2), TeamName.valueOf(players.get(0).getFranchise().getTeamName().toUpperCase().replaceAll(" ", "_")), game);
					newData.add(new PlayerEvent(players.get(0), snapshot, takeawayZone, PlayerEventType.TAKEAWAY));
					break;
				case "PENL":
					Zone penaltyZone = getZone(snapshot, dataField.substring(dataField.indexOf("Zone") - 5, dataField.indexOf("Zone") - 2), TeamName.valueOf(players.get(0).getFranchise().getTeamName().toUpperCase().replaceAll(" ", "_")), game);
					Pattern pattern = Pattern.compile("[A-Z][A-Z][A-Z][A-Z]+\\W");
					Matcher matcher = pattern.matcher(dataField);
					if(matcher.find()){
						PenaltyType infraction = PenaltyType.valueOf(dataField.substring(matcher.end(), dataField.indexOf("(")).toUpperCase().trim().replaceAll(" ", "_"));
						Short minutes = Short.valueOf(dataField.substring(dataField.lastIndexOf("(") + 1, dataField.lastIndexOf("(") + 3).trim());
						for(int i = 0; i < players.size(); i++){
							PlayerEventType penaltyType = null;
							switch (i){
								case 0:
									penaltyType = PlayerEventType.PENALTY_TAKEN;
									break;
								case 1:
									penaltyType = PlayerEventType.PENALTY_DRAWN;
									break;
							}
							newData.add(new Penalty(players.get(i), snapshot, penaltyZone, penaltyType, infraction, minutes));
						}
					}
					break;
				default:
					System.out.println(children.get(4).text());
			}
			for(Object newStat : newData){
				if(newStat instanceof TimeEvent){
					game.addTimeEvent((TimeEvent)newStat);
					//System.out.println(((TimeEvent)newStat).getType());
				}else if(newStat instanceof Shot){
					game.addShot((Shot)newStat);
					//System.out.println(((Shot)newStat).getShotType());
				}else if(newStat instanceof PlayerEvent){
					game.addPlayerEvent((PlayerEvent)newStat);
					//System.out.println(((PlayerEvent)newStat).getType());
				}
			}
		}
		return game;
	}

	private Zone getZone(Snapshot snapshot, String zoneString, TeamName comparingTeam, Game game) {
		if(comparingTeam.equals(game.getHomeTeam())){
			switch(zoneString.toUpperCase()){
				case "DEF":
					if((snapshot.getTimeStamp().getPeriod() % 2) == 0){
						return Zone.ONE;
					}else{
						return Zone.THREE;
					}
				case "NEU":
					return Zone.TWO;
				case "OFF":
					if((snapshot.getTimeStamp().getPeriod() % 2) == 0){
						return Zone.THREE;
					}else{
						return Zone.ONE;
					}
			}
		}else{
			switch(zoneString.toUpperCase()){
				case "DEF":
					if((snapshot.getTimeStamp().getPeriod() % 2) == 0){
						return Zone.THREE;
					}else{
						return Zone.ONE;
					}
				case "NEU":
					return Zone.TWO;
				case "OFF":
					if((snapshot.getTimeStamp().getPeriod() % 2) == 0){
						return Zone.ONE;
					}else{
						return Zone.THREE;
					}
			}
		}
		return null;
	}

	private List<Player> getPlayers(String dataField) throws NumberFormatException, SQLException {
		/*
		 * Players not in DB will be skipped and no data entered for them.
		 * Try and find a way to auto update the DB or to create a way to crawl
		 * through previous records to get missed information.
		*/
/*		List<Player> players = new ArrayList<Player>();
		int index = dataField.indexOf("#");
		while(index != -1){
			TeamName teamName = null;
			if(dataField.substring(index - 4, index - 1).matches("[A-Z][A-Z][A-Z]")){
				teamName = getTeamFromAbbreviation(dataField.substring(index - 4, index - 1));
			}else{
				teamName = getTeamFromAbbreviation(dataField.substring(0, 3));
			}
			Player player = dbPlayerConnection.getPlayer(teamName, Integer.parseInt(dataField.substring(index + 1, index + 3).trim()));
			if(player != null){
				players.add(player);
			}else{
				// TODO add contingency for player not in DB
			}
			index = dataField.indexOf("#", index + 1);
		}
		return players;
	}

	private TeamName getTeamName(String string){
		String lastStringElement = string.split(" ")[string.split(" ").length - 1].toUpperCase();
		switch(lastStringElement){
			case "JACKETS":
				return TeamName.BLUE_JACKETS;
			case "WINGS":
				return TeamName.RED_WINGS;
			case "LEAFS":
				return TeamName.MAPLE_LEAFS;
			default:
				return TeamName.valueOf(lastStringElement);
		}
	}
	
	private Date createDate(String dayString, String timeString) throws ParseException{
		DateFormat format = new SimpleDateFormat("EEEE, MMMM dd, yyyy hh:mm");
		String dateString = dayString.concat(" " + timeString.substring(6, timeString.indexOf(";") - 4));
		return (Date) format.parse(dateString);
	}
	
	private TimeStamp createPeriod(String numberString, String timeString){
		short number = Short.parseShort(numberString);
		String[] timeStrings = timeString.split(" ");
		String[] elapsedTimeStrings = timeStrings[0].split(":");
		int elapsedSeconds = (Integer.parseInt(elapsedTimeStrings[0]) * 60) + (Integer.parseInt(elapsedTimeStrings[1]));
		String[] remainingTimeStrings = timeStrings[1].split(":");
		int secondsLeft = (Integer.parseInt(remainingTimeStrings[0]) * 60) + (Integer.parseInt(remainingTimeStrings[1]));
		return new TimeStamp(number, elapsedSeconds, secondsLeft);
	}
	
	private List<Player> getPlayersOnIce(TeamName teamName, Element playersElement) throws SQLException{
		List<Player> players = new ArrayList<Player>();
		Elements keyElements = playersElement.select("td > font");
		for(Element element : keyElements){
			players.add(dbPlayerConnection.getPlayer(teamName, Integer.parseInt(element.text())));
		}
		return players;
	}
	
	private TeamName getTeamFromAbbreviation(String teamNameAbbreviated){
		if(teamNameAbbreviated.length() == 3){
			switch(teamNameAbbreviated){
				case "BOS":
					return TeamName.BRUINS;
				case "MTL":
					return TeamName.CANADIENS;
				case "ANA":
					return TeamName.DUCKS;
				case "ARI":
					return TeamName.COYOTES;
				case "BUF":
					return TeamName.SABRES;
				case "CGY":
					return TeamName.FLAMES;
				case "CAR":
					return TeamName.HURRICANES;
				case "CHI":
					return TeamName.BLACKHAWKS;
				case "COL":
					return TeamName.AVALANCHE;
				case "CBJ":
					return TeamName.BLUE_JACKETS;
				case "DAL":
					return TeamName.STARS;
				case "DET":
					return TeamName.RED_WINGS;
				case "EDM":
					return TeamName.OILERS;
				case "FLA":
					return TeamName.PANTHERS;
				case "LAK":
					return TeamName.KINGS;
				case "MIN":
					return TeamName.WILD;
				case "NSH":
					return TeamName.PREDATORS;
				case "NJD":
					return TeamName.DEVILS;
				case "NYI":
					return TeamName.ISLANDERS;
				case "NYR":
					return TeamName.RANGERS;
				case "OTT":
					return TeamName.SENATORS;
				case "PHI":
					return TeamName.FLYERS;
				case "PIT":
					return TeamName.PENGUINS;
				case "SJS":
					return TeamName.SHARKS;
				case "STL":
					return TeamName.BLUES;
				case "TBL":
					return TeamName.LIGHTNING;
				case "TOR":
					return TeamName.MAPLE_LEAFS;
				case "VAN":
					return TeamName.CANUCKS;
				case "WSH":
					return TeamName.CAPITALS;
				case "WPG":
					return TeamName.JETS;
				default:
					//System.out.println("TeamAbbreviated didn't work: " + teamNameAbbreviated);
					break;
			}
		}
		return null;
	}
}
*/
