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
	// ������ο� ��Ÿ���ϰ� ����Ÿ������ �������ִ� �޼ҵ�
	public static String writeJsonFileInDir(CrawlData crawlData, JsonData jsonData) throws Exception {
		System.out.println("Start writeJsonFileInDir =====");
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		Gson inputgson = new GsonBuilder().setPrettyPrinting().create();
		List<CrawlData> crawlDataList = null;
		int basicFileNum = 0;
		String realJsonFilePath = "";
		String	jsonFileName = "";
				
		// Json���ϸ��� ������ �ε������� ���ϱ� ���� ����; basicFileNum�� ���ϱ� ���� ����; ���� �� ���� ū �ε��� ���� �Ҵ�
		/*basicFileNum = FileRelatedMethods
						.getJsonFileIndex(crawlData, jsonData.getBasicStoragePath() + jsonData.getJsonFilePath());*/
		basicFileNum = FileRelatedMethods.getCountryCodeNum(crawlData, jsonData, jsonData.getBasicStoragePath() + jsonData.getJsonFilePath() + jsonData.getCountryCodeFlagFile());
		
		jsonFileName = crawlData.getCode() + "_IP_ADDRESS_JSON_" + basicFileNum; // �����̸� ����; �����ڵ�_IP_ADDRESS_JSON_����
		realJsonFilePath = jsonData.getBasicStoragePath() + jsonData.getJsonFilePath() + jsonFileName + "." + jsonData.getFileExt();
		
		//���� ���� ���ο� ���� ������ �Ҵ�
		if(!FileRelatedMethods.checkExistFile(realJsonFilePath)) {
			crawlDataList = new ArrayList<CrawlData>();
			crawlDataList.add(crawlData);
		} else {
			crawlDataList = inputgson.fromJson(FileUtils.readFileToString(new File(realJsonFilePath), "UTF-8")
					, new TypeToken<List<CrawlData>>() {}.getType());
			crawlDataList.add(crawlData);			
		}
		
		// json file ����
		FileUtils.writeStringToFile(new File(realJsonFilePath), gson.toJson(crawlDataList), "UTF-8");
		
		System.out.println("End writeJsonFileInDir =====");
		
		return jsonFileName;
	}
	
	// ������ο� �Ϸ�� json file�� �̵���Ų��
	public static void moveCompleteJsonFileInDir(DbTableData dbTableData, CrawlData crawlData, JsonData jsonData, String realJsonFilePath) throws Exception {
		System.out.println("Start moveCompleteJsonFileInDir =====");
		
		// directory ���ο� ���� ����
		FileRelatedMethods.generateDir(dbTableData.getFilePath());
		
		// ���� ����
		FileUtils.copyFile(new File(realJsonFilePath), 
				new File(dbTableData.getFilePath() + "/" + dbTableData.getSavFileName() + "." + jsonData.getFileExt()));
				
		//���� ���� ����
		//FileUtils.writeStringToFile(new File(realJsonFilePath), "", "UTF-8");
		
		// countryCode flag ���Ͽ� ���� �ε��� ��ȣ ����
		String[] elements = null;
		String cCodesMdf = "";
		List<String> cCodes = FileUtils.readLines(new File(jsonData.getBasicStoragePath() + jsonData.getJsonFilePath()+ jsonData.getCountryCodeFlagFile()),
				"UTF-8"); 
		if(cCodes.size() <= 0) {
			// ���Ͽ� �ƹ� ���� ���� ��
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
	
	// ������ο� ��Ÿ���ϰ� ����Ÿ������ �������ִ� �޼ҵ�
	public static void writeMetaFileInDir(DbTableData dbTableData) throws Exception {
		System.out.println("Start writeFileInDir =====");
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

		// directory ���ο� ���� ����
		//FileRelatedMethods.generateDir(dbTableData.getFilePath());
		
		// ��Ÿ���� ����
		FileUtils.writeStringToFile(new File(dbTableData.getFilePath() + "/" + dbTableData.getSavFileName() + ".meta"),
				gson.toJson(DbRelatedDataInputer.makeDbMetaData(dbTableData)), "UTF-8");

		System.out.println("End writeFileInDir =====");
	}
}
