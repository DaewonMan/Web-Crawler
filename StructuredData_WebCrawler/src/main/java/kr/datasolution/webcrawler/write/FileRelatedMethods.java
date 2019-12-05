package kr.datasolution.webcrawler.write;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import kr.datasolution.webcrawler.crawl.CrawlData;
import kr.datasolution.webcrawler.json.JsonData;

public class FileRelatedMethods {
	// flag ������ �����ڵ带 ��� �޼ҵ�
	public static int getCountryCodeNum(CrawlData crawlData, JsonData jsonData, String filePath) throws Exception {
		int basicFileNum = 1;
		String[] elements = null;
		List<String> cCodes = FileUtils.readLines(new File(jsonData.getBasicStoragePath() + jsonData.getJsonFilePath()+ jsonData.getCountryCodeFlagFile()),
				"UTF-8"); 
		if(cCodes.size() > 0) {
			for (String cCode : cCodes) {
				elements = cCode.split("=");
				// �����ڵ��� �ε��� ��ȣ
				if(elements[0].equalsIgnoreCase(crawlData.getCode()))
					basicFileNum = Integer.parseInt(elements[1]);
			}
	
		}
		return basicFileNum;
	}
	
	// �ش� ������ ��� ������ ��ĵ�Ͽ� �����ڵ�� ��ġ�ϴ� json file���� �������� ���� �ε��� ���� �����ϴ� �޼ҵ�
	public static int getJsonFileIndex(CrawlData crawlData, String path) {
		File dir = new File(path);
		File[] fileArray = dir.listFiles();
		List<String> fileList = new ArrayList<String>();
		int basicFileNum = 1, tempNum = 0;;

		for (int i = 0; i < fileArray.length; i++) {
			File file = fileArray[i];

			if (file.isFile() && !file.getName().contains("flag"))
				fileList.add(file.getName());
		}

		for (String file : fileList) {
			if (file.substring(0, 2).equalsIgnoreCase(crawlData.getCode())) {
				tempNum = Integer
						.parseInt((String) file.subSequence(file.indexOf("JSON_") + "JSON_".length(), file.indexOf(".json")));
				// ���� �ε��� ��ȣ�� ���� ū ���� �Ҵ�
				if(basicFileNum < tempNum) {
					basicFileNum = tempNum;
				}
				
			}
			// System.out.println(file.subSequence(file.indexOf("JSON_")+5,
			// file.indexOf(".json")));
		}

		return basicFileNum;
	}

	// �ܺο� json������ 10000���̻� �ִ��� Ȯ���ϴ� �޼ҵ�
	public static boolean IsJsonData10000(String jsonFile) throws Exception {
		JSONParser jp = new JSONParser();
		JSONArray ja = (JSONArray) jp.parse(new InputStreamReader(new FileInputStream(jsonFile)));
		int size = ja.size();
		
		return size >= 10000 ? true : false;
	}
	// file�� �ִ��� ���θ� �������ִ� �޼ҵ�
	public static boolean checkExistFile(String filePath) throws Exception {
		if (new File(filePath).exists()) return true;
		else return false;
		/*int len = FileUtils.readFileToString(new File(filePath), "UTF-8").length();
		if(len >= 1) {
			return true;
		} else {
			return false;
		}*/
	}
	
	// ip_flag�� ip�ּҸ� ���� �޼ҵ�
	@SuppressWarnings("deprecation")
	public static void writeIpFlag(String filePath, String ip) throws Exception {
		FileUtils.write(new File(filePath), ip);
	}
	
	// ip_flag�� ������ �������� �޼ҵ�
	public static String getIpFlag(String filePath) throws Exception {
		return FileUtils.readFileToString(new File(filePath), "UTF-8");	
	}

	// cid_flag�� �ʱ�ȭ ��Ű�� �޼ҵ�
	@SuppressWarnings("deprecation")
	public static void initializeCidFlag(String filePath, boolean firstStart) throws Exception {
		String num = FileUtils.readFileToString(new File(filePath));
		if (num.equalsIgnoreCase(""))
			FileUtils.write(new File(filePath), "1");
		else if (firstStart)
			FileUtils.write(new File(filePath), "1");
	}

	// �ܺο� Directory�� �ִ��� �˻��Ͽ� �������ִ� �޼ҵ�
	public static void generateDir(String path) throws Exception {
		File f = new File(path);
		if (!f.exists())
			FileUtils.forceMkdir(f);
	}

	// �ܺο� file�� �ִ��� �˻��Ͽ� �������ִ� �޼ҵ�
	// FileOutputer.generateFile("�����ִ�����/j.json");
	public static void generateFile(String filePath) throws Exception {
		File f = new File(filePath);
		if (!f.exists())
			f.createNewFile();
	}
	
	public static String getCidAndPlus(String filePath) throws Exception {
		String num = FileUtils.readFileToString(new File(filePath), "UTF-8");
		FileUtils.write(new File(filePath), (Integer.parseInt(num) + 1) + "", "UTF-8");
		return num;
	}

}
