package com.exam;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.exam.employee.EmployeeList;
import com.exam.util.GsonConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class asd {

	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));
		String uri = "http://www.w3schools.com/angular/customers.php";
		RestTemplate restTemplate = new RestTemplate();
		
//		MappingJackson2HttpMessageConverter mjm = new MappingJackson2HttpMessageConverter();
//		mjm.setObjectMapper(new ObjectMapper());
		restTemplate.getMessageConverters().add(new GsonConverter());
		
		//Employee e = restTemplate.getForObject(uri, Employee.class, 1);//.getForObject(uri, Employee.class);
		
		EmployeeList es = restTemplate.getForObject(uri, EmployeeList.class);
		
		
		System.out.println(es.getEmps().get(0));
	}

}
