package com.bsoft.module_main.fragment;


import android.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bsoft.base.XbaseFragment;

import com.bsoft.module_main.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends XbaseFragment {

    private LinearLayout mLl_bar;

    @Override
    public void startHint() {
        if (isLoaded || !isReceiver) {
            return;
        }
        // taskGetData();
        isLoaded = true;
    }

    @Override
    public void endHint() {
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView(View rootView) {

        //Eyes.setStatusBarLightMode(getActivity(), Color.RED);
        mLl_bar = rootView.findViewById(R.id.ll_bar);
      //  StatusUtil.setColor(baseActivity,getResources().getColor(R.color.theme_bule));

        Button bt1=rootView.findViewById(R.id.bt1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   ARouter.getInstance().build(ConstantUrl.MAINTAB1).navigation();
            }
        });
    }

}
