package com.xy.util;

import com.xy.util.RandomStringGeneratorUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * RandomStringGeneratorUtils Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十二月 2, 2016</pre>
 */
public class RandomStringGeneratorUtilsTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getRandomStringByLength(int length)
     */
    @Test
    public void testGetRandomStringByLength_0() throws Exception {
        int length = -100;
        String ret = RandomStringGeneratorUtils.getRandomStringByLength(length);
        Assert.assertEquals(ret, "");
    }

    @Test
    public void testGetRandomStringByLength_1() throws Exception {
        int length = 0;
        String ret = RandomStringGeneratorUtils.getRandomStringByLength(length);
        Assert.assertEquals(ret, "");
    }

    @Test
    public void testGetRandomStringByLength_2() throws Exception {
        int length = 32;
        String ret = RandomStringGeneratorUtils.getRandomStringByLength(length);
        Assert.assertTrue(ret.length() == length);
    }

    @Test
    public void testGetRandomStringByLength_3() throws Exception {
        int length = 64;
        String ret1 = RandomStringGeneratorUtils.getRandomStringByLength(length);
        String ret2 = RandomStringGeneratorUtils.getRandomStringByLength(length);
        String ret3 = RandomStringGeneratorUtils.getRandomStringByLength(length);
        Assert.assertTrue(ret1.length() == length);
        Assert.assertTrue(ret2.length() == length);
        Assert.assertTrue(ret3.length() == length);
        Assert.assertNotEquals(ret1, ret2, ret3);
    }


} 
