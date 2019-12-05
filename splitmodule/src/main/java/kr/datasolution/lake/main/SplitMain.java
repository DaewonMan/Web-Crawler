package kr.datasolution.lake.main;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 파일 쪼개기
 * 제일 첫 번째 단계로 이루어져야 한다.
 */
public class SplitMain {

    public static void main(String[] args) throws Exception{
        String basePath = "C:\\Users\\chyh0\\Downloads\\FAOSTAT";

        List<String> filePathList = new ArrayList<String>();
        subDirList(basePath, filePathList);

        //디렉토리 스캔 후 파일 사이즈 측정 4MB이상의 파일은 split한다
        for(String tmpFileList : filePathList){
            File f = new File(tmpFileList);
            if(f.length() >= 4591456){
                System.out.println(tmpFileList + ":: 파일 겁나 큼");
                splitFile(tmpFileList);
            } else{
                File file = new File(tmpFileList);
                //파일 사이즈가 4MB이하면 원본파일을 복사하여 DatasolutionDatalakesSplit_으로 시작하는 파일로 제작한다.
                FileUtils.copyFile(new File(tmpFileList), new File(file.getParentFile().toString()+"/DatasolutionDatalakesSplit_"+file.getName()+"_0.csv"));
                System.out.println(tmpFileList + ":: 이정도는 괜찮음");
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
     * 파일 쪼개는 메소드
     *
     * @param filePath
     * @throws Exception
     */
    public static void splitFile(String filePath) throws Exception{
        String encoding = "UTF-8";
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            //대용량 파일의 경우 FileUtils를 사용한 읽기가 불가능하므로 Stream+BufferReader을 사용한 한줄씩 읽기를진행한다.
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), encoding));
            String header = "";
            int lineCnt = 0;
            int count = 0;
            int headerCount = 0;
            StringBuffer tmpString = new StringBuffer();
            for (String line; (line = reader.readLine()) != null;) {
                //해당 메소드는 파일 단위로 반복되므로 파일의 첫번째는 헤더로 인식 헤더는 쪼갠파일마다 삽입되어야하므로 임시저장
                if(headerCount == 0){
                    header = line;
                    headerCount = -1; //헤더카운트 다시 못쓰게 -1로 변경
                }

                if(lineCnt == 0){
//                    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path +"/"+fname+"_"+count+".csv"), encoding));
                    if(count != 0) {
                        //String 보다 StringBuffer을 사용하여 속도를 증가시킨다 거의 100배이상 차이 남
                        //제일 처음 실행된 헤더를 각 파일의 첫번째 구간에 삽입한다.
                        tmpString.append(header+"\n");
//                        writer.write(header);
//                        writer.newLine();
                    }
                }

//                writer.write(line);
//                writer.newLine();
                //원문에서 한줄씩 읽어들인 파일을 계속 stringbuffer객체에 append시킨다.
                tmpString.append(line+"\n");

                lineCnt++;

                //사이즈보다는 문자 길이로 판단, 4MB이상의 길이의 파일이 될 경우 따로 저장시키고 각종 설정값들을 초기화 시켜준다.
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

            //tmpstring에 저장된 문자열 내용을 FileUtils를 이용하여 쓴다.
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
