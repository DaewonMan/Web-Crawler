package com.dwm.webcrawler.main;

import java.util.ArrayList;

import com.dwm.webcrawler.crawl.Crawler;
import com.dwm.webcrawler.db.DbData;
import com.dwm.webcrawler.json.JsonConverter;
import com.dwm.webcrawler.json.JsonData;
import com.dwm.webcrawler.json.JsonInputer;

public class Main {
	public static void main(String[] args) {
		//ArrayList<DbData> dbDatas = null; // DB�� �ʵ忡 �°� �����͸� �����ϴ� ��ü
		try {
			// json file�� �о�� String ��ü�� ���
			String readData = JsonConverter.convertToString();

			// json ������ String ��ü�� �̿��Ͽ� JsonData ��ü�� �Ҵ��ϱ�
			JsonData jsonData = JsonInputer.inputToJsonDataBean(readData);
			
			// �����κ��� �ʿ��� ������ ����
			Crawler.addCrawlDataToList(jsonData);
			/*if(jsonData.getDid() == 70) {
				dbDatas = Crawler.addCrawlUNNewsDataToList(jsonData); // DID�� 70�̸� UN News		
			} else if(jsonData.getDid() == 71){
				dbDatas = Crawler.addCrawlWorldBankDataToList(jsonData); // DID�� 71�̸� World Bank								
			} else {
				System.out.println("DID is not 70 or 71");
				return; // �׳� ����
			}*/
			
			// file path�� ����Ÿ �����ϱ�
			//FileOutputer.writeFileInDir(dbDatas);
			
			// db�� �����ϱ�
			//DAO.addCrawlDataToDB(dbDatas);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
