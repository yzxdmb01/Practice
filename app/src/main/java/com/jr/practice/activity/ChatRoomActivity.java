package com.jr.practice.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jr.practice.R;
import com.jr.practice.entity.Message;
import com.jr.practice.socket.ClientThread;
import com.jr.practice.utils.T;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yzxdm on 2017/11/24.
 */

public class ChatRoomActivity extends BaseActivity {
    private EditText etContent;
    private Button btnSend;
    private ListView mListView;
    private List<Message> messageList = new ArrayList<>();
    private ClientThread clientThread;
    private OutputStream outputStream;
    private TextView tvState;
    private AlertDialog alertDialog;

    private static final int SOCKET_PORT = 50000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        findView();
        initData();
        startSocket();
    }

    private void startSocket() {
        clientThread = new ClientThread(this);
        clientThread.start();
    }

    public void hasNewData(String content) {
        Message msg = new Message();
        msg.setSelf(false);
        msg.setContent(content);
        messageList.add(msg);
        ((ListViewAdapter) mListView.getAdapter()).notifyDataSetChanged();
        mListView.smoothScrollToPosition(mListView.getCount() - 1);
    }

    private void initData() {
        mListView.setAdapter(new ListViewAdapter(ChatRoomActivity.this));
    }

    private void findView() {
        etContent = (EditText) findViewById(R.id.et_content);
        mListView = (ListView) findViewById(R.id.lv_message);
        tvState = (TextView) findViewById(R.id.tv_state);

        btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(v -> sendMsg());
        etContent.setOnEditorActionListener((v, actionId, event) -> {
            sendMsg();
            return true;
        });
        etContent.setOnFocusChangeListener((v, hasFocus) -> T.t("hasFocus:" + hasFocus));
        setTitle("聊天室");
    }

    private void sendMsg() {

        String inputContent = etContent.getText().toString();
        if (TextUtils.isEmpty(inputContent)) return;
        Message message = new Message();
        message.setContent(inputContent);
        message.setSelf(true);
        message.setDate(new Date());
        messageList.add(message);
        ((ListViewAdapter) mListView.getAdapter()).notifyDataSetChanged();
        mListView.smoothScrollToPosition(mListView.getCount() - 1);
        clientThread.sendMsg(inputContent);
        etContent.setText("");

    }

    private class ListViewAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;

        public ListViewAdapter(Context context) {
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return messageList.size();
        }

        @Override
        public Object getItem(int position) {
            return messageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_chat_bubble, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }

            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.bindData(position);
            return convertView;
        }


        private class ViewHolder {
            View convertView;
            TextView tvLeftContent;
            TextView tvRightContent;
            RelativeLayout rlLeft;
            RelativeLayout rlRight;

            public ViewHolder(View convertView) {
                this.convertView = convertView;
                tvLeftContent = (TextView) convertView.findViewById(R.id.tv_left_content);
                tvRightContent = (TextView) convertView.findViewById(R.id.tv_right_content);
                rlLeft = (RelativeLayout) convertView.findViewById(R.id.rl_left);
                rlRight = (RelativeLayout) convertView.findViewById(R.id.rl_right);
            }

            public void bindData(int rowIndex) {
                Message message = messageList.get(rowIndex);
                if (message.isSelf()) {
                    rlLeft.setVisibility(View.GONE);
                    rlRight.setVisibility(View.VISIBLE);
                } else {
                    rlLeft.setVisibility(View.VISIBLE);
                    rlRight.setVisibility(View.GONE);
                }
                tvLeftContent.setText(message.getContent());
                tvRightContent.setText(message.getContent());
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void changeState(String msg, int visibility) {
        tvState.setText(msg);
        tvState.setVisibility(visibility);
    }

    public void changeState(String msg) {
        changeState(msg, View.VISIBLE);
    }

    public void changeState(int visibility) {
        changeState("", visibility);
    }

}
