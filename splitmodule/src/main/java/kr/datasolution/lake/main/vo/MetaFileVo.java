package kr.datasolution.lake.main.vo;

import java.util.List;

public class MetaFileVo {
    int did;
    String crawledTime;
    int cid;
    int aid;
    String title;
    String created;
    String filePath;
    String fileName;
    String fileExt;
    List<String> attach;

    public MetaFileVo() {
    }

    public MetaFileVo(int did, String crawledTime, int cid, int aid, String title, String created, String filePath, String fileName, String fileExt, List<String> attach) {
        this.did = did;
        this.crawledTime = crawledTime;
        this.cid = cid;
        this.aid = aid;
        this.title = title;
        this.created = created;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileExt = fileExt;
        this.attach = attach;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public List<String> getAttach() {
        return attach;
    }

    public void setAttach(List<String> attach) {
        this.attach = attach;
    }
}
