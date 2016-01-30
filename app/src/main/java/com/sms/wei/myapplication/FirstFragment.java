package com.sms.wei.myapplication;

import android.app.*;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

/**
 * Created by wei on 2016/1/30.
 */
public class FirstFragment extends  BaseFragment {
    //唯一 Tag
    public  static final  String TAG = FirstFragment.class.getSimpleName();
    private FirstFragmetnListerner listerner;
    TextView tv;
    @Override
    protected void afterCreateView() {
         tv= (TextView) mRootView.findViewById(R.id.tv);
        Bundle bundle = getArguments();

        tv.setText(bundle.getString("arg1")+bundle.getString("arg2"));
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                com.sms.wei.myapplication.Dialog dialog = new com.sms.wei.myapplication.Dialog();
                dialog.setTargetFragment(FirstFragment.this,1);
                dialog.show(getFragmentManager(),"dialog");

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return  R.layout.activity_main;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("data", "我是保存的数据");


    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        //Object  servierData= savedInstanceState.get("data");
          //此方法处理事件
          initEvent();



    }

    private void initEvent() {
        //判断是否为null
        if (listerner!=null){
            listerner.onEvent();
        }
        //判断绑定的Activity是否实现了此接口
        if (getActivity() instanceof FirstFragmetnListerner){
            ((FirstFragmetnListerner) getActivity()).onEvent();
        }
    }

    //使用静态工厂来获取实例
   @Nullable
    public  static  FirstFragment newInstance(String arg1,String arg2){
       Bundle args = new Bundle();
       args.putString("arg1",arg1);
       args.putString("arg2",arg2);
       FirstFragment fragment = new FirstFragment();
       fragment.setArguments(args);
       return fragment;
   }
 //设置接口Fragment和Activity之间通信
    interface  FirstFragmetnListerner{
     void  onEvent();
 }
    //通过方法设置接口
    public void setFirstFragementListerer(FirstFragmetnListerner listener){
            this.listerner = listener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
     if (requestCode==1){
         tv.setText(data.getStringExtra("123"));
     }
    }
}
