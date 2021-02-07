package kr.ac.konkuk.marketapp;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//ctrl

//리사이클러 뷰랑 데이터모델(SalesItem)을 연동하는 과정을 그려내는 것, 어댑터가 리사이클러뷰에 붙어있어야됨
//어댑터: 콘센트에 코드를 꼽는 어댑터가 있어야 리사이클러뷰가 정상 작동을 할 수 있게 됨
public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.CustomViewHolder>
{

    private ArrayList<SalesItem> arrayList; // 어댑터 쪽에서 모델에서 만들어놓은 데이터값들의 배열을 쥐고있어야댐, 판매목록 리스트를 담고 있다.
    private Context context; //프래그먼트와 액티비티가 소통할때, 어댑터쪽이랑 소통을 할 때 걔네로 부터 Context를 가지고 올 것
    private DatabaseReference databaseReference; //파이어베이스 데이터베이스

    //생성자 구성
    public SalesAdapter(ArrayList<SalesItem> arrayList, Context context)
    {
        //FragSales로 부터 받아오는 arrayList와 context
        this.arrayList = arrayList; //어댑터쪽에서 넘겨받아서 세터가 되있는 친구들을 쥐고 있고(holder.tv_product.setText(arrayList.get(position).getTv_product());로)
        this.context = context;
    }

    @NonNull
    @Override
    public SalesAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sales_item, parent, false); //리사이클러뷰와 list item을 연결하는 순간
        CustomViewHolder holder = new CustomViewHolder(view);

        //위치는 여기
        databaseReference = FirebaseDatabase.getInstance().getReference("SalesItem"); // 파이어베이스 데이터베이스 연동

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SalesAdapter.CustomViewHolder holder, int position)
    { //뷰홀더와 연결
        holder.iv_profile.setImageResource(R.drawable.cart); //일단은 고정, 서버와 연결을 하면 그 이미지들에 대해서 연동(매칭)을 해줘야 함(판매목록에 따라 사진이 다 다르기 때문)

        //액티비티나 프래그먼트에서 직접 판매리스트를 임시로 만들어줄건데 그 데이터들를 끌고와서 연동하는 구문
        holder.tv_product.setText(arrayList.get(position).getTv_product()); //그 어레이리스트에서 getter로 꺼내오는 개념
        holder.tv_price.setText(arrayList.get(position).getTv_price());
        holder.tv_date.setText(arrayList.get(position).getTv_date());

    }

    @Override
    public int getItemCount( )
    {
        return arrayList.size(); //리스트 아이템의 전체 개수
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder
    { //view를 쥐고 있는 것
        ImageView iv_profile;
        TextView tv_product;
        TextView tv_price;
        TextView tv_date;

        public CustomViewHolder(@NonNull View itemView)
        {
            super(itemView);
            //itemView 는 하나하나의 뷰들을 의미함 (리사이클러뷰에 연동된 뷰들)
            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_product = itemView.findViewById(R.id.tv_product);
            this.tv_price = itemView.findViewById(R.id.tv_price);
            this.tv_date = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final int curPos = getAdapterPosition(); //현재 클릭한 아이템의 포지션(위치... 0부터 시작)

                    //수정 이전에
                    final Dialog dialogconfirm = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
                    dialogconfirm.setContentView(R.layout.dialog_confirm_password);

                    EditText et_pass = dialogconfirm.findViewById(R.id.et_pass); //게시글의 비밀번호 확인받을 입력필드

                    Button btn_confirm = dialogconfirm.findViewById(R.id.btn_confirm); // 확인버튼

                    btn_confirm.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if (et_pass.getText().length() == 0)
                            {
                                //현재 입력필드에 값을 안 적은 상황이라면
                                Toast.makeText(context, "비어있는 입력필드가 존재합니다.", Toast.LENGTH_SHORT).show();
                                return; // if 문을 벗어나 온 클릭 수행이 취소가 된다
                            }

                            //String 끼리 비교해줄때는 .equals 로 비교해준다
                            if (et_pass.getText().toString().equals(arrayList.get(curPos).getTv_password()))
                            {
                                //게시글  비밀번호 입력필드에 입력한 값이랑 내가 입력한 값이 일치하면 아래를 실행

                                //비밀번호 confirm 이후
                                final Dialog dialogMod = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
                                dialogMod.setContentView(R.layout.dialog_modify); // xml 레이아웃 연결

                                //adapter에서 dialog의 대한 findViewById 방법
                                EditText et_product = dialogMod.findViewById(R.id.et_product); //상품명
                                EditText et_price = dialogMod.findViewById(R.id.et_price); //가격
                                EditText et_date = dialogMod.findViewById(R.id.et_date); // 날짜
                                Button btn_modify = dialogMod.findViewById(R.id.btn_modify); //수정완료 버튼

                                btn_modify.setOnClickListener(new View.OnClickListener()
                                { //수정 버튼 로직
                                    @Override
                                    public void onClick(View v)
                                    {
                                        //입력을 안했을 때 수정되는 것을 막음
                                        if (et_product.getText().length() == 0 || et_price.getText().length() == 0 || et_date.getText().length() == 0)
                                        {
                                            Toast.makeText(context, "비어있는 입력필드가 존재합니다", Toast.LENGTH_SHORT).show();
                                            return; //if문을 벗어나 온 클릭 수행이 취소가 된다.
                                        }

                                        final SalesItem item = new SalesItem();
                                        item.setTv_product(et_product.getText().toString());
                                        item.setTv_price(et_price.getText().toString());
                                        item.setTv_date(et_date.getText().toString());
                                        item.setTv_password(arrayList.get(curPos).getTv_password());

                                        // 팝업 진입한 이후에 product 이름이 같은 리스트를 골라내야댐 tv_product 친구들을 쭈루룩 다 가져와서
                                        // ->tv_product를 다 가져온 후에 정렬 후에 (orderbyChild)
                                        // 그 중에서 내가 클릭한 놈이랑 동일한(이름이 같은) 것을 찾아냄
                                        // 검색 구문

                                        //같은 product 닉네임을 갖는 아이템을 서버에서 지정을 해줘야함(가리켜야함)
                                        //기준 tv_product
                                        databaseReference.orderByChild("tv_product").equalTo(arrayList.get(curPos).getTv_product()).addListenerForSingleValueEvent(new ValueEventListener()
                                        {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                            {
                                                // 파이어베이스 db의 데이터들을 가지고 오는 곳
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                                { //반복문을 통해 데이터 List를 추출해냄.
                                                    databaseReference.child(snapshot.getKey()).setValue(item); //키 값을 가져옴
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error)
                                            {

                                            }
                                        });

                                        arrayList.set(curPos, item); //리스트에 있는 데이터를 수정
                                        notifyItemChanged(curPos); //수정 완료 후 새로고침

                                        dialogMod.dismiss(); //다이얼로그 닫기
                                    }
                                });

                                Button btn_delete = dialogMod.findViewById(R.id.btn_delete); // 삭제 버튼
                                btn_delete.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {

                                        databaseReference.orderByChild("tv_product").equalTo(arrayList.get(curPos).getTv_product()).addListenerForSingleValueEvent(new ValueEventListener()
                                        {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                            {
                                                // 파이어베이스 db의 데이터들을 가지고 오는 곳
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                                { //반복문을 통해 데이터 List를 추출해냄.
                                                    databaseReference.child(snapshot.getKey()).setValue(null); //키 값을 가져옴
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error)
                                            {

                                            }
                                        });

                                        arrayList.remove(curPos);

                                        notifyItemRemoved(curPos);
                                        notifyItemRangeChanged(curPos, arrayList.size());

                                        dialogMod.dismiss();
                                    }
                                });

                                dialogMod.show(); // show를 해야 dialog가 작동함(활성화)
                            }
                            else
                            {
                                Toast.makeText(context, "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show();
                            }
                            dialogconfirm.dismiss(); // 다이얼로그 종료
                        }
                    });
                    dialogconfirm.show(); //다이얼로그 활성화
                }
            });
        }
    }
}

//이 어댑터를 프래그먼트에 붙혀줘야 함