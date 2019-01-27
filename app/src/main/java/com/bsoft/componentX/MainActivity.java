package com.bsoft.componentX;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bsoft.base.XbaseActivity;
import com.bsoft.common.constant.ConstantUrl;
import com.bsoft.utils.barUtil.StatusBarUtil;

public class MainActivity extends XbaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setStatusBarColor(this,0xFFDFD221);
        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        ARouter.getInstance().build(ConstantUrl.MAINTAB).navigation();
        ARouter.getInstance().build(ConstantUrl.NDK).navigation();

    }

    @Override
    public void findView() {

    }


}
