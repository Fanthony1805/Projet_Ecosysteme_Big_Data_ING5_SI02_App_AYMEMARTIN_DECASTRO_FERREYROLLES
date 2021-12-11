package com.ece_ing5app_2021.big_data_project;

import java.io.IOException;
import java.sql.Array;
import java.util.Arrays;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
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
public class UserController {
	private static Connection conn;
	private static Table table;
	private static int userID;
	
	@RequestMapping(method = RequestMethod.POST, value = "/user", headers="Accept=application/json")
	public String createUser(@RequestParam(value="username") String username, @RequestParam(value="email") String email, @RequestParam(value="password") String password) {
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
	
	@RequestMapping(method = RequestMethod.POST, value = "/login", headers="Accept=application/json")
	public Boolean loginUser(@RequestBody User user) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			String username = User.getName();
			
			Get g = new Get(Bytes.toBytes(username));

			Result result = table.get(g);

			byte[] value = result.getValue(Bytes.toBytes("user"), Bytes.toBytes("password"));
			String user_password = Bytes.toString(value);
			
			return user_password.equals(User.getPassword());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@RequestMapping("/user/{id}")
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
