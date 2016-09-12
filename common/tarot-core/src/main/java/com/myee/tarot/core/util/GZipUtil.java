package com.myee.tarot.core.util;

//import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;

/**
 * Created by Chay on 2016/7/12.
 */
public class GZipUtil {
    /*
    * gzip压缩
    * */

    /**
     * Perform file compression.
     * @param inFileName Name of the file to be compressed
     */
    public static void doCompressFile(String inFileName) {

        try {

            System.out.println("Creating the GZIP output stream.");
            String outFileName = inFileName + ".gz";
            GZIPOutputStream out = null;
            try {
                out = new GZIPOutputStream(new FileOutputStream(outFileName));
            } catch(FileNotFoundException e) {
                System.err.println("Could not create file: " + outFileName);
                System.exit(1);
            }


            System.out.println("Opening the input file.");
            FileInputStream in = null;
            try {
                in = new FileInputStream(inFileName);
            } catch (FileNotFoundException e) {
                System.err.println("File not found. " + inFileName);
                System.exit(1);
            }

            System.out.println("Transfering bytes from input file to GZIP Format.");
            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();

            System.out.println("Completing the GZIP file");
            out.finish();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public static void doCompressFile(File source,File output) {

        try {

            System.out.println("Creating the GZIP output stream.");
//            String outFileName = output.getName();
            GZIPOutputStream out = null;
            File tempFile = new File(output.getPath());
            String tempFilePath = tempFile.getPath().replaceAll(output.getName(), "");//把路径中的反斜杠替换成斜杠
//            System.out.println(tempFilePath);
//            tempFile = FileUtils.getFile(tempFilePath,source.getName()+".gz");
//            System.out.println(tempFile.getPath());

//            FileUtils.copyFile(output,tempFile);
//            System.out.println(tempFile.getPath());
//            System.out.println(tempFile.getName());
            try {
                out = new GZIPOutputStream(new FileOutputStream(tempFile));
            } catch(FileNotFoundException e) {
                System.err.println("Could not create file: " + tempFile.getName());
                System.exit(1);
            }


            System.out.println("Opening the input file.");
            FileInputStream in = null;
            try {
                in = new FileInputStream(source);
            } catch (FileNotFoundException e) {
                System.err.println("File not found. " + source.getName());
                System.exit(1);
            }

            System.out.println("Transfering bytes from input file to GZIP Format.");
            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();

            System.out.println("Completing the GZIP file");
            out.finish();
            out.close();

            //GZip压缩里面和外面的文件名会一样，所以不适合
//            String parent = tempFile.getParent();
//            File newFile = new File(parent, output.getName());
//            if (tempFile.exists()) {
//                tempFile.renameTo(newFile);
//            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }



    /**
     * Sole entry point to the class and application.
     * @param args Array of String arguments.
     */
//    public static void main(String[] args) {
//
//        if (args.length != 1) {
//            System.err.println("Usage: java CompressFileGZIP filename");
//        } else {
//            doCompressFile(args[0]);
//        }
//
//
//    }


    /**
     * -----------------------------------------------------------------------------
     * Used to provide an example of uncompressing a file in the GZIP Format.
     *
     * @version 1.0
     * @author  Jeffrey M. Hunter  (jhunter@idevelopment.info)
     * @author  http://www.idevelopment.info
     * -----------------------------------------------------------------------------
     */


    /**
     * Uncompress the incoming file.
     * @param inFileName Name of the file to be uncompressed
     */
    public static void doUncompressFile(String inFileName) {

        try {

            if (!getExtension(inFileName).equalsIgnoreCase("gz")) {
                System.err.println("File name must have extension of \".gz\"");
                System.exit(1);
            }

            System.out.println("Opening the compressed file.");
            GZIPInputStream in = null;
            try {
                in = new GZIPInputStream(new FileInputStream(inFileName));
            } catch(FileNotFoundException e) {
                System.err.println("File not found. " + inFileName);
                System.exit(1);
            }

            System.out.println("Open the output file.");
            String outFileName = getFileName(inFileName);
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(outFileName);
            } catch (FileNotFoundException e) {
                System.err.println("Could not write to file. " + outFileName);
                System.exit(1);
            }

            System.out.println("Transfering bytes from compressed file to the output file.");
            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            System.out.println("Closing the file and stream");
            in.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * Used to extract and return the extension of a given file.
     * @param f Incoming file to get the extension of
     * @return <code>String</code> representing the extension of the incoming
     *         file.
     */
    public static String getExtension(String f) {
        String ext = "";
        int i = f.lastIndexOf('.');

        if (i > 0 &&  i < f.length() - 1) {
            ext = f.substring(i+1);
        }
        return ext;
    }

    /**
     * Used to extract the filename without its extension.
     * @param f Incoming file to get the filename
     * @return <code>String</code> representing the filename without its
     *         extension.
     */
    public static String getFileName(String f) {
        String fname = "";
        int i = f.lastIndexOf('.');

        if (i > 0 &&  i < f.length() - 1) {
            fname = f.substring(0,i);
        }
        return fname;
    }

    /**
     * Sole entry point to the class and application.
     * @param args Array of String arguments.
     */
//    public static void main(String[] args) {
//
//        if (args.length != 1) {
//            System.err.println("Usage: java UncompressFileGZIP gzipfile");
//        } else {
//            doUncompressFile(args[0]);
//        }
//
//    }

}
