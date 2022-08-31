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
		return new Object[][] { { pNumber, "New Orleans", "Premium Parking", "Premium" } };
	}

	@DataProvider
	public Object[][] cLocationValidCombination() {
		return new Object[][] { { cNumber, "New Orleans", "ABC Parking", "EV Rates" } };
	}

	@DataProvider
	public Object[][] cLocationInvalidCombination() {
		return new Object[][] { { "C" + get4DigitRandomNumber(), "New Orleans", "Premium Parking", "Premium" } };
	}

	@DataProvider
	public Object[][] pLocationInvalidCombination() {
		return new Object[][] { { "P" + get4DigitRandomNumber(), "New Orleans", "Test Parking", "Premium" } };
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
	public void TC_01_Create_Location_as_PP_Operator(String pNumber, String address, String operator, String rate) {

		createLocation_query = "{\"query\":\"mutation addNewLocation($input: LocationDtoInput){\\n  addLocation(input: $input) {\\n    id\\n    name\\n    parkingOperator\\n    address\\n  \\trates\\n \\t  status\\n  \\n      }\\n}\",\"variables\":{\"input\":{\"name\":\""
				+ pNumber + "\",\"address\":\"" + address + "\",\"parkingOperator\":\"" + operator + "\",\"rates\":\""
				+ rate + "\"}},\"operationName\":\"addNewLocation\"}";

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
		passStep("Location 'Parking Operator' recieved in response : "
				+ j.getString("data.addLocation.parkingOperator"));
		passStep("Location 'Address' recieved in response : " + j.getString("data.addLocation.address"));
		passStep("Location 'Status' recieved in response : " + j.getString("data.addLocation.status"));
		passStep("Location 'Rate Structure' recieved in response : " + j.getString("data.addLocation.rates"));

		assertEquals(p_Location_Name, pNumber);
		assertEquals(j.getString("data.addLocation.address"), address);
		assertEquals(j.getString("data.addLocation.parkingOperator"), operator);
		assertEquals(j.getString("data.addLocation.rates"), rate);
		assertEquals(j.getString("data.addLocation.status"), "UNPUBLISH");

	}

	@Test(dataProvider = "cLocationValidCombination", priority = 2)
	public void TC_02_Create_Location_as_Other_Operator(String cNumber, String address, String operator, String rate) {

		createLocation_query = "{\"query\":\"mutation addNewLocation($input: LocationDtoInput){\\n  addLocation(input: $input) {\\n    id\\n    name\\n    parkingOperator\\n    address\\n  \\trates\\n \\t  status\\n  \\n      }\\n}\",\"variables\":{\"input\":{\"name\":\""
				+ cNumber + "\",\"address\":\"" + address + "\",\"parkingOperator\":\"" + operator + "\",\"rates\":\""
				+ rate + "\"}},\"operationName\":\"addNewLocation\"}";

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
		passStep("Location 'Parking Operator' recieved in response : "
				+ j.getString("data.addLocation.parkingOperator"));
		passStep("Location 'Address' recieved in response : " + j.getString("data.addLocation.address"));
		passStep("Location 'Status' recieved in response : " + j.getString("data.addLocation.status"));
		passStep("Location 'Rate Structure' recieved in response : " + j.getString("data.addLocation.rates"));

		assertEquals(c_Location_Name, cNumber);
		assertEquals(j.getString("data.addLocation.address"), address);
		assertEquals(j.getString("data.addLocation.parkingOperator"), operator);
		assertEquals(j.getString("data.addLocation.rates"), rate);
		assertEquals(j.getString("data.addLocation.status"), "UNPUBLISH");

	}

	@Test(dataProvider = "cLocationInvalidCombination", priority = 3)
	public void TC_03_Create_Location_as_PP_Operator_With_CNumber(String cNumber, String address, String operator,
			String rate) {

		createLocation_query = "{\"query\":\"mutation addNewLocation($input: LocationDtoInput){\\n  addLocation(input: $input) {\\n    id\\n    name\\n    parkingOperator\\n    address\\n  \\trates\\n \\t  status\\n  \\n      }\\n}\",\"variables\":{\"input\":{\"name\":\""
				+ cNumber + "\",\"address\":\"" + address + "\",\"parkingOperator\":\"" + operator + "\",\"rates\":\""
				+ rate + "\"}},\"operationName\":\"addNewLocation\"}";

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");
		passStep(createLocation_query);

		Response resp = given()

				.body(createLocation_query).when().post();

		stepInfo("Response Body");
		passStep(resp.asString());

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		assertEquals(resp.getStatusCode(), 400);

	}

	@Test(dataProvider = "pLocationInvalidCombination", priority = 4)
	public void TC_04_Create_Location_as_Other_Operator_With_PNumber(String pNumber, String address, String operator,
			String rate) {

		createLocation_query = "{\"query\":\"mutation addNewLocation($input: LocationDtoInput){\\n  addLocation(input: $input) {\\n    id\\n    name\\n    parkingOperator\\n    address\\n  \\trates\\n \\t  status\\n  \\n      }\\n}\",\"variables\":{\"input\":{\"name\":\""
				+ pNumber + "\",\"address\":\"" + address + "\",\"parkingOperator\":\"" + operator + "\",\"rates\":\""
				+ rate + "\"}},\"operationName\":\"addNewLocation\"}";

		RestAssured.baseURI = uri;
		stepInfo("Request Payload");
		passStep(createLocation_query);

		Response resp = given()

				.body(createLocation_query).when().post();

		stepInfo("Response Body");
		passStep(resp.asString());

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		assertEquals(resp.getStatusCode(), 400);

	}

	@Test(priority = 5)
	public void TC_05_Create_Bulk_Locations_with_Valid_Combinations() {

		String[] loc1 = new String[] { "P" + get4DigitRandomNumber(), "New York", "Premium Parking", "Premium" };
		String[] loc2 = new String[] { "C" + get4DigitRandomNumber(), "Alaska", "Fourth Hokage", "Default" };
		String[] loc3 = new String[] { "P" + get4DigitRandomNumber(), "New York", "Premium Parking", "EV" };

		createLocation_query = "{\"query\":\"mutation bulkAddLocation($input: [LocationDtoInput]){\\n  bulkAddLocation(input: $input)\\n}\",\"variables\":{\"input\":[{\"name\":\""
				+ loc1[0] + "\",\"address\":\"" + loc1[1] + "\",\"parkingOperator\":\"" + loc1[2] + "\",\"rates\":\""
				+ loc1[3] + "\"},{\"name\":\"" + loc2[0] + "\",\"address\":\"" + loc2[1] + "\",\"parkingOperator\":\""
				+ loc2[2] + "\",\"rates\":\"" + loc2[3] + "\"},{\"name\":\"" + loc3[0] + "\",\"address\":\"" + loc3[1]
				+ "\",\"parkingOperator\":\"" + loc3[2] + "\",\"rates\":\"" + loc3[3]
				+ "\"}]},\"operationName\":\"bulkAddLocation\"}";

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
		passStep("Found <b>" + loc_Count + "</b> locations");
		if (loc_Count != 3) {
			failStep("Failed to create 3 locations <span> &#x1F61F; </span> ");
		}

		for (int i = 0; i < loc_Count; i++) {

			stepInfo("Location " + (i + 1) + " : ");
			passStep("Location 'Id' recieved in response : "
					+ j.getString("data.bulkAddLocation.response.data[" + i + "].id"));
			passStep("Location 'Name' recieved in response : "
					+ j.getString("data.bulkAddLocation.response.data[" + i + "].name"));
			passStep("Location 'Parking Operator' recieved in response : "
					+ j.getString("data.bulkAddLocation.response.data[" + i + "].parkingOperator"));
			passStep("Location 'Address' recieved in response : "
					+ j.getString("data.bulkAddLocation.response.data[" + i + "].address"));
			passStep("Location 'Status' recieved in response : "
					+ j.getString("data.bulkAddLocation.response.data[" + i + "].status"));
			passStep("Location 'Rate Structure' recieved in response : "
					+ j.getString("data.bulkAddLocation.response.data[" + i + "].rates"));

		}
		assertEquals(j.getString("data.bulkAddLocation.response.data[0].name"), loc1[0]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[0].address"), loc1[1]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[0].parkingOperator"), loc1[2]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[0].rates"), loc1[3]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[0].status"), "UNPUBLISH");

		assertEquals(j.getString("data.bulkAddLocation.response.data[1].name"), loc2[0]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[1].address"), loc2[1]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[1].parkingOperator"), loc2[2]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[1].rates"), loc2[3]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[1].status"), "UNPUBLISH");

		assertEquals(j.getString("data.bulkAddLocation.response.data[2].name"), loc3[0]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[2].address"), loc3[1]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[2].parkingOperator"), loc3[2]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[2].rates"), loc3[3]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[2].status"), "UNPUBLISH");

	}

	@Test(priority = 6)
	public void TC_06_Create_Bulk_Locations_with_One_Invalid_Combination_PnumberWithOtherOperator() {

		String[] loc1 = new String[] { "P" + get4DigitRandomNumber(), "New York", "Premium Parking", "Premium" };
		String[] loc2 = new String[] { "C" + get4DigitRandomNumber(), "Alaska", "Fourth Hokage", "Default" };
		String[] loc3 = new String[] { "P" + get4DigitRandomNumber(), "New York", "XYZ Parking", "EV" };

		createLocation_query = "{\"query\":\"mutation bulkAddLocation($input: [LocationDtoInput]){\\n  bulkAddLocation(input: $input)\\n}\",\"variables\":{\"input\":[{\"name\":\""
				+ loc1[0] + "\",\"address\":\"" + loc1[1] + "\",\"parkingOperator\":\"" + loc1[2] + "\",\"rates\":\""
				+ loc1[3] + "\"},{\"name\":\"" + loc2[0] + "\",\"address\":\"" + loc2[1] + "\",\"parkingOperator\":\""
				+ loc2[2] + "\",\"rates\":\"" + loc2[3] + "\"},{\"name\":\"" + loc3[0] + "\",\"address\":\"" + loc3[1]
				+ "\",\"parkingOperator\":\"" + loc3[2] + "\",\"rates\":\"" + loc3[3]
				+ "\"}]},\"operationName\":\"bulkAddLocation\"}";

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
		passStep("Found <b>" + loc_Count + "</b> locations");
		if (loc_Count == 3) {
			failStep("Location got created even with the invalid data <span> &#x1F61F; </span> ");
		}

		for (int i = 0; i < loc_Count; i++) {

			stepInfo("Location " + (i + 1) + " : ");
			passStep("Location 'Id' recieved in response : "
					+ j.getString("data.bulkAddLocation.response.data[" + i + "].id"));
			passStep("Location 'Name' recieved in response : "
					+ j.getString("data.bulkAddLocation.response.data[" + i + "].name"));
			passStep("Location 'Parking Operator' recieved in response : "
					+ j.getString("data.bulkAddLocation.response.data[" + i + "].parkingOperator"));
			passStep("Location 'Address' recieved in response : "
					+ j.getString("data.bulkAddLocation.response.data[" + i + "].address"));
			passStep("Location 'Status' recieved in response : "
					+ j.getString("data.bulkAddLocation.response.data[" + i + "].status"));
			passStep("Location 'Rate Structure' recieved in response : "
					+ j.getString("data.bulkAddLocation.response.data[" + i + "].rates"));

		}
		assertEquals(j.getString("data.bulkAddLocation.response.data[0].name"), loc1[0]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[0].address"), loc1[1]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[0].parkingOperator"), loc1[2]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[0].rates"), loc1[3]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[0].status"), "UNPUBLISH");

		assertEquals(j.getString("data.bulkAddLocation.response.data[1].name"), loc2[0]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[1].address"), loc2[1]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[1].parkingOperator"), loc2[2]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[1].rates"), loc2[3]);
		assertEquals(j.getString("data.bulkAddLocation.response.data[1].status"), "UNPUBLISH");

		assertEquals(loc_Count, 2);

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

		String getLocation_query = "{\"query\":\"query getLocationByPNumber($id: String){\\n  \\tgetLocationByPNumber(id:$id){\\n    name\\n    parkingOperator\\n    rates\\n    address\\n    \\n  }\\n}\",\"variables\":{\"id\":\""
				+ pNumber + "\"},\"operationName\":\"getLocationByPNumber\"}";

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

		passStep("Location 'Name' recieved in response : " + j.getString("data.getLocationByPNumber.name"));
		passStep("Location 'Parking Operator' recieved in response : "
				+ j.getString("data.getLocationByPNumber.parkingOperator"));
		passStep("Location 'Address' recieved in response : " + j.getString("data.getLocationByPNumber.address"));
		passStep("Location 'Rate Structure' recieved in response : " + j.getString("data.getLocationByPNumber.rates"));

		assertEquals(j.getString("data.getLocationByPNumber.name"), pNumber);
		assertEquals(j.getString("data.getLocationByPNumber.address"), address);
		assertEquals(j.getString("data.getLocationByPNumber.parkingOperator"), operator);
		assertEquals(j.getString("data.getLocationByPNumber.rates"), rate);

	}

	@Test(dataProvider = "cLocationValidCombination", priority = 10)
	public void TC_10_Get_Location_By_CNumber(String cNumber, String address, String operator, String rate) {

		String getLocation_query = "{\"query\":\"query getLocationByPNumber($id: String){\\n  \\tgetLocationByPNumber(id:$id){\\n    name\\n    parkingOperator\\n    rates\\n    address\\n    \\n  }\\n}\",\"variables\":{\"id\":\""
				+ cNumber + "\"},\"operationName\":\"getLocationByPNumber\"}";

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

		passStep("Location 'Name' recieved in response : " + j.getString("data.getLocationByPNumber.name"));
		passStep("Location 'Parking Operator' recieved in response : "
				+ j.getString("data.getLocationByPNumber.parkingOperator"));
		passStep("Location 'Address' recieved in response : " + j.getString("data.getLocationByPNumber.address"));
		passStep("Location 'Rate Structure' recieved in response : " + j.getString("data.getLocationByPNumber.rates"));

		assertEquals(j.getString("data.getLocationByPNumber.name"), cNumber);
		assertEquals(j.getString("data.getLocationByPNumber.address"), address);
		assertEquals(j.getString("data.getLocationByPNumber.parkingOperator"), operator);
		assertEquals(j.getString("data.getLocationByPNumber.rates"), rate);

	}

	@Test(priority = 11)
	public void TC_11_get_AllLocations() {

		String getAllLocationsQuery = "{\"query\":\"query getAllLocations{\\n\\tgetAllLocations{    \\n    name\\n    address\\n    parkingOperator    \\n    \\n  }\\n}\",\"variables\":{},\"operationName\":\"getAllLocations\"}";

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
			String operator = j.getString("data.getAllLocations[" + i + "].parkingOperator");
			passStep((i + 1) + ")  Location Name : " + loc_name + ", Location Address : " + address
					+ ",  Parking Operator : " + operator);

		}

	}

}
