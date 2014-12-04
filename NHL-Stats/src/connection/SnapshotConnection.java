package connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.DbConnection.Table;
import connection.DbRowSetInstructions.Comparator;
import connection.Franchise.TeamName;

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
	public Snapshot[] getSnapshots() throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.SNAPSHOTS);
		instructions.addJoiningTable(Table.GAMES, GamesFields.ID, SnapshotsFields.GAMEID);
		return convertSnapshots(connection.getResultSet(instructions));
	}

	@Override
	public Snapshot[] getSnapshots(Game game) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.SNAPSHOTS);
		instructions.addJoiningTable(Table.GAMES, GamesFields.ID, SnapshotsFields.GAMEID);
		instructions.addNewFilterCriteria(SnapshotsFields.GAMEID, Comparator.EQUAL, game.getId());
		return convertSnapshots(connection.getResultSet(instructions));
	}

	@Override
	public Snapshot getSnapshot(int id) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.SNAPSHOTS);
		instructions.addJoiningTable(Table.GAMES, GamesFields.ID, SnapshotsFields.GAMEID);
		instructions.addNewFilterCriteria(SnapshotsFields.ID, Comparator.EQUAL, id);
		try{
			return convertSnapshots(connection.getResultSet(instructions))[0];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	private Snapshot[] convertSnapshots(ResultSet resultSet) throws SQLException{
		List<Snapshot> snapshots = new ArrayList<Snapshot>();
		resultSet.beforeFirst();
		while(resultSet.next()){
			int id = resultSet.getInt(SnapshotsFields.ID.toString().toLowerCase());
			int gameId = resultSet.getInt(SnapshotsFields.GAMEID.toString().toLowerCase());
			byte period = resultSet.getByte(SnapshotsFields.PERIOD.toString().toLowerCase());
			short elapsedSeconds = resultSet.getShort(SnapshotsFields.ELAPSEDSECONDS.toString().toLowerCase());
			short secondsLeft = resultSet.getShort(SnapshotsFields.SECONDSLEFT.toString().toLowerCase());
			TimeStamp timeStamp = new TimeStamp(period, elapsedSeconds, secondsLeft);
			Player[] homePlayersOnIce = convertPlayers(resultSet.getString(SnapshotsFields.HOMEPLAYERSONICE.toString().toLowerCase()), TeamName.valueOf(resultSet.getString(GamesFields.HOMETEAM.toString().toLowerCase())));
			Player[] awayPlayersOnIce = convertPlayers(resultSet.getString(SnapshotsFields.AWAYPLAYERSONICE.toString().toLowerCase()), TeamName.valueOf(resultSet.getString(GamesFields.AWAYTEAM.toString().toLowerCase())));
			snapshots.add(new Snapshot(id, gameId, timeStamp, homePlayersOnIce, awayPlayersOnIce));
		}
		return snapshots.toArray(new Snapshot[snapshots.size()]);
	}
	
	private Player[] convertPlayers(String string, TeamName teamName) throws SQLException {
		List<Player> players = new ArrayList<Player>();
		
		String[] playerIds = string.split(" ");
		for(String id : playerIds){
			players.add(PlayerConnection.getInstance().getPlayer(Byte.valueOf(id.trim()), teamName));
		}
		return players.toArray(new Player[players.size()]);
	}
}
