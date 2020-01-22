package com.dwm.webcrawler.main;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ?åå?ùº Ï™ºÍ∞úÍ∏?
 * ?†ú?ùº Ï≤? Î≤àÏß∏ ?ã®Í≥ÑÎ°ú ?ù¥Î£®Ïñ¥?†∏?ïº ?ïú?ã§.
 */
public class SplitMain {

    public static void main(String[] args) throws Exception{
        String basePath = "C:\\Users\\chyh0\\Downloads\\FAOSTAT";

        List<String> filePathList = new ArrayList<String>();
        subDirList(basePath, filePathList);

        //?îî?†â?Ü†Î¶? ?ä§Ï∫? ?õÑ ?åå?ùº ?Ç¨?ù¥Ï¶? Ï∏°Ï†ï 4MB?ù¥?ÉÅ?ùò ?åå?ùº?? split?ïú?ã§
        for(String tmpFileList : filePathList){
            File f = new File(tmpFileList);
            if(f.length() >= 4591456){
                System.out.println(tmpFileList + ":: ?åå?ùº Í≤ÅÎÇò ?Åº");
                splitFile(tmpFileList);
            } else{
                File file = new File(tmpFileList);
                //?åå?ùº ?Ç¨?ù¥Ï¶àÍ? 4MB?ù¥?ïòÎ©? ?õêÎ≥∏Ìåå?ùº?ùÑ Î≥µÏÇ¨?ïò?ó¨ DatasolutionDatalakesSplit_?úºÎ°? ?ãú?ûë?ïò?äî ?åå?ùºÎ°? ?†ú?ûë?ïú?ã§.
                FileUtils.copyFile(new File(tmpFileList), new File(file.getParentFile().toString()+"/DatasolutionDatalakesSplit_"+file.getName()+"_0.csv"));
                System.out.println(tmpFileList + ":: ?ù¥?†ï?èÑ?äî Í¥úÏ∞Æ?ùå");
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
     * ?åå?ùº Ï™ºÍ∞ú?äî Î©îÏÜå?ìú
     *
     * @param filePath
     * @throws Exception
     */
    public static void splitFile(String filePath) throws Exception{
        String encoding = "UTF-8";
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            //???ö©?üâ ?åå?ùº?ùò Í≤ΩÏö∞ FileUtilsÎ•? ?Ç¨?ö©?ïú ?ùΩÍ∏∞Í? Î∂àÍ??ä•?ïòÎØ?Î°? Stream+BufferReader?ùÑ ?Ç¨?ö©?ïú ?ïúÏ§ÑÏî© ?ùΩÍ∏∞Î?ºÏßÑ?ñâ?ïú?ã§.
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), encoding));
            String header = "";
            int lineCnt = 0;
            int count = 0;
            int headerCount = 0;
            StringBuffer tmpString = new StringBuffer();
            for (String line; (line = reader.readLine()) != null;) {
                //?ï¥?ãπ Î©îÏÜå?ìú?äî ?åå?ùº ?ã®?úÑÎ°? Î∞òÎ≥µ?êòÎØ?Î°? ?åå?ùº?ùò Ï≤´Î≤àÏß∏Îäî ?ó§?çîÎ°? ?ù∏?ãù ?ó§?çî?äî Ï™ºÍ∞†?åå?ùºÎßàÎã§ ?ÇΩ?ûÖ?êò?ñ¥?ïº?ïòÎØ?Î°? ?ûÑ?ãú???û•
                if(headerCount == 0){
                    header = line;
                    headerCount = -1; //?ó§?çîÏπ¥Ïö¥?ä∏ ?ã§?ãú Î™ªÏì∞Í≤? -1Î°? Î≥?Í≤?
                }

                if(lineCnt == 0){
//                    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path +"/"+fname+"_"+count+".csv"), encoding));
                    if(count != 0) {
                        //String Î≥¥Îã§ StringBuffer?ùÑ ?Ç¨?ö©?ïò?ó¨ ?Üç?èÑÎ•? Ï¶ùÍ??ãú?Ç®?ã§ Í±∞Ïùò 100Î∞∞Ïù¥?ÉÅ Ï∞®Ïù¥ ?Ç®
                        //?†ú?ùº Ï≤òÏùå ?ã§?ñâ?êú ?ó§?çîÎ•? Í∞? ?åå?ùº?ùò Ï≤´Î≤àÏß? Íµ¨Í∞Ñ?óê ?ÇΩ?ûÖ?ïú?ã§.
                        tmpString.append(header+"\n");
//                        writer.write(header);
//                        writer.newLine();
                    }
                }

//                writer.write(line);
//                writer.newLine();
                //?õêÎ¨∏Ïóê?Ñú ?ïúÏ§ÑÏî© ?ùΩ?ñ¥?ì§?ù∏ ?åå?ùº?ùÑ Í≥ÑÏÜç stringbufferÍ∞ùÏ≤¥?óê append?ãú?Ç®?ã§.
                tmpString.append(line+"\n");

                lineCnt++;

                //?Ç¨?ù¥Ï¶àÎ≥¥?ã§?äî Î¨∏Ïûê Í∏∏Ïù¥Î°? ?åê?ã®, 4MB?ù¥?ÉÅ?ùò Í∏∏Ïù¥?ùò ?åå?ùº?ù¥ ?ê† Í≤ΩÏö∞ ?î∞Î°? ???û•?ãú?Ç§Í≥? Í∞ÅÏ¢Ö ?Ñ§?†ïÍ∞íÎì§?ùÑ Ï¥àÍ∏∞?ôî ?ãúÏºúÏ??ã§.
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

            //tmpstring?óê ???û•?êú Î¨∏Ïûê?ó¥ ?Ç¥?ö©?ùÑ FileUtilsÎ•? ?ù¥?ö©?ïò?ó¨ ?ì¥?ã§.
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
