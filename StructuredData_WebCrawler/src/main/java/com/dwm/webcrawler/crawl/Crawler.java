package com.dwm.webcrawler.crawl;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.dwm.webcrawler.db.DAO;
import com.dwm.webcrawler.db.DbRelatedDataInputer;
import com.dwm.webcrawler.db.DbTableData;
import com.dwm.webcrawler.json.JsonData;
import com.dwm.webcrawler.write.FileOutputer;
import com.dwm.webcrawler.write.FileRelatedMethods;

public class Crawler {
	public static void crawlDataAndSaveIt(JsonData jsonData, String agentFile, String proxyFile, String addUrl, String crawledTime) throws Exception {
		System.out.println("Start crawlDataAndSaveIt ===== Url is " + addUrl);
		int codeIndex = 1; // 두번째 a태그를 통해 국가코드 알 수 있기에, 인덱스번호 1
		
		List<String> agentList = getAgentList(agentFile);
		List<String> proxyList = getProxyList(proxyFile);
		Random random = new Random();
		
		String[] proxyAddr = proxyList.get(random.nextInt(proxyList.size())).split(":");
		String proxyHost = proxyAddr[0]; // proxy host
		int proxyPort = Integer.parseInt(proxyAddr[1]); // proxy port
 
        /*Properties systemProperties = System.getProperties();
        systemProperties.put("proxySet", "true");
        systemProperties.setProperty("https.proxyHost", proxyHost);
        systemProperties.setProperty("https.proxyPort", proxyPort + "");		
        systemProperties.setProperty("Content-Type", "text/plain; charset=utf-8");
        systemProperties.setProperty("Expect", "100-continue");*/
		
		System.setProperty("https.proxyHost", proxyHost);
		System.setProperty("https.proxyPort", proxyPort + "");
		System.setProperty("Content-Type", "text/plain; charset=utf-8");
		System.setProperty("Expect", "100-continue");
        
		String url = jsonData.getWebpage() + addUrl;
		
		Response response = Jsoup.connect(url).response();
		Map<String,String> cookies = new HashMap<String, String>();
		cookies = response.cookies();

		String postStr = Jsoup.connect(url)
						.userAgent(agentList.get(random.nextInt(agentList.size()-1)))
						.timeout(jsonData.getConnectTimeout())
						//.proxy(proxyHost, proxyPort)
						.cookies(cookies)
						//.header("Content-Language", "en-US")
						.get().html();
		
		//Document post = response.parse();
		Document post = Jsoup.parse(postStr);
		
		// 데이터를 수집하여 HashMap에 할당
		HashMap<String, String> dataDic = getCrawlDataInMap(post.select(jsonData.getData().getCountry()), post.select(jsonData.getData().getState()));
		
		// 요청 초과로 웹사이트에 접속이 불가할 경우 예외처리
		if(post.text().contains("Query limit exceeded") || post.text().contains("Please try again in a few hours"))
			throw new Exception("I'm throwing error for stopping bacause of Query limit exceeded ===== +++++ =====");
		
		// url 정보가 하나밖에 없으면 데이터가 없는 것을 간주; 따라서 탈출
		if (post.select(jsonData.getData().getDistrict()).size() < 2) {
			System.out.println("No Data Url~ +++++ ===== +++++");
			System.out.println("End crawlDataAndSaveIt ===== Url is " + addUrl);
			return;
		}
		
		// 국가명으로 감싸진 a태그에서 url정보를 기반으로 국가코드 알아낸다
		dataDic.put("code", post.select(jsonData.getData().getDistrict()).get(codeIndex).attr("href").substring(9)); // 국가코드 할당
		
		// HashMap 객체를 CrawlData VO에 데이터 적재
		CrawlData crawlData = convertMapToCrawlDataVO(dataDic, new CrawlData("noData","noData","noData","noData","noData","noData","noData",
				"noData","noData","noData","noData","noData","noData","noData","noData","noData","noData", "XX", addUrl));
		
		// 국가코드에 따라 json파일 쓰기
		String jsonFileName = FileOutputer.writeJsonFileInDir(crawlData, jsonData);
		// json 파일의 경로
		String realJsonFilePath = jsonData.getBasicStoragePath() + jsonData.getJsonFilePath() + jsonFileName + "." + jsonData.getFileExt();
		
		// 방금 작성한 파일에 10000개의 element가 있으면 
		if(FileRelatedMethods.IsJsonData10000(realJsonFilePath)) {
			// dbTableData객체에 필요한 데이터 저장
			DbTableData dbTableData = DbRelatedDataInputer.addToDbTableData(crawlData, jsonData, crawledTime, url, jsonFileName);
			
			// 경로로 완료된 json 파일 이동
			FileOutputer.moveCompleteJsonFileInDir(dbTableData, crawlData, jsonData, realJsonFilePath);
			
			// 경로에 메타파일 저장
			FileOutputer.writeMetaFileInDir(dbTableData);
			
			// DB 저장
			DAO.addCrawlDataToDB(dbTableData, jsonData);
			
		}

		System.out.println("End crawlDataAndSaveIt ===== Url is " + addUrl);
	}
	
	// agent 파일 리스트를 리턴하는 메소드
	public static List<String> getAgentList(String filePath) throws Exception {
		return FileUtils.readLines(new File(filePath), "UTF-8");
	}
	
	// proxy 파일 리스트를 리턴하는 메소드
	public static List<String> getProxyList(String filePath) throws Exception {
		return FileUtils.readLines(new File(filePath), "UTF-8");
	}
	
	// 수집한 데이터를 HashMap에 할당하여 반환
	public static HashMap<String, String> getCrawlDataInMap(Elements keyElements, Elements valueElements) {
		HashMap<String, String> dataDic = new HashMap<String, String>();
		
		for (int i = 0;i < keyElements.size();i++) {
			if(keyElements.get(i).text().equalsIgnoreCase("Local time")) continue;
			int slashIndex = keyElements.get(i).text().length();
			if(keyElements.get(i).text().contains(" /")) slashIndex = keyElements.get(i).text().indexOf(" /");
			else if(keyElements.get(i).text().contains(" ")) slashIndex = keyElements.get(i).text().indexOf(" ");
			
			dataDic.put(keyElements.get(i).text().substring(0, slashIndex).toLowerCase(), valueElements.get(i).text());
		}
		return dataDic;
	}
	
	// HashMap에 담긴 데이터를 기반으로 CrawlData 빈을 할당하는 메소드
	public static CrawlData convertMapToCrawlDataVO(HashMap<String, String> dataDic, CrawlData crawlData) throws Exception {
        String keyAttribute = null;
        String setMethodString = "set";
        String methodString = null;
        Iterator<String> itr = dataDic.keySet().iterator();
        
        while(itr.hasNext()){
            keyAttribute = itr.next();
            methodString = setMethodString+keyAttribute.substring(0,1).toUpperCase()+keyAttribute.substring(1);            
            Method[] methods = crawlData.getClass().getDeclaredMethods();
            
            for(int i = 0;i < methods.length;i++){
                if(methodString.equals(methods[i].getName())){
                      methods[i].invoke(crawlData, dataDic.get(keyAttribute));
                }
            }
        }
        return crawlData;
    }
}
