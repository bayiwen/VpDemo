/*
 * Copyright (c) 2017.
 *
 * 作者 ： 饭票两角
 *
 * 最后编辑时间 ： 17-3-11 下午4:43
 */

package com.example.byw.vpdemo;

import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import android.graphics.Paint;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;

import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.example.byw.vpdemo.MainActivity.gestureflag;


public class CableQueryActivity extends AppCompatActivity {

    private static final String DB_PATH = "/data/data/com.example.byw.vpdemo/databases/";
    private static final String DB_NAME = "cable.db";
    private static final int VERSION = 1;

    private SQLiteDatabase MySQLiteDB = null;
    private GestureDetector gestureDetector;

    private EditText et_AWG;
    private TextView tv_AWG;
    private TextView tv_diameter;
    private TextView tv_area;
    private TextView tv_resistance;
    private TextView tv_current;
    private TextView tv_MAXcurrent;
    private String condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cable_query);

        // 设置Actionbar左上角返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 实例化一个自定义数据库管理对象，对数据库操作。传入当前活动类、数据库名称及版本号
        final MySQLiteOpenHelp dbhelp = new MySQLiteOpenHelp(this, DB_NAME, null, VERSION);
        InitDatabase(dbhelp);

        gestureDetector = new GestureDetector(this, new CableQueryActivity.MyGestureListener());

        // 定义查询条件
        et_AWG = (EditText)findViewById(R.id.et_AWG);
        // 绑定按键监听，监听回车键
        et_AWG.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    query();
                    return true;
                }
                else return false;
            }
        });
        // 定义查询结果
        tv_AWG = (TextView)findViewById(R.id.tv_AWG);
        tv_AWG.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_diameter = (TextView)findViewById(R.id.tv_diameter);
        tv_diameter.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_area = (TextView)findViewById(R.id.tv_area);
        tv_area.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_resistance = (TextView)findViewById(R.id.tv_resistance);
        tv_resistance.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_current = (TextView)findViewById(R.id.tv_current);
        tv_current.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_MAXcurrent = (TextView)findViewById(R.id.tv_MAXcurrent);
        tv_MAXcurrent.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        // 为查询按钮设置单击监听
        Button btn_query = (Button)findViewById(R.id.btn_query);
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Log.d("FPLJ","查询数据库按钮被点击了");
                // 查询相关信息
                query();
            }
        });
    }

    /**
     * 将此活动的手势动作传入动作管理器
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(gestureflag){
        return  gestureDetector.onTouchEvent(event);
        }
        else return true;
    }
    /**
     * 重新onDestroy方法，程序终止后，关闭数据库
     *
     */

    @Override
    protected void onDestroy(){
        super.onDestroy();
        MySQLiteDB.close();
//        Log.d("FPLJ","程序终止，关闭数据库");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // 如果检测为单击返回按钮，则返回上一级
        if(item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     *  初始化数据库，导入数据库（初次有效），
     *  打开数据库
     * @param dbhelp 传入数据库管理类
     */
    private void InitDatabase(MySQLiteOpenHelp dbhelp){

        // 导入数据库，如果已经导入则不再重复导入
        try {
            this.importdata(dbhelp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 打开数据库并置于内存中
        try{
            MySQLiteDB = dbhelp.openDatabase();
        } catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    private void query(){

        // 获取输入法管理对象
        InputMethodManager imm = (InputMethodManager)
                CableQueryActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏输入法
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        // 清除EditText焦点
        getCurrentFocus().clearFocus();
        // 获取查询条件 即线号
        condition = et_AWG.getText().toString().trim();
        // 判断输入参数是否为空，为空提示
        if(TextUtils.isEmpty(condition)) {
//                    Log.d("FPLJ", "查询参数为空");
            Toast.makeText(CableQueryActivity.this, "参数不能为空", Toast.LENGTH_SHORT)
                    .show();
        }
        else {
            Cursor c = MySQLiteDB.rawQuery("select * from cabledata where AWG=?",
                    new String[]{condition});
            // TODO: 2017/3/11 此处对数据库损坏情况未做异常处理
            c.moveToFirst();
            Log.d("FPLJ","输出查询结果");
            if(c.getCount() > 0){
                // 将结果显示在屏幕上
                tv_AWG.setText(c.getString(c.getColumnIndex("AWG")));
                tv_diameter.setText(c.getString(c.getColumnIndex("diameter")));
                tv_area.setText(c.getString(c.getColumnIndex("area")));
                tv_resistance.setText(c.getString(c.getColumnIndex("resistance")));
                tv_current.setText(c.getString(c.getColumnIndex("current")));
                tv_MAXcurrent.setText(c.getString(c.getColumnIndex("MAXcurrent")));
            }
            else {
                // 无相关结果，提示用户
                Toast.makeText(CableQueryActivity.this, "无此数据，请检查输入值", Toast.LENGTH_SHORT)
                        .show();
            }
            // 关闭游标，释放资源
            c.close();
        }


    }

    /**
     * 自定义数据库管理类
     * 包含四个方法：创建空白数据库、升级数据库、导入数据库、删除数据库
     */
    private class MySQLiteOpenHelp extends SQLiteOpenHelper {

        private String data_name;
        MySQLiteOpenHelp(Context context, String name,
                         SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            data_name = name;
        }

        /**
         * 创建空白数据库
         * @param db 数据
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("FPLJ","创建空白数据库");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        /**
         *  自定义打开数据库方法，不能打开则抛出异常
         *
         * @return 返回SQLiteDatabase对象
         * @throws SQLiteException 抛出异常
         */
        private SQLiteDatabase openDatabase ()throws SQLiteException {
//            Log.d("FPLJ", "打开数据库");
            return SQLiteDatabase.openDatabase(DB_PATH + data_name, null,
                    SQLiteDatabase.OPEN_READONLY);
        }
    }

    /**
     * 自定义数据库导入方法，不能导入则抛出异常
     * @param sqLiteOpenHelper 传入数据库管理对象
     * @throws IOException 抛出异常
     */
    void importdata(SQLiteOpenHelper sqLiteOpenHelper) throws IOException {
        // 在databases文件夹（Android默认数据库路径）下面新建空数据库文件
        File file = new File(DB_PATH + DB_NAME);
        if(file.exists()){
//            Log.d("FPLJ", "已有数据库，不再重复导入");
                    // do nothing
        }else{
            // 建立可写入空白数据库，待导入）
            sqLiteOpenHelper.getReadableDatabase();
            Log.d("FPLJ", "导入数据库");
            // 利用openRawResource方法返回raw资源中的数据流
            InputStream myInput = this.getResources().openRawResource(R.raw.cable);
            // 新建输出流，将原建空数据库文件绑定至此输出流
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            // 新建buff缓存区，大小和输入文件大小一致
            byte[] buff = new byte[myInput.available()];
            // 将输入流写入buff缓存字节组中，注意read为阻塞方法
            myInput.read(buff);
            // 关闭输入流，释放系统资源
            myInput.close();
            // 读取buff缓存中的字节组到输出流中，注意write为阻塞方法
            fileOutputStream.write(buff);
            // 刷新缓存buff，并关闭输出流，释放系统资源
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(this, "首次创建数据库成功",Toast.LENGTH_SHORT).show();
//            Log.d("FPLJ", "导入数据库完成");
        }
    }

    private class MyGestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

//            Log.d("FPLJ", "开始于" + e1.getX() + "结束于" + e2.getX());
            if((25 > e1.getX())&(e2.getX() > 280)){
                finish();
//                Log.d("FPLJ","finish 方法");
            }
            return true;
        }
    }

}
