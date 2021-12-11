package com.ece_ing5app_2021.big_data_project;

import java.io.IOException;
import java.sql.Array;
import java.sql.SQLException;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@RestController
public class ChannelController {
	private static Connection conn;
	// Instantiating HTable class
	private static Table table;

	@GetMapping("/put_channel")
	public String putMessage(@RequestParam String userID, @RequestParam String channelname) {
		try {
			conn = HbaseConnector.getConnectionByFile("/home/a.ferreyrolles-ece/mykey.keytab",
					"/etc/hadoop/conf/core-site.xml", "/etc/krb5.conf", "/etc/hbase/conf/hbase-site.xml",
					"a.ferreyrolles-ece@AU.ADALTAS.CLOUD");

			table = conn.getTable(TableName.valueOf("ece_2021_fall_app_2:AFerreyrolles"));
			
			// Instantiating Get class
			Put put = new Put(Bytes.toBytes("c" + Channel.getId()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("owner"), Bytes.toBytes("u" + userID));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("channelname"), Bytes.toBytes(channelname));
			
			//Add as many columns as you want

			table.put(put);
			
			CounterController.getValues();
			
			int channelID = Counter.getNb_user();
			
			JsonObject user = UserController.getUser(userID);
			
			JsonElement username = null;
			
			username = user.get("username");
			
			put = new Put(Bytes.toBytes("u" + userID +"_c" + channelID));
			put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("username"), Bytes.toBytes(username.toString()));
			put.addColumn(Bytes.toBytes("channel"), Bytes.toBytes("channelname"), Bytes.toBytes(channelname));
			//Add as many columns as you want

			table.put(put);
			
			return "Value successfully added\n";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Couldn't add the value\n";
	}
}
