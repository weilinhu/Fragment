package com.sms.wei.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * Created by wei on 2016/1/30.
 */
public class FirstFragment extends  BaseFragment {
    //唯一 Tag
    public  static final  String TAG = FirstFragment.class.getSimpleName();
    private FirstFragmetnListerner listerner;

    @Override
    protected void afterCreateView() {
        TextView tv= (TextView) mRootView.findViewById(R.id.tv);
        Bundle bundle = getArguments();

        tv.setText(bundle.getString("arg1")+bundle.getString("arg2"));
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
        //or
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
}
