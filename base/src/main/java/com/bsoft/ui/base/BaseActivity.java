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

import static com.bsoft.utils.barUtil.StatusBarUtil.setStatusBarDarkTheme;


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
