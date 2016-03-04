package com.hit.ddmonkey.app.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.hit.ddmonkey.app.database.MonkeyDatabaseHelper;
import com.hit.ddmonkey.app.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dusz on 2015/12/29 0029.
 */
public class GaolSetActivity extends ActionBarActivity {


    MonkeyDatabaseHelper monkeyDatabaseHelper;

    EditText goalNameET;
    EditText goalMoneyET;
    EditText goalTimeET;
    FloatingActionButton addGoalBU;

    String goalDate;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_goalset);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle("新的征程！");
        setSupportActionBar(toolbar);

        goalNameET = (EditText)findViewById(R.id.addGoalNameText);
        goalMoneyET = (EditText)findViewById(R.id.addGoalMoneyText);
        goalMoneyET.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        goalTimeET = (EditText)findViewById(R.id.addGoalTimeText);
        goalTimeET.setInputType(InputType.TYPE_CLASS_NUMBER);
        addGoalBU = (FloatingActionButton)findViewById(R.id.goalconfirmfab);

        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate  = new Date(System.currentTimeMillis());//获取当前时间
        goalDate = formatter.format(curDate);

        monkeyDatabaseHelper = new MonkeyDatabaseHelper(this, "MonData.db", null, 1);//实例化数据库类

        addGoalBU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!TextUtils.isEmpty(goalMoneyET.getText().toString()))&&(!TextUtils.isEmpty(goalNameET.getText().toString()))&&(!TextUtils.isEmpty(goalTimeET.getText().toString()))){
                    SQLiteDatabase monkeyDatabase = monkeyDatabaseHelper.getWritableDatabase();//读入数据库
                    ContentValues values = new ContentValues();
                    values.put("goalname", goalNameET.getText().toString());
                    values.put("time", goalTimeET.getText().toString());
                    values.put("money", Double.parseDouble(goalMoneyET.getText().toString()));

                    monkeyDatabase.insert("Goal", null, values);
                    values.clear();

                    finish();
                }
                else {
                    Snackbar.make(view, "内容不全", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });




    }
}
