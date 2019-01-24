package com.bsoft.mvpbase;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bsoft.base.XbaseActivity;
import com.bsoft.base.XbaseRecyclerViewFragment;


public abstract class BaseMvpRecyclerViewFragment<V extends IView, T extends BasePresenter<V>> extends XbaseRecyclerViewFragment implements IView{

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
        if( ((XbaseActivity) getActivity()).actionBar!=null) {
            ((XbaseActivity) getActivity()).actionBar.startTitleRefresh();
        }
       // ((BaseActivity) getActivity()).showLoadingView();
        showLoadingView();
    }

    @Override
    public void dismissLoading() {
        if( ((XbaseActivity) getActivity()).actionBar!=null) {
            ((XbaseActivity) getActivity()).actionBar.endTitleRefresh();
        }
      //  ((BaseActivity) getActivity()).dismissLoadingDialog();
        refreshComplete();
    }


    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract T createPresenter();

}
