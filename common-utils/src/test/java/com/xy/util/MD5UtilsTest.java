package com.xy.util;

import com.xy.util.MD5Utils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * MD5Utils Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十一月 22, 2016</pre>
 */
public class MD5UtilsTest {

    private String testStr = "hello world";
    private String testStrHex = "68656c6c6f20776f726c64";

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: byteArrayToHexString(byte[] b)
     */
    @Test
    public void testByteArrayToHexString() throws Exception {
        String hexStr = MD5Utils.byteArrayToHexString(testStr.getBytes());
        Assert.assertNotNull(hexStr);
        Assert.assertEquals(testStrHex, hexStr);
    }

    /**
     * Method: MD5Encode(String origin)
     */
    @Test
    public void testMD5Encode() throws Exception {
        String md5 = MD5Utils.MD5Encode(testStr);
        Assert.assertNotNull(md5);
        Assert.assertEquals("5eb63bbbe01eeed093cb22bb8f5acdc3", md5);
    }

    /**
     * Method: byteToHexString(byte b)
     */
    @Test
    public void testByteToHexString() throws Exception {
        try {
            Method method = MD5Utils.class.getMethod("byteToHexString", byte.class);
            Object object = MD5Utils.class.newInstance();
            method.setAccessible(true);
            method.invoke(object, '1');
        } catch (Exception e) {
            Assert.fail();
        }
    }

} 
