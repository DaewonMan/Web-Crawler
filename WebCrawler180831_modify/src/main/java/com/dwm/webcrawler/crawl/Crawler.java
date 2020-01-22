package com.dwm.webcrawler.crawl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
	public static void addCrawlDataToList(JsonData jsonData) throws Exception {
		System.out.println("Start addCrawlDataToList =====");

		//ArrayList<DbData> dbDatas = new ArrayList<DbData>();
		DbData dbData = null;
		CrawlData crawlData = null;
		CrawlDataTag crawlDataTag = jsonData.getData().get(0);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf70Us = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
		SimpleDateFormat sdf71Us = null;
		
		String crawledTime = sdf.format(new Date()); // �޼ҵ� ���� �ð�
		String title = "", date = "", content = null, contentUrl = "", webFileName = null, created = ""; // null�� �� ��ü�� �� �� �� �ֱ⿡
		String nextPageAddParam = ""; // next page parameter ����
		String noticeAddUrl = ""; // notice url�� �߰� ����
		String offsetNum = ""; // ������ �̵� �� ����ϴ� offset �Ķ���� ��ȣ ���� 
		String noticeAttr = ""; // �Խù��� �Ӽ���
		
		Document dataDoc = null; // ���� ���뿡 ���� DOM��ü

		// ������ url
		String url = jsonData.getWebpage();

		// �ش� ����Ʈ�� DOM��ü ��������
		Document doc = Jsoup.connect(url).get();
		
		Element menu = null;
		String postUrl = "";
		Document noticeDoc = null;
		// �Խñ��� �ִ� �������� �̵��ϱ� ���� �ݺ���
		for (CategoryTag catogoryTag : jsonData.getCategories()) {
			menu = doc.selectFirst(catogoryTag.getCategory());

			postUrl = menu.attr("href"); // �Խñ� ī�װ����� url�� �ش��ϴ� parameter ��������
			// �� �տ� /�� ���� �� ����
			if (postUrl.charAt(0) == '/') {
				postUrl = postUrl.substring(1);
			}

			postUrl = url + postUrl; // main url�� �Ķ���͸� �ٿ� �Խ��� url�� �����
			
			// �Խ��� �������� DOM��ü ��������
			noticeDoc = Jsoup.connect(postUrl).get();
		}

		// �Խñۿ� �ش��ϴ� �±׵� �Ҵ�
		Elements notices = noticeDoc.select(jsonData.getPost());

		// ������ ���������� �ݺ�
		for (int i = 0;i < 100; i++) {
			// offset �Ķ���� ��ȣ ����; ���������� �̵��� ����
			if(jsonData.getDid() == 70) { 
				offsetNum = (i + 1) + "";
				noticeAttr = "href";
			}
			else {
				// DID�� 71�� ��
				offsetNum = (i * 3) + "";
				noticeAttr = "data-href";
			} 
			
			// 2��° ������ ����~
			if (i != 0) {
				// ���� �������� url �Ķ���� �����
				nextPageAddParam = jsonData.getNextPageParam().get(0) + offsetNum; // ?offset=����

				// ���� �������� DOM��ü ��������
				noticeDoc = Jsoup.connect(postUrl + nextPageAddParam).get();

				// �Խñۿ� �ش��ϴ� DOM��ü ã��
				notices = noticeDoc.select(jsonData.getPost());
			}

			// ������ �������� Ż��
			if (notices.size() < 1) break;

			System.out.println(postUrl + nextPageAddParam);
			// �ش� �������� �Խù� �о���̱�
			for (Element notice : notices) {
				title = "";
				date = "";
				contentUrl = "";
				content = "";
				contentUrl = "";
				webFileName = "";
				created = "";

				if (!notice.attr(noticeAttr).equals("#")) {
					noticeAddUrl = notice.attr(noticeAttr);
					// �� �տ� /�� ���� �� ����, url�� �������� /�� �ٱ⿡
					if (noticeAddUrl.charAt(0) == '/') { noticeAddUrl = noticeAddUrl.substring(1); }
					System.out.println("121212121212");
					dataDoc = Jsoup.connect(url + noticeAddUrl).userAgent("Opera/9.21 (Windows NT 5.1; U; fr)").get();
					
					// ������ ���� �ܾ�� �����͵�
					title = dataDoc.select(crawlDataTag.getTitle()).text();
					date = dataDoc.select(crawlDataTag.getDate()).text();
					
					if (jsonData.getDid() == 70) {
						// DID�� 70�̸� content ������ �Ҵ�޴´�
						for (String tmp : crawlDataTag.getContent()) { content += dataDoc.select(tmp).text(); }
						// DID 70�� �°� ��¥ ��ȯ
						created = sdf.format(sdf70Us.parse(date));
					} else {
						// DID�� 71�̸� file name�� �Ҵ�޴´�
						webFileName = dataDoc.select(crawlDataTag.getContent().get(0)).text();						
						// DID 71�� �°� ��¥ ��ȯ
						
						//�ݵ�� �����ʿ�
						if(date.length() >= 10) {
							sdf71Us = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
						} else if(date.length() >= 7){
							sdf71Us = new SimpleDateFormat("yyyy-MM", Locale.US);
						} else if(date.length() == 4){
							sdf71Us = new SimpleDateFormat("yyyy", Locale.US);
						}
						created = sdf.format(sdf71Us.parse(date));
					}
					
					if (title.equalsIgnoreCase("") || date.equalsIgnoreCase("") || content.equalsIgnoreCase("")) continue;
					
					crawlData = new CrawlData(title, created, content, crawledTime, url + noticeAddUrl);
					
					dbData = DbRelatedDataInputer.addToDbData(crawlData, jsonData, webFileName); // dbData��ü�� �ʿ��� ������ ����
					
					// DID�� 71�̸� pdf�� ��ũ�� ���� �����͸� �о� ������ �����
					if(jsonData.getDid() == 71) {
						contentUrl = dataDoc.select(crawlDataTag.getContent().get(0)).attr("href"); // pdf�� ��ũ�� �Ҵ�
						// �� �տ� /�� ���� �� ����, url�� �������� /�� �ٱ⿡	
						if (contentUrl.charAt(0) == '/') { contentUrl = contentUrl.substring(1); }
						
						FileOutputer.writeWebFileInDir(url + contentUrl, dbData); // pdf ���� ��ο� �����  						
					}
					
					FileOutputer.writeFileInDir(dbData);
					
					DAO.addCrawlDataToDB(dbData);
					//dbDatas.add(dbData); // db�� ������ ������ ���
				}
			}
		}
		System.out.println("End addCrawlDataToList =====");
		//return dbDatas;
	}
}
