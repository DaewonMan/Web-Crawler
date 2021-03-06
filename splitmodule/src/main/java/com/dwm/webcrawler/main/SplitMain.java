package com.dwm.webcrawler.main;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ??ผ ์ชผ๊ฐ๊ธ?
 * ? ?ผ ์ฒ? ๋ฒ์งธ ?จ๊ณ๋ก ?ด๋ฃจ์ด? ธ?ผ ??ค.
 */
public class SplitMain {

    public static void main(String[] args) throws Exception{
        String basePath = "C:\\Users\\chyh0\\Downloads\\FAOSTAT";

        List<String> filePathList = new ArrayList<String>();
        subDirList(basePath, filePathList);

        //?? ? ๋ฆ? ?ค์บ? ? ??ผ ?ฌ?ด์ฆ? ์ธก์  4MB?ด?? ??ผ?? split??ค
        for(String tmpFileList : filePathList){
            File f = new File(tmpFileList);
            if(f.length() >= 4591456){
                System.out.println(tmpFileList + ":: ??ผ ๊ฒ๋ ?ผ");
                splitFile(tmpFileList);
            } else{
                File file = new File(tmpFileList);
                //??ผ ?ฌ?ด์ฆ๊? 4MB?ด?๋ฉ? ?๋ณธํ?ผ? ๋ณต์ฌ??ฌ DatasolutionDatalakesSplit_?ผ๋ก? ???? ??ผ๋ก? ? ???ค.
                FileUtils.copyFile(new File(tmpFileList), new File(file.getParentFile().toString()+"/DatasolutionDatalakesSplit_"+file.getName()+"_0.csv"));
                System.out.println(tmpFileList + ":: ?ด? ?? ๊ด์ฐฎ?");
            }
        }

    }

    public static void subDirList(String source, List<String> filePathList){
        File dir = new File(source);
        File[] fileList = dir.listFiles();

        try{
            for(int i = 0 ; i < fileList.length ; i++){
                File file = fileList[i];
                if(file.isFile()){
                    filePathList.add(file.getCanonicalPath());
                }else if(file.isDirectory()){
                    subDirList(file.getCanonicalPath().toString(), filePathList);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    /**
     * ??ผ ์ชผ๊ฐ? ๋ฉ์?
     *
     * @param filePath
     * @throws Exception
     */
    public static void splitFile(String filePath) throws Exception{
        String encoding = "UTF-8";
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            //???ฉ? ??ผ? ๊ฒฝ์ฐ FileUtils๋ฅ? ?ฌ?ฉ? ?ฝ๊ธฐ๊? ๋ถ๊??ฅ?๋ฏ?๋ก? Stream+BufferReader? ?ฌ?ฉ? ?์ค์ฉ ?ฝ๊ธฐ๋?ผ์ง???ค.
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), encoding));
            String header = "";
            int lineCnt = 0;
            int count = 0;
            int headerCount = 0;
            StringBuffer tmpString = new StringBuffer();
            for (String line; (line = reader.readLine()) != null;) {
                //?ด?น ๋ฉ์?? ??ผ ?จ?๋ก? ๋ฐ๋ณต?๋ฏ?๋ก? ??ผ? ์ฒซ๋ฒ์งธ๋ ?ค?๋ก? ?ธ? ?ค?? ์ชผ๊ฐ ??ผ๋ง๋ค ?ฝ???ด?ผ?๋ฏ?๋ก? ?????ฅ
                if(headerCount == 0){
                    header = line;
                    headerCount = -1; //?ค?์นด์ด?ธ ?ค? ๋ชป์ฐ๊ฒ? -1๋ก? ๋ณ?๊ฒ?
                }

                if(lineCnt == 0){
//                    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path +"/"+fname+"_"+count+".csv"), encoding));
                    if(count != 0) {
                        //String ๋ณด๋ค StringBuffer? ?ฌ?ฉ??ฌ ??๋ฅ? ์ฆ๊???จ?ค ๊ฑฐ์ 100๋ฐฐ์ด? ์ฐจ์ด ?จ
                        //? ?ผ ์ฒ์ ?ค?? ?ค?๋ฅ? ๊ฐ? ??ผ? ์ฒซ๋ฒ์ง? ๊ตฌ๊ฐ? ?ฝ???ค.
                        tmpString.append(header+"\n");
//                        writer.write(header);
//                        writer.newLine();
                    }
                }

//                writer.write(line);
//                writer.newLine();
                //?๋ฌธ์? ?์ค์ฉ ?ฝ?ด?ค?ธ ??ผ? ๊ณ์ stringbuffer๊ฐ์ฒด? append??จ?ค.
                tmpString.append(line+"\n");

                lineCnt++;

                //?ฌ?ด์ฆ๋ณด?ค? ๋ฌธ์ ๊ธธ์ด๋ก? ??จ, 4MB?ด?? ๊ธธ์ด? ??ผ?ด ?  ๊ฒฝ์ฐ ?ฐ๋ก? ???ฅ??ค๊ณ? ๊ฐ์ข ?ค? ๊ฐ๋ค? ์ด๊ธฐ? ?์ผ์??ค.
                if(tmpString.length() >= 4591456){
                    File file = new File(filePath);
                    String path = file.getParentFile().toString();
                    String fname = file.getName().substring(0, file.getName().lastIndexOf("."));
                    FileUtils.writeStringToFile(new File(path +"/DatasolutionDatalakesSplit_"+fname+"_"+count+".csv"), tmpString.toString(), encoding);
                    tmpString = new StringBuffer();
                    count++;
                    lineCnt = 0;
                }
            }

            //tmpstring? ???ฅ? ๋ฌธ์?ด ?ด?ฉ? FileUtils๋ฅ? ?ด?ฉ??ฌ ?ด?ค.
            File file = new File(filePath);
            String path = file.getParentFile().toString();
            String fname = file.getName().substring(0, file.getName().lastIndexOf("."));
            FileUtils.writeStringToFile(new File(path +"/DatasolutionDatalakesSplit_"+fname+"_"+count+".csv"), tmpString.toString(), encoding);
            count++;
        } catch (Exception e){
            e.printStackTrace();
        } finally {
//            writer.close();
//            reader.close();
        }
    }
}
