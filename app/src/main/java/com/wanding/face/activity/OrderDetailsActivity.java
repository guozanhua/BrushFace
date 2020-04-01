package com.wanding.face.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wanding.face.BaseActivity;
import com.wanding.face.Constants;
import com.wanding.face.NitConfig;
import com.wanding.face.R;
import com.wanding.face.bean.OrderDetailData;
import com.wanding.face.bean.UserBean;
import com.wanding.face.httputil.HttpURLConnectionUtil;
import com.wanding.face.httputil.NetworkUtils;
import com.wanding.face.print.USBPrintTextUtil;
import com.wanding.face.utils.DateTimeUtil;
import com.wanding.face.utils.DecimalUtil;
import com.wanding.face.utils.GsonUtils;
import com.wanding.face.utils.ToastUtil;
import com.wanding.face.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;

/**  订单详情界面 */
@ContentView(R.layout.activity_order_details)
public class OrderDetailsActivity extends BaseActivity implements OnClickListener {


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

	@ViewInject(R.id.order_details_imgPayState)
	ImageView imgPayState;
	@ViewInject(R.id.order_details_tvPayState)
	TextView tvPayState;

	@ViewInject(R.id.order_details_viewRefundAmount)
	View viewRefundAmount;
	@ViewInject(R.id.order_details_layoutRefundAmount)
	RelativeLayout layoutRefundAmount;

	@ViewInject(R.id.order_details_tvPayMoneyTitle)
	TextView tvPayMoneyTitle;
	@ViewInject(R.id.order_details_tvPayMoney)
	TextView tvPayMoney;
	@ViewInject(R.id.order_details_tvRefundAmount)
	TextView tvRefundAmount;
	@ViewInject(R.id.order_details_tvPayType)
	TextView tvPayType;
	@ViewInject(R.id.order_details_tvBusName)
	TextView tvBusName;
	@ViewInject(R.id.order_details_tvTerminalName)
	TextView tvTerminalName;
	@ViewInject(R.id.order_details_tvPayWaterNum)
	TextView tvPayWaterNum;
	@ViewInject(R.id.order_details_tvPayCreateTime)
	TextView tvPayCreateTime;
	@ViewInject(R.id.order_details_tvPayOrderId)
	TextView tvPayOrderId;


	
	/** 完成，重打印  */
	@ViewInject(R.id.order_details_btRefund)
	Button btRefund;
	@ViewInject(R.id.order_details_btPrint)
	Button btPrint;



	private UserBean userBean;
	private OrderDetailData order;//订单对象
	/**
	 * isHistory N：实时订单，Y：历史订单
	 */
	private String isHistory;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imgBack.setVisibility(View.VISIBLE);
		imgBack.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.back_icon));
		tvTitle.setText("交易详情");
		imgTitleImg.setVisibility(View.GONE);
		tvOption.setVisibility(View.GONE);
		tvOption.setText("");



		Intent intent = getIntent();
		userBean = (UserBean) intent.getSerializableExtra("userBean");
		order = (OrderDetailData) intent.getSerializableExtra("order");
		isHistory = intent.getStringExtra("isHistory");


		initListener();


		updateViewData();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		if("N".equals(isHistory)){
			String url = NitConfig.getOrderDetailsUrl;
			getOrderDetails(url);
			Log.e("订单详情：","实时订单");
		}else if("Y".equals(isHistory)){
			String url = NitConfig.getOrderHistoryDetailsUrl;
			getOrderDetails(url);
			Log.e("订单详情：","历史订单");
		}
	}
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
		Log.e(TAG, "释放资源成功");
    }


	
	/** 
	 * 初始化界面控件
	 */
	private void initListener(){

		imgBack.setOnClickListener(this);
		btRefund.setOnClickListener(this);
		btPrint.setOnClickListener(this);
	}


	/** 界面数据初始化 */
    private void updateViewData(){

		/**
		 * 判断是支付交易还是退款交易 0正向 ,1退款,其中正向包括支付交易和退款交易
		 * orderType:交易类型 0正向（支付交易） ,1反向（退款交易）
		 * status：交易状态 	0准备支付1支付完成2支付失败3.包括退款4.全部退款5.支付未知
		 */
		String orderTypeStr = order.getOrderType();
		String orderStateStr = order.getStatus();
		//交易状态
		if(Utils.isNotEmpty(orderTypeStr)){
			if(Utils.isNotEmpty(orderStateStr)&& "5".equals(orderStateStr)){
//                imgPayState.setImageDrawable(getResources().getDrawable(R.drawable.unknown_icon));
				imgPayState.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.unknown_icon));
				tvPayState.setText("支付状态未知");
				tvPayMoneyTitle.setText("收款金额");
//                btRefund.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray_frame1dp_radius10));
//                btRefund.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.bg_gray_frame1dp_radius10));
				btRefund.setBackgroundResource(R.drawable.bg_gray_frame1dp_radius10);
				btRefund.setTextColor(ContextCompat.getColor(activity,R.color.white_f8f8f8));
				btRefund.setEnabled(false);

			}else{
				if("0".equals(orderTypeStr)){
//                    imgPayState.setImageDrawable(getResources().getDrawable(R.drawable.success_icon));
					imgPayState.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.success_icon));
					tvPayState.setText("收款成功");
					tvPayMoneyTitle.setText("收款金额");
