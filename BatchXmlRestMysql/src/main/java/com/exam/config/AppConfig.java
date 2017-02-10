package com.exam.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestTemplate;

import com.exam.employee.Employee;
import com.exam.employee.EmployeeItemProcessor;
import com.exam.employee.EmployeeItemReader;
import com.exam.employee.EmployeeRowMapper;
import com.exam.listeners.JobCompletionListener;
import com.exam.manager.Manager;
import com.exam.manager.ManagerItemProcessor;
import com.exam.manager.ManagerRowMapper;

@Configuration
@EnableBatchProcessing
public class AppConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public DataSource dataSource() throws SQLException {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/est");
		dataSource.setUsername("root");
		dataSource.setPassword("nbuser");
		return dataSource;
	}

	@Bean
	public FlatFileItemReader<Manager> reader() {
		FlatFileItemReader<Manager> reader = new FlatFileItemReader<Manager>();
		reader.setResource(new ClassPathResource("managers.csv"));
		reader.setLineMapper(new DefaultLineMapper<Manager>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "firstname", "lastname", "age" });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Manager>() {
					{
						setTargetType(Manager.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	public JdbcCursorItemReader<Employee> employeeMysqlReader()
			throws SQLException {

		JdbcCursorItemReader<Employee> jr = new JdbcCursorItemReader<Employee>();
		jr.setDataSource(dataSource());
		jr.setRowMapper(new EmployeeRowMapper());
		jr.setSql("select name, city, country from employee");
		return jr;
	}

	@Bean
	public JdbcCursorItemReader<Manager> managerMysqlReader()
			throws SQLException {

		JdbcCursorItemReader<Manager> jr = new JdbcCursorItemReader<Manager>();
		jr.setDataSource(dataSource());
		jr.setRowMapper(new ManagerRowMapper());
		jr.setSql("select firstname, lastname, age from manager");
		return jr;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	EmployeeItemReader employeeItemReader() {
		return new EmployeeItemReader("", restTemplate());
	}

	@Bean
	public EmployeeItemProcessor employeeItemProcessor() {
		return new EmployeeItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Manager> managerMysqlWriter()
			throws SQLException {
		JdbcBatchItemWriter<Manager> writer = new JdbcBatchItemWriter<Manager>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Manager>());
		writer.setSql("INSERT INTO manager (firstname, lastname, age) VALUES (:firstname, :lastname, :age)");
		writer.setDataSource(dataSource());
		return writer;
	}

	@Bean
	public JdbcBatchItemWriter<Employee> employeeMysqlWriter()
			throws SQLException {
		JdbcBatchItemWriter<Employee> writer = new JdbcBatchItemWriter<Employee>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Employee>());
		writer.setSql("INSERT INTO employee (name, city, country) VALUES (:name, :city, :country)");
		writer.setDataSource(dataSource());
		return writer;
	}

	@Bean
	public StaxEventItemWriter<Employee> employeeXmlWriter() {
		StaxEventItemWriter<Employee> jw = new StaxEventItemWriter<Employee>();
		// jw.setMarshaller(marshaller());
		// Resource resource = new Resource();
		String path = System.getProperty("user.dir");
		jw.setResource(new FileSystemResource(path + "\\output\\employees.xml"));
		jw.setRootTagName("employees");

		Jaxb2Marshaller jm = new Jaxb2Marshaller();
		jm.setClassesToBeBound(Employee.class);
		jw.setMarshaller(jm);
		return jw;
	}

	@Bean
	public StaxEventItemWriter<Manager> managerXmlWriter() {
		StaxEventItemWriter<Manager> jw = new StaxEventItemWriter<Manager>();
		// jw.setMarshaller(marshaller());
		// Resource resource = new Resource();
		String path = System.getProperty("user.dir");
		jw.setResource(new FileSystemResource(path + "\\output\\managers.xml"));
		jw.setRootTagName("managers");

		Jaxb2Marshaller jm = new Jaxb2Marshaller();
		jm.setClassesToBeBound(Manager.class);
		jw.setMarshaller(jm);
		return jw;
	}

	@Bean
	public Job myJob(JobCompletionListener listener) throws SQLException {

		return jobBuilderFactory.get("myJob")
				.incrementer(new RunIdIncrementer()).listener(listener)
				.flow(step1()).next(step2()).next(step3()).next(step4()).end()
				.build();
	}

	@Bean
	public Step step1() throws SQLException {
		return stepBuilderFactory.get("step1").<Manager, Manager> chunk(10)
				.reader(reader()).processor(managerProcessor())
				.writer(managerMysqlWriter()).build();
	}

	@Bean
	public Step step2() throws SQLException {
		return stepBuilderFactory.get("step2").<Manager, Manager> chunk(10)
				.reader(managerMysqlReader()).processor(managerProcessor())
				.writer(managerXmlWriter()).build();
	}

	@Bean
	public ManagerItemProcessor managerProcessor() {
		return new ManagerItemProcessor();
	}

	@Bean
	public Step step3() throws SQLException {
		return stepBuilderFactory.get("step3").<Employee, Employee> chunk(10)
				.reader(employeeItemReader())
				.processor(employeeItemProcessor())
				.writer(employeeMysqlWriter()).build();
	}

	@Bean
	public Step step4() throws SQLException {
		return stepBuilderFactory.get("step4").<Employee, Employee> chunk(10)
				.reader(employeeMysqlReader())
				.processor(employeeItemProcessor()).writer(employeeXmlWriter())
				.build();
	} 
	
	// @Bean
		// public XStreamMarshaller marshaller() {
		// XStreamMarshaller xm = new XStreamMarshaller();
		// Map<String, Person> pm = new HashMap<>();
		// pm.put("person", new Person());
		// xm.setAliases(pm);
		//
		// return xm;
		//
		// }

}
