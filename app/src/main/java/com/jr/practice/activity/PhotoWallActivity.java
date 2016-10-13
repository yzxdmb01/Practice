package com.jr.practice.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.jr.practice.R;
import com.jr.practice.utils.ImageLoader;
import com.jr.practice.utils.L;
import com.jr.practice.utils.ScreenUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-07-20.
 */
public class PhotoWallActivity extends BaseActivity {
    private ArrayList<String> mUrlList = new ArrayList<>();
    private ImageLoader mImageLoader;
    private GridView mGridView;
    private int mImageWidth;
    private Button btnTest;
    private ImageAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_wall);
        initData();
        initGridView();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                L.i("???");
//                sleep();
            }
        });
    }

    private void initGridView() {
        mGridView = (GridView) findViewById(R.id.grid_view);
        mAdapter = new ImageAdapter(this);
        mGridView.setAdapter(mAdapter);
    }

    private void initData() {
        btnTest = (Button) findViewById(R.id.btn_test);
        btnTest.setOnClickListener(v -> {
            Toast.makeText(PhotoWallActivity.this, "哈航啊哈哈红扥", Toast.LENGTH_LONG).show();
            Process.killProcess(Process.myPid());
        });

        mImageLoader = new ImageLoader(this);
        String[] imageUrls = {
                "http://b.hiphotos.baidu.com/zhidao/pic/item/a6efce1b9d16fdfafee0cfb5b68f8c5495ee7bd8.jpg",
                "http://pic47.nipic.com/20140830/7487939_180041822000_2.jpg",
                "http://pic41.nipic.com/20140518/4135003_102912523000_2.jpg",
                "http://img2.imgtn.bdimg.com/it/u=1133260524,1171054226&fm=21&gp=0.jpg",
                "http://h.hiphotos.baidu.com/image/pic/item/3b87e950352ac65c0f1f6e9efff2b21192138ac0.jpg",
                "http://pic42.nipic.com/20140618/9448607_210533564001_2.jpg",
                "http://pic10.nipic.com/20101027/3578782_201643041706_2.jpg",
                "http://picview01.baomihua.com/photos/20120805/m_14_634797817549375000_37810757.jpg",
                "http://img2.3lian.com/2014/c7/51/d/26.jpg",
                "http://img3.3lian.com/2013/c1/34/d/93.jpg",
                "http://b.zol-img.com.cn/desk/bizhi/image/3/960x600/1375841395686.jpg",
                "http://picview01.baomihua.com/photos/20120917/m_14_634834710114218750_41852580.jpg",
                "http://cdn.duitang.com/uploads/item/201311/03/20131103171224_rr2aL.jpeg",
                "http://imgrt.pconline.com.cn/images/upload/upc/tx/wallpaper/1210/17/c1/spcgroup/14468225_1350443478079_1680x1050.jpg",
                "http://pic41.nipic.com/20140518/4135003_102025858000_2.jpg",
                "http://www.1tong.com/uploads/wallpaper/landscapes/200-4-730x456.jpg",
                "http://pic.58pic.com/58pic/13/00/22/32M58PICV6U.jpg",
                "http://picview01.baomihua.com/photos/20120629/m_14_634765948339062500_11778706.jpg",
                "http://h.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=429e7b1b92ef76c6d087f32fa826d1cc/7acb0a46f21fbe09cc206a2e69600c338744ad8a.jpg",
                "http://cdn.duitang.com/uploads/item/201405/13/20140513212305_XcKLG.jpeg",
                "http://photo.loveyd.com/uploads/allimg/080618/1110324.jpg",
                "http://img4.duitang.com/uploads/item/201404/17/20140417105820_GuEHe.thumb.700_0.jpeg",
                "http://cdn.duitang.com/uploads/item/201204/21/20120421155228_i52eX.thumb.600_0.jpeg",
                "http://img4.duitang.com/uploads/item/201404/17/20140417105856_LTayu.thumb.700_0.jpeg",
                "http://img04.tooopen.com/images/20130723/tooopen_20530699.jpg",
                "http://pic.dbw.cn/0/01/33/59/1335968_847719.jpg",
                "http://a.hiphotos.baidu.com/image/pic/item/a8773912b31bb051a862339c337adab44bede0c4.jpg",
                "http://h.hiphotos.baidu.com/image/pic/item/f11f3a292df5e0feeea8a30f5e6034a85edf720f.jpg",
                "http://img0.pconline.com.cn/pconline/bizi/desktop/1412/ER2.jpg",
                "http://pic.58pic.com/58pic/11/25/04/91v58PIC6Xy.jpg",
                "http://img3.3lian.com/2013/c2/32/d/101.jpg",
                "http://pic25.nipic.com/20121210/7447430_172514301000_2.jpg",
                "http://img02.tooopen.com/images/20140320/sy_57121781945.jpg"
        };
        for (String s : imageUrls) {
            mUrlList.add(s);
        }
        int screenWidth = ScreenUtils.getScreenWidth();
        int dp_20 = (int) ScreenUtils.dp2px(20);
        mImageWidth = (screenWidth - dp_20) / 3;


        //判断有没有网络
        if (!isWifiConnected()) findViewById(R.id.view_stub).setVisibility(View.VISIBLE);

    }

    private boolean isWifiConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null
                    && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.i("onResume22");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
//            L.i("width:" + btnTest.getMeasuredWidth());
        }
    }

    public void sleep() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class ImageAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private Drawable mDefaultBitmapDrawabel;

        public ImageAdapter(Context context) {
            layoutInflater = LayoutInflater.from(context);
            mDefaultBitmapDrawabel = context.getResources().getDrawable(R.mipmap.ic_launcher);
        }

        @Override
        public int getCount() {
            return mUrlList.size();
        }

        @Override
        public Object getItem(int position) {
            return mUrlList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_gridview, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.siv_gridview_item);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ImageView imageView = viewHolder.imageView;
            final String tag = (String) imageView.getTag();
            final String uri = (String) getItem(position);
//            if(!TextUtils.equals(tag,uri)){
//                imageView.setImageDrawable(mDefaultBitmapDrawabel);
//            }
//            if (!TextUtils.isEmpty(uri)) {
//                imageView.setTag(uri);
//                mImageLoader.bindBitmap(mUrlList.get(position), imageView, mImageWidth, mImageWidth);
//            }
            if (TextUtils.isEmpty(tag) || !TextUtils.equals(tag, uri)) {
                imageView.setTag(uri);
                mImageLoader.bindBitmap((String) imageView.getTag(), imageView, mImageWidth, mImageWidth);
            }


            return convertView;
        }

        private class ViewHolder {
            private ImageView imageView;
        }


    }

}
