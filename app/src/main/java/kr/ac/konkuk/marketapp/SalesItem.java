package kr.ac.konkuk.marketapp;

public class SalesItem
{
    //아이템들의 대한 데이터 모델 (어댑터를 만들어서 이 모델과 연동을 해야 함)
    private String tv_product; //판매 상품명
    private String tv_price; //판매 가격
    private String tv_date; //판매 일자


    private String tv_password;// 게시글의 비밀번호, 이걸 쥐고 있어야만 무분별하게 수정, 삭제가 일어나지 않게끔, 추가를 한 자기자신만 아는 번호

    //alt+insert

    //클래스가 생성될 때 실행할 것을 안에 구현해줌
    public SalesItem( )
    {
    }

    public String getTv_date( )
    {
        return tv_date;
    }

    public void setTv_date(String tv_date)
    {
        this.tv_date = tv_date;
    }

    public String getTv_product( )
    {
        return tv_product;
    }

    public void setTv_product(String tv_product)
    {
        this.tv_product = tv_product;
    }

    public String getTv_price( )
    {
        return tv_price;
    }

    public void setTv_price(String tv_price)
    {
        this.tv_price = tv_price;
    }

    public String getTv_password( )
    {
        return tv_password;
    }

    public void setTv_password(String tv_password)
    {
        this.tv_password = tv_password;
    }
}
