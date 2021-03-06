/*
 * Copyright (c) 2017.
 *
 * 作者 ： 饭票两角
 *
 * 最后编辑时间 ： 17-2-18 上午8:36
 */

package com.example.byw.vpdemo;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import static android.view.KeyEvent.KEYCODE_BACK;
import static com.example.byw.vpdemo.MainActivity.gestureflag;
import static com.example.byw.vpdemo.MainActivity.mToast;
import static com.example.byw.vpdemo.MainActivity.quitflag;
import static com.example.byw.vpdemo.MainActivity.spf;

public class SettingActivity extends AppCompatActivity {

    private GestureDetector gestureDetector;  // 定义手势管理器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        // 新建手势检测器实例
        gestureDetector = new GestureDetector(this, new MyGestureListener());

        Switch sw_quit = (Switch)findViewById(R.id.sw_quit);
        sw_quit.setChecked(quitflag);
        sw_quit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = spf.edit();

                if(isChecked)
                    editor.putBoolean("quitflag", true);
                else  editor.putBoolean("quitflag", false);
                if(editor.commit()) {
                    quitflag = spf.getBoolean("quitflag", true);
//                    Toast.makeText(SettingActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    MainActivity.showToast(SettingActivity.this, "操作成功");
                }
//                else Toast.makeText(SettingActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                else MainActivity.showToast(SettingActivity.this, "操作失败");
            }
        });


        Switch sw_gesture =(Switch)findViewById(R.id.sw_gesture);
        sw_gesture.setChecked(gestureflag);
        sw_gesture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = spf.edit();

                if(isChecked)
                    editor.putBoolean("gestureflag", true);
                else  editor.putBoolean("gestureflag", false);
                if(editor.commit()){
                    gestureflag = spf.getBoolean("gestureflag", true);
//                    Toast.makeText(SettingActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    MainActivity.showToast(SettingActivity.this, "手势设置成功");
                }
//                else Toast.makeText(SettingActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
                else MainActivity.showToast(SettingActivity.this, "设置失败");
            }
        });

        Button btn_del = (Button)findViewById(R.id.btn_del);
        btn_del.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(SettingActivity.this.deleteDatabase("cable.db")&&SettingActivity.this.deleteDatabase("smtr.db")){
//                    Toast.makeText(SettingActivity.this, "删除数据库成功", Toast.LENGTH_SHORT).show();
                    MainActivity.showToast(SettingActivity.this, "删除数据库成功");
//                    Log.d("FPLJ", "删除cable数据库成功");
                }

                else{
//                    Toast.makeText(SettingActivity.this, "数据库已经被删除", Toast.LENGTH_SHORT).show();
                    MainActivity.showToast(SettingActivity.this, "数据库已经被删除");
//                    Log.d("FPLJ", "删除cable数据库失败");
                }
            }
        });
    }

    /**
     * 重写返回键按下处理，如果有Toast，返回键按下后不再显示
     * @param keyCode 按键值
     * @param event 事件
     * @return 如果是返回键，处理后不再分发，不是返回键，继续分发
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KEYCODE_BACK){
            super.onKeyDown(keyCode, event);
            MainActivity.cancelToast();
            return true;
        }
        else return false;
    }

    /**
     * 将动作传入手势管理器
     * @param event 发生动作事件
     * @return true 事件处理完毕
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.d("FPLJ", "传入动作");
        return !gestureflag || gestureDetector.onTouchEvent(event);
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
                MainActivity.cancelToast();
                finish();
            }
            return false;
        }
    }

}
