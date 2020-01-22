package com.dwm.webcrawler.db;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.dwm.webcrawler.crawl.CrawlData;
import com.dwm.webcrawler.json.JsonData;

public class DbRelatedDataInputer {
	private static int cidCnt = 0;
	public static DbData addToDbData(CrawlData crawlData, JsonData jsonData, String webFileName) throws Exception {
		System.out.println("Start addToDbData =====");
		DbData dbData = null;
		
		String path = "", fileName = "", orgFileName = "";
		
		SimpleDateFormat sdfBasic = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
		SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		SimpleDateFormat sdfKo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
		
		jsonData.setCid(++cidCnt); // view에 따라 CID 값 증가 
			
		// 파일이름 설정
		fileName = jsonData.getDid() + "-" + sdfFileName.format(sdfKo.parse(crawlData.getCrawledTime())) + "-" + jsonData.getCid() + "-" + jsonData.getAid();
		
		// 파일 경로
		path = jsonData.getBasicStoragePath() + jsonData.getDid() + "/" + sdfBasic.format(sdfKo.parse(crawlData.getCrawledTime())) + "/" + jsonData.getCid() + "/" + jsonData.getAid();
		
		// ORG_FILE_NAME 설정
		if(jsonData.getDid() == 71) { orgFileName = webFileName; }
		else { orgFileName = crawlData.getTitle(); }
		
		// dbData 객체 생성
		dbData = new DbData(new BigDecimal(jsonData.getDid()), crawlData.getCrawledTime(), new BigDecimal(jsonData.getCid()),
				new BigDecimal(jsonData.getAid()), crawlData.getTitle(), crawlData.getDate(), orgFileName, fileName, path,
				jsonData.getFileExt(), jsonData.getFileType(), crawlData.getUrl(), jsonData.getPreHandlingType(), jsonData.getHashTag(), crawlData.getContent());
		
		System.out.println("End addToDbData =====");

		return dbData;
	}
	
	public static DbMetaData makeDbMetaData(DbData dbData) {
		return new DbMetaData(Integer.parseInt(dbData.getDid().toString()), dbData.getCrawledTime(),
				Integer.parseInt(dbData.getCid().toString()), Integer.parseInt(dbData.getAid().toString()),
				dbData.getTitle(), dbData.getCreated(), dbData.getFilePath(),
				dbData.getSavFileName(), dbData.getFileExt(), null);
	}
}
