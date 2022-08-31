package com.graphQL.utility;

import static org.testng.Assert.ARRAY_MISMATCH_TEMPLATE;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.testng.ITestResult;
import org.testng.annotations.*;

import com.graphQL.reports.extentReports;

public class BaseClass extends extentReports {

	protected String uri = "";
	protected String bearer = "";
	Properties config;
	FileInputStream fis;

	/*
	 * Usage : This method is to load data from application.properties files Author
	 * : Venu Thota
	 */
	@BeforeMethod
	public void loadProperties(Method testMethod) throws IOException {

		config = new Properties();
		String fpath = System.getProperty("user.dir") + "\\src\\test\\resources\\application.properties";
		fis = new FileInputStream(fpath);

		test = report.startTest(getTestCaseName(testMethod));

		config.load(fis);
		uri = config.getProperty("URI");
		bearer = config.getProperty("BEARER");

	}
	
	public String get4DigitRandomNumber() {
		Random random = new Random();
		return String.format("%04d", random.nextInt(10000));
	}

	public String getTestCaseName(Method testMethod) {

		String name = testMethod.getDeclaringClass().getTypeName();
		String className = name.substring(name.lastIndexOf(".") + 1);

		return "<span style=\"color:blue;\">" + className + " : </span> "+testMethod.getName();

	}

	public String getRequestBody(String key) {

		config = new Properties();
		String fpath = System.getProperty("user.dir") + "\\src\\test\\java\\testData\\request.properties";
		try {
			fis = new FileInputStream(fpath);
			try {
				config.load(fis);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return config.getProperty(key);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@AfterMethod
	public void getResult(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {
			stepInfo("Failed Due to below exception : ");
			failStep(result.getThrowable().toString());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			passStep("Testcase passed");
		}
	}

	/*
	 * Usage : To get the current timestamp Author : Venu Thota
	 */
	public String getTimestamp() {
		return new SimpleDateFormat("HHmmss").format(new Date());
	}

	public static double getRandomLatitude() {
		double latitude = (Math.random() * 180.0) - 90.0;
		return (double) Math.round(latitude * 10000d) / 10000d;

	}

	public static double getRandomLongitude() {
		double longitude = (Math.random() * 360.0) - 180.0;
		return (double) Math.round(longitude * 10000d) / 10000d;

	}

}
