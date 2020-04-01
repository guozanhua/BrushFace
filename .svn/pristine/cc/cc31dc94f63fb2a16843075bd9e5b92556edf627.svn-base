package com.wanding.face.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wanding.face.App;
import com.wanding.face.BaseActivity;
import com.wanding.face.NitConfig;
import com.wanding.face.R;
import com.wanding.face.UpdateApkFileProvider;
import com.wanding.face.httputil.NetworkUtils;
import com.wanding.face.update.DownLoadAsyncTask;
import com.wanding.face.update.HttpURLConUtil;
import com.wanding.face.update.UpdateInfo;
import com.wanding.face.update.UpdateUrl;
import com.wanding.face.utils.ToastUtil;
import com.wanding.face.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.File;

/**
 * 悦收银版本信息Activity
 */
@ContentView(R.layout.activity_about_us)
public class AboutUsActivity extends BaseActivity implements OnClickListener {


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



	@ViewInject(R.id.about_us_tvVersionCode)
	TextView tvVersionName;
	@ViewInject(R.id.about_us_testVersionLayout)
	RelativeLayout getVersionCode;


	private UpdateInfo info;
	private Dialog mDialog;


	private static final int REQUEST_CODE_INSTALL_PERMISSION = 0x1001;//8.0安装权限请求



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imgBack.setVisibility(View.VISIBLE);
		imgBack.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.back_icon));
		tvTitle.setText("关于悦收银");
		imgTitleImg.setVisibility(View.GONE);
		tvOption.setVisibility(View.GONE);
		tvOption.setText("");



		initView();

		initListener();
	}


	/**
	 * 初始化界面控件
	 */
	private void initView(){
		try {
			tvVersionName.setText("悦收银："+getVersionName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tvVersionName.setText("悦收银：1.01");
		}

	}

	private void initListener(){
		imgBack.setOnClickListener(this);
		getVersionCode.setOnClickListener(this);
	}

	/**
	 * 获取服务器版本号
	 */
	private void checkVersionTask(){
		showCustomDialog("获取版本号！");
		new Thread(){
			@Override
			public void run() {
				try{
					String versionName = Utils.getVersionName(activity);

					//获取服务器保存版本信息的路径
					String path = "";
					if(NitConfig.isFormal){
						path = UpdateUrl.URL;
						Log.e("更新版本地址：","生产环境-----");
					}else{
						path = UpdateUrl.TEST_URL;
						Log.e("更新版本地址：","测试环境-----");
					}
					//解析xml文件封装成对象
					info =  HttpURLConUtil.getUpdateInfo(path);
					Log.e(TAG,"版本号为："+info.getVersion());
					Log.e(TAG,"下载路径为："+info.getUrl());
					String xmlVersionName = info.getVersion();
					if(xmlVersionName.equals(versionName)){
						int msg = HttpURLConUtil.NO_UPDATE_HINT;
						String text = "";
						sendMessage(msg,text);
					}else{
						Log.e(TAG,"版本号不同 ,提示用户升级 ");
						int msg = HttpURLConUtil.UPDATE_HINT;
						String text = "";
						sendMessage(msg,text);
					}
				}catch (Exception e){
					e.printStackTrace();
					int msg = HttpURLConUtil.TEST_VERSION_FAIL;
					String text = HttpURLConUtil.TEST_VERSION_FAIL_MSG;
					sendMessage(msg,text);
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
				case HttpURLConUtil.NO_UPDATE_HINT:
					ToastUtil.showText(activity,"已近是最新版本呢！",1);
					hideCustomDialog();
					break;
				case HttpURLConUtil.UPDATE_HINT:
					showUpdateDialog();
					hideCustomDialog();
					break;
				case HttpURLConUtil.TO_INSTALL:
					installProcess();
					break;
				case HttpURLConUtil.DOWNLOAD_FAIL:
					errorJsonText = (String) msg.obj;
					ToastUtil.showText(activity,errorJsonText,1);
					break;
				case HttpURLConUtil.TEST_VERSION_FAIL:
					errorJsonText = (String) msg.obj;
					ToastUtil.showText(activity,errorJsonText,1);
					hideCustomDialog();
					break;
				default:
					break;
			}
		}
	};
	/**
	 * 弹出版本升级提示框
	 */
	private void showUpdateDialog(){
		View view = LayoutInflater.from(this).inflate(R.layout.app_update_hint_dialog, null);
		//版本号：
		TextView tvVersion=(TextView) view.findViewById(R.id.app_update_hint_tvVersion);
		tvVersion.setText("v"+info.getVersion());
		//描述信息

		//进度条
		final ProgressBar mProgressBar = view.findViewById(R.id.app_update_hint_progressBar);
		RelativeLayout layoutMsg = view.findViewById(R.id.app_update_hint_layoutMsg);
		mProgressBar.setVisibility(View.INVISIBLE);
		layoutMsg.setVisibility(View.INVISIBLE);
		//操作按钮
		final Button btCancel = (Button) view.findViewById(R.id.app_update_hint_btCancel);
		final Button btUpdate = (Button) view.findViewById(R.id.app_update_hint_btUpdate);
		mDialog = new Dialog(this,R.style.dialog);
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
		btUpdate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mProgressBar.setVisibility(View.VISIBLE);
				btUpdate.setText("正在下载");
				//开始下载
				DownLoadAsyncTask downLoad=new DownLoadAsyncTask(activity, handler,info);
				downLoad.execute(info.getUrl());

				mDialog.dismiss();

			}
		});
		//点击屏幕和物理返回键dialog不消失
		mDialog.setCancelable(false);
		mDialog.show();
	}

	//安装应用的流程
	private void installProcess() {

		//注意以下判断8.0以上要设置未知来源的权限,
		// targetSdkVersion需要设置要大于等于26, 否则 haveInstallPermission  只能获取到false
		boolean haveInstallPermission;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O  ) {
			//先获取是否有安装未知来源应用的权限
			haveInstallPermission = getPackageManager().canRequestPackageInstalls();
			if (!haveInstallPermission) {
				//没有权限
				new AlertDialog.Builder(activity)
						.setTitle("提示")
						.setMessage("安装应用需要打开[允许安装未知来源应用]权限，请去设置中开启权限")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
									startInstallPermissionSettingActivity();
								}
								dialog.dismiss();

							}
						}).show();


				return;
			}
		}
		//有权限，开始安装应用程序
		toInstall();
	}

	private void startInstallPermissionSettingActivity() {
		Uri packageURI = Uri.parse("package:" + getPackageName());
		//注意这个是8.0新API
		Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
		startActivityForResult(intent, REQUEST_CODE_INSTALL_PERMISSION);
	}

	/**
	 * 安装新版本
	 */
	private void toInstall(){



		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		File apkFile = new File(UpdateUrl.APK_SAVE_PATH);
		Uri apkUri;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			// 授予目录临时共享权限
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			String authority = getPackageName() + ".updateFileProvider";
			apkUri = UpdateApkFileProvider.getUriForFile(activity, authority, apkFile);
		} else {
			apkUri = Uri.fromFile(apkFile);
		}
		intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
		startActivity(intent);
		App.getInstance().exit();

	}
	 /*
     * 获取当前程序的版本号
     */
    private String getVersionName() throws Exception {
        //获取packagemanager的实例
        PackageManager packageManager =getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packInfo.versionName;
    }



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CODE_INSTALL_PERMISSION){
			if(resultCode == RESULT_OK){
				//再次执行安装流程，包含权限判等
				installProcess();
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}




	
	@Override
	public void onClick(View v) {
		Intent in = null;
		switch (v.getId()) {
		case R.id.menu_title_imageView:
			finish();
			break;
		case R.id.about_us_testVersionLayout:
			if(Utils.isFastClick()){
				return;
			}
			/**
			 * 比对版本号/读取更新信息/下载APK/安装
			 */
			checkVersionTask();
//			ToastUtil.showText(activity,"暂未开放！",1);
			break;
			default:
				break;
		}
	}
}
