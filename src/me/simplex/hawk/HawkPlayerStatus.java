/*
 * Copyright 2012 s1mpl3x
 * 
 * This file is part of Nordic.
 * 
 * Nordic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Nordic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nordic If not, see <http://www.gnu.org/licenses/>.
 */
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
