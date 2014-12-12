package connection;

import java.sql.SQLException;

import connection.Shot.ShotType;

interface ShotConnector extends TableConnector{
	Shot[] getShots() throws SQLException;
	Shot[] getShots(Player player) throws SQLException;
	Shot[] getShots(Game game) throws SQLException;
	Shot[] getShots(ShotType type) throws SQLException;
	Shot getShot (int id) throws SQLException;
}
