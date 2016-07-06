package com.hit.ddmonkey.app.activity;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.hit.ddmonkey.app.BillRecord;
import com.hit.ddmonkey.app.database.MonkeyDatabaseHelper;
import com.hit.ddmonkey.app.MyRecycleViewAdapter;
import com.hit.ddmonkey.app.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dusz
 */
public class ByDateActivity extends BaseActivity {

    MonkeyDatabaseHelper monkeyDatabaseHelperDa;

    RecyclerView recyclerviewDa;

    List<BillRecord> itemsDa;

    String dateDa;
    Button searchDaButton;

    Button chooseDateButton;
    EditText chooseDateText;

    private static  String currentYearMonthDay;//年、月、日
    private  static int year;
    private static int month;
    private static int day;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_by_date);

        final Toolbar toolbarCa = (Toolbar) findViewById(R.id.toolbar_da);
        toolbarCa.setTitle("按日期查询");
        setSupportActionBar(toolbarCa);

        searchDaButton = (Button)findViewById(R.id.button_da);
        chooseDateButton = (Button)findViewById(R.id.bt_datePick_da);
        chooseDateText = (EditText)findViewById(R.id.ed_datePick_da);

        monkeyDatabaseHelperDa = new MonkeyDatabaseHelper(this, "MonData.db", null, 1);//实例化数据库类

        recyclerviewDa = (RecyclerView)findViewById(R.id.recycler_view_da);
        RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(this);
        recyclerviewDa.setLayoutManager(layoutmanager);
        recyclerviewDa.setHasFixedSize(true);

        /*选择日期*/
        currentYearMonthDay=new String();


        final Calendar calendar=Calendar.getInstance();

        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH)+1;
        day=calendar.get(Calendar.DAY_OF_MONTH);

        currentYearMonthDay=year+"-"+month+"-"+day;


        chooseDateText.setText(currentYearMonthDay);

        chooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ByDateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        ByDateActivity.year=year;
                        ByDateActivity.month=monthOfYear+1;
                        ByDateActivity.day=dayOfMonth;

                        ByDateActivity.currentYearMonthDay=ByDateActivity.year+"-"+ByDateActivity.month+"-"+ByDateActivity.day;

                        chooseDateText.setText(currentYearMonthDay);
                    }
                },calendar.get(Calendar.YEAR),calendar.get(calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });



        searchDaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemsDa = new ArrayList<>();

                SQLiteDatabase monkeyDatabaseCa = monkeyDatabaseHelperDa.getWritableDatabase();//读入数据库

                Cursor cursor = monkeyDatabaseCa.query("Bill",null,"date = ?",new String[]{currentYearMonthDay},null,null,null);
                itemsDa.clear();
                if(cursor.moveToFirst()){
                    do{
                        BillRecord BillRecordItem = new BillRecord();
                        BillRecordItem.setIO(cursor.getString(cursor.getColumnIndex("io")));
                        BillRecordItem.setMoney(Double.parseDouble(cursor.getString(cursor.getColumnIndex("money"))));
                        BillRecordItem.setDate(cursor.getString(cursor.getColumnIndex("date")));
                        BillRecordItem.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                        BillRecordItem.setNote(cursor.getString(cursor.getColumnIndex("note")));
                        itemsDa.add(BillRecordItem);
                    }while (cursor.moveToNext());
                }
                cursor.close();

                MyRecycleViewAdapter myadapter = new MyRecycleViewAdapter(itemsDa,R.layout.left_card);
                recyclerviewDa.setAdapter(myadapter);

            }
        });




    }
}
