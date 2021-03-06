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
        //split->csv2json? κ²°κ³Όλ₯? κ±°μΉ Json??Ό ?? κ³?
        String basePath = "C:\\Users\\chyh0\\Downloads\\FAOSTAT_JSON";
        //json??Ό ?¬λ§? λ³?κ²? ? ?¨κ΅΄κ³³
        String outputBasePath = "C:\\Users\\chyh0\\Downloads\\FAOSTAT_JSON\\result";
        //λ©ν???Ό ??±? ??Όκ²½λ‘ μ§?? 
        String metaOutputBasePath = "/home/datalake/workspace/SaveFile";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //?€?? μ§? λ°μ?€? Dateλ³??
        Date today = new Date();
        //??Όκ²½λ‘ μ§?? ?  ? ?°? ? μ§? ?¬λ§? sdf
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/hh/mm/ss");
        //λ©ν???Ό ?Έ ? μ§?? ?? ? μ§ν¬λ§? sdf
        SimpleDateFormat metaFileSdf = new SimpleDateFormat("yyyy-MM-dd");
        //λ©ν?DB? ?£?? ?°?  ? μ§ν¬λ§? sdf
        SimpleDateFormat metaDbSdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //κ°? sdf?¬λ§·μ λ§μΆ° String??λ‘? ?? ???₯
        String metaFileCreatedTime = metaFileSdf.format(today);
        String crawledTime = sdf.format(today);
        String metaDbCreatedTime = metaDbSdf.format(today);
        //DB ?°κ²°κ°μ²?
        Connection connection = connDB();
        //CIDκ°? cid? κ³ μ ?Όλ‘? aidκ°μΌλ‘? ??Ό? μ¦κ???¨?€.
        //CID? ?λ³Έν?Ό κ°μ ??? ???© ?????€.
        int CidNum = 1;
        //AID? λΆν ? ??Ό ??? ?κ°μ© ?????€.
        int Aidnum = 1;

        //Set?Όλ‘? Dirλ¦¬μ€?Έλ₯? λ°μ?¨?€.
        //Set?? μ€λ³΅? ?΄μ§? ???€ κ³ λ‘ ?κ²½λ‘? ???©λ§? λ°μ?¨?€.
        //λ©μ? ?€?΄κ°?λ³΄λ©΄ getParent?΄μ©κ³ λ‘? ??±??΄ μ΅μ’??Ό? λΆ?λͺ¨κ²½λ‘? λ§μ?λ§ν΄? λ¦¬μ€?Έλ§? κ°?? Έ?€κ²? ?€? ??΄??
        Set<String> dirList = new HashSet<String>();
        getDirList(basePath, dirList);

        for(String tmpDir : dirList){
            List<String> fileList = new ArrayList<String>();
            MetaDBVo metaDBVo = null;
            //?? ? λ¦? λ¦¬μ€?Έλ§κ³  ??Όλ¦¬μ€?Έλ‘? ?€μΊν?¬ ??Ό?€? λͺ©λ‘? λ°μ?¨?€.
            getFileList(tmpDir, fileList);

            //κ°? ?΄????? metainfo.json??Ό? ?½?΄ MetaDbMetaInfoVoκ°μ²΄ ? ? ?Ή
            MetaDbMetaInfoVo metaDbMetaInfoVo = gson.fromJson(FileUtils.readFileToString(new File(tmpDir+"/metainfo.json"), "UTF-8"), new TypeToken<MetaDbMetaInfoVo>(){}.getType());

            for(String tmpFile : fileList){
                File f = new File(tmpFile);

                //DatasolutionDatalakesSplit?Όλ‘? ???? λΆν ? csv??Ό? λ³΅μ¬??¬ datalake? ?¬ ??? λ§κ² λ³????¬ μΉ΄νΌ??€.
                FileUtils.copyFile(new File(tmpDir+"/"+tmpFile),
                        new File(outputBasePath+"/"+metaDbMetaInfoVo.getDID()+"/"+crawledTime+"/"+CidNum+"/"+Aidnum+"/"
                                +metaDbMetaInfoVo.getDID()+"-"+crawledTime.replaceAll("/","-")+"-"+CidNum+"-"+Aidnum
                                +"."+f.getName().substring(f.getName().lastIndexOf(".")+1)));

                //??Ό λ³΅μ¬?? ??? λ©ν???Ό?? ?΄?€.
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

                //MetaDB? ?€?΄κ°? ?΄?©? ??±?΄μ€??€.
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

                //λ©ν???Ό ?¨μ£Όκ³ 
                FileUtils.writeStringToFile(new File(outputBasePath+"/"+metaDbMetaInfoVo.getDID()+"/"+crawledTime+"/"+CidNum+"/"+Aidnum+"/"+metaDbMetaInfoVo.getDID()+"-"+crawledTime.replaceAll("/","-")+"-"+CidNum+"-"+Aidnum+".meta"),gson.toJson(metaFileVo), "UTF-8");
                //?°?΄?° DB? ?£κ³?
                insertData(connection, metaDBVo);
                //AID?¬λ¦°λ€.
                Aidnum++;
            }

            System.out.println(gson.toJson(metaDBVo));

            //? ?΄? ?€ ?λ©? aid? ?€? 1λ‘? μ΄κΈ°?
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
