package connection;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DbConnector {
	ResultSet getBaseResultSet() throws SQLException;
	ResultSet getResultSet(RowSetInstructions instructions) throws SQLException;
	int[] getMatchingColumns(ResultSet resultSet, TableField tableField) throws SQLException;
}
