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
		String noticeAdd_url = null; // notice url�� �߰� ����
		String nextPageAdd_url = null; // next page url�� �߰� ����
		int index_of_year = 0; // �⵵�� �ش��ϴ� ��ġ
		
		Element nextPage = null; // ���� �������� ���� ��ü
		Document dataDoc = null; // ���� ���뿡 ���� DOM��ü
		
		// ������ url
		String url = jsonInfo.getUrl_addr();

		// �ش� ����Ʈ�� DOM��ü ��������
		Document doc = Jsoup.connect(url).get();

		// �Խ��ǿ� �ش��ϴ� �±� ��������
		Elements menus = doc.select(jsonInfo.getNotice_tag());
		Element menu = menus.first();
		
		// �Խ����� url ��������
		String noticeUrl = menu.attr("href");
		
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
		Document noticeDoc = Jsoup.connect(noticeUrl).get();

		// �Խñۿ� �ش��ϴ� �±׵� �Ҵ�
		Elements notices = noticeDoc.select(jsonInfo.getNotices_tag());

		// ���� �������� �ش��ϴ� DOM��ü ã��; ���������� url�� �ڹٽ�ũ��Ʈ �ڵ�� �̷���� ���
		Elements nextPages = null;
		if(!jsonInfo.getNextpages_tag().equals("")) {
			nextPages = noticeDoc.select(jsonInfo.getNextpages_tag());
		}
		
		for (int i = 0; i < jsonInfo.getPage_count(); i++) {
			// 2��° ������ ����~
			if (i != 0) {
				// ���� �������� ���� url ���� ���� ��
				if (nextPages != null) {
					nextPage = nextPages.get(i - 1);
					nextPageAdd_url = nextPage.attr("href");
					
					if(nextPage.text().equals("1")) { jsonInfo.setPage_count(jsonInfo.getPage_count() + 1); continue; } // ���� �������� 1������ ��ũ�� ��� ���� �� ����ó��
					
					if(nextPageAdd_url.charAt(0) == '/') { nextPageAdd_url = nextPageAdd_url.substring(1); } // �� �տ� /�� ���� �� ����, url�� �������� /�� �ٱ⿡					
				} else {
					// ���� �������� ���� ������ �ڹٽ�ũ��Ʈ �ڵ��� ��
					// �Ķ���͸� �����Ͽ� url�� ����
					nextPageAdd_url = jsonInfo.getNext_url_add_info() + (i + 1);
				}
				
				// ���� �������� DOM��ü ��������
				noticeDoc = Jsoup.connect(temp_url + nextPageAdd_url).get();
				// �Խñۿ� �ش��ϴ� DOM��ü ã��
				notices = noticeDoc.select(jsonInfo.getNotices_tag());
			}
			
			// �ش� �������� �Խù� �о���̱�
			for (Element notice : notices) {
				if (!notice.attr("href").equals("#")) {
					noticeAdd_url = notice.attr("href");
					if(noticeAdd_url.charAt(0) == '/') { noticeAdd_url = noticeAdd_url.substring(1); } // �� �տ� /�� ���� �� ����, url�� �������� /�� �ٱ⿡
					
					dataDoc = Jsoup.connect(temp_url + jsonInfo.getUrl_add_info() + noticeAdd_url).get();

					title = dataDoc.select(jsonInfo.getTitle_tag()).text();
					name = dataDoc.select(jsonInfo.getName_tag()).text();
					
					if(name.equals("")) { name = dataDoc.select(jsonInfo.getName_img_tag()).attr("alt"); } // name�� �̸��� ���� �� img�±׷� ã�´�.
					
					content = dataDoc.select(jsonInfo.getContent_tag()).text();
					index_of_year = dataDoc.select(jsonInfo.getDate_tag()).text().indexOf(jsonInfo.getCurrent_year()); // ��¥ �±װ� ���� ��� �ش�⵵�� �������� ��ġ ã�´�.

					if (index_of_year < 0) { continue; } // out of bound�� ���� ����; �ش�⵵�� ���� ���

					date = dataDoc.select(jsonInfo.getDate_tag()).text().substring(index_of_year, index_of_year + jsonInfo.getDate_length()); // ����ϸ� ��󳽴�.
					datas.add(new CrawledData(title, content, name, date)); // ������ ���

				}

			}
		}

		return datas;
	}
}
