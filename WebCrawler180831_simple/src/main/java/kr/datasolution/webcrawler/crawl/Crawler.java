package kr.datasolution.webcrawler.crawl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import kr.datasolution.webcrawler.db.DbData;
import kr.datasolution.webcrawler.db.DbRelatedDataInputer;
import kr.datasolution.webcrawler.json.JsonData;
import kr.datasolution.webcrawler.tag.CategoryTag;
import kr.datasolution.webcrawler.tag.CrawlDataTag;
import kr.datasolution.webcrawler.write.FileOutputer;

public class Crawler {
	public static ArrayList<DbData> addCrawlDataToList(JsonData jsonData) throws Exception {
		System.out.println("Start addCrawlDataToList =====");

		ArrayList<DbData> dbDatas = new ArrayList<DbData>();
		DbData dbData = null;
		CrawlData crawlData = null;
		CrawlDataTag crawlDataTag = jsonData.getData().get(0);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf70Us = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
		SimpleDateFormat sdf71Us = new SimpleDateFormat("yyyy-MM", Locale.US);
		
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
		for (int i = 0; i < 2; i++) {
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

			// �ش� �������� �Խù� �о���̱�
			for (Element notice : notices) {
				title = "";
				date = "";
				contentUrl = "";

				if (!notice.attr(noticeAttr).equals("#")) {
					noticeAddUrl = notice.attr(noticeAttr);
					// �� �տ� /�� ���� �� ����, url�� �������� /�� �ٱ⿡
					if (noticeAddUrl.charAt(0) == '/') { noticeAddUrl = noticeAddUrl.substring(1); }

					dataDoc = Jsoup.connect(url + noticeAddUrl).get();
					
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
						created = sdf.format(sdf71Us.parse(date));
					}
					
					crawlData = new CrawlData(title, created, content, crawledTime, url + noticeAddUrl);
					
					dbData = DbRelatedDataInputer.addToDbData(crawlData, jsonData, webFileName); // dbData��ü�� �ʿ��� ������ ����
					
					// DID�� 71�̸� pdf�� ��ũ�� ���� �����͸� �о� ������ �����
					if(jsonData.getDid() == 71) {
						contentUrl = dataDoc.select(crawlDataTag.getContent().get(0)).attr("href"); // pdf�� ��ũ�� �Ҵ�
						// �� �տ� /�� ���� �� ����, url�� �������� /�� �ٱ⿡	
						if (contentUrl.charAt(0) == '/') { contentUrl = contentUrl.substring(1); }
						
						FileOutputer.writeWebFileInDir(url + contentUrl, dbData); // pdf ���� ��ο� �����  						
					}
					dbDatas.add(dbData); // db�� ������ ������ ���
				}
			}
		}
		System.out.println("End addCrawlDataToList =====");
		return dbDatas;
	}
}
