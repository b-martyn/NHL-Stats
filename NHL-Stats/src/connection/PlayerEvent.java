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
	
	public int getId() {
		return id;
	}
	
	void setId(int id) {
		this.id = id;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	void setPlayer(Player player) {
		this.player = player;
	}
	
	public Snapshot getTimeStamp() {
		return snapshot;
	}
	
	void setTimeStamp(Snapshot snapshot) {
		this.snapshot = snapshot;
	}
	
	public Zone getZone() {
		return zone;
	}
	
	void setZone(Zone zone) {
		this.zone = zone;
	}
	
	public PlayerEventType getType() {
		return type;
	}
	
	void setType(PlayerEventType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "PlayerEvent [id=" + id + ", player=" + player + ", snapshot="
				+ snapshot + ", zone=" + zone + ", type=" + type + "]";
	}
}
