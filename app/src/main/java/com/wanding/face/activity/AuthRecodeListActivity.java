package com.wanding.face.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.wxpayface.IWxPayfaceCallback;
import com.tencent.wxpayface.WxPayFace;
import com.tencent.wxpayface.WxfacePayCommonCode;
import com.wanding.face.BaseActivity;
import com.wanding.face.Constants;
import com.wanding.face.MainActivity;
import com.wanding.face.NitConfig;
import com.wanding.face.R;
import com.wanding.face.adapter.AuthOrderListAdapter;
import com.wanding.face.baidu.tts.MySyntherizer;
import com.wanding.face.bean.AuthRecodeListReqData;
import com.wanding.face.bean.AuthRecodeListResData;
import com.wanding.face.bean.UserBean;
import com.wanding.face.bean.WdPreAuthHistoryVO;
import com.wanding.face.httputil.HttpURLConnectionUtil;
import com.wanding.face.httputil.NetworkUtils;
import com.wanding.face.payutil.QueryParamsReqUtil;
import com.wanding.face.utils.FastJsonUtil;
import com.wanding.face.utils.GsonUtils;
import com.wanding.face.utils.ToastUtil;
import com.wanding.face.utils.Utils;
import com.wanding.face.view.ClearEditText;
import com.wanding.face.view.XListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@ContentView(R.layout.auth_recode_list)
public class AuthRecodeListActivity extends BaseActivity implements View.OnClickListener,XListView.IXListViewListener,AdapterView.OnItemClickListener {


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

    @ViewInject(R.id.recode_list_etSearch)
    ClearEditText etSearch;
    @ViewInject(R.id.recode_list_imgScan)
    ImageView imgScan;
    @ViewInject(R.id.recode_list_imgSearch)
    ImageView imgSearch;
    @ViewInject(R.id.auth_recode_list_xListView)
    private XListView xListView;


    private int pageNum = 1;//默认加载第一页
    private static final int pageNumCount = 20;//默认一页加载xx条数据（死值不变）
    //总条数
    private int orderListTotalCount = 0;
    //每次上拉获取的条数
    private int getMoerNum = 0;
    private static final int REFRESH = 100;
    private static final int LOADMORE = 200;
    private static final int NOLOADMORE = 300;
    private int refreshCount = 1;
    private String loadMore = "0";//loadMore为1表示刷新操作  2为加载更多操作

    private UserBean userBean;


