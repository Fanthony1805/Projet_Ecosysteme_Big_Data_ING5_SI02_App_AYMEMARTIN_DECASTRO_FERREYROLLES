package com.ece_ing5app_2021.big_data_project;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;

public class HbaseConnector {
	private static org.apache.hadoop.conf.Configuration conf;

    public static Connection getConnectionByFile( String keyTabPath, String coresite, String krbConfPath, String hbaseSitePath, String principal) throws IOException, MasterNotRunningException, ZooKeeperConnectionException  {

        // krb5.conf
        System.setProperty("java.security.krb5.conf", krbConfPath);

        conf = HBaseConfiguration.create();
        conf.addResource(new Path(hbaseSitePath));
        conf.addResource(new Path(coresite));

        UserGroupInformation.setConfiguration(conf);
        UserGroupInformation.loginUserFromKeytab(principal, keyTabPath);
        Connection connection = ConnectionFactory.createConnection(conf);
        HBaseAdmin.available(conf);
        
        return connection;
    }

	public static org.apache.hadoop.conf.Configuration getConf() {
		return conf;
	}

	public static void setConf(org.apache.hadoop.conf.Configuration conf) {
		HbaseConnector.conf = conf;
	}
    
    
}