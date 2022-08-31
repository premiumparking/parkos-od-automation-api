package com.graphQL.tests;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.*;

import com.graphQL.utility.BaseClass;
import com.graphQL.utility.Queries;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class MarketAPIs extends BaseClass {

	Queries queries = new Queries();

	String query = "";
	String market_id = "";
	// String update_market_id = "";
	String market_name = "";

	protected String getActiveMarkets_query = getRequestBody("getActiveMarkets");
	protected String createMarkets_query = getRequestBody("createMarket");
	protected String getMarket_By_Id_query = getRequestBody("getMarketById");
	protected String updateMarket_By_Id_query = getRequestBody("updateMarketById");

	@DataProvider
	public Object[][] marketData() {

		return new Object[][] { { "AutomationTest_", "Venu Thota" } };
	}

	@DataProvider
	public Object[] Active() {

		return new Object[] { "ACTIVE" };

	}

	@Test(priority = 1)
	public void Create_Market() {

		market_name = "AutomationTest_" + getTimestamp();

		createMarkets_query = queries.createMarkets(market_name);

		RestAssured.baseURI = uri;
		stepInfo("Request Body");
		passStep(createMarkets_query);

		Response resp = given().body(createMarkets_query).when().post();

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());

		market_id = j.getString("data.addMarket.id");
		String new_name = j.getString("data.addMarket.name");
		String market_status = j.getString("data.addMarket.status");
		String market_latitude = j.getString("data.addMarket.latitude");
		String market_longitude = j.getString("data.addMarket.longitude");

		passStep("Market 'Id' recieved in response : " + market_id);
		passStep("Market 'Name' recieved in response : " + market_name);
		passStep("Market 'Status' recieved in response : " + market_status);
		passStep("Market 'Latitude' recieved in response : " + market_latitude);
		passStep("Market 'Longitude' recieved in response : " + market_longitude);

		assertEquals(market_name, new_name);
		assertEquals(Queries.latitude + "", market_latitude);
		assertEquals(Queries.longitude + "", market_longitude);
	}

	@Test(priority = 2)
	public void Get_Market_By_Id() {

		getMarket_By_Id_query = queries.getMarket_By_Id(market_id);

		RestAssured.baseURI = uri;
		stepInfo("Request Body");
		passStep(getMarket_By_Id_query);

		Response resp = given()

				.body(getMarket_By_Id_query).when().post();

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());

		String get_market_id = j.getString("data.market.id");
		String get_market_name = j.getString("data.market.name");
		String market_status = j.getString("data.market.status");
		String market_latitude = j.getString("data.market.latitude");
		String market_longitude = j.getString("data.market.longitude");
		String market_slug = j.getString("data.market.slug");

		passStep("Market 'Id' recieved in response : " + get_market_id);
		passStep("Market 'Name' recieved in response : " + market_name);
		passStep("Market 'Status' recieved in response : " + market_status);
		passStep("Market 'Latitude' recieved in response : " + market_latitude);
		passStep("Market 'Longitude' recieved in response : " + market_longitude);
		passStep("Market 'Slug recieved in response : " + market_slug);

		assertEquals(get_market_id, market_id);
		assertEquals(get_market_name, market_name);
		assertEquals(Queries.longitude + "", market_longitude);
		assertEquals(Queries.latitude + "", market_latitude);

	}

	@Test(dataProvider = "Active", priority = 3)
	public void Get_All_Active_Markets_AfterCreation(String status) {

		RestAssured.baseURI = uri;
		stepInfo("Request Body");
		passStep(getActiveMarkets_query);

		Response resp = given()

				.body("{\"query\":\"query{\\n   marketByStatus(status:" + status
						+ ",page:0){\\n    id\\n    name\\n    status\\n    center\\n    radius\\n    latitude\\n    longitude\\n  }\\n}\",\"variables\":{},\"operationName\":null}")
				.when().post();

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());

		// get values of JSON array after getting array size
		int loc_Count = j.getInt("data.marketByStatus.size()");
		passStep("Found " + loc_Count + " active markets");
		for (int i = 0; i < loc_Count; i++) {
			String name = j.getString("data.marketByStatus[" + i + "].name");
			String id = j.getString("data.marketByStatus[" + i + "].id");
			if (market_id.equalsIgnoreCase(id)) {
				passStep("<b>" + (i + 1) + ".. newly created Market Id : " + id + " , Market Name : " + name + "</b>");
			}
			passStep((i + 1) + ".. Market Id : " + id + " , Market Name : " + name);
		}
	}

	@Test(priority = 4)
	public void Update_Market_By_Id() {

		String name_Update = "AutomationTest_Update_" + getTimestamp();

		double new_lati = getRandomLatitude();
		double new_longi = getRandomLongitude();

		updateMarket_By_Id_query = "{\"query\":\"mutation {\\n  updateMarket(\\n    input: { name: \\\"" + name_Update
				+ "\\\", longitude: " + new_longi + ", latitude: " + new_lati + " }\\n    id: \\\"" + market_id
				+ "\\\"\\n  ) {\\n    id\\n    name\\n    longitude\\n    latitude\\n  }\\n}\",\"variables\":{},\"operationName\":null}";

		RestAssured.baseURI = uri;
		stepInfo("Request Body");
		passStep(updateMarket_By_Id_query);

		Response resp = given()

				.body(updateMarket_By_Id_query).when().post();

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());

		String update_market_id = j.getString("data.updateMarket.id");
		String market_name = j.getString("data.updateMarket.name");
		String market_latitude = j.getString("data.updateMarket.latitude");
		String market_longitude = j.getString("data.updateMarket.longitude");

		passStep("Market 'Id' recieved in response : " + update_market_id);
		passStep("Market 'Name' recieved in response : " + market_name);
		passStep("Market 'Latitude' recieved in response : " + market_latitude);
		passStep("Market 'Longitude' recieved in response : " + market_longitude);

		assertEquals(market_name, name_Update);
		assertEquals(market_latitude, new_lati + "");
		assertEquals(market_longitude, new_longi + "");
		assertEquals(update_market_id, market_id);

	}

	@Test(dataProvider = "Active", priority = 5)
	public void Get_All_Active_Markets_AfterUpdation(String status) {

		RestAssured.baseURI = uri;
		stepInfo("Request Body");
		passStep(getActiveMarkets_query);

		Response resp = given()

				.body("{\"query\":\"query{\\n   marketByStatus(status:" + status
						+ ",page:0){\\n    id\\n    name\\n    status\\n    center\\n    radius\\n    latitude\\n    longitude\\n  }\\n}\",\"variables\":{},\"operationName\":null}")
				.when().post();

		stepInfo("Response Validation");

		passStep("Received Status code : " + resp.getStatusCode());
		assertEquals(resp.getStatusCode(), 200);
		JsonPath j = new JsonPath(resp.asString());

		// get values of JSON array after getting array size
		int loc_Count = j.getInt("data.marketByStatus.size()");
		passStep("Found " + loc_Count + " active markets");
		for (int i = 0; i < loc_Count; i++) {
			String name = j.getString("data.marketByStatus[" + i + "].name");
			String id = j.getString("data.marketByStatus[" + i + "].id");
			if (market_id.equalsIgnoreCase(id)) {
				passStep("<b>" + (i + 1) + ".. updated Market Id : " + id + " , Market Name : " + name + "</b>");
			}
			passStep((i + 1) + ".. Market Id : " + id + " , Market Name : " + name);
		}
	}
}
