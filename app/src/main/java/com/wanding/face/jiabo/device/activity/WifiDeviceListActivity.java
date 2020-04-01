package com.wanding.face.jiabo.device.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanding.face.BaseActivity;
import com.wanding.face.NitConfig;
import com.wanding.face.R;
import com.wanding.face.activity.CardVerificaActivity;
import com.wanding.face.httputil.HttpURLConnectionUtil;
import com.wanding.face.httputil.NetworkUtils;
import com.wanding.face.jiabo.device.bean.DeviceStateData;
import com.wanding.face.jiabo.device.RequestUtil;
import com.wanding.face.jiabo.device.adapter.DeviceListAdapter;
import com.wanding.face.jiabo.device.bean.QueryDevListResData;
import com.wanding.face.utils.FastJsonUtil;
import com.wanding.face.utils.ToastUtil;
import com.wanding.face.utils.Utils;
import com.wanding.face.view.XListView;

import org.json.JSONException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * wifi云打印机列表
 */
@ContentView(R.layout.activity_device_list)
public class WifiDeviceListActivity extends BaseActivity implements View.OnClickListener,
        XListView.IXListViewListener,
        AdapterView.OnItemClickListener {

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

    @ViewInject(R.id.device_list_listView)
    XListView xListView;


    private List<DeviceStateData> list = new ArrayList<>();
    private BaseAdapter mAdapter;

    private static final int REFRESH = 100;
    private static final int LOADMORE = 200;
    private static final int NOLOADMORE = 300;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.back_icon));
        tvTitle.setText(getString(R.string.printer_device_manage));
        imgTitleImg.setVisibility(View.GONE);
        tvOption.setVisibility(View.GONE);
        tvOption.setText("");

        initView();
        initListener();

        mAdapter = new DeviceListAdapter(activity,list);
        xListView.setAdapter(mAdapter);

        //查询云打印设备
        queryDeviceList();
    }



    /**
     * 初始化界面控件
     */
    private void initView(){
        /**
         * ListView初始化
         */
        xListView.setPullLoadEnable(false);//是否可以上拉加载更多,默认可以上拉
        xListView.setPullRefreshEnable(false);//是否可以下拉刷新,默认可以下拉

    }

    private void initListener(){
        //注册刷新和加载更多接口
        imgBack.setOnClickListener(this);
        xListView.setXListViewListener(this);
        xListView.setOnItemClickListener(this);
    }


    /**
     *  查询云打印设备
     */
    private void queryDeviceList(){


        final String url = NitConfig.queryDeviceListUrl;
        new Thread(){
            @Override
            public void run() {
                try {
                    String content = RequestUtil.queryDevListReq();
                    String content_type = HttpURLConnectionUtil.CONTENT_TYPE_STR;
                    Log.e("发起请求参数：", content);
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

    /**
     * 发送数据到云打印设备执行打印
     */
    private void sendPrintText(){
        final String url = NitConfig.wifiSendMsgUrl;
        new Thread(){
            @Override
            public void run() {
                try {
                    String content = RequestUtil.sendMsgReq();
                    String content_type = HttpURLConnectionUtil.CONTENT_TYPE_STR;
                    Log.e("发起请求参数：", content);
                    String jsonStr = HttpURLConnectionUtil.doPos(url,content,content_type);
                    Log.e("返回字符串结果：", jsonStr);
                    int msg = NetworkUtils.MSG_WHAT_TWO;
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
                    break;
                case NetworkUtils.MSG_WHAT_ONE:
                    String deviceListJson = (String) msg.obj;
                    deviceListJson(deviceListJson);
                    break;
                case NetworkUtils.MSG_WHAT_TWO:

                    break;
                case NetworkUtils.MSG_WHAT_THREE:
                    String saveTestJsonStr=(String) msg.obj;

                    break;
                case NetworkUtils.REQUEST_JSON_CODE:
                    errorJsonText = (String) msg.obj;
                    ToastUtil.showText(activity,errorJsonText,1);
                    hideWaitDialog();
                    break;
                case NetworkUtils.REQUEST_IO_CODE:
                    errorJsonText = (String) msg.obj;
                    ToastUtil.showText(activity,errorJsonText,1);
                    hideWaitDialog();
                    break;
                case NetworkUtils.REQUEST_CODE:
                    errorJsonText = (String) msg.obj;
                    ToastUtil.showText(activity,errorJsonText,1);
                    hideWaitDialog();
                    break;
                default:
                    break;
            }
        }
    };

    private void deviceListJson(String json){
        QueryDevListResData data = (QueryDevListResData) FastJsonUtil.jsonToObject(json,QueryDevListResData.class);
        int code = data.getCode();
        String msg = data.getMsg();
        if(code == 1){
            list.clear();
            List<DeviceStateData> deviceList = new ArrayList<>();
            deviceList = data.getStatusList();
            Log.e("设备数",deviceList.size()+"");
            list.addAll(deviceList);
            if(list.size()>0){
                Message msg1 = new Message();
                msg1.what = REFRESH;
                handler.sendEmptyMessageDelayed(REFRESH, 0);
            }else{
                Message msg1 = new Message();
                msg1.what = NOLOADMORE;
                handler.sendEmptyMessageDelayed(NOLOADMORE, 0);
            }
        }else {
            ToastUtil.showText(activity,msg,1);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(list.size()>0){
            DeviceStateData device = list.get(position - 1);
            if(device.getOnline() == 1){
                if(device.getStatus() == 1){
                    //发送数据打印
                    sendPrintText();

                }else if(device.getStatus() == 2){
                    ToastUtil.showText(activity,"打印机缺纸！",1);
                }else if(device.getStatus() == 2){
                    ToastUtil.showText(activity,"打印机缺纸！",1);
                }else{

                    ToastUtil.showText(activity,"打印机异常！",1);
                }

            }else{
                ToastUtil.showText(activity,"离线状态不可设置！",1);
            }
        }




    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    private void onLoad() {
        xListView.stopRefresh();
        xListView.stopLoadMore();
        xListView.setRefreshTime(new Date().toLocaleString());
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL){
            /*0键*/
            Log.e(TAG,"按下键盘回退/删除键");
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();;
        switch (v.getId()){
            case R.id.menu_title_imageView:
                finish();
                break;
            default:
                break;
        }
    }
}
