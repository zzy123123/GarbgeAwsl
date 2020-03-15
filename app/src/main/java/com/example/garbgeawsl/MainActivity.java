package com.example.garbgeawsl;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
//主界面业务逻辑
public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideBottomUIMenu();//隐藏导航栏及状态栏
    }

    protected void hideBottomUIMenu(){
        ////隐藏导航栏及状态栏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // 这是对于低版本
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {   //这是实际用的版本哦
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


}


