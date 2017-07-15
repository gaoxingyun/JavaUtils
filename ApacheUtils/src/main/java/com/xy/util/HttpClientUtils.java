package com.xy.util;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Http/Https工具类
 */
public class HttpClientUtils {

    public static HttpClientUtils httpsClientUtils = null;
    private  PoolingHttpClientConnectionManager cm = null;

    private static String REQUEST_PROXY_IP = null;
    private static Integer REQUEST_PROXY_PORT = null;

    private static final Integer HTTP_CLIENT_POOL_MAX_TOTAL = 1;
    private static final Integer HTTP_CLIENT_POOL_MAX_PER_ROUTE = 10;

    private static final String EXCEPTION_METHOD_NOSUPPORT = "不支持的请求方法";

    public static final String REQUEST_METHED_GET = "GET";
    public static final String REQUEST_METHED_POST = "POST";
    public static final String REQUEST_HEADER_CONTENT_TYPE_APPLICATION_JSON = "application/json";
    public static final String REQUEST_HEADER_CONTENT_TYPE_APPLICATION_XML = "application/xml";
    public static final String REQUEST_HEADER_ENCODING_UTF8 = "UTF-8";
    public static final String REQUEST_HEADER_ENCODING_GBK = "GBK";


    /**
     * 私有化构造器
     */
    private HttpClientUtils() {
        try {
            initPool();
        } catch (KeyStoreException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化连接池
     */
    private void initPool() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        if (cm == null) {
            cm = new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", new SSLConnectionSocketFactory(SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build(), new HostnameVerifier() {
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            })).build());
            cm.setMaxTotal(HTTP_CLIENT_POOL_MAX_TOTAL);
            cm.setDefaultMaxPerRoute(HTTP_CLIENT_POOL_MAX_PER_ROUTE);

        }
    }

    /**
     * 获取http工具类的唯一实例
     *
     * @return http工具类的唯一实例
     */
    public static HttpClientUtils getInstance() {
        if (httpsClientUtils == null) {
            synchronized (HttpClientUtils.class) {
                if (httpsClientUtils == null) {
                    httpsClientUtils = new HttpClientUtils();
                }
            }
        }
        return httpsClientUtils;
    }

    /**
     * 发送HTTPS	POST请求 HttpClient 4.0使用
     *
     * @param url         请求地址
     * @param data        请求数据
     * @param contentType 请求数据类型
     * @param encoding    请求数据编码
     * @param proxyUrl    代理服务器ip
     * @param proxyPort   代理服务器port
     * @return 返回响应值
     * @deprecated sendHttpsRequestByPostUseProxy
     */
    public String sendHttpsRequestByPostOld(String url, String data, String contentType, String encoding, String proxyUrl, Integer proxyPort) throws HttpUtilsException {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        //创建TrustManager
        X509TrustManager xtm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        //这个好像是HOST验证
        X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
            public void verify(String host, SSLSocket ssl) throws IOException {
            }

            public void verify(String host, X509Certificate cert) throws SSLException {
            }

            public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
            }

            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
        try {
            //TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
            SSLContext ctx = SSLContext.getInstance("TLS");
            //使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
            ctx.init(null, new TrustManager[]{xtm}, null);
            //创建SSLSocketFactory
            SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
            SSLConnectionSocketFactory sslConnectionSocketFactory = SSLConnectionSocketFactory.getSocketFactory();

            socketFactory.setHostnameVerifier(hostnameVerifier);
            //通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", socketFactory, 443));

            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", contentType);
            httpPost.setHeader("Content-Encoding", encoding);
            httpPost.setEntity(new StringEntity(data, encoding));

            if (proxyPort != null && proxyPort != null) {
                HttpHost httpHost = new HttpHost(proxyUrl, proxyPort);
                httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, httpHost);
            }
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity(); // 获取响应实体
            if (entity != null) {
                responseContent = EntityUtils.toString(entity, encoding);
            }
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }


    /**
     * 发送HTTP/HTTPS GET/POST请求(忽略证书) HttpClient 4.5使用
     *
     * @param url         请求地址
     * @param data        请求数据
     * @param contentType 请求数据类型
     * @param encoding    请求数据编码
     * @param methed      请求方式
     * @return 返回响应值
     */
    public String sendHttpRequest(String url, String data, String contentType, String encoding, String methed) throws HttpUtilsException {

        // 内部工具类不验证输入数据，需调用者自己保证传入参数的合理性
        CloseableHttpClient httpClient = null;
        HttpRequestBase httpRequest = null;
        CloseableHttpResponse closeableHttpResponse = null;

        try {
            httpClient = getConnection();

            if (REQUEST_METHED_GET.equals(methed)) {
                HttpGet httpGet = new HttpGet();
                httpRequest = httpGet;
            } else if (REQUEST_METHED_POST.equals(methed)) {
                HttpPost httpPost = new HttpPost();
                httpPost.setEntity(new StringEntity(data));
                httpRequest = httpPost;
            } else {
                throw new HttpUtilsException(EXCEPTION_METHOD_NOSUPPORT);
            }

            httpRequest.setURI(new URI(url));

            httpRequest.addHeader("Content-Type", contentType);
            httpRequest.addHeader("Content-Encoding", encoding);
            if (REQUEST_PROXY_IP != null && REQUEST_PROXY_PORT != null && !REQUEST_PROXY_IP.isEmpty()) {
                // 设置代理
                httpRequest.setConfig(RequestConfig.custom().setProxy(new HttpHost(REQUEST_PROXY_IP, REQUEST_PROXY_PORT)).build());
            }

            closeableHttpResponse =  httpClient.execute(httpRequest);
            String response = EntityUtils.toString(closeableHttpResponse.getEntity());
            return response;
        } catch (IOException | URISyntaxException e) {
            throw new HttpUtilsException(e.getMessage());
        } finally {
            releaseConnection(httpRequest, closeableHttpResponse);
        }
    }


    /**
     * 下载文件
     * @param url 下载地址
     * @param filePath 文件路径
     * @return
     */
    public String dowloadFile(String url, String filePath) throws HttpUtilsException {

        String fileName = null;
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse closeableHttpResponse = null;
        HttpGet httpRequest = null;
        try {
             httpClient = getConnection();
             httpRequest = new HttpGet();
            httpRequest.setURI(new URI(url));

            if (REQUEST_PROXY_IP != null && REQUEST_PROXY_PORT != null && !REQUEST_PROXY_IP.isEmpty()) {
                // 设置代理
                httpRequest.setConfig(RequestConfig.custom().setProxy(new HttpHost(REQUEST_PROXY_IP, REQUEST_PROXY_PORT)).build());
            }

            closeableHttpResponse = httpClient.execute(httpRequest);
            fileName = filePath + closeableHttpResponse.getFirstHeader("Content-Disposition").getValue().split(";")[1];
            closeableHttpResponse.getEntity().writeTo(new FileOutputStream(fileName.toString()));
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            throw new HttpUtilsException(e.getMessage());
        } finally {
            releaseConnection(httpRequest, closeableHttpResponse);
        }
        return fileName.toString();
    }
    /**
     * 获取连接
     *
     * @param
     */
    private CloseableHttpClient getConnection()
    {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        return httpClient;
    }

    /**
     * 释放连接
     *
     * @param httpRequest 需关闭的连接
     * @param closeableHttpResponse 需关闭的response
     */
    private void releaseConnection(HttpRequestBase httpRequest, CloseableHttpResponse closeableHttpResponse) {

        if(closeableHttpResponse != null){
            try {
                closeableHttpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (httpRequest != null) {
            httpRequest.releaseConnection();
        }
    }

    /**
     * 自定义Http uncheck异常类
     */
    class HttpUtilsException extends RuntimeException {
        HttpUtilsException(String msg) {
            super(msg);
        }
    }

    /**
     * 设置代理
     */
    public void setProxy(String proxyIp, Integer proxyPort) {
        REQUEST_PROXY_IP = proxyIp;
        REQUEST_PROXY_PORT = proxyPort;
    }

}
