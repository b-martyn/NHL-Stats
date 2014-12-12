package connection;

public class Player {
	private int id;
	private String firstName;
	private String lastName;
	private Position position;
	
	public enum Position{
		DEFENCE, GOALIE, CENTER, LEFT_WING, RIGHT_WING;
	}
	
	public Player(int id, String firstName, String lastName, Position position) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.position = position;
	}

	public Player(String firstName, String lastName, Position position) {
		this(0, firstName, lastName, position);
	}
	
	public int getId() {
		return id;
	}
	
	void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Position getPosition() {
		return position;
	}

	void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Player) {
			Player compare = (Player) o;
			if (firstName.equals(compare.getFirstName())
					&& lastName.equals(compare.getLastName())
					&& position.equals(compare.getPosition())){
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash *= firstName.length();
		hash *= lastName.length();
		hash *= position.toString().length();
		return hash;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", firstName=" + firstName + ", lastName="
				+ lastName + ", position=" + position + "]";
	}
}
