package com.ece_ing5app_2021.big_data_project;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("deprecation")
@RestController
public class ChannelController {
	private static Connection conn;
	private static Table table;

	@RequestMapping(method = RequestMethod.POST, value = "/channel", headers="Accept=application/json")
	public String createChannel(@RequestBody HashMap<String,Object> channel) {
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
			
			CounterController.getValues();
			
			String channelID = "c" + Counter.getNb_channel();
			
			Put put = new Put(Bytes.toBytes(channelID));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("owner"), Bytes.toBytes(channel.get("userID").toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("name"), Bytes.toBytes(channel.get("name").toString()));

			table.put(put);
			
			HashMap<String,Object> user = UserController.getUser(channel.get("userID").toString());
			
			Object username = user.get("username");
			
			put = new Put(Bytes.toBytes(channel.get("userID").toString() + "_" + channelID));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("owner"), Bytes.toBytes(username.toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("name"), Bytes.toBytes(channel.get("name").toString()));

			table.put(put);
			
			put = new Put(Bytes.toBytes(channelID + "_" + channel.get("userID").toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("owner"), Bytes.toBytes(username.toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("name"), Bytes.toBytes(channel.get("name").toString()));

			table.put(put);
			
			put = new Put(Bytes.toBytes(channel.get("name").toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("owner"), Bytes.toBytes(channel.get("userID").toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("id"), Bytes.toBytes(channelID));

			table.put(put);
			
			CounterController.incrementChannelCounter();
			
			return "Channel successfully added\n";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Couldn't add the channel\n";
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/editchannel/{id}", headers="Accept=application/json")
	public String updateChannel(@PathVariable String id, @RequestBody HashMap<String,Object> channel) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			CounterController.getValues();
			
			String channelID = id;
			
			Put put = new Put(Bytes.toBytes(channelID));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("owner"), Bytes.toBytes(channel.get("userID").toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("name"), Bytes.toBytes(channel.get("name").toString()));

			table.put(put);
			
			HashMap<String,Object> user = UserController.getUser(channel.get("userID").toString());
			
			Object username = user.get("username");
			
			put = new Put(Bytes.toBytes(channel.get("userID").toString() + "_" + channelID));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("owner"), Bytes.toBytes(username.toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("name"), Bytes.toBytes(channel.get("name").toString()));

			table.put(put);
			
			put = new Put(Bytes.toBytes(channelID + "_" + channel.get("userID").toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("owner"), Bytes.toBytes(username.toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("name"), Bytes.toBytes(channel.get("name").toString()));

			table.put(put);
			
			put = new Put(Bytes.toBytes(channel.get("name").toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("owner"), Bytes.toBytes(channel.get("userID").toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("id"), Bytes.toBytes(channelID));

			table.put(put);
			
			return "Channel successfully updated\n";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Couldn't update the channel\n";
	}
	
	@RequestMapping("/channel/{id}")
	public static HashMap<String,Object> getChannel(@PathVariable String id) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			Get g = new Get(Bytes.toBytes(id));

			Result result = table.get(g);

			byte[] value = result.getValue(Bytes.toBytes("channel"), Bytes.toBytes("name"));
			String channelname = Bytes.toString(value);
			
			value = result.getValue(Bytes.toBytes("channel"), Bytes.toBytes("owner"));
			String owner = Bytes.toString(value);
			
			HashMap<String, Object> map = new HashMap<>();
			map.put("name", channelname);
		    map.put("owner", owner);
			
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/channelname/{channelname}")
	public static HashMap<String,Object> getChannelByName(@PathVariable String channelname) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			Get g = new Get(Bytes.toBytes(channelname));

			Result result = table.get(g);

			byte[] value = result.getValue(Bytes.toBytes("channel"), Bytes.toBytes("id"));
			String id = Bytes.toString(value);
			
			value = result.getValue(Bytes.toBytes("channel"), Bytes.toBytes("owner"));
			String owner = Bytes.toString(value);
			
			HashMap<String, Object> map = new HashMap<>();
			map.put("id", id);
		    map.put("owner", owner);
			
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/user/{userID}/channel/{id}")
	public static HashMap<String,Object> getChannel(@PathVariable String userID,@PathVariable String id) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			Get g = new Get(Bytes.toBytes(userID + "_" + id));

			Result result = table.get(g);

			byte[] value = result.getValue(Bytes.toBytes("channel"), Bytes.toBytes("name"));
			String channelname = Bytes.toString(value);
			
			value = result.getValue(Bytes.toBytes("channel"), Bytes.toBytes("owner"));
			String owner = Bytes.toString(value);
			
			HashMap<String, Object> map = new HashMap<>();
			map.put("name", channelname);
		    map.put("owner", owner);
			
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/channel/{id}/adduser/{userID}")
	public static String addUserToChannel(@PathVariable String userID,@PathVariable String id) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			Put put = new Put(Bytes.toBytes(id + "_" + userID));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("owner"), Bytes.toBytes(userID));

			table.put(put);
			
			put = new Put(Bytes.toBytes(userID + "_" + id));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("owner"), Bytes.toBytes(userID));
			
			return "User added\n";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Couldn't add the user to the channel\n";
	}
	
	@RequestMapping("/channel/{id}/removeuser/{userID}")
	public static String removeUserFromChannel(@PathVariable String userID,@PathVariable String id) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			Delete delete = new Delete(Bytes.toBytes(id + "_" + userID));
			table.delete(delete);
			
			return "User successfully removed from channel\n";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Couldn't remove user from channel\n";
	}
	
	@RequestMapping("/channel/{id}/getusers")
	public static HashMap<String,Object> addUserToChannel(@PathVariable String id) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			Scan scan = new Scan();
			RowFilter rowFilter = new RowFilter(CompareOp.EQUAL, new BinaryPrefixComparator(Bytes.toBytes(id + "_")));
			
			scan.setFilter(rowFilter);
	        ResultScanner scanner = table.getScanner(scan);
	        Iterator<Result> iterator = scanner.iterator();
	        HashMap<String, Object> map = new HashMap<>();
	        int i = 0;
	        while (iterator.hasNext()) {
	            Result result = iterator.next();
	            String rowkey = Bytes.toString(result.getRow());
	            if(!rowkey.contains("m")) {
	            	map.put("owner" + i, rowkey);
	            	i++;
	            }
	        }
			
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
