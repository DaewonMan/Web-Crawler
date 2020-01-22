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
        //split->csv2json?�� 결과�? 거친 Json?��?�� ?��?�� �?
        String basePath = "C:\\Users\\chyh0\\Downloads\\FAOSTAT_JSON";
        //json?��?�� ?���? �?�? ?�� ?��굴곳
        String outputBasePath = "C:\\Users\\chyh0\\Downloads\\FAOSTAT_JSON\\result";
        //메�??��?�� ?��?��?�� ?��?��경로 �??��
        String metaOutputBasePath = "/home/datalake/workspace/SaveFile";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //?��?��?���? 받아?��?�� Date�??��
        Date today = new Date();
        //?��?��경로 �??��?�� ?�� ?��?�� ?���? ?���? sdf
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/hh/mm/ss");
        //메�??��?�� ?�� ?�� �??��?��?�� ?��짜포�? sdf
        SimpleDateFormat metaFileSdf = new SimpleDateFormat("yyyy-MM-dd");
        //메�?DB?�� ?��?��?�� ?��?��  ?��짜포�? sdf
        SimpleDateFormat metaDbSdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //�? sdf?��맷에 맞춰 String?��?���? ?��?�� ???��
        String metaFileCreatedTime = metaFileSdf.format(today);
        String crawledTime = sdf.format(today);
        String metaDbCreatedTime = metaDbSdf.format(today);
        //DB ?��결객�?
        Connection connection = connDB();
        //CID�? cid?�� 고정?���? aid값으�? ?��?��?�� 증�??��?��?��.
        //CID?�� ?��본파?�� 개수 ?��?��?�� ?��?��?�� ???��?��?��.
        int CidNum = 1;
        //AID?�� 분할?�� ?��?�� ?��?��?�� ?��개씩 ???��?��?��.
        int Aidnum = 1;

        //Set?���? Dir리스?���? 받아?��?��.
        //Set?? 중복?�� ?���? ?��?��?�� 고로 ?��경로?�� ?��?��?���? 받아?��?��.
        //메소?�� ?��?���?보면 getParent?��쩌고�? ?��?��?��?�� 최종?��?��?�� �?모경�? 마�?막폴?�� 리스?���? �??��?���? ?��?��?��?��?��?��
        Set<String> dirList = new HashSet<String>();
        getDirList(basePath, dirList);

        for(String tmpDir : dirList){
            List<String> fileList = new ArrayList<String>();
            MetaDBVo metaDBVo = null;
            //?��?��?���? 리스?��말고 ?��?��리스?���? ?��캔하?�� ?��?��?��?�� 목록?�� 받아?��?��.
            getFileList(tmpDir, fileList);

            //�? ?��?��?��?��?��?�� metainfo.json?��?��?�� ?��?�� MetaDbMetaInfoVo객체 ?�� ?��?��
            MetaDbMetaInfoVo metaDbMetaInfoVo = gson.fromJson(FileUtils.readFileToString(new File(tmpDir+"/metainfo.json"), "UTF-8"), new TypeToken<MetaDbMetaInfoVo>(){}.getType());

            for(String tmpFile : fileList){
                File f = new File(tmpFile);

                //DatasolutionDatalakesSplit?���? ?��?��?��?�� 분할?�� csv?��?��?�� 복사?��?�� datalake?��?�� ?��?��?�� 맞게 �??��?��?�� 카피?��?��.
                FileUtils.copyFile(new File(tmpDir+"/"+tmpFile),
                        new File(outputBasePath+"/"+metaDbMetaInfoVo.getDID()+"/"+crawledTime+"/"+CidNum+"/"+Aidnum+"/"
                                +metaDbMetaInfoVo.getDID()+"-"+crawledTime.replaceAll("/","-")+"-"+CidNum+"-"+Aidnum
                                +"."+f.getName().substring(f.getName().lastIndexOf(".")+1)));

                //?��?�� 복사?? ?��?��?�� 메�??��?��?��?�� ?��?��.
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

                //MetaDB?�� ?��?���? ?��?��?�� ?��?��?���??��.
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

                //메�??��?�� ?��주고
                FileUtils.writeStringToFile(new File(outputBasePath+"/"+metaDbMetaInfoVo.getDID()+"/"+crawledTime+"/"+CidNum+"/"+Aidnum+"/"+metaDbMetaInfoVo.getDID()+"-"+crawledTime.replaceAll("/","-")+"-"+CidNum+"-"+Aidnum+".meta"),gson.toJson(metaFileVo), "UTF-8");
                //?��?��?�� DB?�� ?���?
                insertData(connection, metaDBVo);
                //AID?��린다.
                Aidnum++;
            }

            System.out.println(gson.toJson(metaDBVo));

            //?�� ?��?�� ?�� ?���? aid?�� ?��?�� 1�? 초기?��
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