//                    btRefund.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_blue_frame1dp_radius10));
//                    btRefund.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.bg_blue_frame1dp_radius10));
					btRefund.setBackgroundResource(R.drawable.button_style);
					btRefund.setTextColor(ContextCompat.getColor(activity,R.color.white_ffffff));
					btRefund.setEnabled(true);
					if("4".equals(orderStateStr)){
//                        imgPayState.setImageDrawable(getResources().getDrawable(R.drawable.success_icon));
						imgPayState.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.success_icon));
						tvPayState.setText("收款成功");
						tvPayMoneyTitle.setText("收款金额");
//                        btRefund.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray_frame1dp_radius10));
//                        btRefund.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.bg_gray_frame1dp_radius10));
						btRefund.setBackgroundResource(R.drawable.bg_gray_frame1dp_radius10);
						btRefund.setTextColor(ContextCompat.getColor(activity,R.color.white_f8f8f8));
						btRefund.setEnabled(false);
					}
//                refundAmountLayout.setVisibility(View.GONE);
				}else if("1".equals(orderTypeStr)){
//                    imgPayState.setImageDrawable(getResources().getDrawable(R.drawable.refund_icon));
					imgPayState.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.refund_icon));
					tvPayState.setText("已退款");
					tvPayMoneyTitle.setText("退款金额");
//                    btRefund.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray_frame1dp_radius10));
//                    btRefund.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.bg_gray_frame1dp_radius10));
					btRefund.setBackgroundResource(R.drawable.bg_gray_frame1dp_radius10);
					btRefund.setTextColor(ContextCompat.getColor(activity,R.color.white_f8f8f8));
					btRefund.setEnabled(false);
