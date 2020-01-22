package com.dwm.webcrawler.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("Duplicates")
/**
 * csvÎ•? Json?úºÎ°? Î∞îÍæ∏?äî Î©îÏÜå?ìú
 * ?è¥?çî ?ä§Ï∫? ?õÑ ?ù∏ÏΩîÎî© Î≥?Í≤?
 * csv -> json?úºÎ°? Î≥?Í≤?
 * ?õêÎ≥? csv?åå?ùº ?Ç≠?†ú ?àú?úºÎ°? ÏßÑÌñâ?êú?ã§.
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
     *?è¨Îß? Î≥?Í≤ΩÏù¥ ?ôÑÎ£åÎêú ?åå?ùº?? ?õêÎ≥? csv?åå?ùº?ùÑ ?Ç≠?†ú?ïú?ã§.
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
     * ?ù∏ÏΩîÎî© Î≥?Í≤?
     * ANSIÎ°? ?ù¥Î£®Ïñ¥Ïß? Î¨∏ÏÑúÎ•? UTF-8Î°? Î≥?Í≤ΩÌïú?ã§.
     * Î∞ëÏóê csv2JsonÎ©îÏÜå?ìú?óê?Ñú ?Ç¨?ö©?ïò?äî csvSchemaÍ∞? ansiÎ•? Ïß??õê?ïà?ïò?äî?ìØ
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
