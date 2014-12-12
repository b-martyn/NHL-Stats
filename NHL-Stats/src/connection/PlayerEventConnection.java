package connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.DbConnection.Table;
import connection.RowSetInstructions.Comparator;
import connection.Franchise.TeamName;
import connection.Penalty.Infraction;
import connection.Player.Position;
import connection.PlayerEvent.PlayerEventType;
import connection.RowSetInstructions.Filter;

public class PlayerEventConnection implements PlayerEventConnector {
	
	private static PlayerEventConnection instance = new PlayerEventConnection();
	private DbConnector connection;
	
	private PlayerEventConnection(){
		try {
			connection = new DbConnectorFactory().getDbConnector(Table.PLAYEREVENTS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static PlayerEventConnection getInstance() {
		return instance;
	}

	@Override
	public RowSetInstructions getDefaultInstructions() {
		RowSetInstructions instructions = new RowSetInstructions(Table.PLAYEREVENTS);
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.PLAYERS, PlayersFields.PLAYERID, PlayerEventsFields.PLAYERID));
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.SNAPSHOTS, SnapshotsFields.SNAPSHOTID, PlayerEventsFields.SNAPSHOTID));
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
	public PlayerEvent[] getPlayerEvents() throws SQLException {
		return convertPlayerEvents(getLoadedResultSet());
	}

