package com.zykj.yundonglian.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zykj.yundonglian.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 类名 : SplashAcitivity
 * 时间 : 2018/5/3
 * 描述 : 欢迎向导界面
 * 修改人 :
 * 修改时间 :
 * 修改备注 :
 *
 * @author : Maybe
 */
@ContentView(R.layout.splash_acitivity)
public class SplashAcitivity extends AppCompatActivity {

    @ViewInject(R.id.splashBanner)
    BGABanner mBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.splash_acitivity);
        x.view().inject(this);


    }

}
