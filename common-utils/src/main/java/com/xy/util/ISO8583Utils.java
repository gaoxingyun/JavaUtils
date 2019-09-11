package com.xy.util;



import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 金融ISO8583报文工具类
 *
 * @author xy
 */
public class ISO8583Utils {

    /**
     * 打包
     *
     * @param isoMessageMap
     * @return
     */
    public static byte[] pack(Map<String, String> isoMessageMap) {
        ISO8583 iso8583 = new ISO8583(isoMessageMap);
        try {
            iso8583.pack();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return iso8583.getISO_MESSAGE_DATA();
    }

    /**
     * 解包
     *
     * @param isoMessageData
     * @return
     */
    public static Map<String, String> unpack(byte[] isoMessageData) {

        ISO8583 iso8583 = new ISO8583(isoMessageData);

        try {
            iso8583.unpack();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return iso8583.getISO_MESSAGE_MAP();
    }


}

/**
 * ISO8583报文内容
 */
class ISO8583 {

    private static final String CHARSET = "UTF-8";

    private static final String MTI_KEY = "MTI";
    private static final String TPDU_KEY = "TPDU";
    private static final String HEAD_KEY = "HEAD";

    private static final Integer MTI_LENGTH = 2;
    private static final Integer TPDU_LENGTH = 5;
    private static final Integer HEAD_LENGTH = 6;


    /**
     * ISO8583报文域数据
     */
    private Map<String, String> ISO_MESSAGE_MAP;
    /**
     * ISO8583报文数据
     */
    private byte[] ISO_MESSAGE_DATA;

    public ISO8583(Map<String, String> isoMessageMap) {
        this.ISO_MESSAGE_MAP = isoMessageMap;
    }

    public ISO8583(byte[] isoMessageData) {
        this.ISO_MESSAGE_DATA = isoMessageData;
    }


    public Map<String, String> getISO_MESSAGE_MAP() {
        return ISO_MESSAGE_MAP;
    }

    public byte[] getISO_MESSAGE_DATA() {
        return ISO_MESSAGE_DATA;
    }

    /**
     * 解包
     */
    public void unpack() throws Exception {
        int index = 0;

        ISO_MESSAGE_MAP = new HashMap<>();

        byte[] tpduBytes = new byte[TPDU_LENGTH];
        System.arraycopy(ISO_MESSAGE_DATA, index, tpduBytes, 0, TPDU_LENGTH);
        String tpdu = bcd2str(tpduBytes);
        ISO_MESSAGE_MAP.put(TPDU_KEY, tpdu);
        index += TPDU_LENGTH;

        byte[] headBytes = new byte[HEAD_LENGTH];
        System.arraycopy(ISO_MESSAGE_DATA, index, headBytes, 0, HEAD_LENGTH);
        String head = bcd2str(headBytes);
        ISO_MESSAGE_MAP.put(HEAD_KEY, head);
        index += HEAD_LENGTH;

        byte[] mtiBytes = new byte[MTI_LENGTH];
        System.arraycopy(ISO_MESSAGE_DATA, index, mtiBytes, 0, MTI_LENGTH);
        String mti = bcd2str(mtiBytes);
        ISO_MESSAGE_MAP.put(MTI_KEY, mti);
        index += MTI_LENGTH;

        byte[] bitMapBytes = new byte[8];
        System.arraycopy(ISO_MESSAGE_DATA, index, bitMapBytes, 0, 8);
        index += 8;

        if (ISO_MESSAGE_DATA[index + 1] >> 7 == 1) {
            // 128 bitmap
            throw new RuntimeException("not support 128 bitmap iso8583");
        } else {
            String fieldNo = null;
            IsoFieldSchema.IsoLengthType lengthType = null;
            IsoFieldSchema.IsoFieldType fieldType = null;
            int fieldLength = 0;
            String fieldValue = null;

            byte[] fieldBytes = null;
            for (int i = 0; i < 8; i++) {
                for (int j = 1; j <= 8; j++) {

                    fieldValue = null;
                    if (((bitMapBytes[i] >> (8 - j)) & 0x01) == 1) {
                        // field exist
                        fieldNo = Integer.toString(8 * i + j);
                        IsoFieldSchema.IsoFieldInfo fieldInfo = IsoFieldSchema.getInstance().getIsoFieldInfo(fieldNo);
                        lengthType = fieldInfo.getLengthType();
                        fieldType = fieldInfo.getFieldType();

                        if (IsoFieldSchema.IsoLengthType.FIXED.equals(lengthType)) {
                            if (IsoFieldSchema.IsoFieldType.N.equals(fieldType)) {
                                fieldLength = fieldInfo.getMaxLength() / 2;
                                fieldBytes = new byte[fieldLength];
                                System.arraycopy(ISO_MESSAGE_DATA, index, fieldBytes, 0, fieldLength);
                                index += fieldLength;

                                fieldValue = bcd2str(fieldBytes);

                            } else if (IsoFieldSchema.IsoFieldType.ANS.equals(fieldType) ||
                                    IsoFieldSchema.IsoFieldType.AN.equals(fieldType) ||
                                    IsoFieldSchema.IsoFieldType.AS.equals(fieldType) ||
                                    IsoFieldSchema.IsoFieldType.A.equals(fieldType)) {

                                fieldLength = fieldInfo.getMaxLength();
                                fieldBytes = new byte[fieldLength];
                                System.arraycopy(ISO_MESSAGE_DATA, index, fieldBytes, 0, fieldLength);
                                index += fieldLength;

                                fieldValue = new String(fieldBytes, CHARSET);


                            } else if (IsoFieldSchema.IsoFieldType.B.equals(fieldType)) {
                                fieldLength = fieldInfo.getMaxLength() / 2;
                                fieldBytes = new byte[fieldLength];
                                System.arraycopy(ISO_MESSAGE_DATA, index, fieldBytes, 0, fieldLength);
                                index += fieldLength;

                                fieldValue = bytes2hex(fieldBytes);

                            } else {
                                throw new RuntimeException("not support field type!");
                            }

                        } else if (IsoFieldSchema.IsoLengthType.LLVAR.equals(lengthType)) {

                            if (IsoFieldSchema.IsoFieldType.N.equals(fieldType)) {
                                byte[] fieldLengthBytes = new byte[1];
                                System.arraycopy(ISO_MESSAGE_DATA, index, fieldLengthBytes, 0, 1);
                                index += 1;
                                fieldLength = Integer.valueOf(bcd2str(fieldLengthBytes));

                                int byteTempLength = fieldLength / 2;
                                if (fieldLength % 2 != 0) {
                                    byteTempLength = byteTempLength + 1;
                                }

                                fieldBytes = new byte[byteTempLength];
                                System.arraycopy(ISO_MESSAGE_DATA, index, fieldBytes, 0, byteTempLength);
                                index += byteTempLength;
                                fieldValue = bcd2str(fieldBytes).substring(0, fieldLength);

                            } else {
                                throw new RuntimeException("not support field type!");
                            }
                        } else if (IsoFieldSchema.IsoLengthType.LLLVAR.equals(lengthType)) {

                            if (IsoFieldSchema.IsoFieldType.N.equals(fieldType)) {


                            } else if (IsoFieldSchema.IsoFieldType.ANS.equals(fieldType) ||
                                    IsoFieldSchema.IsoFieldType.AN.equals(fieldType) ||
                                    IsoFieldSchema.IsoFieldType.AS.equals(fieldType) ||
                                    IsoFieldSchema.IsoFieldType.A.equals(fieldType)) {

                                byte[] fieldLengthBytes = new byte[2];
                                System.arraycopy(ISO_MESSAGE_DATA, index, fieldLengthBytes, 0, 2);
                                index += 2;
                                fieldLength = Integer.valueOf(bcd2str(fieldLengthBytes));

                                fieldBytes = new byte[fieldLength];
                                System.arraycopy(ISO_MESSAGE_DATA, index, fieldBytes, 0, fieldLength);
                                index += fieldLength;

                                fieldValue = new String(fieldBytes, CHARSET);


                            } else if (IsoFieldSchema.IsoFieldType.B.equals(fieldType)) {
                                throw new RuntimeException("not support field type!");
                            } else {
                                throw new RuntimeException("not support field type!");
                            }

                        } else {
                            throw new RuntimeException("not support length type!");


                        }

                        ISO_MESSAGE_MAP.put(fieldNo, fieldValue);
                    }

                }
            }


        }


    }


    /**
     * 打包
     */
    public void pack() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String mti = ISO_MESSAGE_MAP.get(MTI_KEY);
        String tpdu = ISO_MESSAGE_MAP.get(TPDU_KEY);
        String head = ISO_MESSAGE_MAP.get(HEAD_KEY);
        if (tpdu != null && !tpdu.equals("") && tpdu.length() == TPDU_LENGTH * 2) {
            baos.write(str2bcd(tpdu));
        }
        if (head != null && !head.equals("") && head.length() == HEAD_LENGTH * 2) {
            baos.write(str2bcd(head));
        }

        if (mti != null && !mti.equals("") && mti.length() == MTI_LENGTH * 2) {
            baos.write(str2bcd(mti));
        } else {
            throw new RuntimeException("MTI NOT EXIST!");
        }

        byte[] bitMap = buildBitMap64(ISO_MESSAGE_MAP);
        baos.write(bitMap);

        byte[] fieldBytes = buildField64(ISO_MESSAGE_MAP);
        baos.write(fieldBytes);

        this.ISO_MESSAGE_DATA = baos.toByteArray();
    }


    /**
     * 生成64位的bitmap
     *
     * @param fieldMap
     * @return
     */
    private static byte[] buildBitMap64(Map<String, String> fieldMap) {

        int n = 64 / 8;
        byte[] bs = new byte[n];

        for (int i = 0; i < n; ++i) {
            char c = 0;

            for (int j = 0; j < 8; ++j) {
                c = fieldMap.containsKey(Integer.toString(8 * i + j + 1)) ? (char) (c | 1 << 7 - j) : c;
            }

            bs[i] = (byte) c;
        }
        return bs;
    }

    /**
     * 生成域信息
     *
     * @return
     */
    private static byte[] buildField64(Map<String, String> fieldMap) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        for (int i = 1; i <= 64; i++) {
            String fieldKey = Integer.toString(i);
            if (fieldMap.containsKey(fieldKey)) {
                writeFieldByType(baos, IsoFieldSchema.getInstance().getIsoFieldInfo(fieldKey), fieldMap.get(fieldKey));
            }
        }
        return baos.toByteArray();

    }


