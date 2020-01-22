package com.dwm.webcrawler.json;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class JsonData {
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String url_addr;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String notice_tag;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String notices_tag;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String nextpages_tag;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String url_add_info;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String next_url_add_info;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String title_tag; 		
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String name_tag; 		
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String name_img_tag; 		
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String name_tag2; 		
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String name_img_tag2; 		
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String content_tag; 		
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String date_tag;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String date_modify_tag;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private int date_length; 		
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String current_year;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String nextpage_param;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String operator_feature_tag;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String operator_feature_text;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private int page_start_no;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private int page_count;
	@Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String storage_path;
	
	public JsonData() {
		// TODO Auto-generated constructor stub
	}

	public JsonData(String url_addr, String notice_tag, String notices_tag, String nextpages_tag, String url_add_info,
			String next_url_add_info, String title_tag, String name_tag, String name_img_tag, String name_tag2,
			String name_img_tag2, String content_tag, String date_tag, String date_modify_tag, int date_length,
			String current_year, String nextpage_param, String operator_feature_tag, String operator_feature_text,
			int page_start_no, int page_count, String storage_path) {
		super();
		this.url_addr = url_addr;
		this.notice_tag = notice_tag;
		this.notices_tag = notices_tag;
		this.nextpages_tag = nextpages_tag;
		this.url_add_info = url_add_info;
		this.next_url_add_info = next_url_add_info;
		this.title_tag = title_tag;
		this.name_tag = name_tag;
		this.name_img_tag = name_img_tag;
		this.name_tag2 = name_tag2;
		this.name_img_tag2 = name_img_tag2;
		this.content_tag = content_tag;
		this.date_tag = date_tag;
		this.date_modify_tag = date_modify_tag;
		this.date_length = date_length;
		this.current_year = current_year;
		this.nextpage_param = nextpage_param;
		this.operator_feature_tag = operator_feature_tag;
		this.operator_feature_text = operator_feature_text;
		this.page_start_no = page_start_no;
		this.page_count = page_count;
		this.storage_path = storage_path;
	}
}
