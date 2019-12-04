package kr.datasolution.webcrawler.tag;

import java.util.List;

public class CrawlDataTag {
	private String title;
	private String date;
	private List<String> content;
	
	public CrawlDataTag() {
		// TODO Auto-generated constructor stub
	}
	
	public CrawlDataTag(String title, String date, List<String> content) {
		super();
		this.title = title;
		this.date = date;
		this.content = content;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<String> getContent() {
		return content;
	}
	public void setContent(List<String> content) {
		this.content = content;
	}
	
	
}
