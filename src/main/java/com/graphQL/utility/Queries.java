package com.graphQL.utility;

public class Queries extends BaseClass {

	String market_name = "";

	public static double latitude = getRandomLatitude();
	public static double longitude = getRandomLongitude();

	public String createMarkets(String name) {
		
		 String slug = "Venu Thota";

		return "{\"query\":\"mutation{\\n  addMarket(input:{\\n    \\n    name:\\\"" + name + "\\\"\\n    slug:\\\""
				+ slug + "\\\"\\n    latitude:" + latitude + "\\n    longitude:" + longitude
				+ "\\n  }){\\n    id\\n    name\\n    status\\n    latitude\\n    longitude\\n    \\n  }\\n}\",\"variables\":{},\"operationName\":null}";

	}
	
	public String getMarket_By_Id(String market_id) {
		return "{\"query\":\"query{\\n  market(id:\\\"" + market_id
				+ "\\\"\\n){\\n    id\\n    name    \\n    status\\n    longitude\\n    latitude    \\n    radius\\n    slug\\n    \\n  }\\n}\",\"variables\":{},\"operationName\":null}";


	}

}
