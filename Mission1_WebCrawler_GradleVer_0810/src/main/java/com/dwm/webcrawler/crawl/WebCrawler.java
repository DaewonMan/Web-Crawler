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
		
		// �����޼���, �ڹٽ�ũ��Ʈ, css ������
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		
		HtmlPage dataDoc = null;
		
		ArrayList<CrawledData> datas = new ArrayList<CrawledData>();
		
		String title = null, name = null, date = null, content = null;
		String isSelector = null;
		String operator_notice = null;
		String noticeAdd_url = null; // notice url�� �߰� ����
		int index_of_year = 0; // �⵵�� �ش��ϴ� ��ġ
		
		// ������ url
		String url = jsonInfo.getUrl_addr();

		// main page�� html document �������� 
		HtmlPage main_page = webClient.getPage(url);
		
		// �Խ��ǿ� �ش��ϴ� �±� ��������
		DomNode menu = main_page.querySelector(jsonInfo.getNotice_tag());
		
		// �Խ����� url ��������
		String noticeUrl = menu.getAttributes().getNamedItem("href").getNodeValue();
		
		// '//'�� ��ġ ã��; �Խ��� �±� �Ӽ����� url�� �Ķ���ͷ� �����Ǿ� �ִ� ��찡 �ֱ⿡
		int url_indexOfDS = url.indexOf("//");
		int nUrl_indexOfDS = noticeUrl.indexOf("//");
		String temp_url = null;
		
		// //�� ���ٴ� ���, �Ķ���͸� �ִ� �����̴�
		if(nUrl_indexOfDS < 0) {
			temp_url = url; // /board/view/~ �̷� �����̹Ƿ� ���� url �Ҵ�
			
			if(noticeUrl.charAt(0) == '/') { noticeUrl = noticeUrl.substring(1); } // �� �տ� /�� ���� �� ����, url�� �������� /�� �ٱ⿡
			
			noticeUrl = temp_url + noticeUrl; // main url�� �Ķ���͸� �ٿ� �Խ��� url�� �����
		} else {
			// main url�� notice url�� �� url������ �ٸ� �� notice url�� �Ҵ�
			temp_url = url.substring(url_indexOfDS + 2, url_indexOfDS + 6).equals(noticeUrl.substring(nUrl_indexOfDS + 2, nUrl_indexOfDS + 6)) ? url : noticeUrl;
		}
		
		// ������ �̵��� ����� url; �Ķ���� �������� url
		temp_url = temp_url.substring(0, temp_url.indexOf('/', 9) + 1);
		
		// �Խ��� �������� DOM��ü ��������
		HtmlPage noticeDoc = webClient.getPage(noticeUrl);

		// �Խñۿ� �ش��ϴ� �±׵� �Ҵ�
		DomNodeList<DomNode> notices = noticeDoc.querySelectorAll(jsonInfo.getNotices_tag());
		
		// ������ ������ �� ��ŭ �ݺ�
		for (int i = jsonInfo.getPage_start_no(); i <= jsonInfo.getPage_count() + jsonInfo.getPage_start_no() - 1; i++) {
			// 2��° ������ ����~
			if (i != jsonInfo.getPage_start_no()) {
				// ���� �������� DOM��ü ��������
				System.out.println(noticeUrl + jsonInfo.getNextpage_param() + i);
				noticeDoc = webClient.getPage(noticeUrl + jsonInfo.getNextpage_param() + i);
				
				// �Խñۿ� �ش��ϴ� DOM��ü ã��
				notices = noticeDoc.querySelectorAll(jsonInfo.getNotices_tag());
			}
			
			// �ش� �������� �Խù� �о���̱�
			for (DomNode notice : notices) {
				if (!notice.getAttributes().getNamedItem("href").getNodeValue().equals("#")) {
					title = null; name = null; date = null; content = null; // �ʱ�ȭ
					isSelector = null;
					
					noticeAdd_url = notice.getAttributes().getNamedItem("href").getNodeValue();
					
					if(noticeAdd_url.charAt(0) == '/') { noticeAdd_url = noticeAdd_url.substring(1); } // �� �տ� /�� ���� �� ����, url�� �������� /�� �ٱ⿡
					if(noticeAdd_url.substring(0, 4).equals("http")) { continue; } // �Ķ���� ������ �ƴ� ������ url������ ��� ��ŵ
					
					System.out.println(temp_url + jsonInfo.getUrl_add_info() + noticeAdd_url);
					dataDoc = webClient.getPage(temp_url + jsonInfo.getUrl_add_info() + noticeAdd_url);
					
					// ��� �Խ��� ���ϱ�
					isSelector = jsonInfo.getOperator_feature_tag() + ", " + jsonInfo.getName_tag();
					if(!jsonInfo.getName_img_tag().equals("")) { isSelector += ", " + jsonInfo.getName_img_tag(); }
					if(!jsonInfo.getName_tag2().equals("")) { isSelector += ", " + jsonInfo.getName_tag2(); }
					if(!jsonInfo.getName_img_tag2().equals("")) { isSelector += ", " + jsonInfo.getName_img_tag2(); }
					
					//System.out.println(dataDoc.querySelector("#dgn_content_de > div.re_gall_top_1 > div.w_top_left > dl:nth-child(2) > dd:nth-child(2) > span > span"));
					if(dataDoc.querySelector(isSelector) == null) { continue ;}
					operator_notice = dataDoc.querySelector(isSelector).asText();
					
					if(operator_notice.equals(jsonInfo.getOperator_feature_text())) { continue;}
					
					title = dataDoc.querySelector((jsonInfo.getTitle_tag())).asText();
					
					// name�� �ش��ϴ� selector ���
					isSelector = jsonInfo.getName_tag();
					// selector�� �ִ� ��� �Ҵ�
					if(!jsonInfo.getName_img_tag().equals("")) { isSelector += ", " + jsonInfo.getName_img_tag(); }
					if(!jsonInfo.getName_tag2().equals("")) { isSelector += ", " + jsonInfo.getName_tag2(); }
					if(!jsonInfo.getName_img_tag2().equals("")) { isSelector += ", " + jsonInfo.getName_img_tag2(); }
					
					name = dataDoc.querySelector(isSelector).asText();
				
					// name ���� �� ���� ���; �̹��� �±��� ���� �޴´�.	
					if(name.equals("")) { isSelector = jsonInfo.getName_img_tag(); if(!jsonInfo.getName_img_tag2().equals("")) { isSelector += ", " + jsonInfo.getName_img_tag2(); } name = dataDoc.querySelector(isSelector).getAttributes().getNamedItem("alt").getNodeValue();}
					
					content = dataDoc.querySelector(jsonInfo.getContent_tag()).asText();
					
					index_of_year = dataDoc.querySelector(jsonInfo.getDate_tag()).asText().indexOf(jsonInfo.getCurrent_year());
					
					if (index_of_year < 0) { continue; } // out of bound�� ���� ����; �ش�⵵�� ���� ���

					date = dataDoc.querySelector(jsonInfo.getDate_tag()).asText().substring(index_of_year, index_of_year + jsonInfo.getDate_length());
					
					datas.add(new CrawledData(title, content, name, date)); // ������ ���
					
				}

			}
		}
		
		System.out.println("-- end addCrawledDataToList");
		return datas;
	}
}
