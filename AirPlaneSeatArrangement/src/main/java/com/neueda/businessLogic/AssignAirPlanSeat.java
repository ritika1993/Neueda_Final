package com.neueda.businessLogic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.neueda.dto.AirPlaneSeatRowMapper;
import com.neueda.dto.Passenger;
import com.neueda.dto.PassengerGroup;
import com.neueda.dto.Seat;

/***
 * This class has the logic implementation for assigning passengers their
 * preferred seats
 * 
 * @author Ritika Sao
 *
 */
public class AssignAirPlanSeat {

	private Map<Integer, AirPlaneSeatRowMapper> seatMap = new HashMap<>();
	private int totalNoOfAvailableSeats;
	private int satisfiedPassengerCount = 0;
	private int totalNumofPassenger = 0;

	/**
	 * Create Passenger from passenger string
	 * 
	 * @param passenger
	 *            Passenger string
	 * @param isWithGroup
	 *            If the passenger is from group or not
	 * @return Passenger instance
	 */
	private static Passenger createPassenger(String passenger) {

		Matcher matcher = Pattern.compile("([0-9]*)(W)?").matcher(passenger);
		if (!matcher.matches()) {
			throw new IllegalArgumentException(
					"Passenger data format is incorrect");
		}

		return new Passenger(Integer.valueOf(matcher.group(1)), 0, 0,
				matcher.group(2) != null);
	}

