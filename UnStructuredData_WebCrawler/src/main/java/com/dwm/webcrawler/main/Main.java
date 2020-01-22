package com.dwm.webcrawler.main;

import com.dwm.webcrawler.crawl.Crawler;
import com.dwm.webcrawler.json.JsonConverter;
import com.dwm.webcrawler.json.JsonData;
import com.dwm.webcrawler.json.JsonInputer;

public class Main {
	public static void main(String[] args) {
		String agentFile = null;
		try {
			// json file�� �о�� String ��ü�� ���
			String readData = JsonConverter.convertToString(args[0]);

			// json ������ String ��ü�� �̿��Ͽ� JsonData ��ü�� �Ҵ��ϱ�
			JsonData jsonData = JsonInputer.inputToJsonDataBean(readData);
			
			// ���� ��, �ι�° agument�� �־����� �Ҵ�
			if (args.length > 1) agentFile = args[1];
			
			// �����κ��� �ʿ��� ������ ����
			Crawler.crawlDataAndSaveIt(jsonData, agentFile);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
