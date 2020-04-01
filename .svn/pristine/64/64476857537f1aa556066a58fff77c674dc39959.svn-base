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

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 *  功能菜单管理界面
 */
@ContentView(R.layout.activity_auth_manage)
public class AuthManageActivity extends BaseActivity implements View.OnClickListener {



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


    @ViewInject(R.id.auth_maname_authCancelLayout)
    RelativeLayout authCancelLayout;
    @ViewInject(R.id.auth_maname_authConfirmLayout)
    RelativeLayout authConfirmLayout;
    @ViewInject(R.id.auth_maname_authConfirmCancelLayout)
    RelativeLayout authConfirmCancelLayout;

    @ViewInject(R.id.auth_maname_authRecodeLayout)
    RelativeLayout authRecodeLayout;



    /**
     * 登录用户
     */
    private UserBean userBean;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.back_icon));
        tvTitle.setText(getString(R.string.setting_auth_manage));
        imgTitleImg.setVisibility(View.GONE);
        tvOption.setVisibility(View.GONE);
        tvOption.setText("");
        Intent intent = getIntent();
        userBean = (UserBean) intent.getSerializableExtra("userBean");
        initListener();



    }







    private void initListener(){
        imgBack.setOnClickListener(this);
        authCancelLayout.setOnClickListener(this);
        authConfirmLayout.setOnClickListener(this);
        authConfirmCancelLayout.setOnClickListener(this);
        authRecodeLayout.setOnClickListener(this);

    }









    @Override
    public void onClick(View v) {
        Intent intent = new Intent();;
        switch (v.getId()){
            case R.id.menu_title_imageView:
                finish();
                break;
            case R.id.auth_maname_authCancelLayout:
                ToastUtil.showText(activity,"暂未开放！",1);
                break;
            case R.id.auth_maname_authConfirmLayout:
//                intent.setClass(activity,AuthConfirmActivity.class);
//                intent.putExtra("userBean",userBean);
//                startActivity(intent);
                ToastUtil.showText(activity,"暂未开放！",1);

                break;
            case R.id.auth_maname_authConfirmCancelLayout:

                ToastUtil.showText(activity,"暂未开放！",1);

                break;
            case R.id.auth_maname_authRecodeLayout:

                intent.setClass(activity,AuthRecodeListActivity.class);
                intent.putExtra("userBean",userBean);
                startActivity(intent);

                break;
                default:
                    break;
        }
    }


}
