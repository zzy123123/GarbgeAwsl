package com.example.garbgeawsl;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.widget.TextView;

import com.example.garbgeawsl.Card.CardItem;
import com.example.garbgeawsl.Card.CardPagerAdapter;
import com.example.garbgeawsl.Card.ShadowTransformer;
import com.example.garbgeawsl.Util.DBUtils;

import java.util.HashMap;
import java.util.List;

import com.example.garbgeawsl.Search.ICallBack;
import com.example.garbgeawsl.Search.SearchView;
import com.example.garbgeawsl.Util.DisplayUtils;


//主界面业务逻辑
public class MainActivity extends BaseActivity {

    private TextView tv_data;
    private SearchView searchView;
    private ViewPager mViewPager;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private TextView mTextMessage;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 0x11:
                    String s = (String) msg.obj;
                    tv_data.setText(s);
                    break;
                case 0x12:
                    String ss = (String) msg.obj;
                    tv_data.setText(ss);
                    break;
            }

        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    mViewPager.setVisibility(View.GONE);
                    searchView.setVisibility(View.GONE);
                    tv_data.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    mViewPager.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.VISIBLE);
                    tv_data.setVisibility(View.VISIBLE);

                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    mViewPager.setVisibility(View.GONE);
                    searchView.setVisibility(View.GONE);
                    tv_data.setVisibility(View.GONE);
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setVisibility(View.GONE);
        mTextMessage = findViewById(R.id.message);
        tv_data = findViewById(R.id.tv_data);
        tv_data.setVisibility(View.GONE);
        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setVisibility(View.GONE);

        mCardAdapter = new CardPagerAdapter();
        mCardAdapter.addCardItem(new CardItem(R.string.title_1, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_2, R.string.text_2));
        mCardAdapter.addCardItem(new CardItem(R.string.title_3, R.string.text_3));
        mCardAdapter.addCardItem(new CardItem(R.string.title_4, R.string.text_4));

        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mCardShadowTransformer.enableScaling(true);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        hideBottomUIMenu();//隐藏导航栏及状态栏

        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(final String string) {
                // 创建一个线程来连接数据库并获取数据库中对应表的数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 调用数据库工具类DBUtils的getInfoByName方法获取数据库表中数据
                        HashMap<String, Object> map = DBUtils.getInfoByName(string);
                        Message message = handler.obtainMessage();
                        if(map != null){
                            String s = "";
                            for (String key : map.keySet()){
                                s += key + ":" + map.get(key) + "\n";
                            }
                            message.what = 0x12;
                            message.obj = s;
                        }else {
                            message.what = 0x11;
                            message.obj = "查询结果为空";
                        }
                        // 发消息通知主线程更新UI
                        handler.sendMessage(message);
                    }
                }).start();
                hideBottomUIMenu();
            }
        });

    }

    protected void hideBottomUIMenu(){
        ////隐藏导航栏及状态栏

        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // 这是对于低版本
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {   //这是实际用的版本哦
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY ;
            decorView.setSystemUiVisibility(uiOptions);
        }

    }

    @Override
     public boolean dispatchTouchEvent(MotionEvent ev) {
                DisplayUtils.hideInputWhenTouchOtherView(this, ev, getExcludeTouchHideInputViews());
                hideBottomUIMenu();
                 return super.dispatchTouchEvent(ev);
           }

           private List<View> getExcludeTouchHideInputViews() {
                return null;
           }


}


