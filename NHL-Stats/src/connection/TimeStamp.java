package connection;



public class TimeStamp {
	
	private byte period;
	private short elapsedSeconds;
	private short secondsLeft;

	public TimeStamp(byte period, short elapsedSeconds, short secondsLeft) {
		super();
		this.period = period;
		this.elapsedSeconds = elapsedSeconds;
		this.secondsLeft = secondsLeft;
	}
	
	public byte getPeriod() {
		return period;
	}
	
	void setPeriod(byte period) {
		this.period = period;
	}
	
	public short getElapsedSeconds() {
		return elapsedSeconds;
	}
	
	void setElapsedSeconds(short elapsedSeconds) {
		this.elapsedSeconds = elapsedSeconds;
	}
	
	public short getSecondsLeft() {
		return secondsLeft;
	}
	
	void setSecondsLeft(short secondsLeft) {
		this.secondsLeft = secondsLeft;
	}

	@Override
	public String toString() {
		return "TimeStamp [period=" + period + ", elapsedSeconds="
				+ elapsedSeconds + ", secondsLeft=" + secondsLeft + "]";
	}
	
}