package kr.datasolution.lake.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kr.datasolution.lake.main.vo.MetaDBVo;
import kr.datasolution.lake.main.vo.MetaDbMetaInfoVo;
import kr.datasolution.lake.main.vo.MetaFileVo;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

@SuppressWarnings("Duplicates")
public class ChangeFormatMain {
    public static void main(String[] args) throws Exception{
        //split->csv2json의 결과를 거친 Json파일 있는 곳
        String basePath = "C:\\Users\\chyh0\\Downloads\\FAOSTAT_JSON";
        //json파일 포맷 변경 후 떨굴곳
        String outputBasePath = "C:\\Users\\chyh0\\Downloads\\FAOSTAT_JSON\\result";
        //메타파일 작성시 파일경로 지정
        String metaOutputBasePath = "/home/datalake/workspace/SaveFile";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //오늘날짜 받아오는 Date변수
        Date today = new Date();
        //파일경로 지정할 때 쓰는 날짜 포맷 sdf
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/hh/mm/ss");
        //메타파일 쓸 때 지정하는 날짜포맷 sdf
        SimpleDateFormat metaFileSdf = new SimpleDateFormat("yyyy-MM-dd");
        //메타DB에 넣을때 쓰는  날짜포맷 sdf
        SimpleDateFormat metaDbSdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //각 sdf포맷에 맞춰 String형태로 임시 저장
        String metaFileCreatedTime = metaFileSdf.format(today);
        String crawledTime = sdf.format(today);
        String metaDbCreatedTime = metaDbSdf.format(today);
        //DB 연결객체
        Connection connection = connDB();
        //CID값 cid는 고정으로 aid값으로 파일을 증가시킨다.
        //CID는 원본파일 개수 하나에 하나씩 대응한다.
        int CidNum = 1;
        //AID는 분할된 파일 하나에 한개씩 대응한다.
        int Aidnum = 1;

        //Set으로 Dir리스트를 받아온다.
        //Set은 중복을 담지 않는다 고로 한경로에 하나씩만 받아온다.
        //메소드 들어가보면 getParent어쩌고로 작성되어 최종파일의 부모경로 마지막폴더 리스트만 가져오게 설정되어있음
        Set<String> dirList = new HashSet<String>();
        getDirList(basePath, dirList);

        for(String tmpDir : dirList){
            List<String> fileList = new ArrayList<String>();
            MetaDBVo metaDBVo = null;
            //디렉토리 리스트말고 파일리스트로 스캔하여 파일들의 목록을 받아온다.
            getFileList(tmpDir, fileList);

            //각 폴더안에있는 metainfo.json파일을 읽어 MetaDbMetaInfoVo객체 에 할당
            MetaDbMetaInfoVo metaDbMetaInfoVo = gson.fromJson(FileUtils.readFileToString(new File(tmpDir+"/metainfo.json"), "UTF-8"), new TypeToken<MetaDbMetaInfoVo>(){}.getType());

            for(String tmpFile : fileList){
                File f = new File(tmpFile);

                //DatasolutionDatalakesSplit으로 시작하는 분할된 csv파일을 복사하여 datalake적재 형태에 맞게 변형하여 카피한다.
                FileUtils.copyFile(new File(tmpDir+"/"+tmpFile),
                        new File(outputBasePath+"/"+metaDbMetaInfoVo.getDID()+"/"+crawledTime+"/"+CidNum+"/"+Aidnum+"/"
                                +metaDbMetaInfoVo.getDID()+"-"+crawledTime.replaceAll("/","-")+"-"+CidNum+"-"+Aidnum
                                +"."+f.getName().substring(f.getName().lastIndexOf(".")+1)));

                //파일 복사와 동시에 메타파일에도 쓴다.
                MetaFileVo metaFileVo = new MetaFileVo(metaDbMetaInfoVo.getDID()
                                                    , crawledTime
                                                    , CidNum
                                                    , Aidnum
                                                    , tmpDir.substring(tmpDir.lastIndexOf("\\") + 1)
                                                    , metaFileCreatedTime
                                                    , metaOutputBasePath + "/" + metaDbMetaInfoVo.getDID() + "/" + crawledTime + "/" + CidNum + "/" + Aidnum
                                                    , metaDbMetaInfoVo.getDID() + "-" + crawledTime.replaceAll("/", "-") + "-" + CidNum + "-" + Aidnum
                                                    , f.getName().substring(f.getName().lastIndexOf(".") + 1)
                                                    , new ArrayList<String>());

                //MetaDB에 들어갈 내용도 작성해준다.
                metaDBVo = new MetaDBVo(metaDbMetaInfoVo.getDID()
                        , metaDbCreatedTime
                        , CidNum
                        , Aidnum
                        , tmpDir.substring(tmpDir.lastIndexOf("\\") + 1)
                        , metaDbCreatedTime
                        , tmpDir.substring(tmpDir.lastIndexOf("\\") + 1)+".csv"
                        , metaDbMetaInfoVo.getDID() + "-" + crawledTime.replaceAll("/", "-") + "-" + CidNum + "-" + Aidnum
                        , metaOutputBasePath + "/" + metaDbMetaInfoVo.getDID() + "/" + crawledTime + "/" + CidNum + "/" + Aidnum
                        , f.getName().substring(f.getName().lastIndexOf(".") + 1)
                        , metaDbMetaInfoVo.getFILE_TYPE()
                        , metaDbMetaInfoVo.getURL()
                        , metaDbMetaInfoVo.getPRE_HANDDLING_TYPE()
                        , metaDbMetaInfoVo.getTAG()
                );

                //메타파일 써주고
                FileUtils.writeStringToFile(new File(outputBasePath+"/"+metaDbMetaInfoVo.getDID()+"/"+crawledTime+"/"+CidNum+"/"+Aidnum+"/"+metaDbMetaInfoVo.getDID()+"-"+crawledTime.replaceAll("/","-")+"-"+CidNum+"-"+Aidnum+".meta"),gson.toJson(metaFileVo), "UTF-8");
                //데이터 DB에 넣고
                insertData(connection, metaDBVo);
                //AID올린다.
                Aidnum++;
            }

            System.out.println(gson.toJson(metaDBVo));

            //한 폴더 다 돌면 aid는 다시 1로 초기화
            Aidnum = 1;
        }


    }

