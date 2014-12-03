package connection;

import java.sql.SQLException;

import connection.PlayerEvent.PlayerEventType;

interface PlayerEventConnector {
	PlayerEvent[] getPlayerEvents() throws SQLException;
	PlayerEvent[] getPlayerEvents(Player player) throws SQLException;
	PlayerEvent[] getPlayerEvents(Game game) throws SQLException;
	PlayerEvent[] getPlayerEvents(PlayerEventType type) throws SQLException;
	PlayerEvent getPlayerEvent(int id) throws SQLException;
}