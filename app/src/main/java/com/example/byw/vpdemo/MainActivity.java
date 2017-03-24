/*
 * Copyright (c) 2017.
 *
 * 作者 ： 饭票两角
 *
 * 最后编辑时间 ： 17-3-6 上午11:49
 */

package com.example.byw.vpdemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主界面，选择各个功能部位
 *
 */
public class MainActivity extends AppCompatActivity {

    private View page1, page2, page3; // 各个卡页
    private ViewPager viewPager; // 卡页内容
    private TabHost tabHost; // 导航栏
    private AlertDialog.Builder builder; // 对话框管理器
    protected static boolean quitflag = true; // 退出确认标志
    protected static boolean gestureflag = true;// 使用手势返回标志
    protected static SharedPreferences spf;
    protected static Toast mToast;


    @Override
    protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        initData(); // 初始化设置数据
        initTab(); // 初始化导航
        initPage(); // 初始化各页面
        initViewPager(); // 初始化viewpager
        initDialog(); // 初始化关闭对话框
//        Log.d("FPLJ", "主活动创建");
    }

    //菜单布局
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //菜单操作
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.item_setting)
        startActivity(new Intent(MainActivity.this, SettingActivity.class));
        return super.onOptionsItemSelected(item);
    }

    /**
     * 重写后退键方法
     *
     */
    @Override
    public void onBackPressed(){
        // 创建关闭对话框
        if (quitflag)
        builder.create().show();
        else super.onBackPressed();
    }

    /**
     * 重写Destroy方法，处理程序关闭后的信息
     *
     */
    @Override
    protected void onDestroy(){
        super.onDestroy();
        // TODO: 2017/3/13 此处未对程序关闭做处理
//        Log.d("FPLJ", "主活动关闭");
    }
    private void initData(){
        // 定义或者打开静态（其他活动可以直接调用）数据文件管理对象
        spf = getSharedPreferences("flagData", MODE_PRIVATE);
        quitflag = spf.getBoolean("quitflag",true);
        gestureflag = spf.getBoolean("gestureflag",true);
    }

    /**
     * 初始化导航
     *
     */
    private void initTab(){
        int j;
        tabHost = (TabHost)findViewById(R.id.tabhost);
        // 执行addTab方法之前必须执行一次setup方法
        tabHost.setup();
        // 将一个空的xml添加到tabcontent中，tabcontent设置属性为隐藏（gone）
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        layoutInflater.inflate(R.layout.layout_empty, tabHost.getTabContentView());
        // 添加标签和对应空xml到tabcontent中
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("电气计算")
                .setContent(R.id.layout_empty));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("电气查询")
                .setContent(R.id.layout_empty));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("关于")
                .setContent(R.id.layout_empty));

        // 设置tab栏
        for(j = 0; j < 3; j++ ){
            View view;
            TextView textView;
            view = tabHost.getTabWidget().getChildAt(j);
            textView = (TextView)view.findViewById(android.R.id.title);
            textView.setTextSize(16);//设置标题字体大小
            textView.setTypeface(Typeface.SANS_SERIF, 1); // 设置字体和风格
            view.getLayoutParams().height = 140;// 设置tab高度
        }
        // 实例化一个监听器对象，绑定在tab栏上
        tabHost.setOnTabChangedListener(new MyTabChangeListener());
    }

    /**
     * 初始化各个页面
     *
     */
    private void initPage(){

        String[] data1 = { "线缆压降计算", "四色环电阻计算", "五色环电阻计算", "后续添加"};
        String[] data2 = { "美标线缆信息查询","贴片电阻阻值查询","后续添加"};
        int[] imageId1 = {R.drawable.cable, R.drawable.r4, R.drawable.r5, R.drawable.cal};
        int[] imageId2 = {R.drawable.awg, R.drawable.smtr, R.drawable.search};

        List<Map<String, Object>> listitems1 = new ArrayList<>();
        for (int i = 0; i < data1.length; i++) {
            Map<String, Object> listitem1 = new HashMap<>();
            listitem1.put("data1", data1[i]);
            listitem1.put("icon1", imageId1[i]);
            listitems1.add(listitem1);
        }

        SimpleAdapter simplead1 = new SimpleAdapter(this, listitems1,
                R.layout.list_items1, new String[] { "data1", "icon1"},
                new int[] {R.id.item_tv1, R.id.item_icon1});


        List<Map<String, Object>> listitems2 = new ArrayList<>();
        for (int i = 0; i < data2.length; i++) {
            Map<String, Object> listitem2 = new HashMap<>();
            listitem2.put("data2", data2[i]);
            listitem2.put("icon2", imageId2[i]);
            listitems2.add(listitem2);
        }

        SimpleAdapter simplead2 = new SimpleAdapter(this, listitems2,
                R.layout.list_items2, new String[] { "data2", "icon2"},
                new int[] {R.id.item_tv2, R.id.item_icon2});


        LayoutInflater inflater = getLayoutInflater();
        // 将页面xml转化为view对象，并设置各个页面显示内容
        page1 = inflater.inflate(R.layout.layout_list1, viewPager, false);
        ListView lv1 = (ListView) page1.findViewById(R.id.lv1);
        lv1.setAdapter(simplead1);
        lv1.setOnItemClickListener(new page1ItemClickListener());

        page2 = inflater.inflate(R.layout.layout_list2, viewPager, false);
        ListView lv2 = (ListView) page2.findViewById(R.id.lv2);
        lv2.setAdapter(simplead2);
        lv2.setOnItemClickListener(new page2ItemClickListener());

        page3 = inflater.inflate(R.layout.layout_page3, viewPager, false);
    }

    /**
     * 初始化viewpager
     *
     */
    private void initViewPager()
    {
        // 将页面view添加到页面列表组中
        List<View> viewList = new ArrayList<>();
        viewList.add(page1);
        viewList.add(page2);
        viewList.add(page3);
        viewPager = (ViewPager)findViewById(R.id.viewpage);
        // 实例化一个适配器，为viewPager绑定此适配器
        viewPager.setAdapter(new MyPageAdapter(viewList));
        // 设置viewPager初始显示画面
        viewPager.setCurrentItem(0);
        // 实例化一个页面监听对象，并绑定到此viewPager上
        viewPager.addOnPageChangeListener(new MyPageListener());
    }

    private void initDialog(){
        builder = new AlertDialog.Builder(this,R.style.DialogTheme);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("注意");
        builder.setMessage("是否退出应用程序？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    /**
     * 自定义适配器类
     *
     */
    class MyPageAdapter extends PagerAdapter{
        private List<View> viewList;
        // 定义构造函数，将页面列表传入
        MyPageAdapter(List<View> viewList){
            this.viewList = viewList;
        }

        @Override
        public int getCount() { return viewList.size(); }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }
    }

    /**
     * 自定义tab监听器
     *
     */
    class  MyTabChangeListener implements TabHost.OnTabChangeListener {
        private int i = 0;
        // 重写onTabChanged方法，tab改变，对应改变页面
        @Override
        public void onTabChanged(String tabId) {
//            Log.d("FPLJ","tab改变"+tabId);
            switch (tabId){
                case "tab1": i = 0;
                    break;
                case "tab2": i = 1;
                    break;
                case "tab3": i = 2;
                    break;
            }
            // 防止重复调用
            if(viewPager.getCurrentItem() != i) {
            viewPager.setCurrentItem(i);
//            Log.d("FPLJ","设置viewpage"+viewPager.getCurrentItem());
            }
        }
    }

    /**
     * 自定义页面更改监听器
     *
     */
    private class MyPageListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        //重写onPageSelected,更改页面，对应的更改导航tab
        @Override
        public void onPageSelected(int position) {
//            Log.d("FPLJ","view页面改变"+position+""+tabHost.getCurrentTab());
            if(tabHost.getCurrentTab() != position) {
                tabHost.setCurrentTab(position);
//                Log.d("FPLJ", "设置tab" + tabHost.getCurrentTab());
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    //实例化一个listview 点击监听接口
    private class page1ItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 0 :
                    startActivity(new Intent(MainActivity.this, CableCalculateActivity.class));
                    break;
                case 1 :
                    startActivity(new Intent(MainActivity.this, R4Activity.class));
                    break;
                case 2:
                    startActivity(new Intent(MainActivity.this, R5Activity.class));
                    break;
            }
        }
    }

    private class page2ItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 0:
                    startActivity(new Intent(MainActivity.this, CableQueryActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(MainActivity.this, SmtrActivity.class));
                    break;
            }
        }
    }

    /**
     * 自义定静态Toast显示方法，可用于整个应用
     * @param mContext 要显示的活动
     * @param string 要显示的内容
     */
    protected static void showToast(Context mContext, String string){
        if(mToast == null){
            mToast = Toast.makeText(mContext, string, Toast.LENGTH_SHORT);
        }
        else{
            mToast.setText(string);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * 自定义静态Toast阻止方法
     *
     */
    protected static void cancelToast(){
        if(mToast != null) mToast.cancel();
        mToast = null;
    }
}