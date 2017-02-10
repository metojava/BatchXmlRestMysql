package com.exam.employee;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="employee")
public class Employee {

	private String Name;
	private String City;
	private String Country;

	public Employee() {
	}

	public Employee(String name, String city, String country) {
		super();
		Name = name;
		City = city;
		Country = country;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	@Override
	public String toString() {
		return "Employee [Name=" + Name + ", City=" + City + ", Country="
				+ Country + "]";
	}

	

}
