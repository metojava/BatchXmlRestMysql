package com.exam.employee;

import org.springframework.batch.item.ItemProcessor;

public class EmployeeItemProcessor implements ItemProcessor<Employee, Employee>{

	@Override
	public Employee process(Employee e) throws Exception {
		System.out.println(e.getCity());
		return e;
	}

}
