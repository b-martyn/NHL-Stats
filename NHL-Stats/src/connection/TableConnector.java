package connection;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface TableConnector {
	public RowSetInstructions getDefaultInstructions();
	public ResultSet getLoadedResultSet() throws SQLException;
}
