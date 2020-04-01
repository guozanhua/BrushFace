package com.wanding.face.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanding.face.BaseActivity;
import com.wanding.face.NitConfig;
import com.wanding.face.R;
import com.wanding.face.bean.CheckPasswdReqData;
import com.wanding.face.bean.UserBean;
import com.wanding.face.httputil.HttpURLConnectionUtil;
import com.wanding.face.httputil.NetworkUtils;
import com.wanding.face.payutil.QueryParamsReqUtil;
import com.wanding.face.utils.FastJsonUtil;
import com.wanding.face.utils.ToastUtil;
import com.wanding.face.utils.Utils;
import com.wanding.face.view.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;

/**
 * Time: 2019/11/27
 * Author:Administrator
 * Description: 输入操作员密码
 */
@ContentView(R.layout.activity_modify_passwd)
public class ModifyPasswdActivity extends BaseActivity implements View.OnClickListener {

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


    @ViewInject(R.id.modify_passwd_etPasswd)
    ClearEditText etPasswd;

    @ViewInject(R.id.modify_passwd_btOk)
    Button btOk;


    private UserBean userBean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.back_icon));
        tvTitle.setText("修改管理员密码");
        imgTitleImg.setVisibility(View.GONE);
        tvOption.setVisibility(View.GONE);
        tvOption.setText("");

        Intent intent = getIntent();
        userBean = (UserBean) intent.getSerializableExtra("userBean");

        initListener();
    }

    private void initListener(){
        imgBack.setOnClickListener(this);
        btOk.setOnClickListener(this);
    }

    /**
     * 修改密码
     */
    private void modifyPasswd(String passwd){
        showCustomDialog();
        final String url = NitConfig.modifyPasswdUrl;
        Log.e(TAG,"请求地址："+url);
        final CheckPasswdReqData reqData = QueryParamsReqUtil.checkPasswdReq(passwd);
        new Thread(){
            @Override
            public void run() {
                try {
                    String content = FastJsonUtil.toJSONString(reqData);
                    Log.e("发起请求参数：", content);
                    String content_type = HttpURLConnectionUtil.CONTENT_TYPE_JSON;
                    String jsonStr = HttpURLConnectionUtil.doPos(url,content,content_type);
                    Log.e("返回字符串结果：", jsonStr);
                    int msg = NetworkUtils.MSG_WHAT_ONE;
                    String text = jsonStr;
                    sendMessage(msg,text);

                } catch (JSONException e) {
                    e.printStackTrace();
                    sendMessage(NetworkUtils.REQUEST_JSON_CODE,NetworkUtils.REQUEST_JSON_TEXT);
                }catch (IOException e){
                    e.printStackTrace();
                    sendMessage(NetworkUtils.REQUEST_IO_CODE,NetworkUtils.REQUEST_IO_TEXT);
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessage(NetworkUtils.REQUEST_CODE,NetworkUtils.REQUEST_TEXT);
                }
            }
        }.start();
    }


    private void sendMessage(int what,String text){
        Message msg = new Message();
        msg.what = what;
        msg.obj = text;
        handler.sendMessage(msg);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String errorJsonText = "";
            switch (msg.what){
                case NetworkUtils.MSG_WHAT_ONE:
                    String modifyPasswdJson=(String) msg.obj;
                    modifyPasswdJson(modifyPasswdJson);
                    hideCustomDialog();
                    break;
                case NetworkUtils.REQUEST_JSON_CODE:
                    errorJsonText = (String) msg.obj;
                    ToastUtil.showText(activity,errorJsonText,1);
                    hideCustomDialog();
                    break;
                case NetworkUtils.REQUEST_IO_CODE:
                    errorJsonText = (String) msg.obj;
                    ToastUtil.showText(activity,errorJsonText,1);
                    hideCustomDialog();
                    break;
                case NetworkUtils.REQUEST_CODE:
                    errorJsonText = (String) msg.obj;
                    ToastUtil.showText(activity,errorJsonText,1);
                    hideCustomDialog();
                    break;
                default:
                    break;
            }
        }
    };

    private void modifyPasswdJson(String json){
        try {
            JSONObject job = new JSONObject(json);
            String status = job.getString("status");
            String message = job.getString("message");
            if(status.equals("200")){
                if(Utils.isNotEmpty(message)){
                    ToastUtil.showText(activity,message,1);
                }else{
                    ToastUtil.showText(activity,"密码修改正确",1);
                }
            }else{
                if(Utils.isNotEmpty(message)){
                    ToastUtil.showText(activity,message,1);
                }else{
                    ToastUtil.showText(activity,"密码修改错误",1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtil.showText(activity,"密码修改接口返回错误",1);

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showText(activity,"密码修改接口返回错误",1);
        }

        finish();
    }


    /**
     * 是否确认修改密码
     */
    private void showIsModifyPasswdDialog(final String etPasswdStr){
        View view = LayoutInflater.from(this).inflate(R.layout.action_prompt_dialog, null);
        //描述信息
        TextView tvMsg = view.findViewById(R.id.action_prompt_tvMsg);
        tvMsg.setText("密码修改成功后，将使用新密码执行所有敏感操作，是否确认修改？");
        //操作按钮
        final Button btCancel = (Button) view.findViewById(R.id.action_prompt_btCancel);
        final Button btSuccess = (Button) view.findViewById(R.id.action_prompt_btSuccess);

        final Dialog mDialog = new Dialog(this,R.style.dialog);
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setAttributes(params);
        mDialog.setContentView(view);
        btCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();

            }
        });
        btSuccess.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(Utils.isFastClick()){
                    return;
                }
                mDialog.dismiss();
                //修改密码
                modifyPasswd(etPasswdStr);

            }
        });
        //点击屏幕和物理返回键dialog不消失
        mDialog.setCancelable(false);
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_title_imageView:
                finish();
                break;
            case R.id.modify_passwd_btOk:
                if(Utils.isFastClick()){
                    return;
                }
                String etPasswdStr = etPasswd.getText().toString().trim();
                if(Utils.isEmpty(etPasswdStr)){
                    ToastUtil.showText(activity,"请输入操作员新密码！",1);
                    return;
                }
                showIsModifyPasswdDialog(etPasswdStr);

                break;
                default:
                    break;
        }
    }
}
