package kr.datasolution.webcrawler.db;

public class DbTableData {
	private int did;
	private String crawledTime;
	private int cid;
	private int aid;
	private String title;
	private String created;
	private String orgFileName;
	private String savFileName;
	private String filePath;
	private String fileExt;		
	private char fileType;
	private String url;
	private String preHandlingType;		
	private String tag;
	
	public DbTableData() {
		// TODO Auto-generated constructor stub
	}

	public DbTableData(int did, String crawledTime, int cid, int aid, String title, String created, String orgFileName,
			String savFileName, String filePath, String fileExt, char fileType, String url, String preHandlingType,
			String tag) {
		super();
		this.did = did;
		this.crawledTime = crawledTime;
		this.cid = cid;
		this.aid = aid;
		this.title = title;
		this.created = created;
		this.orgFileName = orgFileName;
		this.savFileName = savFileName;
		this.filePath = filePath;
		this.fileExt = fileExt;
		this.fileType = fileType;
		this.url = url;
		this.preHandlingType = preHandlingType;
		this.tag = tag;
	}

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	public String getCrawledTime() {
		return crawledTime;
	}

	public void setCrawledTime(String crawledTime) {
		this.crawledTime = crawledTime;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getOrgFileName() {
		return orgFileName;
	}

	public void setOrgFileName(String orgFileName) {
		this.orgFileName = orgFileName;
	}

	public String getSavFileName() {
		return savFileName;
	}

	public void setSavFileName(String savFileName) {
		this.savFileName = savFileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPreHandlingType() {
		return preHandlingType;
	}

	public void setPreHandlingType(String preHandlingType) {
		this.preHandlingType = preHandlingType;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
