package com.dwm.webcrawler.json;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonConverter {
	public static String convertToString() throws Exception {
		System.out.println("Start convertToString =====");
		// 내용 빼낼 준비
		InputStream is = new FileInputStream("initSetting.json");
		InputStreamReader isr = new InputStreamReader(is);
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(isr);

		String line = null;
		StringBuffer sb = new StringBuffer();

		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		System.out.println("End convertToString =====");
		return sb.toString();
	}
}
