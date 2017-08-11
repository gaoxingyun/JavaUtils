package com.xy.util;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * 压缩工具类
 * Created by xy on 2017/6/27.
 */
public class CompressUtils {


    /**
     * 把zip文件解压到指定的文件夹
     * @param zipFilePathName   zip文件路径, 如 "D:/test/aa.zip"
     * @param saveFileDir   解压后的文件存放路径, 如"D:/test/"
     */
    public static void decompressZip(String zipFilePathName,String zipEncoding,String saveFileDir) {

        if(!isEndsWithZip(zipFilePathName)){
            throw new RuntimeException("file is not endwith zip!");
        }

        File file = new File(zipFilePathName);
        if(!file.exists()){
            throw new RuntimeException("file is not exist!");
        }

        ZipFile zipFile = null;
        InputStream is = null;
        OutputStream os = null;

        try {
            zipFile = new ZipFile(file, zipEncoding);
            Enumeration<ZipArchiveEntry> ZipArchiveEntrys = zipFile.getEntries();
            ZipArchiveEntry archiveEntry = null;
            while ((archiveEntry = ZipArchiveEntrys.nextElement()) != null){
                try {
                    is = zipFile.getInputStream(archiveEntry);
                    byte[] content = IOUtils.toByteArray(is);
                    String entryFileName = archiveEntry.getName();
                    String entryFilePath = saveFileDir + entryFileName;
                    File entryFile = new File(entryFilePath);
                    os = new BufferedOutputStream(new FileOutputStream(entryFile));
                    os.write(content);
                }catch(IOException e) {
                    throw new IOException(e);
                }finally {
                    if(os != null) {
                        os.flush();
                        os.close();
                    }
                    if(is != null){
                        is.close();
                    }
                }
            }
        }catch (NoSuchElementException e){
            // 最后一次读取不到实体异常，忽略
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 判断文件名是否以.zip为后缀
     * @param fileName        需要判断的文件名
     * @return 是zip文件返回true,否则返回false
     */
    public static boolean isEndsWithZip(String fileName) {
        boolean flag = false;
        if(fileName != null && !"".equals(fileName.trim())) {
            if(fileName.endsWith(".ZIP")||fileName.endsWith(".zip")){
                flag = true;
            }
        }
        return flag;
    }


}
