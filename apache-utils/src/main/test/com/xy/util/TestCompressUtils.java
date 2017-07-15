package com.xy.util;

import com.xy.util.CompressUtils;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import java.io.*;

/**
 * 压缩工具类
 * Created by xy on 2017/6/27.
 */
public class TestCompressUtils {


   public static void main(String[] argv)
   {
       CompressUtils.decompressZip("//Users//xy//Downloads//2016050501366405_2017-06-26.csv.zip","GBK", "//Users//xy//Downloads//");

   }

}
