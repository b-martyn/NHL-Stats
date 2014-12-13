package connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import connection.Franchise.TeamName;
import connection.PlayerEvent.PlayerEventType;
import connection.Shot.ShotType;
import connection.TimeEvent.TimeEventType;

public class Connection implements GameConnector, PlayerConnector, PlayerEventConnector, ShotConnector, TimeEventConnector, SnapshotConnector, RostersConnector{
	
	private static GameConnection gameConnection;
	private static PlayerConnection playerConnection;
	private static PlayerEventConnection playerEventConnection;
	private static ShotConnection shotConnection;
	private static TimeEventConnection timeEventConnection;
	private static SnapshotConnection snapshotConnection;
	private static RostersConnection rostersConnection;
	
	public Connection(){
		gameConnection = GameConnection.getInstance();
		playerConnection = PlayerConnection.getInstance();
		playerEventConnection = PlayerEventConnection.getInstance();
		shotConnection = ShotConnection.getInstance();
		timeEventConnection = TimeEventConnection.getInstance();
		snapshotConnection = SnapshotConnection.getInstance();
		rostersConnection = RostersConnection.getInstance();
	}

	@Override
	public RowSetInstructions getDefaultInstructions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet getLoadedResultSet() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Snapshot[] getSnapshots() throws SQLException {
		return snapshotConnection.getSnapshots();
	}

	@Override
	public Snapshot[] getSnapshots(Game game) throws SQLException {
		return snapshotConnection.getSnapshots(game);
	}

	@Override
	public Snapshot getSnapshot(int id) throws SQLException {
		return snapshotConnection.getSnapshot(id);
	}

	@Override
	public TimeEvent[] getTimeEvents() throws SQLException {
		return timeEventConnection.getTimeEvents();
	}

	@Override
	public TimeEvent[] getTimeEvents(Game game) throws SQLException {
		return timeEventConnection.getTimeEvents(game);
	}

	@Override
	public TimeEvent[] getTimeEvents(TimeEventType type) throws SQLException {
		return timeEventConnection.getTimeEvents(type);
	}

	@Override
	public TimeEvent getTimeEvent(int id) throws SQLException {
		return timeEventConnection.getTimeEvent(id);
	}

	@Override
	public Shot[] getShots() throws SQLException {
		return shotConnection.getShots();
	}

	@Override
	public Shot[] getShots(Player player) throws SQLException {
		return shotConnection.getShots(player);
	}

	@Override
	public Shot[] getShots(Game game) throws SQLException {
		return shotConnection.getShots(game);
	}

	@Override
	public Shot[] getShots(ShotType type) throws SQLException {
		return shotConnection.getShots(type);
	}

	@Override
	public Shot getShot(int id) throws SQLException {
		return shotConnection.getShot(id);
	}

	@Override
	public PlayerEvent[] getPlayerEvents() throws SQLException {
		return playerEventConnection.getPlayerEvents();
	}

	@Override
	public PlayerEvent[] getPlayerEvents(Player player) throws SQLException {
		return playerEventConnection.getPlayerEvents(player);
	}

	@Override
	public PlayerEvent[] getPlayerEvents(Game game) throws SQLException {
		return playerEventConnection.getPlayerEvents(game);
	}

	@Override
	public PlayerEvent[] getPlayerEvents(PlayerEventType type) throws SQLException {
		return playerEventConnection.getPlayerEvents(type);
	}

	@Override
	public PlayerEvent getPlayerEvent(int id) throws SQLException {
		return playerEventConnection.getPlayerEvent(id);
	}

	@Override
	public Player[] getPlayers() throws SQLException {
		return playerConnection.getPlayers();
	}

	@Override
	public Player getPlayer(int id) throws SQLException {
		return playerConnection.getPlayer(id);
	}

	@Override
	public Player getPlayer(String name) throws SQLException {
		return playerConnection.getPlayer(name);
	}

	@Override
	public Player getPlayer(String firstName, String lastName) throws SQLException {
		return playerConnection.getPlayer(firstName, lastName);
	}

	@Override
	public Game[] getGames() throws SQLException {
		return gameConnection.getGames();
	}

	@Override
	public Game[] getGames(TeamName teamName) throws SQLException {
		return gameConnection.getGames(teamName);
	}

	@Override
	public Game[] getGames(Season season) throws SQLException {
		return gameConnection.getGames(season);
	}

	@Override
	public Game getGame(int id) throws SQLException {
		return gameConnection.getGame(id);
	}

	@Override
	public Game getGame(Date date, TeamName teamName) throws SQLException {
		return gameConnection.getGame(date, teamName);
	}

	@Override
	public Roster[] getRosters() throws SQLException {
		return rostersConnection.getRosters();
	}

	@Override
	public Roster[] getRosters(Game game) throws SQLException {
		return rostersConnection.getRosters(game);
	}

	@Override
	public Roster[] getRosters(TeamName teamName) throws SQLException {
		return rostersConnection.getRosters(teamName);
	}

	@Override
	public Roster getRoster(TeamName teamName, Date date) throws SQLException {
		return rostersConnection.getRoster(teamName, date);
	}
}