package com.dwm.webcrawler.crawl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CrawledData {
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private	String title;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String content;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String name;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String date;
}