	@Override
	public PlayerEvent[] getPlayerEvents(Player player) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(PlayerEventsFields.PLAYERID, Comparator.EQUAL, player.getId());
		instructions.addFilter(filter);
		filter = instructions.new Filter(PlayersFields.FIRSTNAME, Comparator.EQUAL, player.getFirstName());
		filter.filterAdditionField(PlayersFields.LASTNAME, Comparator.EQUAL, player.getLastName(), true);
		instructions.addFilter(filter);
		return convertPlayerEvents(connection.getResultSet(instructions));
	}

	@Override
	public PlayerEvent[] getPlayerEvents(Game game) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(SnapshotsFields.GAMEID, Comparator.EQUAL, game.getId());
		instructions.addFilter(filter);
		filter = instructions.new Filter(GamesFields.DATE, Comparator.EQUAL, new java.sql.Date(game.getDate().getTime()));
		filter.filterAdditionField(GamesFields.HOMETEAM, Comparator.EQUAL, game.getHomeTeam().toString(), true);
		filter.filterAdditionField(GamesFields.AWAYTEAM, Comparator.EQUAL, game.getAwayTeam().toString(), false);
		instructions.addFilter(filter);
		return convertPlayerEvents(connection.getResultSet(instructions));
	}

	@Override
	public PlayerEvent[] getPlayerEvents(PlayerEventType type) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(PlayerEventsFields.PLAYEREVENTTYPE, Comparator.EQUAL, type.toString());
		instructions.addFilter(filter);
		return convertPlayerEvents(connection.getResultSet(instructions));
	}

	@Override
	public PlayerEvent getPlayerEvent(int id) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(PlayerEventsFields.PLAYEREVENTID, Comparator.EQUAL, id);
		instructions.addFilter(filter);
		try{
			return convertPlayerEvents(connection.getResultSet(instructions))[0];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}

	
	private PlayerEvent[] convertPlayerEvents(ResultSet resultSet) throws SQLException{
		List<PlayerEvent> playerEvents = new ArrayList<PlayerEvent>();
		PlayerEvent playerEvent = new PlayerEvent();
		int[] firstNameFields = connection.getMatchingColumns(resultSet, PlayersFields.FIRSTNAME);
		int[] lastNameFields = connection.getMatchingColumns(resultSet, PlayersFields.LASTNAME);
		int[] positionFields = connection.getMatchingColumns(resultSet, PlayersFields.POSITION);
		
		resultSet.beforeFirst();
		while(resultSet.next()){
			int snapshotPlayerId = resultSet.getInt(SnapshotPlayersFields.SNAPSHOTPLAYERID.toString().toLowerCase());
			String snapshotFirstName = resultSet.getString(firstNameFields[1]);
			String snapshotLastName = resultSet.getString(lastNameFields[1]);
			Position snapshotPosition = Position.valueOf(resultSet.getString(positionFields[1]));
			
			TeamName homeTeam = TeamName.valueOf(resultSet.getString(GamesFields.HOMETEAM.toString().toLowerCase()));
			TeamName awayTeam = TeamName.valueOf(resultSet.getString(GamesFields.AWAYTEAM.toString().toLowerCase()));
			
			if((resultSet.getInt(PlayerEventsFields.PLAYEREVENTID.toString().toLowerCase())) == playerEvent.getId()){
				
				if(homeTeam.equals(TeamName.valueOf(resultSet.getString(RostersFields.TEAM.toString().toLowerCase())))){
					playerEvent.getSnapshot().addHomePlayerOnIce(new Player(snapshotPlayerId, snapshotFirstName, snapshotLastName, snapshotPosition));
				}else if(awayTeam.equals(TeamName.valueOf(resultSet.getString(RostersFields.TEAM.toString().toLowerCase())))){
					playerEvent.getSnapshot().addAwayPlayerOnIce(new Player(snapshotPlayerId, snapshotFirstName, snapshotLastName, snapshotPosition));
				}
			}else{
				if(resultSet.getRow() != 1){
					playerEvents.add(playerEvent);
				}
				
				int snapshotId = resultSet.getInt(SnapshotsFields.SNAPSHOTID.toString().toLowerCase());
				int gameId = resultSet.getInt(GamesFields.GAMEID.toString().toLowerCase());
				byte period = resultSet.getByte(SnapshotsFields.PERIOD.toString().toLowerCase());
				short elapsedSeconds = resultSet.getShort(SnapshotsFields.ELAPSEDSECONDS.toString().toLowerCase());
				short secondsLeft = resultSet.getShort(SnapshotsFields.SECONDSLEFT.toString().toLowerCase());
				int playerEventId = resultSet.getInt(PlayerEventsFields.PLAYEREVENTID.toString().toLowerCase());
				int playerEventPlayerId = resultSet.getInt(PlayerEventsFields.PLAYERID.toString().toLowerCase());
				String playerEventFirstName = resultSet.getString(firstNameFields[0]);
				String playerEventLastName = resultSet.getString(lastNameFields[0]);
				Position playerEventPosition = Position.valueOf(resultSet.getString(positionFields[0]));
				Zone zone = Zone.valueOf(resultSet.getString(PlayerEventsFields.ZONE.toString().toLowerCase()));
				Player player = new Player(playerEventPlayerId, playerEventFirstName, playerEventLastName, playerEventPosition);
				PlayerEventType type = PlayerEventType.valueOf(resultSet.getString(PlayerEventsFields.PLAYEREVENTTYPE.toString().toLowerCase()));
				if(type == PlayerEventType.PENALTY_DRAWN || type == PlayerEventType.PENALTY_TAKEN){
					Infraction infraction = Infraction.valueOf(resultSet.getString(PlayerEventsFields.INFRACTION.toString().toLowerCase()));
					byte minutes = resultSet.getByte(PlayerEventsFields.MINUTES.toString().toLowerCase());
					playerEvent = new Penalty(playerEventId, player, 
									new Snapshot(snapshotId, gameId, 
									new TimeStamp(period, elapsedSeconds, secondsLeft)), 
									zone, type, infraction, minutes);
				}else{
					playerEvent = new PlayerEvent(playerEventId, player, 
									new Snapshot(snapshotId, gameId, 
									new TimeStamp(period, elapsedSeconds, secondsLeft)), 
									zone, type);
				}
				
				if(homeTeam.equals(TeamName.valueOf(resultSet.getString(RostersFields.TEAM.toString().toLowerCase())))){
					playerEvent.getSnapshot().addHomePlayerOnIce(new Player(snapshotPlayerId, snapshotFirstName, snapshotLastName, snapshotPosition));
				}else if(awayTeam.equals(TeamName.valueOf(resultSet.getString(RostersFields.TEAM.toString().toLowerCase())))){
					playerEvent.getSnapshot().addAwayPlayerOnIce(new Player(snapshotPlayerId, snapshotFirstName, snapshotLastName, snapshotPosition));
				}
			}
		}
		playerEvents.add(playerEvent);
		
		return playerEvents.toArray(new PlayerEvent[playerEvents.size()]);
	}
}
