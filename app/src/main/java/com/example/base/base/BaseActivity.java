package com.example.base.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.example.base.R;
import com.example.base.base.loading.CommonLoading;
import com.example.base.base.nonetwork.CommonNoNetWorkFragment;
import com.example.base.base.nonetwork.CommonNoNetWorkUtils;
import com.example.base.base.titlebar.NormalTitleBar;
import com.example.base.base.titlebar.TitleBarFactory;
import com.example.base.base.titlebar.TitleBarWrapper;
import com.example.base.utils.CommonToast;

/**
 * @author zpan
 * @date 2019/3/29 17:53
 *
 * description: Activity基类-公用title
 */
public abstract class BaseActivity extends BaseZpActivity {

    private CommonNoNetWorkFragment mNoNetWorkFragment;
    private CommonLoading mCommonLoading;
    private View contentView;

    @Override
    protected void buildInitLib() {

    }

    @Override
    public void buildInitView(Bundle savedInstanceState) {
        setContentView(R.layout.common_activity_base_view);
        buildOnCreatBundle(savedInstanceState);
        TitleBarWrapper titleBarWrapper = findViewById(R.id.ctb_common_title_bar);
        View titleBarContent = exInitTitleBar(new TitleBarFactory(this));
        titleBarWrapper.removeAllViews();
        if (titleBarContent != null) {
            titleBarWrapper.addView(titleBarContent);
        } else {
            titleBarWrapper.setVisibility(View.GONE);
        }
        mCommonLoading = findViewById(R.id.cl_common_loading);
        FrameLayout commonContent = findViewById(R.id.fl_common_content);
        contentView = LayoutInflater.from(this).inflate(exInitLayout(), null);
        commonContent.addView(contentView);
        refreshInitView();
    }

    @Override
    protected void buildOnCreatBundle(Bundle savedInstanceState) {

    }

    @Override
    public void refreshInitView() {
        if (!CommonNoNetWorkUtils.isConnected(this)) {
            CommonNoNetWorkFragment.OnNoNetWorkCallBack callBack = getNoNetWorkCallback();
            if (callBack != null) {
                showNotNetwork(getNoNetWorkCallback());
                return;
            }
        }
        if (contentView != null) {
            contentView.setVisibility(View.VISIBLE);
        }
        if (mNoNetWorkFragment != null) {
            CommonNoNetWorkUtils.getInstance().hideNoNetWorkFragment(getSupportFragmentManager(), mNoNetWorkFragment);
        }
        exInitView();
    }

    /**
     * 返回定制化的titleBar布局
     *
     * @return 定制化titleBar布局
     */
    @Override
    protected View exInitTitleBar(TitleBarFactory titleBarFactory) {
        NormalTitleBar normalTitleBar = (NormalTitleBar) titleBarFactory.build(TitleBarFactory.COMMON_TITLE_TYPE_NORMAL);
        String titleBarLabel = getResources().getString(exTitleBarLabel());
        normalTitleBar.setTitleLabel(titleBarLabel);
        return normalTitleBar.getContentView();
    }

    @Override
    public void showCommonLoading() {
        if (mCommonLoading.getVisibility() == View.GONE) {
            mCommonLoading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hindCommonLoading() {
        if (mCommonLoading.getVisibility() == View.VISIBLE) {
            mCommonLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public void showCommonToast(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        CommonToast.getInstance(this).setText(msg).show();
    }

    @Override
    public void showCommonToast(int msgId) {
        if (msgId <= 0) {
            return;
        }
        CommonToast.getInstance(this).setTextId(msgId).show();
    }

    protected CommonNoNetWorkFragment.OnNoNetWorkCallBack getNoNetWorkCallback() {
        return new CommonNoNetWorkFragment.OnNoNetWorkCallBack() {
            @Override
            public void clickRefresh() {
                refreshInitView();
                refreshNoNetWork();
            }
        };
    }

    @Override
    public void refreshNoNetWork() {

    }

    private void showNotNetwork(CommonNoNetWorkFragment.OnNoNetWorkCallBack callback) {
        if (contentView != null) {
            contentView.setVisibility(View.GONE);
        }
        mNoNetWorkFragment = CommonNoNetWorkUtils.getInstance().getNoNetworkFragment(getSupportFragmentManager(), mNoNetWorkFragment);
        mNoNetWorkFragment.setNoNetWorkCallBack(callback);
        CommonNoNetWorkUtils.getInstance().showNoNetWorkFragment(R.id.fl_common_content, getSupportFragmentManager(), mNoNetWorkFragment);
    }
}
