package com.xy.util;

import com.xy.util.HttpClientUtils;

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
        String url = "http://dwbillcenter.alipay.com/downloadBillFile.resource?bizType=trade&userId=20888021754107650156&fileType=csv.zip&bizDates=20170628&downloadFileName=20888021754107650156_20170628.csv.zip&fileId=%2Ftrade%2F20888021754107650156%2F20170628.csv.zip&timestamp=1498787283&token=64542d172f6899e94cf988f17f38532f";
        String path = "";
        try {
            String res = HttpClientUtils.getInstance().dowloadFile(url, path);
            System.out.println(res);
        } catch (HttpClientUtils.HttpUtilsException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
//        testpost();
        testDowload();
    }
}
