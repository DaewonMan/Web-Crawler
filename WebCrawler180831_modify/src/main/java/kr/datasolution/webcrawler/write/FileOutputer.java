package kr.datasolution.webcrawler.write;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import kr.datasolution.webcrawler.crawl.CrawlData;
import kr.datasolution.webcrawler.db.DbData;
import kr.datasolution.webcrawler.db.DbRelatedDataInputer;

public class FileOutputer {
	// ������ο� ��Ÿ���ϰ� ����Ÿ������ �������ִ� �޼ҵ�
	public static void writeFileInDir(DbData dbData) throws Exception {
		System.out.println("Start writeFileInDir =====");
		// Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		File f = null;

		// ���丮 ���� �˻�
		f = new File(dbData.getFilePath());
		if (!f.exists())
			FileUtils.forceMkdir(f);

		// DID�� 71�� �ƴѰ�츸 ���ϸ����
		if (Integer.parseInt(dbData.getDid().toString()) != 71) {
			// ���������� ���Ϸ� �ۼ�
			FileUtils.writeStringToFile(
					new File(dbData.getFilePath() + "/" + dbData.getSavFileName() + "." + dbData.getFileExt()),
					gson.toJson(new CrawlData(dbData.getTitle(), dbData.getCreated(), dbData.getContent())), "UTF-8");
		}
		// ��Ÿ���� ����
		FileUtils.writeStringToFile(new File(dbData.getFilePath() + "/" + dbData.getSavFileName() + ".meta"),
				gson.toJson(DbRelatedDataInputer.makeDbMetaData(dbData)), "UTF-8");

		System.out.println("End writeFileInDir =====");
	}

	// Web���� pdf���Ͽ� �ش��ϴ� ��ũ�� �о �����ο� �������ִ� �޼ҵ�
	public static void writeWebFileInDir(String url, DbData dbData) throws Exception {
		int responseCode = 0;
		URL obj = new URL(url);
		HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();

		conn.setRequestMethod("GET");
		conn.setDoOutput(true); // ������ ����
		conn.setDoInput(true); // �б��� ����
		conn.setUseCaches(false); // ĳ�̵����͸� ������ �ȹ�����
		conn.setDefaultUseCaches(false); // ĳ�̵����� ����Ʈ �� ����
		conn.setRequestProperty("Cache-Control", "no-cache");

		responseCode = conn.getResponseCode();
		// System.out.println("\nSending 'GET' request to URL : " + url);
		// System.out.println("Response Code : " + responseCode);

		InputStream inputStream = conn.getInputStream();

		File f = new File(dbData.getFilePath());
		if (!f.exists())
			FileUtils.forceMkdir(f);

		FileOutputStream outputStream = new FileOutputStream(
				dbData.getFilePath() + "/" + dbData.getSavFileName() + "." + dbData.getFileExt());

		int bytesRead = -1;
		byte[] buffer = new byte[4096];

		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}

		if (responseCode == 200) {
			outputStream.close();
			inputStream.close();
		}
	}
}
