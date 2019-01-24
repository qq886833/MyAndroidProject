package com.bsoft.componentX;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bsoft.common.constant.ConstantUrl;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        ARouter.getInstance().build(ConstantUrl.MAINTAB).navigation();
    }


}
