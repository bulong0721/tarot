package com.myee.tarot.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Date;

/**
 * Created by jelynn on 2016/5/23.
 */
public class FileUtils {

	private final static Logger logger = LoggerFactory.getLogger(FileUtils.class);
    public static long copyFile(File srcFile, File desFile) throws Exception {
        long time = new Date().getTime();
        int length = 2097152;
        FileInputStream in = new FileInputStream(srcFile);
        FileOutputStream out = new FileOutputStream(desFile);
        byte[] buffer = new byte[length];
        while (true) {
            int ins = in.read(buffer);
            if (ins == -1) {
                in.close();
                out.flush();
                out.close();
                return new Date().getTime() - time;
            } else
                out.write(buffer, 0, ins);
        }
    }

    /**
     * 将版本信息写入本地文件
     *
     * @param text
     * @param path
     */
    public static void writeToTXT(String text, String path) {
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                createFileAndDir(path);
            }
            fos = new FileOutputStream(file);
            byte[] bytes = text.getBytes();
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
			logger.error(" write version info file ", e);
        } catch (IOException e) {
			logger.error(" write version info file ", e);
        } catch (Exception e) {
			logger.error(" write version info file ", e);
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
					logger.error(" write version info file ", e);
                }
            }
        }
    }

    /**
     * 读取txt文件中的内容
     *
     * @param path
     * @return
     */
    public static String readTXT(String path) {
		logger.info("readTXT  file path={}", path);
        String res = "";
        try {
            File file = new File(path);
            if (file.exists()) {
				FileInputStream fis = new FileInputStream(path);
				int length = fis.available();
				byte[] buffer = new byte[length];
				fis.read(buffer);
				res = new String(buffer, "UTF-8");
            }
        } catch (FileNotFoundException e) {
			logger.error(" read version txt error ", e);
        } catch (Exception e) {
			logger.error(" read version txt error ", e);
        }
        return res;
    }

    private static void createFileAndDir(String path) {
        File file = new File(path);
        String dir = path.substring(0, path.lastIndexOf("/"));
        File fileDir = new File(dir);
        fileDir.mkdirs();
        try {
            file.createNewFile();
        } catch (Exception e) {
			logger.error(" create file error ", e);
        }
    }
}
