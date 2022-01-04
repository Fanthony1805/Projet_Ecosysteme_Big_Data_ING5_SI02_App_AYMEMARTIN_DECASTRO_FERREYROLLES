package com.ece_ing5app_2021.big_data_project;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	private static Connection conn;
	private static Table table;
	private static String userID;
	
	@RequestMapping(method = RequestMethod.POST, value = "/user", headers="Accept=application/json")
	public String createUser(@RequestBody HashMap<String,Object> user) {
		try {
			//without docker
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			// with docker	
			/*	
			conn = HbaseConnector.getConnectionByFile("mykey.keytab",
					"core-site.xml", "krb5.conf", "hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");
			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			*/

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			CounterController.getValues();
			
			userID = "u" + Integer.toString(Counter.getNb_user() + 1);
			
			Put put = new Put(Bytes.toBytes(userID));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("username"), Bytes.toBytes(user.get("username").toString()));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("email"), Bytes.toBytes(user.get("email").toString()));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("password"), Bytes.toBytes(user.get("password").toString()));

			table.put(put);
			
			put = new Put(Bytes.toBytes(user.get("username").toString()));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("password"), Bytes.toBytes(user.get("password").toString()));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("ID"), Bytes.toBytes(userID));
			
			table.put(put);
			
			CounterController.incrementUserCounter();
			
			return "User successfully added\n";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Couldn't add the user\n";
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/edituser/{id}", headers="Accept=application/json")
	public String updateUser(@PathVariable String id, @RequestBody HashMap<String,Object> user) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			CounterController.getValues();
			
			userID = id;
			
			Put put = new Put(Bytes.toBytes(userID));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("username"), Bytes.toBytes(user.get("username").toString()));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("email"), Bytes.toBytes(user.get("email").toString()));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("password"), Bytes.toBytes(user.get("password").toString()));

			table.put(put);
			
			put = new Put(Bytes.toBytes(user.get("username").toString()));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("password"), Bytes.toBytes(user.get("password").toString()));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("ID"), Bytes.toBytes(userID));
			
			table.put(put);
			
			return "User successfully update\n";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Couldn't update the user\n";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/login", headers="Accept=application/json")
	public Boolean loginUser(@RequestBody HashMap<String,Object> user) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			Object username = user.get("username");
			
			Get g = new Get(Bytes.toBytes(username.toString()));

			Result result = table.get(g);

			byte[] value = result.getValue(Bytes.toBytes("user"), Bytes.toBytes("password"));
			String user_password = Bytes.toString(value);
			
			return user_password.equals(user.get("password").toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@RequestMapping("/user/{id}")
	public static HashMap<String,Object> getUser(@PathVariable String id) {
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
			
			HashMap<String, Object> map = new HashMap<>();
			map.put("username", username);
		    map.put("email", user_email);
		    map.put("password", user_password);
			
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/user/{username}")
	public static HashMap<String,Object> getUserByName(@PathVariable String username) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			Get g = new Get(Bytes.toBytes(username));

			Result result = table.get(g);

			byte[] value = result.getValue(Bytes.toBytes("user"), Bytes.toBytes("ID"));
			String id = Bytes.toString(value);
			
			value = result.getValue(Bytes.toBytes("user"), Bytes.toBytes("password"));
			String user_password = Bytes.toString(value);
			
			HashMap<String, Object> map = new HashMap<>();
			map.put("ID", id);
		    map.put("password", user_password);
			
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
