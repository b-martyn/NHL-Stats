package connection;

public class BlockedShot extends Shot {

	private Player blockingPlayer;
	
	public BlockedShot(int id, Snapshot snapshot, Player player, ShotType shotType, byte distanceOut, Player blockingPlayer) {
		super(id, snapshot, player, shotType, distanceOut);
		this.blockingPlayer = blockingPlayer;
	}
	
	public BlockedShot(Snapshot snapshot, Player player, ShotType shotType, byte distanceOut, Player blockingPlayer) {
		this(0, snapshot, player, shotType, distanceOut, blockingPlayer);
	}
	
	public BlockedShot(){
		super();
		this.blockingPlayer = new Player();
	}
	
	public Player getBlockingPlayer() {
		return blockingPlayer;
	}

	void setBlockingPlayer(Player blockingPlayer) {
		this.blockingPlayer = blockingPlayer;
	}

	@Override
	public String toString() {
		return "BlockedShot [blockingPlayer=" + blockingPlayer + "]" + super.toString();
	}
}