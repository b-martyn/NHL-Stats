package connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import connection.DbConnection.Table;
import connection.RowSetInstructions.Comparator;
import connection.Franchise.TeamName;
import connection.MissedShot.MissedLocation;
import connection.Player.Position;
import connection.RowSetInstructions.Filter;
import connection.Shot.ShotType;

public class ShotConnection implements ShotConnector {
	
	private static ShotConnection instance = new ShotConnection();
	private DbConnector connection;
	
	private ShotConnection(){
		try {
			connection = new DbConnectorFactory().getDbConnector(Table.SHOTS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ShotConnection getInstance() {
		return instance;
	}

	@Override
	public RowSetInstructions getDefaultInstructions() {
		RowSetInstructions instructions = new RowSetInstructions(Table.SHOTS);
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.PLAYERS, PlayersFields.PLAYERID, ShotsFields.PLAYERID));
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.SHOTPLAYERS, ShotPlayersFields.SHOTID, ShotsFields.SHOTID));
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.PLAYERS, PlayersFields.PLAYERID, ShotPlayersFields.SHOTPLAYERID));
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.SNAPSHOTS, SnapshotsFields.SNAPSHOTID, ShotsFields.SNAPSHOTID));
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.GAMES, GamesFields.GAMEID, SnapshotsFields.GAMEID));
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.SNAPSHOTPLAYERS, SnapshotPlayersFields.SNAPSHOTID, SnapshotsFields.SNAPSHOTID));
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.PLAYERS, PlayersFields.PLAYERID, SnapshotPlayersFields.SNAPSHOTPLAYERID));
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.ROSTERPLAYERS, RosterPlayersFields.ROSTERPLAYERID, SnapshotPlayersFields.SNAPSHOTPLAYERID));
		instructions.addJoiningResultSet(instructions.new JoiningResultSet(Table.ROSTERS, RostersFields.ROSTERID, RosterPlayersFields.ROSTERID));
		return instructions;
	}
	
	@Override
	public ResultSet getLoadedResultSet() throws SQLException{
		return connection.getResultSet(getDefaultInstructions());
	}
	
	@Override
	public Shot[] getShots() throws SQLException {
		return convertShots(getLoadedResultSet());
	}

	@Override
	public Shot[] getShots(Player player) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(ShotsFields.PLAYERID, Comparator.EQUAL, player.getId());
		filter.filterAdditionField(ShotPlayersFields.SHOTPLAYERID, Comparator.EQUAL, player.getId(), false);
		instructions.addFilter(filter);
		return convertShots(connection.getResultSet(instructions));
	}

	@Override
	public Shot[] getShots(Game game) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(SnapshotsFields.GAMEID, Comparator.EQUAL, game.getId());
		instructions.addFilter(filter);
		filter = instructions.new Filter(GamesFields.DATE, Comparator.EQUAL, new java.sql.Date(game.getDate().getTime()));
		filter.filterAdditionField(GamesFields.HOMETEAM, Comparator.EQUAL, game.getHomeTeam().toString(), true);
		filter.filterAdditionField(GamesFields.AWAYTEAM, Comparator.EQUAL, game.getAwayTeam().toString(), false);
		instructions.addFilter(filter);
		return convertShots(connection.getResultSet(instructions));
	}

	@Override
	public Shot[] getShots(ShotType type) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(ShotsFields.SHOTTYPE, Comparator.EQUAL, type.toString());
		instructions.addFilter(filter);
		return convertShots(connection.getResultSet(instructions));
	}

	@Override
	public Shot getShot(int id) throws SQLException {
		RowSetInstructions instructions = getDefaultInstructions();
		Filter filter = instructions.new Filter(ShotsFields.SHOTID, Comparator.EQUAL, id);
		instructions.addFilter(filter);
		try{
			return convertShots(connection.getResultSet(instructions))[0];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	private Shot[] convertShots(ResultSet resultSet) throws SQLException{
		List<Shot> shots = new ArrayList<Shot>();
		Snapshot snapshot = new Snapshot(0, new Game(new Date(), TeamName.BRUINS, TeamName.BRUINS), new TimeStamp((byte)0, (short)0, (short)0));
		Shot shot = new Shot(0, snapshot, new Player(0, "", "", Position.GOALIE), ShotType.BACKHAND, (byte)0);
		resultSet.beforeFirst();
		while(resultSet.next()){
			int shotPlayerId = resultSet.getInt(ShotPlayersFields.SHOTPLAYERID.toString().toLowerCase());
			//Always more snapshotPlayers than any other field
			int snapshotPlayerIdIndex = resultSet.findColumn(SnapshotPlayersFields.SNAPSHOTPLAYERID.toString().toLowerCase());
			int snapshotPlayerId = resultSet.getInt(snapshotPlayerIdIndex);
			String snapshotPlayerFirstName = resultSet.getString(snapshotPlayerIdIndex + (PlayersFields.FIRSTNAME.getColumnNumber() - PlayersFields.PLAYERID.getColumnNumber()));
			String snapshotPlayerLastName = resultSet.getString(snapshotPlayerIdIndex + (PlayersFields.LASTNAME.getColumnNumber() - PlayersFields.PLAYERID.getColumnNumber()));
			Position snapshotPlayerPosition = Position.valueOf(resultSet.getString(snapshotPlayerIdIndex + (PlayersFields.POSITION.getColumnNumber() - PlayersFields.PLAYERID.getColumnNumber())));
			Player snapshotPlayer = new Player(snapshotPlayerId, snapshotPlayerFirstName, snapshotPlayerLastName, snapshotPlayerPosition);
			
			if((resultSet.getInt(ShotsFields.SHOTID.toString().toLowerCase())) == shot.getId()){
				if(snapshot.getGame().getHomeTeam().equals(TeamName.valueOf(resultSet.getString(RostersFields.TEAM.toString().toLowerCase())))){
					snapshot.addHomePlayerOnIce(snapshotPlayer);
				}else{
					snapshot.addAwayPlayerOnIce(snapshotPlayer);
				}
				if(shotPlayerId != shot.getPlayer().getId()){
					int shotPlayerIdIndex = resultSet.findColumn(ShotPlayersFields.SHOTPLAYERID.toString().toLowerCase());
					String shotPlayerFirstName = resultSet.getString(shotPlayerIdIndex + 2);
					String shotPlayerLastName = resultSet.getString(shotPlayerIdIndex + 3);
					Position shotPlayerPosition = Position.valueOf(resultSet.getString(shotPlayerIdIndex + 4));
					Player shotPlayer = new Player(shotPlayerId, shotPlayerFirstName, shotPlayerLastName, shotPlayerPosition);
					if(shot instanceof Goal){
						Goal goal = (Goal)shot;
						Player assist = goal.getAssist1();
						if(assist == null){
							shot = new Goal(goal.getId(), goal.getSnapshot(), goal.getPlayer(), goal.getShotType(), goal.getDistanceOut(), shotPlayer);
						}else if(goal.getAssist1().getId() != shotPlayerId){
							shot = new Goal(goal.getId(), goal.getSnapshot(), goal.getPlayer(), goal.getShotType(), goal.getDistanceOut(), goal.getAssist1(), shotPlayer);
						}
					}else if(shot instanceof BlockedShot){
						//Do Nothing
					}else{
						shot = new BlockedShot(shot.getId(), shot.getSnapshot(), shot.getPlayer(), shot.getShotType(), shot.getDistanceOut(), shotPlayer);
					}
				}
			}else{
				if(resultSet.getRow() != 1){
					shots.add(shot);
				}
				int snapshotId = resultSet.getInt(SnapshotsFields.SNAPSHOTID.toString().toLowerCase());
				byte period = resultSet.getByte(SnapshotsFields.PERIOD.toString().toLowerCase());
				short elapsedSeconds = resultSet.getShort(SnapshotsFields.ELAPSEDSECONDS.toString().toLowerCase());
				short secondsLeft = resultSet.getShort(SnapshotsFields.SECONDSLEFT.toString().toLowerCase());
				
				int gameId = resultSet.getInt(SnapshotsFields.GAMEID.toString().toLowerCase());
				Date date = resultSet.getDate(GamesFields.DATE.toString().toLowerCase());
				TeamName homeTeam = TeamName.valueOf(resultSet.getString(GamesFields.HOMETEAM.toString().toLowerCase()));
				TeamName awayTeam = TeamName.valueOf(resultSet.getString(GamesFields.AWAYTEAM.toString().toLowerCase()));
				byte homeScore = resultSet.getByte(GamesFields.HOMESCORE.toString().toLowerCase());
				byte awayScore = resultSet.getByte(GamesFields.AWAYSCORE.toString().toLowerCase());
				snapshot = new Snapshot(snapshotId, new Game(gameId, date, homeTeam, awayTeam, homeScore, awayScore), new TimeStamp(period, elapsedSeconds, secondsLeft));
				if(snapshot.getGame().getHomeTeam().equals(TeamName.valueOf(resultSet.getString(RostersFields.TEAM.toString().toLowerCase())))){
					snapshot.addHomePlayerOnIce(snapshotPlayer);
				}else{
					snapshot.addAwayPlayerOnIce(snapshotPlayer);
				}				

				int playerId = resultSet.getInt(PlayersFields.PLAYERID.toString().toLowerCase());
				String firstName = resultSet.getString(PlayersFields.FIRSTNAME.toString().toLowerCase());
				String lastName = resultSet.getString(PlayersFields.LASTNAME.toString().toLowerCase());
				Position position = Position.valueOf(resultSet.getString(PlayersFields.POSITION.toString().toLowerCase()));
				Player player = new Player(playerId, firstName, lastName, position);
				
				int shotId = resultSet.getInt(ShotsFields.SHOTID.toString().toLowerCase());
				ShotType shotType = ShotType.valueOf(resultSet.getString(ShotsFields.SHOTTYPE.toString().toLowerCase()));
				byte distance = resultSet.getByte(ShotsFields.DISTANCE.toString().toLowerCase());
				boolean goal = resultSet.getBoolean(ShotsFields.GOAL.toString().toLowerCase());
				String missedLocationString = resultSet.getString(ShotsFields.MISSEDSHOTLOCATION.toString().toLowerCase());
				
				if(missedLocationString != null){
					MissedLocation missedLocation = MissedLocation.valueOf(missedLocationString);
					shot = new MissedShot(shotId, snapshot, player, shotType, distance, missedLocation);
				}else if(snapshot.getTimeStamp().getElapsedSeconds() == 0 && snapshot.getTimeStamp().getSecondsLeft() == 0){
					shot = new ShootoutShot(shotId, snapshot, player, shotType, distance, goal);
				}else if(goal){
					shot = new Goal(shotId, snapshot, player, shotType, distance);
				}else if(shotPlayerId != playerId){
					int shotPlayerIdIndex = resultSet.findColumn(ShotPlayersFields.SHOTPLAYERID.toString().toLowerCase());
					String shotPlayerFirstName = resultSet.getString(shotPlayerIdIndex + 2);
					String shotPlayerLastName = resultSet.getString(shotPlayerIdIndex + 3);
					Position shotPlayerPosition = Position.valueOf(resultSet.getString(shotPlayerIdIndex + 4));
					Player shotPlayer = new Player(shotPlayerId, shotPlayerFirstName, shotPlayerLastName, shotPlayerPosition);
					if(goal){
						shot = new Goal(shotId, snapshot, player, shotType, distance, shotPlayer);
					}else{
						shot = new BlockedShot(shotId, snapshot, player, shotType, distance, shotPlayer);
					}
				}else{
					shot = new Shot(shotId, snapshot, player, shotType, distance);
				}
			}
		}
		shots.add(shot);
		
		return shots.toArray(new Shot[shots.size()]);
	}
}
