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
 * 交易查询当天，昨天，本月选择PopupWindow
 *
 */
public class OrderListDateSelectPopupWindow extends PopupWindow{

    private Context context;

    public static final int POP_MENU1 = 1;
    public static final int POP_MENU2 = 2;
    public static final int POP_MENU3 = 3;

    public OrderListDateSelectPopupWindow(Context context) {
        super(context);
        this.context = context;
    }

    private OnSelectClickListener onSelectClickListener;

    public void setOnSelectClickListener(OnSelectClickListener onSelectClickListener){
        this.onSelectClickListener = onSelectClickListener;
    }

    public interface OnSelectClickListener{
        public void selectListener(int type);
    }

    public void showWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.order_list_top_popupwindow,null);
        TextView tvMonth = view.findViewById(R.id.order_list_topPup_month);
        TextView tvYesterday = view.findViewById(R.id.order_list_topPup_yesterday);
        TextView tvDay = view.findViewById(R.id.order_list_topPup_day);
        // 当月（不包含当天）
        tvMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = POP_MENU1;
                onSelectClickListener.selectListener(type);
                // 销毁弹出框
                dismiss();
            }
        });
        // 昨天
        tvYesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = POP_MENU2;
                onSelectClickListener.selectListener(type);
                // 销毁弹出框
                dismiss();
            }
        });

        // 当天
        tvDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = POP_MENU3;
                onSelectClickListener.selectListener(type);
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
        this.setFocusable(false);
        // 设置SelectPicPopupWindow弹出窗口动画效果
        this.setAnimationStyle(R.style.AnimTop);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗口的背景
        this.setBackgroundDrawable(dw);

    }


}
