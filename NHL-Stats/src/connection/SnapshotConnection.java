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
		return convertSnapshots(connection.getResultSet());
	}

	@Override
	public Snapshot[] getSnapshots(Game game) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.SNAPSHOTS);
		instructions.addNewFilterCriteria(SnapshotsFields.GAMEID, Comparator.EQUAL, game.getId());
		return convertSnapshots(connection.getResultSet(instructions));
	}

	@Override
	public Snapshot getSnapshot(int id) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.SNAPSHOTS);
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
			int id = resultSet.getInt("id");
			Game game = GameConnection.getInstance().getGame(resultSet.getInt("gameid"));
			int gameId = game.getId();
			byte period = resultSet.getByte("period");
			short elapsedSeconds = resultSet.getShort("elapsedseconds");
			short secondsLeft = resultSet.getShort("secondsleft");
			TimeStamp timeStamp = new TimeStamp(period, elapsedSeconds, secondsLeft);
			Player[] homePlayersOnIce = convertPlayers(resultSet.getString("homeplayersonice"), game.getHomeTeam());
			Player[] awayPlayersOnIce = convertPlayers(resultSet.getString("awayplayersonice"), game.getAwayTeam());
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
		return null;
	}
}
