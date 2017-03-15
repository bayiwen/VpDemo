/*
 * Copyright (c) 2017.
 *
 * 作者 ： 饭票两角
 *
 * 最后编辑时间 ： 17-3-5 下午4:04
 */

package com.example.byw.vpdemo;

import android.content.Context;
import android.graphics.Paint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigDecimal;

import static com.example.byw.vpdemo.MainActivity.gestureflag;

public class CableCalculateActivity extends AppCompatActivity {

    private Spinner spn_cabletype, spn_section; // 定义下拉选择框
    private EditText et_section, et_cl, et_cd; // 定义输入框
    private TextView tv_section, tv_r, tv_v; // 定义文本内容
    private GestureDetector gestureDetector;  // 定义手势管理器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cable);
        // 设置Actionbar左上角返回按钮
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // 新建手势检测器实例
        gestureDetector = new GestureDetector(this, new MyGestureListener());
        // 获取加载布局的各个控件
        spn_cabletype = (Spinner)findViewById(R.id.spn_cabletype);
        spn_section = (Spinner)findViewById(R.id.spn_section);
        et_section = (EditText)findViewById(R.id.et_section);
        et_cl = (EditText)findViewById(R.id.et_length);
        et_cd = (EditText)findViewById(R.id.et_current);
        et_cd.setOnKeyListener(new View.OnKeyListener() {


            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == event.KEYCODE_ENTER) {

                    // 获取输入法管理对象
                    InputMethodManager imm = (InputMethodManager)
                            CableCalculateActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    // 隐藏输入法
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    // 清除EditText焦点
                    getCurrentFocus().clearFocus();
                    // 计算数据
                    cableCalculate();
                    return true ;
                }
                else return false;
            }
        });
        tv_section = (TextView)findViewById(R.id.tv_section);
        tv_r = (TextView)findViewById(R.id.tv_r);
        tv_v = (TextView)findViewById(R.id.tv_v);

        // 设置输出文本格式：斜体+加粗
        tv_r.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_v.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        //对界面选择下拉框绑定选择监听，根据不同类型显示不同单位
        spn_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    // 如果选择的是线径，则单位显示为mm
                    case 0: tv_section.setText("mm");
                        break;
                    // 如果选择的是面积，则单位显示为mm^2
                    case 1: tv_section.setText(R.string.cable_area);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 为计算按钮绑定点击监听，如果单击计算按钮，则开始计算压降和线阻
        Button btn_c = (Button) findViewById(R.id.btn_calculate);
        btn_c.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Log.d("FPLJ","计算按钮被点击了");
                // 获取输入法管理对象
                InputMethodManager imm = (InputMethodManager)
                        CableCalculateActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏输入法
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                // 清除EditText焦点
                getCurrentFocus().clearFocus();
                // 计算数据
                cableCalculate();
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
//        Log.d("FPLJ", "传入动作");
        if(gestureflag){
            return  gestureDetector.onTouchEvent(event);
        }
        else return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        // 如果检测为单击返回按钮，则返回上一级
        if(item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }


    /**
     *  压降计算函数
     *  无输入参数，无返回参数
     *
     */

    private void cableCalculate(){
        // 定义各个成员变量；
        // V压降，I电流，R电阻，L线缆长度，S线缆截面积，p材质电阻率
        float  V, I, R, L, S, p;
        //初始化电阻率及线缆截面积参数
        p=0;
        S=0;
        // 判断输入参数是否为空，为空则提示参数无效
        if(TextUtils.isEmpty(et_section.getText())
                |TextUtils.isEmpty(et_cd.getText())
                |TextUtils.isEmpty(et_cl.getText()))
            Toast.makeText(this, "输入不能为空，请重新输入",Toast.LENGTH_SHORT).show();
        else if(0 >= Float.parseFloat(et_section.getText().toString().trim())
                |0 >= Float.parseFloat(et_cl.getText().toString().trim())
                |0 >= Float.parseFloat(et_cd.getText().toString().trim()))
            Toast.makeText(this, "输入参数有误，请重新输入",Toast.LENGTH_SHORT).show();

        else {
            //根据材质不同，确定电阻率
            switch (spn_cabletype.getSelectedItemPosition()){
                case 0: p = 17.5f;
                    break;
                case 1: p = 28.3f;
                    break;
            }

            //根据截面类型，计算截面积
            switch (spn_section.getSelectedItemPosition()) {
                case 0:
                    float r;
                    r = Float.parseFloat(et_section.getText().toString().trim());
                    S = (float) (Math.pow(r/2, 2)*(float)Math.PI);
                    break;
                case 1:
                    S = Float.parseFloat(et_section.getText().toString().trim());
                    break;
            }

            //获取线缆长度
            L = Float.parseFloat(et_cl.getText().toString().trim());
            //获取电流大小
            I = Float.parseFloat(et_cd.getText().toString().trim());
            //计算出线缆电阻值，四舍五入并保留三维有效数字
            R = new BigDecimal(p*L/S).setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
            //计算出线缆压降值，四舍五入并保留三维有效数字
            V = new BigDecimal(I*p*L/S).setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
            //将计算的结果显示在屏幕上
            tv_r.setText(String.format("%s", R));
            tv_v.setText(String.format("%s", V));
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
            }
            return false;
        }
    }
}
