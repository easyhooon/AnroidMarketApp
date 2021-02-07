package kr.ac.konkuk.marketapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragMain extends Fragment
{ // 프레그먼트는 액티비티의 자식 개념이라고 생각하면된다
    //다른액티비티에서 이 프레그먼트에 무엇인가를 전달하려면 이 프레그먼트가 있는 액티비티로 먼저 전달한 후 액티비티에서 프레그먼트로 전달해주면 된다.

    private View view;

    public static FragMain newInstance( )
    {
        FragMain fragMain = new FragMain(); //FragMain을 생성하는 구문

        return fragMain;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.frag_main, container, false);

        return view; //inflater구문과 return view 까지 하면 뷰까지 정상적으로 연결
    }
}
