package connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.DbConnection.Table;
import connection.DbRowSetInstructions.Comparator;
import connection.TimeEvent.TimeEventType;

public class TimeEventConnection implements TimeEventConnector {
	
	private static TimeEventConnection instance = new TimeEventConnection();
	private DbConnector connection;
	
	protected TimeEventConnection(){
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
	public TimeEvent[] getTimeEvents() throws SQLException {
		return convertTimeEvents(connection.getResultSet());
	}

	@Override
	public TimeEvent[] getTimeEvents(Game game) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.TIMEEVENTS);
		instructions.addJoiningTable(Table.SNAPSHOTS, SnapshotsFields.ID, TimeEventsFields.SNAPSHOTID);
		instructions.addNewFilterCriteria(SnapshotsFields.GAMEID, Comparator.EQUAL, game.getId());
		return convertTimeEvents(connection.getResultSet(instructions));
	}

	@Override
	public TimeEvent[] getTimeEvents(TimeEventType type) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.TIMEEVENTS);
		instructions.addNewFilterCriteria(TimeEventsFields.TIMEEVENTTYPE, Comparator.EQUAL, type.toString());
		return convertTimeEvents(connection.getResultSet(instructions));
	}

	@Override
	public TimeEvent getTimeEvent(int id) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.TIMEEVENTS);
		instructions.addNewFilterCriteria(TimeEventsFields.ID, Comparator.EQUAL, id);
		try{
			return convertTimeEvents(connection.getResultSet(instructions))[0];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	private TimeEvent[] convertTimeEvents(ResultSet resultSet) throws SQLException{
		List<TimeEvent> timeEvents = new ArrayList<TimeEvent>();
		
		resultSet.beforeFirst();
		while(resultSet.next()){
			int id = resultSet.getInt("id");
			boolean starting = resultSet.getBoolean("startingclock");
			Snapshot snapshot = SnapshotConnection.getInstance().getSnapshot(resultSet.getInt("snapshotId"));
			TimeEventType type = TimeEventType.valueOf(resultSet.getString("timeeventtype"));
			timeEvents.add(new TimeEvent(id, starting, snapshot, type));
		}
		return timeEvents.toArray(new TimeEvent[timeEvents.size()]);
	}
}
