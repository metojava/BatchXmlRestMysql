package com.exam.listeners;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.exam.employee.Employee;
import com.exam.employee.EmployeeRowMapper;

@Component
public class JobCompletionListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory
			.getLogger(JobCompletionListener.class);

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JobCompletionListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		String sql = "select * from employee";
		super.afterJob(jobExecution);
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			List<Employee> emps = jdbcTemplate.query(sql,
					new EmployeeRowMapper());

			emps.forEach(System.out::println);
		}
	}

}
