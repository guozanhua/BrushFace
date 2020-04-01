package com.wanding.face.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.wanding.face.BaseActivity;
import com.wanding.face.NitConfig;
import com.wanding.face.R;
import com.wanding.face.adapter.OrderListAdapter;
import com.wanding.face.bean.OrderDetailData;
import com.wanding.face.bean.OrderListReqData;
import com.wanding.face.bean.OrderListResData;
import com.wanding.face.bean.UserBean;
import com.wanding.face.httputil.HttpURLConnectionUtil;
import com.wanding.face.httputil.NetworkUtils;
import com.wanding.face.payutil.PayParamsReqUtil;
import com.wanding.face.utils.FastJsonUtil;
import com.wanding.face.utils.GsonUtils;
import com.wanding.face.utils.ToastUtil;
import com.wanding.face.view.OrderListDateSelectPopupWindow;
import com.wanding.face.view.XListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 交易明细Activity
 */
@ContentView(R.layout.activity_order_list)
public class OrderListActivity extends BaseActivity implements View.OnClickListener,XListView.IXListViewListener,
                                                    AdapterView.OnItemClickListener,OrderListDateSelectPopupWindow.OnSelectClickListener {

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
    @ViewInject(R.id.order_list_mXListView)
    XListView mListView;


    /**
     * 下拉刷新，上拉加载参数初始化
     */
    private int refreshCount = 1;
    private int pageNum = 1;//默认加载第一页
    private static final int pageNumCount = 20;//默认一页加载xx条数据（死值不变）
    //总条数
    private int orderListTotalCount = 0;
    //每次上拉获取的条数
    private int getMoerNum = 0;
    private static final int REFRESH = 100;
    private static final int LOADMORE = 200;
    private static final int NOLOADMORE = 300;
    private String loadMore = "0";//loadMore为1表示刷新操作  2为加载更多操作，

    //交易列表
    private List<OrderDetailData> list = new ArrayList<OrderDetailData>();
    private OrderListAdapter mAdapter;

    /**
     *（"1"=当日交易）（"2"=本月交易不含今天）（"3" = 昨日交易）
     */
    private String date_typeStr = "1";

    private OrderListDateSelectPopupWindow popupWindow;
    private float alpha = 1;
    private static final int WINDOW_BG_ALPHA = 0x1001;

    private UserBean userBean;
    /**
     * isHistory N：实时订单，Y：历史订单
     */
    private String isHistory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.back_icon));
        tvTitle.setText(getString(R.string.order_list_pop_menu3));
        imgTitleImg.setVisibility(View.VISIBLE);
        tvOption.setVisibility(View.GONE);
        tvOption.setText("");


        Intent intent = getIntent();
        userBean = (UserBean) intent.getSerializableExtra("userBean");


        initView();

        initListener();

        mAdapter = new OrderListAdapter(this,list);
        mListView.setAdapter(mAdapter);

        getOrderList(pageNum,pageNumCount);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list = null;
    }

    private void initView(){

        /**
         * ListView初始化
         */
        mListView.setPullLoadEnable(true);//是否可以上拉加载更多,默认可以上拉
        mListView.setPullRefreshEnable(true);//是否可以下拉刷新,默认可以下拉


    }

    private void initListener(){
        imgBack.setOnClickListener(this);
        titleLayout.setOnClickListener(this);
        tvOption.setOnClickListener(this);
        //注册刷新和加载更多接口
        mListView.setXListViewListener(this);
        mListView.setOnItemClickListener(this);
    }

    /**
     * 获取交易明细
     * 入参：mid，eid，date_type（"1"=当日交易）（"2"=本月交易不含今天）
     **/
    private void getOrderList(final int pageNum,final int pageCount){
        if(refreshCount == 1){

            showCustomDialog();
        }
        final String url;
        if("1".equals(date_typeStr) || "3".equals(date_typeStr)){
            url = NitConfig.queryOrderDayListUrl;
        }else{
            url = NitConfig.queryOrderMonListUrl;
        }
        final OrderListReqData reqData = PayParamsReqUtil.getOrderListReqData(userBean,pageNum,pageCount,date_typeStr);
        new Thread(){
            @Override
            public void run() {
                try {
                    String content = FastJsonUtil.toJSONString(reqData);
                    Log.e("交易明细发起请求参数：", content);
                    String content_type = HttpURLConnectionUtil.CONTENT_TYPE_JSON;
                    String jsonStr = HttpURLConnectionUtil.doPos(url,content,content_type);
                    Log.e("交易明细返回JSON值：", jsonStr);
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
                case WINDOW_BG_ALPHA:
                    backgroundAlpha((float)msg.obj);
                    break;
                case REFRESH:
                    mAdapter.notifyDataSetChanged();
                    //更新完毕
                    onLoad();
                    break;
                case LOADMORE:
                    mListView.setPullLoadEnable(true);//是否可以上拉加载更多
                    mAdapter.notifyDataSetChanged();
                    // 加载更多完成
                    onLoad();
                    break;
                case NOLOADMORE:
                    mListView.setPullLoadEnable(false);//是否可以上拉加载更多
                    mAdapter.notifyDataSetChanged();
                    // 加载更多完成-->>已没有更多
                    onLoad();
                    break;
                case NetworkUtils.MSG_WHAT_ONE:
                    String orderListJsonStr=(String) msg.obj;
                    orderListJsonStr(orderListJsonStr);
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
        }
    };

    private void orderListJsonStr(String jsonStr){
        try{
            JSONObject job = new JSONObject(jsonStr);
            if("200".equals(job.getString("status"))){
                String dataJson = job.getString("data");
                Gson gjson  =  GsonUtils.getGson();
                java.lang.reflect.Type type = new TypeToken<OrderListResData>() {}.getType();
                OrderListResData order = gjson.fromJson(dataJson, type);
                //获取总条数
                orderListTotalCount = order.getTotalCount();
                Log.e("总条数：", orderListTotalCount+"");
                isHistory = order.getIsHistory();
                List<OrderDetailData> orderList = new ArrayList<OrderDetailData>();
                //获取的list
                orderList = order.getOrderList();
                getMoerNum = orderList.size();
                if(pageNum == 1){
                    list.clear();
                }
                list.addAll(orderList);
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
                    Message msg1 = new Message();
                    msg1.what = REFRESH;
                    handler.sendEmptyMessageDelayed(REFRESH, 2000);
                }else if("2".equals(loadMore)){
                    Message msg1 = new Message();
                    msg1.what = LOADMORE;
                    handler.sendEmptyMessageDelayed(LOADMORE, 2000);
                }
            }else{
                Message msg1 = new Message();
                msg1.what = NOLOADMORE;
                handler.sendEmptyMessageDelayed(NOLOADMORE, 0);
                ToastUtil.showText(activity,"查询失败！",1);
            }

        } catch (JsonSyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Message msg1 = new Message();
            msg1.what = NOLOADMORE;
            handler.sendEmptyMessageDelayed(NOLOADMORE, 0);
            ToastUtil.showText(activity,"查询失败！",1);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Message msg1 = new Message();
            msg1.what = NOLOADMORE;
            handler.sendEmptyMessageDelayed(NOLOADMORE, 0);
            ToastUtil.showText(activity,"查询失败！",1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Message msg1 = new Message();
            msg1.what = NOLOADMORE;
            handler.sendEmptyMessageDelayed(NOLOADMORE, 0);
            ToastUtil.showText(activity,"查询失败！",1);
        }
    }

    /**
     * 背景渐变暗
     */
    private void setWindowBackground(View v){
        popupWindow = new OrderListDateSelectPopupWindow(this);
        popupWindow.showWindow();
        popupWindow.setOnSelectClickListener(this);
        popupWindow.showAsDropDown(v, 0,0);
        //添加pop窗口关闭事件，主要是实现关闭时改变背景的透明度
        popupWindow.setOnDismissListener(new poponDismissListener());
        backgroundAlpha(1f);
        new Thread(new Runnable(){
            @Override
            public void run() {
                while(alpha > 0.5f){
                    try {
                        //4是根据弹出动画时间和减少的透明度计算
                        Thread.sleep(4);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = handler.obtainMessage();
                    msg.what = WINDOW_BG_ALPHA;
                    //每次减少0.01，精度越高，变暗的效果越流畅
                    alpha-=0.01f;
                    msg.obj = alpha ;
                    handler.sendMessage(msg);
                }
            }

        }).start();
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }




    /**
     * 返回或者点击空白位置的时候将背景透明度改回来
     */
    class poponDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            new Thread(new Runnable(){
                @Override
                public void run() {
                    //此处while的条件alpha不能<= 否则会出现黑屏
                    while(alpha<1f){
                        try {
                            Thread.sleep(4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("HeadPortrait","alpha:"+alpha);

                        Message msg = handler.obtainMessage();
                        msg.what = WINDOW_BG_ALPHA;
                        alpha+=0.01f;
                        msg.obj =alpha ;
                        handler.sendMessage(msg);
                    }
                }

            }).start();
        }

    }

    @Override
    public void selectListener(int type) {
        if(OrderListDateSelectPopupWindow.POP_MENU1 == type){
            //当前显示的不是本月的数据时才去查询本月数据
            if(!"2".equals(date_typeStr)){
                tvTitle.setText(getString(R.string.order_list_pop_menu1));
                pageNum = 1;
                date_typeStr = "2";
                getOrderList(pageNum,pageNumCount);
            }
        }else if(OrderListDateSelectPopupWindow.POP_MENU2 == type){
            if(!"3".equals(date_typeStr)){
                tvTitle.setText(getString(R.string.order_list_pop_menu2));
                pageNum = 1;
                date_typeStr = "3";
                getOrderList(pageNum,pageNumCount);
            }
        }else if(OrderListDateSelectPopupWindow.POP_MENU3 == type){
            if(!"1".equals(date_typeStr)){
                tvTitle.setText(getString(R.string.order_list_pop_menu3));
                pageNum = 1;
                date_typeStr = "1";
                getOrderList(pageNum,pageNumCount);
            }
        }
        imgTitleImg.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.open_popupwindow_icon));
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.menu_title_imageView:
                finish();
                break;
            case R.id.menu_title_layout:
                if (popupWindow != null&&popupWindow.isShowing()) {
                    imgTitleImg.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.open_popupwindow_icon));
                    popupWindow.dismiss();
                    return;
                } else {
                    imgTitleImg.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.cloes_popupwindow_icon));
                    //背景渐变暗
                    setWindowBackground(v);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //这里因为添加了头部，所以数据Item的索引值发生变化，即对应item应为：position-1；
        OrderDetailData order = list.get(position-1);
        Intent intent = new Intent();
        intent.setClass(activity,OrderDetailsActivity.class);
        intent.putExtra("userBean",userBean);
        intent.putExtra("order",order);
        intent.putExtra("isHistory",isHistory);
        startActivity(intent);

    }

    @Override
    public void onRefresh() {
        refreshCount++;
        loadMore = "1";
        pageNum = 1;
        getOrderList(pageNum,pageNumCount);
    }

    @Override
    public void onLoadMore() {

        refreshCount++;
        loadMore = "2";
        if(list.size()<=orderListTotalCount&&getMoerNum==pageNumCount){
            //已取出数据条数<=服务器端总条数&&上一次上拉取出的条数 == 规定的每页取出条数时代表还有数据库还有数据没取完
            pageNum = pageNum + 1;
            getOrderList(pageNum,pageNumCount);
        }else{
            //没有数据执行两秒关闭view
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = NOLOADMORE;
                    handler.sendMessage(msg);
                }
            }, 1000);

        }
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime(new Date().toLocaleString());
    }
}
