package com.sms.wei.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wei on 2016/1/30.
 */
public abstract class BaseFragment extends Fragment {
    protected  View mRootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null==mRootView){
            mRootView = inflater.inflate(getLayoutId(),container,false);
        }
        afterCreateView();
        return mRootView;
    }

    protected abstract void afterCreateView();

    protected abstract int getLayoutId();


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        afterCreate(savedInstanceState);
    }

    protected abstract void afterCreate(Bundle savedInstanceState);


}
