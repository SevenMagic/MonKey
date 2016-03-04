package com.hit.ddmonkey.app.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.hit.ddmonkey.app.BillRecord;
import com.hit.ddmonkey.app.database.MonkeyDatabaseHelper;
import com.hit.ddmonkey.app.MyRecycleViewAdapter;
import com.hit.ddmonkey.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dusz on 2015/12/30 0030.
 */
public class ByCategoryActivity extends ActionBarActivity {

    MonkeyDatabaseHelper monkeyDatabaseHelperCa;

    Spinner categorySpinnerCa;
    ArrayAdapter<String> categoryAdapterCa = null;

    RecyclerView recyclerviewCa;

    List<BillRecord> itemsCa;

    public static final String[] categoryListCa = {"日常","娱乐","旅游","学习","意外"};
    String categoryCa;
    Button searchCaButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_by_category);

        searchCaButton = (Button)findViewById(R.id.button_ca);
        final Toolbar toolbarCa = (Toolbar) findViewById(R.id.toolbar_ca);
        toolbarCa.setTitle("按类型查询");
        setSupportActionBar(toolbarCa);

        monkeyDatabaseHelperCa = new MonkeyDatabaseHelper(this, "MonData.db", null, 1);//实例化数据库类

        recyclerviewCa = (RecyclerView)findViewById(R.id.recycler_view_ca);
        RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(this);
        recyclerviewCa.setLayoutManager(layoutmanager);
        recyclerviewCa.setHasFixedSize(true);

        categorySpinnerCa = (Spinner)findViewById(R.id.spinner_ca);
        categoryAdapterCa = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,categoryListCa);//
        categoryAdapterCa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//下拉列表风格
        categorySpinnerCa.setAdapter(categoryAdapterCa);
        categorySpinnerCa.setVisibility(View.VISIBLE);

        categorySpinnerCa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
//                result.setText("你的选择是：" + ((TextView) arg1).getText());
                categoryCa = ((TextView) arg1).getText().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        searchCaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemsCa = new ArrayList<>();

                SQLiteDatabase monkeyDatabaseCa = monkeyDatabaseHelperCa.getWritableDatabase();//读入数据库

                Cursor cursor = monkeyDatabaseCa.query("Bill",null,"category = ?",new String[]{categoryCa},null,null,null);
                itemsCa.clear();
                if(cursor.moveToFirst()){
                    do{
                        BillRecord BillRecordItem = new BillRecord();
                        BillRecordItem.setIO(cursor.getString(cursor.getColumnIndex("io")));
                        BillRecordItem.setMoney(Double.parseDouble(cursor.getString(cursor.getColumnIndex("money"))));
                        BillRecordItem.setDate(cursor.getString(cursor.getColumnIndex("date")));
                        BillRecordItem.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                        BillRecordItem.setNote(cursor.getString(cursor.getColumnIndex("note")));
                        itemsCa.add(BillRecordItem);
                    }while (cursor.moveToNext());
                }
                cursor.close();

                MyRecycleViewAdapter myadapter = new MyRecycleViewAdapter(itemsCa,R.layout.left_card);
                recyclerviewCa.setAdapter(myadapter);

            }
        });

    }
}
