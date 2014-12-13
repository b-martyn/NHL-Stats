package connection;

public class PlayerEvent {
	
	public enum PlayerEventType {
		TAKEAWAY, GIVEAWAY,	FACEOFF_WIN, FACEOFF_LOSS, HIT_GIVEN, HIT_RECEIVED, PENALTY_DRAWN, PENALTY_TAKEN;
	}
	
	private int id;
	private Player player;
	private Snapshot snapshot;
	private Zone zone;
	private PlayerEventType type;

	public PlayerEvent(int id, Player player, Snapshot snapshot, Zone zone, PlayerEventType type) {
		super();
		this.id = id;
		this.player = player;
		this.snapshot = snapshot;
		this.zone = zone;
		this.type = type;
	}
	
	public PlayerEvent(Player player, Snapshot snapshot, Zone zone, PlayerEventType type) {
		this(0, player, snapshot, zone, type);
	}
	
	public PlayerEvent(){
		this(0, new Player(), new Snapshot(), null, null);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Snapshot getSnapshot() {
		return snapshot;
	}
	
	public void setSnapshot(Snapshot snapshot) {
		this.snapshot = snapshot;
	}
	
	public Zone getZone() {
		return zone;
	}
	
	public void setZone(Zone zone) {
		this.zone = zone;
	}
	
	public PlayerEventType getType() {
		return type;
	}
	
	public void setType(PlayerEventType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "PlayerEvent [id=" + id + ", player=" + player + ", snapshot="
				+ snapshot + ", zone=" + zone + ", type=" + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		result = prime * result	+ ((snapshot == null) ? 0 : snapshot.hashCode());
		if(type == null){
			result = prime * result;
		}else{
			for(char character : type.toString().toCharArray()){
				result = prime * result + character;
			}
		}
		if(zone == null){
			result = prime * result;
		}else{
			for(char character : zone.toString().toCharArray()){
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
		
		PlayerEvent other = (PlayerEvent) obj;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player)){
			return false;
		}
		if (snapshot == null) {
			if (other.snapshot != null){
				return false;
			}
		} else if (!snapshot.equals(other.snapshot)){
			return false;
		}
		if (type != other.type || zone != other.zone){
			return false;
		}
		
		return true;
	}
}
