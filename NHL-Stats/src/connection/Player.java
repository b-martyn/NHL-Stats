package connection;

import connection.Franchise.TeamName;

public class Player {
	private int id;
	private String firstName;
	private String lastName;
	private Franchise franchise;
	private Position position;
	private byte number;
	
	public enum Position{
		DEFENCE, GOALIE, CENTER, LEFT_WING, RIGHT_WING;
	}
	
	public Player(int id, String firstName, String lastName, TeamName teamName,	Position position, byte number) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.franchise = new Franchise(teamName);
		this.position = position;
		this.number = number;
	}

	public Player(String firstName, String lastName, TeamName teamName,	Position position, byte number) {
		this(0, firstName, lastName, teamName, position, number);
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

	public Franchise getFranchise() {
		return franchise;
	}

	void setFranchise(TeamName teamName) {
		Franchise franchise = new Franchise(teamName);
		this.franchise = franchise;
	}

	public Position getPosition() {
		return position;
	}

	void setPosition(Position position) {
		this.position = position;
	}

	public byte getNumber() {
		return number;
	}

	void setNumber(byte number) {
		this.number = number;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Player) {
			Player compare = (Player) o;
			if (firstName.equals(compare.getFirstName())
					&& lastName.equals(compare.getLastName())
					&& franchise.getTeamName().equals(compare.getFranchise().getTeamName())
					&& position.equals(compare.getPosition())
					&& number == compare.getNumber()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash *= number;
		hash *= firstName.length();
		hash *= number;
		hash *= lastName.length();
		hash *= number;
		hash *= position.toString().length();
		hash *= number;
		hash *= franchise.toString().length();
		return hash;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", firstName=" + firstName + ", lastName="
				+ lastName + ", franchise=" + franchise.getTeamName() + ", position="
				+ position + ", number=" + number + "]";
	}
}
