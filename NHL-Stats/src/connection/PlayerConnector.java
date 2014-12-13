package connection;

import java.sql.SQLException;

public interface PlayerConnector extends TableConnector{
	Player[] getPlayers() throws SQLException;
	Player getPlayer(int id) throws SQLException;
	Player getPlayer(String name) throws SQLException;
	Player getPlayer(String firstName, String lastName) throws SQLException;
}
