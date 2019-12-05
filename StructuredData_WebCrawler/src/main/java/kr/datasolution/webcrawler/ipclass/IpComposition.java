package kr.datasolution.webcrawler.ipclass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import kr.datasolution.webcrawler.crawl.Crawler;
import kr.datasolution.webcrawler.json.JsonData;
import kr.datasolution.webcrawler.write.FileRelatedMethods;

public class IpComposition {
	// ALL Ŭ����	1.0.0.0 ~ 254.255.255.255
	public static void callALLClassURL(JsonData jsonData, String agentFile, String proxyFile, String crawledTime, String[] ipAddr) throws Exception {
		System.out.println("Start callALLClassURL =====");
		Random random = new Random();
		int aRestart = 1; 
		int bRestart = 0; 
		int cRestart = 0; 
		int dRestart = 0; 
		String addUrl = "";
		
		if(ipAddr.length >= 4) {
			aRestart = Integer.parseInt(ipAddr[0]);
			bRestart = Integer.parseInt(ipAddr[1]);
			cRestart = Integer.parseInt(ipAddr[2]);
			dRestart = Integer.parseInt(ipAddr[3]);
		}
		
		A : for(int i = 1;i <= 254 ;i++) {
				if(i == 127) continue; // 127.xxx.xxx.xxx�� ���� Ŭ����
			for(int j = 0; j <= 255;j++) {
				for(int k = 0;k <= 255;k++) {
					for(int l = 0;l <= 255;l++) {
						//�ߴܵǾ� �ٽ� ������ ��
						if(i == 1 && j == 0 && k == 0 && l == 0) {
							i = aRestart;
							j = bRestart;
							k = cRestart;
							l = dRestart;
						}
							
						// A class ���� 1.0.0.1 ~ 126.255.255.254
						if((1 <= i && i <= 126) && (l == 0 || l == 255)) continue;
						// B class ���� 128.0.0.1 ~ 191.255.255.254
						if((128 <= i && i <= 191) && (l == 0 || l == 255)) continue;
						// C class ���� 192.0.0.1 ~ 223.255.255.254
						if((192 <= i && i <= 223) && (l == 0 || l == 255)) continue;
						// D class ���� 224.0.0.0 ~ 239.255.255.255
						// E class ���� 240.0.0.0 ~ 254.255.255.254
						if((240 <= i && i <= 254) && (l == 255)) continue;
							
						addUrl = i + "." + j + "." + k + "." + l;
						try {
							Crawler.crawlDataAndSaveIt(jsonData, agentFile, proxyFile, addUrl, crawledTime);
							// 5~8�� ����
							Thread.sleep((random.nextInt(3) + 5) * 1000);
						} catch (Exception e) {
							FileRelatedMethods.writeIpFlag(jsonData.getBasicStoragePath() + jsonData.getJsonFilePath() + jsonData.getIpFlagFile(), addUrl);
							e.printStackTrace();
							System.out.println("Url is nothing!!! I'm gonna continue~ ===== That Url is " + addUrl );
							
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String nowTime = sdf.format(new Date()); // ���� ���� �ð�
							System.out.println("Err :: EndTime" +  nowTime);
							
							break A;
						}
					}
				}
			}
		}
		System.out.println("End callALLClassURL =====");
	}
}
