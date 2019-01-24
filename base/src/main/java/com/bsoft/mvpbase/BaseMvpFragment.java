package com.bsoft.mvpbase;

import android.os.Bundle;

import com.bsoft.base.XbaseActivity;
import com.bsoft.base.XbaseFragment;


import io.reactivex.annotations.Nullable;

public abstract  class BaseMvpFragment <V extends IView, T extends BasePresenter<V>> extends XbaseFragment implements IView{

    protected T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);//因为之后所有的子类都要实现对应的View接口
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void showLoading() {
//        if(actionBar!=null) {
//            actionBar.startTitleRefresh();
//        }
        ((XbaseActivity) getActivity()).showLoadingView();
    }

    @Override
    public void dismissLoading() {
//        if(actionBar!=null) {
//            actionBar.endTitleRefresh();
//        }
        ((XbaseActivity) getActivity()).dismissLoadingDialog();
    }

    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract T createPresenter();

}
