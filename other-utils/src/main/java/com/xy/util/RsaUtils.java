package com.xy.util;


import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * RSA工具类
 *
 * @author xy
 */
public class RsaUtils {

    /**
     * 加密算法
     */
    private static final String CIPHER_DE = "RSA";
    /**
     * 解密算法
     */
    private static final String CIPHER_EN = "RSA";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 私钥名称
     */
    private static final String PRIVATE_KEY_NAME = "privateKey";

    /**
     * 公钥名称
     */
    private static final String PUBLIC_KEY_NAME = "publicKey";

    /**
     * 生成RSA密钥对 1024
     * @return
     * @throws Exception
     */
    public static Map<String, String> buildRsa1024Key() throws Exception{
        return buildRsaKey(1024);
    }

    /**
     * 生成RSA密钥对 2048
     * @return
     * @throws Exception
     */
    public static Map<String, String> buildRsa2048Key() throws Exception{
        return buildRsaKey(2048);
    }



    /**
     * 生成RSA密钥对
     *
     * @return 密钥对Map
     */
    public static Map<String, String> buildRsaKey(int length) throws Exception {

        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException var7) {
            throw new Exception("不支持的算法RSA");
        }

        SecureRandom secureRandom = new SecureRandom();
        keyPairGenerator.initialize(length, secureRandom);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        Map<String, String> keyMap = new HashMap<>();

