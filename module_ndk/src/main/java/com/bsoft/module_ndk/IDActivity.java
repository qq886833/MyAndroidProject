package com.bsoft.module_ndk;

import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bsoft.base.XbaseActivity;
import com.bsoft.common.constant.ConstantUrl;

@Route(path = ConstantUrl.NDK)
public class IDActivity extends XbaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        TextView  tv = new TextView(this);
     //   tv.setText(Test.sayHello());
    }

    @Override
    public void findView() {

    }
}
