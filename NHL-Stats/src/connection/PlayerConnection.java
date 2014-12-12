package connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.DbConnector.Table;
import connection.Player.Position;
import connection.RowSetInstructions.Comparator;
import connection.RowSetInstructions.Filter;

public class PlayerConnection implements PlayerConnector {
	
	private static PlayerConnection instance = new PlayerConnection();
	private DbConnector connection;
	
	private PlayerConnection(){
		try {
			connection = new DbConnectorFactory().getDbConnector(Table.PLAYERS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static PlayerConnection getInstance() {
		return instance;
	}
	
	@Override
	public RowSetInstructions getDefaultInstructions(){
		RowSetInstructions instructions = new RowSetInstructions(Table.ROSTERS);
		return instructions;
	}
	
	@Override
	public ResultSet getLoadedResultSet() throws SQLException{
		return connection.getResultSet(getDefaultInstructions());
	}
	@Override
	public Player[] getPlayers() throws SQLException {
		return convertPlayers(getLoadedResultSet());
	}

	@Override
	public Player getPlayer(int id) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(PlayersFields.PLAYERID, Comparator.EQUAL, id);
		instructions.addFilter(filter);
		try{
			return convertPlayers(connection.getResultSet(instructions))[0];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}

	@Override
	public Player getPlayer(String name) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(PlayersFields.FIRSTNAME, Comparator.EQUAL, name);
		filter.filterAdditionField(PlayersFields.LASTNAME, Comparator.EQUAL, name, false);
		instructions.addFilter(filter);
		try{
			return convertPlayers(connection.getResultSet(instructions))[0];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}

	@Override
	public Player getPlayer(String firstName, String lastName) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(PlayersFields.FIRSTNAME, Comparator.EQUAL, firstName);
		filter.filterAdditionField(PlayersFields.LASTNAME, Comparator.EQUAL, lastName, true);
		instructions.addFilter(filter);
		try{
			return convertPlayers(connection.getResultSet(instructions))[0];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	private Player[] convertPlayers(ResultSet resultSet) throws SQLException{
		List<Player> players = new ArrayList<Player>();
		
		resultSet.beforeFirst();
		while(resultSet.next()){
			int id = resultSet.getInt(PlayersFields.PLAYERID.toString().toLowerCase());
			String firstName = resultSet.getString(PlayersFields.FIRSTNAME.toString().toLowerCase());
			String lastName = resultSet.getString(PlayersFields.LASTNAME.toString().toLowerCase());
			Position position = Position.valueOf(resultSet.getString(PlayersFields.POSITION.toString().toLowerCase()));
			players.add(new Player(id, firstName, lastName, position));
		}
		return players.toArray(new Player[players.size()]);
	}
	
}
