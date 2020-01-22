package com.dwm.webcrawler.db;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.dwm.webcrawler.crawl.CrawlData;
import com.dwm.webcrawler.json.JsonData;
import com.dwm.webcrawler.write.FileRelatedMethods;

public class DbRelatedDataInputer {
	//private static int cidCnt = 0;
	public static DbTableData addToDbTableData(CrawlData crawlData, JsonData jsonData, String crawledTime, String url, String jsonFileName) throws Exception {
		System.out.println("Start addToDbTableData =====");
		DbTableData dbTableData = null;
		
		String path = "", orgFileName = "", savFileName = "";
		
		SimpleDateFormat sdfBasic = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
		SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		SimpleDateFormat sdfKo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
		
		/// cid�� �ް� ���� 
		String cid = FileRelatedMethods.getCidAndPlus(jsonData.getBasicStoragePath() + jsonData.getJsonFilePath() + jsonData.getCidFlagFile());
				
		// ���� ���
		path = jsonData.getBasicStoragePath() + jsonData.getDid() + "/" + sdfBasic.format(sdfKo.parse(crawledTime)) + "/" + cid + "/" + 1;
		
		// ORG_FILE_NAME ����; title�� ������ jsonFileName�̴�
		orgFileName = jsonFileName;
		
		// SAV_FILE_NAME ����
		savFileName = jsonData.getDid() + "-" + sdfFileName.format(sdfKo.parse(crawledTime)) + "-" + cid + "-" + 1;
		
		// dbData ��ü ����
		dbTableData = new DbTableData(jsonData.getDid(), crawledTime, Integer.parseInt(cid), 1, jsonFileName, null,
				orgFileName, savFileName, path, jsonData.getFileExt(), jsonData.getFileType(), url, 
				jsonData.getPreHandlingType(), jsonData.getHashTag()); 
				
		System.out.println("End addToDbTableData =====");

		return dbTableData;
	}
	
	public static DbMetaData makeDbMetaData(DbTableData dbTableData) {
		return new DbMetaData(dbTableData.getDid(), dbTableData.getCrawledTime(),
				dbTableData.getCid(), dbTableData.getAid(),
				null, null, dbTableData.getFilePath(),
				dbTableData.getSavFileName(), dbTableData.getFileExt(), null);
	}
}
