package kr.datasolution.webcrawler.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class DAO {
	public static void addCrawlDataToDB(ArrayList<DbData> dbDatas) {
		System.out.println("Start addCrawlDataToDB =====");
		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "";
		try {
			conn = DBManager.connect();

			sql = "insert into DW_TEST_crawl_data (DID, CRAWLEDTIME, CID, AID, TITLE, CREATED, ORG_FILE_NAME,"
					+ "SAV_FILE_NAME, FILE_PATH, FILE_EXT, FILE_TYPE, URL, PRE_HANDLING_TYPE, TAG) "
					+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			for (DbData dbData : dbDatas) {
				pstmt.setBigDecimal(1, dbData.getDid());
				pstmt.setString(2, dbData.getCrawledTime());
				pstmt.setBigDecimal(3, dbData.getCid());
				pstmt.setBigDecimal(4, dbData.getAid());
				pstmt.setString(5, dbData.getTitle());
				pstmt.setString(6, dbData.getCreated());
				pstmt.setString(7, dbData.getOrgFileName());
				pstmt.setString(8, dbData.getSavFileName());
				pstmt.setString(9, dbData.getFilePath());
				pstmt.setString(10, dbData.getFileExt());
				pstmt.setString(11, dbData.getFileType()+"");
				pstmt.setString(12, dbData.getUrl());
				pstmt.setString(13, dbData.getPreHandlingType());
				pstmt.setString(14, dbData.getTag());
			
				if (pstmt.executeUpdate() == 1) { 
					// db에 데이터 넣기 성공
					System.out.println("Success insert to DB");
					//pstmt.close();
				} else { 
					System.out.println("Fail insert to DB"); 
				}
			}
			System.out.println("End addCrawlDataToDB =====");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.destroy(conn, pstmt, null);
		}
	}
}
