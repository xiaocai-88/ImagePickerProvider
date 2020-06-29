package com.example.imagepickerprovider.adapter;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.imagepickerprovider.R;
import com.example.imagepickerprovider.domain.ImageItem;
import com.example.imagepickerprovider.utils.SizeUtils;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * description 主界面显示的图片
 * create by xiaocai on 2020/6/29
 */
public class ResultImageAdapter extends RecyclerView.Adapter<ResultImageAdapter.InnerHolder> {

    private List<ImageItem> mImageItems = new ArrayList<>();
    private int mHorizonCount=1;

    /**
     * 布局和ImageListAdapter使用同一个布局
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        //回显时将CheckBox的图标取消显示
        view.findViewById(R.id.image_check_box).setVisibility(View.GONE);
        return new InnerHolder(view);
    }

    /**
     * 设置数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        //根据所选图片个数来显示图片
        View itemView = holder.itemView;
        Point point = SizeUtils.getScreenSize(itemView.getContext());
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(point.x / mHorizonCount, point.x / mHorizonCount);
        itemView.setLayoutParams(layoutParams);
        String imagePath = mImageItems.get(position).getPath();
        //根据path显示图片
        ImageView imageView = itemView.findViewById(R.id.image_iv);
        Glide.with(imageView.getContext()).load(imagePath).into(imageView);
    }

    @Override
    public int getItemCount() {
        return mImageItems.size();
    }

    /**
     * 设置数据
     * 1、清除数据
     * 2、设置数据
     * 3、更新UI
     *
     * @param result
     * @param mHorizonCount 回显时展示的图片数量
     */
    public void setDate(List<ImageItem> result, int mHorizonCount) {
        this.mHorizonCount = mHorizonCount;
        mImageItems.clear();
        mImageItems.addAll(result);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
