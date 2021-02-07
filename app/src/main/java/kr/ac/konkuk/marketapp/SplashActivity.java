package kr.ac.konkuk.marketapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity
{

    //약 3초간 스플래시 화면을 띄우고 메인 액티비티 돌입

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run( )
            {
                //3초 뒤에 일어날 액션을 구현
                Intent intent = new Intent(SplashActivity.this, ViewPagerActivity.class);
                startActivity(intent);
                finish(); // 현재 액티비티를 파괴 (다음에 쓰지 않기 때문에
            }
        }, 3000);
    }
}