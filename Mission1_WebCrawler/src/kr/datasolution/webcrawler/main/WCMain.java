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
		ArrayList<CrawledData> datas; // 웹을 통해 긁어온 데이터를 저장
		ArrayList<CrawledData> new_datas; // 긁어온 데이터 중 새로운 데이터를 저장
		HashMap<String, CrawledData> old_datas; // 이전에 저장되어 있던 데이터를 저장
		
		try {
			// json file 가져오기
			String json_ctt = FileConverter.convertToString();

			// json data를 자바빈에 할당
			JsonData jsonInfo = JsonInputer.inputToBean(json_ctt);
			
			// 웹으로부터 필요한 데이터 수집
			datas = WebCrawler.addCrawledDataToList(jsonInfo);
			
			// 기존에 저장돼 있는 scd파일 수집
			old_datas = FileConverter.convertScdToMap(jsonInfo.getStorage_path());
			
			// 중복된 데이터를 제외하고 새로운 데이터만 저장
			new_datas = DataMerger.pickOutNewDatas(old_datas, datas);

			// 수집한 데이터를 SCD형식으로 텍스트파일에 저장
			TextWriter.writeDataToText(new_datas, jsonInfo.getStorage_path());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}