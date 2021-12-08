package com.ece_ing5app_2021.big_data_project;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;

public class HbaseConnector {

    public static Connection getConnectionByFile( String keyTabPath, String krbConfPath, String hbaseSitePath, String principal) throws IOException, MasterNotRunningException, ZooKeeperConnectionException  {

        // krb5.conf
        System.setProperty("java.security.krb5.conf", krbConfPath);

        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        conf.addResource(new Path(hbaseSitePath));

        UserGroupInformation.setConfiguration(conf);

        UserGroupInformation.loginUserFromKeytab(principal, keyTabPath);
        Connection connection = ConnectionFactory.createConnection(conf);
        HBaseAdmin.available(conf);
        return connection;
    }
/*
    public static void main(String[] args) throws IOException {
        Connection connection = getConnectionByFile("/etc/security/keytabs/hbase.service.keytab", "krb5.conf", "hbase-site.xml", "hbase/_HOST@AU.ADALTAS.CLOUD");
        
             
 		Admin admin = connection.getAdmin();
        if (admin.tableExists(TableName.valueOf("tonseal:tonseal_table"))) {
            System.out.println("tonseal:tonseal_table");
        } else {
            System.err.println("tonseal:tonseal_table");
        }
        admin.close();
        connection.close();
        
    }
    */
}