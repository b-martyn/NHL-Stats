package connection;

import java.sql.SQLException;

public interface SnapshotConnector extends TableConnector{
	Snapshot[] getSnapshots() throws SQLException;
	Snapshot[] getSnapshots(Game game) throws SQLException;
	Snapshot getSnapshot(int id) throws SQLException;
}
