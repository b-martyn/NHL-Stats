package connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.DbConnection.Table;
import connection.Franchise.TeamName;
import connection.Player.Position;
import connection.RowSetInstructions.Comparator;
import connection.RowSetInstructions.Filter;

public class SnapshotConnection implements SnapshotConnector {

	private static SnapshotConnection instance = new SnapshotConnection();
	private DbConnector connection;
	
	private SnapshotConnection(){
		try {
			connection = new DbConnectorFactory().getDbConnector(Table.SNAPSHOTS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static SnapshotConnection getInstance() {
		return instance;
	}
	
	@Override
	public RowSetInstructions getDefaultInstructions(){
		RowSetInstructions instructions = new RowSetInstructions(Table.SNAPSHOTS);
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.GAMES, GamesFields.GAMEID, SnapshotsFields.GAMEID));
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.SNAPSHOTPLAYERS, SnapshotPlayersFields.SNAPSHOTID, SnapshotsFields.SNAPSHOTID));
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.PLAYERS, PlayersFields.PLAYERID, SnapshotPlayersFields.SNAPSHOTPLAYERID));
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.ROSTERPLAYERS, RosterPlayersFields.ROSTERPLAYERID, PlayersFields.PLAYERID));
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.ROSTERS, RostersFields.ROSTERID, RosterPlayersFields.ROSTERID));
		return instructions;
	}
	
	@Override
	public ResultSet getLoadedResultSet() throws SQLException{
		return connection.getResultSet(getDefaultInstructions());
	}
	
	@Override
	public Snapshot[] getSnapshots() throws SQLException {
		return convertSnapshots(getLoadedResultSet());
	}

	@Override
	public Snapshot[] getSnapshots(Game game) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(SnapshotsFields.GAMEID, Comparator.EQUAL, game.getId());
		instructions.addFilter(filter);
		filter = instructions.new Filter(GamesFields.DATE, Comparator.EQUAL, new java.sql.Date(game.getDate().getTime()));
		filter.filterAdditionField(GamesFields.HOMETEAM, Comparator.EQUAL, game.getHomeTeam().toString(), true);
		filter.filterAdditionField(GamesFields.AWAYTEAM, Comparator.EQUAL, game.getAwayTeam().toString(), false);
		instructions.addFilter(filter);
		return convertSnapshots(connection.getResultSet(instructions));
	}

	@Override
	public Snapshot getSnapshot(int id) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(SnapshotsFields.SNAPSHOTID, Comparator.EQUAL, id);
		instructions.addFilter(filter);
		try{
			return convertSnapshots(connection.getResultSet(instructions))[0];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	private Snapshot[] convertSnapshots(ResultSet resultSet) throws SQLException{
		List<Snapshot> snapshots = new ArrayList<Snapshot>();
		Snapshot snapshot = new Snapshot();
		int snapshotId = 0;
		resultSet.beforeFirst();
		while(resultSet.next()){
			//Always more snapshotPlayers than any other field
			int playerId = resultSet.getInt(SnapshotPlayersFields.SNAPSHOTPLAYERID.toString().toLowerCase());
			String firstName = resultSet.getString(PlayersFields.FIRSTNAME.toString().toLowerCase());
			String lastName = resultSet.getString(PlayersFields.LASTNAME.toString().toLowerCase());
			Position position = Position.valueOf(resultSet.getString(PlayersFields.POSITION.toString().toLowerCase()));
			
			TeamName homeTeam = TeamName.valueOf(resultSet.getString(GamesFields.HOMETEAM.toString().toLowerCase()));
			
			if((resultSet.getInt(SnapshotsFields.SNAPSHOTID.toString().toLowerCase())) == snapshotId){
				if(homeTeam.equals(TeamName.valueOf(resultSet.getString(RostersFields.TEAM.toString().toLowerCase())))){
					snapshot.addHomePlayerOnIce(new Player(playerId, firstName, lastName, position));
				}else{
					snapshot.addAwayPlayerOnIce(new Player(playerId, firstName, lastName, position));
				}
			}else{
				if(resultSet.getRow() != 1){
					snapshots.add(snapshot);
				}
				
				snapshotId = resultSet.getInt(SnapshotsFields.SNAPSHOTID.toString().toLowerCase());
				int gameId = resultSet.getInt(GamesFields.GAMEID.toString().toLowerCase());
				byte period = resultSet.getByte(SnapshotsFields.PERIOD.toString().toLowerCase());
				short elapsedSeconds = resultSet.getShort(SnapshotsFields.ELAPSEDSECONDS.toString().toLowerCase());
				short secondsLeft = resultSet.getShort(SnapshotsFields.SECONDSLEFT.toString().toLowerCase());
				snapshot = new Snapshot(snapshotId, gameId, new TimeStamp(period, elapsedSeconds, secondsLeft));
				if(homeTeam.equals(TeamName.valueOf(resultSet.getString(RostersFields.TEAM.toString().toLowerCase())))){
					snapshot.addHomePlayerOnIce(new Player(playerId, firstName, lastName, position));
				}else{
					snapshot.addAwayPlayerOnIce(new Player(playerId, firstName, lastName, position));
				}
			}
		}
		snapshots.add(snapshot);
		
		return snapshots.toArray(new Snapshot[snapshots.size()]);
	}
}
