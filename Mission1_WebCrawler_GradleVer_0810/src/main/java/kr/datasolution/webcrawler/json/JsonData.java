package kr.datasolution.webcrawler.json;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

//@Data
//@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
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
}
