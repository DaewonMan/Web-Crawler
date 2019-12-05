package kr.datasolution.lake.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("Duplicates")
/**
 * csv를 Json으로 바꾸는 메소드
 * 폴더 스캔 후 인코딩 변경
 * csv -> json으로 변경
 * 원본 csv파일 삭제 순으로 진행된다.
 */
public class CsvToJsonMain {

    public static void main(String[] args) throws Exception{
        //String basePath = "C:\\Users\\chyh0\\Downloads\\FAOSTAT";
        String basePath = "C:\\Users\\chyh0\\Downloads\\FAOSTAT_JSON";

//        List<String> fileList = new ArrayList<String>();
//        Set<String> fileDirList = new HashSet<String>();
//        subDirList(basePath, fileList, fileDirList);
//
//        for(String tmp : fileList){
//            changeEncoding(tmp);
//        }
//
//        for(String tmp : fileList){
//            csv2Json(tmp);
//        }
//
//        for(String tmp : fileDirList){
//            deleteFileList(tmp);
//        }


    }

    public static void subDirList(String source, List<String> filePathList, Set<String> fileDirList){
        File dir = new File(source);
        File[] fileList = dir.listFiles();

        try{
            for(int i = 0 ; i < fileList.length ; i++){
                File file = fileList[i];
                if(file.isFile()){
                    if(file.getName().contains("DatasolutionDatalakesSplit")){
                        filePathList.add(file.getCanonicalPath());
                    }
                    fileDirList.add(file.getParentFile().toString());
                }else if(file.isDirectory()){
                    subDirList(file.getCanonicalPath().toString(), filePathList, fileDirList);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    /**
     *포맷 변경이 완료된 파일은 원본 csv파일을 삭제한다.
     */
    public static void deleteFileList(String source){
        File dir = new File(source);
        File[] fileList = dir.listFiles();

        try{
            for(int i = 0 ; i < fileList.length ; i++){
                File file = fileList[i];
                if(file.isFile()){
                    if(file.getName().substring(file.getName().lastIndexOf(".")+1).equalsIgnoreCase("csv")){
                        file.delete();
                    }
                }else if(file.isDirectory()){
                    deleteFileList(file.getCanonicalPath().toString());
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    /**
     * 인코딩 변경
     * ANSI로 이루어진 문서를 UTF-8로 변경한다.
     * 밑에 csv2Json메소드에서 사용하는 csvSchema가 ansi를 지원안하는듯
     * @param filePath
     * @throws Exception
     */
    public static void changeEncoding(String filePath) throws Exception{
        StringBuffer sb = new StringBuffer();
        sb.append(FileUtils.readFileToString(new File(filePath)));
        FileUtils.writeStringToFile(new File(filePath), sb.toString(), "UTF-8");
    }

    public static void csv2Json(String filePath) throws Exception{
        File input = new File(filePath);

        String outputFilePath = input.getParentFile().toString();
        String outFileName = input.getName().substring(0, input.getName().lastIndexOf("."));
        File output = new File(outputFilePath+"/"+outFileName+".json");

        CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build();
        CsvMapper csvMapper = new CsvMapper();

        List<Object> readAll = csvMapper.readerFor(Map.class).with(csvSchema).readValues(input).readAll();

        ObjectMapper mapper = new ObjectMapper();

        mapper.writerWithDefaultPrettyPrinter().writeValue(output, readAll);

    }
}
