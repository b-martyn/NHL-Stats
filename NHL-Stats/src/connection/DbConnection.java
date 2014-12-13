package connection;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.FilteredRowSet;
import javax.sql.rowset.JoinRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import connection.RowSetInstructions.JoiningResultSet;

final class DbConnection implements DbConnector {
	/*
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
	*/
	// TODO add db information
	private static final String USER_NAME = "";
	private static final String PASSWORD = "";
	private static final String URL_STRING = "";
	private CachedRowSet cachedRowSet;
	
	DbConnection(Table table) throws SQLException {
		cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.setUrl(URL_STRING);
		cachedRowSet.setUsername(USER_NAME);
		cachedRowSet.setPassword(PASSWORD);
		cachedRowSet.setCommand("SELECT * FROM " + table.toString().toLowerCase());
		cachedRowSet.execute();
	}
	
	@Override
	public ResultSet getBaseResultSet() throws SQLException {
		return cachedRowSet;
	}
	
	@Override
	public ResultSet getResultSet(RowSetInstructions instructions) throws SQLException{
		RowSetFactory rowSetFactory = RowSetProvider.newFactory();
		
		StringBuilder tables = new StringBuilder();
		JoiningResultSet[] joiningResultSets = instructions.getJoiningResultSets();
		JoinRowSet jrs = rowSetFactory.createJoinRowSet();
		//joins the first field in cachedRowSet for this DbConnection (is the id field)
		jrs.addRowSet(cachedRowSet, 1);
		if(cachedRowSet.getMetaData().getTableName(1).equalsIgnoreCase(instructions.getMainTable().toString())){
			tables.append(instructions.getMainTable() + ",");
			if(joiningResultSets.length > 0){
				for(JoiningResultSet joiningResultSet : joiningResultSets){
					if(!joiningResultSet.isJoiningResultSet()){
						JoinRowSet tempJRS = rowSetFactory.createJoinRowSet();
						String tableMatchingField = joiningResultSet.getTableMatchingField();
						String resultSetMatchingField = joiningResultSet.getResultSetMatchField();
						tempJRS.addRowSet(jrs, resultSetMatchingField);
						Table joiningTable = (Table)joiningResultSet.getJoining();
						tables.append(joiningTable + ",");
						tempJRS.addRowSet((CachedRowSet)new DbConnection(joiningTable).getBaseResultSet(), tableMatchingField);
						jrs = tempJRS;
					}
				}
				for(JoiningResultSet joiningResultSet : joiningResultSets){
					if(joiningResultSet.isJoiningResultSet()){
						CachedRowSet rowSet = (CachedRowSet)joiningResultSet.getJoining();
						tables.append(rowSet.getTableName() + ",");
						String tableMatchingField = joiningResultSet.getTableMatchingField();
						String resultSetMatchingField = joiningResultSet.getResultSetMatchField();
						if(joiningResultSet.isJoinAfter()){
							jrs.addRowSet(rowSet, tableMatchingField);
						}else{
							JoinRowSet tempJRS = rowSetFactory.createJoinRowSet();
							tempJRS.addRowSet(rowSet, tableMatchingField);
							tempJRS.addRowSet(jrs, resultSetMatchingField);
							jrs = tempJRS;
						}
					}
				}
			}
		}else{
			throw new IllegalArgumentException("This RowSetInstructions for table: " + instructions.getMainTable().toString() + " does not match this DbConnection for table: " + cachedRowSet.getMetaData().getTableName(1));
		}
		
		FilteredRowSet frs = rowSetFactory.createFilteredRowSet();
		jrs.beforeFirst();
		frs.populate(jrs);
		frs.setFilter(instructions.getMyPredicate());
		frs.setTableName(tables.toString());
		return frs;
	}
	
	@Override
	public int[] getMatchingColumns(ResultSet resultSet, TableField tableField) throws SQLException{
		int[] matchingColumns = null;
		for(int i = 0; i < resultSet.getMetaData().getColumnCount(); i++){
			if(resultSet.getMetaData().getColumnName(i + 1).equalsIgnoreCase(tableField.toString())){
				if(matchingColumns != null){
					int[] newArray = new int[matchingColumns.length + 1];
					for(int j = 0; j < matchingColumns.length; j++){
						newArray[j] = matchingColumns[j];
					}
					newArray[newArray.length - 1] = (i + 1);
					matchingColumns = newArray;
				}else{
					matchingColumns = new int[]{i + 1};
				}
			}
		}
		return matchingColumns;
	}
}