package com.ece_ing5app_2021.big_data_project;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class GreetingController {
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "Anthony") String name) {
		Connection conn;
		
		 try {
			 conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab","/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml", "a.ferreyrolles-ece@AU.ADALTAS.CLOUD");
			 
			 Admin admin = conn.getAdmin();
		     System.out.println(admin.tableExists(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles")));
		     
		     
		 } catch (MasterNotRunningException e) {
				e.printStackTrace();
			} catch (ZooKeeperConnectionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
}
