package connection;

public class Shot {
	
	public enum ShotType {
		WRIST, SLAP, BACKHAND, SNAP, TIP_IN, WRAP_AROUND;
	}
	
	private int id;
	private Snapshot snapshot;
	private Player player;
	private ShotType shotType;
	private byte distanceOut;

	public Shot(int id, Snapshot snapshot, Player player, ShotType shotType, byte distanceOut) {
		this.id = id;
		this.snapshot = snapshot;
		this.player = player;
		this.shotType = shotType;
		this.distanceOut = distanceOut;
	}
	
	public Shot(Snapshot snapshot, Player player, ShotType shotType, byte distanceOut){
		this(0, snapshot, player, shotType, distanceOut);
	}
	
	public Shot(){
		this(0, new Snapshot(), new Player(), null, (byte)0);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Snapshot getSnapshot() {
		return snapshot;
	}
	
	public void setSnapshot(Snapshot snapshot) {
		this.snapshot = snapshot;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public ShotType getShotType() {
		return shotType;
	}
	
	public void setShotType(ShotType shotType) {
		this.shotType = shotType;
	}
	
	public byte getDistanceOut() {
		return distanceOut;
	}
	
	public void setDistanceOut(byte distanceOut) {
		this.distanceOut = distanceOut;
	}

	@Override
	public String toString() {
		return "Shot [id=" + id + ", snapshot=" + snapshot + ", player="
				+ player + ", shotType=" + shotType + ", distanceOut="
				+ distanceOut + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + distanceOut;
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		result = prime * result	+ ((snapshot == null) ? 0 : snapshot.hashCode());
		if(shotType == null){
			result = prime * result;
		}else{
			for(char character : shotType.toString().toCharArray()){
				result = prime * result + character;
			}
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()){
			return false;
		}
		if (this == obj){
			return true;
		}
		
		Shot other = (Shot) obj;
		if (player == null){
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		if (snapshot == null){
			if (other.snapshot != null){
				return false;
			}
		} else if (!snapshot.equals(other.snapshot)){
			return false;
		}
		if (shotType != other.shotType || distanceOut != other.distanceOut){
			return false;
		}
		
		return true;
	}
}
