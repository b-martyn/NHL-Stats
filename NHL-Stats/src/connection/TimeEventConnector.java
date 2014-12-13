package connection;

import java.sql.SQLException;

import connection.TimeEvent.TimeEventType;

public interface TimeEventConnector extends TableConnector{
	TimeEvent[] getTimeEvents() throws SQLException;
	TimeEvent[] getTimeEvents(Game game) throws SQLException;
	TimeEvent[] getTimeEvents(TimeEventType type) throws SQLException;
	TimeEvent getTimeEvent(int id) throws SQLException;
}
