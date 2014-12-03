package connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.DbConnection.Table;
import connection.DbRowSetInstructions.Comparator;
import connection.MissedShot.MissedLocation;
import connection.Shot.ShotType;

public class ShotConnection implements ShotConnector {
	
	private static ShotConnection instance = new ShotConnection();
	private DbConnector connection;
	
	private ShotConnection(){
		try {
			connection = new DbConnectorFactory().getDbConnector(Table.SHOTS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ShotConnection getInstance() {
		return instance;
	}
	
	@Override
	public Shot[] getShots() throws SQLException {
		return convertShots(connection.getResultSet());
	}

	@Override
	public Shot[] getShots(Player player) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.SHOTS);
		instructions.addNewFilterCriteria(ShotsFields.FIRSTPLAYERID, Comparator.EQUAL, player.getId());
		instructions.addNewConditionCriteria(ShotsFields.SECONDPLAYERID, Comparator.EQUAL, player.getId(), false);
		instructions.addNewConditionCriteria(ShotsFields.THIRDPLAYERID, Comparator.EQUAL, player.getId(), false);
		return convertShots(connection.getResultSet(instructions));
	}

	@Override
	public Shot[] getShots(Game game) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.SHOTS);
		instructions.addJoiningTable(Table.SNAPSHOTS, SnapshotsFields.ID, ShotsFields.SNAPSHOTID);
		instructions.addNewFilterCriteria(SnapshotsFields.GAMEID, Comparator.EQUAL, game.getId());
		return convertShots(connection.getResultSet(instructions));
	}

	@Override
	public Shot[] getShots(ShotType type) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.SHOTS);
		instructions.addNewFilterCriteria(ShotsFields.SHOTTYPE, Comparator.EQUAL, type.toString());
		return convertShots(connection.getResultSet(instructions));
	}

	@Override
	public Shot getShot(int id) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.SHOTS);
		instructions.addNewFilterCriteria(ShotsFields.ID, Comparator.EQUAL, id);
		try{
			return convertShots(connection.getResultSet(instructions))[0];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	private Shot[] convertShots(ResultSet resultSet) throws SQLException{
		List<Shot> shots = new ArrayList<Shot>();
		
		resultSet.beforeFirst();
		while(resultSet.next()){
			int id = resultSet.getInt("id");
			Snapshot snapshot = SnapshotConnection.getInstance().getSnapshot(resultSet.getInt("snapshotid"));
			ShotType shotType = ShotType.valueOf(resultSet.getString("shottype"));
			byte distance = resultSet.getByte("distance");
			Player player = PlayerConnection.getInstance().getPlayer(resultSet.getInt("firstplayerid"));
			Player player2 = PlayerConnection.getInstance().getPlayer(resultSet.getInt("secondplayerid"));
			Player player3 = PlayerConnection.getInstance().getPlayer(resultSet.getInt("thirdplayerid"));
			String missedLocationString = resultSet.getString("missedshotlocation");
			MissedLocation missedLocation = null;
			if(missedLocationString != null){
				missedLocation = MissedLocation.valueOf(missedLocationString);
			}
			boolean goal = resultSet.getBoolean("goal");
			if(goal){
				if(player3 != null){
					shots.add(new Goal(id, snapshot, player, shotType, distance, player2, player3));
				}else if(player2 != null){
					shots.add(new Goal(id, snapshot, player, shotType, distance, player2));
				}else{
					shots.add(new Goal(id, snapshot, player, shotType, distance));
				}
			}else if(missedLocation != null){
				shots.add(new MissedShot(id, snapshot, player, shotType, distance, missedLocation));
			}else if(player2 != null){
				shots.add(new BlockedShot(id, snapshot, player, shotType, distance, player2));
			}else{
				shots.add(new Shot(id, snapshot, player, shotType, distance));
			}
		}
		return shots.toArray(new Shot[shots.size()]);
	}
}