    /**
     * 转换ISO8583域值
     *
     * @param baos
     * @param fieldInfo
     * @param fieldValue
     */
    private static void writeFieldByType(ByteArrayOutputStream baos, IsoFieldSchema.IsoFieldInfo fieldInfo, String fieldValue) throws Exception {
        IsoFieldSchema.IsoFieldType fieldType = fieldInfo.getFieldType();
        IsoFieldSchema.IsoLengthType lengthType = fieldInfo.getLengthType();
        Integer fieldLength = fieldInfo.getMaxLength();

        byte[] bytes = null;

        if (fieldType.equals(IsoFieldSchema.IsoFieldType.ANS) ||
                fieldType.equals(IsoFieldSchema.IsoFieldType.AN) ||
                fieldType.equals(IsoFieldSchema.IsoFieldType.AS) ||
                fieldType.equals(IsoFieldSchema.IsoFieldType.A)) {

            bytes = fieldValue.getBytes(CHARSET);
            if (lengthType.equals(IsoFieldSchema.IsoLengthType.FIXED)) {
            } else {
                baos.write(str2bcd_l(num_l0(bytes.length, lengthType.getLength())));
            }
            baos.write(bytes);
        } else if (fieldType.equals(IsoFieldSchema.IsoFieldType.N)) {

            if (lengthType.equals(IsoFieldSchema.IsoLengthType.FIXED)) {
                fieldValue = num_l0(Integer.valueOf(fieldValue), fieldLength);

            } else {
                String lengthFormat = "%0" + lengthType.getLength() + "d";
                baos.write(str2bcd_l(String.format(lengthFormat, fieldValue.length())));
            }
            baos.write(str2bcd_r(fieldValue));
        } else if (fieldType.equals(IsoFieldSchema.IsoFieldType.B)) {
            if (lengthType.equals(IsoFieldSchema.IsoLengthType.FIXED)) {
                baos.write(hex2bytes(fieldValue));
            } else {
                throw new RuntimeException("not support ");
            }
        } else {
            throw new RuntimeException("not support field type!");
        }

    }

