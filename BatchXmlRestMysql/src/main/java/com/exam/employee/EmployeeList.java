package com.exam.employee;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class EmployeeList {

	@SerializedName(value = "records")
	private List<Employee> emps;

	public EmployeeList() {
	}

	public EmployeeList(List<Employee> emps) {
		super();
		this.emps = emps;
	}

	public List<Employee> getEmps() {
		return emps;
	}

	public void setEmps(List<Employee> emps) {
		this.emps = emps;
	}

	

}
