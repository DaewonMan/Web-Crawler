package com.dwm.webcrawler.file;

import java.util.ArrayList;
import java.util.HashMap;

import com.dwm.webcrawler.crawl.CrawledData;

public class DataMerger {
	public static ArrayList<CrawledData> pickOutNewDatas(HashMap<String, CrawledData> old_datas, ArrayList<CrawledData> datas) {
		
		ArrayList<CrawledData> new_datas = new ArrayList<>();
		
		for (CrawledData data : datas) {
			// ���� �����Ϳ� ���ο� �����Ͱ� ���Ե��� ������ �Ҵ�; Ű���� '��¥ + " " + ����' �� ������ ���� ������ �Ҵ�
			if(!old_datas.containsKey(data.getDate() + " " + data.getTitle())) {
				new_datas.add(data);
			}
		}
		
		return new_datas;
	}
}