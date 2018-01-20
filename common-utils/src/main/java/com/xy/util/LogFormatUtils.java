package com.xy.util;

/**
 * 日志格式化工具类
 *
 * @author xy
 */
public class LogFormatUtils {

    private static final String NEWLINE = "\n";


    /**
     * 格式化byte数组到16进制字符串日志
     *
     * @param bytes
     * @param offset
     * @param length
     * @return
     */
    public static String formatBytes2HexString(byte[] bytes, int offset, int length) {

        final int startIndex = offset;
        final int fullRows = length >>> 4;
        final int remainder = length & 0xF;

        StringBuilder dump = new StringBuilder();

        dump.append(
                "         +-------------------------------------------------+" +
                        NEWLINE + "         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |" +
                        NEWLINE + "+--------+-------------------------------------------------+----------------+");

        // Dump the rows which have 16 bytes.
        for (int row = 0; row < fullRows; row++) {
            int rowStartIndex = (row << 4) + startIndex;

            // Per-row prefix.
            appendHexDumpRowPrefix(dump, row, rowStartIndex);

            // Hex dump
            int rowEndIndex = rowStartIndex + 16;
            for (int j = rowStartIndex; j < rowEndIndex; j++) {
                dump.append(" ").append((bytes[j] >> 4) == 0 ? "0" : "").append(Integer.toHexString(bytes[j] & 0xff));
            }
            dump.append(" |");

            // ASCII dump
            for (int j = rowStartIndex; j < rowEndIndex; j++) {
                dump.append((char) bytes[j]);
            }

            dump.append('|');
        }

        // Dump the last row which has less than 16 bytes.
        if (remainder != 0) {
            int rowStartIndex = (fullRows << 4) + startIndex;
            appendHexDumpRowPrefix(dump, fullRows, rowStartIndex);

            // Hex dump
            int rowEndIndex = rowStartIndex + remainder;
            for (int j = rowStartIndex; j < rowEndIndex; j++) {
                dump.append(" ").append((bytes[j] >> 4) == 0 ? "0" : "").append(Integer.toHexString(bytes[j] & 0xff));
            }

            for (int j = 0; j < 16 - remainder; j++) {
                dump.append("   ");
            }

            dump.append(" |");

            // Ascii dump
            for (int j = rowStartIndex; j < rowEndIndex; j++) {
                dump.append((char) bytes[j]);
            }
            for (int j = 0; j < 16 - remainder; j++) {
                dump.append(" ");
            }
            dump.append('|');
        }

        dump.append(NEWLINE +
                "+--------+-------------------------------------------------+----------------+");

        return dump.toString();
    }

    private static void appendHexDumpRowPrefix(StringBuilder dump, int row, int rowStartIndex) {
        dump.append(NEWLINE);
        dump.append(Long.toHexString(rowStartIndex & 0xFFFFFFFFL | 0x100000000L));
        dump.setCharAt(dump.length() - 9, '|');
        dump.append('|');
    }
}
