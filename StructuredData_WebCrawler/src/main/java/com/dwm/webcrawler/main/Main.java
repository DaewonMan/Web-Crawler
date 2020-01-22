package com.dwm.webcrawler.main;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.dwm.webcrawler.ipclass.IpComposition;
import com.dwm.webcrawler.json.JsonConverter;
import com.dwm.webcrawler.json.JsonData;
import com.dwm.webcrawler.json.JsonInputer;
import com.dwm.webcrawler.write.FileRelatedMethods;

/***
 * CLASS ���� ���� �� A Ŭ���� xxx.xxx.xxx.xxx 1.0.0.1 ~ 126.255.255.254 61.211.123.22 B
 * Ŭ���� xxx.xxx.xxx.xxx 128.0.0.1 ~ 191.255.255.254 181.123.211.33 C Ŭ����
 * xxx.xxx.xxx.xxx 192.0.0.1 ~ 223.255.255.254 221.23.222.222 D Ŭ���� 224.0.0.0 ~
 * 239.255.255.255 E Ŭ���� 240.0.0.0 ~ 254.255.255.254
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
			firstStart = "false"; //args[3] => true�� ó������, false�� ���� ��������
			
			// json file�� �о�� String ��ü�� ���
			readData = JsonConverter.convertToString(jsonFile);

			// json ������ String ��ü�� �̿��Ͽ� JsonData ��ü�� �Ҵ��ϱ�
			jsonData = JsonInputer.inputToJsonDataBean(readData);
			
			// flag�� ����� ���� ����
			FileRelatedMethods.generateDir(jsonData.getBasicStoragePath() + jsonData.getJsonFilePath());
			// cid ���� ����
			FileRelatedMethods.generateFile(jsonData.getBasicStoragePath() + jsonData.getJsonFilePath() + jsonData.getCidFlagFile());
			// cid ���� �ʱ�ȭ �� ���� ������ �Ҵ�
			FileRelatedMethods.initializeCidFlag(jsonData.getBasicStoragePath() + jsonData.getJsonFilePath() + jsonData.getCidFlagFile(), Boolean.parseBoolean(firstStart));
			// ip���� ����
			FileRelatedMethods.generateFile(jsonData.getBasicStoragePath() + jsonData.getJsonFilePath() + jsonData.getIpFlagFile());
			// ip ���� ������ �Ҵ�
			if(firstStart.equalsIgnoreCase("false")) {
				ipAddr = FileRelatedMethods.getIpFlag(jsonData.getBasicStoragePath() + jsonData.getJsonFilePath() + jsonData.getIpFlagFile()).split("\\.");
			}
			// country code ���� ����
			FileRelatedMethods.generateFile(jsonData.getBasicStoragePath() + jsonData.getJsonFilePath() + jsonData.getCountryCodeFlagFile());
			
			// ���� ���� ����
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String crawledTime = sdf.format(new Date()); // ���� ���� �ð�
			
			// ALL class�� �ش��ϴ� ip�ּ� ����
			IpComposition.callALLClassURL(jsonData, agentFile, proxyFile, crawledTime, ipAddr);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
