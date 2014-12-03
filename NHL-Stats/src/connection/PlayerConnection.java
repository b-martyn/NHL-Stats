package connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.DbConnection.Table;
import connection.DbRowSetInstructions.Comparator;
import connection.Franchise.TeamName;
import connection.Player.Position;

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
	public Player[] getPlayers() throws SQLException {
		return convertPlayers(connection.getResultSet());
	}

	@Override
	public Player[] getPlayers(TeamName teamName) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.PLAYERS);
		instructions.addNewFilterCriteria(PlayersFields.TEAM, Comparator.EQUAL, teamName.toString());
		return convertPlayers(connection.getResultSet(instructions));
	}

	@Override
	public Player getPlayer(int id) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.PLAYERS);
		instructions.addNewFilterCriteria(PlayersFields.ID, Comparator.EQUAL, id);
		try{
			return convertPlayers(connection.getResultSet(instructions))[0];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}

	@Override
	public Player getPlayer(byte number, TeamName teamName) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.PLAYERS);
		instructions.addNewFilterCriteria(PlayersFields.NUMBER, Comparator.EQUAL, number);
		instructions.addNewConditionCriteria(PlayersFields.TEAM, Comparator.EQUAL, teamName.toString(), true);
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
			int id = resultSet.getInt("id");
			String firstName = resultSet.getString("firstname");
			String lastName = resultSet.getString("lastname");
			TeamName team = TeamName.valueOf(resultSet.getString("team"));
			Position position = Position.valueOf(resultSet.getString("position"));
			byte number = resultSet.getByte("number");
			players.add(new Player(id, firstName, lastName, team, position, number));
		}
		return players.toArray(new Player[players.size()]);
	}
	
}