//                refundAmountLayout.setVisibility(View.GONE);
				}
			}
		}


		//交易金额
		String payMoneyStr = order.getGoodsPrice();
		if(Utils.isNotEmpty(payMoneyStr)){
			tvPayMoney.setText(DecimalUtil.StringToPrice(payMoneyStr)+"元");
		}
		//退款金额
		String refundAmountStr = order.getRefundAmount();
		String refundAmount = "0.00";
		if(Utils.isNotEmpty(orderTypeStr)){
			if("0".equals(orderTypeStr)){
				viewRefundAmount.setVisibility(View.VISIBLE);
				layoutRefundAmount.setVisibility(View.VISIBLE);
				if(Utils.isNotEmpty(refundAmountStr)){
					refundAmount = refundAmountStr;
				}

			}else if("1".equals(orderTypeStr)){
				viewRefundAmount.setVisibility(View.GONE);
				layoutRefundAmount.setVisibility(View.GONE);
			}

		}
		tvRefundAmount.setText(DecimalUtil.StringToPrice(refundAmount)+"元");

		//交易类别
		String payTypeStr = order.getPayWay();
		String payType = "未知";
		if("WX".equals(payTypeStr)){
			payType = "微信";
		}else if("ALI".equals(payTypeStr)){
			payType = "支付宝";
		}else if("BEST".equals(payTypeStr)){
			payType = "翼支付";
		}else if("DEBIT".equals(payTypeStr)){
			//DEBIT= 借记卡       CREDIT=贷记卡
			payType = "银行卡(借记卡)";
		}else if("CREDIT".equals(payTypeStr)){
			//DEBIT= 借记卡       CREDIT=贷记卡
			payType = "银行卡(贷记卡)";
		}else if("UNIONPAY".equals(payTypeStr)){
			//UNIONPAY = 银联二维码
			payType = "银联二维码";
		}else if("BANK".equals(payTypeStr)){
			//BANK = 银行卡
			payType = "银行卡";
		}else{
			payType = "未知";
		}
		tvPayType.setText(payType);

		//门店名称
		String busNameStr = order.getStoreName();
		if(Utils.isNotEmpty(busNameStr)){
			tvBusName.setText(busNameStr);
		}
		//终端名称
		String terminalNameStr = order.getUsername();
		if(Utils.isNotEmpty(terminalNameStr)){
			tvTerminalName.setText(terminalNameStr);
		}
		//终端流水
		String transactionIdStr = order.getTransactionId();
		if(Utils.isNotEmpty(transactionIdStr)){
			tvPayWaterNum.setText(transactionIdStr);
		}
		//日期时间
		String orderTimeStr = order.getPayTime();
		String orderPayTime = "";

		if(Utils.isNotEmpty(orderTimeStr)){
			try{
				orderPayTime = DateTimeUtil.stampToFormatDate(Long.parseLong(orderTimeStr), "yyyy年MM月dd日 HH:mm:ss");
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		tvPayCreateTime.setText(orderPayTime);
		//订单号
		String orderIdStr = order.getOrderId();
		String orderId = "";
		if(Utils.isNotEmpty(orderIdStr)){
			orderId = orderIdStr;
		}
		tvPayOrderId.setText(orderId);

    }

	/**
	 * 订单详情 api/app/200/1/queryOrderDetail
	 * 入参：orderId（订单主键id）
	 */
	private void getOrderDetails(final String url) {
		showCustomDialog();
		new Thread(){
			@Override
			public void run() {
				try {
					// 拼装JSON数据，向服务端发起请求
					JSONObject userJSON = new JSONObject();
					userJSON.put("orderId", order.getId());
					String content = String.valueOf(userJSON);
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
					String orderDetailJsonStr=(String) msg.obj;
					orderDetailJsonStr(orderDetailJsonStr);
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

	private void orderDetailJsonStr(String jsonStr){
		try {
			JSONObject job = new JSONObject(jsonStr);
			if ("200".equals(job.getString("status"))) {
				JSONObject dataObj = new JSONObject(job.getString("data"));
				String orderJson = dataObj.getString("order");
				Gson gjson  =  GsonUtils.getGson();
				order = gjson.fromJson(orderJson, OrderDetailData.class);
				updateViewData();

			} else {
				ToastUtil.showText(activity,"查询失败！",1);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void startPrint(){

		//打印
//		USBPrinter.initPrinter(this);
		USBPrintTextUtil.orderDetailText(userBean,order);

		/*if ( DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null ||
				!DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState() )
		{
			ToastUtil.showText( activity, getString( R.string.str_cann_printer ),1 );
			return;
		}
		threadPool = ThreadPool.getInstantiation();
		threadPool.addTask( new Runnable()
		{
			@Override
			public void run()
			{
				if ( DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentPrinterCommand() == PrinterCommand.ESC )
				{
					JiaBoPrintTextUtil.orderDetailText(activity,id,userBean,order);
				} else {
					mHandler.obtainMessage( PRINTER_COMMAND_ERROR ).sendToTarget();
				}
			}
		} );*/
	}

	/**
	 * 是否确认修改密码
	 */
	private void showMsgDialog(){
		View view = LayoutInflater.from(this).inflate(R.layout.action_prompt_dialog, null);
		//描述信息
		TextView tvMsg = view.findViewById(R.id.action_prompt_tvMsg);
		tvMsg.setText("该笔订单为预授权完成交易，如需退款需从预授权交易记录入口进行预授权完成撤销操作！");
		//操作按钮
		final Button btCancel = (Button) view.findViewById(R.id.action_prompt_btCancel);
		final Button btSuccess = (Button) view.findViewById(R.id.action_prompt_btSuccess);

		final Dialog mDialog = new Dialog(this,R.style.dialog);
		Window dialogWindow = mDialog.getWindow();
		WindowManager.LayoutParams params = mDialog.getWindow().getAttributes(); // 获取对话框当前的参数值
		dialogWindow.setAttributes(params);
		mDialog.setContentView(view);
		btCancel.setVisibility(View.GONE);
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

			}
		});
		//点击屏幕和物理返回键dialog不消失
		mDialog.setCancelable(false);
		mDialog.show();
	}

	@Override
	public void onClick(View v) {
		Intent in = null;
		switch (v.getId()) {
		case R.id.menu_title_imageView:
			finish();
			break;
		case R.id.order_details_btRefund:
			if(Utils.isFastClick()){
				return;
			}
			//退款时交易分为正常支付以及预授权等交易，需根据订单号开头两位判断是否为预授权交易
			String orderIdStr = order.getOrderId();
			if(Utils.isNotEmpty(orderIdStr)){
				String outTradeNo = orderIdStr.substring(0,2);
				if("60".equals(outTradeNo)){
					showMsgDialog();
					return;
				}
			}
			Intent intent = new Intent();
			intent.setClass(activity,EnterPasswdActivity.class);
			intent.putExtra("userBean",userBean);
			intent.putExtra("action",Constants.ACTION_PASSWD_DETAIL_REFUND);
			intent.putExtra("order",order);
			startActivity(intent);
			break;
		case R.id.order_details_btPrint:
			if(Utils.isFastClick()){
				return;
			}
			//交易未知不打印小票
			boolean orderStatus = true;
			String orderTypeStr = order.getOrderType();
			if(orderTypeStr!=null&&!"".equals(orderTypeStr) &&!"null".equals(orderTypeStr)){
				//先判断是支付交易还是退款交易 0正向 ,1退款
				if("0".equals(orderTypeStr)){
					//判断交易状态状态status 状态为支付、预支付、退款等	0准备支付1支付完成2支付失败3.包括退款5.支付未知
					String statusStr = order.getStatus();
					if(statusStr!=null&&!"null".equals(statusStr) && "1".equals(statusStr)){

					}else if(statusStr!=null&&!"null".equals(statusStr) && "3".equals(statusStr)){

					}else if(statusStr!=null&&!"null".equals(statusStr) && "4".equals(statusStr)){

					}else if(statusStr!=null&&!"null".equals(statusStr) && "5".equals(statusStr)){
						orderStatus = false;
					}else{
						orderStatus = false;
					}
				}
			}


			String payTypeStr = order.getPayWay();
			if(orderStatus){
				if(!"DEBIT".equals(payTypeStr) &&!"CREDIT".equals(payTypeStr) &&!"BANK".equals(payTypeStr)){

					//打印
					startPrint();

				}else{
					Toast.makeText(OrderDetailsActivity.this, "银行卡交易不支持明细补打！", Toast.LENGTH_LONG).show();
				}

			}else{
				Toast.makeText(OrderDetailsActivity.this, "该订单状态未知，不打印小票！", Toast.LENGTH_LONG).show();
			}
			break;
			default:
				break;
			
		}
	}
}
