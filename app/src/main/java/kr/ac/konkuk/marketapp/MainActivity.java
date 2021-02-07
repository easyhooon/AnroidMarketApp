package kr.ac.konkuk.marketapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    //home이랑 end 버튼을 누르면 그 줄에 첫줄과 끝줄로 이동됨

    TextView tv_title;
    Button btn_buy;
    EditText et_name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_title = findViewById(R.id.tv_title);
        btn_buy = findViewById(R.id.btn_buy);
        et_name = findViewById(R.id.et_name);

        tv_title.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tv_title.setText("요네즈 켄시 노래 내라");
            }
        });

        btn_buy.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class); //앞에는 나 자신.this 뒤에는 이동할 액티비티.class 이 기능이 이제 intent 객체에 담김
                startActivity(intent);
                Toast.makeText(MainActivity.this, et_name.getText().toString() + "이/가 구매 되었습니다.", Toast.LENGTH_SHORT).show();

            }
        });


    }
}