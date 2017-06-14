package com.xy.util;

import com.xy.util.DateUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DateUtils Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十二月 2, 2016</pre>
 */
public class DateUtilsTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }


    /**
     * Method: convertDateFormat(String sourceDate, String sourceDateFormat, String destDateFormat)
     */
    @Test
    public void testConvertDateFormat() throws Exception {
        String sourceDateFormat = "yyyyMMdd HHmmss";
        String destDateFormat = "yyyy-MM-dd HH:mm:ss";
        Date date = new Date();
        String sourceDate = new SimpleDateFormat(sourceDateFormat).format(date);
        String destDate = new SimpleDateFormat(destDateFormat).format(date);
        String ret = DateUtils.convertDateFormat(sourceDate, sourceDateFormat, destDateFormat);
        Assert.assertEquals(destDate, ret);
    }


}
