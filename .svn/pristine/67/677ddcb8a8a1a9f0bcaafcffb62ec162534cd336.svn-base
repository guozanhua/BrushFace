package com.wanding.face.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sym.libs.usbprint.USBPrinter;
import com.wanding.face.NitConfig;
import com.wanding.face.R;
import com.wanding.face.ShiftBaseActivity;
import com.wanding.face.bean.ShiftRecordResData;
import com.wanding.face.bean.ShiftResData;
import com.wanding.face.bean.SubReocrdSummaryResData;
import com.wanding.face.bean.SubTimeSummaryResData;
import com.wanding.face.bean.SubTotalSummaryResData;
import com.wanding.face.bean.UserBean;
import com.wanding.face.httputil.HttpURLConnectionUtil;
import com.wanding.face.httputil.NetworkUtils;
import com.wanding.face.jiabo.device.DeviceConnFactoryManager;
import com.wanding.face.jiabo.device.PrinterCommand;
import com.wanding.face.jiabo.device.ThreadPool;
import com.wanding.face.print.JiaBoPrintTextUtil;
import com.wanding.face.print.USBPrintTextUtil;
import com.wanding.face.utils.ToastUtil;
import com.wanding.face.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 结算、交接班记录详情Activity
 */
@ContentView(R.layout.activity_shift_record_detail)
public class ShiftRecordDetailActivity extends ShiftBaseActivity implements OnClickListener {

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
	
	@ViewInject(R.id.shift_record_detail_tvPrint)
    TextView tvPrint;
	

	private ShiftRecordResData record;
    private ShiftResData summary;


    private String staffName;

