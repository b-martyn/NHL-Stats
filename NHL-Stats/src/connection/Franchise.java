package connection;

import java.awt.Color;

public class Franchise {
	private String province;
	private TeamName teamName;

	private Color baseColor;
	private Color mainColor;
	private Color secondaryColor;
	
	public enum TeamName{
		BRUINS("Boston"), CANADIENS("Montr\u00E9al"), DUCKS("Anaheim"), COYOTES("Arizona"), SABRES("Buffalo"), FLAMES("Calgary"), HURRICANES("Carolina"), 
		BLACKHAWKS("Chicago"), AVALANCHE("Colorado"), BLUE_JACKETS("Columbus"), STARS("Dallas"), RED_WINGS("Detroit"), OILERS("Edmonton"), 
		PANTHERS("Florida"), KINGS("Los Angeles"), WILD("Minnesota"), PREDATORS("Nashville"), DEVILS("New Jersey"), ISLANDERS("New York"), 
		RANGERS("New York"), SENATORS("Ottowa"), FLYERS("Philadelphia"), PENGUINS("Pittsburgh"), SHARKS("San Jose"), BLUES("St Louis"), 
		LIGHTNING("Tampa Bay"), MAPLE_LEAFS("Toronto"), CANUCKS("Vancouver"), CAPITALS("Washington"), JETS("Winnipeg");
		
		private String province;
		
		private TeamName(String province){
			this.province = province;
		}
		
		public String province(){
			return province;
		}
	}

	public Franchise(TeamName teamName) {
		this.teamName = teamName;
		this.province = teamName.province();
		pickColors(teamName);
	}

	public String getProvince() {
		return province;
	}

	public TeamName getTeamName() {
		return teamName;
	}

	public Color getBaseColor() {
		return baseColor;
	}

	public Color getMainColor() {
		return mainColor;
	}

	public Color getSecondaryColor() {
		return secondaryColor;
	}

