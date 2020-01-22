package com.dwm.webcrawler.file;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.dwm.webcrawler.crawl.CrawledData;

public class TextWriter {
	public static void writeDataToText(ArrayList<CrawledData> datas, String storage_path) throws Exception {
		OutputStream os = new FileOutputStream(storage_path, true);
		OutputStreamWriter osw = new OutputStreamWriter(os);
		BufferedWriter bw = new BufferedWriter(osw);
		
		for (CrawledData data : datas) {
			bw.append("^title:" + data.getTitle());
			bw.newLine();
			bw.flush();
			
			bw.append("^date:" + data.getDate());
			bw.newLine();
			bw.flush();
			
			bw.append("^name:" + data.getName());
			bw.newLine();
			bw.flush();
			
			bw.append("^content:" + data.getContent().replace("\r\n", ""));
			bw.newLine();
			bw.flush();
		}
		
		bw.close();
	}
}
