package com.xy.util;


/**
 * ABCD->16禁止选项压缩算法
 */
public class ABCDUtils {


    private Integer ABCD2Num(char abcd)
    {
        switch (abcd){
            case 'A':
                return 1;
            case 'B':
                return 2;
            case 'C':
                return 3;
            case 'D':
                return 4;

            default:
                throw new RuntimeException("this char is not a ABCD");
        }
    }

}
