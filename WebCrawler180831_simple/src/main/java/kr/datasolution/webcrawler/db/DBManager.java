package kr.datasolution.webcrawler.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBManager {
	public static Connection connect() throws SQLException {
		String url = "jdbc:mysql://192.168.210.146:3306/DataLake_Proto_Test?characterEncoding=UTF-8&serverTimezone=UTC&autoReconnect=true";
		return DriverManager.getConnection(url, "root", "Opensns!#@$");
	}
	
	public static void destroy(Connection con, PreparedStatement pstmt, ResultSet rs) {
		try {rs.close();} catch (Exception e) {}
		try {pstmt.close();} catch (Exception e) {}
		try {con.close();} catch (Exception e) {}
	}
}
