package com.ece_ing5app_2021.big_data_project;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("deprecation")
@RestController
public class MessageController {
	private static Connection conn;
	private static Table table;

	@RequestMapping(method = RequestMethod.POST, value = "/message", headers="Accept=application/json")
	public String createMessage(@RequestBody HashMap<String,Object> message) {
		try {
			// without docker
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
			
			String userID = message.get("userID").toString();
			String channelID = message.get("channelID").toString();
			String content = message.get("content").toString();
			String created_at = message.get("created_at").toString();
			
			CounterController.getValues();
			
			String messageID = "m" + Counter.getNb_message();
			
			HashMap	<String,Object> user = UserController.getUser(userID);
			HashMap	<String,Object> channel = ChannelController.getChannel(channelID);
			
			String username = user.get("username").toString();
			String channelname = channel.get("name").toString();
			
			Put put = new Put(Bytes.toBytes(messageID));
			put.addColumn(Bytes.toBytes("message"), Bytes.toBytes("channelID"), Bytes.toBytes(channelID));
			put.addColumn(Bytes.toBytes("message"), Bytes.toBytes("author"), Bytes.toBytes(username));
			put.addColumn(Bytes.toBytes("message"), Bytes.toBytes("content"), Bytes.toBytes(content));
			put.addColumn(Bytes.toBytes("message"), Bytes.toBytes("created_at"), Bytes.toBytes(created_at));

			table.put(put);
			
			put = new Put(Bytes.toBytes(userID + "_" + channelID + "_" + messageID));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("username"), Bytes.toBytes(username.toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("name"), Bytes.toBytes(channelname));
			put.addColumn(Bytes.toBytes("message"), Bytes.toBytes("content"), Bytes.toBytes(content));
			put.addColumn(Bytes.toBytes("message"), Bytes.toBytes("created_at"), Bytes.toBytes(created_at));

			table.put(put);
			
			/*put = new Put(Bytes.toBytes(channelID + "_" + userID + "_" + messageID));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("username"), Bytes.toBytes(username.toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("name"), Bytes.toBytes(channelname));
			put.addColumn(Bytes.toBytes("message"), Bytes.toBytes("content"), Bytes.toBytes(content));

			table.put(put);*/
			
			put = new Put(Bytes.toBytes(channelID + "_" + messageID));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("username"), Bytes.toBytes(username.toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("name"), Bytes.toBytes(channelname));
			put.addColumn(Bytes.toBytes("message"), Bytes.toBytes("content"), Bytes.toBytes(content));
			put.addColumn(Bytes.toBytes("message"), Bytes.toBytes("created_at"), Bytes.toBytes(created_at));

			table.put(put);
			
			CounterController.incrementMessageCounter();
			
			return "Message successfully added\n";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Couldn't add the message\n";
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/editmessage/{id}", headers="Accept=application/json")
	public String updateMessage(@PathVariable String id, @RequestBody HashMap<String,Object> message) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			String userID = message.get("userID").toString();
			String channelID = message.get("channelID").toString();
			String content = message.get("content").toString();
			String created_at = message.get("created_at").toString();
			
			CounterController.getValues();
			
			String messageID = id;
			
			HashMap	<String,Object> user = UserController.getUser(userID);
			HashMap	<String,Object> channel = ChannelController.getChannel(channelID);
			
			String username = user.get("username").toString();
			String channelname = channel.get("name").toString();
			
			Put put = new Put(Bytes.toBytes(messageID));
			put.addColumn(Bytes.toBytes("message"), Bytes.toBytes("channelID"), Bytes.toBytes(channelID));
			put.addColumn(Bytes.toBytes("message"), Bytes.toBytes("author"), Bytes.toBytes(username));
			put.addColumn(Bytes.toBytes("message"), Bytes.toBytes("content"), Bytes.toBytes(content));
			put.addColumn(Bytes.toBytes("message"), Bytes.toBytes("created_at"), Bytes.toBytes(created_at));

			table.put(put);
			
			put = new Put(Bytes.toBytes(userID + "_" + channelID + "_" + messageID));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("username"), Bytes.toBytes(username.toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("name"), Bytes.toBytes(channelname));
			put.addColumn(Bytes.toBytes("message"), Bytes.toBytes("content"), Bytes.toBytes(content));
			put.addColumn(Bytes.toBytes("message"), Bytes.toBytes("created_at"), Bytes.toBytes(created_at));

			table.put(put);
			
			/*put = new Put(Bytes.toBytes(channelID + "_" + userID + "_" + messageID));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("username"), Bytes.toBytes(username.toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("name"), Bytes.toBytes(channelname));
			put.addColumn(Bytes.toBytes("message"), Bytes.toBytes("content"), Bytes.toBytes(content));

			table.put(put);*/
			
			put = new Put(Bytes.toBytes(channelID + "_" + messageID));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("username"), Bytes.toBytes(username.toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("name"), Bytes.toBytes(channelname));
			put.addColumn(Bytes.toBytes("message"), Bytes.toBytes("content"), Bytes.toBytes(content));
			put.addColumn(Bytes.toBytes("message"), Bytes.toBytes("created_at"), Bytes.toBytes(created_at));

			table.put(put);
			
			return "Message successfully updated\n";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Couldn't update the message\n";
	}
	
	@RequestMapping("/message/{id}")
	public static HashMap<String,Object> getMessage(@PathVariable String id) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			Get g = new Get(Bytes.toBytes(id));

			Result result = table.get(g);

			byte[] value = result.getValue(Bytes.toBytes("message"), Bytes.toBytes("channelID"));
			String channelID = Bytes.toString(value);
			
			value = result.getValue(Bytes.toBytes("message"), Bytes.toBytes("author"));
			String author = Bytes.toString(value);
			
			value = result.getValue(Bytes.toBytes("message"), Bytes.toBytes("content"));
			String content = Bytes.toString(value);
			
			value = result.getValue(Bytes.toBytes("message"), Bytes.toBytes("created_at"));
			String created_at = Bytes.toString(value);
			
			HashMap<String, Object> map = new HashMap<>();
			map.put("channelID", channelID);
		    map.put("author", author);
		    map.put("content", content);
		    map.put("created_at", created_at);
			
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/channel/{channelID}/message/{id}")
	public static HashMap<String,Object> getMessage(@PathVariable String channelID, @PathVariable String id) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			Get g = new Get(Bytes.toBytes(channelID + "_" + id));

			Result result = table.get(g);

			byte[] value = result.getValue(Bytes.toBytes("message"), Bytes.toBytes("content"));
			String content = Bytes.toString(value);
			
			value = result.getValue(Bytes.toBytes("user"), Bytes.toBytes("username"));
			String username = Bytes.toString(value);
			
			value = result.getValue(Bytes.toBytes("channel"), Bytes.toBytes("name"));
			String channelname = Bytes.toString(value);
			
			HashMap<String, Object> map = new HashMap<>();
			map.put("username", username);
		    map.put("channelname", channelname);
		    map.put("content", content);
			
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/user/{userID}/channel/{channelID}/message/{id}")
	public static HashMap<String,Object> getMessage(@PathVariable String userID, @PathVariable String channelID, @PathVariable String id) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			Get g = new Get(Bytes.toBytes(userID + "_" + channelID + "_" + id));

			Result result = table.get(g);

			byte[] value = result.getValue(Bytes.toBytes("message"), Bytes.toBytes("content"));
			String content = Bytes.toString(value);
			
			value = result.getValue(Bytes.toBytes("user"), Bytes.toBytes("username"));
			String username = Bytes.toString(value);
			
			value = result.getValue(Bytes.toBytes("channel"), Bytes.toBytes("name"));
			String channelname = Bytes.toString(value);
			
			HashMap<String, Object> map = new HashMap<>();
			map.put("username", username);
		    map.put("channelname", channelname);
		    map.put("content", content);
			
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/channel/{id}/getmessages")
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
	            if(!rowkey.contains("u")) {
	            	map.put("message" + i, rowkey);
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
