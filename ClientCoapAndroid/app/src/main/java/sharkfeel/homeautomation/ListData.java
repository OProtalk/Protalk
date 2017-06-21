package sharkfeel.homeautomation;

import android.graphics.drawable.Drawable;

/**
 * Created by Jimyeol on 2017-06-14.
 */

public class ListData {
    /**
     * 리스트 정보를 담고 있을 객체 생성
     */

    public Drawable mIcon;
    public String mTitle;
    public String mServerIP;
    public String mSensorURL;
    public String mData;
    public String mClass; //클래스가 Switch면 스위치 출력 Text면 텍스트출력
    public Boolean mCheck = false;

    //

    public boolean bOnCheckedChangeListener;
    public boolean bCoapGetTask;

    void setCheck(Boolean check){
        this.mCheck = check;
    }
    Boolean getCheck(){
        return mCheck;
    }
}
