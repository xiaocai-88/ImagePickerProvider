package com.example.imagepickerprovider;

import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagepickerprovider.adapter.ImageListAdapter;
import com.example.imagepickerprovider.domain.ImageItem;
import com.example.imagepickerprovider.utils.PickerConfig;

import java.util.ArrayList;
import java.util.List;
//图片数据库中的相关字段
//        D/PickerActivity: _id is ======1097
//        D/PickerActivity: _data is ======/storage/emulated/0/Pictures/Screenshots/pictwo.png
//        D/PickerActivity: _size is ======22302
//        D/PickerActivity: _display_name is ======pictwo.png
//        D/PickerActivity: mime_type is ======image/png
//        D/PickerActivity: title is ======pictwo
//        D/PickerActivity: date_added is ======1593314451
//        D/PickerActivity: date_modified is ======1593314298
//        D/PickerActivity: description is ======null
//        D/PickerActivity: picasa_id is ======null
//        D/PickerActivity: isprivate is ======null
//        D/PickerActivity: latitude is ======null
//        D/PickerActivity: longitude is ======null
//        D/PickerActivity: datetaken is ======1593314298000
//        D/PickerActivity: orientation is ======null
//        D/PickerActivity: mini_thumb_magic is ======null
//        D/PickerActivity: bucket_id is ======1028075469
//        D/PickerActivity: bucket_display_name is ======Screenshots
//        D/PickerActivity: width is ======366
//        D/PickerActivity: height is ======287

public class PickerActivity extends AppCompatActivity {

    private static final String TAG = "PickerActivity";
    private static final int LOADER_ID = 1;
    private List<ImageItem> mItemlist = new ArrayList<>();
    private ImageListAdapter mImageListAdapter;
    private TextView mFinishTv;
    private PickerConfig mPickerConfig;
    private ImageView mBackBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

//        ContentResolver contentResolver = getContentResolver();
//        Uri imageUri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        Cursor cursor = contentResolver.query(imageUri, null, null, null, null);
//        //将每个字段打印出来
//        String[] columnNames = cursor.getColumnNames();
//        while (cursor.moveToNext()) {
//            Log.d(TAG,"=========================");
//            for (String columnName : columnNames) {
//                Log.d(TAG,columnName+" is ======"+cursor.getString(cursor.getColumnIndex(columnName)));
//            }
//        }
//        cursor.close();

        initLoadManager();
        initView();
        initEvent();
        initConfig();
    }

    /**
     * 在mainactivity中设置max值，在此处对adapter进行max的赋值
     */
    private void initConfig() {
        mPickerConfig = PickerConfig.getInstance();
        int maxSelectedCount = mPickerConfig.getMaxSelectedCount();
        mImageListAdapter.setMaxSelectedCount(maxSelectedCount);
    }

    private void initEvent() {
        //选择图片时的监听事件
        mImageListAdapter.setOnItemSelectedChangeListener(new ImageListAdapter.OnItemSelectedChangeListener() {
            @Override
            public void onItemSelectedChange(List<ImageItem> SelectedItem) {
                //所选择的数据发生了变化
                mFinishTv.setText("(" + SelectedItem.size() + "/" + mImageListAdapter.getMaxSelectedCount() + ")已选择");
            }
        });

        //选择确定上传的监听事件
        mFinishTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取到所选择的数据，通知mainactivity，结束当前界面
                List<ImageItem> selectedResult = new ArrayList<>();
                selectedResult.addAll(mImageListAdapter.getSelectedItem());
                mImageListAdapter.reSetDate();
                //通过pickerconfig的单例模式将数据设置进去，用于在mainactivity中显示
                PickerConfig.OnImageSelectedFinishLisenter onImageSelectedFinishLisenter=mPickerConfig.getOnImageSelectedFinishLisenter();
                if (onImageSelectedFinishLisenter != null) {
                    onImageSelectedFinishLisenter.onSelectedFinish(selectedResult);
                }
                //结束界面
                finish();
            }
        });

        //返回按钮被点击
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        mBackBtn = findViewById(R.id.back_btn);
        mFinishTv = findViewById(R.id.finishTv);
        RecyclerView listView = findViewById(R.id.image_list_view);
        listView.setLayoutManager(new GridLayoutManager(this, 3));
        //设置adapter
        mImageListAdapter = new ImageListAdapter();
        listView.setAdapter(mImageListAdapter);
        //设置间距
        listView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 6;
                outRect.bottom = 6;
            }
        });
    }

    /**
     * 使用initLoadManager加载图片
     */
    private void initLoadManager() {
        //先清空,再将数据放进去
        mItemlist.clear();
        //getInstance()可以在activity中或fragment中使用
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        //初始化loaderManager
        loaderManager.initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                //此处的id就是LOADER_ID
                if (id == LOADER_ID) {
                    return new CursorLoader(PickerActivity.this,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            new String[]{"_data", "_display_name", "date_added"},
                            null, null, "date_added DESC");
                }
                return null;
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
                if (cursor != null) {
                    //将每个字段打印出来
                    String[] columnNames = cursor.getColumnNames();
                    while (cursor.moveToNext()) {
                        Log.d(TAG, "=========================");
                        for (String columnName : columnNames) {
                            Log.d(TAG, columnName + " is ======" + cursor.getString(cursor.getColumnIndex(columnName)));
                        }
                        String path = cursor.getString(0);
                        String title = cursor.getString(1);
                        long date = cursor.getLong(2);

                        ImageItem imageItem = new ImageItem(path, title, date);
                        mItemlist.add(imageItem);
                    }
                    cursor.close();
//                    for (ImageItem imageItem : mItemlist) {
//                        Log.d(TAG,"image ---->"+imageItem.toString());
//                    }
                    mImageListAdapter.setDate(mItemlist);
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {

            }
        });
    }
}
