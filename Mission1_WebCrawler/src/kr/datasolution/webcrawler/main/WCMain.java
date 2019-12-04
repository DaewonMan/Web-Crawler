package kr.datasolution.webcrawler.main;

import java.util.ArrayList;
import java.util.HashMap;

import kr.datasolution.webcrawler.crawl.CrawledData;
import kr.datasolution.webcrawler.crawl.WebCrawler;
import kr.datasolution.webcrawler.file.DataMerger;
import kr.datasolution.webcrawler.file.FileConverter;
import kr.datasolution.webcrawler.file.TextWriter;
import kr.datasolution.webcrawler.json.JsonData;
import kr.datasolution.webcrawler.json.JsonInputer;

public class WCMain {
	public static void main(String[] args) {
		ArrayList<CrawledData> datas; // ���� ���� �ܾ�� �����͸� ����
		ArrayList<CrawledData> new_datas; // �ܾ�� ������ �� ���ο� �����͸� ����
		HashMap<String, CrawledData> old_datas; // ������ ����Ǿ� �ִ� �����͸� ����
		
		try {
			// json file ��������
			String json_ctt = FileConverter.convertToString();

			// json data�� �ڹٺ� �Ҵ�
			JsonData jsonInfo = JsonInputer.inputToBean(json_ctt);
			
			// �����κ��� �ʿ��� ������ ����
			datas = WebCrawler.addCrawledDataToList(jsonInfo);
			
			// ������ ����� �ִ� scd���� ����
			old_datas = FileConverter.convertScdToMap(jsonInfo.getStorage_path());
			
			// �ߺ��� �����͸� �����ϰ� ���ο� �����͸� ����
			new_datas = DataMerger.pickOutNewDatas(old_datas, datas);

			// ������ �����͸� SCD�������� �ؽ�Ʈ���Ͽ� ����
			TextWriter.writeDataToText(new_datas, jsonInfo.getStorage_path());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}