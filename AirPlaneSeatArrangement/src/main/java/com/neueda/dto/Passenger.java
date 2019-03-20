package com.neueda.dto;
/***
 * DTO for maintaining passenger info
 * @author Ritika Sao
 *
 */
public class Passenger {

	private int passengerId;
	private int score;
	private int seatNum;
	private boolean hasWindowSeatPreference;
	
	public Passenger(int passengerId, int score, int seatNum, boolean hasWindowSeatPreference) {
		super();
		this.passengerId = passengerId;
		this.score = score;
		this.seatNum = seatNum;
		this.hasWindowSeatPreference = hasWindowSeatPreference;
	}
	public int getPassengerId() {
		return passengerId;
	}
	public void setPassengerId(int passengerId) {
		this.passengerId = passengerId;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getSeatNum() {
		return seatNum;
	}
	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}
	public boolean isHasWindowSeatPreference() {
		return hasWindowSeatPreference;
	}
	public void setHasWindowSeatPreference(boolean hasWindowSeatPreference) {
		this.hasWindowSeatPreference = hasWindowSeatPreference;
	}
	@Override
	public String toString() {
		return "Passenger [passengerId=" + passengerId + ", score=" + score
				+ ", seatNum=" + seatNum + ", hasWindowSeatPreference="
				+ hasWindowSeatPreference + "]";
	}

	

}
