package connection;

import java.sql.SQLException;

import connection.Franchise.TeamName;

interface PlayerConnector {
	Player[] getPlayers() throws SQLException;
	Player[] getPlayers(TeamName teamName) throws SQLException;
	Player getPlayer(int id) throws SQLException;
	Player getPlayer(byte number, TeamName teamName) throws SQLException;
}
