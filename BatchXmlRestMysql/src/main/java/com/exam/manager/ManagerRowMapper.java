package com.exam.manager;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ManagerRowMapper implements RowMapper<Manager> {

	@Override
	public Manager mapRow(ResultSet rs, int ind) throws SQLException {
		Manager m = new Manager();
		m.setFirstname(rs.getString("firstname"));
		m.setLastname(rs.getString("lastname"));
		m.setAge(Integer.valueOf(rs.getString("age")));

		return m;
	}
}
