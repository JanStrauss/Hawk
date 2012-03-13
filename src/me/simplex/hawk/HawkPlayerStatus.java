package me.simplex.hawk;

public class HawkPlayerStatus {
	public static enum Flystate{FLY, HOVER}
	
	private Flystate state = Flystate.FLY;
	private int time_since_consume = 0;
	
	public Flystate getState() {
		return state;
	}

	public void setState(Flystate state) {
		this.state = state;
	}

	public int getTime_since_consume() {
		return time_since_consume;
	}

	public void increaseTime() {
		time_since_consume++;
	}
	
	public void resetTime(){
		time_since_consume = 0;
	}
}
