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
public class ChannelController {
	private static Connection conn;
	private static Table table;

	@RequestMapping(method = RequestMethod.POST, value = "/channel", headers="Accept=application/json")
	public String createChannel(@RequestBody HashMap<String,Object> channel) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
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
			
			/*put = new Put(Bytes.toBytes(channelID + "_" + userID));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("owner"), Bytes.toBytes(username.toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("name"), Bytes.toBytes(channelname));

			table.put(put);*/
			
			put = new Put(Bytes.toBytes(channel.get("name").toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("owner"), Bytes.toBytes(channel.get("userID").toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("id"), Bytes.toBytes(channelID));

			table.put(put);
			
			return "Channel successfully added\n";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Couldn't add the channel\n";
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
}
