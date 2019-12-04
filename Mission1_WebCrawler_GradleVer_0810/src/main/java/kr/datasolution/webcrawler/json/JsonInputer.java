package kr.datasolution.webcrawler.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonInputer {
	public static JsonData inputToBean(String json_ctt) throws Exception {
		System.out.println("-- start inputToBean");
		// String -> json �Ľ�
		JSONParser jp = new JSONParser();
		JSONObject jo = (JSONObject) jp.parse(json_ctt);
		JSONArray documents = (JSONArray) jo.get("documents");

		JSONObject info = (JSONObject) documents.get(0);
		
		// �ں�� json ������ �Ҵ�
		JsonData j_data = new JsonData();
		j_data.setUrl_addr((String) info.get("url_addr"));
		j_data.setNotice_tag((String) info.get("notice_tag"));
		j_data.setNotices_tag((String) info.get("notices_tag"));
		j_data.setNextpages_tag((String) info.get("nextpages_tag"));
		j_data.setUrl_add_info((String) info.get("url_add_info"));
		j_data.setNext_url_add_info((String) info.get("next_url_add_info"));
		j_data.setTitle_tag((String) info.get("title_tag"));
		j_data.setName_tag((String) info.get("name_tag"));
		j_data.setName_img_tag((String) info.get("name_img_tag"));
		j_data.setName_tag2((String) info.get("name_tag2"));
		j_data.setName_img_tag2((String) info.get("name_img_tag2"));
		j_data.setContent_tag((String) info.get("content_tag"));
		j_data.setDate_tag((String) info.get("date_tag"));
		j_data.setDate_modify_tag((String) info.get("date_modify_tag"));
		j_data.setDate_length(Integer.parseInt((String) info.get("date_length")));
		j_data.setCurrent_year((String) info.get("current_year"));
		j_data.setPage_count(Integer.parseInt((String) info.get("page_count")));
		j_data.setNextpage_param((String) info.get("nextpage_param"));
		j_data.setOperator_feature_tag((String) info.get("operator_feature_tag"));
		j_data.setOperator_feature_text((String) info.get("operator_feature_text"));
		j_data.setPage_start_no(Integer.parseInt((String) info.get("page_start_no")));
		j_data.setStorage_path((String) info.get("storage_path"));
		
		System.out.println("-- end inputToBean");
		
		return j_data;
	}
}