    public static void getDirList(String source, Set<String> fileDirList){
        File dir = new File(source);
        File[] fileList = dir.listFiles();

        try{
            for(int i = 0 ; i < fileList.length ; i++){
                File file = fileList[i];
                if(file.isFile()){
                    fileDirList.add(file.getParentFile().toString());
                }else if(file.isDirectory()){
                    getDirList(file.getCanonicalPath().toString(), fileDirList);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public static void getFileList(String source, List<String> filePathList){
        File dir = new File(source);
        File[] fileList = dir.listFiles();

        try{
            for(int i = 0 ; i < fileList.length ; i++){
                File file = fileList[i];
                if(file.isFile() && !file.getName().equalsIgnoreCase("metainfo.json")){
                    filePathList.add(file.getName());
                }else if(file.isDirectory()){
                    getFileList(file.getCanonicalPath().toString(), filePathList);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public static Connection connDB(){
        Connection connection = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://192.168.210.146:3306/DataLake_Proto_Test?useSSL=false" , "root", "Opensns!#@$");
//            Statement st = null;
//            st = connection.createStatement();
//
//            String sql;
//            sql = "select * FROM def_data;";
//
//            ResultSet rs = st.executeQuery(sql);
//
//            while (rs.next()) {
//                String sqlRecipeProcess = rs.getString("DESCRIPTION");
//                System.out.println(sqlRecipeProcess);
//            }
//
//            rs.close();
//            st.close();
//            connection.close();
        } catch (SQLException se1) {
            se1.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return connection;
    }

    public static void insertData(Connection connection, MetaDBVo metaDBVo) {
        String sql = "INSERT INTO crawl_data(DID, CRAWLEDTIME, CID, AID, TITLE, ORG_FILE_NAME, SAV_FILE_NAME, FILE_PATH, URL, PRE_HANDLING_TYPE, TAG, INSERT_MONGO, FILE_EXT, FILE_TYPE, CREATED)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, metaDBVo.getDID());
            pstmt.setString(2, metaDBVo.getCRAWLEDTIME());
            pstmt.setInt(3, metaDBVo.getCID());
            pstmt.setInt(4, metaDBVo.getAID());
            pstmt.setString(5, metaDBVo.getTITLE());
            pstmt.setString(6, metaDBVo.getORG_FILE_NAME());
            pstmt.setString(7, metaDBVo.getSAV_FILE_NAME());
            pstmt.setString(8, metaDBVo.getFILE_PATH());
            pstmt.setString(9, metaDBVo.getURL());
            pstmt.setString(10, metaDBVo.getPRE_HANDLING_TYPE());
            pstmt.setString(11, metaDBVo.getTAG());
            pstmt.setString(12, "E");
            pstmt.setString(13, metaDBVo.getFILE_EXT());
            pstmt.setString(14, metaDBVo.getFILE_TYPE());
            pstmt.setString(15, metaDBVo.getCREATED());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
