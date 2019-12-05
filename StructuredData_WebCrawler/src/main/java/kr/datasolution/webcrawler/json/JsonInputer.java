package kr.datasolution.webcrawler.json;

import com.google.gson.Gson;

public class JsonInputer {
	public static JsonData inputToJsonDataBean(String readData) throws Exception {
		System.out.println("Start inputToBean =====");

		// from String data to JsonData instance
		JsonData jsonData = new Gson().fromJson(readData, JsonData.class);
		
		System.out.println("End inputToBean =====");
		return jsonData;
	}
}
