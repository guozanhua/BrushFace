package com.wanding.face.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wanding.face.R;

/**
 * 拍照，图库选择PopupWindow
 *
 */
public class DevicePopupWindow extends PopupWindow{

    private Context context;

    public DevicePopupWindow(Context context) {
        super(context);
        this.context = context;
    }

    private OnSelectClickListener onSelectClickListener;

    public void setOnSelectClickListener(OnSelectClickListener onSelectClickListener){
        this.onSelectClickListener = onSelectClickListener;
    }

    public interface OnSelectClickListener{
        void selectListener(int type);
    }

    public void showPhotoWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.device_select_popwindow,null);
        TextView tvUSB = view.findViewById(R.id.popwindow_tvUSB);
        TextView tvBluetooth = view.findViewById(R.id.popwindow_tvBluetooth);
        TextView tvSerialPort = view.findViewById(R.id.popwindow_tvSerialPort);
        TextView tvWifi = view.findViewById(R.id.popwindow_tvWifi);
        TextView tvCancel = view.findViewById(R.id.popwindow_tvCancel);
        // USB
        tvUSB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = 1;
                onSelectClickListener.selectListener(type);
                // 销毁弹出框
                dismiss();
            }
        });
        // Bluetoooth
        tvBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = 2;
                onSelectClickListener.selectListener(type);
                // 销毁弹出框
                dismiss();
            }
        });
        // SerialPort
        tvSerialPort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = 3;
                onSelectClickListener.selectListener(type);
                // 销毁弹出框
                dismiss();
            }
        });
        // tvWifi
        tvWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = 4;
                onSelectClickListener.selectListener(type);
                // 销毁弹出框
                dismiss();
            }
        });

        // 取消
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });

        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗口的宽
        this.setWidth(LinearLayout.LayoutParams.FILL_PARENT);
        // 设置SelectPicPopupWindow弹出窗口的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗口可点击  点击空白处时，隐藏掉pop窗口
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗口动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗口的背景
        this.setBackgroundDrawable(dw);

    }


}
