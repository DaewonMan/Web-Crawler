package com.dwm.webcrawler.write;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.FileUtils;

import com.dwm.webcrawler.db.DbData;
import com.dwm.webcrawler.db.DbRelatedDataInputer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
//					new File(dbData.getFilePath() + "/" + dbData.getSavFileName() + "." + dbData.getFileExt()),
//					gson.toJson(new CrawlData(dbData.getTitle(), dbData.getCreated(), dbData.getContent())), "UTF-8");
					new File(dbData.getFilePath() + "/" + dbData.getSavFileName() + "." + dbData.getFileExt()), dbData.getContent(), "UTF-8");
		}
		// ��Ÿ���� ����
		FileUtils.writeStringToFile(new File(dbData.getFilePath() + "/" + dbData.getSavFileName() + ".meta"),
				gson.toJson(DbRelatedDataInputer.makeDbMetaData(dbData)), "UTF-8");

		System.out.println("End writeFileInDir =====");
	}

	// Web���� pdf���Ͽ� �ش��ϴ� ��ũ�� �о �����ο� �������ִ� �޼ҵ�
	public static boolean writeWebFileInDir(String url, DbData dbData) {
		int responseCode = 0;
		boolean resultStatus = false;
		URL obj;

		try {
			obj = new URL(url);
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
				resultStatus = true;
			} else {
				resultStatus = false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 
		return resultStatus;
	}
}