    /**
     * byte 数组 转 Hex String
     *
     * @param bytes
     * @return
     */
    private static String bytes2hex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * Hex String 转 byte数组
     *
     * @param hexString
     * @return
     */
    private static byte[] hex2bytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

        }
        return d;
    }

    /**
     * Char 转 byte
     *
     * @param c
     * @return
     */
    private static byte charToByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * 字符左补0
     *
     * @param s
     * @param length
     * @return
     */
    private static String str_l0(String s, int length) {
        return String.format("%0" + length + "s", s);
    }


    /**
     * 数字左补0
     *
     * @param n
     * @param length
     * @return
     */
    private static String num_l0(int n, int length) {
        return String.format("%0" + length + "d", n);
    }


    /**
     * String 转 BCD码，左补0
     *
     * @param s
     * @return
     */
    private static byte[] str2bcd_l(String s) {
        if (s.length() % 2 != 0) {
            s = "0" + s;
        }
        return str2bcd(s);
    }


    /**
     * String 转 BCD码，右补0
     *
     * @param s
     * @return
     */
    private static byte[] str2bcd_r(String s) {
        if (s.length() % 2 != 0) {
            s = s + "0";
        }
        return str2bcd(s);
    }


    /**
     * String 转 BCD码，不补0
     *
     * @param s
     * @return
     */
    private static byte[] str2bcd(String s) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        char[] cs = s.toCharArray();

        for (int i = 0; i < cs.length; i += 2) {
            int high = cs[i] - 48;
            int low = cs[i + 1] - 48;
            baos.write(high << 4 | low);
        }

        return baos.toByteArray();
    }


    /**
     * BCD码 转 String
     *
     * @param b
     * @return
     */
    private static String bcd2str(byte[] b) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < b.length; ++i) {
            int h = ((b[i] & 255) >> 4) + 48;
            sb.append((char) h);
            int l = (b[i] & 15) + 48;
            sb.append((char) l);
        }

        return sb.toString();
    }

}


