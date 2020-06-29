package com.example.imagepickerprovider.adapter;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.imagepickerprovider.R;
import com.example.imagepickerprovider.domain.ImageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * description 选择照片界面的adapter
 * create by xiaocai on 2020/6/28
 */
public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.InnerHolder> {

    private List<ImageItem> mImageItems = new ArrayList<>();
    //用来保存选中的item,将选中的item利用get、set方法暴露给外部去获取
    private List<ImageItem> mSelectedItem = new ArrayList<>();
    private OnItemSelectedChangeListener mOnItemSelectedChangeListener = null;
    private static final int MAX_SELECTED_COUNT = 2;
    //暴露方法出去给外部设置最多可选张数
    private int maxSelectedCount = MAX_SELECTED_COUNT;

    public List<ImageItem> getSelectedItem() {
        return mSelectedItem;
    }

    public void setSelectedItem(List<ImageItem> selectedItem) {
        mSelectedItem = selectedItem;
    }

    public int getMaxSelectedCount() {
        return maxSelectedCount;
    }

    public void setMaxSelectedCount(int maxSelectedCount) {
        this.maxSelectedCount = maxSelectedCount;
    }

    /**
     * 加载itemview
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        //将图片与屏幕适配
        Point point = new Point();
        ((WindowManager) parent.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(point);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(point.x / 3, point.x / 3);
        view.setLayoutParams(layoutParams);
        return new InnerHolder(view);
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull final InnerHolder holder, int position) {
        ImageView imageView = holder.itemView.findViewById(R.id.image_iv);
        final CheckBox checkBox = holder.itemView.findViewById(R.id.image_check_box);
        final View cover = holder.itemView.findViewById(R.id.image_cover);
        final ImageItem imageItem = mImageItems.get(position);
        Glide.with(imageView.getContext()).load(imageItem.getPath()).into(imageView);
        //根据数据状态显示内容
        if (mSelectedItem.contains(imageItem)) {
            //如果此时已经选中，设为选中的图标，设置阴影
            mSelectedItem.add(imageItem);
            checkBox.setChecked(false);
            checkBox.setButtonDrawable(holder.itemView.getContext().getDrawable(R.mipmap.checkitem));
            cover.setVisibility(View.VISIBLE);
        } else {
            //此时没有选中，就设为未选中的图标，取消阴影
            mSelectedItem.remove(imageItem);
            checkBox.setChecked(true);
            checkBox.setButtonDrawable(holder.itemView.getContext().getDrawable(R.mipmap.itemtwo));
            cover.setVisibility(View.GONE);
        }

        //设置点击事件,点击时改变显示状态
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果选中就变为取消
                //如果没选中就变为选中

                //如果点击的时候包含该imageview，就说明当前选中
                if (mSelectedItem.contains(imageItem)) {
                    //当前已经选择了，点击后就设为未选择的状态
                    mSelectedItem.remove(imageItem);
                    //修改UI,checkbox设为选中，
                    checkBox.setChecked(true);
                    checkBox.setButtonDrawable(holder.itemView.getContext().getDrawable(R.mipmap.itemtwo));
                    cover.setVisibility(View.GONE);
                } else {
                    //对最大的选择值进行一个判断
                    if (mSelectedItem.size() > maxSelectedCount - 1) {
                        Toast.makeText(checkBox.getContext(), "最多可选" + maxSelectedCount + "张图片", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //如果当前是未选择的状态，点击后就变为选中
                    mSelectedItem.add(imageItem);
                    checkBox.setChecked(false);
                    checkBox.setButtonDrawable(holder.itemView.getContext().getDrawable(R.mipmap.checkitem));
                    cover.setVisibility(View.VISIBLE);
                }
                if (mOnItemSelectedChangeListener != null) {
                    mOnItemSelectedChangeListener.onItemSelectedChange(mSelectedItem);
                }
            }
        });
    }

    /**
     * 将adapter中选中的数据设置进去
     *
     * @param listener
     */
    public void setOnItemSelectedChangeListener(OnItemSelectedChangeListener listener) {
        this.mOnItemSelectedChangeListener = listener;
    }

    /**
     * 将adapter中的数据重置，销毁当前已选的image
     */
    public void reSetDate() {
        mSelectedItem.clear();
    }

    /**
     * 暴露接口通知外面哪些是被选中的
     */
    public interface OnItemSelectedChangeListener {
        void onItemSelectedChange(List<ImageItem> SelectedItem);
    }

    @Override
    public int getItemCount() {
        return mImageItems.size();
    }

    /**
     * 设置数据
     * 1、先清空数据
     * 2、将数据添加
     * 3、更新UI
     *
     * @param itemlist
     */
    public void setDate(List<ImageItem> itemlist) {
        mImageItems.clear();
        mImageItems.addAll(itemlist);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
