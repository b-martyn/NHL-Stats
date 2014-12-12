package connection;

import java.sql.SQLException;
import java.util.Date;

import connection.Franchise.TeamName;

public interface RostersConnector extends TableConnector{
	Roster[] getRosters() throws SQLException;
	Roster[] getRosters(Game game) throws SQLException;
	Roster[] getRosters(TeamName teamName) throws SQLException;
	Roster getRoster(TeamName teamName, Date date) throws SQLException;
}

