package com.bsoft.componentX;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bsoft.AppApplication;
import com.bsoft.config.AppContext;
import com.bsoft.constant.Constants;
import com.facebook.drawee.backends.pipeline.Fresco;

public  class MyAppAplication extends AppApplication {


    @Override
    public void onCreate() {
        super.onCreate();

        AppContext.initialize(getApplicationContext(), Constants.HTTP_URL, "");
        Fresco.initialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);
    }

    }
