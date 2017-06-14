package com.xy.util;

/**
 * Created by xy on 2017/2/20.
 */
public class TestHttpUtils {

    public static void testpost() {


        String url = "https://ibsbjstar.ccb.com.cn/CCBIS/ccbMain?CCB_IBSVersion=V6";
        String json = "https://ibsbjstar.ccb.com.cn/CCBIS/ccbMain?CCB_IBSVersion=V6&MERCHANTID=310A370654&POSID=000732462&BRANCHID=310000000&ORDERID=11160045170328160240&PAYMENT=1&CURCODE=01&TXCODE=530550&RETURNTYPE=1&TIMEOUT=20170328160243&MAC=462DC3202A2BBF54E08B31352C27567D";

        url = json;
        String res = null;
        try {
            // res = HttpClientUtils.getInstance().sendHttpsRequestByPostOld("https://shq-api-test.51fubei.com/gateway", json, "application/json", "UTF-8", "112.91.208.78", 9999);
            //HttpClientUtils.getInstance().setProxy("58.59.68.91",9797);
            //
            res = HttpClientUtils.getInstance().sendHttpRequest(url, json, "application/json", "UTF-8", "GET");
            // res = HttpClientUtils.getInstance().sendHttpRequestByPost("https://www.baidu.com", json, "application/json;charset=utf-8", "UTF-8", "GET");

        } catch (HttpClientUtils.HttpUtilsException e) {
            e.printStackTrace();
        }
        System.out.println(res);

    }


    public static void main(String[] argv) {
        testpost();
    }
}
