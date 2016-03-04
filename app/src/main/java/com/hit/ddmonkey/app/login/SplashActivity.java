package com.hit.ddmonkey.app.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hit.ddmonkey.app.R;
import com.hit.ddmonkey.app.DDUser;
import com.hit.ddmonkey.app.activity.MainActivity;

import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by 道谊戎 on 2016/3/2.
 */
public class SplashActivity extends BaseActivity {
    private RelativeLayout rootLayout;
    private TextView versionText;

    private static final int sleepTime=2000;
    //BmobKey
    private static final String BMOB_APP_KEY="42af7c593262427462293346704b8e88";

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context=this;
        //初始化ButterKnife和Bmob

        ButterKnife.bind(this);
        Bmob.initialize(this, BMOB_APP_KEY);


        rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
        versionText = (TextView) findViewById(R.id.tv_version);

        versionText.setText(getVersion());
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1500);
        rootLayout.startAnimation(animation);

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DDUser currentUser= BmobUser.getCurrentUser(context, DDUser.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(currentUser!=null){
                    //进入主Activity
                    long start=System.currentTimeMillis();
                    //后台加载用户的信息
                    long costTime=System.currentTimeMillis();
                    if(sleepTime-costTime>0){
                        try{
                            Thread.sleep(sleepTime-costTime);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }
                else{
                    //登陆界面
                    try{
                        Thread.sleep(sleepTime);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }
            }
        }).start();
    }


    /**
     * 获取当前应用程序的版本号
     */
    private String getVersion() {
        String st = getResources().getString(R.string.Version_number_is_wrong);
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(getPackageName(), 0);
            String version = packinfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return st;
        }
    }
}
