package com.exam.employee;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.web.client.RestTemplate;

import com.exam.util.GsonConverter;

public class EmployeeItemReader implements ItemReader<Employee> {

	private String uri;
	private RestTemplate restTemplate;
	private int empIndex;

	public EmployeeItemReader(String uri, RestTemplate restTemplate) {
		this.uri = "http://www.w3schools.com/angular/customers.php";
		// MappingJackson2HttpMessageConverter mjm = new
		// MappingJackson2HttpMessageConverter();
		// mjm.setObjectMapper(new ObjectMapper());
		// restTemplate.getMessageConverters().add(mjm);

		restTemplate.getMessageConverters().add(new GsonConverter());

		this.restTemplate = restTemplate;
		empIndex = 0;
	}

	@Override
	public Employee read() throws Exception, UnexpectedInputException,
			ParseException, NonTransientResourceException {
		// Employee e = restTemplate.getForObject(uri, Employee.class);
		EmployeeList ell = restTemplate.getForObject(uri, EmployeeList.class);
		List<Employee> emps = ell.getEmps();
		Employee e = null;
		if (empIndex < emps.size()) {
			e = emps.get(empIndex);
			empIndex++;
		}

		return e;
	}

}
