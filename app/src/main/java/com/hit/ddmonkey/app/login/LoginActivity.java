package com.hit.ddmonkey.app.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.hit.ddmonkey.app.Constants;
import com.hit.ddmonkey.app.R;
import com.hit.ddmonkey.app.activity.BaseActivity;
import com.hit.ddmonkey.app.activity.MainActivity;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.OtherLoginListener;

/**
 * Created by 道谊戎
 */
public class LoginActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.base_toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @OnClick(R.id.button_login)
    public void button_login(){
//        Toast toast=Toast.makeText(getApplicationContext(), "Toast", Toast.LENGTH_SHORT);
//        toast.show();
        startActivity(new Intent(LoginActivity.this,LoginByAccount.class));

    }
    @OnClick(R.id.button_register)
    public void button_register(){
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

    }

    private static Tencent mTencent;

    @OnClick(R.id.bt_login_qq)
    public void bt_login_byqq(){
        qqAuthorize();
    }
    @OnClick(R.id.bt_login_weixin)
    public void bt_login_byweixin(){

    }
    @OnClick(R.id.bt_login_weibo)
    public void bt_login_byweibo(){

    }


    /**
     * @method loginWithAuth
     * @param
     * @param authInfo
     * @return void
     * @exception
     */
    public void loginWithAuth(final BmobUser.BmobThirdUserAuth authInfo){
        BmobUser.loginWithAuthData(LoginActivity.this, authInfo, new OtherLoginListener() {

            @Override
            public void onSuccess(JSONObject userAuth) {
                // TODO Auto-generated method stub
                Log.i("smile", authInfo.getSnsType() + "登陆成功返回:" + userAuth);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("json", userAuth.toString());
                intent.putExtra("from", authInfo.getSnsType());
                startActivity(intent);
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                toast("第三方登陆失败："+msg);
            }

        });
    }
    private void qqAuthorize(){
        if(mTencent==null){
            mTencent = Tencent.createInstance(Constants.QQ_APP_ID, getApplicationContext());
        }
        mTencent.logout(this);
        mTencent.login(this, "all", new IUiListener() {

            @Override
            public void onComplete(Object arg0) {
                // TODO Auto-generated method stub
                if(arg0!=null){
                    JSONObject jsonObject = (JSONObject) arg0;
                    try {
                        String token = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
                        String expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
                        String openId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
                        BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ,token, expires,openId);
                        loginWithAuth(authInfo);
                    } catch (JSONException e) {
                    }
                }
            }

            @Override
            public void onError(UiError arg0) {
                // TODO Auto-generated method stub
                toast("QQ授权出错："+arg0.errorCode+"--"+arg0.errorDetail);
            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                toast("取消qq授权");
            }
        });
    }



}
