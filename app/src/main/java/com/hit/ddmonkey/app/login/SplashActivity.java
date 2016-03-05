package com.hit.ddmonkey.app.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hit.ddmonkey.app.Constants;
import com.hit.ddmonkey.app.R;
import com.hit.ddmonkey.app.DDUser;
import com.hit.ddmonkey.app.activity.BaseActivity;
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


    private static final int READ_PHONE_STATE_ALLOWED=1;
    private static final int WRITE_EXTERNAL_STORAGE_ALLOWED=2;
    private static final int sleepTime=2000;
    //BmobKey

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context=this;
        //初始化ButterKnife和Bmob

        ButterKnife.bind(this);
        Bmob.initialize(this, Constants.BMOB_APPID);



        rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
        versionText = (TextView) findViewById(R.id.tv_version);


        versionText.setText(getVersion());
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1500);
        rootLayout.startAnimation(animation);
        askPermission();

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case READ_PHONE_STATE_ALLOWED:
                toast("READ_PHONE_STATE_ALLOWED");
            case WRITE_EXTERNAL_STORAGE_ALLOWED:
                toast("WRITE_EXTERNAL_STORAGE_ALLOWED");
                default:
                    toast("not get permission");
        }
    }

    private void askPermission() {

        int phoneState = ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_PHONE_STATE);
        int writeExternalStorage = ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (phoneState != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                //处理用户拒绝之后的事件

            } else {
                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_ALLOWED);

            }
        }
        if (writeExternalStorage != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //处理用户拒绝之后的事件

            } else {
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_ALLOWED);

            }
        }

    }
}
