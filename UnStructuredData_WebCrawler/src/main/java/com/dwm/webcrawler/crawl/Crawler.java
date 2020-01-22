package com.dwm.webcrawler.crawl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dwm.webcrawler.db.DAO;
import com.dwm.webcrawler.db.DbData;
import com.dwm.webcrawler.db.DbRelatedDataInputer;
import com.dwm.webcrawler.json.JsonData;
import com.dwm.webcrawler.tag.CategoryTag;
import com.dwm.webcrawler.tag.CrawlDataTag;
import com.dwm.webcrawler.write.FileOutputer;

public class Crawler {
	public static void crawlDataAndSaveIt(JsonData jsonData, String agentFile) throws Exception {
		System.out.println("Start addCrawlDataToList =====");
		
		List<String> agentList = getAgentList(agentFile);
		Random random = new Random();
		
		DbData dbData = null;
		CrawlData crawlData = null;
		CrawlDataTag crawlDataTag = jsonData.getData().get(0);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf70Us = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
		SimpleDateFormat sdf71Us = null;
		
		String crawledTime = sdf.format(new Date()); // 메소드 시작 시간
		String title = "", date = "", content = null, contentUrl = "", webFileName = null, created = ""; // null로 둔 객체는 안 쓸 수 있기에
		String nextPageAddParam = ""; // next page parameter 정보
		String noticeAddUrl = ""; // notice url의 추가 정보
		String offsetNum = ""; // 페이지 이동 사 사용하는 offset 파라미터 번호 설정 
		String noticeAttr = ""; // 게시물의 속성명
		
		Document dataDoc = null; // 본문 내용에 대한 DOM객체

		// 수집할 url
		String url = jsonData.getWebpage();

		// 해당 사이트의 DOM객체 가져오기
		Document doc = Jsoup.connect(url).get();
		
		Element menu = null;
		String postUrl = "";
		Document noticeDoc = null;
		// 게시글이 있는 페이지로 이동하기 위한 반복문
		for (CategoryTag catogoryTag : jsonData.getCategories()) {
			menu = doc.selectFirst(catogoryTag.getCategory());

			postUrl = menu.attr("href"); // 게시글 카테고리에서 url에 해당하는 parameter 가져오기
			// 맨 앞에 /가 있을 시 제거
			if (postUrl.charAt(0) == '/') {
				postUrl = postUrl.substring(1);
			}

			postUrl = url + postUrl; // main url에 파라미터를 붙여 게시판 url을 만든다
			
			// 게시판 페이지의 DOM객체 가져오기
			noticeDoc = Jsoup.connect(postUrl).get();
		}

		// 게시글에 해당하는 태그들 할당
		Elements notices = noticeDoc.select(jsonData.getPost());

		// 마지막 페이지까지 반복
		for (int i = 0;; i++) {
			// offset 파라미터 번호 설정; 다음페이지 이동을 위함
			if(jsonData.getDid() == 70) { 
				offsetNum = (i + 1) + "";
				noticeAttr = "href";
			}
			else {
				// DID가 71일 때
				offsetNum = (i * 3) + "";
				noticeAttr = "data-href";
			} 
			
			// 2번째 페이지 부터~
			if (i != 0) {
				// 다음 페이지의 url 파라미터 만들기
				nextPageAddParam = jsonData.getNextPageParam().get(0) + offsetNum; // ?offset=숫자

				try {
					// 다음 페이지의 DOM객체 가져오기
					noticeDoc = Jsoup.connect(postUrl + nextPageAddParam).userAgent(agentList.get(random.nextInt(agentList.size()-1))).timeout(30000).get();					
				} catch (Exception e) {
					System.out.println("Connection Error!!! So, Sleep time 5 second~ ===== offsetNum is " + offsetNum);
					Thread.sleep(5000); // 에러발생 시, 5초 쉬어주고 다시 반복
					continue;
				}

				// 게시글에 해당하는 DOM객체 찾기
				notices = noticeDoc.select(jsonData.getPost());
			}

			// 마지막 페이지면 탈출
			if (notices.size() < 1) break;
			
			// 해당 페이지의 게시물 읽어들이기
			for (Element notice : notices) {
				title = "";
				date = "";
				contentUrl = "";
				content = null;
				contentUrl = "";
				webFileName = null;
				created = "";

				if (!notice.attr(noticeAttr).equals("#") && !notice.attr(noticeAttr).equalsIgnoreCase("") ) {
					noticeAddUrl = notice.attr(noticeAttr);
					
					// 맨 앞에 /가 있을 시 제거, url에 마지막에 /가 붙기에
					if (noticeAddUrl.charAt(0) == '/') { noticeAddUrl = noticeAddUrl.substring(1); }
					
					try {
						// 7초 안에 응답없으면 에러!!
						dataDoc = Jsoup.connect(url + noticeAddUrl).userAgent(agentList.get(random.nextInt(agentList.size()-1))).timeout(30000).get();						
					} catch (Exception e) {
						System.out.println("Connection Error!!! So, Sleep time 5 second~ ===== notice page parameter is " + noticeAddUrl );
						Thread.sleep(5000); // 에러발생 시, 5초 쉬어주고 다시 반복
						continue;
					}
					
					// 웹으로 부터 긁어온 데이터들
					title = dataDoc.select(crawlDataTag.getTitle()).text();
					date = dataDoc.select(crawlDataTag.getDate()).text();
					
					if (jsonData.getDid() == 70) {
						content = "";
						// DID가 70이면 content 내용을 할당받는다
						for (String tmp : crawlDataTag.getContent()) { content += dataDoc.select(tmp).text(); }
						// DID 70에 맞게 날짜 변환
						created = sdf.format(sdf70Us.parse(date));
					} else {
						// DID가 71이면 file name을 할당받는다
						webFileName = dataDoc.select(crawlDataTag.getContent().get(0)).text();						
						
						// DID 71에 맞게 날짜 변환
						//반드시 수정필요
						if(date.length() >= 10) {
							sdf71Us = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
						} else if(date.length() >= 7){
							sdf71Us = new SimpleDateFormat("yyyy-MM", Locale.US);
						} else if(date.length() == 4){
							sdf71Us = new SimpleDateFormat("yyyy", Locale.US);
						}
						created = sdf.format(sdf71Us.parse(date));
					}
					// 데이터가 누락된 경우 다음 게시글로
					if (content != null && (title.equalsIgnoreCase("") || date.equalsIgnoreCase("") || content.equalsIgnoreCase(""))) continue;
					
					crawlData = new CrawlData(title, created, content, crawledTime, url + noticeAddUrl);
					
					dbData = DbRelatedDataInputer.addToDbData(crawlData, jsonData, webFileName); // dbData객체에 필요한 데이터 저장
					
					// DID가 71이면 pdf의 링크를 통해 데이터를 읽어 파일을 써야함
					if(jsonData.getDid() == 71) {
						contentUrl = dataDoc.select(crawlDataTag.getContent().get(0)).attr("href"); // pdf의 링크를 할당
						
						// pdf링크 못 들어가는 경우
						if (contentUrl.equalsIgnoreCase("")) { continue; }
						// 맨 앞에 /가 있을 시 제거, url에 마지막에 /가 붙기에	
						if (contentUrl.charAt(0) == '/') { contentUrl = contentUrl.substring(1); }
						
						if(!(FileOutputer.writeWebFileInDir(url + contentUrl, dbData))) continue; // pdf 파일 경로에 만들기  						
					}
					
					FileOutputer.writeFileInDir(dbData);
					
					DAO.addCrawlDataToDB(dbData, jsonData);
				}
			}
		}
		System.out.println("End addCrawlDataToList =====");
	}
	
	public static List<String> getAgentList(String filePath) throws Exception {
		return FileUtils.readLines(new File(filePath), "UTF-8");
	}
}