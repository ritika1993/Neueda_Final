package com.neueda.dto;

/***
 * DTO for maintaining seat information
 * @author Ritika Sao
 *
 */
public class Seat {

	private int seatNo;
	private int rowNo;
	private boolean isWindowSeat;
	private Passenger assignedPassenger;	
	
	public Seat(int seatNo, int rowNo, boolean isWindowSeat) {
		this.seatNo = seatNo;
		this.rowNo = rowNo;
		this.isWindowSeat = isWindowSeat;
	}
	public int getSeatNo() {
		return seatNo;
	}
	public void setSeatNo(int seatNo) {
		this.seatNo = seatNo;
	}
	public int getRowNo() {
		return rowNo;
	}
	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}
	public boolean isWindowSeat() {
		return isWindowSeat;
	}
	public Passenger getAssignedPassenger() {
		return assignedPassenger;
	}
	public void setAssignedPassenger(Passenger assignedPassenger) {
		this.assignedPassenger = assignedPassenger;
	}
	@Override
	public String toString() {
		return "Seat [seatNo=" + seatNo + ", rowNo=" + rowNo
				+ ", isWindowSeat=" + isWindowSeat + ", assignedPassenger="
				+ assignedPassenger + "]";
	}	
	
	
}
