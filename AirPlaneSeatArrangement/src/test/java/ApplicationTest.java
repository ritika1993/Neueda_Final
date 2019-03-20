import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.neueda.businessLogic.AssignAirPlanSeat;
import com.neueda.dto.AirPlaneSeatRowMapper;

public class ApplicationTest {

	private static String filePath1 = "./src/test/resources/input1.txt";
	private static String filePath2 = "./src/test/resources/input2.txt";

	@Test
	public void test100PercentCustomerSatisfaction() {

		AssignAirPlanSeat arrangement = new AssignAirPlanSeat();
		Map<Integer, AirPlaneSeatRowMapper> airPlaneMap = arrangement
				.arrangeSeatMain(filePath1);
		Assert.assertNotNull(airPlaneMap);
		Assert.assertEquals(100, arrangement.satisfiedPassengerPercentage());

	}

	@Test
	public void testUnSatisfiedCustomer() {

		AssignAirPlanSeat arrangement2 = new AssignAirPlanSeat();
		Map<Integer, AirPlaneSeatRowMapper> airPlaneMap2 = arrangement2
				.arrangeSeatMain(filePath2);
		Assert.assertNotNull(airPlaneMap2);
		Assert.assertEquals(92, arrangement2.satisfiedPassengerPercentage());

	}
}
