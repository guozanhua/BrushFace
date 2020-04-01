package com.wanding.face.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.wanding.face.BaseActivity;
import com.wanding.face.R;
import com.wanding.face.SharedPreConstants;
import com.wanding.face.utils.SharedPreferencesUtil;
import com.wanding.face.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Time: 2019/12/11
 * Author:Administrator
 * Description: 权限设置
 */
@ContentView(R.layout.activity_permissions_setting)
public class PermissionsSettingActivity extends BaseActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{

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

    @ViewInject(R.id.refund_passwd_switch)
    Switch refundPasswdSwitch;
    @ViewInject(R.id.auth_passwd_switch)
    Switch authPasswdSwitch;

    private SharedPreferencesUtil sharedPreferencesUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.back_icon));
        tvTitle.setText(getString(R.string.setting_permissions));
        imgTitleImg.setVisibility(View.GONE);
        tvOption.setVisibility(View.GONE);
        tvOption.setText("");

        initView();
        initListener();

    }

    private void initListener(){
        imgBack.setOnClickListener(this);
        refundPasswdSwitch.setOnCheckedChangeListener(this);
        authPasswdSwitch.setOnCheckedChangeListener(this);
    }

    private void initView(){
        sharedPreferencesUtil = new SharedPreferencesUtil(activity,SharedPreConstants.PERMISSIONS_FILE_NAME);
        if(sharedPreferencesUtil.contain(SharedPreConstants.PERMISSIONS_REFUND_KEY_NAME)){
            boolean refundPermissions = (boolean) sharedPreferencesUtil.getSharedPreference(SharedPreConstants.PERMISSIONS_REFUND_KEY_NAME,false);
            if(refundPermissions){
                refundPasswdSwitch.setChecked(true);
            }else{

                refundPasswdSwitch.setChecked(false);
            }
        }else{
            //默认开启
            refundPasswdSwitch.setChecked(true);
        }

        if(sharedPreferencesUtil.contain(SharedPreConstants.PERMISSIONS_AUTH_KEY_NAME)){
            boolean authPermissions = (boolean) sharedPreferencesUtil.getSharedPreference(SharedPreConstants.PERMISSIONS_AUTH_KEY_NAME,false);
            if(authPermissions){
                authPasswdSwitch.setChecked(true);
            }else{

                authPasswdSwitch.setChecked(false);
            }
        }else{

            //默认开启
            authPasswdSwitch.setChecked(true);
        }

    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.refund_passwd_switch:
                if(isChecked){
                    Log.e(TAG,"退款管理员密码打开！");
                }else{
                    Log.e(TAG,"退款管理员密码关闭！");
                }
                sharedPreferencesUtil.put(SharedPreConstants.PERMISSIONS_REFUND_KEY_NAME,isChecked);
                break;
            case R.id.auth_passwd_switch:
                if(isChecked){
                    Log.e(TAG,"预授权管理员密码打开！");
                }else{
                    Log.e(TAG,"预授权管理员密码关闭！");
                }
                sharedPreferencesUtil.put(SharedPreConstants.PERMISSIONS_AUTH_KEY_NAME,isChecked);
                break;
                default:
                    break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_title_imageView:
                finish();
                break;
            default:
                break;
        }
    }
}
