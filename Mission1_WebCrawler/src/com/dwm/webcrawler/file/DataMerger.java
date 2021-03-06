package com.dwm.webcrawler.file;

import java.util.ArrayList;
import java.util.HashMap;

import com.dwm.webcrawler.crawl.CrawledData;

public class DataMerger {
	public static ArrayList<CrawledData> pickOutNewDatas(HashMap<String, CrawledData> old_datas, ArrayList<CrawledData> datas) {
		
		ArrayList<CrawledData> new_datas = new ArrayList<>();
		
		for (CrawledData data : datas) {
			// 기존 데이터에 새로운 데이터가 포함되지 않으면 할당; 키값인 '날짜 + " " + 제목' 의 유무에 따라 데이터 할당
			if(!old_datas.containsKey(data.getDate() + " " + data.getTitle())) {
				new_datas.add(data);
			}
		}
		
		return new_datas;
	}
}
