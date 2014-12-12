package connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import connection.DbConnection.Table;
import connection.Franchise.TeamName;
import connection.Player.Position;
import connection.RowSetInstructions.Comparator;
import connection.RowSetInstructions.Filter;

public class RostersConnection implements RostersConnector{
	
	private static RostersConnection instance = new RostersConnection();
	private DbConnector connection;
	
	private RostersConnection(){
		try {
			connection = new DbConnectorFactory().getDbConnector(Table.ROSTERS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static RostersConnection getInstance() {
		return instance;
	}
	
	@Override
	public RowSetInstructions getDefaultInstructions(){
		RowSetInstructions instructions = new RowSetInstructions(Table.ROSTERS);
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.ROSTERPLAYERS, RosterPlayersFields.ROSTERID, RostersFields.ROSTERID));
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.PLAYERS, PlayersFields.PLAYERID, RosterPlayersFields.ROSTERPLAYERID));
		return instructions;
	}
	
	@Override
	public ResultSet getLoadedResultSet() throws SQLException{
		return connection.getResultSet(getDefaultInstructions());
	}
	
	@Override
	public Roster[] getRosters() throws SQLException {
		return convertRosters(getLoadedResultSet());
	}

	@Override
	public Roster[] getRosters(Game game) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(RostersFields.STARTDATE, Comparator.LESS_THEN, new java.sql.Date(game.getDate().getTime()));
		filter.filterAdditionField(RostersFields.ENDDATE, Comparator.GREATER_THEN_EQUAL, new java.sql.Date(game.getDate().getTime()), true);
		instructions.addFilter(filter);
		return convertRosters(connection.getResultSet(instructions));
	}

	@Override
	public Roster[] getRosters(TeamName teamName) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(RostersFields.TEAM, Comparator.EQUAL, teamName.toString());
		instructions.addFilter(filter);
		return convertRosters(connection.getResultSet(instructions));
	}

	@Override
	public Roster getRoster(TeamName teamName, java.util.Date date) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(RostersFields.STARTDATE, Comparator.LESS_THEN, new java.sql.Date(date.getTime()));
		filter.filterAdditionField(RostersFields.ENDDATE, Comparator.GREATER_THEN_EQUAL, new java.sql.Date(date.getTime()), true);
		filter.filterAdditionField(RostersFields.TEAM, Comparator.EQUAL, teamName.toString(), false);
		instructions.addFilter(filter);
		return convertRosters(connection.getResultSet(instructions))[0];
	}
	
	private Roster[] convertRosters(ResultSet resultSet) throws SQLException{
		List<Roster> rosters = new ArrayList<Roster>();
		Roster roster = new Roster();
		resultSet.beforeFirst();
		while(resultSet.next()){
			if((resultSet.getInt(RostersFields.ROSTERID.toString().toLowerCase())) == roster.getId()){
				int playerId = resultSet.getInt(PlayersFields.PLAYERID.toString().toLowerCase());
				String firstName = resultSet.getString(PlayersFields.FIRSTNAME.toString().toLowerCase());
				String lastName = resultSet.getString(PlayersFields.LASTNAME.toString().toLowerCase());
				Position position = Position.valueOf(resultSet.getString(PlayersFields.POSITION.toString().toLowerCase()));
				roster.addPlayer(new Player(playerId, firstName, lastName, position));
			}else{
				if(roster != null){
					rosters.add(roster);
				}
				
				int rosterId = resultSet.getInt(RostersFields.ROSTERID.toString().toLowerCase());
				TeamName teamName = TeamName.valueOf(resultSet.getString(RostersFields.TEAM.toString().toLowerCase()).toUpperCase());
				Date startDate = resultSet.getDate(RostersFields.STARTDATE.toString().toLowerCase());
				Date endDate = resultSet.getDate(RostersFields.ENDDATE.toString().toLowerCase());
				roster = new Roster(rosterId, teamName, startDate, endDate);int playerId = resultSet.getInt(PlayersFields.PLAYERID.toString().toLowerCase());
				String firstName = resultSet.getString(PlayersFields.FIRSTNAME.toString().toLowerCase());
				String lastName = resultSet.getString(PlayersFields.LASTNAME.toString().toLowerCase());
				Position position = Position.valueOf(resultSet.getString(PlayersFields.POSITION.toString().toLowerCase()));
				roster.addPlayer(new Player(playerId, firstName, lastName, position));
			}
		}
		rosters.add(roster);
		
		return rosters.toArray(new Roster[rosters.size()]);
	}
}