	@Override
	public String toString() {
		return this.province + " " + this.teamName;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Franchise){
			Franchise compareingFranchise = (Franchise) o;
			if(compareingFranchise.getProvince().equals(this.province)
					&& compareingFranchise.getTeamName().equals(this.teamName)
					&& compareingFranchise.getBaseColor().equals(this.baseColor)
					&& compareingFranchise.getMainColor().equals(this.mainColor)
					&& compareingFranchise.getSecondaryColor().equals(this.secondaryColor)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		int hash = 7;
		hash *= province.length();
		hash *= teamName.toString().length();
		hash *= (baseColor.getBlue() + baseColor.getRed() + baseColor.getRed() + 1);
		hash *= (mainColor.getBlue() + mainColor.getRed() + mainColor.getRed() + 1);
		hash *= (secondaryColor.getBlue() + secondaryColor.getRed() + secondaryColor.getRed() + 1);
		return hash;
	}
	
	private void pickColors(TeamName teamName) {
		switch (teamName) {
			case BRUINS:
				this.baseColor = new Color(0, 0, 0);
				this.mainColor = new Color(255, 196, 34);
				this.secondaryColor = new Color(255, 255, 255);
				break;
			case CANADIENS:
				this.baseColor = new Color(191, 47, 56);
				this.mainColor = new Color(255, 255, 255);
				this.secondaryColor = new Color(33, 55, 112);
				break;
			case DUCKS:
				this.baseColor = new Color(0, 0, 0);
				this.mainColor = new Color(145, 118, 75);
				this.secondaryColor = new Color(239, 82, 37);
				break;
			case COYOTES:
				this.baseColor = new Color(132, 31, 39);
				this.mainColor = new Color(0, 0, 0);
				this.secondaryColor = new Color(239, 255, 198);
				break;
			case SABRES:
				this.baseColor = new Color(0, 46, 98);
				this.mainColor = new Color(235, 187, 47);
				this.secondaryColor = new Color(147, 182, 185);
				break;
			case FLAMES:
				this.baseColor = new Color(224, 58, 62);
				this.mainColor = new Color(255, 199, 88);
				this.secondaryColor = new Color(0, 0, 0);
				break;
			case HURRICANES:
				this.baseColor = new Color(224, 58, 62);
				this.mainColor = new Color(0, 0, 0);
				this.secondaryColor = new Color(142, 142, 144);
				break;
			case BLACKHAWKS:
				this.baseColor = new Color(227, 38, 58);
				this.mainColor = new Color(0, 0, 0);
				this.secondaryColor = new Color(255, 255, 255);
				break;
			case AVALANCHE:
				this.baseColor = new Color(139, 41, 66);
				this.mainColor = new Color(1, 84, 138);
				this.secondaryColor = new Color(0, 0, 0);
				break;
			case BLUE_JACKETS:
				this.baseColor = new Color(0, 40, 92);
				this.mainColor = new Color(224, 58, 62);
				this.secondaryColor = new Color(169, 176, 184);
				break;
			case STARS:
				this.baseColor = new Color(0, 106, 78);
				this.mainColor = new Color(0, 0, 0);
				this.secondaryColor = new Color(255, 255, 255);
				break;
			case RED_WINGS:
				this.baseColor = new Color(236, 31, 38);
				this.mainColor = new Color(255, 255, 255);
				this.secondaryColor = new Color(0, 0, 0);
				break;
			case OILERS:
				this.baseColor = new Color(0, 55, 119);
				this.mainColor = new Color(230, 106, 32);
				this.secondaryColor = new Color(255, 255, 255);
				break;
			case PANTHERS:
				this.baseColor = new Color(200, 33, 63);
				this.mainColor = new Color(0, 46, 95);
				this.secondaryColor = new Color(213, 156, 5);
				break;
			case KINGS:
				this.baseColor = new Color(0, 0, 0);
				this.mainColor = new Color(255, 255, 255);
				this.secondaryColor = new Color(175, 183, 186);
				break;
			case WILD:
				this.baseColor = new Color(2, 87, 54);
				this.mainColor = new Color(191, 43, 55);
				this.secondaryColor = new Color(239, 180, 16);
				break;
			case PREDATORS:
				this.baseColor = new Color(253, 187, 47);
				this.mainColor = new Color(0, 46, 98);
				this.secondaryColor = new Color(255, 255, 255);
				break;
			case DEVILS:
				this.baseColor = new Color(224, 58, 62);
				this.mainColor = new Color(0, 0, 0);
				this.secondaryColor = new Color(255, 255, 255);
				break;
			case ISLANDERS:
				this.baseColor = new Color(0, 82, 155);
				this.mainColor = new Color(245, 125, 49);
				this.secondaryColor = new Color(255, 255, 255);
				break;
			case RANGERS:
				this.baseColor = new Color(1, 97, 171);
				this.mainColor = new Color(230, 57, 63);
				this.secondaryColor = new Color(255, 255, 255);
				break;
			case SENATORS:
				this.baseColor = new Color(228, 23, 62);
				this.mainColor = new Color(0, 0, 0);
				this.secondaryColor = new Color(214, 159, 15);
				break;
			case FLYERS:
				this.baseColor = new Color(244, 121, 64);
				this.mainColor = new Color(0, 0, 0);
				this.secondaryColor = new Color(255, 255, 255);
				break;
			case PENGUINS:
				this.baseColor = new Color(0, 0, 0);
				this.mainColor = new Color(209, 189, 128);
				this.secondaryColor = new Color(255, 255, 255);
				break;
			case SHARKS:
				this.baseColor = new Color(5, 83, 93);
				this.mainColor = new Color(243, 143, 32);
				this.secondaryColor = new Color(0, 0, 0);
				break;
			case BLUES:
				this.baseColor = new Color(5, 70, 160);
				this.mainColor = new Color(255, 195, 37);
				this.secondaryColor = new Color(16, 31, 72);
				break;
			case LIGHTNING:
				this.baseColor = new Color(1, 62, 125);
				this.mainColor = new Color(255, 255, 255);
				this.secondaryColor = new Color(0, 0, 0);
				break;
			case MAPLE_LEAFS:
				this.baseColor = new Color(0, 55, 119);
				this.mainColor = new Color(255, 255, 255);
				this.secondaryColor = new Color(0, 0, 0);
				break;
			case CANUCKS:
				this.baseColor = new Color(7, 52, 111);
				this.mainColor = new Color(4, 122, 74);
				this.secondaryColor = new Color(168, 169, 173);
				break;
			case CAPITALS:
				this.baseColor = new Color(207, 19, 43);
				this.mainColor = new Color(0, 33, 78);
				this.secondaryColor = new Color(255, 255, 255);
				break;
			case JETS:
				this.baseColor = new Color(0, 46, 98);
				this.mainColor = new Color(255, 255, 255);
				this.secondaryColor = new Color(168, 169, 173);
				break;
			default:
				this.baseColor = Color.WHITE;
				this.mainColor = Color.BLACK;
				this.secondaryColor = Color.GRAY;
				break;
		}
	}
}