        keyMap.put(PRIVATE_KEY_NAME, Base64.encode(privateKey.getEncoded()));
        keyMap.put(PUBLIC_KEY_NAME, Base64.encode(publicKey.getEncoded()));
        return keyMap;
    }

    /**
     * 取得私钥
     * @param keyMap
     * @return
     */
    public static String achievePrivateKey(Map<String, String> keyMap){
        return keyMap.get(PRIVATE_KEY_NAME);
    }

    /**
     * 取得公钥
     * @param keyMap
     * @return
     */
    public static String achievePublicKey(Map<String, String> keyMap){
        return keyMap.get(PUBLIC_KEY_NAME);
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data 源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(CIPHER_DE);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }


    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(CIPHER_EN);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }



    public static void main(String[] args){

        try {
            Map<String, String> map =  buildRsa1024Key();

            String privateKey = map.get("privateKey");
            String publicKey = map.get("publicKey");
            System.out.println(privateKey);
            System.out.println(publicKey);

//            publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCz7SbYgDmD3cZ5o9IL8BVObkHKms8I0mQ5ly6uIQm1a95J4lgcK2mdjveuVcQ5oXhC1OQx+gSe8pYgaVr1Nyse+sSg+rDOFBypr8/4i+a0OANtYXeUKzS1j7ra/lqon6loN3zYaJNLTGUponP9n6+/FHN0XEjTiW45oEzVTqcVyQIDAQAB";
//
//            privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALPtJtiAOYPdxnmj0gvwFU5uQcqazwjSZDmXLq4hCbVr3kniWBwraZ2O965VxDmheELU5DH6BJ7yliBpWvU3Kx76xKD6sM4UHKmvz/iL5rQ4A21hd5QrNLWPutr+WqifqWg3fNhok0tMZSmic/2fr78Uc3RcSNOJbjmgTNVOpxXJAgMBAAECgYBDUgNL1EYw0aT2VFY+AzllnBlfviairV20sp1Tp6bjS5XjXR4MhC3DNv/zKcH+siy2DMPI8zwRYMDNJb5Tq62lDB/CWXxuRu0b9+ODXZ3yePvvUKVqc2NXZD/8txjtZh5FzEeLw6MeW1ylFsYZtLH97IJiKWEU9w4B9vWEp5uqAQJBANj/SIYK3CfKbpunqnckkop3udmJ4BUhhaFmD8fZbQmMw6SJSB+o9FB0FTwLmWdc2UV3kbpqyyEZd8h75EBs90ECQQDURB/TjObftKO9dVmK+lxR/imktunFWtQ64k8MehuDnZasLm860pQNpeLI3aDGOlYV+RHllC0tCDCXWrkFycSJAkAb2Jab7OTXjlinTNrJMz5C2p5U1iaVT5nwXkKEKNifMxsgECXbOjkv4dWfwPVMmFOhYHio7W9nrfb7GTrvMYyBAkEAmskqpCuFV+/zzv4505yJocjDOTeg9KctR9srZZ/NXIaYDuq1daGFEQa8f1kOGj8D83Xy1QTehI4KUPR5I31kcQJAFeweluxwANSYxKiugt0iiI6TuPCMPZhJtQ9Y5y/S+aK0OvARHr/aC03g1JfDnt+Uts7NgvYboeQWGG2oJVGWLw==";

            String data = "https://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttpshttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafhttps://ss.fdas&=aafdsafdsafdasgfdsgfdsgfdsgfdsgffdttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aaf://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafttps://ss.fdas&=aafdsafdsafsafdsgdsfg593793274932794732947329874fdsgfdsg";

            byte[] ss = encryptByPublicKey(data.getBytes(), publicKey);

            String req = Base64.encode(ss);

            System.out.println(req);


            System.out.println(URLEncoder.encode(req, "UTF-8"));


            byte[] tt = Base64.decode(req);

            byte[] aaa = decryptByPrivateKey(tt,privateKey);

            System.out.println(new String(aaa));
            //System.out.println(aa);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}

class Base64
{

    static private final int     BASELENGTH           = 128;
    static private final int     LOOKUPLENGTH         = 64;
    static private final int     TWENTYFOURBITGROUP   = 24;
    static private final int     EIGHTBIT             = 8;
    static private final int     SIXTEENBIT           = 16;
    static private final int     FOURBYTE             = 4;
    static private final int     SIGN                 = -128;
    static private final char    PAD                  = '=';
    static private final boolean fDebug               = false;
    static final private byte[]  base64Alphabet       = new byte[BASELENGTH];
    static final private char[]  lookUpBase64Alphabet = new char[LOOKUPLENGTH];

    static
    {
        for (int i = 0; i < BASELENGTH; ++i)
        {
            base64Alphabet[i] = -1;
        }
        for (int i = 'Z'; i >= 'A'; i--)
        {
            base64Alphabet[i] = (byte) (i - 'A');
        }
        for (int i = 'z'; i >= 'a'; i--)
        {
            base64Alphabet[i] = (byte) (i - 'a' + 26);
        }

        for (int i = '9'; i >= '0'; i--)
        {
            base64Alphabet[i] = (byte) (i - '0' + 52);
        }

        base64Alphabet['+'] = 62;
        base64Alphabet['/'] = 63;

        for (int i = 0; i <= 25; i++)
        {
            lookUpBase64Alphabet[i] = (char) ('A' + i);
        }

        for (int i = 26, j = 0; i <= 51; i++, j++)
        {
            lookUpBase64Alphabet[i] = (char) ('a' + j);
        }

        for (int i = 52, j = 0; i <= 61; i++, j++)
        {
            lookUpBase64Alphabet[i] = (char) ('0' + j);
        }
        lookUpBase64Alphabet[62] = (char) '+';
        lookUpBase64Alphabet[63] = (char) '/';

    }

    private static boolean isWhiteSpace(char octect)
    {
        return (octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9);
    }

    private static boolean isPad(char octect)
    {
        return (octect == PAD);
    }

    private static boolean isData(char octect)
    {
        return (octect < BASELENGTH && base64Alphabet[octect] != -1);
    }

    /**
     * Encodes hex octects into Base64
     *
     * @param binaryData
     *            Array containing binaryData
     * @return Encoded Base64 array
     */
    public static String encode(byte[] binaryData)
    {

        if (binaryData == null)
        {
            return null;
        }

        int lengthDataBits = binaryData.length * EIGHTBIT;
        if (lengthDataBits == 0)
        {
            return "";
        }

        int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
        int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
        int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1
                : numberTriplets;
        char encodedData[] = null;

        encodedData = new char[numberQuartet * 4];

        byte k = 0, l = 0, b1 = 0, b2 = 0, b3 = 0;

        int encodedIndex = 0;
        int dataIndex = 0;
        if (fDebug)
        {
            System.out.println("number of triplets = " + numberTriplets);
        }

        for (int i = 0; i < numberTriplets; i++)
        {
            b1 = binaryData[dataIndex++];
            b2 = binaryData[dataIndex++];
            b3 = binaryData[dataIndex++];

            if (fDebug)
            {
                System.out.println("b1= " + b1 + ", b2= " + b2 + ", b3= " + b3);
            }

            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
                    : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4)
                    : (byte) ((b2) >> 4 ^ 0xf0);
            byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6)
                    : (byte) ((b3) >> 6 ^ 0xfc);

            if (fDebug)
            {
                System.out.println("val2 = " + val2);
                System.out.println("k4   = " + (k << 4));
                System.out.println("vak  = " + (val2 | (k << 4)));
            }

            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | (k << 4)];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[(l << 2) | val3];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[b3 & 0x3f];
        }

        // form integral number of 6-bit groups
        if (fewerThan24bits == EIGHTBIT)
        {
            b1 = binaryData[dataIndex];
            k = (byte) (b1 & 0x03);
            if (fDebug)
            {
                System.out.println("b1=" + b1);
                System.out.println("b1<<2 = " + (b1 >> 2));
            }
            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
                    : (byte) ((b1) >> 2 ^ 0xc0);
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[k << 4];
            encodedData[encodedIndex++] = PAD;
            encodedData[encodedIndex++] = PAD;
        }
        else if (fewerThan24bits == SIXTEENBIT)
        {
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
                    : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4)
                    : (byte) ((b2) >> 4 ^ 0xf0);

            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | (k << 4)];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[l << 2];
            encodedData[encodedIndex++] = PAD;
        }

        return new String(encodedData);
    }

    /**
     * Decodes Base64 data into octects
     *
     * @param encoded
     *            string containing Base64 data
     * @return Array containind decoded data.
     */
    public static byte[] decode(String encoded)
    {

        if (encoded == null)
        {
            return null;
        }

        char[] base64Data = encoded.toCharArray();
        // remove white spaces
        int len = removeWhiteSpace(base64Data);

        if (len % FOURBYTE != 0)
        {
            return null;// should be divisible by four
        }

        int numberQuadruple = (len / FOURBYTE);

        if (numberQuadruple == 0)
        {
            return new byte[0];
        }

        byte decodedData[] = null;
        byte b1 = 0, b2 = 0, b3 = 0, b4 = 0;
        char d1 = 0, d2 = 0, d3 = 0, d4 = 0;

        int i = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        decodedData = new byte[(numberQuadruple) * 3];

        for (; i < numberQuadruple - 1; i++)
        {

            if (!isData((d1 = base64Data[dataIndex++]))
                    || !isData((d2 = base64Data[dataIndex++]))
                    || !isData((d3 = base64Data[dataIndex++]))
                    || !isData((d4 = base64Data[dataIndex++])))
            {
                return null;
            }// if found "no data" just return null

            b1 = base64Alphabet[d1];
            b2 = base64Alphabet[d2];
            b3 = base64Alphabet[d3];
            b4 = base64Alphabet[d4];

            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
        }

        if (!isData((d1 = base64Data[dataIndex++]))
                || !isData((d2 = base64Data[dataIndex++])))
        {
            return null;// if found "no data" just return null
        }

        b1 = base64Alphabet[d1];
        b2 = base64Alphabet[d2];

        d3 = base64Data[dataIndex++];
        d4 = base64Data[dataIndex++];
        if (!isData((d3)) || !isData((d4)))
        {// Check if they are PAD characters
            if (isPad(d3) && isPad(d4))
            {
                if ((b2 & 0xf) != 0)// last 4 bits should be zero
                {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 1];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                return tmp;
            }
            else if (!isPad(d3) && isPad(d4))
            {
                b3 = base64Alphabet[d3];
                if ((b3 & 0x3) != 0)// last 2 bits should be zero
                {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 2];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
                tmp[encodedIndex] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
                return tmp;
            }
            else
            {
                return null;
            }
        }
        else
        { // No PAD e.g 3cQl
            b3 = base64Alphabet[d3];
            b4 = base64Alphabet[d4];
            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);

        }

        return decodedData;
    }

    /**
     * remove WhiteSpace from MIME containing encoded Base64 data.
     *
     * @param data
     *            the byte array of base64 data (with WS)
     * @return the new length
     */
    private static int removeWhiteSpace(char[] data)
    {
        if (data == null)
        {
            return 0;
        }

        // count characters that's not whitespace
        int newSize = 0;
        int len = data.length;
        for (int i = 0; i < len; i++)
        {
            if (!isWhiteSpace(data[i]))
            {
                data[newSize++] = data[i];
            }
        }
        return newSize;
    }
}

