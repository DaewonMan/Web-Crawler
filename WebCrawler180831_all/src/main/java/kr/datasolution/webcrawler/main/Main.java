package kr.datasolution.webcrawler.main;

import java.util.ArrayList;

import kr.datasolution.webcrawler.crawl.Crawler;
import kr.datasolution.webcrawler.db.DAO;
import kr.datasolution.webcrawler.db.DbData;
import kr.datasolution.webcrawler.json.JsonConverter;
import kr.datasolution.webcrawler.json.JsonData;
import kr.datasolution.webcrawler.json.JsonInputer;
import kr.datasolution.webcrawler.write.FileOutputer;

public class Main {
	public static void main(String[] args) {
		ArrayList<DbData> dbDatas = null; // DB의 필드에 맞게 데이터를 저장하는 객체
		try {
			// json file을 읽어와 String 객체에 담기
			String readData = JsonConverter.convertToString();

			// json 형식의 String 객체를 이용하여 JsonData 객체에 할당하기
			JsonData jsonData = JsonInputer.inputToJsonDataBean(readData);
			
			// 웹으로부터 필요한 데이터 수집
			dbDatas = Crawler.addCrawlDataToList(jsonData);
			/*if(jsonData.getDid() == 70) {
				dbDatas = Crawler.addCrawlUNNewsDataToList(jsonData); // DID가 70이면 UN News		
			} else if(jsonData.getDid() == 71){
				dbDatas = Crawler.addCrawlWorldBankDataToList(jsonData); // DID가 71이면 World Bank								
			} else {
				System.out.println("DID is not 70 or 71");
				return; // 그냥 종료
			}*/
			
			// file path에 데이타 저장하기
			FileOutputer.writeFileInDir(dbDatas);
			
			// db에 저장하기
			DAO.addCrawlDataToDB(dbDatas);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
