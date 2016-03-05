package com.hit.ddmonkey.app.activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import com.hit.ddmonkey.app.database.MonkeyDatabaseHelper;
import com.hit.ddmonkey.app.R;
import java.util.Calendar;

/**
 * Created by dusz on 2015/12/29 0029.
 */
public class AddBillActivity extends BaseActivity {

    RadioGroup IOGroup;
    Spinner categorySpinner;
    ArrayAdapter<String> categoryAdapter = null;
    EditText billMoneyET;
    EditText billNoteET;
    FloatingActionButton addBillBU;



    //获取日期的量

    private EditText ed_currentDate;
    private Button bt_datePick;

    private static  String currentYearMonthDay;//年、月、日
    private static String currentYearMonthDayHourMinuteSecond;//年月日时分秒
    private  static int year;
    private static int month;
    private static int day;
    private static int hour;
    private static int minute;
    private static int second;

    String ioFlag = "-";
    public static final String[] categoryList = {"日常","娱乐","旅游","学习","意外"};
    String category;

    MonkeyDatabaseHelper monkeyDatabaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_billadd);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle("账单记录");
        setSupportActionBar(toolbar);

        billMoneyET = (EditText)findViewById(R.id.addRecordMoneyText);
        billMoneyET.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        billNoteET = (EditText)findViewById(R.id.addRecordNoteText);

        addBillBU = (FloatingActionButton)findViewById(R.id.confirmfab);

        IOGroup = (RadioGroup)findViewById(R.id.io_group);
        categorySpinner = (Spinner)findViewById(R.id.categorySpinner);
        categoryAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,categoryList);//
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//下拉列表风格
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setVisibility(View.VISIBLE);

        IOGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.outRadioButton) {
                    ioFlag = "-";

                } else if (checkedId == R.id.inRadioButton) {
                    ioFlag = "+";

                }
            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
//                result.setText("你的选择是：" + ((TextView) arg1).getText());
                category = ((TextView) arg1).getText().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        /*日期的所有东东*/

        currentYearMonthDay=new String();
        currentYearMonthDayHourMinuteSecond=new String();



        ed_currentDate=(EditText)findViewById(R.id.ed_datePick);
        bt_datePick=(Button)findViewById(R.id.bt_datePick);



        final Calendar calendar=Calendar.getInstance();

        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH)+1;
        day=calendar.get(Calendar.DAY_OF_MONTH);
        hour=calendar.get(Calendar.HOUR);
        minute=calendar.get(Calendar.MINUTE);
        second=calendar.get(Calendar.SECOND);

        currentYearMonthDay=year+"-"+month+"-"+day;
        currentYearMonthDayHourMinuteSecond=year+"-"+month+"-"+day+"-"+hour+"-"+minute+"-"+second;

        ed_currentDate.setText(currentYearMonthDay);

        bt_datePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddBillActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        AddBillActivity.year=year;
                        AddBillActivity.month=monthOfYear+1;
                        AddBillActivity.day=dayOfMonth;
                        //AddBillActivity.currentYearMonthDayHourMinuteSecond=AddBillActivity.year+"年"+AddBillActivity.month+"月"+AddBillActivity.day+"日"+hour+"时"+minute+"分"+second+"秒";

                        AddBillActivity.currentYearMonthDay=AddBillActivity.year+"-"+AddBillActivity.month+"-"+AddBillActivity.day;

                        ed_currentDate.setText(currentYearMonthDay);
                    }
                },calendar.get(Calendar.YEAR),calendar.get(calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


//        SimpleDateFormat    formatter;
//        formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
//        Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
//        String    str    =    formatter.format(curDate);
//
//

//    Toast.makeText(MyActivity.this,"系统时间"+str,Toast.LENGTH_SHORT).show();


        monkeyDatabaseHelper = new MonkeyDatabaseHelper(this, "MonData.db", null, 1);//实例化数据库类
        addBillBU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((!TextUtils.isEmpty(billMoneyET.getText().toString()))&&(!TextUtils.isEmpty(billNoteET.getText().toString()))){
                    SQLiteDatabase monkeyDatabase = monkeyDatabaseHelper.getWritableDatabase();//读入数据库
                    ContentValues values = new ContentValues();
                    values.put("io",ioFlag);
                    values.put("money",Double.parseDouble(billMoneyET.getText().toString()));
                    values.put("date",currentYearMonthDay);
                    values.put("category",category);
                    values.put("dateid",currentYearMonthDayHourMinuteSecond);
                    values.put("note", billNoteET.getText().toString());
                    monkeyDatabase.insert("Bill", null, values);
                    values.clear();
                    finish();
                }
                else {
                    Snackbar.make(view, "内容不全,but for test", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                    //方便加入测试
//                    SQLiteDatabase monkeyDatabase = monkeyDatabaseHelper.getWritableDatabase();//读入数据库
//                    ContentValues values = new ContentValues();
//                    values.put("io","+");
//                    values.put("money",63.6);
//                    values.put("date","2016-3-5");
//                    values.put("category","快速测试");
//                    values.put("dateid",currentYearMonthDayHourMinuteSecond);
//                    values.put("note", "无");
//                    monkeyDatabase.insert("Bill", null, values);
//                    values.clear();
//                    finish();
                }



            }
        });




    }
}
