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

	public void setBlockingPlayer(Player blockingPlayer) {
		this.blockingPlayer = blockingPlayer;
	}

	@Override
	public String toString() {
		return "BlockedShot [blockingPlayer=" + blockingPlayer + "]" + super.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result	+ ((blockingPlayer == null) ? 0 : blockingPlayer.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj) || getClass() != obj.getClass()){
			return false;
		}
		if (this == obj){
			return true;
		}
		
		BlockedShot other = (BlockedShot) obj;
		if (blockingPlayer == null){
			if (other.blockingPlayer != null){
				return false;
			}
		} else if (!blockingPlayer.equals(other.blockingPlayer)){
			return false;
		}
		
		return true;
	}
}