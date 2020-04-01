package com.wanding.face;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.wanding.face.utils.SharedPreferencesUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Time: 2019/10/21
 * Author:Administrator
 * Description: 退款
 */
@ContentView(R.layout.activity_setting_parameter)
public class ParameterSettingActivity extends BaseActivity implements View.OnClickListener {

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

    @ViewInject(R.id.select_face_code_type_radioGroup)
    private RadioGroup rgFaceCodeType;
    @ViewInject(R.id.select_face_code_type_rbFaceCode)
    private RadioButton rbFaceCode;
    @ViewInject(R.id.select_face_code_type_rbPaymentCode)
    private RadioButton rbPaymentCode;


    /**
     * 刷脸参数face_code_type选择值   默认1
     */
    private boolean isFaceCode;
    private SharedPreferencesUtil sharedPreferencesUtil;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.back_icon));
        tvTitle.setText("参数设置");
        imgTitleImg.setVisibility(View.GONE);
        tvOption.setVisibility(View.GONE);
        tvOption.setText("");
        initView();
        initListener();
    }

    /**
     * 初始化界面控件
     */
    private void initListener(){

        imgBack.setOnClickListener(this);
        rgFaceCodeType.setOnCheckedChangeListener(selectServerListener);

    }

    private void initView(){
        sharedPreferencesUtil = new SharedPreferencesUtil(activity,SharedPreConstants.FACE_CODE_TYPE_FILE_NAME);
        if(sharedPreferencesUtil.contain(SharedPreConstants.FACE_CODE_TYPE_KEY_NAME)){
            isFaceCode = (boolean) sharedPreferencesUtil.getSharedPreference(SharedPreConstants.FACE_CODE_TYPE_KEY_NAME,false);
            if(isFaceCode){
                rbFaceCode.setChecked(true);
                rbPaymentCode.setChecked(false);
            }else{
                rbFaceCode.setChecked(false);
                rbPaymentCode.setChecked(true);
            }
        }else{
            //默认选择生产
            rbFaceCode.setChecked(false);
            rbPaymentCode.setChecked(true);
        }


    }

    private RadioGroup.OnCheckedChangeListener selectServerListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(checkedId == R.id.select_face_code_type_rbFaceCode){
                isFaceCode = true;
            }else{
                isFaceCode = false;
            }
            sharedPreferencesUtil.put(SharedPreConstants.FACE_CODE_TYPE_KEY_NAME,isFaceCode);
        }
    };

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.menu_title_imageView:
                finish();
                break;

            default:
                break;

        }
    }
}
