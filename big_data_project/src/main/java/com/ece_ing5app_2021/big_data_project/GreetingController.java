package com.ece_ing5app_2021.big_data_project;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@SuppressWarnings("finally")
	@GetMapping("/greeting")
	public String greeting(@RequestParam(value = "name", defaultValue = "Anthony") String name) {
		Connection conn;
		
		 try {
			 conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab","/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml", "a.ferreyrolles-ece@AU.ADALTAS.CLOUD");
			 
			 Admin admin = conn.getAdmin();
		     System.out.println(admin.tableExists(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles")));
		     
		  // Instantiating HTable class
		      Table table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
		      
		     // Instantiating Get class
		      Get g = new Get(Bytes.toBytes("user"));
		      
		   // Reading the data
		      Result result = table.get(g);
		      
		      byte[] value = result.getValue(Bytes.toBytes("username"), Bytes.toBytes(""));
		      String valueStr = Bytes.toString(value);
		      
		      System.out.println("value : " + value);

		      System.out.println("valueStr : " + valueStr);
		      return valueStr;	
		 } catch (MasterNotRunningException e) {
				e.printStackTrace();
			} catch (ZooKeeperConnectionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
				return "tamer";

		
	}
}
