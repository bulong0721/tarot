package com.myee.tarot.web.apiold.housekeeper;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by Ray.Fu on 2016/7/15.
 */
@Controller
public class WebAppController {

    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;

    /**
     * 缓存上传接口:由某店铺下PC发起缓存请求。
     * @param orgID 店铺ID
     * @param entry 缓存key，供查询使用
     * @param value 缓存的值value
     * @param resp
     * @return
     */
    @RequestMapping(value = "api/catchBytes/save")
    @ResponseBody
    public String catchToFile(@RequestParam("orgID") Long orgID, @RequestParam("entry") String entry, @RequestParam("value") byte[] value, HttpServletResponse resp) {
        try {
            if(value.length > 100000){
                return "错误:数据大小超过100KB限制";
            }
            String fileName = File.separator + entry + ".bin";
            File dest = FileUtils.getFile(DOWNLOAD_HOME, String.valueOf(orgID), File.separator + "catch");
            dest.mkdirs();
            dest = FileUtils.getFile(dest.getPath(), fileName);

            FileOutputStream fos  =   new  FileOutputStream( dest );
            OutputStreamWriter osw  =   new OutputStreamWriter(fos,  "UTF-8" );
            System.out.println(new String(value,"UTF-8"));
            osw.write(new String(value,"UTF-8"));
            osw.flush();

            return "缓存成功";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "缓存失败";
    }

    /**
     * 缓存读取接口:由某店铺下PC发起读取缓存请求。
     * @param orgID 店铺ID
     * @param entry 缓存key，供查询使用
     * @param resp 读取的缓存的值
     * @return
     */
    @RequestMapping(value = "api/catchBytes/get")
    public void readCatchFile(@RequestParam("orgID") Long orgID, @RequestParam("entry") String entry, HttpServletResponse resp) {
        FileInputStream fis = null;
        String fileContent = "";
        byte[] result = null;
        try {

            String fileName = File.separator + entry + ".bin";
            File dest = FileUtils.getFile(DOWNLOAD_HOME, String.valueOf(orgID),File.separator + "catch" +fileName);

            if(dest.exists()){
                fis  =   new  FileInputStream( dest );
                InputStreamReader isr  =   new  InputStreamReader(fis,  "UTF-8" );
                BufferedReader br  =   new  BufferedReader(isr);
                String line  =   null ;
                while  ((line  =  br.readLine())  !=   null ) {
                    fileContent  +=  line;
                }
                result = fileContent.getBytes("UTF8");
            }

            OutputStream os = new BufferedOutputStream(resp.getOutputStream());
            resp.setHeader("Content-type", "application/octet-stream;charset=utf8");
            os.write(result);// 输出文件
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e1) {
                }
            }
        }
    }

}
