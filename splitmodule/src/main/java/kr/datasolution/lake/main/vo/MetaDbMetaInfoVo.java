package kr.datasolution.lake.main.vo;

public class MetaDbMetaInfoVo {
    int DID;
    String FILE_TYPE;
    String URL;
    String PRE_HANDDLING_TYPE;
    String TAG;

    public MetaDbMetaInfoVo() {
    }

    public MetaDbMetaInfoVo(int DID, String FILE_TYPE, String URL, String PRE_HANDDLING_TYPE, String TAG) {
        this.DID = DID;
        this.FILE_TYPE = FILE_TYPE;
        this.URL = URL;
        this.PRE_HANDDLING_TYPE = PRE_HANDDLING_TYPE;
        this.TAG = TAG;
    }

    public int getDID() {
        return DID;
    }

    public void setDID(int DID) {
        this.DID = DID;
    }

    public String getFILE_TYPE() {
        return FILE_TYPE;
    }

    public void setFILE_TYPE(String FILE_TYPE) {
        this.FILE_TYPE = FILE_TYPE;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getPRE_HANDDLING_TYPE() {
        return PRE_HANDDLING_TYPE;
    }

    public void setPRE_HANDDLING_TYPE(String PRE_HANDDLING_TYPE) {
        this.PRE_HANDDLING_TYPE = PRE_HANDDLING_TYPE;
    }

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }
}
