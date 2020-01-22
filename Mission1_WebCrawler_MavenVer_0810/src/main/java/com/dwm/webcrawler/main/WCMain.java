package com.dwm.webcrawler.main;

import java.util.ArrayList;
import java.util.HashMap;

import com.dwm.webcrawler.crawl.CrawledData;
import com.dwm.webcrawler.crawl.WebCrawler;
import com.dwm.webcrawler.file.DataMerger;
import com.dwm.webcrawler.file.FileConverter;
import com.dwm.webcrawler.file.TextWriter;
import com.dwm.webcrawler.json.JsonData;
import com.dwm.webcrawler.json.JsonInputer;

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
			//datas = WebCrawler.addCrawledDataToList(null);
			
			// ������ ����� �ִ� scd���� ����
			old_datas = FileConverter.convertScdToMap(jsonInfo.getStorage_path());
			
			// �ߺ��� �����͸� �����ϰ� ���ο� �����͸� ����
			new_datas = DataMerger.pickOutNewDatas(old_datas, datas);
			
			System.out.println("11212121121212121");
			// ������ �����͸� SCD�������� �ؽ�Ʈ���Ͽ� ����
			TextWriter.writeDataToText(new_datas, jsonInfo.getStorage_path());
			System.out.println("454545454545454545");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
