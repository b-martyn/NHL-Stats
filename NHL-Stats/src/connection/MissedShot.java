package connection;

public class MissedShot extends Shot {

	public enum MissedLocation {
		OVER_NET, WIDE_OF_NET, HIT_CROSSBAR, GOALPOST;
	}

	private MissedLocation missedLocation;

	public MissedShot(int id, Snapshot snapshot, Player player, ShotType shotType, byte distanceOut, MissedLocation missedLocation) {
		super(id, snapshot, player, shotType, distanceOut);
		this.missedLocation = missedLocation;
	}
	
	public MissedShot(Snapshot snapshot, Player player, ShotType shotType, byte distanceOut, MissedLocation missedLocation) {
		this(0, snapshot, player, shotType, distanceOut, missedLocation);
	}
	
	public MissedShot(){
		this(0, new Snapshot(), new Player(), null, (byte)0, null);
	}
	
	public MissedLocation getMissedLocation() {
		return missedLocation;
	}

	public void setMissedLocation(MissedLocation missedLocation) {
		this.missedLocation = missedLocation;
	}

	@Override
	public String toString() {
		return "MissedShot [missedLocation=" + missedLocation + "]" + super.toString();
	}
}
