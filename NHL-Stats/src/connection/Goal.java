package connection;

public class Goal extends Shot {
	
	private Player assist1;
	private Player assist2;

	public Goal(int id, Snapshot snapshot, Player player, ShotType shotType, byte distanceOut, Player assist1, Player assist2) {
		super(id, snapshot, player, shotType, distanceOut);
		this.assist1 = assist1;
		this.assist2 = assist2;
	}
	
	public Goal(int id, Snapshot snapshot, Player player, ShotType shotType, byte distanceOut, Player assist1) {
		super(id, snapshot, player, shotType, distanceOut);
		this.assist1 = assist1;
	}
	
	public Goal(int id, Snapshot snapshot, Player player, ShotType shotType, byte distanceOut) {
		super(id, snapshot, player, shotType, distanceOut);
	}
	
	public Goal(Snapshot snapshot, Player player, ShotType shotType, byte distanceOut, Player assist1, Player assist2) {
		this(0, snapshot, player, shotType, distanceOut, assist1, assist2);
	}
	
	public Goal(Snapshot snapshot, Player player, ShotType shotType, byte distanceOut, Player assist1) {
		this(0, snapshot, player, shotType, distanceOut, assist1);
	}
	
	public Goal(Snapshot snapshot, Player player, ShotType shotType, byte distanceOut) {
		this(0, snapshot, player, shotType, distanceOut);
	}
	
	public Goal(){
		super();
		this.assist1 = new Player();
		this.assist2 = new Player();
	}
		
	public Player getAssist1() {
		return assist1;
	}
	
	public void setAssist1(Player assist1) {
		this.assist1 = assist1;
	}
	
	public Player getAssist2() {
		return assist2;
	}
	
	public void setAssist2(Player assist2) {
		this.assist2 = assist2;
	}

	@Override
	public String toString() {
		return "Goal [assist1=" + assist1 + ", assist2=" + assist2 + "]" + super.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (((assist1 == null) ? 0 : assist1.hashCode()) + ((assist2 == null) ? 0 : assist2.hashCode()));
		//result = prime * result + ((assist2 == null) ? 0 : assist2.hashCode());
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
		
		Goal other = (Goal) obj;
		if (assist1 == null) {
			if (other.assist1 != null){
				return false;
			}
		}
		if (assist2 == null) {
			if (other.assist2 != null){
				return false;
			}
		}else if(!assist1.equals(other.getAssist1()) && !assist1.equals(other.getAssist2())){
			return false;
		}else if(!assist2.equals(other.getAssist1()) && !assist2.equals(other.getAssist2())){
			return false;
		}
		if(assist1 != null && assist2 == null){
			if(!assist1.equals(other.assist2)){
				return false;
			}
		}
		
		return true;
	}
}
