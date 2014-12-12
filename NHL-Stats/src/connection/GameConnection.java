package connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import connection.DbConnection.Table;
import connection.RowSetInstructions.Comparator;
import connection.Franchise.TeamName;
import connection.MissedShot.MissedLocation;
import connection.Penalty.Infraction;
import connection.Player.Position;
import connection.PlayerEvent.PlayerEventType;
import connection.RowSetInstructions.Filter;
import connection.Shot.ShotType;
import connection.TimeEvent.TimeEventType;

public class GameConnection implements GameConnector {
	
	private static GameConnection instance = new GameConnection();
	private DbConnector connection;
	
	private GameConnection() {
		try {
			connection = new DbConnectorFactory().getDbConnector(Table.GAMES);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static GameConnection getInstance(){
		return instance;
	}

	@Override
	public RowSetInstructions getDefaultInstructions() {
		RowSetInstructions instructions = new RowSetInstructions(Table.GAMES);
		
		RowSetInstructions shotInstructions = new RowSetInstructions(Table.SHOTS);
		shotInstructions.addJoiningResultSet(shotInstructions.new JoiningResultSet(Table.PLAYERS, PlayersFields.PLAYERID, ShotsFields.PLAYERID));
		shotInstructions.addJoiningResultSet(shotInstructions.new JoiningResultSet(Table.SHOTPLAYERS, ShotPlayersFields.SHOTID, ShotsFields.SHOTID));
		shotInstructions.addJoiningResultSet(shotInstructions.new JoiningResultSet(Table.PLAYERS, PlayersFields.PLAYERID, ShotPlayersFields.SHOTPLAYERID));
		shotInstructions.addJoiningResultSet(shotInstructions.new JoiningResultSet(Table.SNAPSHOTS, SnapshotsFields.SNAPSHOTID, ShotsFields.SNAPSHOTID));
		shotInstructions.addJoiningResultSet(shotInstructions.new JoiningResultSet(Table.SNAPSHOTPLAYERS, SnapshotPlayersFields.SNAPSHOTID, SnapshotsFields.SNAPSHOTID));
		shotInstructions.addJoiningResultSet(shotInstructions.new JoiningResultSet(Table.PLAYERS, PlayersFields.PLAYERID, SnapshotPlayersFields.SNAPSHOTPLAYERID));
		shotInstructions.addJoiningResultSet(shotInstructions.new JoiningResultSet(Table.ROSTERPLAYERS, RosterPlayersFields.ROSTERPLAYERID, SnapshotPlayersFields.SNAPSHOTPLAYERID));
		shotInstructions.addJoiningResultSet(shotInstructions.new JoiningResultSet(Table.ROSTERS, RostersFields.ROSTERID, RosterPlayersFields.ROSTERID));
		
		RowSetInstructions timeEventInstructions = new RowSetInstructions(Table.TIMEEVENTS);
		timeEventInstructions.addJoiningResultSet(timeEventInstructions.new JoiningResultSet(Table.SNAPSHOTS, SnapshotsFields.SNAPSHOTID, TimeEventsFields.SNAPSHOTID));
		timeEventInstructions.addJoiningResultSet(timeEventInstructions.new JoiningResultSet(Table.SNAPSHOTPLAYERS,SnapshotPlayersFields.SNAPSHOTID, SnapshotsFields.SNAPSHOTID));
		timeEventInstructions.addJoiningResultSet(timeEventInstructions.new JoiningResultSet(Table.PLAYERS,PlayersFields.PLAYERID, SnapshotPlayersFields.SNAPSHOTPLAYERID));
		timeEventInstructions.addJoiningResultSet(timeEventInstructions.new JoiningResultSet(Table.ROSTERPLAYERS, RosterPlayersFields.ROSTERPLAYERID, SnapshotPlayersFields.SNAPSHOTPLAYERID));
		timeEventInstructions.addJoiningResultSet(timeEventInstructions.new JoiningResultSet(Table.ROSTERS, RostersFields.ROSTERID, RosterPlayersFields.ROSTERID));
		
		RowSetInstructions playerEventInstructions = new RowSetInstructions(Table.PLAYEREVENTS);
		playerEventInstructions.addJoiningResultSet(playerEventInstructions.new JoiningResultSet(Table.PLAYERS, PlayersFields.PLAYERID, PlayerEventsFields.PLAYERID));
		playerEventInstructions.addJoiningResultSet(playerEventInstructions.new JoiningResultSet(Table.SNAPSHOTS, SnapshotsFields.SNAPSHOTID, PlayerEventsFields.SNAPSHOTID));
		playerEventInstructions.addJoiningResultSet(playerEventInstructions.new JoiningResultSet(Table.SNAPSHOTPLAYERS,SnapshotPlayersFields.SNAPSHOTID, SnapshotsFields.SNAPSHOTID));
		playerEventInstructions.addJoiningResultSet(playerEventInstructions.new JoiningResultSet(Table.PLAYERS,PlayersFields.PLAYERID, SnapshotPlayersFields.SNAPSHOTPLAYERID));
		playerEventInstructions.addJoiningResultSet(playerEventInstructions.new JoiningResultSet(Table.ROSTERPLAYERS, RosterPlayersFields.ROSTERPLAYERID, SnapshotPlayersFields.SNAPSHOTPLAYERID));
		playerEventInstructions.addJoiningResultSet(playerEventInstructions.new JoiningResultSet(Table.ROSTERS, RostersFields.ROSTERID, RosterPlayersFields.ROSTERID));
		
		try{
			ResultSet shotsResultSet = new DbConnectorFactory().getDbConnector(Table.SHOTS).getResultSet(shotInstructions);
			ResultSet timeEventsResultSet = new DbConnectorFactory().getDbConnector(Table.TIMEEVENTS).getResultSet(timeEventInstructions);
			ResultSet playerEventsResultSet = new DbConnectorFactory().getDbConnector(Table.PLAYEREVENTS).getResultSet(playerEventInstructions);
			instructions.addJoiningResultSet(instructions.new JoiningResultSet(shotsResultSet, GamesFields.GAMEID));
			instructions.addJoiningResultSet(instructions.new JoiningResultSet(timeEventsResultSet, GamesFields.GAMEID));
			instructions.addJoiningResultSet(instructions.new JoiningResultSet(playerEventsResultSet, GamesFields.GAMEID));
		}catch(SQLException e){
			e.printStackTrace();
		}
		return instructions;
	}
	
	@Override
	public ResultSet getLoadedResultSet() throws SQLException{
		return connection.getResultSet(getDefaultInstructions());
	}
	
	@Override
	public Game[] getGames() throws SQLException {
		return convertGames(getLoadedResultSet());
	}
	
	@Override
	public Game[] getGames(TeamName teamName) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(GamesFields.HOMETEAM, Comparator.EQUAL, teamName.toString());
		filter.filterAdditionField(GamesFields.AWAYTEAM, Comparator.EQUAL, teamName.toString(), false);
		instructions.addFilter(filter);
		return convertGames(connection.getResultSet(instructions));
	}
	
	@Override
	public Game[] getGames(Season season) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(GamesFields.DATE, Comparator.GREATER_THEN_EQUAL, new java.sql.Date(season.getStartDate().getTime()));
		filter.filterAdditionField(GamesFields.DATE, Comparator.LESS_THEN_EQUAL, new java.sql.Date(season.getEndDate().getTime()), true);
		instructions.addFilter(filter);
		return convertGames(connection.getResultSet(instructions));
	}

