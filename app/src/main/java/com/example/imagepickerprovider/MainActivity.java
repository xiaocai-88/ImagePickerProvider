package com.example.imagepickerprovider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagepickerprovider.adapter.ResultImageAdapter;
import com.example.imagepickerprovider.domain.ImageItem;
import com.example.imagepickerprovider.utils.PickerConfig;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity implements PickerConfig.OnImageSelectedFinishLisenter {

    private static final int PREMISSION_REQUEST_CODE = 1;
    private static final String TAG = "MainActivity";
    private static final int MAX_SELECTED_COUNT = 4;
    private RecyclerView mResultList;
    private ResultImageAdapter mResultImageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        checkPremission();
        initPickerConfig();
    }

    private void initView() {
        mResultList = findViewById(R.id.result_list);
        mResultImageAdapter = new ResultImageAdapter();
        mResultList.setAdapter(mResultImageAdapter);
    }

    /**
     * 设置可选的最大值
     */
    private void initPickerConfig() {
        PickerConfig pickerConfig = PickerConfig.getInstance();
        pickerConfig.setMaxSelectedCount(MAX_SELECTED_COUNT);
        pickerConfig.setOnImageSelectedFinishLisenter(this);
    }


    private void checkPremission() {
        int readExStroagePresmission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.d(TAG, "readExStroagePresmission" + readExStroagePresmission);
        if (readExStroagePresmission == PackageManager.PERMISSION_GRANTED) {
            //有权限
        } else {
            //没有权限，需要去申请权限
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PREMISSION_REQUEST_CODE);
        }
    }

    /**
     * 申请权限后检查权限结果
     *
     * @return
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PREMISSION_REQUEST_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //如果请求的权限是我们这里的requestcode，则说明请求成功
            } else {
                //请求失败
                //根据交互去处理
            }
        }
    }

    public void pickerImage(View view) {
        //打开另外一个界面
        //显式意图跳转
        startActivity(new Intent(this, PickerActivity.class));
    }

    @Override
    public void onSelectedFinish(List<ImageItem> result) {
        //所选择的图片列表在该处回来了
        //将数据设置到adapter中去显示出来
        for (ImageItem imageItem : result) {
            Log.d(TAG, "image item is " + imageItem);
        }

        //设置布局管理器,用来根据选择图片的数量来进行摆放
        //如果数量>3了，就需要换行了
        int mHorizonCount;
        if (result.size() < 3) {
            mHorizonCount = result.size();
        } else {
            mHorizonCount = 3;
        }
        //注意！！！此处一定要用
        //    pickerConfig.setOnImageSelectedFinishLisenter(this);写法
        //不然设置布局管理器的时候，context传递 的就是listener类型变量了
        mResultList.setLayoutManager(new GridLayoutManager(this, mHorizonCount));
        mResultImageAdapter.setDate(result,mHorizonCount);
    }
}
