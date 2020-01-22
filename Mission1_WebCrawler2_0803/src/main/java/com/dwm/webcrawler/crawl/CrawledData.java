package com.dwm.webcrawler.crawl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class CrawledData {
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private	String title;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String content;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String name;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String date;
	
	public CrawledData() {
		// TODO Auto-generated constructor stub
	}

	public CrawledData(String title, String content, String name, String date) {
		super();
		this.title = title;
		this.content = content;
		this.name = name;
		this.date = date;
	}
}
