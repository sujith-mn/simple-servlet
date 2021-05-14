package com.test;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public final class DbUtil {
	Connection con;
	public DbUtil() throws SQLException{
		String url = "jdbc:h2:mem:test";
		con = DriverManager.getConnection(url);
		createTable();
	}
	public void close(){
		if(con==null) return;
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void createTable() throws SQLException {
		Statement stmt = con.createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS  request "
				+ "(id bigint auto_increment,DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " + " method VARCHAR(255), "
				+ " data VARCHAR(2048), " + " PRIMARY KEY ( id ))";
		stmt.executeUpdate(sql);
		stmt.close();
	}

	public void insertData(String method, String data) throws SQLException {
		if (data.equals(""))
			return;
		String sql = "INSERT INTO request (method,data) VALUES (?,?)";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, method);
		stmt.setString(2, data);
		stmt.executeUpdate();
		stmt.close();
	}

	public void show(String key,String value, PrintWriter out) throws SQLException {
		PreparedStatement ps = con.prepareStatement("select * from request where "+key+"=?");
		ps.setString(1, value);
		ResultSet rs = ps.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		Set<String> columnNames = new HashSet<>();
		for (int i = 1; i <= count; i++) {
			columnNames.add(rsmd.getColumnName(i).toLowerCase());
		}
		JSONArray ja = new JSONArray();

		while (rs.next()) {
			JSONObject obj = new JSONObject();
			ja.add(obj);
			for (String columnName : columnNames) {
				obj.put(columnName, rs.getString(columnName));
			}
		}
		out.println(ja);
	}

	public void show(PrintWriter out) throws SQLException {
		PreparedStatement ps = con.prepareStatement("select * from request");
		ResultSet rs = ps.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		Set<String> columnNames = new HashSet<>();
		for (int i = 1; i <= count; i++) {
			columnNames.add(rsmd.getColumnName(i).toLowerCase());
		}
		JSONArray ja = new JSONArray();

		while (rs.next()) {
			JSONObject obj = new JSONObject();
			ja.add(obj);
			for (String columnName : columnNames) {
				obj.put(columnName, rs.getString(columnName));
			}
		}
		out.println(ja);
	}
}