	private UserBean userBean;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.back_icon));
        tvTitle.setText("交接班");
        imgTitleImg.setVisibility(View.GONE);
        tvOption.setVisibility(View.GONE);
        tvOption.setText("");

		Intent intent = getIntent();
		userBean = (UserBean) intent.getSerializableExtra("userBean");
		record = (ShiftRecordResData) intent.getSerializableExtra("record");
		String staffNameStr = record.getReserve1();
		if(Utils.isNotEmpty(staffNameStr)){
			staffName = staffNameStr;
		}else{
			staffName = "";
		}

		SummaryDetail();

		initListener();

		

	}
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
		Log.e(TAG, "释放资源成功");
    }
	

	

	


	
	private void initListener() {
		imgBack.setOnClickListener(this);
		tvPrint.setOnClickListener(this);
	}
	


	
	/** 结算请求  */
	private void SummaryDetail(){
	    showWaitDialog();
		final String url = NitConfig.settlementRecordDetailUrl;
		new Thread(){
			@Override
            public void run() {
                try {
                    JSONObject userJSON = new JSONObject();
                    userJSON.put("mid",userBean.getMid());
                    userJSON.put("eid",userBean.getEid());
                    userJSON.put("worId",record.getId());

                    String content = String.valueOf(userJSON);
                    Log.e("交接班记录详情发起请求参数：", content);

					String content_type = HttpURLConnectionUtil.CONTENT_TYPE_JSON;
					String jsonStr = HttpURLConnectionUtil.doPos(url,content,content_type);
                    Log.e("交接班记录详情返回JSON值：", jsonStr);
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
			case NetworkUtils.MSG_WHAT_ONE:
				String jsonStr;
				try {
					jsonStr = (String) msg.obj;
					JSONObject job = new JSONObject(jsonStr);
					if(job.getBoolean("isSuccess")){
						summaryJson(jsonStr);
					}else{

                        ToastUtil.showText(activity,"查询失败！",1);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
                    ToastUtil.showText(activity,"查询失败！",1);
				}
				hideWaitDialog();
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
		};
	};
	
	/** 结算请求返回JSON数据处理 */
	private void summaryJson(String jsonStr){

		try {
			JSONObject job = new JSONObject(jsonStr);
			summary = new ShiftResData();
			JSONObject obj = job.getJSONObject("data");
			JSONArray timeArray = obj.getJSONArray("time");
			ArrayList<SubTimeSummaryResData> timeList = new ArrayList<SubTimeSummaryResData>();
			for (int i = 0; i < timeArray.length(); i++) {
				JSONObject time_obj = timeArray.optJSONObject(i);
				SubTimeSummaryResData time = new SubTimeSummaryResData();
				
				String time_typeStr = time_obj.getString("type");
				Log.e("time_typeStr:", time_typeStr);
				if(time_typeStr==null|| "null".equals(time_typeStr)){
					time.setType("");
				}else{
					time.setType(time_typeStr);
				}
				Log.e("timeType:", time.getType());
				
				String time_totalCountStr = time_obj.getString("totalCount");
				Log.e("time_totalCountStr:", time_totalCountStr);
				if(time_totalCountStr==null|| "null".equals(time_totalCountStr)){
					time.setTotalCount("");
				}else{
					time.setTotalCount(time_totalCountStr);
					
				}
				Log.e("timeTotalCount:", time.getTotalCount());
				
				String time_moneyStr = time_obj.getString("money");
				if(time_moneyStr==null|| "null".equals(time_moneyStr)){
					time.setMoney("");
				}else{
					
					time.setMoney(time_moneyStr);
				}
				Log.e("timeMoney:", time.getMoney());
				
				String time_modeStr = time_obj.getString("mode");
				if(time_modeStr==null|| "null".equals(time_modeStr)){
					
					time.setMode("");
					
				}else{

					time.setMode(time_modeStr);
				}
				Log.e("timeMode:", time.getMode());
				timeList.add(time);
			}
			summary.setTimeList(timeList);
			
			JSONArray reocrdArray = obj.getJSONArray("reocrd");
			ArrayList<SubReocrdSummaryResData> reocrdList = new ArrayList<SubReocrdSummaryResData>();
			for (int i = 0; i < reocrdArray.length(); i++) {
				JSONObject reocrd_obj = reocrdArray.optJSONObject(i);
				SubReocrdSummaryResData reocrd = new SubReocrdSummaryResData();
				
				String reocrd_typeStr = reocrd_obj.getString("type");
				if(reocrd_typeStr==null|| "null".equals(reocrd_typeStr)){
					
					reocrd.setType("");
					
				}else{
					reocrd.setType(reocrd_typeStr);
				}
				
				String reocrd_totalCountStr = reocrd_obj.getString("totalCount");
				if(reocrd_totalCountStr==null|| "null".equals(reocrd_totalCountStr)){
					reocrd.setTotalCount("0");
				}else{
					reocrd.setTotalCount(reocrd_totalCountStr);
					
				}
				
				String reocrd_moneyStr = reocrd_obj.getString("money");
				if(reocrd_moneyStr ==null|| "null".equals(reocrd_moneyStr)){
					reocrd.setMoney("0.00");
				}else{
					
					reocrd.setMoney(reocrd_moneyStr);
				}
				
				String reocrd_modeStr = reocrd_obj.getString("mode");
				if(reocrd_modeStr==null|| "null".equals(reocrd_modeStr)){
					reocrd.setMode("");
				}else{
					reocrd.setMode(reocrd_modeStr);
				}
				reocrdList.add(reocrd);
			}
			summary.setReocrdList(reocrdList);
			
			JSONArray totalArray = obj.getJSONArray("total");
			ArrayList<SubTotalSummaryResData> totalList = new ArrayList<SubTotalSummaryResData>();
			for (int i = 0; i < totalArray.length(); i++) {
				JSONObject total_obj = totalArray.optJSONObject(i);
				SubTotalSummaryResData total = new SubTotalSummaryResData();
				
				
				String total_typeStr = total_obj.getString("type");
				if(total_typeStr==null|| "null".equals(total_typeStr)){
					total.setType("");
				}else{
					
					total.setType(total_typeStr);
				}
				
				String total_totalCountStr = total_obj.getString("totalCount");
				if(total_totalCountStr==null|| "null".equals(total_totalCountStr)){
					total.setTotalCount("0");
				}else{
					
					total.setTotalCount(total_totalCountStr);
				}
				
				String total_moneyStr = total_obj.getString("money");
				if(total_moneyStr==null|| "null".equals(total_moneyStr)){
					total.setMoney("0.00");
				}else{
					
					total.setMoney(total_moneyStr);
				}
				
				String total_modeStr = total_obj.getString("mode");
				if(total_modeStr==null|| "null".equals(total_modeStr)){
					total.setMode("total");
				}else{
					
					total.setMode(total_modeStr);
				}
				totalList.add(total);
			}
			summary.setTotalList(totalList);
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateView(summary);
		
	}

	private void startPrint(){
		//打印
//		USBPrinter.initPrinter(this);
		USBPrintTextUtil.shiftExitText(userBean,summary,staffName);

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
					JiaBoPrintTextUtil.shiftExitText(activity,id,userBean,summary,staffName);
				} else {
					mHandler.obtainMessage( PRINTER_COMMAND_ERROR ).sendToTarget();
				}
			}
		} );*/
	}
	

	@Override
	public void onClick(View v) {
		Intent in = null;
		switch (v.getId()) {
		case R.id.menu_title_imageView:
			finish();
			break;
		case R.id.shift_record_detail_tvPrint:
			//打印
			startPrint();
			break;
			default:
				break;
		}
	}
	
}
