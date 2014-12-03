package connection;

public class Penalty extends PlayerEvent {
	
	public enum Infraction{
		HOOKING, FIGHTING, INTERFERENCE_ON_GOALKEEPER, TRIPPING, INTERFERENCE, ELBOWING, UNSPORTSMANLIKE_CONDUCT;
	}
	
	private Infraction infraction;
	private short minutes;

	public Penalty(int id, Player player, Snapshot snapshot, Zone zone, PlayerEventType type, Infraction infraction, short minutes) {
		super(id, player, snapshot, zone, type);
		this.infraction = infraction;
		this.minutes = minutes;
	}
	
	public Penalty(Player player, Snapshot snapshot, Zone zone, PlayerEventType type, Infraction infraction, short minutes) {
		this(0, player, snapshot, zone, type, infraction, minutes);
	}
		
	public Infraction getInfraction() {
		return infraction;
	}
	
	void setInfraction(Infraction infraction) {
		this.infraction = infraction;
	}
	
	public short getMinutes() {
		return minutes;
	}
	
	void setMinutes(short minutes) {
		this.minutes = minutes;
	}

	@Override
	public String toString() {
		return "Penalty [infraction=" + infraction + ", minutes=" + minutes
				+ "]";
	}
}
