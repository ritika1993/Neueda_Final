package com.neueda.dto;

import java.util.List;
/***
 * DTO for maintaining passenger group info
 * @author Ritika Sao
 *
 */
public class PassengerGroup {

	public List<Passenger> listOfPassengers;
	
	public PassengerGroup(List<Passenger> listOfPassengers) {
		this.listOfPassengers = listOfPassengers ;
	}

	public List<Passenger> getListOfPassengers() {
		return listOfPassengers;
	}

	public int getTotalPassengers() {
		return listOfPassengers != null ? listOfPassengers.size() : 0;
	}
}
