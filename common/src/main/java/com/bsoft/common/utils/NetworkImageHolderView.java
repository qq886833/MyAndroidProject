package com.bsoft.common.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bsoft.AppApplication;
import com.bsoft.common.R;
import com.bsoft.common.bean.banner.BannerVo;
import com.bsoft.constant.Constants;
import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * 网络图片加载例子
 */
public class NetworkImageHolderView implements Holder<BannerVo> {
    private SimpleDraweeView draweeView;
    
    public NetworkImageHolderView(){
        
    }


    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        
        draweeView = (SimpleDraweeView) LayoutInflater.from(context).inflate(R.layout.item_banner, null);
        return draweeView;
    }

    @Override
    public void UpdateUI(Context context, final int position, BannerVo data) {


        if (data.resId!=-1){
            draweeView.setImageResource(data.resId);
            draweeView.setScaleType(ImageView.ScaleType.FIT_XY);
        }else{
            ImageUtil.showNetWorkImage(draweeView, ImageSizeUtil.getBannerUrl(
                    ImageUrlUtil.getUrl(Constants.HttpImgUrl, data.picture+"")
                    , AppApplication.getWidthPixels()), R.drawable.pic_default); }


    }
}
