package kr.datasolution.webcrawler.crawl;

public class CrawlData {
	private String title;
	private String date;
	private String content;
	private String crawledTime;
	private	String url;
	
	public CrawlData() {
		// TODO Auto-generated constructor stub
	}

	public CrawlData(String title, String date, String content, String crawledTime, String url) {
		super();
		this.title = title;
		this.date = date;
		this.content = content;
		this.crawledTime = crawledTime;
		this.url = url;
	}
	
	public CrawlData(String title, String date, String content) {
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCrawledTime() {
		return crawledTime;
	}

	public void setCrawledTime(String crawledTime) {
		this.crawledTime = crawledTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
