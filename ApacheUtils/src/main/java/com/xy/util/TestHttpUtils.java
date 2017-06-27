package com.xy.util;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by xy on 2017/2/20.
 */
public class TestHttpUtils {

    public static void testpost() {


        String url = "http://pay.sandgate.cn";
        String json = "{\"hello\":\"world\"}";

        String res = null;
        try {
            // res = HttpClientUtils.getInstance().sendHttpsRequestByPostOld("https://shq-api-test.51fubei.com/gateway", json, "application/json", "UTF-8", "112.91.208.78", 9999);
            // HttpClientUtils.getInstance().setProxy("180.167.34.187",80);

           // HttpClientUtils.getInstance().setProxy(proxyIp, Integer.parseInt(proxyPort));
//            res = HttpClientUtils.getInstance().sendHttpRequest(url, json, "application/json", "UTF-8", "POST");
//            res = HttpClientUtils.getInstance().sendHttpRequest(url, json, "application/json", "UTF-8", "POST");
//            res = HttpClientUtils.getInstance().sendHttpRequest(url, json, "application/json", "UTF-8", "POST");

            res = HttpClientUtils.getInstance().sendHttpRequest(url,null,null,"UTF-8", "GET");
            //res = HttpClientUtils.getInstance().sendHttpRequestByPost("https://www.baidu.com", json, "application/json;charset=utf-8", "UTF-8", "GET");
        } catch (HttpClientUtils.HttpUtilsException e) {
            e.printStackTrace();
        }
        System.out.println(res);

    }


    public static void testDowload()
    {
        String url = "https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.Ff5Bs5&treeId=193&articleId=106262&docType=1";

        try {
            OutputStream outputStream = new FileOutputStream("1.txt");
            HttpClientUtils.getInstance().dowloadFile(url, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (HttpClientUtils.HttpUtilsException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
//        testpost();
        testDowload();
    }
}
