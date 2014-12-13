package connection;

public class TimeEvent {
	
	public enum TimeEventType {
		PERIOD_START, PERIOD_END, GOALIE_STOPPED, ICING, PUCK_FROZEN, TV_TIMEOUT, NET_OFF, PUCK_IN_NETTING, 
		REFEREE_OR_LINESMAN, PLAYER_EQUIPMENT, PUCK_IN_BENCHES, OFFSIDE, RINK_REPAIR, PLAYER_INJURY;
	}
	
	private int id;
	private boolean starting;
	private Snapshot snapshot;
	private TimeEventType type;

	public TimeEvent(int id, boolean starting, Snapshot snapshot, TimeEventType type) {
		super();
		this.id = id;
		this.starting = starting;
		this.snapshot = snapshot;
		this.type = type;
	}
	
	public TimeEvent(boolean starting, Snapshot snapshot, TimeEventType type) {
		this(0, starting, snapshot, type);
	}
	
	public TimeEvent(){
		this(0, false, new Snapshot(), null);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isStarting() {
		return starting;
	}
	
	public void setStarting(boolean starting) {
		this.starting = starting;
	}
	
	public Snapshot getSnapshot() {
		return snapshot;
	}
	
	public void setSnapshot(Snapshot snapshot) {
		this.snapshot = snapshot;
	}
	
	public TimeEventType getType() {
		return type;
	}
	
	public void setType(TimeEventType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "TimeEvent [id=" + id + ", starting=" + starting + ", snapshot="
				+ snapshot + ", type=" + type + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((snapshot == null) ? 0 : snapshot.hashCode());
		result = prime * result + (starting ? 1231 : 1237);
		if(type == null){
			result = prime * result;
		}else{
			for(char character : type.toString().toCharArray()){
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
		
		TimeEvent other = (TimeEvent) obj;
		if (snapshot == null) {
			if (other.snapshot != null)
				return false;
		} else if (!snapshot.equals(other.snapshot))
			return false;
		if (starting != other.starting || type != other.type){
			return false;
		}
		
		return true;
	}
	
}
