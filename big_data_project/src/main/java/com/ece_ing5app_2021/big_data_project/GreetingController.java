package com.ece_ing5app_2021.big_data_project;

import java.io.IOException;
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
		 try {
			HbaseConnector.getConnectionByFile("/etc/security/keytabs/hbase.service.keytab", "krb5.conf", "hbase-site.xml", "hbase/_HOST@AU.ADALTAS.CLOUD");
		 } catch (MasterNotRunningException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ZooKeeperConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		/*Configuration config = HBaseConfiguration.create();
		
		String path = this.getClass()
		  .getClassLoader()
		  .getResource("hbase-site.xml")
		  .getPath();
		config.addResource(new Path(path));
		try {
			HBaseAdmin.available(config);
		} catch (MasterNotRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
}
