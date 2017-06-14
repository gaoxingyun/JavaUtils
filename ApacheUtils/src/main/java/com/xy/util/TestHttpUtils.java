package com.xy.util;

/**
 * Created by xy on 2017/2/20.
 */
public class TestHttpUtils {

    public static void testpost(String proxyIp, String proxyPort) {


        String url = "https://shq-api-test.51fubei.com/gateway";
        String json = "{\"hello\":\"world\"}";

        String res = null;
        try {
            // res = HttpClientUtils.getInstance().sendHttpsRequestByPostOld("https://shq-api-test.51fubei.com/gateway", json, "application/json", "UTF-8", "112.91.208.78", 9999);
            // HttpClientUtils.getInstance().setProxy("180.167.34.187",80);

            HttpClientUtils.getInstance().setProxy(proxyIp, Integer.parseInt(proxyPort));
            res = HttpClientUtils.getInstance().sendHttpRequest(url, json, "application/json", "UTF-8", "POST");
            // res = HttpClientUtils.getInstance().sendHttpRequestByPost("https://www.baidu.com", json, "application/json;charset=utf-8", "UTF-8", "GET");
        } catch (HttpClientUtils.HttpUtilsException e) {
            e.printStackTrace();
        }
        System.out.println(res);

    }


    public static void main(String[] args) {
        testpost(args[0], args[1]);
    }
}