	@Override
	public Game getGame(int id) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(GamesFields.GAMEID, Comparator.EQUAL, id);
		instructions.addFilter(filter);
		try{
			return convertGames(connection.getResultSet(instructions))[0];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}

	@Override
	public Game getGame(Date date, TeamName teamName) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(GamesFields.DATE, Comparator.EQUAL, new java.sql.Date(date.getTime()));
		filter.filterAdditionField(GamesFields.AWAYTEAM, Comparator.EQUAL, teamName.toString(), true);
		filter.filterAdditionField(GamesFields.HOMETEAM, Comparator.EQUAL, teamName.toString(), false);
		try{
			return convertGames(connection.getResultSet(instructions))[0];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	private Game[] convertGames(ResultSet resultSet) throws SQLException{
		List<Game> games = new ArrayList<Game>();
		List<Shot> shots = new ArrayList<Shot>();
		List<PlayerEvent> playerEvents = new ArrayList<PlayerEvent>();
		List<TimeEvent> timeEvents = new ArrayList<TimeEvent>();
		Game game = new Game();
		Shot shot = new Shot();
		PlayerEvent playerEvent = new PlayerEvent();
		TimeEvent timeEvent = new TimeEvent();
		
		int[] snapshotIdColumns = connection.getMatchingColumns(resultSet, SnapshotsFields.SNAPSHOTID);
		int[] periodColumns = connection.getMatchingColumns(resultSet, SnapshotsFields.PERIOD);
		int[] elapsedSecondsColumns = connection.getMatchingColumns(resultSet, SnapshotsFields.ELAPSEDSECONDS);
		int[] secondsLeftColumns = connection.getMatchingColumns(resultSet, SnapshotsFields.SECONDSLEFT);
		int[] snapshotPlayerIdColumns = connection.getMatchingColumns(resultSet, SnapshotPlayersFields.SNAPSHOTPLAYERID);
		int[] playerIdColumns = connection.getMatchingColumns(resultSet, PlayersFields.PLAYERID);
		int[] firstNameColumns = connection.getMatchingColumns(resultSet, PlayersFields.FIRSTNAME);
		int[] lastNameColumns = connection.getMatchingColumns(resultSet, PlayersFields.LASTNAME);
		int[] positionColumns = connection.getMatchingColumns(resultSet, PlayersFields.POSITION);
		int[] teamColumns = connection.getMatchingColumns(resultSet, RostersFields.TEAM);
		
		resultSet.beforeFirst();
		while(resultSet.next()){
			int secondShotPlayerId = resultSet.getInt(ShotPlayersFields.SHOTPLAYERID.toString().toLowerCase());
			int shotSnapshotPlayerId = resultSet.getInt(snapshotPlayerIdColumns[0]);
			String shotSnapshotPlayerFirstName = resultSet.getString(firstNameColumns[2]);
			String shotSnapshotPlayerLastName = resultSet.getString(lastNameColumns[2]);
			Position shotSnapshotPlayerPosition = Position.valueOf(resultSet.getString(positionColumns[2]));
			TeamName shotSnapshotPlayerTeam = TeamName.valueOf(resultSet.getString(teamColumns[0]));
			Player shotSnapshotPlayer = new Player(shotSnapshotPlayerId, shotSnapshotPlayerFirstName, shotSnapshotPlayerLastName, shotSnapshotPlayerPosition);
			
			int timeEventSnapshotPlayerId = resultSet.getInt(snapshotPlayerIdColumns[1]);
			String timeEventSnapshotPlayerFirstName = resultSet.getString(firstNameColumns[3]);
			String timeEventSnapshotPlayerLastName = resultSet.getString(lastNameColumns[3]);
			Position timeEventSnapshotPlayerPosition = Position.valueOf(resultSet.getString(positionColumns[3]));
			TeamName timeEventSnapshotPlayerTeam = TeamName.valueOf(resultSet.getString(teamColumns[1]));
			Player timeEventSnapshotPlayer = new Player(timeEventSnapshotPlayerId, timeEventSnapshotPlayerFirstName, timeEventSnapshotPlayerLastName, timeEventSnapshotPlayerPosition);
			
			int playerEventSnapshotPlayerId = resultSet.getInt(snapshotPlayerIdColumns[2]);
			String playerEventSnapshotPlayerFirstName = resultSet.getString(firstNameColumns[5]);
			String playerEventSnapshotPlayerLastName = resultSet.getString(lastNameColumns[5]);
			Position playerEventSnapshotPlayerPosition = Position.valueOf(resultSet.getString(positionColumns[5]));
			TeamName playerEventSnapshotPlayerTeam = TeamName.valueOf(resultSet.getString(teamColumns[2]));
			Player playerEventSnapshotPlayer = new Player(playerEventSnapshotPlayerId, playerEventSnapshotPlayerFirstName, playerEventSnapshotPlayerLastName, playerEventSnapshotPlayerPosition);
			
			if((resultSet.getInt(GamesFields.GAMEID.toString().toLowerCase())) == game.getId()){
				if(shot.getId() == resultSet.getInt(ShotsFields.SHOTID.toString().toLowerCase())){
					if(secondShotPlayerId != shot.getPlayer().getId()){
						if(shot instanceof BlockedShot){
							//Do nothing, just a repeat of information
						}else{
							int shotPlayerIdIndex = resultSet.findColumn(ShotPlayersFields.SHOTPLAYERID.toString().toLowerCase());
							String shotPlayerFirstName = resultSet.getString(shotPlayerIdIndex + 2);
							String shotPlayerLastName = resultSet.getString(shotPlayerIdIndex + 3);
							Position shotPlayerPosition = Position.valueOf(resultSet.getString(shotPlayerIdIndex + 4));
							Player shotPlayer = new Player(secondShotPlayerId, shotPlayerFirstName, shotPlayerLastName, shotPlayerPosition);
							if(shot instanceof Goal){
								Goal goal = (Goal)shot;
								Player assist = goal.getAssist1();
								if(assist == null){
									shot = new Goal(goal.getId(), goal.getSnapshot(), goal.getPlayer(), goal.getShotType(), goal.getDistanceOut(), shotPlayer);
								}else if(goal.getAssist1().getId() != secondShotPlayerId){
									shot = new Goal(goal.getId(), goal.getSnapshot(), goal.getPlayer(), goal.getShotType(), goal.getDistanceOut(), goal.getAssist1(), shotPlayer);
								}
							}else{
								shot = new BlockedShot(resultSet.getInt(ShotsFields.SHOTID.toString().toLowerCase()), shot.getSnapshot(), shot.getPlayer(), shot.getShotType(), shot.getDistanceOut(), shotPlayer);
							}
						}
					}
					
					boolean result = false;
					for(Player player : timeEvent.getSnapshot().getHomePlayersOnIce()){
						if(player.getId() == shotSnapshotPlayerId){
							result = true;
						}
					}
					for(Player player : timeEvent.getSnapshot().getAwayPlayersOnIce()){
						if(player.getId() == shotSnapshotPlayerId){
							result = true;
						}
					}
					if(!result){
						if(game.getHomeTeam().equals(shotSnapshotPlayerTeam)){
							shot.getSnapshot().addHomePlayerOnIce(shotSnapshotPlayer);
						}else if(game.getAwayTeam().equals(shotSnapshotPlayerTeam)){
							shot.getSnapshot().addAwayPlayerOnIce(shotSnapshotPlayer);
						}
					}
				}else{
					shots.add(shot);
					
					int shotSnapshotId = resultSet.getInt(snapshotIdColumns[0]);
					byte shotSnapshotPeriod = resultSet.getByte(periodColumns[0]);
					short shotSnapshotElapsedSeconds = resultSet.getShort(elapsedSecondsColumns[0]);
					short shotSnapshotSecondsLeft = resultSet.getShort(secondsLeftColumns[0]);
					Snapshot shotSnapshot = new Snapshot(shotSnapshotId, game.getId(), new TimeStamp(shotSnapshotPeriod, shotSnapshotElapsedSeconds, shotSnapshotSecondsLeft));
					int shotPlayerId = resultSet.getInt(playerIdColumns[0]);
					String shotPlayerFirstName = resultSet.getString(firstNameColumns[0]);
					String shotPlayerLastName = resultSet.getString(lastNameColumns[0]);
					Position shotPlayerPosition = Position.valueOf(resultSet.getString(positionColumns[0]));
					Player shotPlayer = new Player(shotPlayerId, shotPlayerFirstName, shotPlayerLastName, shotPlayerPosition);
					int shotId = resultSet.getInt(ShotsFields.SHOTID.toString().toLowerCase());
					ShotType shotType = ShotType.valueOf(resultSet.getString(ShotsFields.SHOTTYPE.toString().toLowerCase()));
					byte distance = resultSet.getByte(ShotsFields.DISTANCE.toString().toLowerCase());
					boolean goal = resultSet.getBoolean(ShotsFields.GOAL.toString().toLowerCase());
					String missedLocationString = resultSet.getString(ShotsFields.MISSEDSHOTLOCATION.toString().toLowerCase());
					if(missedLocationString != null){
						MissedLocation missedLocation = MissedLocation.valueOf(missedLocationString);
						shot = new MissedShot(shotId, shotSnapshot, shotPlayer, shotType, distance, missedLocation);
					}else if(shotSnapshot.getTimeStamp().getElapsedSeconds() == 0 && shotSnapshot.getTimeStamp().getSecondsLeft() == 0){
						shot = new ShootoutShot(shotId, shotSnapshot, shotPlayer, shotType, distance, goal);
					}else if(goal){
						shot = new Goal(shotId, shotSnapshot, shotPlayer, shotType, distance);
					}else if(shotPlayerId != secondShotPlayerId){
						String secondShotPlayerFirstName = resultSet.getString(firstNameColumns[1]);
						String secondShotPlayerLastName = resultSet.getString(lastNameColumns[1]);
						Position secondShotPlayerPosition = Position.valueOf(resultSet.getString(positionColumns[1]));
						Player secondShotPlayer = new Player(secondShotPlayerId, secondShotPlayerFirstName, secondShotPlayerLastName, secondShotPlayerPosition);
						if(goal){
							shot = new Goal(shotId, shotSnapshot, shotPlayer, shotType, distance, secondShotPlayer);
						}else{
							shot = new BlockedShot(shotId, shotSnapshot, shotPlayer, shotType, distance, secondShotPlayer);
						}
					}else{
						shot = new Shot(shotId, shotSnapshot, shotPlayer, shotType, distance);
					}
				}
				
				if(timeEvent.getId() == resultSet.getInt(TimeEventsFields.TIMEEVENTID.toString().toLowerCase())){
					boolean result = false;
					for(Player player : timeEvent.getSnapshot().getHomePlayersOnIce()){
						if(player.getId() == timeEventSnapshotPlayerId){
							result = true;
						}
					}
					for(Player player : timeEvent.getSnapshot().getAwayPlayersOnIce()){
						if(player.getId() == timeEventSnapshotPlayerId){
							result = true;
						}
					}
					if(!result){
						if(game.getHomeTeam().equals(timeEventSnapshotPlayerTeam)){
							timeEvent.getSnapshot().addHomePlayerOnIce(timeEventSnapshotPlayer);
						}else if(game.getAwayTeam().equals(timeEventSnapshotPlayerTeam)){
							timeEvent.getSnapshot().addAwayPlayerOnIce(timeEventSnapshotPlayer);
						}
					}
				}else{
					timeEvents.add(timeEvent);
					
					int timeEventSnapshotId = resultSet.getInt(snapshotIdColumns[1]);
					byte timeEventSnapshotPeriod = resultSet.getByte(periodColumns[1]);
					short timeEventSnapshotElapsedSeconds = resultSet.getShort(elapsedSecondsColumns[1]);
					short timeEventSnapshotSecondsLeft = resultSet.getShort(secondsLeftColumns[1]);
					int timeEventId = resultSet.getInt(TimeEventsFields.TIMEEVENTID.toString().toLowerCase());
					boolean starting = resultSet.getBoolean(TimeEventsFields.STARTINGCLOCK.toString().toLowerCase());
					TimeEventType timeEventType = TimeEventType.valueOf(resultSet.getString(TimeEventsFields.TIMEEVENTTYPE.toString().toLowerCase()));
					timeEvent = new TimeEvent(timeEventId, starting, 
									new Snapshot(timeEventSnapshotId, game.getId(), 
									new TimeStamp(timeEventSnapshotPeriod, timeEventSnapshotElapsedSeconds, timeEventSnapshotSecondsLeft)), 
									timeEventType);
				}
				
				if(playerEvent.getId() == resultSet.getInt(PlayerEventsFields.PLAYEREVENTID.toString().toLowerCase())){
					boolean result = false;
					for(Player player : timeEvent.getSnapshot().getHomePlayersOnIce()){
						if(player.getId() == playerEventSnapshotPlayerId){
							result = true;
						}
					}
					for(Player player : timeEvent.getSnapshot().getAwayPlayersOnIce()){
						if(player.getId() == playerEventSnapshotPlayerId){
							result = true;
						}
					}
					if(!result){
						if(game.getHomeTeam().equals(playerEventSnapshotPlayerTeam)){
							playerEvent.getSnapshot().addHomePlayerOnIce(playerEventSnapshotPlayer);
						}else if(game.getAwayTeam().equals(playerEventSnapshotPlayer)){
							playerEvent.getSnapshot().addAwayPlayerOnIce(playerEventSnapshotPlayer);
						}
					}
				}else{
					playerEvents.add(playerEvent);
					
					int playerEventSnapshotId = resultSet.getInt(snapshotIdColumns[2]);
					byte playerEventSnapshotPeriod = resultSet.getByte(periodColumns[2]);
					short playerEventSnapshotElapsedSeconds = resultSet.getShort(elapsedSecondsColumns[2]);
					short playerEventSnapshotSecondsLeft = resultSet.getShort(secondsLeftColumns[2]);
					Snapshot playerEventSnapshot = new Snapshot(playerEventSnapshotId, game.getId(), 
							new TimeStamp(playerEventSnapshotPeriod, playerEventSnapshotElapsedSeconds, playerEventSnapshotSecondsLeft));
					
					int playerEventId = resultSet.getInt(PlayerEventsFields.PLAYEREVENTID.toString().toLowerCase());
					int playerEventPlayerId = resultSet.getInt(playerIdColumns[1]);
					String playerEventFirstName = resultSet.getString(firstNameColumns[4]);
					String playerEventLastName = resultSet.getString(lastNameColumns[4]);
					Position playerEventPosition = Position.valueOf(resultSet.getString(positionColumns[4]));
					Zone zone = Zone.valueOf(resultSet.getString(PlayerEventsFields.ZONE.toString().toLowerCase()));
					Player player = new Player(playerEventPlayerId, playerEventFirstName, playerEventLastName, playerEventPosition);
					PlayerEventType playerEventType = PlayerEventType.valueOf(resultSet.getString(PlayerEventsFields.PLAYEREVENTTYPE.toString().toLowerCase()));
					if(playerEventType == PlayerEventType.PENALTY_DRAWN || playerEventType == PlayerEventType.PENALTY_TAKEN){
						Infraction infraction = Infraction.valueOf(resultSet.getString(PlayerEventsFields.INFRACTION.toString().toLowerCase()));
						byte minutes = resultSet.getByte(PlayerEventsFields.MINUTES.toString().toLowerCase());
						playerEvent = new Penalty(playerEventId, player, playerEventSnapshot, zone, playerEventType, infraction, minutes);
					}else{
						playerEvent = new PlayerEvent(playerEventId, player, playerEventSnapshot, zone, playerEventType);
					}
				}
			}else{
				if(resultSet.getRow() != 1){
					shots.add(shot);
					playerEvents.add(playerEvent);
					timeEvents.add(timeEvent);
					game.setShots(shots.toArray(new Shot[shots.size()]));
					game.setPlayerEvents(playerEvents.toArray(new PlayerEvent[playerEvents.size()]));
					game.setTimeEvents(timeEvents.toArray(new TimeEvent[timeEvents.size()]));
					games.add(game);
				}
				
				int gameId = resultSet.getInt(GamesFields.GAMEID.toString().toLowerCase());
				Date date = resultSet.getDate(GamesFields.DATE.toString().toLowerCase());
				TeamName homeTeam = TeamName.valueOf(resultSet.getString(GamesFields.HOMETEAM.toString().toLowerCase()));
				TeamName awayTeam = TeamName.valueOf(resultSet.getString(GamesFields.AWAYTEAM.toString().toLowerCase()));
				byte homeScore = resultSet.getByte(GamesFields.HOMESCORE.toString().toLowerCase());
				byte awayScore = resultSet.getByte(GamesFields.AWAYSCORE.toString().toLowerCase());
				game = new Game(gameId, date, homeTeam, awayTeam, homeScore, awayScore);
				
				int shotSnapshotId = resultSet.getInt(snapshotIdColumns[0]);
				byte shotSnapshotPeriod = resultSet.getByte(periodColumns[0]);
				short shotSnapshotElapsedSeconds = resultSet.getShort(elapsedSecondsColumns[0]);
				short shotSnapshotSecondsLeft = resultSet.getShort(secondsLeftColumns[0]);
				Snapshot shotSnapshot = new Snapshot(shotSnapshotId, game.getId(), 
						new TimeStamp(shotSnapshotPeriod, shotSnapshotElapsedSeconds, shotSnapshotSecondsLeft));
				int shotPlayerId = resultSet.getInt(playerIdColumns[0]);
				String shotPlayerFirstName = resultSet.getString(firstNameColumns[0]);
				String shotPlayerLastName = resultSet.getString(lastNameColumns[0]);
				Position shotPlayerPosition = Position.valueOf(resultSet.getString(positionColumns[0]));
				Player shotPlayer = new Player(shotPlayerId, shotPlayerFirstName, shotPlayerLastName, shotPlayerPosition);
				int shotId = resultSet.getInt(ShotsFields.SHOTID.toString().toLowerCase());
				ShotType shotType = ShotType.valueOf(resultSet.getString(ShotsFields.SHOTTYPE.toString().toLowerCase()));
				byte distance = resultSet.getByte(ShotsFields.DISTANCE.toString().toLowerCase());
				boolean goal = resultSet.getBoolean(ShotsFields.GOAL.toString().toLowerCase());
				String missedLocationString = resultSet.getString(ShotsFields.MISSEDSHOTLOCATION.toString().toLowerCase());
				if(missedLocationString != null){
					MissedLocation missedLocation = MissedLocation.valueOf(missedLocationString);
					shot = new MissedShot(shotId, shotSnapshot, shotPlayer, shotType, distance, missedLocation);
				}else if(shotSnapshot.getTimeStamp().getElapsedSeconds() == 0 && shotSnapshot.getTimeStamp().getSecondsLeft() == 0){
					shot = new ShootoutShot(shotId, shotSnapshot, shotPlayer, shotType, distance, goal);
				}else if(goal){
					shot = new Goal(shotId, shotSnapshot, shotPlayer, shotType, distance);
				}else if(shotPlayerId != secondShotPlayerId){
					String secondShotPlayerFirstName = resultSet.getString(firstNameColumns[1]);
					String secondShotPlayerLastName = resultSet.getString(lastNameColumns[1]);
					Position secondShotPlayerPosition = Position.valueOf(resultSet.getString(positionColumns[1]));
					Player secondShotPlayer = new Player(secondShotPlayerId, secondShotPlayerFirstName, secondShotPlayerLastName, secondShotPlayerPosition);
					if(goal){
						shot = new Goal(shotId, shotSnapshot, shotPlayer, shotType, distance, secondShotPlayer);
					}else{
						shot = new BlockedShot(shotId, shotSnapshot, shotPlayer, shotType, distance, secondShotPlayer);
					}
				}else{
					shot = new Shot(shotId, shotSnapshot, shotPlayer, shotType, distance);
				}
				
				int timeEventSnapshotId = resultSet.getInt(snapshotIdColumns[1]);
				byte timeEventSnapshotPeriod = resultSet.getByte(periodColumns[1]);
				short timeEventSnapshotElapsedSeconds = resultSet.getShort(elapsedSecondsColumns[1]);
				short timeEventSnapshotSecondsLeft = resultSet.getShort(secondsLeftColumns[1]);
				int timeEventId = resultSet.getInt(TimeEventsFields.TIMEEVENTID.toString().toLowerCase());
				boolean starting = resultSet.getBoolean(TimeEventsFields.STARTINGCLOCK.toString().toLowerCase());
				TimeEventType timeEventType = TimeEventType.valueOf(resultSet.getString(TimeEventsFields.TIMEEVENTTYPE.toString().toLowerCase()));
				timeEvent = new TimeEvent(timeEventId, starting, 
								new Snapshot(timeEventSnapshotId, game.getId(), 
								new TimeStamp(timeEventSnapshotPeriod, timeEventSnapshotElapsedSeconds, timeEventSnapshotSecondsLeft)), 
								timeEventType);
				
				int playerEventSnapshotId = resultSet.getInt(snapshotIdColumns[2]);
				byte playerEventSnapshotPeriod = resultSet.getByte(periodColumns[2]);
				short playerEventSnapshotElapsedSeconds = resultSet.getShort(elapsedSecondsColumns[2]);
				short playerEventSnapshotSecondsLeft = resultSet.getShort(secondsLeftColumns[2]);
				Snapshot playerEventSnapshot = new Snapshot(playerEventSnapshotId, game.getId(), 
						new TimeStamp(playerEventSnapshotPeriod, playerEventSnapshotElapsedSeconds, playerEventSnapshotSecondsLeft));
				int playerEventId = resultSet.getInt(PlayerEventsFields.PLAYEREVENTID.toString().toLowerCase());
				int playerEventPlayerId = resultSet.getInt(playerIdColumns[1]);
				String playerEventFirstName = resultSet.getString(firstNameColumns[4]);
				String playerEventLastName = resultSet.getString(lastNameColumns[4]);
				Position playerEventPosition = Position.valueOf(resultSet.getString(positionColumns[4]));
				Zone zone = Zone.valueOf(resultSet.getString(PlayerEventsFields.ZONE.toString().toLowerCase()));
				Player player = new Player(playerEventPlayerId, playerEventFirstName, playerEventLastName, playerEventPosition);
				PlayerEventType playerEventType = PlayerEventType.valueOf(resultSet.getString(PlayerEventsFields.PLAYEREVENTTYPE.toString().toLowerCase()));
				if(playerEventType == PlayerEventType.PENALTY_DRAWN || playerEventType == PlayerEventType.PENALTY_TAKEN){
					Infraction infraction = Infraction.valueOf(resultSet.getString(PlayerEventsFields.INFRACTION.toString().toLowerCase()));
					byte minutes = resultSet.getByte(PlayerEventsFields.MINUTES.toString().toLowerCase());
					playerEvent = new Penalty(playerEventId, player, playerEventSnapshot, zone, playerEventType, infraction, minutes);
				}else{
					playerEvent = new PlayerEvent(playerEventId, player, playerEventSnapshot, zone, playerEventType);
				}
				
				if(game.getHomeTeam().equals(shotSnapshotPlayerTeam)){
					shot.getSnapshot().addHomePlayerOnIce(shotSnapshotPlayer);
				}else if(game.getAwayTeam().equals(shotSnapshotPlayerTeam)){
					shot.getSnapshot().addAwayPlayerOnIce(shotSnapshotPlayer);
				}
				if(game.getHomeTeam().equals(timeEventSnapshotPlayerTeam)){
					timeEvent.getSnapshot().addHomePlayerOnIce(timeEventSnapshotPlayer);
				}else if(game.getAwayTeam().equals(timeEventSnapshotPlayerTeam)){
					timeEvent.getSnapshot().addAwayPlayerOnIce(timeEventSnapshotPlayer);
				}
				if(game.getHomeTeam().equals(playerEventSnapshotPlayerTeam)){
					playerEvent.getSnapshot().addHomePlayerOnIce(playerEventSnapshotPlayer);
				}else if(game.getAwayTeam().equals(playerEventSnapshotPlayer)){
					playerEvent.getSnapshot().addAwayPlayerOnIce(playerEventSnapshotPlayer);
				}
			}
		}
		shots.add(shot);
		playerEvents.add(playerEvent);
		timeEvents.add(timeEvent);
		game.setShots(shots.toArray(new Shot[shots.size()]));
		game.setPlayerEvents(playerEvents.toArray(new PlayerEvent[playerEvents.size()]));
		game.setTimeEvents(timeEvents.toArray(new TimeEvent[timeEvents.size()]));
		games.add(game);
		
		return games.toArray(new Game[games.size()]);
	}
}
