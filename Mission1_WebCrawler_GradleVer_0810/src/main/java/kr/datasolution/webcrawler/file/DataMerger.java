package kr.datasolution.webcrawler.file;

import java.util.ArrayList;
import java.util.HashMap;

import kr.datasolution.webcrawler.crawl.CrawledData;

public class DataMerger {
	public static ArrayList<CrawledData> pickOutNewDatas(HashMap<String, CrawledData> old_datas, ArrayList<CrawledData> datas) {
		System.out.println("-- start pickOutNewDatas");
		ArrayList<CrawledData> new_datas = new ArrayList<CrawledData>();
		
		for (CrawledData data : datas) {
			// 기존 데이터에 새로운 데이터가 포함되지 않으면 할당; 키값인 '날짜 + " " + 제목' 의 유무에 따라 데이터 할당
			if(!old_datas.containsKey(data.getDate() + " " + data.getTitle())) {
				new_datas.add(data);
			}
		}
		
		System.out.println("-- end pickOutNewDatas");
		return new_datas;
	}
}
