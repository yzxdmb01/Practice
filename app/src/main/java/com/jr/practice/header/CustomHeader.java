package com.jr.practice.header;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jr.practice.R;
import com.jr.practice.utils.L;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import in.srain.cube.views.ptr.indicator.PtrTensionIndicator;

/**
 * Created by Administrator on 2016-08-25.
 */

public class CustomHeader extends LinearLayout implements PtrUIHandler {
    private PtrFrameLayout ptrFrameLayout;
    private PtrTensionIndicator ptrTensionIndicator;
    private ImageView ivProgressBar;
    private View header;

    public CustomHeader(Context context) {
        super(context);
        init();
    }

    public void setUp(PtrFrameLayout ptrFrameLayout) {
        this.ptrFrameLayout = ptrFrameLayout;
        ptrTensionIndicator = new PtrTensionIndicator();
//        ptrFrameLayout.setPtrIndicator(ptrTensionIndicator);
    }

    private void init() {
        header = LayoutInflater.from(getContext()).inflate(R.layout.header_custom, this);
        ivProgressBar = (ImageView) header.findViewById(R.id.iv_progress_bar);
    }

    public CustomHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {

    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        ivProgressBar.setImageLevel((int) ((Math.min(1, ptrIndicator.getCurrentPercent()) * 85)));
    }
}
