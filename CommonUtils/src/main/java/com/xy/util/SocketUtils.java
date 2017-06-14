package com.xy.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by xy on 2017/2/24.
 */
public class SocketUtils {

    public static byte[] sendPack(String host, int port, byte[] pack) {

        Socket socket = null;
        OutputStream os = null;
        InputStream is = null;

        byte[] buffer = new byte[1024];

        try {
            socket = new Socket(host, port);
            os = socket.getOutputStream();
            os.write(pack);
            is = socket.getInputStream();
            is.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer;
    }

}
