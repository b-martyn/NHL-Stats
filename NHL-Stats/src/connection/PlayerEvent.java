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
}
