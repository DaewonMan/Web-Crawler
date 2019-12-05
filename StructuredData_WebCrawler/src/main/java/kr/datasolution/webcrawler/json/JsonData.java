package kr.datasolution.webcrawler.json;

import kr.datasolution.webcrawler.crawl.CrawlData;

public class JsonData {
	private String webpage;
	private CrawlData data;
	private String hashTag;
	private int did;
	private String fileExt;
	private char fileType;
	private String preHandlingType;
	private String basicStoragePath;
	private String dbUrl;
	private String dbId;
	private String dbPw;
	private String dbTableName;
	private int connectTimeout;
	private String jsonFilePath;
	private String cidFlagFile;
	private String ipFlagFile;
	private String countryCodeFlagFile;
	
	public JsonData() {
		// TODO Auto-generated constructor stub
	}

	public JsonData(String webpage, CrawlData data, String hashTag, int did, String fileExt, char fileType,
			String preHandlingType, String basicStoragePath, String dbUrl, String dbId, String dbPw, String dbTableName,
			int connectTimeout, String jsonFilePath, String cidFlagFile, String ipFlagFile,
			String countryCodeFlagFile) {
		super();
		this.webpage = webpage;
		this.data = data;
		this.hashTag = hashTag;
		this.did = did;
		this.fileExt = fileExt;
		this.fileType = fileType;
		this.preHandlingType = preHandlingType;
		this.basicStoragePath = basicStoragePath;
		this.dbUrl = dbUrl;
		this.dbId = dbId;
		this.dbPw = dbPw;
		this.dbTableName = dbTableName;
		this.connectTimeout = connectTimeout;
		this.jsonFilePath = jsonFilePath;
		this.cidFlagFile = cidFlagFile;
		this.ipFlagFile = ipFlagFile;
		this.countryCodeFlagFile = countryCodeFlagFile;
	}



	public String getWebpage() {
		return webpage;
	}

	public void setWebpage(String webpage) {
		this.webpage = webpage;
	}

	public CrawlData getData() {
		return data;
	}

	public void setData(CrawlData data) {
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

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbId() {
		return dbId;
	}

	public void setDbId(String dbId) {
		this.dbId = dbId;
	}

	public String getDbPw() {
		return dbPw;
	}

	public void setDbPw(String dbPw) {
		this.dbPw = dbPw;
	}

	public String getDbTableName() {
		return dbTableName;
	}

	public void setDbTableName(String dbTableName) {
		this.dbTableName = dbTableName;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public String getJsonFilePath() {
		return jsonFilePath;
	}

	public void setJsonFilePath(String jsonFilePath) {
		this.jsonFilePath = jsonFilePath;
	}

	public String getCidFlagFile() {
		return cidFlagFile;
	}

	public void setCidFlagFile(String cidFlagFile) {
		this.cidFlagFile = cidFlagFile;
	}

	public String getIpFlagFile() {
		return ipFlagFile;
	}

	public void setIpFlagFile(String ipFlagFile) {
		this.ipFlagFile = ipFlagFile;
	}

	public String getCountryCodeFlagFile() {
		return countryCodeFlagFile;
	}

	public void setCountryCodeFlagFile(String countryCodeFlagFile) {
		this.countryCodeFlagFile = countryCodeFlagFile;
	}
}
