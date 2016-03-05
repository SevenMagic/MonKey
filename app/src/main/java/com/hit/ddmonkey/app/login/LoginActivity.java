package com.hit.ddmonkey.app.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.hit.ddmonkey.app.R;
import com.hit.ddmonkey.app.activity.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 道谊戎 on 2016/3/2.
 */
public class LoginActivity extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tv_toolbar)
    TextView tv_toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        tv_toolbar.setText("登录");
        tv_toolbar.setTextColor(Color.WHITE);
        toolbar.setTitle(" ");
        toolbar.inflateMenu(R.menu.base_toolbar_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.base_toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @OnClick(R.id.bt_login_byphone)
    public void bt_login_byphone(){
        startActivity(new Intent(LoginActivity.this,LoginByAccount.class));

    }
    @OnClick(R.id.bt_login_register)
    public void bt_login_register(){
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

    }
    @OnClick(R.id.bt_login_byqq)
    public void bt_login_byqq(){

    }
    @OnClick(R.id.bt_login_byweixin)
    public void bt_login_byweixin(){

    }
    @OnClick(R.id.bt_login_byweibo)
    public void bt_login_byweibo(){

    }

}
