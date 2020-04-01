package com.wanding.face.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanding.face.Constants;
import com.wanding.face.R;
import com.wanding.face.bean.OrderDetailData;
import com.wanding.face.utils.DateTimeUtil;
import com.wanding.face.utils.DecimalUtil;
import com.wanding.face.utils.Utils;

import java.util.List;

/**
 *  首页列表Adapter
 */
public class OrderListAdapter extends BaseAdapter{


    private Context context;
    private List<OrderDetailData> list;
    private LayoutInflater inflater;

    public OrderListAdapter(Context context, List<OrderDetailData> list) {
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
        OrderDetailData order = list.get(position);
        ViewHolder vh = null;
        if(subView == null){
            subView = inflater.inflate(R.layout.order_list_item,null);
            vh = new ViewHolder();
            vh.imgIcon = subView.findViewById(R.id.order_list_item_imgIcon);
            vh.tvOrderId = subView.findViewById(R.id.order_list_item_orderId);
            vh.tvOrderPayTime  = subView.findViewById(R.id.order_list_item_orderPayTime);
            vh.tvOrderTotal  = subView.findViewById(R.id.order_list_item_orderTotal);
            vh.tvOrderStatus = subView.findViewById(R.id.order_list_item_orderStatus);
            subView.setTag(vh);
        }else{
            vh = (ViewHolder) subView.getTag();

        }
        //交易类型icon WX=微信，ALI=支付宝，BEST=翼支付
        String payWayStr = order.getPayWay();
        if(Utils.isNotEmpty(payWayStr)){
            if(payWayStr.equals(Constants.PAY_TYPE_WX)){
                vh.imgIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.wxin_log_icon));
            }else if(payWayStr.equals(Constants.PAY_TYPE_ALI)){
                vh.imgIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ali_log_icon));
            }
        }
        //订单号
        String orderId = order.getOrderId();
        vh.tvOrderId.setText(String.format(context.getResources().getString(R.string.order_list_item_orderId), orderId));
        //支付时间
        String payTimeStr = order.getPayTime();
        String payTime = "";
        if(Utils.isNotEmpty(payTimeStr)){
            try{
                payTime = DateTimeUtil.stampToFormatDate(Long.parseLong(payTimeStr), "yyyy-MM-dd HH:mm:ss");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        vh.tvOrderPayTime.setText(payTime);
        //支付金额
        String goodsPriceStr = order.getGoodsPrice();
        String goodsPrice = "";
        if(Utils.isNotEmpty(goodsPriceStr)){
            goodsPrice = DecimalUtil.StringToPrice(goodsPriceStr);
        }
        vh.tvOrderTotal.setText(String.format(context.getResources().getString(R.string.order_list_item_orderPayTotal), goodsPrice));
        //支付状态
        String orderType = order.getOrderType();
        String status = order.getStatus();
        vh.tvOrderStatus.setText(Constants.getOrderStatus(orderType,status));

        return subView;
    }
}
