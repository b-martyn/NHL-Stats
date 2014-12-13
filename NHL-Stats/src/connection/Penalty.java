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
	
	public Penalty(){
		super();
		this.infraction = null;
		this.minutes = 0;
	}
	
	public Infraction getInfraction() {
		return infraction;
	}
	
	public void setInfraction(Infraction infraction) {
		this.infraction = infraction;
	}
	
	public short getMinutes() {
		return minutes;
	}
	
	public void setMinutes(short minutes) {
		this.minutes = minutes;
	}

	@Override
	public String toString() {
		return "Penalty [infraction=" + infraction + ", minutes=" + minutes
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		if(infraction == null){
			result = prime * result;
		}else{
			for(char character : infraction.toString().toCharArray()){
				result = prime * result + character;
			}
		}
		result = prime * result + minutes;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj) || this.getClass() != obj.getClass()){
			return false;
		}
		if (this == obj)
			return true;
		
		Penalty other = (Penalty) obj;
		if (infraction != other.infraction || minutes != other.minutes){
			return false;
		}
		
		return true;
	}
}
