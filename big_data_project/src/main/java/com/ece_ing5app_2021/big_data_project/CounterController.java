package com.ece_ing5app_2021.big_data_project;

import java.io.IOException;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

public class CounterController {
	private static Connection conn;
	// Instantiating HTable class
	private static Table table;
	
	public static void getValues() {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			// Instantiating Get class
			Get g = new Get(Bytes.toBytes("counter"));

			// Reading the data
			Result result = table.get(g);

			byte[] value = result.getValue(Bytes.toBytes("user"), Bytes.toBytes("nb_user"));
			System.out.println(Bytes.toString(value));
			Counter.setNb_user(Integer.parseInt(Bytes.toString(value)));
			
			value = result.getValue(Bytes.toBytes("channel"), Bytes.toBytes("nb_channel"));
			System.out.println(Bytes.toString(value));
			Counter.setNb_channel(Integer.parseInt(Bytes.toString(value)));
			
			value = result.getValue(Bytes.toBytes("message"), Bytes.toBytes("nb_message"));
			System.out.println(Bytes.toString(value));
			Counter.setNb_message(Integer.parseInt(Bytes.toString(value)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void incrementUserCounter() {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			// Instantiating Get class
			Put put = new Put(Bytes.toBytes("counter"));

			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("nb_user"), Bytes.toBytes(Counter.getNb_user() + 1));
			//Add as many columns as you want

			table.put(put);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void incrementChannelCounter() {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			// Instantiating Get class
			Put put = new Put(Bytes.toBytes("counter"));

			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("nb_channel"), Bytes.toBytes(Counter.getNb_channel() + 1));
			//Add as many columns as you want

			table.put(put);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void incrementMessageCounter() {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			// Instantiating Get class
			Put put = new Put(Bytes.toBytes("counter"));

			put.addColumn(Bytes.toBytes("message"), Bytes.toBytes("nb_message"), Bytes.toBytes(Counter.getNb_message() + 1));
			//Add as many columns as you want

			table.put(put);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
