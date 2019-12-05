package kr.datasolution.webcrawler.db;

import java.math.BigDecimal;

public class DbData {
	private BigDecimal did;
	private String crawledTime;
	private BigDecimal cid;
	private BigDecimal aid;
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
	private String content;
	
	public DbData() {
		// TODO Auto-generated constructor stub
	}

	public DbData(BigDecimal did, String crawledTime, BigDecimal cid, BigDecimal aid, String title, String created,
			String orgFileName, String savFileName, String filePath, String fileExt, char fileType, String url,
			String preHandlingType, String tag, String content) {
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
		this.content = content;
	}

	public BigDecimal getDid() {
		return did;
	}

	public void setDid(BigDecimal did) {
		this.did = did;
	}

	public String getCrawledTime() {
		return crawledTime;
	}

	public void setCrawledTime(String crawledTime) {
		this.crawledTime = crawledTime;
	}

	public BigDecimal getCid() {
		return cid;
	}

	public void setCid(BigDecimal cid) {
		this.cid = cid;
	}

	public BigDecimal getAid() {
		return aid;
	}

	public void setAid(BigDecimal aid) {
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
