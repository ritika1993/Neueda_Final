package com.neueda.dto;

import java.util.List;

/***
 * DTO for a air plan seat row 
 * @author Ritika Sao
 *
 */
public class AirPlaneSeatRowMapper {

	private int seatRowIndex;
	private int remaining;
	private int totalSize;
	private int availableWindowSeatsCount;
	private List<Seat> listofSeatsInaRow;

	public AirPlaneSeatRowMapper(int seatRowIndex, int totalSize, int availableWindowSeatsCount,
			List<Seat> listofSeatsInaRow) {
		super();
		this.seatRowIndex = seatRowIndex;
		this.remaining = totalSize;
		this.totalSize = totalSize;
		this.availableWindowSeatsCount = availableWindowSeatsCount;
		this.listofSeatsInaRow = listofSeatsInaRow;
	}


	public int getSeatRowIndex() {
		return seatRowIndex;
	}


	public void setSeatRowIndex(int seatRowIndex) {
		this.seatRowIndex = seatRowIndex;
	}


	public int getRemaining() {
		return remaining;
	}


	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}


	public int getTotalSize() {
		return totalSize;
	}


	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}


	public int getAvailableWindowSeatsCount() {
		return availableWindowSeatsCount;
	}


	public void setAvailableWindowSeatsCount(int availableWindowSeatsCount) {
		this.availableWindowSeatsCount = availableWindowSeatsCount;
	}


	public List<Seat> getListofSeatsInaRow() {
		return listofSeatsInaRow;
	}


	public void setListofSeatsInaRow(List<Seat> listofSeatsInaRow) {
		this.listofSeatsInaRow = listofSeatsInaRow;
	}


	/**
	 * Return the view of the seat row
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Seat seat : listofSeatsInaRow) {
			sb.append(seat.getAssignedPassenger() != null ? seat.getAssignedPassenger().getPassengerId() : 0)
					.append(" ");
		}
		return sb.toString();
	}
}
