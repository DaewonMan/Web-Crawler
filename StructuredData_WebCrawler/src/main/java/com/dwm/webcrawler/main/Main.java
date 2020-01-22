package com.dwm.webcrawler.main;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.dwm.webcrawler.ipclass.IpComposition;
import com.dwm.webcrawler.json.JsonConverter;
import com.dwm.webcrawler.json.JsonData;
import com.dwm.webcrawler.json.JsonInputer;
import com.dwm.webcrawler.write.FileRelatedMethods;

/***
 * CLASS 구성 범위 예 A 클래스 xxx.xxx.xxx.xxx 1.0.0.1 ~ 126.255.255.254 61.211.123.22 B
 * 클래스 xxx.xxx.xxx.xxx 128.0.0.1 ~ 191.255.255.254 181.123.211.33 C 클래스
 * xxx.xxx.xxx.xxx 192.0.0.1 ~ 223.255.255.254 221.23.222.222 D 클래스 224.0.0.0 ~
 * 239.255.255.255 E 클래스 240.0.0.0 ~ 254.255.255.254
 ***/
public class Main {
	public static void main(String[] args) {
		String jsonFile = null, agentFile = null, proxyFile = null, firstStart = null;
		
		String readData = null;
		JsonData jsonData = null;
		String[] ipAddr = null;
		
		try {
			ipAddr = new String[] {};
			jsonFile = "initSetting.json"; //args[0]
			agentFile = "all_user_agent.txt"; //args[1]
			proxyFile = "all_proxy_list.txt"; // args[2], https://hidemyna.me/en/proxy-list/#list
			firstStart = "false"; //args[3] => true면 처음부터, false면 멈춘 시점부터
			
			// json file을 읽어와 String 객체에 담기
			readData = JsonConverter.convertToString(jsonFile);

			// json 형식의 String 객체를 이용하여 JsonData 객체에 할당하기
			jsonData = JsonInputer.inputToJsonDataBean(readData);
			
			// flag가 저장될 폴더 생성
			FileRelatedMethods.generateDir(jsonData.getBasicStoragePath() + jsonData.getJsonFilePath());
			// cid 파일 생성
			FileRelatedMethods.generateFile(jsonData.getBasicStoragePath() + jsonData.getJsonFilePath() + jsonData.getCidFlagFile());
			// cid 파일 초기화 및 기존 데이터 할당
			FileRelatedMethods.initializeCidFlag(jsonData.getBasicStoragePath() + jsonData.getJsonFilePath() + jsonData.getCidFlagFile(), Boolean.parseBoolean(firstStart));
			// ip파일 생성
			FileRelatedMethods.generateFile(jsonData.getBasicStoragePath() + jsonData.getJsonFilePath() + jsonData.getIpFlagFile());
			// ip 기존 데이터 할당
			if(firstStart.equalsIgnoreCase("false")) {
				ipAddr = FileRelatedMethods.getIpFlag(jsonData.getBasicStoragePath() + jsonData.getJsonFilePath() + jsonData.getIpFlagFile()).split("\\.");
			}
			// country code 파일 생성
			FileRelatedMethods.generateFile(jsonData.getBasicStoragePath() + jsonData.getJsonFilePath() + jsonData.getCountryCodeFlagFile());
			
			// 수집 시점 저장
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String crawledTime = sdf.format(new Date()); // 수집 시작 시간
			
			// ALL class에 해당하는 ip주소 수집
			IpComposition.callALLClassURL(jsonData, agentFile, proxyFile, crawledTime, ipAddr);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
