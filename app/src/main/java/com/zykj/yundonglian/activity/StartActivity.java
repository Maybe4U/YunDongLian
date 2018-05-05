package com.zykj.yundonglian.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.zykj.yundonglian.R;
import com.zykj.yundonglian.utils.LogUtil;
import com.zykj.yundonglian.utils.NetManager;
import com.zykj.yundonglian.utils.SPUtils;

import static com.zykj.yundonglian.Const.Const.Splash.ENTER_HOME;
import static com.zykj.yundonglian.Const.Const.Splash.ENTER_SPLASH;
import static com.zykj.yundonglian.Const.Const.Splash.JSON_ERROR;
import static com.zykj.yundonglian.Const.Const.Splash.NET_ERROR;
import static com.zykj.yundonglian.Const.Const.Splash.SHOW_UPDATE_DAILOG;
import static com.zykj.yundonglian.Const.Const.Splash.URL_ERROR;
/**
 * 类名 : StartActivity
 * 时间 : 2018/5/3
 * 描述 :启动界面
 * 修改人 :
 * 修改时间 :
 * 修改备注 :
 *
 * @author   : Maybe
 */
public class StartActivity extends AppCompatActivity {

    private static final String TAG = "STARTACTIVITY";
    private static final long DELAY_TIME = 2000;
    private NetManager mNetManager;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ENTER_SPLASH://进入主界面
                    enterHome();
                    LogUtil.e(TAG, "进入主界面");
                    break;
                case ENTER_HOME://进入主界面
                    enterHome();
                    LogUtil.e(TAG, "进入主界面");
                    break;
                case SHOW_UPDATE_DAILOG://弹出升级对话框
                    LogUtil.e(TAG, "发现新版本，弹出升级对话框");
                    //showUpdateDialog();
                    //enterHome();
                    break;
                case URL_ERROR://URL异常
                    enterHome();
                    Toast.makeText(getApplicationContext(), "URL异常", Toast.LENGTH_SHORT).show();
                    break;
                case NET_ERROR://网络异常
                    enterHome();
                    Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                    break;
                case JSON_ERROR://JSON解析异常
                    enterHome();
                    Toast.makeText(getApplicationContext(), "JSON解析异常", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = View.inflate(this, R.layout.activity_start, null);
        setContentView(mView);


        mNetManager = new NetManager(this);

        if (mNetManager.isOpenNetwork()) {
            init();
        } else {
            netSet();
        }
    }

    /**
     * method desc.  :初始化 版本升级 启动欢迎页
     * params        :
     * return        :
     */
    private void init() {
        //新版本升级提示标志位 true为提示
        Boolean isCheck = (Boolean) SPUtils.get(this, "updateFlag", false);
        if (isCheck) {
            checkVersion();//检查版本
        } else {
            //带有动画效果的进入应用首页
            enterWithAnimation();

        }
    }

    private void enterWithAnimation() {
        // 设置动画效果是alpha，在anim目录下的alpha.xml文件中定义动画效果
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        // 给view设置动画效果
        mView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            // 这里监听动画结束的动作，在动画结束的时候开启一个线程，这个线程中绑定一个Handler,并
            // 在这个Handler中调用goHome方法，而通过postDelayed方法使这个方法延迟500毫秒执行，达到
            // 达到持续显示第一屏500毫秒的效果
            @Override
            public void onAnimationEnd(Animation arg0) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //延迟两秒进入主界面
                        enterHome();
                    }
                }, DELAY_TIME);
            }
        });
    }

    /**
     * method desc.  :无网络提醒 设置网络
     * params        :
     * return        :
     */
    private void netSet() {
        // 如果网络不可用，则弹出对话框，对网络进行设置
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("没有可用的网络");
        builder.setMessage("是否对网络进行设置?");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = null;
                        try {
                            String sdkVersion = android.os.Build.VERSION.SDK;
                            if (Integer.valueOf(sdkVersion) > 10) {
                                intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                            } else {
                                intent = new Intent();
                                ComponentName comp = new ComponentName(
                                        "com.android.settings",
                                        "com.android.settings.WirelessSettings");
                                intent.setComponent(comp);
                                intent.setAction("android.intent.action.VIEW");
                            }
                            StartActivity.this.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StartActivity.this.finish();
                    }
                });
        builder.show();
    }

    /**
     * method desc.  :检查当前版本
     * params        :
     * return        :
     */
    private void checkVersion() {

    }

    /**
     * method desc.  :进入应用界面
     * params        :
     * return        :
     */
    private void enterHome() {
        //判断是否第一次进入应用
        Boolean is_first = (Boolean) SPUtils.get(this, "is_first", true);
        if (is_first) {
            startActivityForAnim(SplashAcitivity.class);
            //设置第一次进入应用标志位为false
            SPUtils.put(this,"is_first",false);
        }else {
            startActivityForAnim(MainActivity.class);
        }
    }


    private void startActivityForAnim(Class clazz) {
        startActivity(new Intent(StartActivity.this, clazz));
        // 设置Activity的切换效果
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        StartActivity.this.finish();
    }
}
