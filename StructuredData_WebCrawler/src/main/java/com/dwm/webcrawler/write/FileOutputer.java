package com.dwm.webcrawler.write;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.dwm.webcrawler.crawl.CrawlData;
import com.dwm.webcrawler.db.DbRelatedDataInputer;
import com.dwm.webcrawler.db.DbTableData;
import com.dwm.webcrawler.json.JsonData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class FileOutputer {
	// 지정경로에 메타파일과 데이타파일을 저장해주는 메소드
	public static String writeJsonFileInDir(CrawlData crawlData, JsonData jsonData) throws Exception {
		System.out.println("Start writeJsonFileInDir =====");
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		Gson inputgson = new GsonBuilder().setPrettyPrinting().create();
		List<CrawlData> crawlDataList = null;
		int basicFileNum = 0;
		String realJsonFilePath = "";
		String	jsonFileName = "";
				
		// Json파일명의 마지막 인덱스값을 정하기 위한 로직; basicFileNum을 정하기 위한 로직; 파일 중 가장 큰 인덱스 값을 할당
		/*basicFileNum = FileRelatedMethods
						.getJsonFileIndex(crawlData, jsonData.getBasicStoragePath() + jsonData.getJsonFilePath());*/
		basicFileNum = FileRelatedMethods.getCountryCodeNum(crawlData, jsonData, jsonData.getBasicStoragePath() + jsonData.getJsonFilePath() + jsonData.getCountryCodeFlagFile());
		
		jsonFileName = crawlData.getCode() + "_IP_ADDRESS_JSON_" + basicFileNum; // 파일이름 설정; 국가코드_IP_ADDRESS_JSON_숫자
		realJsonFilePath = jsonData.getBasicStoragePath() + jsonData.getJsonFilePath() + jsonFileName + "." + jsonData.getFileExt();
		
		//파일 존재 여부에 따라 데이터 할당
		if(!FileRelatedMethods.checkExistFile(realJsonFilePath)) {
			crawlDataList = new ArrayList<CrawlData>();
			crawlDataList.add(crawlData);
		} else {
			crawlDataList = inputgson.fromJson(FileUtils.readFileToString(new File(realJsonFilePath), "UTF-8")
					, new TypeToken<List<CrawlData>>() {}.getType());
			crawlDataList.add(crawlData);			
		}
		
		// json file 생성
		FileUtils.writeStringToFile(new File(realJsonFilePath), gson.toJson(crawlDataList), "UTF-8");
		
		System.out.println("End writeJsonFileInDir =====");
		
		return jsonFileName;
	}
	
	// 지정경로에 완료된 json file을 이동시킨다
	public static void moveCompleteJsonFileInDir(DbTableData dbTableData, CrawlData crawlData, JsonData jsonData, String realJsonFilePath) throws Exception {
		System.out.println("Start moveCompleteJsonFileInDir =====");
		
		// directory 여부에 따라 생성
		FileRelatedMethods.generateDir(dbTableData.getFilePath());
		
		// 파일 이전
		FileUtils.copyFile(new File(realJsonFilePath), 
				new File(dbTableData.getFilePath() + "/" + dbTableData.getSavFileName() + "." + jsonData.getFileExt()));
				
		//파일 내용 삭제
		//FileUtils.writeStringToFile(new File(realJsonFilePath), "", "UTF-8");
		
		// countryCode flag 파일에 파일 인덱스 번호 쓰기
		String[] elements = null;
		String cCodesMdf = "";
		List<String> cCodes = FileUtils.readLines(new File(jsonData.getBasicStoragePath() + jsonData.getJsonFilePath()+ jsonData.getCountryCodeFlagFile()),
				"UTF-8"); 
		if(cCodes.size() <= 0) {
			// 파일에 아무 내용 없을 시
			FileUtils.writeStringToFile(new File(jsonData.getBasicStoragePath() + jsonData.getJsonFilePath()+ jsonData.getCountryCodeFlagFile()),
					crawlData.getCode()+"="+2, "UTF-8");
		} else {
			for (String cCode : cCodes) {
				elements = cCode.split("=");
				if(elements[0].equalsIgnoreCase(crawlData.getCode())) {
					elements[1] = (Integer.parseInt(elements[1]) + 1) + "";
				}
				cCodesMdf += elements[0] + "=" + elements[1] + "\r\n";
			}
			FileUtils.writeStringToFile(new File(jsonData.getBasicStoragePath() + jsonData.getJsonFilePath()+ jsonData.getCountryCodeFlagFile()), 
					cCodesMdf.toString(), "UTF-8");
		}
		
		System.out.println("End moveCompleteJsonFileInDir =====");
	}
	
	// 지정경로에 메타파일과 데이타파일을 저장해주는 메소드
	public static void writeMetaFileInDir(DbTableData dbTableData) throws Exception {
		System.out.println("Start writeFileInDir =====");
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

		// directory 여부에 따라 생성
		//FileRelatedMethods.generateDir(dbTableData.getFilePath());
		
		// 메타파일 생성
		FileUtils.writeStringToFile(new File(dbTableData.getFilePath() + "/" + dbTableData.getSavFileName() + ".meta"),
				gson.toJson(DbRelatedDataInputer.makeDbMetaData(dbTableData)), "UTF-8");

		System.out.println("End writeFileInDir =====");
	}
}
