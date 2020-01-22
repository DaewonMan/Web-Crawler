package com.dwm.webcrawler.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.dwm.webcrawler.crawl.CrawledData;

public class FileConverter {
	public static String convertToString() throws Exception {
		// 내용 빼낼 준비
		InputStream is = new FileInputStream("webSiteInfo.json");
		InputStreamReader isr = new InputStreamReader(is);
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(isr);

		String line = null;
		StringBuffer sb = new StringBuffer();

		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		return sb.toString();
	}

	public static HashMap<String, CrawledData> convertScdToMap(String path) throws Exception {
		// 내용 빼낼 준비
		InputStream is = new FileInputStream(path);
		InputStreamReader isr = new InputStreamReader(is);
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(isr);

		HashMap<String, CrawledData> data_map = new HashMap<String, CrawledData>();
		String line = null;
		String title = null, name = null, date = null, content = null;
		
		while ((line = br.readLine()) != null) {
			title = line.substring(line.indexOf(":") + 1); // ^title:~ 이런형식이니 : 이후부터 실제 내용
			
			line = br.readLine();
			date = line.substring(line.indexOf(":") + 1);
			
			line = br.readLine();
			name = line.substring(line.indexOf(":") + 1);
			
			line = br.readLine();
			content = line.substring(line.indexOf(":") + 1);
			
			data_map.put(date + " " + title, new CrawledData(title, content, name, date));
		}

		return data_map;
	}
}
