package com.hit.ddmonkey.app.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hit.ddmonkey.app.R;
import com.hit.ddmonkey.app.DDUser;
import com.hit.ddmonkey.app.activity.BaseActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 道谊戎 on 2016/3/2.
 */
public class RegisterActivity extends BaseActivity {


    Context context=RegisterActivity.this;
    private static final String SMS_MODEL_NAME="DDMonkey";
    private static final int READ_PHONE_STATE_OK=1;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_toolbar)
    TextView tv_toolbar;


    @Bind(R.id.ed_register_phonenumber)
    EditText ed_phoneNum;
    @Bind(R.id.ed_register_username) EditText ed_username;
    @Bind(R.id.ed_register_password1) EditText ed_password1;
    @Bind(R.id.ed_register_password2) EditText ed_password2;
    @Bind(R.id.ed_register_smscode) EditText ed_smscode;

    @Bind(R.id.bt_register_getsmscode)
    Button bt_getsmsCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        tv_toolbar.setText("注册");
        tv_toolbar.setTextColor(Color.WHITE);
        toolbar.setTitle(" ");
        toolbar.inflateMenu(R.menu.base_toolbar_menu);



    }


    @OnClick(R.id.bt_register_getsmscode)
    public void getSmsCode(){

        if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,
                    Manifest.permission.READ_PHONE_STATE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.


            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        READ_PHONE_STATE_OK);


                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @OnClick(R.id.bt_register_ok)
    public void register(){
        String phoneNumber=ed_phoneNum.getText().toString();
        String userName=ed_username.getText().toString();
        String password1=ed_password1.getText().toString();
        String password2=ed_password2.getText().toString();
        String smsCode=ed_smscode.getText().toString();

        if(phoneNumber==null||phoneNumber.length()!=11){
            toast("手机号不正确");
            return;
        }

        if(userName==null){
            toast("请输入用户名");
            return;
        }
        if(password1==null){
            toast("请输入密码");
            return;
        }
        if(!password1.equals(password2)){
            toast("两次输入密码不一样");
            return;
        }

        //登验证验证码
        final DDUser newUser=new DDUser();
        newUser.setUsername(userName);
        newUser.setPassword(password1);
        newUser.setMobilePhoneNumber(phoneNumber);
        newUser.signOrLogin(context, smsCode, new SaveListener() {
            @Override
            public void onSuccess() {
                toast("注册成功");
                Log.i("test", "" + newUser.getUsername() + "-" + newUser.getAge() + "-" + newUser.getObjectId());
                startActivity(new Intent(RegisterActivity.this, LoginByAccount.class));
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                toast("错误码：" + i + ",错误原因：" + s);

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==READ_PHONE_STATE_OK){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                //设置系统获得了发送短信的许可
                sendMessage();

            } else {
                // Permission Denied
                toast("wrong!!!");
            }


        }
    }
    public void sendMessage(){
        String phoneNumber=ed_phoneNum.getText().toString();
        if(phoneNumber==null||phoneNumber.length()!=11){
            toast("手机号不正确");
            return;
        }
        toast("ALLOW");
        BmobQuery<DDUser> query=new BmobQuery<DDUser>();
        query.addWhereEqualTo("mobilePhoneNumber", phoneNumber);

        query.findObjects(context, new FindListener<DDUser>() {
            @Override
            public void onSuccess(List<DDUser> list) {
                if(!list.isEmpty()){
                    toast("该电话号码已经注册");
                    bt_getsmsCode.setClickable(true);
                    return;
                }
            }
            @Override
            public void onError(int i, String s) {
                toast("查询手机号"+s);
            }
        });

        BmobSMS.requestSMSCode(context, phoneNumber, SMS_MODEL_NAME, new RequestSMSCodeListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    toast("验证码是" + integer);
                    MyCounter counter = new MyCounter(60000, 1000, bt_getsmsCode);
                    counter.start();
                }else{
                    toast("wrong"+e);
                }
            }
        });

    }

    class MyCounter extends CountDownTimer {
        Button mButton;

        public MyCounter(long millisInFuture, long countDownInterval,Button bt) {
            super(millisInFuture, countDownInterval);
            this.mButton=bt;

        }
        @Override
        public void onTick(long l) {
            mButton.setClickable(false);
            mButton.setBackgroundColor(getResources().getColor(R.color.light_gray));
            mButton.setText("再次发送"+"（"+l/1000+")");
        }

        @Override
        public void onFinish() {
            mButton.setText("获取验证码");
            mButton.setBackgroundColor(getResources().getColor(R.color.deep_skyblue));
            mButton.setClickable(true);
        }
    }
}
