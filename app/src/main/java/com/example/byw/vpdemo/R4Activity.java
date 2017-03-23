/*
 * Copyright (c) 2017.
 *
 * 作者 ： 饭票两角
 *
 * 最后编辑时间 ： 17-3-22 下午8:50
 */

package com.example.byw.vpdemo;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import static com.example.byw.vpdemo.MainActivity.gestureflag;

public class R4Activity extends AppCompatActivity {
    private RadioGroup rg1, rg2, rg3, rg4, rg5;
    private float n1;
    private float n2;
    private float t;
    private String d, p;
    private TextView tv_rr, tv_p, tv_d;
    private TextView tv_r1, tv_r2, tv_r3, tv_r4, tv_r5;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r4);

        // 新建手势检测器实例
        gestureDetector = new GestureDetector(this, new R4Activity.MyGestureListener());

        // 设置Actionbar左上角返回按钮
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 初始化计算因子
        n1 = 8;
        n2 = 8;
        t = 1;
        d = "Ω";
        p = "±1%";

        // 设置文本对象
        tv_rr = (TextView)findViewById(R.id.tv_rr);
        tv_p = (TextView)findViewById(R.id.tv_p);
        tv_d = (TextView)findViewById(R.id.tv_d);

        // 设置电阻色环对象
        tv_r1 = (TextView)findViewById(R.id.tv_r1);
        tv_r2 = (TextView)findViewById(R.id.tv_r2);
        tv_r4 = (TextView)findViewById(R.id.tv_r4);
        tv_r5 = (TextView)findViewById(R.id.tv_r5);

        // 为单选组设置默认值并设置监听
        rg1 = (RadioGroup)findViewById(R.id.rg1);
        rg1.check(R.id.rb1_9);
        rg1.setOnCheckedChangeListener(new MyRGCheckedListener());

        rg2 = (RadioGroup)findViewById(R.id.rg2);
        rg2.check(R.id.rb2_9);
        rg2.setOnCheckedChangeListener(new MyRGCheckedListener());

        rg4 = (RadioGroup)findViewById(R.id.rg4);
        rg4.check(R.id.rb4_1);
        rg4.setOnCheckedChangeListener(new MyRGCheckedListener());

        rg5 = (RadioGroup)findViewById(R.id.rg5);
        rg5.check(R.id.rb5_1);
        rg5.setOnCheckedChangeListener(new MyRGCheckedListener());


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 如果不允许手势，则拦截手势，不再传入
        return !gestureflag || gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // 如果检测为单击返回按钮，则返回上一级
        if(item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     *  自定义单选监听类，对单选时间进行处理计算得出阻值
     *
     */
    private class MyRGCheckedListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
//            Log.d("FPLJ", "one of rg was be checked");
            if (group == rg1){
                RadioButton rb = (RadioButton)findViewById(checkedId);
                ColorDrawable colorDrawable = (ColorDrawable)rb.getBackground();
                tv_r1.setBackgroundColor(colorDrawable.getColor());
//                Log.d("FPLJ", "rg1 was be checked");
                switch (checkedId){
                    case R.id.rb1_1: n1 = 0;
                        break;
                    case R.id.rb1_2: n1 = 1;
                        break;
                    case R.id.rb1_3: n1 = 2;
                        break;
                    case R.id.rb1_4: n1 = 3;
                        break;
                    case R.id.rb1_5: n1 = 4;
                        break;
                    case R.id.rb1_6: n1 = 5;
                        break;
                    case R.id.rb1_7: n1 = 6;
                        break;
                    case R.id.rb1_8: n1 = 7;
                        break;
                    case R.id.rb1_9: n1 = 8;
                        break;
                    case R.id.rb1_10: n1 = 9;
                        break;
                }

            }
            else if(group == rg2){
                RadioButton rb = (RadioButton)findViewById(checkedId);
                ColorDrawable colorDrawable = (ColorDrawable)rb.getBackground();
                tv_r2.setBackgroundColor(colorDrawable.getColor());
//                Log.d("FPLJ", "rg2 be checked");
                switch (checkedId){
                    case R.id.rb2_1: n2 = 0;
                        break;
                    case R.id.rb2_2: n2 = 1;
                        break;
                    case R.id.rb2_3: n2 = 2;
                        break;
                    case R.id.rb2_4: n2 = 3;
                        break;
                    case R.id.rb2_5: n2 = 4;
                        break;
                    case R.id.rb2_6: n2 = 5;
                        break;
                    case R.id.rb2_7: n2 = 6;
                        break;
                    case R.id.rb2_8: n2 = 7;
                        break;
                    case R.id.rb2_9: n2 = 8;
                        break;
                    case R.id.rb2_10: n2 = 9;
                        break;
                }
            }
            else if(group == rg4){
                RadioButton rb = (RadioButton)findViewById(checkedId);
                ColorDrawable colorDrawable = (ColorDrawable)rb.getBackground();
                tv_r4.setBackgroundColor(colorDrawable.getColor());
//                Log.d("FPLJ", "rg4 be checked");
                switch (checkedId){
                    case R.id.rb4_1: t = 1;
                        d = "Ω";
                        break;
                    case R.id.rb4_2: t = 10;
                        d = "Ω";
                        break;
                    case R.id.rb4_3: t = 100;
                        d = "Ω";
                        break;
                    case R.id.rb4_4: t = 1;
                        d = "kΩ";
                        break;
                    case R.id.rb4_5: t = 10;
                        d = "kΩ";
                        break;
                    case R.id.rb4_6: t = 100;
                        d = "kΩ";
                        break;
                    case R.id.rb4_7: t = 1;
                        d = "MΩ";
                        break;
                    case R.id.rb4_8: t = 10;
                        d = "MΩ";
                        break;
                    case R.id.rb4_9: t = 100;
                        d = "MΩ";
                        break;
                    case R.id.rb4_10: t = 1000;
                        d = "MΩ";
                        break;
                    case R.id.rb4_11: t = 0.1f;
                        d = "Ω";
                        break;
                    case R.id.rb4_12: t = 0.01f;
                        d = "Ω";
                        break;
                }
            }
            else if(group == rg5){
                RadioButton rb = (RadioButton)findViewById(checkedId);
                ColorDrawable colorDrawable = (ColorDrawable)rb.getBackground();
                tv_r5.setBackgroundColor(colorDrawable.getColor());
//                Log.d("FPLJ", "rg5 be checked");
                switch (checkedId) {
                    case R.id.rb5_1: p = "±1%";
                        break;
                    case R.id.rb5_2: p = "±2%";
                        break;
                    case R.id.rb5_3: p = "±3%";
                        break;
                    case R.id.rb5_4: p = "±5%";
                        break;
                    case R.id.rb5_5: p = "±10%";
                        break;
                }
            }

            float r = (n1 * 10 + n2) * t;
            // 显示去小数点后无关零
            if(r - (int)r <= 0) tv_rr.setText("" + (int)r);
            else tv_rr.setText("" + r);
            tv_d.setText(d);
            tv_p.setText(p);
        }
    }


    /**
     *  自定义手势监听类，从左侧侧边滑动到中部为返回上一级
     *
     */
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
                MainActivity.cancelToast();
                finish();
            }
            return false;
        }
    }

}


