package com.bsoft.base;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsoft.AppApplication;
import com.bsoft.constant.Constants;
import com.bsoft.push.PushInfo;
import com.bsoft.ui.base.BaseActivity;
import com.bsoft.utils.EffectUtil;
import com.bsoft.utils.ExitUtil;
import com.bsoft.utils.LiveDataBus;
import com.bsoft.utils.StringUtil;

import com.bsoft.widget.loading.LoadViewHelper;

import java.util.List;

public abstract  class XbaseActivity extends BaseActivity {
    public BaseActivity baseActivity;
    public LoadViewHelper viewHelper;
    public AppApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.application = (AppApplication) getApplication();
        this.baseActivity = this;
//        StatusBarUtils.setStatusBar(this, false, false);
//        StatusUtil.setColor(baseActivity,getResources().getColor(R.color.colorPrimary));
        LiveDataBus.get().with(Constants.Logout_ACTION, PushInfo.class)
                .observe(this, new Observer<PushInfo>() {
                    @Override
                    public void onChanged(@Nullable PushInfo pushInfo) {
                        showDialog(pushInfo.title, pushInfo.description, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                              //Todo  退出
                            }
                        });
                    }
                });
    }

    public abstract void findView();
    @Override
    public int getActionBarBg() {
        return R.color.colorPrimary;
    }
    @Override
    public int getActionBarTitleColor() {
        return R.color.black_text;
    }
    @Override
    public int getActionBarActionColor() {
        return R.color.colorAccent;
    }

    public void back() {
        hideKeyboard();
        finish();
    }

    /**
     * 提示框（统一风格）
     */
    private Dialog alertDialog;

    public void showDialog(String title, String content, String confirmStr, String cancelstr, final View.OnClickListener confirmListener, final View.OnClickListener cancellistener) {
        showDialog(baseContext, title, content, null, confirmStr, cancelstr, confirmListener, cancellistener, false);
    }
    public void showDialog(String title, String content, final View.OnClickListener confirmListener, final View.OnClickListener cancellistener) {
        showDialog(baseContext, title, content, null, "确认", "取消", confirmListener, cancellistener, false);
    }
    public void showDialog(String title, String content, final View.OnClickListener confirmListener) {
        showDialog(baseContext, title, content, null, "确认", "取消", confirmListener, null, false);
    }
    public void showDialog(String title, String content, final View.OnClickListener confirmListener, boolean isShowCancel) {
        showDialog(baseContext, title, content, null, "确认", isShowCancel ? "取消" : "", confirmListener, null, false);
    }
    public void showDialog(String title, String content, String info, final View.OnClickListener confirmListener, boolean isShowCancel) {
        showDialog(baseContext, title, content, info, "确认", isShowCancel ? "取消" : "", confirmListener, null, false);
    }

    public void showDialog(Context baseContext, String title, String content, String info,
                           String confirmStr, String cancelstr,
                           final View.OnClickListener confirmListener,
                           final View.OnClickListener cancellistener,
                           boolean isCancelable) {
        if (alertDialog != null && alertDialog.isShowing()){ return;}
        alertDialog = new Dialog(baseContext, R.style.alertDialogTheme);
        View viewDialog = LayoutInflater.from(baseContext).inflate(R.layout.dialog_alert, null);
        // 设置对话框的宽高
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                AppApplication.getWidthPixels() * 85 / 100, LinearLayout.LayoutParams.WRAP_CONTENT);
        alertDialog.setCancelable(isCancelable);
        alertDialog.setCanceledOnTouchOutside(isCancelable);
        alertDialog.setContentView(viewDialog, layoutParams);

        //确定按钮
        TextView tv_confirm = (TextView) viewDialog.findViewById(R.id.tv_confirm);
        //取消按钮，默认隐藏
        TextView tv_cancel = (TextView) viewDialog.findViewById(R.id.tv_cancel);
        EffectUtil.addClickEffect(tv_confirm);
        EffectUtil.addClickEffect(tv_cancel);

        if (!TextUtils.isEmpty(confirmStr) && !TextUtils.isEmpty(cancelstr)) {
            viewDialog.findViewById(R.id.tv_divider).setVisibility(View.VISIBLE);
//			tv_confirm.setBackgroundResource(R.drawable.item_selected_white);
//			tv_cancel.setBackgroundResource(R.drawable.item_selected_white);
        }
        if (!TextUtils.isEmpty(confirmStr)) {
            tv_confirm.setText(confirmStr);
        }
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmListener != null) {
                    confirmListener.onClick(v);
                    dismissAlertDialog();
                }
                else {
                    dismissAlertDialog();
                }
            }
        });

        if (!TextUtils.isEmpty(cancelstr)) {
            tv_cancel.setVisibility(View.VISIBLE);
            tv_cancel.setText(cancelstr);
        }else{
            tv_cancel.setVisibility(View.GONE);
        }
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancellistener != null) {
                    cancellistener.onClick(v);
                    dismissAlertDialog();
                }
                else {
                    dismissAlertDialog();
                }
            }
        });

        TextView tv_content = (TextView) alertDialog.findViewById(R.id.tv_content);
        //标题默认隐藏
        if (!TextUtils.isEmpty(title)) {
            TextView tv_title = (TextView) alertDialog.findViewById(R.id.tv_title);
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(title);
        }
        if (!TextUtils.isEmpty(info)) {
            TextView tvInfo = (TextView) alertDialog.findViewById(R.id.tv_info);
            tvInfo.setVisibility(View.VISIBLE);
            tvInfo.setText(info);
        }
        if (!TextUtils.isEmpty(content)) {
            tv_content.setText(content);
        }
        alertDialog.show();
    }
    /**
     * 底部列表提示框
     */
    public void showListDialog(String title, List<String> btn_strs, final View.OnClickListener listener) {
        final Dialog dialog = new Dialog(baseContext, R.style.dialog_fullscreen);
        dialog.show();
        View viewDialog = LayoutInflater.from(baseContext).inflate(R.layout.layout_alert_list_dialog, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(AppApplication.getWidthPixels(), LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(viewDialog, lp);
        if (!StringUtil.isEmpty(title)) {
            TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
            tv_title.setText(title);
        }
        LinearLayout parent = (LinearLayout) viewDialog.findViewById(R.id.dialogLayout);
        parent.removeAllViews();
        int length = btn_strs.size();
        for (int i = 0; i < length; i++) {
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(-1, -2);
            params1.rightMargin = 1;
            final TextView tv = new TextView(baseContext);
            tv.setLayoutParams(params1);
            tv.setTextSize(18);
            tv.setText(btn_strs.get(i));
            tv.setTag(i);
            int pad = baseContext.getResources().getDimensionPixelSize(R.dimen.padding10);
            tv.setPadding(pad, pad, pad, pad);
            tv.setSingleLine(true);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(baseContext.getResources().getColor(R.color.blue));
            if (i != length - 1)
                tv.setBackgroundResource(R.drawable.dialog_select);
            else
                tv.setBackgroundResource(R.drawable.dialog_item_selector);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    if (listener != null)
                        listener.onClick(arg0);
                }
            });
            parent.addView(tv);
            if (i != length - 1) {
                TextView divider = new TextView(baseContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, (int) 1);
                divider.setLayoutParams(params);
                divider.setBackgroundResource(R.color.divider2bg);
                parent.addView(divider);
            }
        }
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        viewDialog.findViewById(R.id.btn_cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();
                    }
                });
    }
    public void dismissAlertDialog(){
        if(alertDialog != null && alertDialog.isShowing()){
            alertDialog.dismiss();
        }
    }
    @Override
    protected void onDestroy() {
        dismissAlertDialog();
        ExitUtil.clear();
        super.onDestroy();
    }

    public int getDialogWidth() {
        return AppApplication.getWidthPixels() * 75 / 100;
    }

    public void showErrorView() {
        showErrorView("加载失败，点击重试");
    }

    public void showErrorView(String error) {
        if(null!=viewHelper){
            viewHelper.showError(error);
        }
    }

    public void showEmptyView() {
        if(null!=viewHelper){
            viewHelper.showEmpty();
        }
    }
    public void showEmptyView(View view) {
        if(null!=viewHelper){
            viewHelper.showEmpty(view);
        }
    }
    public void showLoadingView() {
        if(null!=viewHelper){
            viewHelper.showLoading();
        }
    }

    public void restoreView() {
        if(null!=viewHelper){
            viewHelper.restore();
        }
    }

    public void hideKeyboard(){
        if (null != getCurrentFocus()
                && null != getCurrentFocus().getWindowToken()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            // 隐藏软键盘
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public void showKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(getWindow().getDecorView(), InputMethodManager.SHOW_FORCED);
    }
    public void showKeyboard(EditText editText){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(editText.getWindowToken(), 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(isLoadingDialogShowing()){
                finish();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }


}
