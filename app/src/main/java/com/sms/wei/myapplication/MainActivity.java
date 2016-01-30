package com.sms.wei.myapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends Activity {

    private FrameLayout mContentFrameLayout;
    private  FirstFragment firstFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        mContentFrameLayout = (FrameLayout) findViewById(R.id.fl_content);
        //判断是否保存数据
        if (savedInstanceState!=null){
            firstFragment= (FirstFragment) getFragmentManager().findFragmentByTag(FirstFragment.TAG);
        }
        if (firstFragment==null){
            firstFragment=FirstFragment.newInstance("我是第一个","Fragment");
            getFragmentManager().beginTransaction().add(R.id.fl_content,firstFragment,FirstFragment.TAG).commit();
        }


    }
}