    String etSearchStr = "";
    private List<WdPreAuthHistoryVO> list = new ArrayList<WdPreAuthHistoryVO>();
    private BaseAdapter mAdapter;
    /**
     * 百度语音合成
     */
    MySyntherizer synthesizer = MainActivity.synthesizer;
    Dialog scanQrcodeDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.back_icon));
        tvTitle.setText("预授权交易记录");
        imgTitleImg.setVisibility(View.GONE);
        tvOption.setVisibility(View.GONE);
        tvOption.setText("");

        Intent intent = getIntent();
        userBean = (UserBean) intent.getSerializableExtra("userBean");

        initView();
        initListener();
        mAdapter = new AuthOrderListAdapter(activity,list);
        xListView.setAdapter(mAdapter);

        startCodeScanner();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG,"onStart...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list = null;
    }

    private void initView(){



        xListView.setPullLoadEnable(true);//是否可以上拉加载更多,默认可以上拉
        xListView.setPullRefreshEnable(true);//是否可以下拉刷新,默认可以下拉

    }

    private void initListener(){
        imgBack.setOnClickListener(this);
        imgScan.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        //注册刷新和加载更多接口
        xListView.setXListViewListener(this);
        xListView.setOnItemClickListener(this);
    }
    /**
     * speak 实际上是调用 synthesize后，获取音频流，然后播放。
     * 获取音频流的方式见SaveFileActivity及FileSaveListener
     * 需要合成的文本text的长度不能超过1024个GBK字节。
     */
    private void speak(String text) {

        int result = synthesizer.speak(text);
        checkResult(result, "speak");
    }

    /**
     * 暂停播放
     */
    private void stop() {

        int result = synthesizer.stop();
        checkResult(result, "speak");
    }

    private void checkResult(int result, String method) {
        if (result != 0) {
//            toPrint("error code :" + result + " method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
            Log.e("error code :", result+" method:" + method );
        }
    }

    /**
     * 开启扫码
     */
    private void startCodeScanner(){
        showScanQrcodeDialog();
        WxPayFace.getInstance().startCodeScanner(new IWxPayfaceCallback() {
            @Override
            public void response(Map info) throws RemoteException {
                if (info == null) {
                    showToast("扫码失败,扫码返回为空！");
                    finish();
                    return;
                }
                String code = (String)info.get(Constants.RETURN_CODE);
                String msg = (String)info.get(Constants.RETURN_MSG);
                Log.e(TAG, "response | getWxpayfaceRawdata " + code + " | " + msg);
                if (code == null || !code.equals(WxfacePayCommonCode.VAL_RSP_PARAMS_SUCCESS)) {
                    showToast("扫码失败！");
                    finish();
                    return;
                }
                //{return_code=SUCCESS, code_msg=134665614656016576, return_msg=scan code success}
                Log.e("获取扫码返回:",info.toString());
                String code_msg = info.get("code_msg").toString();
                if(Utils.isNotEmpty(code_msg)){

                    int what = NetworkUtils.MSG_WHAT_ONEHUNDRED;
                    String text = code_msg;
                    sendMessage(what,text);

                }else{
                    ToastUtil.showText(activity,"扫码返回结果为空！",1);
                }
                if(scanQrcodeDialog!=null&&scanQrcodeDialog.isShowing()){
                    scanQrcodeDialog.dismiss();
                }

            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {



            }
        });
    }

    /**
     * 停止扫码
     */
    private void stopScanner(){
        try{
            WxPayFace.getInstance().stopCodeScanner();
        }catch(Exception e){
            e.printStackTrace();
            showToast("停止扫码SDK调用失败！！");
        }
    }

    /**
     * 扫码提示对话框
     */
    private void showScanQrcodeDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.scan_qrcode_hint_dialog, null);
        ImageView imgClose = view.findViewById(R.id.scan_qrcode_hint_dialog_imgClose);
        TextView tvScanHintTitle = view.findViewById(R.id.scan_qrcode_hint_dialog_tvScanHintTitle);
        tvScanHintTitle.setText("扫二维码查单");
        scanQrcodeDialog = new Dialog(this,R.style.dialog);
        Window dialogWindow = scanQrcodeDialog.getWindow();
        WindowManager.LayoutParams params = scanQrcodeDialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setAttributes(params);
        scanQrcodeDialog.setContentView(view);
        //点击屏幕和物理返回键dialog不消失
        scanQrcodeDialog.setCancelable(false);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scanQrcodeDialog!=null&&scanQrcodeDialog.isShowing()){
                    scanQrcodeDialog.dismiss();
                }
                stopScanner();
            }
        });
        scanQrcodeDialog.show();
    }


    private void getRecodeList(final int pageNum,final int pageNumCount){

        if(refreshCount == 1){
            showCustomDialog();
        }

        final String startTimeStr = "";
        final String endTimeStr = "";
        //参数实体
        final AuthRecodeListReqData reqData = QueryParamsReqUtil.queryAuthRecodeListReq(pageNum,pageNumCount,userBean,etSearchStr,startTimeStr,endTimeStr);

        final String url = NitConfig.authRecodeListUrl;
        Log.e("请求地址：", url);
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

            };
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
            switch (msg.what) {
                case NetworkUtils.MSG_WHAT_ONEHUNDRED:
                    speak("扫码成功！");
                    if(scanQrcodeDialog!=null&&scanQrcodeDialog.isShowing()){
                        scanQrcodeDialog.dismiss();
                    }
                    stopScanner();
                    String code_msg = (String) msg.obj;
                    etSearch.setText(code_msg);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    etSearchStr = etSearch.getText().toString().trim();
                    refreshCount =1;
                    loadMore = "0";
                    pageNum = 1;
                    getRecodeList(pageNum,pageNumCount);
                    break;
                case REFRESH:
                    mAdapter.notifyDataSetChanged();
                    //更新完毕
                    onLoad();
                    break;
                case LOADMORE:
                    xListView.setPullLoadEnable(true);//是否可以上拉加载更多
                    mAdapter.notifyDataSetChanged();
                    // 加载更多完成
                    onLoad();
                    break;
                case NOLOADMORE:
                    xListView.setPullLoadEnable(false);//是否可以上拉加载更多
                    mAdapter.notifyDataSetChanged();
                    // 加载更多完成-->>已没有更多
                    onLoad();
                    if(list.size() == 1){
                        WdPreAuthHistoryVO authOrder = list.get(0);
                        Intent intent = new Intent();
                        intent.setClass(activity,AuthOrderDetailsActivity.class);
                        intent.putExtra("userBean",userBean);
                        intent.putExtra("authOrder",authOrder);
                        startActivity(intent);
                    }
                    break;
                case NetworkUtils.MSG_WHAT_ONE:
                    String recodeListJsonStr = (String) msg.obj;
                    recodeListJsonStr(recodeListJsonStr);
                    hideCustomDialog();
                    loadMore = "0";
                    break;
                case NetworkUtils.REQUEST_JSON_CODE:
                    errorJsonText = (String) msg.obj;
                    ToastUtil.showText(activity,errorJsonText,1);
                    hideCustomDialog();
                    loadMore = "0";
                    break;
                case NetworkUtils.REQUEST_IO_CODE:
                    errorJsonText = (String) msg.obj;
                    ToastUtil.showText(activity,errorJsonText,1);
                    hideCustomDialog();
                    loadMore = "0";
                    break;
                case NetworkUtils.REQUEST_CODE:
                    errorJsonText = (String) msg.obj;
                    ToastUtil.showText(activity,errorJsonText,1);
                    hideCustomDialog();
                    loadMore = "0";
                    break;
                default:
                    break;
            }
        };
    };

    private void recodeListJsonStr(String jsonStr){
        try {
            JSONObject job = new JSONObject(jsonStr);
            String status = job.getString("status");
            String message = job.getString("message");
            if("200".equals(status)){
                String dataJson = job.getString("data");

                Gson gjson  =  GsonUtils.getGson();
                java.lang.reflect.Type type = new TypeToken<AuthRecodeListResData>() {}.getType();
                AuthRecodeListResData recodeResData = gjson.fromJson(dataJson, type);
                //获取总条数
                orderListTotalCount = recodeResData.getTotalCount();
                Log.e("总条数：", orderListTotalCount+"");
                List<WdPreAuthHistoryVO> recodeList = new ArrayList<WdPreAuthHistoryVO>();
                //获取的list
                recodeList = recodeResData.getOrderList();
                getMoerNum = recodeList.size();
                if(pageNum == 1){
                    list.clear();
                    mAdapter.notifyDataSetInvalidated();
                }
                list.addAll(recodeList);
                Log.e("查询数据：", list.size()+""+"条");
                //关闭上拉或下拉View，刷新Adapter
                if("0".equals(loadMore)){
                    if(list.size()<=orderListTotalCount&&getMoerNum==pageNumCount){
                        Message msg1 = new Message();
                        msg1.what = LOADMORE;
                        handler.sendEmptyMessageDelayed(LOADMORE, 0);
                    }else{
                        Message msg1 = new Message();
                        msg1.what = NOLOADMORE;
                        handler.sendEmptyMessageDelayed(NOLOADMORE, 0);
                    }
                }else if("1".equals(loadMore)){
                    if(list.size()<=orderListTotalCount&&getMoerNum==pageNumCount){
                        Message msg1 = new Message();
                        msg1.what = LOADMORE;
                        handler.sendEmptyMessageDelayed(LOADMORE, 0);
                    }else{
                        Message msg1 = new Message();
                        msg1.what = NOLOADMORE;
                        handler.sendEmptyMessageDelayed(NOLOADMORE, 0);
                    }

                }else if("2".equals(loadMore)){
                    Message msg1 = new Message();
                    msg1.what = LOADMORE;
                    handler.sendEmptyMessageDelayed(LOADMORE, 0);
                }


            }else{
                if(Utils.isNotEmpty(message)){
                    Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(activity, "查询失败！", Toast.LENGTH_LONG).show();
                }
                Message msg1 = new Message();
                msg1.what = NOLOADMORE;
                handler.sendEmptyMessageDelayed(NOLOADMORE, 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.menu_title_imageView:
                finish();
                break;
            case R.id.recode_list_imgScan:
                if(Utils.isFastClick()){
                    return;
                }
                startCodeScanner();
                break;
            case R.id.recode_list_imgSearch:
                if(Utils.isFastClick()){
                    return;
                }
                etSearchStr = etSearch.getText().toString().trim();
                refreshCount =1;
                loadMore = "0";
                pageNum = 1;
                getRecodeList(pageNum,pageNumCount);
                break;
            default:
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(Utils.isFastClick()){
            return;
        }
        try{
            if(list.size()>=1){
                WdPreAuthHistoryVO authOrder = list.get(position - 1);
                Intent intent = new Intent();
                intent.setClass(activity,AuthOrderDetailsActivity.class);
                intent.putExtra("userBean",userBean);
                intent.putExtra("authOrder",authOrder);
                startActivity(intent);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        refreshCount++;
        loadMore = "1";
        pageNum = 1;
        getRecodeList(pageNum,pageNumCount);

    }

    @Override
    public void onLoadMore() {
        refreshCount++;
        loadMore = "2";
        if(list.size()<=orderListTotalCount&&getMoerNum==pageNumCount){
            //已取出数据条数<=服务器端总条数&&上一次上拉取出的条数 == 规定的每页取出条数时代表还有数据库还有数据没取完
            pageNum = pageNum + 1;
            getRecodeList(pageNum,pageNumCount);
        }else{
            //没有数据执行两秒关闭view
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = NOLOADMORE;
                    handler.sendMessage(msg);
                }
            }, 0);

        }
    }

    private void onLoad() {
        xListView.stopRefresh();
        xListView.stopLoadMore();
        xListView.setRefreshTime(new Date().toLocaleString());
    }
}
