package kr.datasolution.webcrawler.main;

import kr.datasolution.webcrawler.crawl.Crawler;
import kr.datasolution.webcrawler.json.JsonConverter;
import kr.datasolution.webcrawler.json.JsonData;
import kr.datasolution.webcrawler.json.JsonInputer;

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
