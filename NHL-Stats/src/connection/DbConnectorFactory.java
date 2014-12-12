package connection;

import java.sql.SQLException;

import connection.DbConnector.Table;

public class DbConnectorFactory {
	public DbConnector getDbConnector(Table table) throws SQLException{
		DbConnection connection = new DbConnection(table);
		return connection;
	}
}
