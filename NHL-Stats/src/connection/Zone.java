/*    
 *  ____________________
 * /      |      |      \
 * | D 1  |  2   |  3 D |
 * \______|______|______/
 *   Home          Away
 * For all periods!  Zone 1 becomes zone 3 in period 2 and then back in period 3.
 * Zone 1 will always be the home teams defense zone regardless of period.
 * 
 */

package connection;

public enum Zone {
	OFF(1), NEU(2), DEF(3);
	
	private int zone;
	
	private Zone(int i){
		this.zone = i;
	}
	
	public int value(){
		return zone;
	}
}
