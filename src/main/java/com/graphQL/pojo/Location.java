package com.graphQL.pojo;

public class Location {
	
	String name;
	String pNumber;
	String address;
	String parkingOperator;
	String rates;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getpNumber() {
		return pNumber;
	}
	public void setpNumber(String pNumber) {
		this.pNumber = pNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getParkingOperator() {
		return parkingOperator;
	}
	public void setParkingOperator(String parkingOperator) {
		this.parkingOperator = parkingOperator;
	}
	public String getRates() {
		return rates;
	}
	public void setRates(String rates) {
		this.rates = rates;
	}
	@Override
	public String toString() {
		return "Location [name=" + name + ", pNumber=" + pNumber + ", address=" + address + ", parkingOperator="
				+ parkingOperator + ", rates=" + rates + "]";
	}
	
	
	

}
