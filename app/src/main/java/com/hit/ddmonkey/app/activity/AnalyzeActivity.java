

package com.hit.ddmonkey.app.activity;


        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.support.design.widget.CoordinatorLayout;
        import android.support.design.widget.Snackbar;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.View;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import com.echo.holographlibrary.PieGraph;
        import com.echo.holographlibrary.PieSlice;
        import com.hit.ddmonkey.app.BillRecord;
        import com.hit.ddmonkey.app.Goal;
        import com.hit.ddmonkey.app.database.MonkeyDatabaseHelper;
        import com.hit.ddmonkey.app.R;

        import java.util.ArrayList;
        import java.util.List;

/**
 * Created by 道谊戎 on 2015/12/30.
 */
public class AnalyzeActivity extends BaseActivity {

    private PieGraph pieGraph;
    private PieGraph pieGraph2;
    private MonkeyDatabaseHelper monkeyDatabaseHelperCa;
    private List<BillRecord> itemsCa;

    private TextView outTextView;
    private TextView inTextView;
    private TextView zanTextView;
    private TextView viTextView;
    private LinearLayout viLayout1;
    private LinearLayout viLayout2;
    private TextView viTextView2;
    private LinearLayout viLayout3;

    private List<BillRecord> itemsIn;
    private List<Goal> goalA;
    private Goal goalN;
    private PieSlice slice;
    private static double[]  money;
    private static double moneyGoal;
    private static double moneyZan;
    private static double in = 0;
    private static double out = 0;

    CoordinatorLayout mcoo;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyst);

        mcoo = (CoordinatorLayout)findViewById(R.id.main_content_an);

        outTextView = (TextView)findViewById(R.id.outText);
        inTextView = (TextView)findViewById(R.id.inText);
        zanTextView = (TextView)findViewById(R.id.zanText);

        viTextView = (TextView)findViewById(R.id.viText1);
        viLayout1 = (LinearLayout)findViewById(R.id.viLayout1);
        viLayout2 = (LinearLayout)findViewById(R.id.viLayout2);

        viTextView2 = (TextView)findViewById(R.id.viText2);
        viLayout3 = (LinearLayout)findViewById(R.id.viLayout3);

        final Toolbar toolbarAn = (Toolbar) findViewById(R.id.toolbar_an);
        toolbarAn.setTitle("分析、分析~~");
        //setSupportActionBar(toolbarAn);

        monkeyDatabaseHelperCa = new MonkeyDatabaseHelper(this, "MonData.db", null, 1);//实例化数据库类

        itemsCa = new ArrayList<>();

        itemsIn = new ArrayList<>();
        goalA = new ArrayList<>();

        SQLiteDatabase monkeyDatabaseCa = monkeyDatabaseHelperCa.getWritableDatabase();//读入数据库

        Cursor cursor = monkeyDatabaseCa.query("Bill",null,"io = ?",new String[]{"-"},null,null,null);
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



        String category=new String();
        money =new double[5];
        for(int i=0;i<money.length;i++){
            money[i]=0;
        }
        for(int i=0;i<itemsCa.size();i++){

            category=itemsCa.get(i).getCategory();
            switch (category){
                case "日常":money[0]=money[0]+itemsCa.get(i).getMoney();

                    Log.i("日常",""+money[0]);
                    break;
                case "娱乐":money[1]=money[1]+itemsCa.get(i).getMoney();
                    Log.i("娱乐",""+money[1]);
                    break;

                case "旅游":money[2]=money[2]+itemsCa.get(i).getMoney();
                    Log.i("旅游",""+money[2]);
                    break;

                case "学习":money[3]=money[3]+itemsCa.get(i).getMoney();
                    Log.i("学习",""+money[3]);
                    break;

                case "意外":money[4]=money[4]+itemsCa.get(i).getMoney();
                    Log.i("意外",""+money[4]);
                    break;

            }

        }

        out = 0;
        for(int i =0;i<itemsCa.size();i++){
            out += itemsCa.get(i).getMoney();
        }


        if(out > 0){

            //画饼状图
            //按照类别
            pieGraph=(PieGraph)findViewById(R.id.pieGraph);

            //日常 红色
            slice=new PieSlice();
            slice.setColor(Color.parseColor("#FF0000"));
            slice.setValue((int)money[0]);
            pieGraph.addSlice(slice);

            //娱乐 橙色
            slice = new PieSlice();
            slice.setColor(Color.parseColor("#FFBB33"));
            slice.setValue((int)money[1]);
            pieGraph.addSlice(slice);

            //旅游 紫色
            slice = new PieSlice();
            slice.setColor(Color.parseColor("#AA66CC"));
            slice.setValue((int)money[2]);
            pieGraph.addSlice(slice);

            //学习 绿色
            slice = new PieSlice();
            slice.setColor(Color.parseColor("#00FF00"));
            slice.setValue((int)money[3]);
            pieGraph.addSlice(slice);

            //意外 蓝色
            slice = new PieSlice();
            slice.setColor(Color.parseColor("#0000FF"));
            slice.setValue((int)money[4]);
            pieGraph.addSlice(slice);
        }

        else {
            viTextView2.setVisibility(View.GONE);
            viLayout3.setVisibility(View.GONE);
        }








        //按照收入与支出


        Cursor cursor2 = monkeyDatabaseCa.query("Bill",null,"io = ?",new String[]{"+"},null,null,null);
        itemsIn.clear();
        if(cursor2.moveToFirst()){
            do{
                BillRecord BillRecordItem = new BillRecord();
                BillRecordItem.setIO(cursor2.getString(cursor2.getColumnIndex("io")));
                BillRecordItem.setMoney(Double.parseDouble(cursor2.getString(cursor2.getColumnIndex("money"))));
                BillRecordItem.setDate(cursor2.getString(cursor2.getColumnIndex("date")));
                BillRecordItem.setCategory(cursor2.getString(cursor2.getColumnIndex("category")));
                BillRecordItem.setNote(cursor2.getString(cursor2.getColumnIndex("note")));
                itemsIn.add(BillRecordItem);
            }while (cursor2.moveToNext());
        }
        cursor2.close();




        in = 0;
        for(int i =0;i<itemsIn.size();i++) {
            in += itemsIn.get(i).getMoney();
        }

        outTextView.setText("目前所有支出项统计为 "+out+" 元~~");
        inTextView.setText("目前所有收入项统计为 "+in+" 元~~");


        moneyZan = in - out;



        Cursor cursor3 = monkeyDatabaseCa.query("Goal",null,null,null,null,null,null);
        goalA.clear();
        if(cursor3.moveToFirst()){
            do{
                Goal goalItem = new Goal();
                goalItem.setName(cursor3.getString(cursor3.getColumnIndex("goalname")));
                goalItem.setMoney(Double.parseDouble(cursor3.getString(cursor3.getColumnIndex("money"))));
                goalItem.setTime(Integer.parseInt(cursor3.getString(cursor3.getColumnIndex("time"))));
                goalA.add(goalItem);
            }while (cursor3.moveToNext());
        }
        cursor3.close();

        goalN = goalA.get(goalA.size() - 1);

        moneyGoal = goalN.getMoney();
