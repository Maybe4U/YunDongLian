package com.zykj.yundonglian;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by  on 2018/05/03.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Xutils3.0初始化
        x.Ext.init(this);
        x.Ext.setDebug(false); //输出debug日志，开启会影响性能
        //监听异常
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
    }

    private class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable ex) {
            File file = new File(Environment.getExternalStorageDirectory(),"crash.log");
            try {
                Log.e("Exception","捕获了一个异常");
                PrintWriter err = new PrintWriter(file);
                ex.printStackTrace(err);
                err.flush();
                err.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