/**
 * ISO8583报文域信息
 */
class IsoFieldSchema {

    private static IsoFieldSchema instance = new IsoFieldSchema();

    /**
     * ISO8583报文元信息
     */
    private HashMap<String, IsoFieldInfo> isoSchema = new HashMap<String, IsoFieldInfo>();

    private IsoFieldSchema() {
        isoSchema.put("MTI", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 4));
        isoSchema.put("2", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.LLVAR, 19));
        isoSchema.put("3", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 6));
        isoSchema.put("4", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 12));
        isoSchema.put("5", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 12));
        isoSchema.put("6", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 12));
        isoSchema.put("7", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 10));
        isoSchema.put("8", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 8));
        isoSchema.put("9", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 8));
        isoSchema.put("10", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 8));
        isoSchema.put("11", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 6));
        isoSchema.put("12", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 6));
        isoSchema.put("13", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 4));
        isoSchema.put("14", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 4));
        isoSchema.put("15", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 4));
        isoSchema.put("16", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 4));
        isoSchema.put("17", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 4));
        isoSchema.put("18", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 4));
        isoSchema.put("19", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 3));
        isoSchema.put("20", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 3));
        isoSchema.put("21", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 3));
        isoSchema.put("22", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 3));
        isoSchema.put("23", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 3));
        isoSchema.put("24", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 3));
        isoSchema.put("25", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 2));
        isoSchema.put("26", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 2));
        isoSchema.put("27", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 1));
        isoSchema.put("28", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 8));
        isoSchema.put("29", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 8));
        isoSchema.put("30", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 8));
        isoSchema.put("31", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 8));
        isoSchema.put("32", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.LLVAR, 11));
        isoSchema.put("33", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.LLVAR, 11));
        isoSchema.put("34", new IsoFieldInfo(IsoFieldType.NS, IsoLengthType.LLVAR, 28));
        isoSchema.put("35", new IsoFieldInfo(IsoFieldType.Z, IsoLengthType.LLVAR, 37));
        isoSchema.put("36", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.LLVAR, 104));
        isoSchema.put("37", new IsoFieldInfo(IsoFieldType.AN, IsoLengthType.FIXED, 12));
        isoSchema.put("38", new IsoFieldInfo(IsoFieldType.AN, IsoLengthType.FIXED, 6));
        isoSchema.put("39", new IsoFieldInfo(IsoFieldType.AN, IsoLengthType.FIXED, 2));
        isoSchema.put("40", new IsoFieldInfo(IsoFieldType.AN, IsoLengthType.FIXED, 3));
        isoSchema.put("41", new IsoFieldInfo(IsoFieldType.ANS, IsoLengthType.FIXED, 8));
        isoSchema.put("42", new IsoFieldInfo(IsoFieldType.ANS, IsoLengthType.FIXED, 15));
        isoSchema.put("43", new IsoFieldInfo(IsoFieldType.ANS, IsoLengthType.FIXED, 40));
        isoSchema.put("44", new IsoFieldInfo(IsoFieldType.AN, IsoLengthType.LLVAR, 25));
        isoSchema.put("45", new IsoFieldInfo(IsoFieldType.AN, IsoLengthType.LLVAR, 76));
        isoSchema.put("46", new IsoFieldInfo(IsoFieldType.AN, IsoLengthType.LLLVAR, 999));
        isoSchema.put("47", new IsoFieldInfo(IsoFieldType.AN, IsoLengthType.LLLVAR, 999));
        isoSchema.put("48", new IsoFieldInfo(IsoFieldType.AN, IsoLengthType.LLLVAR, 999));
        isoSchema.put("49", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 3));
        isoSchema.put("50", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 3));
        isoSchema.put("51", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 3));
        isoSchema.put("52", new IsoFieldInfo(IsoFieldType.B, IsoLengthType.FIXED, 64));
        isoSchema.put("53", new IsoFieldInfo(IsoFieldType.N, IsoLengthType.FIXED, 16));
        isoSchema.put("54", new IsoFieldInfo(IsoFieldType.AN, IsoLengthType.LLVAR, 120));
        isoSchema.put("55", new IsoFieldInfo(IsoFieldType.ANS, IsoLengthType.LLLVAR, 999));
        isoSchema.put("56", new IsoFieldInfo(IsoFieldType.ANS, IsoLengthType.LLLVAR, 999));
        isoSchema.put("57", new IsoFieldInfo(IsoFieldType.ANS, IsoLengthType.LLLVAR, 999));
        isoSchema.put("58", new IsoFieldInfo(IsoFieldType.ANS, IsoLengthType.LLLVAR, 999));
        isoSchema.put("59", new IsoFieldInfo(IsoFieldType.ANS, IsoLengthType.LLLVAR, 999));
        isoSchema.put("60", new IsoFieldInfo(IsoFieldType.ANS, IsoLengthType.LLLVAR, 999));
        isoSchema.put("61", new IsoFieldInfo(IsoFieldType.ANS, IsoLengthType.LLLVAR, 999));
        isoSchema.put("62", new IsoFieldInfo(IsoFieldType.ANS, IsoLengthType.LLLVAR, 999));
        isoSchema.put("63", new IsoFieldInfo(IsoFieldType.ANS, IsoLengthType.LLLVAR, 999));
        isoSchema.put("64", new IsoFieldInfo(IsoFieldType.B, IsoLengthType.FIXED, 16));
    }


    /**
     * 获得单例对象
     *
     * @return
     */
    public static IsoFieldSchema getInstance() {
        return instance;
    }


    /**
     * 获得域对应的信息
     *
     * @param fieldKey
     * @return
     */
    public IsoFieldInfo getIsoFieldInfo(String fieldKey) {
        return isoSchema.get(fieldKey);
    }


    /**
     * ISO8583一个报文域信息
     */
    class IsoFieldInfo {
        private IsoFieldType fieldType;
        private IsoLengthType lengthType;
        private Integer maxLength;

        public IsoFieldInfo(IsoFieldType fieldType, IsoLengthType lengthType, Integer maxLength) {
            this.fieldType = fieldType;
            this.lengthType = lengthType;
            this.maxLength = maxLength;
        }

        public IsoFieldType getFieldType() {
            return fieldType;
        }

        public IsoLengthType getLengthType() {
            return lengthType;
        }

        public Integer getMaxLength() {
            return maxLength;
        }
    }

    /**
     * ISO8583报文数据类型
     */
    enum IsoFieldType {
        A("A", "a"),
        N("N", "n"),
        S("S", "Ss"),
        AN("AN", "an"),
        AS("AS", "as"),
        NS("NS", "ns"),
        ANS("ANS", "ans"),
        B("B", "b"),
        Z("Z", "z"),
        D("D", ". or .. or ..."),
        X("X", "x or xx or xxx");
        /**
         * 类型
         */
        private String type;

        /**
         * 描述
         */
        private String mean;

        IsoFieldType(String type, String mean) {
            this.type = type;
            this.mean = mean;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMean() {
            return mean;
        }

        public void setMean(String mean) {
            this.mean = mean;
        }
    }

    /**
     * ISO8583报文长度类型
     */
    enum IsoLengthType {
        FIXED("FIXED", 0, "Fixed"),
        LLVAR("LLVAR", 2, "LLVAR or (..xx)"),
        LLLVAR("LLLVAR", 3, "LLLVAR or (...xxx)"),
        HEX_OR_ASCII_L_VAR("HEX_OR_ASCII_L_VAR", 3, "LL and LLL are hex or ASCII. A VAR field can be compressed or ASCII depending of the data element type.");
        /**
         * 类型
         */
        private String type;

        /**
         * 长度
         */
        private int length;

        /**
         * 描述
         */
        private String mean;

        IsoLengthType(String type, int length, String mean) {
            this.type = type;
            this.length = length;
            this.mean = mean;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public String getMean() {
            return mean;
        }

        public void setMean(String mean) {
            this.mean = mean;
        }
    }


}

