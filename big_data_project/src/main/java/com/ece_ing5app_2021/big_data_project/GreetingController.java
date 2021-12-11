package com.ece_ing5app_2021.big_data_project;

import java.io.IOException;
import java.sql.Array;
import java.util.Arrays;

import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
	private static Connection conn;
	// Instantiating HTable class
	private static Table table;

	@GetMapping("/try_connection")
	public String greeting(@RequestParam(value = "name", defaultValue = "Anthony") String name) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			Admin admin = conn.getAdmin();
			
			return "Connection success: " + admin.tableExists(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles")) + "\n";
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Connection Failed\n";
	}
	
	@GetMapping("/delete_row")
	public String deleteRow() {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			// Instantiating Get class
			Delete delete = new Delete(Bytes.toBytes("user"));
			
			table.delete(delete);
			
			return "Row successfully deleted\n";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Couldn't delete the row\n";
	}
	
	@GetMapping("/delete_columnfamily")
	public String deleteColumnFamily() {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			Admin admin = conn.getAdmin();
			admin.deleteColumnFamily(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"), Bytes.toBytes("opinion"));
			
			return "Column family successfully deleted\n";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Couldn't delete the column family\n";
	}
	
private static int userID;
	
	@RequestMapping(method = RequestMethod.POST, value = "/user")
	public String createUser(@RequestBody String username, @RequestBody String email, @RequestBody String password) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			CounterController.getValues();
			
			userID = Counter.getNb_user();
			
			Put put = new Put(Bytes.toBytes("u" + (userID + 1)));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("username"), Bytes.toBytes(username));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("email"), Bytes.toBytes(email));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("password"), Bytes.toBytes(password));

			table.put(put);
			
			put = new Put(Bytes.toBytes(username));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("password"), Bytes.toBytes(password));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("ID"), Bytes.toBytes("u" + userID));
			
			table.put(put);
			
			CounterController.incrementUserCounter();
			
			return "Value successfully added\n";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Couldn't add the value\n";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/login")
	public Boolean loginUser(@RequestParam String username, @RequestParam String password) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			Get g = new Get(Bytes.toBytes(username));

			Result result = table.get(g);

			byte[] value = result.getValue(Bytes.toBytes("user"), Bytes.toBytes("password"));
			String user_password = Bytes.toString(value);
			
			return user_password.equals(password);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@GetMapping("/user/{id}")
	public static Array getUser(@PathVariable String id) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			Get g = new Get(Bytes.toBytes(id));

			Result result = table.get(g);

			byte[] value = result.getValue(Bytes.toBytes("user"), Bytes.toBytes("username"));
			String username = Bytes.toString(value);
			
			value = result.getValue(Bytes.toBytes("user"), Bytes.toBytes("email"));
			String user_email = Bytes.toString(value);
			
			value = result.getValue(Bytes.toBytes("user"), Bytes.toBytes("password"));
			String user_password = Bytes.toString(value);
			
			System.out.println("username = " + username);
			
			return (Array) Arrays.asList(
			        username,
			        user_email,
			        user_password);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
