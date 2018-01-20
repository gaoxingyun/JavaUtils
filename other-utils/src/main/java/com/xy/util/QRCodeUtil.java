package com.xy.util;


import java.io.ByteArrayOutputStream;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * 二维码工具类
 *
 * @author xy
 */
public class QRCodeUtil {

    private final static int width = 200;// 图片的宽度
    private final static int height = 200;// 图片的高度
    private final static String imgFormat = "jpg";

    /**
     * 生成二维码
     *
     * @param content
     * @return
     */
    public static byte[] generateQrCode(String content) {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, imgFormat, stream);
            byte[] imageBytes = stream.toByteArray();
            return imageBytes;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
