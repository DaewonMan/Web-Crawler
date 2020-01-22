package com.dwm.webcrawler.json;

import java.util.List;

import com.dwm.webcrawler.tag.CategoryTag;
import com.dwm.webcrawler.tag.CrawlDataTag;

public class JsonData {
	private String webpage;
	private List<CategoryTag> categories;
	private String post;
	private String nextPage;
	private List<String> nextPageParam;
	private List<CrawlDataTag> data;
	private String hashTag;
	private int did;
	private int cid;
	private int aid;
	private String fileExt;
	private char fileType;
	private String preHandlingType;
	private String basicStoragePath;
	
	public JsonData() {
		// TODO Auto-generated constructor stub
	}

	public JsonData(String webpage, List<CategoryTag> categories, String post, String nextPage,
			List<String> nextPageParam, List<CrawlDataTag> data, String hashTag, int did, int cid, int aid,
			String fileExt, char fileType, String preHandlingType, String basicStoragePath) {
		super();
		this.webpage = webpage;
		this.categories = categories;
		this.post = post;
		this.nextPage = nextPage;
		this.nextPageParam = nextPageParam;
		this.data = data;
		this.hashTag = hashTag;
		this.did = did;
		this.cid = cid;
		this.aid = aid;
		this.fileExt = fileExt;
		this.fileType = fileType;
		this.preHandlingType = preHandlingType;
		this.basicStoragePath = basicStoragePath;
	}

	public String getWebpage() {
		return webpage;
	}

	public void setWebpage(String webpage) {
		this.webpage = webpage;
	}

	public List<CategoryTag> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryTag> categories) {
		this.categories = categories;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getNextPage() {
		return nextPage;
	}

	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

	public List<String> getNextPageParam() {
		return nextPageParam;
	}

	public void setNextPageParam(List<String> nextPageParam) {
		this.nextPageParam = nextPageParam;
	}

	public List<CrawlDataTag> getData() {
		return data;
	}

	public void setData(List<CrawlDataTag> data) {
		this.data = data;
	}

	public String getHashTag() {
		return hashTag;
	}

	public void setHashTag(String hashTag) {
		this.hashTag = hashTag;
	}

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public char getFileType() {
		return fileType;
	}

	public void setFileType(char fileType) {
		this.fileType = fileType;
	}

	public String getPreHandlingType() {
		return preHandlingType;
	}

	public void setPreHandlingType(String preHandlingType) {
		this.preHandlingType = preHandlingType;
	}

	public String getBasicStoragePath() {
		return basicStoragePath;
	}

	public void setBasicStoragePath(String basicStoragePath) {
		this.basicStoragePath = basicStoragePath;
	}
}
