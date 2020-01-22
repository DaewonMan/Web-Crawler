package com.dwm.webcrawler.main;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ?��?�� 쪼개�?
 * ?��?�� �? 번째 ?��계로 ?��루어?��?�� ?��?��.
 */
public class SplitMain {

    public static void main(String[] args) throws Exception{
        String basePath = "C:\\Users\\chyh0\\Downloads\\FAOSTAT";

        List<String> filePathList = new ArrayList<String>();
        subDirList(basePath, filePathList);

        //?��?��?���? ?���? ?�� ?��?�� ?��?���? 측정 4MB?��?��?�� ?��?��?? split?��?��
        for(String tmpFileList : filePathList){
            File f = new File(tmpFileList);
            if(f.length() >= 4591456){
                System.out.println(tmpFileList + ":: ?��?�� 겁나 ?��");
                splitFile(tmpFileList);
            } else{
                File file = new File(tmpFileList);
                //?��?�� ?��?��즈�? 4MB?��?���? ?��본파?��?�� 복사?��?�� DatasolutionDatalakesSplit_?���? ?��?��?��?�� ?��?���? ?��?��?��?��.
                FileUtils.copyFile(new File(tmpFileList), new File(file.getParentFile().toString()+"/DatasolutionDatalakesSplit_"+file.getName()+"_0.csv"));
                System.out.println(tmpFileList + ":: ?��?��?��?�� 괜찮?��");
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
     * ?��?�� 쪼개?�� 메소?��
     *
     * @param filePath
     * @throws Exception
     */
    public static void splitFile(String filePath) throws Exception{
        String encoding = "UTF-8";
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            //???��?�� ?��?��?�� 경우 FileUtils�? ?��?��?�� ?��기�? 불�??��?���?�? Stream+BufferReader?�� ?��?��?�� ?��줄씩 ?��기�?�진?��?��?��.
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), encoding));
            String header = "";
            int lineCnt = 0;
            int count = 0;
            int headerCount = 0;
            StringBuffer tmpString = new StringBuffer();
            for (String line; (line = reader.readLine()) != null;) {
                //?��?�� 메소?��?�� ?��?�� ?��?���? 반복?���?�? ?��?��?�� 첫번째는 ?��?���? ?��?�� ?��?��?�� 쪼갠?��?��마다 ?��?��?��?��?��?���?�? ?��?��???��
                if(headerCount == 0){
                    header = line;
                    headerCount = -1; //?��?��카운?�� ?��?�� 못쓰�? -1�? �?�?
                }

                if(lineCnt == 0){
//                    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path +"/"+fname+"_"+count+".csv"), encoding));
                    if(count != 0) {
                        //String 보다 StringBuffer?�� ?��?��?��?�� ?��?���? 증�??��?��?�� 거의 100배이?�� 차이 ?��
                        //?��?�� 처음 ?��?��?�� ?��?���? �? ?��?��?�� 첫번�? 구간?�� ?��?��?��?��.
                        tmpString.append(header+"\n");
//                        writer.write(header);
//                        writer.newLine();
                    }
                }

//                writer.write(line);
//                writer.newLine();
                //?��문에?�� ?��줄씩 ?��?��?��?�� ?��?��?�� 계속 stringbuffer객체?�� append?��?��?��.
                tmpString.append(line+"\n");

                lineCnt++;

                //?��?��즈보?��?�� 문자 길이�? ?��?��, 4MB?��?��?�� 길이?�� ?��?��?�� ?�� 경우 ?���? ???��?��?���? 각종 ?��?��값들?�� 초기?�� ?��켜�??��.
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

            //tmpstring?�� ???��?�� 문자?�� ?��?��?�� FileUtils�? ?��?��?��?�� ?��?��.
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
