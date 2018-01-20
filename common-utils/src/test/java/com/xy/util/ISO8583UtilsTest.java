package com.xy.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ISO8583UtilsTest {

    /**
     * 测试打包
     */
    @Test
    public void testPack() {

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("MTI", "0021");
        map.put("TPDU", "6020141011");
        map.put("HEAD", "201424016333");
        map.put("3", "010002");
        map.put("4", "12");
        // map.put("56", "fsdfsaxvxcvxczvxcvcxzv");
        //map.put("32", "12344");
        map.put("39", "00");
        map.put("41", "qweryuis");
        map.put("56", "你好");
        map.put("64", "1234567890abcdef");

        byte[] bytes = ISO8583Utils.pack(map);

        System.out.println(LogFormatUtils.formatBytes2HexString(bytes, 0, bytes.length));

        Map<String, String> isoMap = ISO8583Utils.unpack(bytes);
        System.out.println(isoMap);
    }

    /**
     * 测试解包
     */
    @Test
    public void testUnpack() {

    }

    @Test
    public void testStrformat() {
        String s = "fdsf";
        int n = 123;
        String str = String.format("%06", s);
        // String str = String.format("%06d", n);
        System.out.println(str);
    }




}
