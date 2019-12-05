package kr.datasolution.lake.main.vo;

public class MetaDBVo {
    int DID;
    String CRAWLEDTIME;
    int CID;
    int AID;
    int CDATAIDX;
    String TITLE;
    String CREATED;
    String ORG_FILE_NAME;
    String SAV_FILE_NAME;
    String FILE_PATH;
    String FILE_EXT;
    String FILE_TYPE;
    String URL;
    String PRE_HANDLING_TYPE;
    String TAG;

    public MetaDBVo() {
    }

    public MetaDBVo(int DID, String CRAWLEDTIME, int CID, int AID, String TITLE, String CREATED, String ORG_FILE_NAME, String SAV_FILE_NAME, String FILE_PATH, String FILE_EXT, String FILE_TYPE, String URL, String PRE_HANDLING_TYPE, String TAG) {
        this.DID = DID;
        this.CRAWLEDTIME = CRAWLEDTIME;
        this.CID = CID;
        this.AID = AID;
        this.TITLE = TITLE;
        this.CREATED = CREATED;
        this.ORG_FILE_NAME = ORG_FILE_NAME;
        this.SAV_FILE_NAME = SAV_FILE_NAME;
        this.FILE_PATH = FILE_PATH;
        this.FILE_EXT = FILE_EXT;
        this.FILE_TYPE = FILE_TYPE;
        this.URL = URL;
        this.PRE_HANDLING_TYPE = PRE_HANDLING_TYPE;
        this.TAG = TAG;
    }

    public int getDID() {
        return DID;
    }

    public void setDID(int DID) {
        this.DID = DID;
    }

    public String getCRAWLEDTIME() {
        return CRAWLEDTIME;
    }

    public void setCRAWLEDTIME(String CRAWLEDTIME) {
        this.CRAWLEDTIME = CRAWLEDTIME;
    }

    public int getCID() {
        return CID;
    }

    public void setCID(int CID) {
        this.CID = CID;
    }

    public int getAID() {
        return AID;
    }

    public void setAID(int AID) {
        this.AID = AID;
    }

    public int getCDATAIDX() {
        return CDATAIDX;
    }

    public void setCDATAIDX(int CDATAIDX) {
        this.CDATAIDX = CDATAIDX;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getCREATED() {
        return CREATED;
    }

    public void setCREATED(String CREATED) {
        this.CREATED = CREATED;
    }

    public String getORG_FILE_NAME() {
        return ORG_FILE_NAME;
    }

    public void setORG_FILE_NAME(String ORG_FILE_NAME) {
        this.ORG_FILE_NAME = ORG_FILE_NAME;
    }

    public String getSAV_FILE_NAME() {
        return SAV_FILE_NAME;
    }

    public void setSAV_FILE_NAME(String SAV_FILE_NAME) {
        this.SAV_FILE_NAME = SAV_FILE_NAME;
    }

    public String getFILE_PATH() {
        return FILE_PATH;
    }

    public void setFILE_PATH(String FILE_PATH) {
        this.FILE_PATH = FILE_PATH;
    }

    public String getFILE_EXT() {
        return FILE_EXT;
    }

    public void setFILE_EXT(String FILE_EXT) {
        this.FILE_EXT = FILE_EXT;
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

    public String getPRE_HANDLING_TYPE() {
        return PRE_HANDLING_TYPE;
    }

    public void setPRE_HANDLING_TYPE(String PRE_HANDLING_TYPE) {
        this.PRE_HANDLING_TYPE = PRE_HANDLING_TYPE;
    }

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }
}
