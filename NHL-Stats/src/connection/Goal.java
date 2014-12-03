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
}
