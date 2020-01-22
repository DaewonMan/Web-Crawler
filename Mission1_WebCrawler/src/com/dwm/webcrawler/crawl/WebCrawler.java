package com.dwm.webcrawler.crawl;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dwm.webcrawler.json.JsonData;

public class WebCrawler {
	public static ArrayList<CrawledData> addCrawledDataToList(JsonData jsonInfo) throws Exception {
		
		ArrayList<CrawledData> datas = new ArrayList<>();
		
		String title = null, name = null, date = null, content = null;
		String noticeAdd_url = null; // notice url의 추가 정보
		String nextPageAdd_url = null; // next page url의 추가 정보
		int index_of_year = 0; // 년도에 해당하는 위치
		
		Element nextPage = null; // 다음 페이지에 대한 객체
		Document dataDoc = null; // 본문 내용에 대한 DOM객체
		
		// 수집할 url
		String url = jsonInfo.getUrl_addr();

		// 해당 사이트의 DOM객체 가져오기
		Document doc = Jsoup.connect(url).get();

		// 게시판에 해당하는 태그 가져오기
		Elements menus = doc.select(jsonInfo.getNotice_tag());
		Element menu = menus.first();
		
		// 게시판의 url 가져오기
		String noticeUrl = menu.attr("href");
		
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
		Document noticeDoc = Jsoup.connect(noticeUrl).get();

		// 게시글에 해당하는 태그들 할당
		Elements notices = noticeDoc.select(jsonInfo.getNotices_tag());

		// 다음 페이지에 해당하는 DOM객체 찾기; 다음페이지 url이 자바스크립트 코드로 이루어진 경우
		Elements nextPages = null;
		if(!jsonInfo.getNextpages_tag().equals("")) {
			nextPages = noticeDoc.select(jsonInfo.getNextpages_tag());
		}
		
		for (int i = 0; i < jsonInfo.getPage_count(); i++) {
			// 2번째 페이지 부터~
			if (i != 0) {
				// 다음 페이지에 대한 url 정보 있을 시
				if (nextPages != null) {
					nextPage = nextPages.get(i - 1);
					nextPageAdd_url = nextPage.attr("href");
					
					if(nextPage.text().equals("1")) { jsonInfo.setPage_count(jsonInfo.getPage_count() + 1); continue; } // 다음 페이지를 1페이지 링크로 들어 갔을 시 예외처리
					
					if(nextPageAdd_url.charAt(0) == '/') { nextPageAdd_url = nextPageAdd_url.substring(1); } // 맨 앞에 /가 있을 시 제거, url에 마지막에 /가 붙기에					
				} else {
					// 다음 페이지에 대한 정보가 자바스크립트 코드일 때
					// 파라미터를 수정하여 url를 만듦
					nextPageAdd_url = jsonInfo.getNext_url_add_info() + (i + 1);
				}
				
				// 다음 페이지의 DOM객체 가져오기
				noticeDoc = Jsoup.connect(temp_url + nextPageAdd_url).get();
				// 게시글에 해당하는 DOM객체 찾기
				notices = noticeDoc.select(jsonInfo.getNotices_tag());
			}
			
			// 해당 페이지의 게시물 읽어들이기
			for (Element notice : notices) {
				if (!notice.attr("href").equals("#")) {
					noticeAdd_url = notice.attr("href");
					if(noticeAdd_url.charAt(0) == '/') { noticeAdd_url = noticeAdd_url.substring(1); } // 맨 앞에 /가 있을 시 제거, url에 마지막에 /가 붙기에
					
					dataDoc = Jsoup.connect(temp_url + jsonInfo.getUrl_add_info() + noticeAdd_url).get();

					title = dataDoc.select(jsonInfo.getTitle_tag()).text();
					name = dataDoc.select(jsonInfo.getName_tag()).text();
					
					if(name.equals("")) { name = dataDoc.select(jsonInfo.getName_img_tag()).attr("alt"); } // name에 이름이 없을 시 img태그로 찾는다.
					
					content = dataDoc.select(jsonInfo.getContent_tag()).text();
					index_of_year = dataDoc.select(jsonInfo.getDate_tag()).text().indexOf(jsonInfo.getCurrent_year()); // 날짜 태그가 없는 경우 해당년도를 기준으로 위치 찾는다.

					if (index_of_year < 0) { continue; } // out of bound를 막기 위함; 해당년도가 없는 경우

					date = dataDoc.select(jsonInfo.getDate_tag()).text().substring(index_of_year, index_of_year + jsonInfo.getDate_length()); // 등록일만 골라낸다.
					datas.add(new CrawledData(title, content, name, date)); // 데이터 담기

				}

			}
		}

		return datas;
	}
}
