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
			int id = resultSet.getInt(GamesFields.ID.toString().toLowerCase());
			Date date = resultSet.getDate(GamesFields.DATE.toString().toLowerCase());
			TeamName homeTeam = TeamName.valueOf(resultSet.getString(GamesFields.HOMETEAM.toString().toLowerCase()));
			TeamName awayTeam = TeamName.valueOf(resultSet.getString(GamesFields.AWAYTEAM.toString().toLowerCase()));
			byte homeScore = resultSet.getByte(GamesFields.HOMESCORE.toString().toLowerCase());
			byte awayScore = resultSet.getByte(GamesFields.AWAYSCORE.toString().toLowerCase());
			Game newGame = new Game(id, date, homeTeam, awayTeam, homeScore, awayScore);
			games.add(newGame);
		}
		for(Game game : games){
			if((game.getHomeScore() != 0) || (game.getAwayScore() != 0)){
				Shot[] shots = ShotConnection.getInstance().getShots(game);
				PlayerEvent[] playerEvents = PlayerEventConnection.getInstance().getPlayerEvents(game);
				TimeEvent[] timeEvents = TimeEventConnection.getInstance().getTimeEvents(game);
				game.setShots(shots);
				game.setPlayerEvents(playerEvents);
				game.setTimeEvents(timeEvents);
			}
		}
		
		return games.toArray(new Game[games.size()]);
	}
}
