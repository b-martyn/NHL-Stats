package connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.DbConnection.Table;
import connection.DbRowSetInstructions.Comparator;
import connection.PlayerEvent.PlayerEventType;

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
	public PlayerEvent[] getPlayerEvents() throws SQLException {
		return convertPlayerEvents(connection.getResultSet());
	}

	@Override
	public PlayerEvent[] getPlayerEvents(Player player) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.PLAYEREVENTS);
		instructions.addNewFilterCriteria(PlayerEventsFields.PLAYERID, Comparator.EQUAL, player.getId());
		return convertPlayerEvents(connection.getResultSet(instructions));
	}

	@Override
	public PlayerEvent[] getPlayerEvents(Game game) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.PLAYEREVENTS);
		instructions.addJoiningTable(Table.SNAPSHOTS, SnapshotsFields.ID, PlayerEventsFields.SNAPSHOTID);
		instructions.addNewFilterCriteria(SnapshotsFields.GAMEID, Comparator.EQUAL, game.getId());
		return convertPlayerEvents(connection.getResultSet(instructions));
	}

	@Override
	public PlayerEvent[] getPlayerEvents(PlayerEventType type) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.PLAYEREVENTS);
		instructions.addNewFilterCriteria(PlayerEventsFields.PLAYEREVENTTYPE, Comparator.EQUAL, type.toString());
		return convertPlayerEvents(connection.getResultSet(instructions));
	}

	@Override
	public PlayerEvent getPlayerEvent(int id) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.PLAYEREVENTS);
		instructions.addNewFilterCriteria(PlayerEventsFields.ID, Comparator.EQUAL, id);
		try{
			return convertPlayerEvents(connection.getResultSet(instructions))[0];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}

	
	private PlayerEvent[] convertPlayerEvents(ResultSet resultSet) throws SQLException{
		List<PlayerEvent> playerEvents = new ArrayList<PlayerEvent>();
		
		resultSet.beforeFirst();
		while(resultSet.next()){
			int id = resultSet.getInt("id");
			Player player = PlayerConnection.getInstance().getPlayer(resultSet.getInt("playerid"));
			Snapshot snapshot = SnapshotConnection.getInstance().getSnapshot(resultSet.getInt("snapshotid"));
			Zone zone = Zone.valueOf(resultSet.getString("zone"));
			PlayerEventType type = PlayerEventType.valueOf(resultSet.getString("playereventtype"));
			playerEvents.add(new PlayerEvent(id, player, snapshot, zone, type));
		}
		return playerEvents.toArray(new PlayerEvent[playerEvents.size()]);
	}
}
