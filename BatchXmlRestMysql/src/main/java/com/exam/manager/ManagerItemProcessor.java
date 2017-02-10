package com.exam.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;


public class ManagerItemProcessor implements ItemProcessor<Manager, Manager>{

	 private static final Logger log = LoggerFactory.getLogger(ManagerItemProcessor.class);
	 
	@Override
	public Manager process(Manager m) throws Exception {
		log.info("really good manager - "+m.toString());
		Manager mm = m;
		return mm;
	}

}
