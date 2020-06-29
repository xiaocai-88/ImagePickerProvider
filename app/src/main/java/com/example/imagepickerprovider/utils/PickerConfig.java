package com.example.imagepickerprovider.utils;

import com.example.imagepickerprovider.domain.ImageItem;

import java.util.List;

/**
 * description 用来让外部设置上传的最大值
 * create by xiaocai on 2020/6/29
 */
public class PickerConfig {

    //单例模式保证入口是同一个
    private PickerConfig() {
    }

    private static PickerConfig sPickerConfig;

    public static PickerConfig getInstance() {
        if (sPickerConfig == null) {
            sPickerConfig = new PickerConfig();
        }
        return sPickerConfig;
    }


    private int maxSelectedCount = 1;
    private OnImageSelectedFinishLisenter onImageSelectedFinishLisenter = null;

    public int getMaxSelectedCount() {
        return maxSelectedCount;
    }

    public void setMaxSelectedCount(int maxSelectedCount) {
        this.maxSelectedCount = maxSelectedCount;
    }

    public void setOnImageSelectedFinishLisenter(OnImageSelectedFinishLisenter lisenter) {
        this.onImageSelectedFinishLisenter = lisenter;
    }

    public OnImageSelectedFinishLisenter getOnImageSelectedFinishLisenter(){
        return onImageSelectedFinishLisenter;
    }

    /**
     * 暴露接口让外部去设置
     */
    public interface OnImageSelectedFinishLisenter {
        void onSelectedFinish(List<ImageItem> result);
    }
}
