package me.cpf.fbp;

import android.app.Application;

/**
 * Created by lenovo on 2017/7/5.
 */
public class MyApplication extends Application {

    public static MyApplication mMcsApplication;

    public static Application get() {
        return mMcsApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mMcsApplication = this;
    }
}
