package com.graphQL.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.*;

import com.graphQL.utility.BaseClass;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class LocationAPIs extends BaseClass {

	private String getAllLocations_query = getRequestBody("allLocations");
	private String createLocation_query = getRequestBody("createLocation");

	private String location_Id = "";
	private String p_Location_Name = "";
	private String c_Location_Name = "";

	String pNumber = "P" + get4DigitRandomNumber();
	String cNumber = "C" + get4DigitRandomNumber();

	@DataProvider
	public Object[][] pLocationValidCombination() {
		return new Object[][] { { pNumber, "New Orleans", "2922cb64-dcb9-463a-b8a3-7d104de26cc7", "Premium" } };
	}

	@DataProvider
	public Object[][] cLocationValidCombination() {
		return new Object[][] { { cNumber, "New Orleans", "9d0ccf0f-ae71-4efb-8ece-90554ae8fde5", "EV Rates" } };
	}

	@DataProvider
	public Object[][] cLocationInvalidCombination() {
		return new Object[][] { { cNumber, "New Orleans", "2922cb64-dcb9-463a-b8a3-7d104de26cc7", "Premium" } };
	}

	@DataProvider
	public Object[][] pLocationInvalidCombination() {
		return new Object[][] { { pNumber, "New Orleans", "2b1ebe79-b8f2-4812-b348-fdf3aa87d5c5", "Premium" } };
	}

	@DataProvider
	public Object[][] locationId() {
		return new Object[][] { { location_Id } };
	}

	@DataProvider
	public Object[][] pLocationName() {
		return new Object[][] { { p_Location_Name } };
	}

	@DataProvider
	public Object[][] cLocationName() {
		return new Object[][] { { c_Location_Name } };
	}

	@Test(dataProvider = "pLocationValidCombination", priority = 1)
	public void TC_01_Create_Location_as_PP_Operator(String pNumber, String address, String operator_id, String rate) {

		createLocation_query = "{\"query\":\"mutation addNewLocation($input: LocationDtoInput){\\n  addLocation(input: $input) {\\n    id\\n    name\\n    parkingOperator{\\n      id\\n    }\\n    address\\n  \\trates\\n \\t  status\\n   \\n  }\\n}\",\"variables\":{\"input\":{\"name\":\""
				+ pNumber + "\",\"address\":\"" + address + "\",\"parkingOperator\":{\"id\":\"" + operator_id
				+ "\"},\"rates\":\"" + rate + "\"}},\"operationName\":\"addNewLocation\"}";

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");
		passStep(createLocation_query);

		Response resp = given()

				.body(createLocation_query).when().post();

		stepInfo("Response Body");
		passStep(resp.asString());

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());

		location_Id = j.getString("data.addLocation.id");
		p_Location_Name = j.getString("data.addLocation.name");

		passStep("Location 'Id' recieved in response : " + location_Id);
		passStep("Location 'Name' recieved in response : " + p_Location_Name);
		passStep("Location 'Parking Operator Id' recieved in response : "
				+ j.getString("data.addLocation.parkingOperator.id"));
		passStep("Location 'Address' recieved in response : " + j.getString("data.addLocation.address"));
		passStep("Location 'Status' recieved in response : " + j.getString("data.addLocation.status"));
		passStep("Location 'Rate Structure' recieved in response : " + j.getString("data.addLocation.rates"));

		assertEquals(p_Location_Name, pNumber);
		assertEquals(j.getString("data.addLocation.address"), address);
		assertEquals(j.getString("data.addLocation.parkingOperator.id"), operator_id);
		assertEquals(j.getString("data.addLocation.rates"), rate);
		assertEquals(j.getString("data.addLocation.status"), "UNPUBLISH");

	}

	@Test(dataProvider = "cLocationValidCombination", priority = 2)
	public void TC_02_Create_Location_as_Other_Operator(String cNumber, String address, String operator_id,
			String rate) {

		createLocation_query = "{\"query\":\"mutation addNewLocation($input: LocationDtoInput){\\n  addLocation(input: $input) {\\n    id\\n    name\\n    parkingOperator{\\n      id\\n    }\\n    address\\n  \\trates\\n \\t  status\\n   \\n  }\\n}\",\"variables\":{\"input\":{\"name\":\""
				+ cNumber + "\",\"address\":\"" + address + "\",\"parkingOperator\":{\"id\":\"" + operator_id
				+ "\"},\"rates\":\"" + rate + "\"}},\"operationName\":\"addNewLocation\"}";
		RestAssured.baseURI = uri;
		stepInfo("Request Payload");
		passStep(createLocation_query);

		Response resp = given()

				.body(createLocation_query).when().post();

		stepInfo("Response Body");
		passStep(resp.asString());

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());

		c_Location_Name = j.getString("data.addLocation.name");

		passStep("Location 'Id' recieved in response : " + j.getString("data.addLocation.id"));
		passStep("Location 'Name' recieved in response : " + c_Location_Name);
		passStep("Location 'Parking Operator Id' recieved in response : "
				+ j.getString("data.addLocation.parkingOperator.id"));
		passStep("Location 'Address' recieved in response : " + j.getString("data.addLocation.address"));
		passStep("Location 'Status' recieved in response : " + j.getString("data.addLocation.status"));
		passStep("Location 'Rate Structure' recieved in response : " + j.getString("data.addLocation.rates"));

		assertEquals(c_Location_Name, cNumber);
		assertEquals(j.getString("data.addLocation.address"), address);
		assertEquals(j.getString("data.addLocation.parkingOperator.id"), operator_id);
		assertEquals(j.getString("data.addLocation.rates"), rate);
		assertEquals(j.getString("data.addLocation.status"), "UNPUBLISH");

	}

	@Test(dataProvider = "cLocationInvalidCombination", priority = 3)
	public void TC_03_Create_Location_as_PP_Operator_With_CNumber(String cNumber, String address, String operator_id,
			String rate) {

		createLocation_query = "{\"query\":\"mutation addNewLocation($input: LocationDtoInput){\\n  addLocation(input: $input) {\\n    id\\n    name\\n    parkingOperator{\\n      id\\n    }\\n    address\\n  \\trates\\n \\t  status\\n   \\n  }\\n}\",\"variables\":{\"input\":{\"name\":\""
				+ cNumber + "\",\"address\":\"" + address + "\",\"parkingOperator\":{\"id\":\"" + operator_id
				+ "\"},\"rates\":\"" + rate + "\"}},\"operationName\":\"addNewLocation\"}";

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");
		passStep(createLocation_query);

		Response resp = given()

				.body(createLocation_query).when().post();

		stepInfo("Response Body");
		passStep(resp.asString());

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(resp.asString());

		passStep("Error message recieved in response : " + j.getString("errors.message"));

	}

	@Test(dataProvider = "pLocationInvalidCombination", priority = 4)
	public void TC_04_Create_Location_as_Other_Operator_With_PNumber(String pNumber, String address, String operator_id,
			String rate) {

		createLocation_query = "{\"query\":\"mutation addNewLocation($input: LocationDtoInput){\\n  addLocation(input: $input) {\\n    id\\n    name\\n    parkingOperator{\\n      id\\n    }\\n    address\\n  \\trates\\n \\t  status\\n   \\n  }\\n}\",\"variables\":{\"input\":{\"name\":\""
				+ pNumber + "\",\"address\":\"" + address + "\",\"parkingOperator\":{\"id\":\"" + operator_id
				+ "\"},\"rates\":\"" + rate + "\"}},\"operationName\":\"addNewLocation\"}";

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");
		passStep(createLocation_query);

		Response resp = given()

				.body(createLocation_query).when().post();

		stepInfo("Response Body");
		passStep(resp.asString());

		stepInfo("Response Validation");

		JsonPath j = new JsonPath(resp.asString());

		passStep("Error message recieved in response : " + j.getString("errors.message"));

	}

	@Test(priority = 5)
	public void TC_05_Create_Bulk_Locations_with_Valid_Combinations() {

		String[] loc1 = new String[] { "P" + get4DigitRandomNumber(), "New York",
				"2922cb64-dcb9-463a-b8a3-7d104de26cc7", "Premium" };
		String[] loc2 = new String[] { "C" + get4DigitRandomNumber(), "Alaska", "9d0ccf0f-ae71-4efb-8ece-90554ae8fde5",
				"Default" };
		String[] loc3 = new String[] { "P" + get4DigitRandomNumber(), "New York",
				"2922cb64-dcb9-463a-b8a3-7d104de26cc7", "EV" };

		createLocation_query = "{\"query\":\"mutation bulkAddLocation($input: [LocationDtoInput]){\\n  bulkAddLocation(input: $input)\\n}\",\"variables\":{\"input\":[{\"name\":\""
				+ loc1[0] + "\",\"address\":\"" + loc1[1] + "\",\"parkingOperator\":{\"id\":\"" + loc1[2]
				+ "\"},\"rates\":\"" + loc1[3] + "\"},{\"name\":\"" + loc2[0] + "\",\"address\":\"" + loc2[1]
				+ "\",\"parkingOperator\":{\"id\":\"" + loc2[2] + "\"},\"rates\":\"" + loc2[3] + "\"},{\"name\":\""
				+ loc3[0] + "\",\"address\":\"" + loc3[1] + "\",\"parkingOperator\":{\"id\":\"" + loc3[2]
				+ "\"},\"rates\":\"" + loc3[3] + "\"}]},\"operationName\":\"bulkAddLocation\"}";

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");
		passStep(createLocation_query);

		Response resp = given()

				.body(createLocation_query).when().post();

		stepInfo("Response Body");
		passStep(resp.asString());

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());

		int loc_Count = j.getInt("data.bulkAddLocation.response.data.size()");
		int error_Count = j.getInt("data.bulkAddLocation.response.errors.size()");
		passStep("Found <b>" + loc_Count + "</b> locations");
		if (loc_Count != 3) {
			failStep("Failed to create 3 locations <span> &#x1F61F; </span> ");
		}
		if (error_Count == 0) {
			passStep("No Errors while creating bulk locations");
		} else {
			failStep(
					"Errors received while creating bulk locations <span> &#x1F61F; </span> (Please refer response body)");
		}

		for (int i = 0; i < loc_Count; i++) {

			stepInfo("Location " + (i + 1) + " : ");
			passStep("Location 'Id' recieved in response : "
					+ j.getString("data.bulkAddLocation.response.data[" + i + "].id"));
			passStep("Location 'Name' recieved in response : "
					+ j.getString("data.bulkAddLocation.response.data[" + i + "].name"));
			passStep("Location 'Parking Operator Id' recieved in response : "
					+ j.getString("data.bulkAddLocation.response.data[" + i + "].parkingOperator.id"));
			passStep("Location 'Address' recieved in response : "
					+ j.getString("data.bulkAddLocation.response.data[" + i + "].address"));
			passStep("Location 'Status' recieved in response : "
					+ j.getString("data.bulkAddLocation.response.data[" + i + "].status"));
			passStep("Location 'Rate Structure' recieved in response : "
					+ j.getString("data.bulkAddLocation.response.data[" + i + "].rates"));

		}
		assertEquals(j.getString("data.bulkAddLocation.response.data[0].name"), loc1[0]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[0].address"), loc1[1]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[0].parkingOperator.id"), loc1[2]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[0].rates"), loc1[3]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[0].status"), "UNPUBLISH");

		assertEquals(j.getString("data.bulkAddLocation.response.data[1].name"), loc2[0]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[1].address"), loc2[1]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[1].parkingOperator.id"), loc2[2]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[1].rates"), loc2[3]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[1].status"), "UNPUBLISH");

		assertEquals(j.getString("data.bulkAddLocation.response.data[2].name"), loc3[0]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[2].address"), loc3[1]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[2].parkingOperator.id"), loc3[2]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[2].rates"), loc3[3]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[2].status"), "UNPUBLISH");

	}

	@Test(priority = 6)
	public void TC_06_Create_Bulk_Locations_with_One_Invalid_Combination_PnumberWithOtherOperator() {

		String[] loc1 = new String[] { "P" + get4DigitRandomNumber(), "New York",
				"2922cb64-dcb9-463a-b8a3-7d104de26cc7", "Premium" };
		String[] loc2 = new String[] { "C" + get4DigitRandomNumber(), "Alaska", "9d0ccf0f-ae71-4efb-8ece-90554ae8fde5",
				"Default" };
		String[] loc3 = new String[] { "P" + get4DigitRandomNumber(), "New York",
				"aa29821c-10bf-4fae-b7e8-0323091d973c", "EV" };

		stepInfo("Trying with invalid combination " + loc3[2] + "(non PP operator) with PNumber " + loc3[0]);

		createLocation_query = "{\"query\":\"mutation bulkAddLocation($input: [LocationDtoInput]){\\n  bulkAddLocation(input: $input)\\n}\",\"variables\":{\"input\":[{\"name\":\""
				+ loc1[0] + "\",\"address\":\"" + loc1[1] + "\",\"parkingOperator\":{\"id\":\"" + loc1[2]
				+ "\"},\"rates\":\"" + loc1[3] + "\"},{\"name\":\"" + loc2[0] + "\",\"address\":\"" + loc2[1]
				+ "\",\"parkingOperator\":{\"id\":\"" + loc2[2] + "\"},\"rates\":\"" + loc2[3] + "\"},{\"name\":\""
				+ loc3[0] + "\",\"address\":\"" + loc3[1] + "\",\"parkingOperator\":{\"id\":\"" + loc3[2]
				+ "\"},\"rates\":\"" + loc3[3] + "\"}]},\"operationName\":\"bulkAddLocation\"}";

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");
		passStep(createLocation_query);

		Response resp = given()

				.body(createLocation_query).when().post();

		stepInfo("Response Body");
		passStep(resp.asString());

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());

		int error_Count = j.getInt("data.bulkAddLocation.response.errors.size()");
		passStep("Found errors in <b>" + error_Count + "</b> locations");

		for (int i = 0; i < error_Count; i++) {
			String nameError = j.getString("data.bulkAddLocation.response.errors[" + i + "].name.error");
			String parkingOperatorError = j
					.getString("data.bulkAddLocation.response.errors[" + i + "].parkingOperator.error");

			if (nameError.equals("true")) {
				String value = j.getString("data.bulkAddLocation.response.errors[" + i + "].name.value");
				String message = j.getString("data.bulkAddLocation.response.errors[" + i + "].name.message");

				passStep("Failed to create the location with name " + value + " due to error " + message);
			} else if (parkingOperatorError.equals("true")) {
				String value = j.getString("data.bulkAddLocation.response.errors[" + i + "].parkingOperator.value");
				String pnumber = j.getString("data.bulkAddLocation.response.errors[" + i + "].name.value");
				String message = j.getString("data.bulkAddLocation.response.errors[" + i + "].parkingOperator.message");

				passStep("Failed to create the location with Operator id <b>" + value + "</b> and P Number <b>"
						+ pnumber + "</b> due to error <b>" + message + "</b>");
			}

		}

	}

	@Test(dataProvider = "locationId", priority = 7)
	public void TC_07_Change_Location_Status_From_UnPublish_to_Publish(String loc_id) {

		String updateLocation_query = "{\"query\":\"mutation toggleLocationStatus($id: String) {\\n  updateLocationStatus(id:$id){\\n    id\\n    status\\n  }\\n}\",\"variables\":{\"id\":\""
				+ loc_id + "\"},\"operationName\":\"toggleLocationStatus\"}";

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");
		passStep(updateLocation_query);

		Response resp = given()

				.body(updateLocation_query).when().post();

		stepInfo("Response Body");
		passStep(resp.asString());

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());

		passStep("Location 'Id' recieved in response : " + j.getString("data.updateLocationStatus.id"));
		passStep("Location 'Status' recieved in response : " + j.getString("data.updateLocationStatus.status"));

		assertEquals(j.getString("data.updateLocationStatus.id"), loc_id);
		assertEquals(j.getString("data.updateLocationStatus.status"), "PUBLISH");

	}

	@Test(dataProvider = "locationId", priority = 8)
	public void TC_08_Change_Location_Status_From_Publish_to_UnPublish(String loc_id) {

		String updateLocation_query = "{\"query\":\"mutation toggleLocationStatus($id: String) {\\n  updateLocationStatus(id:$id){\\n    id\\n    status\\n  }\\n}\",\"variables\":{\"id\":\""
				+ loc_id + "\"},\"operationName\":\"toggleLocationStatus\"}";

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");
		passStep(updateLocation_query);

		Response resp = given()

				.body(updateLocation_query).when().post();

		stepInfo("Response Body");
		passStep(resp.asString());

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());

		passStep("Location 'Id' recieved in response : " + j.getString("data.updateLocationStatus.id"));
		passStep("Location 'Status' recieved in response : " + j.getString("data.updateLocationStatus.status"));

		assertEquals(j.getString("data.updateLocationStatus.id"), loc_id);
		assertEquals(j.getString("data.updateLocationStatus.status"), "UNPUBLISH");

	}

	@Test(dataProvider = "pLocationValidCombination", priority = 9)
	public void TC_09_Get_Location_By_PNumber(String pNumber, String address, String operator, String rate) {

		String getLocation_query = "{\"query\":\"query getLocationByName($name: String){\\n  \\tgetLocationByName(name:$name){\\n    name\\n    parkingOperator\\n    {id}\\n    rates\\n    address\\n    oldLocation {\\n      monthlyParkingUrl\\n      colorThemeId\\n      options\\n      deadRate\\n      reservationInstructions\\n      vehicleInformationHint\\n    }\\n  }\\n}\",\"variables\":{\"name\":\""
				+ pNumber + "\"},\"operationName\":\"getLocationByName\"}";

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");
		passStep(getLocation_query);

		Response resp = given()

				.body(getLocation_query).when().post();

		stepInfo("Response Body");
		passStep(resp.asString());

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());

		passStep("Location 'Name' recieved in response : " + j.getString("data.getLocationByName.name"));
		passStep("Location 'Parking Operator Id' recieved in response : "
				+ j.getString("data.getLocationByName.parkingOperator.id"));
		passStep("Location 'Address' recieved in response : " + j.getString("data.getLocationByName.address"));
		passStep("Location 'Rate Structure' recieved in response : " + j.getString("data.getLocationByName.rates"));

		assertEquals(j.getString("data.getLocationByName.name"), pNumber);
		assertEquals(j.getString("data.getLocationByName.address"), address);
		assertEquals(j.getString("data.getLocationByName.parkingOperator.id"), operator);
		assertEquals(j.getString("data.getLocationByName.rates"), rate);

	}

	@Test(dataProvider = "cLocationValidCombination", priority = 10)
	public void TC_10_Get_Location_By_CNumber(String cNumber, String address, String operator, String rate) {

		String getLocation_query = "{\"query\":\"query getLocationByName($name: String){\\n  \\tgetLocationByName(name:$name){\\n    name\\n    parkingOperator\\n    {id}\\n    rates\\n    address\\n    oldLocation {\\n      monthlyParkingUrl\\n      colorThemeId\\n      options\\n      deadRate\\n      reservationInstructions\\n      vehicleInformationHint\\n    }\\n  }\\n}\",\"variables\":{\"name\":\""
				+ cNumber + "\"},\"operationName\":\"getLocationByName\"}";

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");
		passStep(getLocation_query);

		Response resp = given()

				.body(getLocation_query).when().post();

		stepInfo("Response Body");
		passStep(resp.asString());

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());

		passStep("Location 'Name' recieved in response : " + j.getString("data.getLocationByName.name"));
		passStep("Location 'Parking Operator Id' recieved in response : "
				+ j.getString("data.getLocationByName.parkingOperator.id"));
		passStep("Location 'Address' recieved in response : " + j.getString("data.getLocationByName.address"));
		passStep("Location 'Rate Structure' recieved in response : " + j.getString("data.getLocationByName.rates"));

		assertEquals(j.getString("data.getLocationByName.name"), cNumber);
		assertEquals(j.getString("data.getLocationByName.address"), address);
		assertEquals(j.getString("data.getLocationByName.parkingOperator.id"), operator);
		assertEquals(j.getString("data.getLocationByName.rates"), rate);

	}

	@Test(dataProvider = "pLocationValidCombination", priority = 11)
	public void TC_11_Get_Location_By_Id(String pNumber, String address, String operator, String rate) {

		String getLocation_query = "{\"query\":\"query getLocationById($id: String){\\n  locationItem(id:$id){\\n    name\\n    parkingOperator{id\\n    name}\\n    rates\\n    address   \\n  }\\n}\",\"variables\":{\"id\":\""
				+ location_Id + "\"},\"operationName\":\"getLocationById\"}";

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");
		passStep(getLocation_query);

		Response resp = given()

				.body(getLocation_query).when().post();

		stepInfo("Response Body");
		passStep(resp.asString());

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());

		passStep("Location 'Name' recieved in response : " + j.getString("data.locationItem.name"));
		passStep("Location 'Parking Operator Id' recieved in response : "
				+ j.getString("data.locationItem.parkingOperator.id"));
		passStep("Location 'Address' recieved in response : " + j.getString("data.locationItem.address"));
		passStep("Location 'Rate Structure' recieved in response : " + j.getString("data.locationItem.rates"));

		assertEquals(j.getString("data.locationItem.name"), pNumber);
		assertEquals(j.getString("data.locationItem.address"), address);
		assertEquals(j.getString("data.locationItem.parkingOperator.id"), operator);
		assertEquals(j.getString("data.locationItem.rates"), rate);

	}

	@Test(priority = 12)
	public void TC_12_get_AllLocations() {

		String getAllLocationsQuery = "{\"query\":\"query getAllLocations{\\n\\tgetAllLocations{\\n    id\\n    name\\n    address\\n    parkingOperator\\n    {\\n      id\\n      name\\n    }\\n    rates\\n  }\\n}\",\"variables\":{},\"operationName\":\"getAllLocations\"}";

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");
		passStep(getAllLocations_query);

		Response resp = given()

				.body(getAllLocationsQuery).when().post();

		stepInfo("Response Validation");

		passStep("Received status code : " + resp.getStatusCode());
		assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());

		// get values of JSON array after getting array size
		int loc_Count = j.getInt("data.getAllLocations.size()");
		passStep("Found <b>" + loc_Count + "</b> locations");
		for (int i = 0; i < loc_Count; i++) {

			String loc_name = j.getString("data.getAllLocations[" + i + "].name");
			String address = j.getString("data.getAllLocations[" + i + "].address");
			String operator_id = j.getString("data.getAllLocations[" + i + "].parkingOperator.id");
			String operator_name = j.getString("data.getAllLocations[" + i + "].parkingOperator.name");
			passStep((i + 1) + ")  Location Name : " + loc_name + ", Location Address : " + address
					+ ",  Parking Operator Id: " + operator_id + ",  Parking Operator Name: " + operator_name);

		}

	}

}
