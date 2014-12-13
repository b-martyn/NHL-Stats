package connection;

public class ShootoutShot extends Shot {

	boolean goal;

	public ShootoutShot(int id, Snapshot snapshot, Player player,	ShotType shotType, byte distanceOut, boolean goal) {
		super(id, snapshot, player, shotType, distanceOut);
		this.goal = goal;
	}
	
	public ShootoutShot(Snapshot snapshot, Player player,	ShotType shotType, byte distanceOut, boolean goal) {
		this(0, snapshot, player, shotType, distanceOut, goal);
	}
	
	public ShootoutShot(){
		super();
		this.goal = false;
	}
	
	public boolean isGoal() {
		return goal;
	}
	
	public void setGoal(boolean goal) {
		this.goal = goal;
	}

	@Override
	public String toString() {
		return "ShootoutShot [goal=" + goal + "]" + super.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (goal ? 1231 : 1237);
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
		
		ShootoutShot other = (ShootoutShot) obj;
		if (goal != other.goal){
			return false;
		}
		
		return true;
	}
}
