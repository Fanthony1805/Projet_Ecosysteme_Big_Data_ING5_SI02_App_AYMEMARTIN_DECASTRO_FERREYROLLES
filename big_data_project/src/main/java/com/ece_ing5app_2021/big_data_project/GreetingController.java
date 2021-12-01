package com.ece_ing5app_2021.big_data_project;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.hadoop.hbase.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "Anthony") String name) {
		Configuration config = HBaseConfiguration.create();

		String path = this.getClass()
		  .getClassLoader()
		  .getResource("conf/hbase-site.xml")
		  .getPath();
		config.addResource(new Path(path));
		
		HBaseAdmin.checkHBaseAvailable(config);
		
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
}