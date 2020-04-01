package com.wanding.face.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wanding.face.BaseActivity;
import com.wanding.face.R;
import com.wanding.face.bean.UserBean;
import com.wanding.face.utils.ToastUtil;
import com.wanding.face.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 *  会员功能菜单管理界面
 */
@ContentView(R.layout.activity_member_manage)
public class MemberManageActivity extends BaseActivity implements View.OnClickListener {



    @ViewInject(R.id.menu_title_imageView)
    ImageView imgBack;
    @ViewInject(R.id.menu_title_layout)
    LinearLayout titleLayout;
    @ViewInject(R.id.menu_title_tvTitle)
    TextView tvTitle;
    @ViewInject(R.id.menu_title_imgTitleImg)
    ImageView imgTitleImg;
    @ViewInject(R.id.menu_title_tvOption)
    TextView tvOption;





    @ViewInject(R.id.member_manage_cardVerificaLayout)
    RelativeLayout cardVerificaLayout;
    @ViewInject(R.id.member_manage_memberTopUpLayout)
    RelativeLayout memberTopUpLayout;


    private UserBean userBean;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.back_icon));
        tvTitle.setText(getString(R.string.member_manage));
        imgTitleImg.setVisibility(View.GONE);
        tvOption.setVisibility(View.GONE);
        tvOption.setText("");

        Intent intent = getIntent();
        userBean = (UserBean) intent.getSerializableExtra("userBean");

        initListener();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initListener(){
        imgBack.setOnClickListener(this);
        cardVerificaLayout.setOnClickListener(this);
        memberTopUpLayout.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        Intent intent = new Intent();;
        switch (v.getId()){
            case R.id.menu_title_imageView:
                finish();
                break;
            case R.id.member_manage_cardVerificaLayout:
                if(Utils.isFastClick()){
                    return;
                }
                intent.setClass(activity,CardVerificaActivity.class);
                intent.putExtra("userBean",userBean);
                startActivity(intent);

                break;
            case R.id.member_manage_memberTopUpLayout:
                if(Utils.isFastClick()){
                    return;
                }
                ToastUtil.showText(activity,"暂未开放！",1);
                break;
                default:
                    break;
        }
    }

   /* @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL){
            *//*0键*//*
            Log.e(TAG,"按下键盘回退/删除键");
            finish();
        }
        return true;
    }*/
}
