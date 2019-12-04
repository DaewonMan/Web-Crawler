package kr.datasolution.webcrawler.file;

import java.util.ArrayList;
import java.util.HashMap;

import kr.datasolution.webcrawler.crawl.CrawledData;

public class DataMerger {
	public static ArrayList<CrawledData> pickOutNewDatas(HashMap<String, CrawledData> old_datas, ArrayList<CrawledData> datas) {
		System.out.println("-- start pickOutNewDatas");
		ArrayList<CrawledData> new_datas = new ArrayList<CrawledData>();
		
		for (CrawledData data : datas) {
			// ���� �����Ϳ� ���ο� �����Ͱ� ���Ե��� ������ �Ҵ�; Ű���� '��¥ + " " + ����' �� ������ ���� ������ �Ҵ�
			if(!old_datas.containsKey(data.getDate() + " " + data.getTitle())) {
				new_datas.add(data);
			}
		}
		
		System.out.println("-- end pickOutNewDatas");
		return new_datas;
	}
}
