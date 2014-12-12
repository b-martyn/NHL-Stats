package connection;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DbConnector {
	public enum Table{
		GAMES(6), PLAYERS(5), SNAPSHOTS(5), SNAPSHOTPLAYERS(2), TIMEEVENTS(4), SHOTS(9), SHOTPLAYERS(2), PLAYEREVENTS(7), ROSTERS(4), ROSTERPLAYERS(3);
		
		private int size;
		
		private Table(int size){
			this.size = size;
		}
		
		public int getSize(){
			return size;
		}
	}
	
	ResultSet getBaseResultSet() throws SQLException;
	ResultSet getResultSet(RowSetInstructions instructions) throws SQLException;
	int[] getMatchingColumns(ResultSet resultSet, TableField tableField) throws SQLException;
}
