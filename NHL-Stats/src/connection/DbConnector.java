package connection;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DbConnector {
	ResultSet getResultSet() throws SQLException;
	ResultSet getResultSet(DbRowSetInstructions instructions) throws SQLException;
}
