package com.jr.practice.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jr.practice.R;
import com.jr.practice.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * 判断listview的滑动方向以及是否到顶部、底部
 */
public class ListViewActivity extends BaseActivity {
    private ListView mListView;
    private TextView tvTitle;
    private TextView tvBottom;
    private TextView tvStatus;
    TextView tvTest;
    private ListViewAdapter mAdapter;
    private List<Character> mData;
    private int lastFirstVisibleItem = 0;  //上一次滑动时第一个可见item的position
    private int lastItemTop = 0;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        findView();
        initData();
        initListView();
    }

    private void initData() {
        String[] texts = {
                "0老阿三扥老萨单飞了阿赛况解放啦扫街父老阿囧概况啊啊扫谅解法拉扫将",
                "1拉扫单弗拉空单分拉肯单",
                "2aklsdjf;ldkj阿龙框架房;asldkfjsa;dlkfjsalkjgad;lfkgj我怕厄尔we破人阿龙道具费",
                "3alskd",
                "4捱三带三大充电",
                "5老阿三扥老萨单飞了阿赛况解放啦扫街父老阿囧概况啊啊扫谅解法拉扫将",
                "6拉扫单弗拉空单分拉肯单",
                "7aklsdjf;ldkj阿龙框架房;asldkfjsa;dlkfjsalkjgad;lfkgj我怕厄尔we破人阿龙道具费",
                "8alskd",
                "9捱三带三大充电"
        };
        for (String s : texts) {
            list.add(s);
        }
    }

    private void initListView() {
        tvTest = (TextView) findViewById(R.id.textview1);
        mData = new ArrayList<>();
        for (int i = 'A'; i <= 'z'; i++) {
            mData.add((char) i);
        }
//        mAdapter = new ListViewAdapter(this);
//        mListView.setAdapter(mAdapter);
        mListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mData));
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (firstVisibleItem >= 4) {
//                    tvTest.setVisibility(View.VISIBLE);
//                } else {
//                    tvTest.setVisibility(View.GONE);
//                }
                boolean canScrollVertically = view.canScrollVertically(1);
//                L.i("canScrollVertically:" + canScrollVertically);

                if (visibleItemCount <= 0) {
                    return;
                }
                if (lastFirstVisibleItem == firstVisibleItem) {
                    //第一个可见item的top值
                    int itemTop = view.getChildAt(0).getTop();
//                    Log.i(TAG, "itemTop:" + itemTop + ",lastItemTop:" + lastItemTop);
                    if (itemTop != lastItemTop && lastItemTop != 0) {
                        tvStatus.setText(itemTop - lastItemTop > 0 ? "上" : "下");
                    }
                    lastItemTop = itemTop;
                } else {
                    //只需要判断两次的位置即可判断出listview滑动方向
                    tvStatus.setText(firstVisibleItem - lastFirstVisibleItem > 0 ? "下" : "上");
                    lastItemTop = 0;
                }
                lastFirstVisibleItem = firstVisibleItem;
                if (firstVisibleItem == 0 && view.getChildAt(0).getTop() == 0) {
                    tvStatus.setText("顶部");
                }
//                Log.i(TAG, "lvp:" + view.getLastVisiblePosition() + "," + "firstVisible:" + firstVisibleItem);
                if (view.getLastVisiblePosition() == totalItemCount - 1 && view.getChildAt(visibleItemCount - 1).getBottom() <= view.getHeight()) {
                    tvStatus.setText("底部啊");
                }
            }
        });
    }

    private void findView() {
        mListView = (ListView) findViewById(R.id.listview);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvBottom = (TextView) findViewById(R.id.tv_bottom);
        tvStatus = (TextView) findViewById(R.id.tv_status);

        Button btnTop = (Button) findViewById(R.id.btn_top);
        btnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.post(new Runnable() {
                    @Override
                    public void run() {
                        mData.remove(0);
//                        ((ArrayAdapter) mListView.getAdapter()).notifyDataSetChanged();
                    }
                });
            }
        });


    }

    private class ListViewAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;

        public ListViewAdapter(Context context) {
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_textlistview, parent, false);
                viewHolder = new ViewHolder();
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textview);
            viewHolder.textView.setText(list.get(position));
            convertView.setBackgroundColor(position % 2 == 0 ? Color.RED : Color.BLUE);
            return convertView;
        }

        private class ViewHolder {
            TextView textView;
        }
    }

}
