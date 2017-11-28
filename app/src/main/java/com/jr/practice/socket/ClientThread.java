package com.jr.practice.socket;

import android.view.View;

import com.jr.practice.activity.ChatRoomActivity;
import com.jr.practice.utils.L;
import com.jr.practice.utils.T;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by yzxdm on 2017/11/27.
 * Description socket客户端线程
 */

public class ClientThread extends Thread {
    private Socket socket;
    private BufferedReader bufferedReader;
    private ChatRoomActivity chatRoomActivity;
    private TempHandler tempHandler = new TempHandler();

    private static final String IP = "192.168.1.126";
    private static final int SOCKET_PORT = 50000;

    public ClientThread(ChatRoomActivity chatRoomActivity) {
        this.chatRoomActivity = chatRoomActivity;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(IP, SOCKET_PORT);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            chatRoomActivity.runOnUiThread(() -> {
                T.tLong("无法连接到socket服务器,IP:" + IP + ",PORT:" + SOCKET_PORT);
                chatRoomActivity.changeState("无法连接到服务器:" + IP);
            });
        }
        chatRoomActivity.runOnUiThread(() -> {
            chatRoomActivity.changeState(View.GONE);
            T.tLong("已连接到服务器：" + IP);
        });
        String content;
        try {
            while ((content = bufferedReader.readLine()) != null) {
                L.i("接收到：" + content);
                final String finalStr = content;
                chatRoomActivity.runOnUiThread(() -> chatRoomActivity.hasNewData(finalStr));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        new Thread(() -> {
            try {
                BufferedOutputStream dataOutputStream = new BufferedOutputStream(socket.getOutputStream());
                dataOutputStream.write((msg + "\n").getBytes());
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
                L.i("粗事了");
            }
        }).start();

    }

    public void stopSocket() {
        try {
            socket.shutdownInput();
            socket.shutdownOutput();
        } catch (IOException e) {
            e.printStackTrace();
            L.i("停止运行");
        }
    }
}
