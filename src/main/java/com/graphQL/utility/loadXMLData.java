package com.graphQL.utility;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.graphQL.dataModel.User;

public class loadXMLData {
	
	public User getUserTestData() {
		JAXBContext jaxbContext;
		User user = null;
		File xmlFile = new File("src\\test\\java\\testData\\user.xml");

		try {
			jaxbContext = JAXBContext.newInstance(User.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			user = (User) jaxbUnmarshaller.unmarshal(xmlFile);
			//System.out.println(user);
		} 
		
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

}
