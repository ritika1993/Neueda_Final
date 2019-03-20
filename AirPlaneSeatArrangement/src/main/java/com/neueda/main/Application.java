package com.neueda.main;

import java.util.Map;
import java.util.stream.Collectors;

import com.neueda.businessLogic.AssignAirPlanSeat;
import com.neueda.dto.AirPlaneSeatRowMapper;
/***
 * This application is intended to suggest a seat map plan
 * for an airplane based on requests coming from the passengers
 * 
 * An input line is assumed as a group of passengers and a "W" indicates window preference
 * Note: This application assumes that the no. of passenger are equivalent to no. of seats
 * 		 Group are given priority and then the window preference
 * @author Ritika Sao
 *
 */
public class Application {

	private static final String FILEPATH = "./src/main/resources/file.txt";

	/**
	 * Entry point of the application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AssignAirPlanSeat arrangement = new AssignAirPlanSeat();
			Map<Integer, AirPlaneSeatRowMapper> airPlaneMap = arrangement
					.arrangeSeatMain(FILEPATH);
			if (airPlaneMap != null) {
				String arrangedSeatMap = airPlaneMap.values().stream()
						.map(s -> s.toString())
						.collect(Collectors.joining(System.lineSeparator()));
				StringBuilder sb = new StringBuilder();

				sb.append("Final Seat Map ")
						.append(System.lineSeparator())
						.append(arrangedSeatMap)
						.append(System.lineSeparator())
						.append(arrangement.satisfiedPassengerPercentage()
								+ "%");
				System.out.print(sb.toString());
			} else {
				System.out
						.print("Couldn't process seat mapping, error in input!!");
			}

		} catch (Exception e) {
			System.out.println("Error!!" + e.getMessage());
			e.printStackTrace();
		}
	}

}
