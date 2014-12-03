package connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import connection.DbConnection.Table;
import connection.DbRowSetInstructions.Comparator;
import connection.Franchise.TeamName;

public class GameConnection implements GameConnector {
	
	private static GameConnection instance = new GameConnection();
	private DbConnector connection;
	
	private GameConnection() {
		try {
			connection = new DbConnectorFactory().getDbConnector(Table.GAMES);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static GameConnection getInstance(){
		return instance;
	}
	
	@Override
	public Game[] getGames() throws SQLException {
		return convertGames(connection.getResultSet());
	}
	
	@Override
	public Game[] getGames(TeamName teamName) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.GAMES);
		instructions.addNewFilterCriteria(GamesFields.HOMETEAM, Comparator.EQUAL, teamName.toString());
		instructions.addNewConditionCriteria(GamesFields.AWAYTEAM, Comparator.EQUAL, teamName.toString(), false);
		return convertGames(connection.getResultSet(instructions));
	}
	
	@Override
	public Game[] getGames(Season season) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.GAMES);
		instructions.addNewFilterCriteria(GamesFields.DATE, Comparator.GREATER_THEN_EQUAL, new java.sql.Date(season.getStartDate().getTime()));
		instructions.addNewConditionCriteria(GamesFields.DATE, Comparator.LESS_THEN_EQUAL, new java.sql.Date(season.getEndDate().getTime()), true);
		return convertGames(connection.getResultSet(instructions));
	}

	@Override
	public Game getGame(int id) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.GAMES);
		instructions.addNewFilterCriteria(GamesFields.ID, Comparator.EQUAL, id);
		try{
			return convertGames(connection.getResultSet(instructions))[0];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}

	@Override
	public Game getGame(Date date, TeamName teamName) throws SQLException {
		DbRowSetInstructions instructions = new DbRowSetInstructions(Table.GAMES);
		instructions.addNewFilterCriteria(GamesFields.DATE, Comparator.EQUAL, new java.sql.Date(date.getTime()));
		instructions.addNewConditionCriteria(GamesFields.AWAYTEAM, Comparator.EQUAL, teamName.toString(), true);
		instructions.addNewConditionCriteria(GamesFields.HOMETEAM, Comparator.EQUAL, teamName.toString(), false);
		try{
			return convertGames(connection.getResultSet(instructions))[0];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	private Game[] convertGames(ResultSet resultSet) throws SQLException{
		List<Game> games = new ArrayList<Game>();
		
		resultSet.beforeFirst();
		while(resultSet.next()){
			int id = resultSet.getInt("id");
			Date date = resultSet.getDate("date");
			TeamName homeTeam = TeamName.valueOf(resultSet.getString("homeTeam"));
			TeamName awayTeam = TeamName.valueOf(resultSet.getString("awayTeam"));
			byte homeScore = resultSet.getByte("homeScore");
			byte awayScore = resultSet.getByte("awayScore");
			games.add(new Game(id, date, homeTeam, awayTeam, homeScore, awayScore));
		}
		
		return games.toArray(new Game[games.size()]);
	}
}
