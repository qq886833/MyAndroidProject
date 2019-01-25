package com.bsoft.module_splash.guide;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bsoft.base.XbaseActivity;
import com.bsoft.common.constant.ConstantUrl;

import com.bsoft.module_splash.R;
import com.bsoft.utils.TPreferences;


/**
 * Created by Administrator on 2017/4/26.
 */

public class GuideActivity extends XbaseActivity {
    
    ViewPager viewPager;
    GuideAdapter pagerAdapter;

    //1 第一次进入  2 关于那里进入
    public int flag = 1;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        flag = getIntent().getIntExtra("flag", 1);

        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new GuideAdapter(getSupportFragmentManager());
        TypedArray guideArr = getResources().obtainTypedArray(R.array.guide_res);
        int length = guideArr.length();
        for(int i = 0; i < length; i++){
            pagerAdapter.addItem(CommonFragment.getInstance(guideArr.getResourceId(i, 0), i == length-1));
        }
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);
    }



    @Override
    public void findView() {
        
    }

    public void goIn() {
        if (flag == 1) {
            TPreferences.getInstance().setStringData("first", "1");
//            Intent intent = new Intent(baseContext, LoginActivity.class);
//            startActivity(intent);
//            finish();
            ARouter.getInstance().build(ConstantUrl.LOGIN).navigation();
            finish();
        } else if (flag == 2) {
            finish();
        }
    }
}
