package com.myee.tarot.core.util;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

/**
 * Created by Chay on 2016/7/13.
 */
public class ZipUtil {

    /**
     * 源头文件的文件名不能包含空格，否则会被认为是多个关键字，而导致压缩时找不到源文件、压缩文件为空
     * @param srcPathName
     * @param srcFileName
     * @param targetPathName
     */
    public static void compress(String srcPathName,String srcFileName,String targetPathName) {
        File zipFile = new File(targetPathName);
        String srcPath = srcPathName.replaceAll(srcFileName, "");
        File srcdir = new File(srcPath);
        if (!srcdir.exists()) {
            throw new RuntimeException(srcPath + "不存在！");
        }
        Project prj = new Project();
        Zip zip = new Zip();
        zip.setProject(prj);
        zip.setDestFile(zipFile);
        FileSet fileSet = new FileSet();
        fileSet.setProject(prj);
        fileSet.setDir(srcdir);
        //fileSet.setIncludes("**/*.java"); 包括哪些文件或文件夹 eg:zip.setIncludes("*.java");
        //fileSet.setExcludes(...); 排除哪些文件或文件夹
        fileSet.setIncludes(srcFileName);//文件名不能包含空格，否则会被认为是多个关键字
        zip.addFileset(fileSet);

        zip.execute();
    }

}
