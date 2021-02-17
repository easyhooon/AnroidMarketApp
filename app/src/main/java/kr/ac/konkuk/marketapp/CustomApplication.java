package kr.ac.konkuk.marketapp;

import androidx.multidex.MultiDexApplication;

//멀티덱스 활성화를 위한 클래스
public class CustomApplication extends MultiDexApplication
{
    private static CustomApplication instance;

    //Ctrl + o
    @Override
    public void onCreate( )
    {
        super.onCreate();
        instance = this;
    }

}
