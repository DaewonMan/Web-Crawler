package kr.datasolution.webcrawler.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.datasolution.webcrawler.json.JsonData;

public class DBManager {
	public static Connection connect(JsonData jsonData) throws SQLException {
		String url = jsonData.getDbUrl(); 
		return DriverManager.getConnection(url, jsonData.getDbId(), jsonData.getDbPw());
	}
	
	public static void destroy(Connection con, PreparedStatement pstmt, ResultSet rs) {
		try {rs.close();} catch (Exception e) {}
		try {pstmt.close();} catch (Exception e) {}
		try {con.close();} catch (Exception e) {}
	}
}
