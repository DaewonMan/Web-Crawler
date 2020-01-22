package com.dwm.webcrawler.main;

import com.dwm.webcrawler.main.vo.MetaDBVo;
import com.dwm.webcrawler.main.vo.MetaDbMetaInfoVo;
import com.dwm.webcrawler.main.vo.MetaFileVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

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
        //split->csv2json?˜ ê²°ê³¼ë¥? ê±°ì¹œ Json?ŒŒ?¼ ?ˆ?Š” ê³?
        String basePath = "C:\\Users\\chyh0\\Downloads\\FAOSTAT_JSON";
        //json?ŒŒ?¼ ?¬ë§? ë³?ê²? ?›„ ?–¨êµ´ê³³
        String outputBasePath = "C:\\Users\\chyh0\\Downloads\\FAOSTAT_JSON\\result";
        //ë©”í??ŒŒ?¼ ?‘?„±?‹œ ?ŒŒ?¼ê²½ë¡œ ì§?? •
        String metaOutputBasePath = "/home/datalake/workspace/SaveFile";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //?˜¤?Š˜?‚ ì§? ë°›ì•„?˜¤?Š” Dateë³??ˆ˜
        Date today = new Date();
        //?ŒŒ?¼ê²½ë¡œ ì§?? •?•  ?•Œ ?“°?Š” ?‚ ì§? ?¬ë§? sdf
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/hh/mm/ss");
        //ë©”í??ŒŒ?¼ ?“¸ ?•Œ ì§?? •?•˜?Š” ?‚ ì§œí¬ë§? sdf
        SimpleDateFormat metaFileSdf = new SimpleDateFormat("yyyy-MM-dd");
        //ë©”í?DB?— ?„£?„?•Œ ?“°?Š”  ?‚ ì§œí¬ë§? sdf
        SimpleDateFormat metaDbSdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //ê°? sdf?¬ë§·ì— ë§ì¶° String?˜•?ƒœë¡? ?„?‹œ ???¥
        String metaFileCreatedTime = metaFileSdf.format(today);
        String crawledTime = sdf.format(today);
        String metaDbCreatedTime = metaDbSdf.format(today);
        //DB ?—°ê²°ê°ì²?
        Connection connection = connDB();
        //CIDê°? cid?Š” ê³ ì •?œ¼ë¡? aidê°’ìœ¼ë¡? ?ŒŒ?¼?„ ì¦ê??‹œ?‚¨?‹¤.
        //CID?Š” ?›ë³¸íŒŒ?¼ ê°œìˆ˜ ?•˜?‚˜?— ?•˜?‚˜?”© ???‘?•œ?‹¤.
        int CidNum = 1;
        //AID?Š” ë¶„í• ?œ ?ŒŒ?¼ ?•˜?‚˜?— ?•œê°œì”© ???‘?•œ?‹¤.
        int Aidnum = 1;

        //Set?œ¼ë¡? Dirë¦¬ìŠ¤?Š¸ë¥? ë°›ì•„?˜¨?‹¤.
        //Set?? ì¤‘ë³µ?„ ?‹´ì§? ?•Š?Š”?‹¤ ê³ ë¡œ ?•œê²½ë¡œ?— ?•˜?‚˜?”©ë§? ë°›ì•„?˜¨?‹¤.
        //ë©”ì†Œ?“œ ?“¤?–´ê°?ë³´ë©´ getParent?–´ì©Œê³ ë¡? ?‘?„±?˜?–´ ìµœì¢…?ŒŒ?¼?˜ ë¶?ëª¨ê²½ë¡? ë§ˆì?ë§‰í´?” ë¦¬ìŠ¤?Š¸ë§? ê°?? ¸?˜¤ê²? ?„¤? •?˜?–´?ˆ?Œ
        Set<String> dirList = new HashSet<String>();
        getDirList(basePath, dirList);

        for(String tmpDir : dirList){
            List<String> fileList = new ArrayList<String>();
            MetaDBVo metaDBVo = null;
            //?””? ‰?† ë¦? ë¦¬ìŠ¤?Š¸ë§ê³  ?ŒŒ?¼ë¦¬ìŠ¤?Š¸ë¡? ?Š¤ìº”í•˜?—¬ ?ŒŒ?¼?“¤?˜ ëª©ë¡?„ ë°›ì•„?˜¨?‹¤.
            getFileList(tmpDir, fileList);

            //ê°? ?´?”?•ˆ?—?ˆ?Š” metainfo.json?ŒŒ?¼?„ ?½?–´ MetaDbMetaInfoVoê°ì²´ ?— ?• ?‹¹
            MetaDbMetaInfoVo metaDbMetaInfoVo = gson.fromJson(FileUtils.readFileToString(new File(tmpDir+"/metainfo.json"), "UTF-8"), new TypeToken<MetaDbMetaInfoVo>(){}.getType());

            for(String tmpFile : fileList){
                File f = new File(tmpFile);

                //DatasolutionDatalakesSplit?œ¼ë¡? ?‹œ?‘?•˜?Š” ë¶„í• ?œ csv?ŒŒ?¼?„ ë³µì‚¬?•˜?—¬ datalake? ?¬ ?˜•?ƒœ?— ë§ê²Œ ë³??˜•?•˜?—¬ ì¹´í”¼?•œ?‹¤.
                FileUtils.copyFile(new File(tmpDir+"/"+tmpFile),
                        new File(outputBasePath+"/"+metaDbMetaInfoVo.getDID()+"/"+crawledTime+"/"+CidNum+"/"+Aidnum+"/"
                                +metaDbMetaInfoVo.getDID()+"-"+crawledTime.replaceAll("/","-")+"-"+CidNum+"-"+Aidnum
                                +"."+f.getName().substring(f.getName().lastIndexOf(".")+1)));

                //?ŒŒ?¼ ë³µì‚¬?? ?™?‹œ?— ë©”í??ŒŒ?¼?—?„ ?“´?‹¤.
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

                //MetaDB?— ?“¤?–´ê°? ?‚´?š©?„ ?‘?„±?•´ì¤??‹¤.
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

                //ë©”í??ŒŒ?¼ ?¨ì£¼ê³ 
                FileUtils.writeStringToFile(new File(outputBasePath+"/"+metaDbMetaInfoVo.getDID()+"/"+crawledTime+"/"+CidNum+"/"+Aidnum+"/"+metaDbMetaInfoVo.getDID()+"-"+crawledTime.replaceAll("/","-")+"-"+CidNum+"-"+Aidnum+".meta"),gson.toJson(metaFileVo), "UTF-8");
                //?°?´?„° DB?— ?„£ê³?
                insertData(connection, metaDBVo);
                //AID?˜¬ë¦°ë‹¤.
                Aidnum++;
            }

            System.out.println(gson.toJson(metaDBVo));

            //?•œ ?´?” ?‹¤ ?Œë©? aid?Š” ?‹¤?‹œ 1ë¡? ì´ˆê¸°?™”
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
