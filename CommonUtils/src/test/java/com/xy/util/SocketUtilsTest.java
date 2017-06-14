package com.xy.util;

import com.xy.util.SocketUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * SocketUtils Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>二月 25, 2017</pre>
 */
public class SocketUtilsTest {

    private static int PORT = 11101;

    @Before
    public void before() throws Exception {
        // start a socket listenner port
        new Thread(new Runnable() {
            public void run() {
                try {
                    ByteBuffer byteBuffers = ByteBuffer.wrap(new byte[1024]);
                    Socket socket = new ServerSocket(PORT).accept();
                    SocketChannel socketChannel = socket.getChannel();
                    socketChannel.read(byteBuffers);
                    System.out.println(new String(byteBuffers.array()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: sendPack(String host, int port, byte[] pack)
     */
    @Test
    public void testSendPack() throws Exception {
        byte[] recv = SocketUtils.sendPack("127.0.0.1", PORT, "Helllo World".getBytes());
        System.out.println(new String(recv));
    }


} 
