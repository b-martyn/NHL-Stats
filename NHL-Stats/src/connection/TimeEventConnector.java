package connection;

import java.sql.SQLException;

import connection.TimeEvent.TimeEventType;

interface TimeEventConnector {
	TimeEvent[] getTimeEvents() throws SQLException;
	TimeEvent[] getTimeEvents(Game game) throws SQLException;
	TimeEvent[] getTimeEvents(TimeEventType type) throws SQLException;
	TimeEvent getTimeEvent(int id) throws SQLException;
}