	/**
	 * Method to read the passenger info from file and assign the seats
	 * 
	 * @param filePath
	 *            Path of the file containing passenger info
	 * @return Seat map with assigned passenger
	 * @throws IllegalArgumentException
	 */
	public Map<Integer, AirPlaneSeatRowMapper> arrangeSeatMain(String filePath) {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			int inputLineNum = 1;
			String line;
			while ((line = br.readLine()) != null) {
				String[] inputArray = line.split("[\\s+]");

				if (inputLineNum == 1) {
					setSeatDimensionsNcreateEmptyArrangement(inputArray);
				} else {
					try {
						int totalPassenger = -1;
						totalNumofPassenger = totalNumofPassenger
								+ inputArray.length;
						if (inputArray.length >= 1) {
							// created passengers and collect in a list
							List<Passenger> passengerList = Arrays
									.stream(inputArray)
									.map(String::trim)
									.filter(s -> !s.isEmpty())
									.map(s -> AssignAirPlanSeat
											.createPassenger(s))
									.collect(Collectors.toList());
							// adding passengers to the Passenger Group
							PassengerGroup passengerGroup = new PassengerGroup(
									passengerList);
							totalPassenger = (passengerGroup
									.getListOfPassengers() != null ? passengerGroup
									.getListOfPassengers().size() : 0);
							int seatRowIndex = -1;
							for (AirPlaneSeatRowMapper seatRow : seatMap
									.values()) {
								if (seatRow.getRemaining() >= passengerGroup
										.getTotalPassengers()) {
									// checks if seats vacant in a row more than
									// passenger in a group
									// if yes, checks if any passenger in group
									// has window preference and a window seat
									// is vacant in that row
									if ((passengerGroup
											.getListOfPassengers()
											.stream()
											.filter(p -> p
													.isHasWindowSeatPreference())
											.count() > 0 ? true : false)
											&& seatRow
													.getAvailableWindowSeatsCount() > 0) {
										// assigns that seat to seatRowIndex
										seatRowIndex = seatRow
												.getSeatRowIndex();
										break;
									} else if (!(passengerGroup
											.getListOfPassengers()
											.stream()
											.filter(p -> p
													.isHasWindowSeatPreference())
											.count() > 0 ? true : false)) {
										// if no window preference, assigns the
										// seat to seatRow
										seatRowIndex = seatRow
												.getSeatRowIndex();
										break;
									}
								}
							}
							if (seatRowIndex >= 0) {
								assignSeatsForPassengerGroupbasedOnAvailability(
										seatRowIndex, passengerGroup);
								totalNoOfAvailableSeats = totalNoOfAvailableSeats
										- inputArray.length;
							} else if (totalNoOfAvailableSeats >= totalPassenger) {
								assignEmptySeatsForRemainingPassengerGroup(passengerGroup);
								totalNoOfAvailableSeats = totalNoOfAvailableSeats
										- inputArray.length;
							} else {
								System.out
										.println("No available seats for passengers"
												+ "Available Seats:"
												+ totalNoOfAvailableSeats
												+ "and Total Passenger in the group:"
												+ totalPassenger);
								return null;
							}
						}
					} catch (IllegalArgumentException ex) {
						System.out
								.println("This line cannot be executed contains error in format"
										+ line);
						ex.printStackTrace();
					}
				}
				inputLineNum++;
			}

			return seatMap;
		} catch (IOException e) {
			System.out.println("Could not able to read the passenger file . "
					+ e.getMessage());
		}
		return null;
	}

	/**
	 * THis method parse the dimensions for the seat map and initialise
	 *
	 * @param dimensions
	 *            The dimensions
	 */
	private void setSeatDimensionsNcreateEmptyArrangement(String[] dimensions) {
		if (dimensions.length < 2) {
			System.out
					.println("First line of the input dosen't have number of rows and no seats per row");
			return;
		}
		try {
			Integer numOfSeatsInARow = Integer.parseInt(dimensions[0]);
			Integer numOfRowsInPlane = Integer.parseInt(dimensions[1]);
			totalNoOfAvailableSeats = numOfSeatsInARow * numOfRowsInPlane;
			if (totalNoOfAvailableSeats == 0) {
				System.out
						.println("No seats can be arranged as the Plane doesn't have any Seats, please check first line of input");
				return;
			}
			initSeatArrangements(numOfRowsInPlane, numOfSeatsInARow);
		} catch (NumberFormatException e) {
			System.out.println("The dimensions provided are not valid ");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method initialise the seat map at the first time
	 * 
	 * @param numOfRowsInPlane
	 *            Number of the rows in the plane
	 * @param numOfSeatsInARow
	 *            Number of seats in one row
	 */
	private void initSeatArrangements(Integer numOfRowsInPlane,
			Integer numOfSeatsInARow) {
		for (int rowNum = 0; rowNum < numOfRowsInPlane.intValue(); rowNum++) {
			List<Seat> listOfSeatsInARow = new ArrayList<>();
			for (int seatNum = 0; seatNum < numOfSeatsInARow.intValue(); seatNum++) {
				boolean isWindowSeat = false;
				if (seatNum == 0 || seatNum == numOfSeatsInARow.intValue() - 1)
					isWindowSeat = true;
				listOfSeatsInARow.add(new Seat(seatNum, rowNum, isWindowSeat));
			}
			seatMap.put(rowNum, new AirPlaneSeatRowMapper(rowNum,
					numOfSeatsInARow, 2, listOfSeatsInARow));
		}
	}

	/**
	 * This method assign seats for the group of passengers
	 * 
	 * @param seatRowIndex
	 *            SeatRow index which have capacity for the passengers from
	 *            passenger group
	 * @param passengerGroup
	 *            The group of passengers
	 */
	private void assignSeatsForPassengerGroupbasedOnAvailability(
			int seatRowIndex, PassengerGroup passengerGroup) {
		AirPlaneSeatRowMapper seatRow = seatMap.get(seatRowIndex);
		List<Passenger> passengerList = new ArrayList<>();
		List<Passenger> listWithOutWindowPref = new ArrayList<>();
		if (passengerGroup.getListOfPassengers() != null
				&& (passengerGroup.getListOfPassengers().stream()
						.filter(p -> p.isHasWindowSeatPreference()).count() > 0 ? true
						: false)) {
			for (Passenger p : passengerGroup.getListOfPassengers()) {
				if (p.isHasWindowSeatPreference()) {
					passengerList.add(p);
				} else {
					listWithOutWindowPref.add(p);
				}
			}
			passengerList.addAll(listWithOutWindowPref);
		} else
			passengerList = passengerGroup.getListOfPassengers();
		int remainingPassenger = passengerList.size();
		List<Seat> seatList = seatRow.getListofSeatsInaRow();
		int emptySeatIndex = (int) seatList.stream()
				.filter(seat -> seat.getAssignedPassenger() == null)
				.findFirst().map(s -> s.getSeatNo()).orElse(-1);

		Seat windowSeatLeft = seatList.get(0);
		Seat windowSeatRight = seatList.get(seatRow.getTotalSize() - 1);

		for (Passenger passenger : passengerList) {
			if (passenger.isHasWindowSeatPreference()) {
				if (windowSeatLeft.getAssignedPassenger() == null) {
					seatList.get(0).setAssignedPassenger(passenger);
					emptySeatIndex++;

					remainingPassenger--;
					satisfiedPassengerCount++;
					int winSeatAvail = seatRow.getAvailableWindowSeatsCount();
					seatRow.setAvailableWindowSeatsCount((winSeatAvail--));
					continue;
				} else if (windowSeatRight.getAssignedPassenger() == null) {
					seatList.get(seatRow.getTotalSize() - 1)
							.setAssignedPassenger(passenger);
					remainingPassenger--;
					satisfiedPassengerCount++;
					emptySeatIndex = seatRow.getTotalSize()
							- remainingPassenger - 1;
					int winSeatAvail = seatRow.getAvailableWindowSeatsCount();
					seatRow.setAvailableWindowSeatsCount((winSeatAvail--));
					continue;
				}
			}
			seatList.get(emptySeatIndex++).setAssignedPassenger(passenger);
			satisfiedPassengerCount++;
		}
		seatRow.setRemaining(seatRow.getRemaining() - passengerList.size());
		seatMap.put(seatRowIndex, seatRow);

	}

	/**
	 * This method assigns remaining empty seats for the passengers
	 * 
	 * @param passengerGroup
	 * 
	 */
	private void assignEmptySeatsForRemainingPassengerGroup(
			PassengerGroup passengerGroup) {
		List<Passenger> passengerList = new ArrayList<>();
		List<Passenger> listWithOutWindowPref = new ArrayList<>();
		if (passengerGroup.getListOfPassengers() != null
				&& (passengerGroup.getListOfPassengers().stream()
						.filter(p -> p.isHasWindowSeatPreference()).count() > 0 ? true
						: false)) {
			for (Passenger p : passengerGroup.getListOfPassengers()) {
				if (p.isHasWindowSeatPreference()) {
					passengerList.add(p);
				} else {
					listWithOutWindowPref.add(p);
				}
			}
			passengerList.addAll(listWithOutWindowPref);
		} else
			passengerList = passengerGroup.getListOfPassengers();
		int i = 0;
		for (AirPlaneSeatRowMapper seatRow : seatMap.values()) {
			List<Seat> seatList = seatRow.getListofSeatsInaRow();
			for (Seat seat : seatList) {
				if (i == passengerList.size()) {
					return;
				} else if (seat.getAssignedPassenger() == null) {
					satisfiedPassengerCount++;
				}
			}
		}
	}

	/**
	 * This method return percentage of satisfied passengers
	 * 
	 * @return Percentage of satisfied passengeres
	 */
	public int satisfiedPassengerPercentage() {
		List<List<Seat>> result = seatMap.values().stream()
				.filter(e -> e.getListofSeatsInaRow().size() != 0)
				.map(e -> e.getListofSeatsInaRow())
				.collect(Collectors.toList());
		result.forEach(a -> {
			a.forEach(m -> {

				if (m.getAssignedPassenger() == null)
					satisfiedPassengerCount--;

			});

		});

		return (satisfiedPassengerCount) * 100 / (totalNumofPassenger);

	}

}
