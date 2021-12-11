package com.ece_ing5app_2021.big_data_project;

import java.io.IOException;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
	private static Connection conn;
	// Instantiating HTable class
	private static Table table;
	private static int userID;
	private static int channelID;
	private static int MessageID;
	private static String username;
	private static String channelname;

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
}
