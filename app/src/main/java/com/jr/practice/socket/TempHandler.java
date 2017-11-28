package com.jr.practice.socket;


import android.os.Handler;
import android.os.Message;

import com.jr.practice.utils.T;

/**
 * Created by yzxdm on 2017/11/27.
 */

public class TempHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
        T.t(msg.obj.toString());
    }
}
