package com.wanding.face.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanding.face.App;
import com.wanding.face.Constants;
import com.wanding.face.R;
import com.wanding.face.bean.OrderDetailData;
import com.wanding.face.bean.WdPreAuthHistoryVO;
import com.wanding.face.utils.DateTimeUtil;
import com.wanding.face.utils.DecimalUtil;
import com.wanding.face.utils.Utils;

import java.util.List;

/**
 *  预授权列表Item
 */
public class AuthOrderListAdapter extends BaseAdapter{


    private Context context;
    private List<WdPreAuthHistoryVO> list;
    private LayoutInflater inflater;

    public AuthOrderListAdapter(Context context, List<WdPreAuthHistoryVO> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{

        ImageView imgIcon;//交易类型icon
        TextView tvOrderId;
        TextView tvOrderPayTime;
        TextView tvOrderTotal;
        TextView tvOrderStatus;
    }

    @Override
    public View getView(int position, View subView, ViewGroup parent) {
        WdPreAuthHistoryVO order = list.get(position);
        ViewHolder vh = null;
        if(subView == null){
            subView = inflater.inflate(R.layout.auth_recode_list_item,null);
            vh = new ViewHolder();
            vh.imgIcon = subView.findViewById(R.id.auth_recode_list_item_imgIcon);
            vh.tvOrderId = subView.findViewById(R.id.auth_recode_list_item_authOrderId);
            vh.tvOrderPayTime  = subView.findViewById(R.id.auth_recode_list_item_authPayTime);
            vh.tvOrderTotal  = subView.findViewById(R.id.auth_recode_list_item_authTotal);
            vh.tvOrderStatus = subView.findViewById(R.id.auth_recode_list_item_authStatus);
            subView.setTag(vh);
        }else{
            vh = (ViewHolder) subView.getTag();

        }
        //交易类型icon WX=微信，ALI=支付宝，BEST=翼支付
        String payWayStr = order.getPreType();
        if(Utils.isNotEmpty(payWayStr)){
            if(payWayStr.equals(Constants.PAY_TYPE_WX)){
                vh.imgIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.wxin_log_icon));
            }else if(payWayStr.equals(Constants.PAY_TYPE_ALI)){
                vh.imgIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ali_log_icon));
            }
        }
        //订单号
        String orderId = order.getMchntOrderNo();
        vh.tvOrderId.setText(String.format(context.getResources().getString(R.string.order_list_item_orderId), orderId));

        //交易状态:1 预授权 2撤销 3押金消费 4押金退款
        String payAuthStatusStr = order.getPayAuthStatus();
        //交易时间
        String payTime ="";
        if(Utils.isNotEmpty(payAuthStatusStr)){
            if("1".equals(payAuthStatusStr))
            {
                Long payTimeStr = order.getPreTime();
                if(payTimeStr !=null){
                    payTime = DateTimeUtil.stampToFormatDate(payTimeStr, "yyyy-MM-dd HH:mm:ss");
                }
            }else if("2".equals(payAuthStatusStr))
            {
                Long payTimeStr = order.getTxnEndTs();
                if(payTimeStr !=null){
                    payTime = DateTimeUtil.stampToFormatDate(payTimeStr, "yyyy-MM-dd HH:mm:ss");
                }
            }else if("3".equals(payAuthStatusStr))
            {
                Long payTimeStr = order.getTxnEndTs();
                if(payTimeStr !=null){
                    payTime = DateTimeUtil.stampToFormatDate(payTimeStr, "yyyy-MM-dd HH:mm:ss");
                }


            }else if("4".equals(payAuthStatusStr))
            {
                Long payTimeStr = order.getTxnEndTs();
                if(payTimeStr !=null){
                    payTime = DateTimeUtil.stampToFormatDate(payTimeStr, "yyyy-MM-dd HH:mm:ss");
                }

            }
        }
        vh.tvOrderPayTime.setText(payTime);
        /**
         * 交易金额（根据不同的业务只显示当前业务金额）
         * 例如：预授权完成状态的订单只显示订单的扣费金额 预授权完成撤销的订单只显示退款金额
         */
        String goodsPriceStr = "";
        if(Utils.isNotEmpty(payAuthStatusStr)){
            if("1".equals(payAuthStatusStr))
            {
                goodsPriceStr = order.getOrderAmt();
            }else if("2".equals(payAuthStatusStr))
            {
                goodsPriceStr = order.getOrderAmt();
            }else if("3".equals(payAuthStatusStr))
            {
                goodsPriceStr = order.getConsumeFee();
            }else if("4".equals(payAuthStatusStr))
            {
                goodsPriceStr = order.getRefundFee();
            }
        }


        String goodsPrice = "";
        if(Utils.isNotEmpty(goodsPriceStr)){
            goodsPrice = DecimalUtil.StringToPrice(goodsPriceStr);
        }
        vh.tvOrderTotal.setText(String.format(context.getResources().getString(R.string.order_list_item_orderPayTotal), goodsPrice));

        String payAuthStatus = "交易未知";
        if(Utils.isNotEmpty(payAuthStatusStr)){
            payAuthStatus = Constants.getAuthPayTypeStr(payAuthStatusStr);
        }
        vh.tvOrderStatus.setText(payAuthStatus);

        return subView;
    }
}
