package connection;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.FilteredRowSet;
import javax.sql.rowset.JoinRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

final class DbConnection implements DbConnector {
	
	public enum Table{
		GAMES, PLAYERS, SNAPSHOTS, TIMEEVENTS, SHOTS, PLAYEREVENTS;
	}
	
	// TODO add db information
	private static final String USER_NAME = "";
	private static final String PASSWORD = "";
	private static final String URL_STRING = "jdbc:mysql://173.48.157.224:3306/nhl";
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
	public ResultSet getResultSet() throws SQLException {
		return cachedRowSet;
	}
	
	@Override
	public ResultSet getResultSet(DbRowSetInstructions instructions) throws SQLException{
		Object[][] joiningTablesInstructions = instructions.getJoiningTables();
		RowSetFactory rowSetFactory = RowSetProvider.newFactory();
		JoinRowSet jrs = rowSetFactory.createJoinRowSet();
		if(joiningTablesInstructions.length > 0){
			Table[] tables = new Table[joiningTablesInstructions.length];
			TableField[] newTableField = new TableField[joiningTablesInstructions.length];
			TableField[] currentTableField = new TableField[joiningTablesInstructions.length];
			for(int i = 0; i < joiningTablesInstructions.length; i++){
				tables[i] = (Table)joiningTablesInstructions[i][0];
				newTableField[i] = (TableField)joiningTablesInstructions[i][1];
				currentTableField[i] = (TableField)joiningTablesInstructions[i][2];
			}
			jrs.addRowSet(cachedRowSet, currentTableField[0].toString().toLowerCase());
			jrs.addRowSet((CachedRowSet)new DbConnection(tables[0]).getResultSet(), newTableField[0].toString().toLowerCase());
			for(int i = 1; i < tables.length; i++){
				JoinRowSet newJrs = rowSetFactory.createJoinRowSet();
				newJrs.addRowSet(jrs, currentTableField[i].toString().toLowerCase());
				newJrs.addRowSet((CachedRowSet)new DbConnection(tables[i]).getResultSet(), newTableField[i].toString().toLowerCase());
				jrs = newJrs;
			}
		}else{
			jrs.addRowSet(cachedRowSet, "id");
		}
		FilteredRowSet frs = rowSetFactory.createFilteredRowSet();
		jrs.beforeFirst();
		frs.populate(jrs);
		frs.setFilter(instructions.getMyPredicate());
		return frs;
	}
}