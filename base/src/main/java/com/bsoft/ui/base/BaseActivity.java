package com.bsoft.ui.base;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.bsoft.AppApplication;
import com.bsoft.base.R;
import com.bsoft.utils.ExitUtil;
import com.bsoft.utils.LiveDataBus;
import com.bsoft.utils.barUtil.StatusBarUtil;
import com.bsoft.widget.BsoftActionBar;
import com.bsoft.widget.dialog.LoadingDialog;
import com.bsoft.widget.loading.LoadViewHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


public abstract class BaseActivity extends RxAppCompatActivity {

    LoadingDialog loadingDialog;
    public Context baseContext;
    public BsoftActionBar actionBar;
    protected RxPermissions rxPermissions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.baseContext = this;
        loadingDialog = new LoadingDialog();
        rxPermissions = new RxPermissions(this);

        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);

        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }

    }

    public abstract int getActionBarBg();
    public abstract int getActionBarTitleColor();
    public abstract int getActionBarActionColor();
    public void findActionBar() {
        actionBar = (BsoftActionBar) findViewById(R.id.actionbar);
        actionBar.setBackGround(ContextCompat.getColor(baseContext, getActionBarBg()));
        actionBar.setTitleColor(ContextCompat.getColor(baseContext, getActionBarTitleColor()));
        actionBar.setActionTxtColor(ContextCompat.getColor(baseContext, getActionBarActionColor()));
    }

    public void showToast(String text) {
        if (text != null && !text.trim().equals("")) {
               Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            //  ToastUtil.showAtCenter(baseContext,text);
           // EToastUtils.show(text);
        }
    }
    public void showToast(@StringRes int resId) {
        Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public void showLoadingDialog(){
        showLoadingDialog(null);
    }
    public void showLoadingDialog(String msg){
        loadingDialog.build(this).show(msg);
    }
    public void dismissLoadingDialog(){
        loadingDialog.dismiss();
    }

    public boolean isLoadingDialogShowing(){
        return loadingDialog.isShowing();
    }

    @Override
    protected void onDestroy() {

        ExitUtil.clear();
        dismissLoadingDialog();

        super.onDestroy();
    }


}
