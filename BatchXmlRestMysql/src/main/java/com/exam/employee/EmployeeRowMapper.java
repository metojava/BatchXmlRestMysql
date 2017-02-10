package com.exam.employee;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class EmployeeRowMapper implements RowMapper<Employee> {

	@Override
	public Employee mapRow(ResultSet rs, int ind) throws SQLException {

		Employee e = new Employee();
		e.setCity(rs.getString("city"));
		e.setName(rs.getString("name"));
		e.setCountry(rs.getString("country"));
		return e;
	}

}