//        Snackbar.make(mcoo, "目前已经攒了 "+moneyZan+" 元了！", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
//        Toast.makeText(AnalyzeActivity.this,"目前已经攒了 "+moneyGoal+"元了！",Toast.LENGTH_LONG).show();




        if(moneyZan > 0 && moneyZan < moneyGoal){

            zanTextView.setText("目前已经成功攒了 "+moneyZan+" 元了！");
            //按照类别
            pieGraph2=(PieGraph)findViewById(R.id.pieGraph2);

            //目标 红
            slice=new PieSlice();
            slice.setColor(Color.parseColor("#FF0000"));
            slice.setValue((int)(moneyGoal-moneyZan));
            pieGraph2.addSlice(slice);


            slice = new PieSlice();
            slice.setColor(Color.parseColor("#FFBB33"));
            slice.setValue((int)moneyZan);
            pieGraph2.addSlice(slice);

        }

        else if (moneyZan <= 0){
            zanTextView.setText("");
            viTextView.setVisibility(View.GONE);
            viLayout1.setVisibility(View.GONE);
            viLayout2.setVisibility(View.GONE);
            Snackbar.make(mcoo, "当前还没有开始财富积累哦~", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        else if (moneyZan >= moneyGoal){
            zanTextView.setText("");
            viTextView.setVisibility(View.GONE);
            viLayout1.setVisibility(View.GONE);
            viLayout2.setVisibility(View.GONE);
            Snackbar.make(mcoo, "少年，你当前的攒钱目标已经完成了！", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }





    }

}
