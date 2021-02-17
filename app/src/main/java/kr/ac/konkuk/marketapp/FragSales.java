package kr.ac.konkuk.marketapp;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;

public class FragSales extends Fragment
{ // 프레그먼트는 액티비티의 자식 개념이라고 생각하면된다
    //다른액티비티에서 이 프레그먼트에 무엇인가를 전달하려면 이 프레그먼트가 있는 액티비티로 먼저 전달한 후 액티비티에서 프레그먼트로 전달해주면 된다.

    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<SalesItem> arrayList;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference; //파이어베이스 데이터베이스

    private Button btn_add; //item 추가 버튼

    private InterstitialAd mInterstitialAd; // 애드몹 전면광고 객체

    public static FragSales newInstance( )
    {
        FragSales fragSales = new FragSales(); ////FragSales을 생성하는 구문

        return fragSales;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    { //프래그먼트 시작 시 수행하는 생명 주기
        view = inflater.inflate(R.layout.frag_sales, container, false);

        mInterstitialAd = new InterstitialAd(getContext()); //액티비티이면 this , fragment일 경우 getContext()
        mInterstitialAd.setAdUnitId("ca-app-pub-5570932833347277/1853320725");

        mInterstitialAd.loadAd(new AdRequest.Builder().build()); //로드

        btn_add = view.findViewById(R.id.btn_add);//아이템 추가버튼

        recyclerView = view.findViewById(R.id.rv_sales); //id 연동
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존 성능 강화
        layoutManager = new LinearLayoutManager(getContext());

        databaseReference = FirebaseDatabase.getInstance().getReference("SalesItem"); // 파이어베이스 데이터베이스 연동
        //path 이름이 동일해야만 가져올 수 있다.



        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); //판매목록을 담아주려는 빈 배열 리스트 생성

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                arrayList.clear(); //초기화(기존 배열리스트가 존재하지 않게 초기화)
                // 파이어베이스 db의 데이터들을 가지고 오는 곳
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                { //반복문을 통해 데이터 List를 추출해냄.
                    SalesItem salesItem = snapshot.getValue(SalesItem.class); //만들어뒀던 SalesItem 모델 객체에 데이터를 input (서버로 부터 가져온 데이터)
                    arrayList.add(salesItem);
                }

                //어댑터의 기능을 정상적으로 하기 위한 위치
                //for문이 끝난 시점에는 arraylist가 존재한다고 보장받는 시점이기 때문에 어댑터가 이 곳에 위치하는 것이 생명주기상 더 적절
                adapter = new SalesAdapter(arrayList, getContext()); //어댑터를 생성하면서 리스트를 어댑터에 넘긴다.
                // 프래그먼트에서는 getContext()를 하면 편하게 context를 가져올 수 있다.
                recyclerView.setAdapter(adapter); //리사이클러뷰에 커스텀 어댑터를 연결(장착)

                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

//        SalesItem salesItem = new SalesItem(); //판매 목록 데이터 클래스
//        salesItem.setTv_product("게임기");
//        salesItem.setTv_price("50000원");
//        salesItem.setTv_date("2021-02-04");
//        arrayList.add(salesItem); //배열리스트에 추가.
//
//        SalesItem salesItem2 = new SalesItem(); //판매 목록 데이터 클래스
//        salesItem2.setTv_product("마우스");
//        salesItem2.setTv_price("20000원");
//        salesItem2.setTv_date("2021-02-04");
//        arrayList.add(salesItem2); //배열리스트에 추가.
//
//        SalesItem salesItem3 = new SalesItem(); //판매 목록 데이터 클래스
//        salesItem3.setTv_product("키보드");
//        salesItem3.setTv_price("30000원");
//        salesItem3.setTv_date("2021-02-04");
//        arrayList.add(salesItem3); //배열리스트에 추가

        btn_add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                전면광고 로직
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                final Dialog dialogAdd = new Dialog(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert);
                dialogAdd.setContentView(R.layout.dialog_add); // xml 레이아웃 연결

                final EditText et_product = dialogAdd.findViewById(R.id.et_product); //상품명
                final EditText et_price = dialogAdd.findViewById(R.id.et_price); //가격
                final EditText et_date = dialogAdd.findViewById(R.id.et_date); //날짜
                final EditText et_pass = dialogAdd.findViewById(R.id.et_pass); //비밀번호
                Button btn_add = dialogAdd.findViewById(R.id.btn_add); //추가 버튼

                btn_add.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (et_product.getText().length() == 0 || et_price.getText().length() == 0 || et_date.getText().length() == 0 || et_pass.getText().length() == 0)
                        {
                            Toast.makeText(getContext(), "비어있는 입력필드가 존재합니다", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        SalesItem item = new SalesItem(); //salesItem에 커서를 두고 ctrl+b를 누르면 모델로 바로 진입 가능
                        item.setTv_product(et_product.getText().toString());
                        item.setTv_price(et_price.getText().toString());
                        item.setTv_date(et_date.getText().toString());
                        item.setTv_password(et_pass.getText().toString());
                        //여기까지가 로컬에서의 CRUD구현

                        //이제 서버에서의 CRUD
                        //수정이나 삭제를 할때 비밀번호를 입력하게끔해서 처리하게 구현
                        databaseReference.push().setValue(item); //랜덤한 키 값을 만들어낸 다음에 차일드로 item을 넣음

                        arrayList.add(0, item); //가장 위로 추가 해주는 방식
                        adapter.notifyItemInserted(0); //추가 새로고침


                        dialogAdd.dismiss(); // 다이얼로그 닫기
                    }
                });

                dialogAdd.show(); // 다이얼로그 실행
            }
        });


        return view; //inflater구문과 return view 까지 하면 뷰까지 정상적으로 연결
    }
}
