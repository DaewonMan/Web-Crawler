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
	// 지정경로에 메타파일과 데이타파일을 저장해주는 메소드
	public static void writeFileInDir(DbData dbData) throws Exception {
		System.out.println("Start writeFileInDir =====");
		// Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		File f = null;

		// 디렉토리 존재 검사
		f = new File(dbData.getFilePath());
		if (!f.exists())
			FileUtils.forceMkdir(f);

		// DID가 71이 아닌경우만 파일만들기
		if (Integer.parseInt(dbData.getDid().toString()) != 71) {
			// 수집데이터 파일로 작성
			FileUtils.writeStringToFile(
					new File(dbData.getFilePath() + "/" + dbData.getSavFileName() + "." + dbData.getFileExt()),
					gson.toJson(new CrawlData(dbData.getTitle(), dbData.getCreated(), dbData.getContent())), "UTF-8");
		}
		// 메타파일 생성
		FileUtils.writeStringToFile(new File(dbData.getFilePath() + "/" + dbData.getSavFileName() + ".meta"),
				gson.toJson(DbRelatedDataInputer.makeDbMetaData(dbData)), "UTF-8");

		System.out.println("End writeFileInDir =====");
	}

	// Web에서 pdf파일에 해당하는 링크를 읽어서 저장경로에 저장해주는 메소드
	public static void writeWebFileInDir(String url, DbData dbData) throws Exception {
		int responseCode = 0;
		URL obj = new URL(url);
		HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();

		conn.setRequestMethod("GET");
		conn.setDoOutput(true); // 쓰기모드 지정
		conn.setDoInput(true); // 읽기모드 지정
		conn.setUseCaches(false); // 캐싱데이터를 받을지 안받을지
		conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정
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
