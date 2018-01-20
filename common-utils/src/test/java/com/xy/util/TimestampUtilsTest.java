package com.xy.util;

import com.xy.util.TimestampUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * TimestampUtils Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十二月 2, 2016</pre>
 */
public class TimestampUtilsTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: checkTimestamp(long timestamp, int errorValue)
     */
    @Test
    public void testCheckTimestamp() throws Exception {

    }

    @Test
    public void testCheckTimestamp_0() throws Exception {
        long timestamp =  TimestampUtils.getTimestamp();
        boolean ret = TimestampUtils.checkTimestamp(timestamp,1);
        Assert.assertTrue(ret);
    }

    @Test
    public void testCheckTimestamp_1() throws Exception {
        long timestamp =  TimestampUtils.getTimestamp() - 1;
        boolean ret = TimestampUtils.checkTimestamp(timestamp,0);
        Assert.assertFalse(ret);
    }

    @Test
    public void testCheckTimestamp_2() throws Exception {
        long timestamp =  TimestampUtils.getTimestamp() - 1000;
        boolean ret = TimestampUtils.checkTimestamp(timestamp,10000);
        Assert.assertTrue(ret);
    }

    /**
     * Method: getTimestamp()
     */
    @Test
    public void testGetTimestamp() throws Exception {
        Long timestamp = TimestampUtils.getTimestamp();
        System.out.println(timestamp);
    }

    @Test
    public void testGetTimestamp_0() throws Exception {
        long ret = TimestampUtils.getTimestamp();
        Assert.assertTrue(("" + ret).length() == 10);
    }


} 
