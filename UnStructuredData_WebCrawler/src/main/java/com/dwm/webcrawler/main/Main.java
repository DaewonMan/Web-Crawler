package com.dwm.webcrawler.main;

import com.dwm.webcrawler.crawl.Crawler;
import com.dwm.webcrawler.json.JsonConverter;
import com.dwm.webcrawler.json.JsonData;
import com.dwm.webcrawler.json.JsonInputer;

public class Main {
	public static void main(String[] args) {
		String agentFile = null;
		try {
			// json file을 읽어와 String 객체에 담기
			String readData = JsonConverter.convertToString(args[0]);

			// json 형식의 String 객체를 이용하여 JsonData 객체에 할당하기
			JsonData jsonData = JsonInputer.inputToJsonDataBean(readData);
			
			// 실행 시, 두번째 agument를 넣었으면 할당
			if (args.length > 1) agentFile = args[1];
			
			// 웹으로부터 필요한 데이터 수집
			Crawler.crawlDataAndSaveIt(jsonData, agentFile);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
