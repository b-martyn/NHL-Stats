package connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.DbConnection.Table;
import connection.RowSetInstructions.Comparator;
import connection.Franchise.TeamName;
import connection.Player.Position;
import connection.RowSetInstructions.Filter;
import connection.TimeEvent.TimeEventType;

public class TimeEventConnection implements TimeEventConnector {
	
	private static TimeEventConnection instance = new TimeEventConnection();
	private DbConnector connection;
	
	private TimeEventConnection(){
		try {
			connection = new DbConnectorFactory().getDbConnector(Table.TIMEEVENTS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static TimeEventConnection getInstance() {
		return instance;
	}
	
	@Override
	public RowSetInstructions getDefaultInstructions(){
		RowSetInstructions instructions = new RowSetInstructions(Table.TIMEEVENTS);
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.SNAPSHOTS, SnapshotsFields.SNAPSHOTID, TimeEventsFields.SNAPSHOTID));
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.GAMES, GamesFields.GAMEID, SnapshotsFields.GAMEID));
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.SNAPSHOTPLAYERS,SnapshotPlayersFields.SNAPSHOTID, SnapshotsFields.SNAPSHOTID));
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.PLAYERS,PlayersFields.PLAYERID, SnapshotPlayersFields.SNAPSHOTPLAYERID));
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.ROSTERPLAYERS, RosterPlayersFields.ROSTERPLAYERID, SnapshotPlayersFields.SNAPSHOTPLAYERID));
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.ROSTERS, RostersFields.ROSTERID, RosterPlayersFields.ROSTERID));
		return instructions;
	}
	
	@Override
	public ResultSet getLoadedResultSet() throws SQLException{
		return connection.getResultSet(getDefaultInstructions());
	}
	
	@Override
	public TimeEvent[] getTimeEvents() throws SQLException {
		return convertTimeEvents(getLoadedResultSet());
	}

	@Override
	public TimeEvent[] getTimeEvents(Game game) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(SnapshotsFields.GAMEID, Comparator.EQUAL, game.getId());
		instructions.addFilter(filter);
		filter = instructions.new Filter(GamesFields.DATE, Comparator.EQUAL, new java.sql.Date(game.getDate().getTime()));
		filter.filterAdditionField(GamesFields.HOMETEAM, Comparator.EQUAL, game.getHomeTeam(), true);
		filter.filterAdditionField(GamesFields.AWAYTEAM, Comparator.EQUAL, game.getAwayTeam(), true);
		instructions.addFilter(filter);
		return convertTimeEvents(connection.getResultSet(instructions));
	}

	@Override
	public TimeEvent[] getTimeEvents(TimeEventType type) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(TimeEventsFields.TIMEEVENTTYPE, Comparator.EQUAL, type.toString());
		instructions.addFilter(filter);
		return convertTimeEvents(connection.getResultSet(instructions));
	}

	@Override
	public TimeEvent getTimeEvent(int id) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(TimeEventsFields.TIMEEVENTID, Comparator.EQUAL, id);
		instructions.addFilter(filter);
		try{
			return convertTimeEvents(connection.getResultSet(instructions))[0];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	private TimeEvent[] convertTimeEvents(ResultSet resultSet) throws SQLException{
		List<TimeEvent> timeEvents = new ArrayList<TimeEvent>();
		TimeEvent timeEvent = new TimeEvent();
		resultSet.beforeFirst();
		while(resultSet.next()){
			int playerId = resultSet.getInt(PlayersFields.PLAYERID.toString().toLowerCase());
			String firstName = resultSet.getString(PlayersFields.FIRSTNAME.toString().toLowerCase());
			String lastName = resultSet.getString(PlayersFields.LASTNAME.toString().toLowerCase());
			Position position = Position.valueOf(resultSet.getString(PlayersFields.POSITION.toString().toLowerCase()));
			
			TeamName homeTeam = TeamName.valueOf(resultSet.getString(GamesFields.HOMETEAM.toString().toLowerCase()));
			
			if((resultSet.getInt(SnapshotsFields.SNAPSHOTID.toString().toLowerCase())) == timeEvent.getId()){
				if(homeTeam.equals(TeamName.valueOf(resultSet.getString(RostersFields.TEAM.toString().toLowerCase())))){
					timeEvent.getSnapshot().addHomePlayerOnIce(new Player(playerId, firstName, lastName, position));
				}else{
					timeEvent.getSnapshot().addAwayPlayerOnIce(new Player(playerId, firstName, lastName, position));
				}
			}else{
				if(resultSet.getRow() != 1){
					timeEvents.add(timeEvent);
				}
				
				int snapshotId = resultSet.getInt(SnapshotsFields.SNAPSHOTID.toString().toLowerCase());
				int gameId = resultSet.getInt(GamesFields.GAMEID.toString().toLowerCase());
				byte period = resultSet.getByte(SnapshotsFields.PERIOD.toString().toLowerCase());
				short elapsedSeconds = resultSet.getShort(SnapshotsFields.ELAPSEDSECONDS.toString().toLowerCase());
				short secondsLeft = resultSet.getShort(SnapshotsFields.SECONDSLEFT.toString().toLowerCase());
				
				int timeEventId = resultSet.getInt(TimeEventsFields.TIMEEVENTID.toString().toLowerCase());
				boolean starting = resultSet.getBoolean(TimeEventsFields.STARTINGCLOCK.toString().toLowerCase());
				TimeEventType type = TimeEventType.valueOf(resultSet.getString(TimeEventsFields.TIMEEVENTTYPE.toString().toLowerCase()));
				timeEvent = new TimeEvent(timeEventId, starting, new Snapshot(snapshotId, gameId, new TimeStamp(period, elapsedSeconds, secondsLeft)), type);
				
				if(homeTeam.equals(TeamName.valueOf(resultSet.getString(RostersFields.TEAM.toString().toLowerCase())))){
					timeEvent.getSnapshot().addHomePlayerOnIce(new Player(playerId, firstName, lastName, position));
				}else{
					timeEvent.getSnapshot().addAwayPlayerOnIce(new Player(playerId, firstName, lastName, position));
				}
			}
		}
		timeEvents.add(timeEvent);
		
		return timeEvents.toArray(new TimeEvent[timeEvents.size()]);
	}
}
