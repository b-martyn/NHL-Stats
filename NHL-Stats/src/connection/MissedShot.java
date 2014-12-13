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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		if(missedLocation == null){
			result = prime * result;
		}else{
			for(char character : missedLocation.toString().toCharArray()){
				result = prime * result + character;
			}
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj) || this.getClass() != obj.getClass()){
			return false;
		}
		if (this == obj){
			return true;
		}
		
		MissedShot other = (MissedShot) obj;
		if (missedLocation != other.missedLocation){
			return false;
		}
		
		return true;
	}
}
