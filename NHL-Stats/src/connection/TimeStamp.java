package connection;



public class TimeStamp {
	
	private byte period;
	private short elapsedSeconds;
	private short secondsLeft;

	public TimeStamp(byte period, short elapsedSeconds, short secondsLeft) {
		this.period = period;
		this.elapsedSeconds = elapsedSeconds;
		this.secondsLeft = secondsLeft;
	}
	
	public TimeStamp(){
		this((byte)-1, (short)-1, (short)-1);
	}
	
	public byte getPeriod() {
		return period;
	}
	
	public void setPeriod(byte period) {
		this.period = period;
	}
	
	public short getElapsedSeconds() {
		return elapsedSeconds;
	}
	
	public void setElapsedSeconds(short elapsedSeconds) {
		this.elapsedSeconds = elapsedSeconds;
	}
	
	public short getSecondsLeft() {
		return secondsLeft;
	}
	
	public void setSecondsLeft(short secondsLeft) {
		this.secondsLeft = secondsLeft;
	}

	@Override
	public String toString() {
		return "TimeStamp [period=" + period + ", elapsedSeconds="
				+ elapsedSeconds + ", secondsLeft=" + secondsLeft + "]";
	}
	
}