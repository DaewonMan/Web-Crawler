package com.dwm.webcrawler.crawl;

import java.util.ArrayList;

import com.dwm.webcrawler.json.JsonData;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class WebCrawler {
	public static ArrayList<CrawledData> addCrawledDataToList(JsonData jsonInfo) throws Exception {
		System.out.println("-- start addCrawledDataToList");
		@SuppressWarnings("resource")
		WebClient webClient = new WebClient();
		
		// 에러메세지, 자바스크립트, css 사용안함
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		
		HtmlPage dataDoc = null;
		
		ArrayList<CrawledData> datas = new ArrayList<CrawledData>();
		
		String title = null, name = null, date = null, content = null;
		String isSelector = null;
		String operator_notice = null;
		String noticeAdd_url = null; // notice url의 추가 정보
		int index_of_year = 0; // 년도에 해당하는 위치
		
		// 수집할 url
		String url = jsonInfo.getUrl_addr();

		// main page의 html document 가져오기 
		HtmlPage main_page = webClient.getPage(url);
		
		// 게시판에 해당하는 태그 가져오기
		DomNode menu = main_page.querySelector(jsonInfo.getNotice_tag());
		
		// 게시판의 url 가져오기
		String noticeUrl = menu.getAttributes().getNamedItem("href").getNodeValue();
		
		// '//'의 위치 찾기; 게시판 태그 속성에서 url이 파라미터로 설정되어 있는 경우가 있기에
		int url_indexOfDS = url.indexOf("//");
		int nUrl_indexOfDS = noticeUrl.indexOf("//");
		String temp_url = null;
		
		// //가 없다는 경우, 파라미터만 있는 형식이다
		if(nUrl_indexOfDS < 0) {
			temp_url = url; // /board/view/~ 이런 형태이므로 기존 url 할당
			
			if(noticeUrl.charAt(0) == '/') { noticeUrl = noticeUrl.substring(1); } // 맨 앞에 /가 있을 시 제거, url에 마지막에 /가 붙기에
			
			noticeUrl = temp_url + noticeUrl; // main url에 파라미터를 붙여 게시판 url을 만든다
		} else {
			// main url과 notice url의 앞 url형식이 다를 시 notice url을 할당
			temp_url = url.substring(url_indexOfDS + 2, url_indexOfDS + 6).equals(noticeUrl.substring(nUrl_indexOfDS + 2, nUrl_indexOfDS + 6)) ? url : noticeUrl;
		}
		
		// 페이지 이동시 사용할 url; 파라메터 전까지의 url
		temp_url = temp_url.substring(0, temp_url.indexOf('/', 9) + 1);
		
		// 게시판 페이지의 DOM객체 가져오기
		HtmlPage noticeDoc = webClient.getPage(noticeUrl);

		// 게시글에 해당하는 태그들 할당
		DomNodeList<DomNode> notices = noticeDoc.querySelectorAll(jsonInfo.getNotices_tag());
		
		// 지정한 페이지 수 만큼 반복
		for (int i = jsonInfo.getPage_start_no(); i <= jsonInfo.getPage_count() + jsonInfo.getPage_start_no() - 1; i++) {
			// 2번째 페이지 부터~
			if (i != jsonInfo.getPage_start_no()) {
				// 다음 페이지의 DOM객체 가져오기
				System.out.println(noticeUrl + jsonInfo.getNextpage_param() + i);
				noticeDoc = webClient.getPage(noticeUrl + jsonInfo.getNextpage_param() + i);
				
				// 게시글에 해당하는 DOM객체 찾기
				notices = noticeDoc.querySelectorAll(jsonInfo.getNotices_tag());
			}
			
			// 해당 페이지의 게시물 읽어들이기
			for (DomNode notice : notices) {
				if (!notice.getAttributes().getNamedItem("href").getNodeValue().equals("#")) {
					title = null; name = null; date = null; content = null; // 초기화
					isSelector = null;
					
					noticeAdd_url = notice.getAttributes().getNamedItem("href").getNodeValue();
					
					if(noticeAdd_url.charAt(0) == '/') { noticeAdd_url = noticeAdd_url.substring(1); } // 맨 앞에 /가 있을 시 제거, url에 마지막에 /가 붙기에
					if(noticeAdd_url.substring(0, 4).equals("http")) { continue; } // 파라미터 형식이 아닌 완전한 url형식일 경우 스킵
					
					System.out.println(temp_url + jsonInfo.getUrl_add_info() + noticeAdd_url);
					dataDoc = webClient.getPage(temp_url + jsonInfo.getUrl_add_info() + noticeAdd_url);
					
					// 운영자 게시판 피하기
					isSelector = jsonInfo.getOperator_feature_tag() + ", " + jsonInfo.getName_tag();
					if(!jsonInfo.getName_img_tag().equals("")) { isSelector += ", " + jsonInfo.getName_img_tag(); }
					if(!jsonInfo.getName_tag2().equals("")) { isSelector += ", " + jsonInfo.getName_tag2(); }
					if(!jsonInfo.getName_img_tag2().equals("")) { isSelector += ", " + jsonInfo.getName_img_tag2(); }
					
					//System.out.println(dataDoc.querySelector("#dgn_content_de > div.re_gall_top_1 > div.w_top_left > dl:nth-child(2) > dd:nth-child(2) > span > span"));
					if(dataDoc.querySelector(isSelector) == null) { continue ;}
					operator_notice = dataDoc.querySelector(isSelector).asText();
					
					if(operator_notice.equals(jsonInfo.getOperator_feature_text())) { continue;}
					
					title = dataDoc.querySelector((jsonInfo.getTitle_tag())).asText();
					
					// name에 해당하는 selector 담기
					isSelector = jsonInfo.getName_tag();
					// selector가 있는 경우 할당
					if(!jsonInfo.getName_img_tag().equals("")) { isSelector += ", " + jsonInfo.getName_img_tag(); }
					if(!jsonInfo.getName_tag2().equals("")) { isSelector += ", " + jsonInfo.getName_tag2(); }
					if(!jsonInfo.getName_img_tag2().equals("")) { isSelector += ", " + jsonInfo.getName_img_tag2(); }
					
					name = dataDoc.querySelector(isSelector).asText();
				
					// name 값을 못 받을 경우; 이미지 태그의 값을 받는다.	
					if(name.equals("")) { isSelector = jsonInfo.getName_img_tag(); if(!jsonInfo.getName_img_tag2().equals("")) { isSelector += ", " + jsonInfo.getName_img_tag2(); } name = dataDoc.querySelector(isSelector).getAttributes().getNamedItem("alt").getNodeValue();}
					
					content = dataDoc.querySelector(jsonInfo.getContent_tag()).asText();
					
					index_of_year = dataDoc.querySelector(jsonInfo.getDate_tag()).asText().indexOf(jsonInfo.getCurrent_year());
					
					if (index_of_year < 0) { continue; } // out of bound를 막기 위함; 해당년도가 없는 경우

					date = dataDoc.querySelector(jsonInfo.getDate_tag()).asText().substring(index_of_year, index_of_year + jsonInfo.getDate_length());
					
					datas.add(new CrawledData(title, content, name, date)); // 데이터 담기
					
				}

			}
		}
		
		System.out.println("-- end addCrawledDataToList");
		return datas;
	}
}
