package kr.datasolution.webcrawler.db;

import java.sql.Connection;
import java.sql.PreparedStatement;

import kr.datasolution.webcrawler.json.JsonData;

public class DAO {
	public static void addCrawlDataToDB(DbTableData dbTableData, JsonData jsonData) {
		System.out.println("Start addCrawlDataToDB =====");
		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "";
		try {
			conn = DBManager.connect(jsonData);

			sql = "insert into " + jsonData.getDbTableName() + " (DID, CRAWLEDTIME, CID, AID, TITLE, ORG_FILE_NAME,"
					+ "SAV_FILE_NAME, FILE_PATH, FILE_EXT, FILE_TYPE, URL, PRE_HANDLING_TYPE, TAG, CREATED) "
					+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now())";

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, dbTableData.getDid());
			pstmt.setString(2, dbTableData.getCrawledTime());
			pstmt.setInt(3, dbTableData.getCid());
			pstmt.setInt(4, dbTableData.getAid());
			pstmt.setString(5, dbTableData.getTitle());
			pstmt.setString(6, dbTableData.getOrgFileName());
			pstmt.setString(7, dbTableData.getSavFileName());
			pstmt.setString(8, dbTableData.getFilePath());
			pstmt.setString(9, dbTableData.getFileExt());
			pstmt.setString(10, dbTableData.getFileType() + "");
			pstmt.setString(11, dbTableData.getUrl());
			pstmt.setString(12, dbTableData.getPreHandlingType());
			pstmt.setString(13, dbTableData.getTag());
//			pstmt.setString(14, "now()");

			if (pstmt.executeUpdate() == 1) {
				// db에 데이터 넣기 성공
				System.out.println("Success insert to DB");
				// pstmt.close();
			} else {
				System.out.println("Fail insert to DB");
			}
			System.out.println("End addCrawlDataToDB =====");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception" + dbTableData.getDid() + "." + dbTableData.getCid() + "." + dbTableData.getAid());
		} finally {
			DBManager.destroy(conn, pstmt, null);
		}
	}
}
