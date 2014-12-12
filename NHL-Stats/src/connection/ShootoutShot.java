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
	
	void setGoal(boolean goal) {
		this.goal = goal;
	}

	@Override
	public String toString() {
		return "ShootoutShot [goal=" + goal + "]" + super.toString();
	}
}
