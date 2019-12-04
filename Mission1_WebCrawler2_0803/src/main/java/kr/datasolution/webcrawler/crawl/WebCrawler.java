package kr.datasolution.webcrawler.crawl;

import java.util.ArrayList;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import kr.datasolution.webcrawler.json.JsonData;

public class WebCrawler {
	public static ArrayList<CrawledData> addCrawledDataToList(JsonData jsonInfo) throws Exception {
		
		@SuppressWarnings("resource")
		WebClient webClient = new WebClient();
		
		ArrayList<CrawledData> datas = new ArrayList<CrawledData>();
		
		String title = null, name = null, date = null, content = null;
		String operator_notice = null;
		String noticeAdd_url = null; // notice url�� �߰� ����
		String nextPageAdd_url = null; // next page url�� �߰� ����
		int index_of_year = 0; // �⵵�� �ش��ϴ� ��ġ
	
		DomNode nextPage = null;
		HtmlPage dataDoc = null;
		
		// ������ url
		String url = jsonInfo.getUrl_addr();

		// �ش� ����Ʈ�� DOM��ü ��������
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
		
		// ���� �������� �ش��ϴ� DOM��ü ã��; ���������� url�� �ڹٽ�ũ��Ʈ �ڵ�� �̷���� ���
		DomNodeList<DomNode> nextPages = null;
		
		// ������������ ���� selector ������ �ִ� ��� �Ҵ�
		/*if(!jsonInfo.getNextpages_tag().equals("")) {
			nextPages = noticeDoc.querySelectorAll(jsonInfo.getNextpages_tag());
		}
		*/
		// ������ ������ �� ��ŭ �ݺ�
		for (int i = jsonInfo.getPage_start_no(); i <= jsonInfo.getPage_count() + jsonInfo.getPage_start_no() - 1; i++) {
			// 2��° ������ ����~
			if (i != jsonInfo.getPage_start_no()) {
				/*// ���� �������� ���� url ���� ���� ��
				if (nextPages != null) {
					nextPage = nextPages.get(i - 1);
					nextPageAdd_url = nextPage.getAttributes().getNamedItem("href").getNodeValue();
					
					if(nextPage.asText().equals("1")) { jsonInfo.setPage_count(jsonInfo.getPage_count() + 1); continue; } // ���� �������� 1������ ��ũ�� ��� ���� �� ����ó��
					
					if(nextPageAdd_url.charAt(0) == '/') { nextPageAdd_url = nextPageAdd_url.substring(1); } // �� �տ� /�� ���� �� ����, url�� �������� /�� �ٱ⿡					
				} else {
					// ���� �������� ���� ������ �ڹٽ�ũ��Ʈ �ڵ��� ��
					// �Ķ���͸� �����Ͽ� url�� ����
					nextPageAdd_url = jsonInfo.getNext_url_add_info() + (i + 1);
				}*/
				
				
				// ���� �������� DOM��ü ��������
				//noticeDoc = webClient.getPage(temp_url + nextPageAdd_url);
				System.out.println(noticeUrl + jsonInfo.getNextpage_param() + i);
				noticeDoc = webClient.getPage(noticeUrl + jsonInfo.getNextpage_param() + i);
				// �Խñۿ� �ش��ϴ� DOM��ü ã��
				notices = noticeDoc.querySelectorAll(jsonInfo.getNotices_tag());
			}
			
			// �ش� �������� �Խù� �о���̱�
			for (DomNode notice : notices) {
				if (!notice.getAttributes().getNamedItem("href").getNodeValue().equals("#")) {
					title = null; name = null; date = null; content = null; // �ʱ�ȭ
					
					noticeAdd_url = notice.getAttributes().getNamedItem("href").getNodeValue();
					
					if(noticeAdd_url.charAt(0) == '/') { noticeAdd_url = noticeAdd_url.substring(1); } // �� �տ� /�� ���� �� ����, url�� �������� /�� �ٱ⿡
					if(noticeAdd_url.substring(0, 4).equals("http")) { continue; } // �Ķ���� ������ �ƴ� ������ url������ ��� ��ŵ
					
					System.out.println(temp_url + jsonInfo.getUrl_add_info() + noticeAdd_url);
					dataDoc = webClient.getPage(temp_url + jsonInfo.getUrl_add_info() + noticeAdd_url);
					
					// ��� �Խ��� ���ϱ�
					operator_notice = dataDoc.querySelector(jsonInfo.getOperator_feature_tag() + ", " + jsonInfo.getName_tag()).asText();
					if(operator_notice.equals(jsonInfo.getOperator_feature_text())) { continue;}
					
					title = dataDoc.querySelector((jsonInfo.getTitle_tag())).asText();
					//if(title.equals("")) { continue; } // ���� ���� ���� ��ŵ
					
					name = dataDoc.querySelector(jsonInfo.getName_tag() + ", " + jsonInfo.getName_img_tag() + ", " + jsonInfo.getName_tag2() + ", " + jsonInfo.getName_img_tag2()).asText();
					
					if(name.equals("")) {name = dataDoc.querySelector(jsonInfo.getName_img_tag() + ", " + jsonInfo.getName_img_tag2()).getAttributes().getNamedItem("alt").getNodeValue();}
					//if(name.equals("")) { continue; } // ������ ��ŵ
					
					content = dataDoc.querySelector(jsonInfo.getContent_tag()).asText();
					//if(content.equals("")) { continue; } // ������ ��ŵ					
					
					index_of_year = dataDoc.querySelector(jsonInfo.getDate_tag()).asText().indexOf(jsonInfo.getCurrent_year());
					
					if (index_of_year < 0) { continue; } // out of bound�� ���� ����; �ش�⵵�� ���� ���

					date = dataDoc.querySelector(jsonInfo.getDate_tag()).asText().substring(index_of_year, index_of_year + jsonInfo.getDate_length());
					
					System.out.println(title + "===================" + name);
					datas.add(new CrawledData(title, content, name, date)); // ������ ���
				}

			}
		}

		return datas;
	}
}
