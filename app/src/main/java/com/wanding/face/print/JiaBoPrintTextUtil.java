package com.wanding.face.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.wanding.face.Constants;
import com.wanding.face.R;
import com.wanding.face.bean.AuthResultResponse;
import com.wanding.face.bean.CardVerificaResData;
import com.wanding.face.bean.OrderDetailData;
import com.wanding.face.bean.PayTypeBean;
import com.wanding.face.bean.RefundResData;
import com.wanding.face.bean.ShiftResData;
import com.wanding.face.bean.SubReocrdSummaryResData;
import com.wanding.face.bean.SubTimeSummaryResData;
import com.wanding.face.bean.SubTotalSummaryResData;
import com.wanding.face.bean.SummaryResData;
import com.wanding.face.bean.UserBean;
import com.wanding.face.bean.WdPreAuthHistoryVO;
import com.wanding.face.jiabo.device.DeviceConnFactoryManager;
import com.wanding.face.jiabo.device.bean.PosScanpayResData;
import com.wanding.face.utils.DateTimeUtil;
import com.wanding.face.utils.DecimalUtil;
import com.wanding.face.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
/**
 * https://www.jianshu.com/p/2314f8a81ec5
 */
public class JiaBoPrintTextUtil {

//    /** 支付完成打印等待时间打印消费有礼 */
//    public static final long time = 3000;
//
//    /** 统一空格字符串   */
//    private static final String twoSpaceStr = "  ";
//    private static final String threeSpaceStr = "   ";
//    private static final String fiveSpaceStr = "     ";
//    private static final String sixSpaceStr = "      ";
//    private static final String sevenSpaceStr = "       ";
//    private static final String eightSpaceStr = "        ";
//    private static final String nineSpaceStr = "         ";
//
//    private static String DOTTED_LINE = "-------------------------------";
//    private static String DOTTED_LINE_X = "----x---------------------x----";
//    private static String TEL = "悦收银客服电话  400-888-5400";
//
//
//
//
//    public static String getDateTimeFormatStr(String timeStr) {
//        //20160325160000
//        if(Utils.isNotEmpty(timeStr)){
//            if(timeStr.length()>=14){
//                String year = timeStr.substring(0,4);
//                String month = timeStr.substring(4,6);
//                String day = timeStr.substring(6,8);
//                String hour = timeStr.substring(8,10);
//                String minute = timeStr.substring(10,12);
//                String second = timeStr.substring(12);
//                Log.e("日期解析：",year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second);
//                return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
//            }
//        }
//
//        return "";
//    }
//
//
//    /**
//     * 付款成功
//     */
//    public static void payOrderText(Context context, int id, UserBean userBean,PosScanpayResData resData){
//        EscCommand esc = new EscCommand();
//        esc.addInitializePrinter();
//        esc.addPrintAndFeedLines( (byte) 3 );
//        /* 设置打印居中 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.CENTER );
//        /* 设置为倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF );
//        /* 打印文字 */
//        esc.addText( "收款凭据\n" );
//        esc.addPrintAndLineFeed();
//
//        /* 打印文字 */
//        /* 取消倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF );
//        /* 设置打印左对齐 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.LEFT );
//        /* 打印文字 */
//        esc.addText( "商户名称："+userBean.getMname()+"\n" );
//        esc.addText( "终端号："+userBean.getTerminal_id()+"\n" );
//        esc.addText( "交易类型："+Constants.getPayWay("0",resData.getPay_type(),true) +"\n" );
//        String outTradeNoStr = resData.getOut_trade_no();
//        String outTradeNo = "";
//        String outTradeNoSuffix = "";
//        if(Utils.isNotEmpty(outTradeNoStr)&&outTradeNoStr.length()>=32){
//            outTradeNo = outTradeNoStr.substring(0,24);
//            outTradeNoSuffix = outTradeNoStr.substring(24);
//            esc.addText( "订单号："+outTradeNo+"\n" );
//            esc.addText( "        "+outTradeNoSuffix+"\n" );
//        }else{
//            outTradeNo = outTradeNoStr;
//            esc.addText( "订单号："+outTradeNo+"\n" );
//        }
//        esc.addText( "日期/时间："+getDateTimeFormatStr(resData.getEnd_time())+"\n" );
//        esc.addText( "金额：RMB "+DecimalUtil.branchToElement(resData.getTotal_fee())+"\n" );
//        esc.addText( "备注：\n" );
//        esc.addText( "持卡人签名：\n" );
//        esc.addPrintAndLineFeed();
//        esc.addText( "  交易金额不足300.00元，无需签名\n" );
//        esc.addText( "  本人确认以上交易，同意将其记入本卡账户\n" );
//        esc.addPrintAndLineFeed();
//        esc.addText(DOTTED_LINE+"\n");
//        esc.addText(TEL+"\n");
//        esc.addText(DOTTED_LINE_X+"\n");
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        /* 开钱箱 */
////        esc.addGeneratePlus( LabelCommand.FOOT.F5, (byte) 255, (byte) 255 );
////        esc.addPrintAndFeedLines( (byte) 8 );
////        /* 加入查询打印机状态，用于连续打印 */
//        byte[] bytes = { 29, 114, 1 };
//        esc.addUserCommand( bytes );
//        Vector<Byte> datas = esc.getCommand();
//        /* 发送数据 */
//        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately( datas );
//    }
//
//    /**
//     * 订单详情
//     */
//    public static void orderDetailText(Context context, int id, UserBean userBean,OrderDetailData order){
//        EscCommand esc = new EscCommand();
//        esc.addInitializePrinter();
//        esc.addPrintAndFeedLines( (byte) 3 );
//        /* 设置打印居中 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.CENTER );
//        /* 设置为倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF );
//        /* 打印文字 */
//        esc.addText( "收款凭据\n" );
//        esc.addPrintAndLineFeed();
//
//        /* 打印文字 */
//        /* 取消倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF );
//        /* 设置打印左对齐 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.LEFT );
//        /* 打印文字 */
//        esc.addText( "商户名称："+userBean.getMname()+"\n" );
//        esc.addText( "终端号："+userBean.getTerminal_id()+"\n" );
//        esc.addText( "交易类型："+Constants.getPayWay(order.getOrderType(),order.getPayWay(),true) +"\n" );
//        String outTradeNoStr = order.getOrderId();
//        String outTradeNo = "";
//        String outTradeNoSuffix = "";
//        if(Utils.isNotEmpty(outTradeNoStr)&&outTradeNoStr.length()>=32){
//            outTradeNo = outTradeNoStr.substring(0,24);
//            outTradeNoSuffix = outTradeNoStr.substring(24);
//            esc.addText( "订单号："+outTradeNo+"\n" );
//            esc.addText( "        "+outTradeNoSuffix+"\n" );
//        }else{
//            outTradeNo = outTradeNoStr;
//            esc.addText( "订单号："+outTradeNo+"\n" );
//        }
//        String dateStr = DateTimeUtil.stampToDate(Long.parseLong(order.getPayTime()));
//        Log.e("日期转换结果：",dateStr);
//        esc.addText( "日期/时间："+dateStr+"\n" );
//        esc.addText( "金额：RMB "+DecimalUtil.StringToPrice(order.getGoodsPrice())+"\n" );
//        esc.addText( "备注：明细补打\n" );
//        esc.addText( "持卡人签名：\n" );
//        esc.addPrintAndLineFeed();
//        esc.addText( "  交易金额不足300.00元，无需签名\n" );
//        esc.addText( "  本人确认以上交易，同意将其记入本卡账户\n" );
//        esc.addPrintAndLineFeed();
//        esc.addText(DOTTED_LINE+"\n");
//        esc.addText(TEL+"\n");
//        esc.addText(DOTTED_LINE_X+"\n");
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        /* 开钱箱 */
////        esc.addGeneratePlus( LabelCommand.FOOT.F5, (byte) 255, (byte) 255 );
////        esc.addPrintAndFeedLines( (byte) 8 );
////        /* 加入查询打印机状态，用于连续打印 */
//        byte[] bytes = { 29, 114, 1 };
//        esc.addUserCommand( bytes );
//        Vector<Byte> datas = esc.getCommand();
//        /* 发送数据 */
//        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately( datas );
//    }
//
//
//
//    /**
//     * 消费有礼
//     */
//    public static void cardStockQRcode(int id,String url){
//        EscCommand esc = new EscCommand();
//        esc.addInitializePrinter();
//        esc.addPrintAndFeedLines( (byte) 3 );
//        /* 设置打印居中 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.CENTER );
//        /* 设置为倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF );
//        /* 设置qrcode模块大小 */
//        esc.addSelectSizeOfModuleForQRCode( (byte) 5 );
//        /* 设置qrcode内容 */
//        esc.addStoreQRCodeData( url );
//        /* 打印QRCode */
//        esc.addPrintQRCode();
//        esc.addPrintAndLineFeed();
//        esc.addText(DOTTED_LINE+"\n");
//        esc.addText(TEL+"\n");
//        esc.addText(DOTTED_LINE_X+"\n");
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        /* 开钱箱 */
////        esc.addGeneratePlus( LabelCommand.FOOT.F5, (byte) 255, (byte) 255 );
////        esc.addPrintAndFeedLines( (byte) 8 );
////        /* 加入查询打印机状态，用于连续打印 */
//        byte[] bytes = { 29, 114, 1 };
//        esc.addUserCommand( bytes );
//        Vector<Byte> datas = esc.getCommand();
//        /* 发送数据 */
//        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately( datas );
//    }
//
//
//    /**
//     * 退款成功打印
//     */
//    public static void refundText( int id, UserBean userBean,RefundResData order){
//        EscCommand esc = new EscCommand();
//        esc.addInitializePrinter();
//        esc.addPrintAndFeedLines( (byte) 3 );
//        /* 设置打印居中 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.CENTER );
//        /* 设置为倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF );
//        /* 打印文字 */
//        esc.addText( "收款凭据\n" );
//        esc.addPrintAndLineFeed();
//
//        /* 打印文字 */
//        /* 取消倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF );
//        /* 设置打印左对齐 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.LEFT );
//        /* 打印文字 */
//        esc.addText( "商户名称："+userBean.getMname()+"\n" );
//        esc.addText( "终端号："+userBean.getTerminal_id()+"\n" );
//        esc.addText( "交易类型："+Constants.getPayWay("1",order.getPay_type(),true) +"\n" );
//
//        String outRefundNoStr = order.getOut_refund_no();
//        String outRefundNo = "";
//        String outRefundNoNoSuffix = "";
//        if(Utils.isNotEmpty(outRefundNoStr)&&outRefundNoStr.length()>=32){
//            outRefundNo = outRefundNoStr.substring(0,24);
//            outRefundNoNoSuffix = outRefundNoStr.substring(24);
//            esc.addText( "订单号："+outRefundNo +"\n" );
//            esc.addText( "        "+outRefundNoNoSuffix +"\n" );
//
//        }else{
//            outRefundNo = outRefundNoStr;
//            esc.addText( "订单号："+outRefundNo +"\n" );
//        }
//        esc.addText( "原订单号："+order.getOut_trade_no() +"\n" );
////        esc.addText( "日期/时间："+getDateTimeFormatStr(order.getEnd_time()) +"\n" );
//        esc.addText( "金额：RMB "+DecimalUtil.branchToElement(order.getRefund_fee()) +"\n" );
//        esc.addText( "备注：\n" );
//        esc.addText( "持卡人签名：\n" );
//        esc.addPrintAndLineFeed();
//        esc.addText( "  交易金额不足300.00元，无需签名\n" );
//        esc.addText( "  本人确认以上交易，同意将其记入本卡账户\n" );
//        esc.addPrintAndLineFeed();
//        esc.addText(DOTTED_LINE+"\n");
//        esc.addText(TEL+"\n");
//        esc.addText(DOTTED_LINE_X+"\n");
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        /* 开钱箱 */
////        esc.addGeneratePlus( LabelCommand.FOOT.F5, (byte) 255, (byte) 255 );
////        esc.addPrintAndFeedLines( (byte) 8 );
////        /* 加入查询打印机状态，用于连续打印 */
//        byte[] bytes = { 29, 114, 1 };
//        esc.addUserCommand( bytes );
//        Vector<Byte> datas = esc.getCommand();
//        /* 发送数据 */
//        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately( datas );
//    }
//
//    /**
//     * 交班退出打印(交班记录详情补打)
//     */
//    public static void shiftExitText(Context context, int id, UserBean userBean, ShiftResData summary, String staffName){
//        EscCommand esc = new EscCommand();
//        esc.addInitializePrinter();
//        esc.addPrintAndFeedLines( (byte) 3 );
//        /* 设置打印居中 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.CENTER );
//        /* 设置为倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF );
//        /* 打印文字 */
//        esc.addText( "收款凭据\n" );
//        esc.addPrintAndLineFeed();
//
//        /* 打印文字 */
//        /* 取消倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF );
//        /* 设置打印左对齐 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.LEFT );
//        /* 打印文字 */
//        esc.addText( "商户名称："+userBean.getMname()+"\n" );
//        esc.addText( "终端号："+userBean.getTerminal_id()+"\n" );
//        //结算时间
//        ArrayList<SubTimeSummaryResData> timeList = summary.getTimeList();
//        SubTimeSummaryResData subStartTime = null;
//        SubTimeSummaryResData subEndTime = null;
//        for (int i = 0; i < timeList.size(); i++) {
//            subStartTime = timeList.get(0);
//            subEndTime = timeList.get(1);
//
//        }
//        Log.e("打印开始时间：",subStartTime.getType());
//        Log.e("打印结束时间：",subEndTime.getType());
//        esc.addText( "开始时间："+ subStartTime.getType()+"\n" );
//        esc.addText( "结束时间："+ subEndTime.getType()+"\n" );
//        if(Utils.isNotEmpty(staffName)){
//            esc.addText( "交 班 人："+staffName+"\n" );
//        }else{
//            esc.addText( "交 班 人："+""+"\n" );
//        }
//        esc.addText( "类型/TYPE  笔数/SUM  金额/AMOUNT"+"\n" );
//        esc.addText(DOTTED_LINE_X+"\n");
//        //交易明细
//        ArrayList<SubReocrdSummaryResData> reocrdList = summary.getReocrdList();
//        for (int i = 0; i < reocrdList.size(); i++) {
//            SubReocrdSummaryResData reocrd = reocrdList.get(i);
//            String mode = reocrd.getMode();
//            if("WX".equals(mode)){
//                String type = reocrd.getType();
//                if("noRefund".equals(type)){
//                    esc.addText( "微信"+nineSpaceStr+reocrd.getTotalCount()+nineSpaceStr+reocrd.getMoney()+"\n" );
//                }else if("refund".equals(type)){
//                    esc.addText( "微信退款"+fiveSpaceStr+reocrd.getTotalCount()+nineSpaceStr+reocrd.getMoney()+"\n" );
//                }
//            }else if("ALI".equals(mode)){
//                String type = reocrd.getType();
//                if("noRefund".equals(type)){
//                    esc.addText( "支付宝"+sevenSpaceStr+reocrd.getTotalCount()+nineSpaceStr+reocrd.getMoney()+"\n" );
//                }else if("refund".equals(type)){
//                    esc.addText("支付宝退款"+threeSpaceStr+reocrd.getTotalCount()+nineSpaceStr+reocrd.getMoney()+"\n" );
//                }
//            }else if("BEST".equals(mode)){
//                String type = reocrd.getType();
//                if("noRefund".equals(type)){
//                    esc.addText("翼支付"+sevenSpaceStr+reocrd.getTotalCount()+nineSpaceStr+reocrd.getMoney()+"\n" );
//                }else if("refund".equals(type)){
//                    esc.addText( "翼支付退款"+threeSpaceStr+reocrd.getTotalCount()+nineSpaceStr+reocrd.getMoney()+"\n" );
//                }
//            }else if("BANK".equals(mode)){
//                String type = reocrd.getType();
//                if("noRefund".equals(type)){
//                    esc.addText( "银行卡消费"+threeSpaceStr+reocrd.getTotalCount()+nineSpaceStr+reocrd.getMoney()+"\n" );
//                }else if("refund".equals(type)){
//                    esc.addText( "银行卡退款"+threeSpaceStr+reocrd.getTotalCount()+nineSpaceStr+reocrd.getMoney()+"\n" );
//                }
//            }else if("UNIONPAY".equals(mode)){
//                String type = reocrd.getType();
//                if("noRefund".equals(type)){
//                    esc.addText( "银联二维码"+threeSpaceStr+reocrd.getTotalCount()+nineSpaceStr+reocrd.getMoney()+"\n" );
//                }else if("refund".equals(type)){
//                    esc.addText( "银联二维码退款"+twoSpaceStr+reocrd.getTotalCount()+nineSpaceStr+reocrd.getMoney()+"\n" );
//                }
//            }
//        }
//        //总计
//        ArrayList<SubTotalSummaryResData> totalList = summary.getTotalList();
//        SubTotalSummaryResData total = null;
//        for (int i = 0; i < totalList.size(); i++) {
//            total = totalList.get(i);
//        }
//        esc.addText( "总计"+nineSpaceStr+total.getTotalCount()+nineSpaceStr+total.getMoney()+"\n" );
//        esc.addText(DOTTED_LINE_X+"\n");
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        /* 开钱箱 */
////        esc.addGeneratePlus( LabelCommand.FOOT.F5, (byte) 255, (byte) 255 );
////        esc.addPrintAndFeedLines( (byte) 8 );
////        /* 加入查询打印机状态，用于连续打印 */
//        byte[] bytes = { 29, 114, 1 };
//        esc.addUserCommand( bytes );
//        Vector<Byte> datas = esc.getCommand();
//        /* 发送数据 */
//        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately( datas );
//    }
//
//    /**
//     * 汇总信息打印
//     */
//    public static void summaryText(Context context, int id, UserBean userBean, SummaryResData summary, String dateStr){
//        EscCommand esc = new EscCommand();
//        esc.addInitializePrinter();
//        esc.addPrintAndFeedLines( (byte) 3 );
//        /* 设置打印居中 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.CENTER );
//        /* 设置为倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF );
//        /* 打印文字 */
//        esc.addText( "收款凭据\n" );
//        esc.addPrintAndLineFeed();
//
//        /* 打印文字 */
//        /* 取消倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF );
//        /* 设置打印左对齐 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.LEFT );
//        /* 打印文字 */
//        esc.addText( "商户名称："+userBean.getMname()+"\n" );
//        esc.addText( "终端号："+userBean.getTerminal_id()+"\n" );
//        esc.addText( "日期/时间："+dateStr+"\n" );
//        esc.addText( "类型/TYPE  笔数/SUM  金额/AMOUNT"+"\n" );
//        esc.addText( DOTTED_LINE+"\n" );
//        //交易明细
//        List<PayTypeBean> lsPayType = new ArrayList<PayTypeBean>();
//        lsPayType = summary.getOrderSumList();
//        //银行卡总金额  = 贷记卡总金额 + 借记卡总金额
//        double sumMoney = 0;
//        //银行卡总笔数 = 贷记卡总笔数 + 借记卡总笔数
//        int sumNum = 0;
//        //标示是否有银行卡记录
//        boolean isBank = false;
//        for (int i = 0; i < lsPayType.size(); i++) {
//            PayTypeBean payType = lsPayType.get(i);
//            String mode = payType.getPayWay();
//            if("WX".equals(mode)){
//                Double amount = payType.getAmount();
//                Integer total = payType.getTotal();
//
//                double amount_dou = amount.doubleValue();
//                int total_int = total.intValue();
//                esc.addText( "微信支付"+fiveSpaceStr+String.valueOf(total_int)+nineSpaceStr+String.valueOf(amount_dou)+"\n" );
//            }else if("ALI".equals(mode)){
//                Double amount = payType.getAmount();
//                Integer total = payType.getTotal();
//
//                double amount_dou = amount.doubleValue();
//                int total_int = total.intValue();
//
//                esc.addText( "支付宝支付"+threeSpaceStr+String.valueOf(total_int)+nineSpaceStr+String.valueOf(amount_dou)+"\n" );
//
//            }else if("BEST".equals(mode)){
//                Double amount = payType.getAmount();
//                Integer total = payType.getTotal();
//
//                double amount_dou = amount.doubleValue();
//                int total_int = total.intValue();
//
//                esc.addText( "翼支付支付"+threeSpaceStr+String.valueOf(total_int)+nineSpaceStr+String.valueOf(amount_dou)+"\n" );
//
//            }else if("UNIONPAY".equals(mode)){
//                Double amount = payType.getAmount();
//                Integer total = payType.getTotal();
//
//                double amount_dou = amount.doubleValue();
//                int total_int = total.intValue();
//                esc.addText( "银联二维码"+threeSpaceStr+String.valueOf(total_int)+nineSpaceStr+String.valueOf(amount_dou)+"\n" );
//
//            }else if("DEBIT".equals(mode) || "CREDIT".equals(mode)){
//                Double amount = payType.getAmount();
//                Integer total = payType.getTotal();
//
//
//
//                double amount_dou = amount.doubleValue();
//                sumMoney = sumMoney + amount_dou;
//                int total_int = total.intValue();
//                sumNum = sumNum + total_int;
//
//                isBank = true;
//
//
//            }
//        }
//        if(isBank){
//            esc.addText( "银行卡消费"+threeSpaceStr+String.valueOf(sumNum)+nineSpaceStr+String.valueOf(sumMoney)+"\n" );
//        }
//        //总笔数
//        Integer sumTotal = summary.getSumTotal();
//        int sumTotal_int = sumTotal.intValue();
//        //总金额
//        Double sumAmt = summary.getSumAmt();
//        double sumAmt_dou = sumAmt.doubleValue();
//        esc.addText( "总计"+nineSpaceStr+String.valueOf(sumTotal_int)+nineSpaceStr+String.valueOf(sumAmt_dou)+"\n" );
//
//
//
//        esc.addText(DOTTED_LINE_X+"\n");
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        /* 开钱箱 */
////        esc.addGeneratePlus( LabelCommand.FOOT.F5, (byte) 255, (byte) 255 );
////        esc.addPrintAndFeedLines( (byte) 8 );
////        /* 加入查询打印机状态，用于连续打印 */
//        byte[] bytes = { 29, 114, 1 };
//        esc.addUserCommand( bytes );
//        Vector<Byte> datas = esc.getCommand();
//        /* 发送数据 */
//        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately( datas );
//    }
//
//
//    /**
//     * 会员卡劵核销成功打印
//     */
//    public static void cardVerificaText(int id, UserBean userBean, CardVerificaResData resData){
//        EscCommand esc = new EscCommand();
//        esc.addInitializePrinter();
//        esc.addPrintAndFeedLines( (byte) 3 );
//        /* 设置打印居中 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.CENTER );
//        /* 设置为倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF );
//        /* 打印文字 */
//        esc.addText( "收款凭据\n" );
//        esc.addPrintAndLineFeed();
//
//        /* 打印文字 */
//        /* 取消倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF );
//        /* 设置打印左对齐 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.LEFT );
//        /* 打印文字 */
//        esc.addText( "商户名称："+userBean.getMname()+"\n" );
//        esc.addText( "终端号："+userBean.getTerminal_id()+"\n" );
//        esc.addText( "卡劵名称："+resData.getTitle()+"\n" );
//        esc.addText( "核销劵码："+resData.getCode()+"\n" );
//        esc.addText( "核销状态：使用成功"+"\n" );
//        esc.addText( "使用时间："+DateTimeUtil.stampToDate(Long.parseLong(resData.getCreateTime()))+"\n" );
//        esc.addText( "备注："+"\n" );
//        esc.addText(DOTTED_LINE+"\n");
//        esc.addText(TEL+"\n");
//        esc.addText(DOTTED_LINE_X+"\n");
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        /* 开钱箱 */
////        esc.addGeneratePlus( LabelCommand.FOOT.F5, (byte) 255, (byte) 255 );
////        esc.addPrintAndFeedLines( (byte) 8 );
////        /* 加入查询打印机状态，用于连续打印 */
//        byte[] bytes = { 29, 114, 1 };
//        esc.addUserCommand( bytes );
//        Vector<Byte> datas = esc.getCommand();
//        /* 发送数据 */
//        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately( datas );
//    }
//
//
//    /**
//     * 预授权
//     */
//    public static void authOrderText(int id,UserBean userBean, AuthResultResponse resData){
//        EscCommand esc = new EscCommand();
//        esc.addInitializePrinter();
//        esc.addPrintAndFeedLines( (byte) 3 );
//        /* 设置打印居中 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.CENTER );
//        /* 设置为倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF );
//        /* 打印文字 */
//        esc.addText( "收款凭据\n" );
//        esc.addPrintAndLineFeed();
//
//        /* 打印文字 */
//        /* 取消倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF );
//        /* 设置打印左对齐 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.LEFT );
//        /* 打印文字 */
//        esc.addText( "商户名称："+userBean.getMname()+"\n" );
//        esc.addText( "终端号："+userBean.getTerminal_id()+"\n" );
//        esc.addText( "交易类型："+Constants.getAuthPayTypeStr("1") +"\n" );
//        esc.addText( "订单号："+resData.getOut_trade_no() +"\n" );
//        esc.addText( "金额：RMB "+DecimalUtil.branchToElement(resData.getTotal_amount()) +"\n" );
//        esc.addText( "日期/时间："+resData.getEnd_time()+"\n" );
//        esc.addText( "备注：\n" );
//        esc.addText( "持卡人签名：\n" );
//        esc.addPrintAndLineFeed();
//        esc.addText( "  交易金额不足300.00元，无需签名\n" );
//        esc.addText( "  本人确认以上交易，同意将其记入本卡账户\n" );
//        esc.addPrintAndLineFeed();
//        esc.addText(DOTTED_LINE+"\n");
//        /* 设置打印居中 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.CENTER );
//        /* 设置qrcode模块大小 */
//        esc.addSelectSizeOfModuleForQRCode( (byte) 15 );
//        /* 设置qrcode内容 */
//        esc.addStoreQRCodeData( resData.getOut_trade_no() );
//        /* 打印QRCode */
//        esc.addPrintQRCode();
//        esc.addPrintAndLineFeed();
//        esc.addText( "预授权完成/撤销可直接扫描上方二维码!"+"\n" );
//
//        esc.addText(DOTTED_LINE+"\n");
//        esc.addText(TEL+"\n");
//        esc.addText(DOTTED_LINE_X+"\n");
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        /* 开钱箱 */
////        esc.addGeneratePlus( LabelCommand.FOOT.F5, (byte) 255, (byte) 255 );
////        esc.addPrintAndFeedLines( (byte) 8 );
////        /* 加入查询打印机状态，用于连续打印 */
//        byte[] bytes = { 29, 114, 1 };
//        esc.addUserCommand( bytes );
//        Vector<Byte> datas = esc.getCommand();
//        /* 发送数据 */
//        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately( datas );
//    }
//
//    /**
//     * 预授权撤销
//     */
//    public static void authCancelOrderText(int id, UserBean userBean,AuthResultResponse resData){
//        EscCommand esc = new EscCommand();
//        esc.addInitializePrinter();
//        esc.addPrintAndFeedLines( (byte) 3 );
//        /* 设置打印居中 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.CENTER );
//        /* 设置为倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF );
//        /* 打印文字 */
//        esc.addText( "收款凭据\n" );
//        esc.addPrintAndLineFeed();
//
//        /* 打印文字 */
//        /* 取消倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF );
//        /* 设置打印左对齐 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.LEFT );
//        /* 打印文字 */
//        esc.addText( "商户名称："+userBean.getMname()+"\n" );
//        esc.addText( "终端号："+userBean.getTerminal_id()+"\n" );
//        esc.addText( "交易类型："+Constants.getAuthPayTypeStr("2") +"\n" );
//        esc.addText( "订单号："+resData.getOut_trade_no() +"\n" );
//        esc.addText( "金额：RMB "+DecimalUtil.branchToElement(resData.getTotal_amount()) +"\n" );
//        esc.addText( "日期/时间："+resData.getEnd_time()+"\n" );
//        esc.addText( "备注：\n" );
//        esc.addText( "持卡人签名：\n" );
//        esc.addPrintAndLineFeed();
//        esc.addText( "  交易金额不足300.00元，无需签名\n" );
//        esc.addText( "  本人确认以上交易，同意将其记入本卡账户\n" );
//        esc.addPrintAndLineFeed();
//        esc.addText(DOTTED_LINE+"\n");
//        esc.addText(TEL+"\n");
//        esc.addText(DOTTED_LINE_X+"\n");
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        /* 开钱箱 */
////        esc.addGeneratePlus( LabelCommand.FOOT.F5, (byte) 255, (byte) 255 );
////        esc.addPrintAndFeedLines( (byte) 8 );
////        /* 加入查询打印机状态，用于连续打印 */
//        byte[] bytes = { 29, 114, 1 };
//        esc.addUserCommand( bytes );
//        Vector<Byte> datas = esc.getCommand();
//        /* 发送数据 */
//        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately( datas );
//    }
//
//    /**
//     * 预授权完成
//     */
//    public static void authConfirmOrderText(int id, UserBean userBean,AuthResultResponse resData){
//        EscCommand esc = new EscCommand();
//        esc.addInitializePrinter();
//        esc.addPrintAndFeedLines( (byte) 3 );
//        /* 设置打印居中 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.CENTER );
//        /* 设置为倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF );
//        /* 打印文字 */
//        esc.addText( "收款凭据\n" );
//        esc.addPrintAndLineFeed();
//
//        /* 打印文字 */
//        /* 取消倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF );
//        /* 设置打印左对齐 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.LEFT );
//        /* 打印文字 */
//        esc.addText( "商户名称："+userBean.getMname()+"\n" );
//        esc.addText( "终端号："+userBean.getTerminal_id()+"\n" );
//        esc.addText( "交易类型："+Constants.getAuthPayTypeStr("3") +"\n" );
//        esc.addText( "订单号："+resData.getOut_trade_no() +"\n" );
//        esc.addText( "金额：RMB "+DecimalUtil.branchToElement(resData.getConsume_fee()) +"\n" );
//        esc.addText( "日期/时间："+resData.getEnd_time()+"\n" );
//        esc.addText( "备注：\n" );
//        esc.addText( "持卡人签名：\n" );
//        esc.addPrintAndLineFeed();
//        esc.addText( "  交易金额不足300.00元，无需签名\n" );
//        esc.addText( "  本人确认以上交易，同意将其记入本卡账户\n" );
//        esc.addPrintAndLineFeed();
//        esc.addText(DOTTED_LINE+"\n");
//        esc.addText(TEL+"\n");
//        esc.addText(DOTTED_LINE_X+"\n");
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        /* 开钱箱 */
////        esc.addGeneratePlus( LabelCommand.FOOT.F5, (byte) 255, (byte) 255 );
////        esc.addPrintAndFeedLines( (byte) 8 );
////        /* 加入查询打印机状态，用于连续打印 */
//        byte[] bytes = { 29, 114, 1 };
//        esc.addUserCommand( bytes );
//        Vector<Byte> datas = esc.getCommand();
//        /* 发送数据 */
//        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately( datas );
//    }
//    /**
//     * 预授权完成撤销
//     */
//    public static void authConfirmCancelOrderText(int id, UserBean userBean,AuthResultResponse resData){
//        EscCommand esc = new EscCommand();
//        esc.addInitializePrinter();
//        esc.addPrintAndFeedLines( (byte) 3 );
//        /* 设置打印居中 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.CENTER );
//        /* 设置为倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF );
//        /* 打印文字 */
//        esc.addText( "收款凭据\n" );
//        esc.addPrintAndLineFeed();
//
//        /* 打印文字 */
//        /* 取消倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF );
//        /* 设置打印左对齐 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.LEFT );
//        /* 打印文字 */
//        esc.addText( "商户名称："+userBean.getMname()+"\n" );
//        esc.addText( "终端号："+userBean.getTerminal_id()+"\n" );
//        esc.addText( "交易类型："+Constants.getAuthPayTypeStr("4") +"\n" );
//        esc.addText( "订单号："+resData.getOut_trade_no() +"\n" );
//        esc.addText( "金额：RMB "+DecimalUtil.branchToElement(resData.getRefund_fee()) +"\n" );
//        esc.addText( "日期/时间："+resData.getEnd_time()+"\n" );
//        esc.addText( "备注：\n" );
//        esc.addText( "持卡人签名：\n" );
//        esc.addPrintAndLineFeed();
//        esc.addText( "  交易金额不足300.00元，无需签名\n" );
//        esc.addText( "  本人确认以上交易，同意将其记入本卡账户\n" );
//        esc.addPrintAndLineFeed();
//        esc.addText(DOTTED_LINE+"\n");
//        esc.addText(TEL+"\n");
//        esc.addText(DOTTED_LINE_X+"\n");
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        /* 开钱箱 */
////        esc.addGeneratePlus( LabelCommand.FOOT.F5, (byte) 255, (byte) 255 );
////        esc.addPrintAndFeedLines( (byte) 8 );
////        /* 加入查询打印机状态，用于连续打印 */
//        byte[] bytes = { 29, 114, 1 };
//        esc.addUserCommand( bytes );
//        Vector<Byte> datas = esc.getCommand();
//        /* 发送数据 */
//        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately( datas );
//    }
//
//
//    /**
//     * 预授权交易详情
//     */
//    public static void authOrderDetailText(int id, UserBean userBean,WdPreAuthHistoryVO order){
//        EscCommand esc = new EscCommand();
//        esc.addInitializePrinter();
//        esc.addPrintAndFeedLines( (byte) 3 );
//        /* 设置打印居中 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.CENTER );
//        /* 设置为倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF );
//        /* 打印文字 */
//        esc.addText( "收款凭据\n" );
//        esc.addPrintAndLineFeed();
//
//        /* 打印文字 */
//        /* 取消倍高倍宽 */
//        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF );
//        /* 设置打印左对齐 */
//        esc.addSelectJustification( EscCommand.JUSTIFICATION.LEFT );
//        /* 打印文字 */
//        esc.addText( "商户名称："+userBean.getMname()+"\n" );
//        esc.addText( "终端号："+userBean.getTerminal_id()+"\n" );
//        /**
//         * payAuthStatus:1 预授权 2撤销 3押金消费 4押金退款
//         */
//        String payAuthStatus = order.getPayAuthStatus();
//        if(Utils.isNotEmpty(payAuthStatus)){
//            esc.addText( "交易类型："+Constants.getAuthPayTypeStr(order.getPayAuthStatus()) +"\n" );
//            esc.addText( "订单号："+order.getMchntOrderNo() +"\n" );
//            if("1".equals(payAuthStatus)){
//                esc.addText( "押金金额：RMB "+DecimalUtil.StringToPrice(order.getOrderAmt()) +"\n" );
//            }else if("2".equals(payAuthStatus)){
//                esc.addText( "押金金额：RMB "+DecimalUtil.StringToPrice(order.getOrderAmt()) +"\n" );
//            }else if("3".equals(payAuthStatus)){
//                esc.addText( "押金金额：RMB "+DecimalUtil.StringToPrice(order.getOrderAmt()) +"\n" );
//                esc.addText( "消费金额：RMB "+DecimalUtil.StringToPrice(order.getConsumeFee()) +"\n" );
//            }else if("4".equals(payAuthStatus)){
//                esc.addText( "原订单号："+order.getOrgOrderNo() +"\n" );
//                esc.addText( "押金金额：RMB "+DecimalUtil.StringToPrice(order.getOrderAmt()) +"\n" );
//                esc.addText( "消费金额：RMB "+DecimalUtil.StringToPrice(order.getConsumeFee()) +"\n" );
//                esc.addText( "退款金额：RMB "+DecimalUtil.StringToPrice(order.getRefundFee()) +"\n" );
//            }
//        }
//        esc.addText( "日期/时间："+DateTimeUtil.stampToFormatDate(order.getGmtCreate(), "yyyy-MM-dd HH:mm:ss")+"\n" );
//        esc.addText( "备注：交易明细\n" );
//        esc.addText( "持卡人签名：\n" );
//        esc.addPrintAndLineFeed();
//        esc.addText( "  交易金额不足300.00元，无需签名\n" );
//        esc.addText( "  本人确认以上交易，同意将其记入本卡账户\n" );
//        esc.addPrintAndLineFeed();
//        esc.addText(DOTTED_LINE+"\n");
//        if(Utils.isNotEmpty(payAuthStatus)){
//            esc.addText( "交易类型："+Constants.getAuthPayTypeStr(order.getPayAuthStatus()) +"\n" );
//            esc.addText( "订单号："+order.getMchntOrderNo() +"\n" );
//            if("1".equals(payAuthStatus)){
//                /* 设置打印居中 */
//                esc.addSelectJustification( EscCommand.JUSTIFICATION.CENTER );
//                /* 设置qrcode模块大小 */
//                esc.addSelectSizeOfModuleForQRCode( (byte) 15 );
//                /* 设置qrcode内容 */
//                esc.addStoreQRCodeData( order.getMchntOrderNo() );
//                /* 打印QRCode */
//                esc.addPrintQRCode();
//                esc.addPrintAndLineFeed();
//                esc.addText( "预授权完成/撤销可直接扫描上方二维码!"+"\n" );
//            }
//        }
//        esc.addText(DOTTED_LINE+"\n");
//        esc.addText(TEL+"\n");
//        esc.addText(DOTTED_LINE_X+"\n");
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        esc.addPrintAndLineFeed();
//        /* 开钱箱 */
////        esc.addGeneratePlus( LabelCommand.FOOT.F5, (byte) 255, (byte) 255 );
////        esc.addPrintAndFeedLines( (byte) 8 );
////        /* 加入查询打印机状态，用于连续打印 */
//        byte[] bytes = { 29, 114, 1 };
//        esc.addUserCommand( bytes );
//        Vector<Byte> datas = esc.getCommand();
//        /* 发送数据 */
//        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately( datas );
//    }


















    public static String wifiSendMsgDetail(){
        String msg =
                "<gpWord Align=1 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>此处为打印的文本内容</gpWord>"+"<gpBr/>"+
                "<gpBarCode Align=1 Type=1 Width=5 Height=5 Position=0>条码内容</gpBarCode>"+"<gpBr/>"+
                "<gpQRCode Align=1 Size=15 Error=L>二维码内容</gpQRCode>"+"<gpBr/>";

        return msg;
    }

    public static void printText(Context context,int id){
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addPrintAndFeedLines( (byte) 3 );
        /* 设置打印居中 */
        esc.addSelectJustification( EscCommand.JUSTIFICATION.CENTER );
        /* 设置为倍高倍宽 */
        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF );
        /* 打印文字 */
        esc.addText( "Sample\n" );
        esc.addPrintAndLineFeed();

        /* 打印文字 */
        /* 取消倍高倍宽 */
        esc.addSelectPrintModes( EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF );
        /* 设置打印左对齐 */
        esc.addSelectJustification( EscCommand.JUSTIFICATION.LEFT );
        /* 打印文字 */
        esc.addText( "Print text\n" );
        /* 打印文字 */
        esc.addText( "Welcome to use SMARNET printer!\n" );

        /* 打印繁体中文 需要打印机支持繁体字库 */
        String message = "佳博智匯票據打印機\n";
        esc.addText( message, "GB2312" );
        esc.addPrintAndLineFeed();

        /* 绝对位置 具体详细信息请查看GP58编程手册 */
        esc.addText( "智汇" );
        esc.addSetHorAndVerMotionUnits( (byte) 7, (byte) 0 );
        esc.addSetAbsolutePrintPosition( (short) 6 );
        esc.addText( "网络" );
        esc.addSetAbsolutePrintPosition( (short) 10 );
        esc.addText( "设备" );
        esc.addPrintAndLineFeed();

        /* 打印图片 */
        /* 打印文字 */
        esc.addText( "Print bitmap!\n" );
        Bitmap b = BitmapFactory.decodeResource( context.getResources(),R.drawable.gprinter );
        /* 打印图片 */
        esc.addRastBitImage( b, 380, 0 );

        /* 打印一维条码 */
        /* 打印文字 */
        esc.addText( "Print code128\n" );
        esc.addSelectPrintingPositionForHRICharacters( EscCommand.HRI_POSITION.BELOW );
        /*
         * 设置条码可识别字符位置在条码下方
         * 设置条码高度为60点
         */
        esc.addSetBarcodeHeight( (byte) 60 );
        /* 设置条码单元宽度为1 */
        esc.addSetBarcodeWidth( (byte) 1 );
        /* 打印Code128码 */
        esc.addCODE128( esc.genCodeB( "SMARNET" ) );
        esc.addPrintAndLineFeed();


        /*
         * QRCode命令打印 此命令只在支持QRCode命令打印的机型才能使用。 在不支持二维码指令打印的机型上，则需要发送二维条码图片
         */
        /* 打印文字 */
        esc.addText( "Print QRcode\n" );
        /* 设置纠错等级 */
        esc.addSelectErrorCorrectionLevelForQRCode( (byte) 0x31 );
        /* 设置qrcode模块大小 */
        esc.addSelectSizeOfModuleForQRCode( (byte) 3 );
        /* 设置qrcode内容 */
        esc.addStoreQRCodeData( "www.smarnet.cc" );
        esc.addPrintQRCode(); /* 打印QRCode */
        esc.addPrintAndLineFeed();

        /* 设置打印左对齐 */
        esc.addSelectJustification( EscCommand.JUSTIFICATION.CENTER );
        /* 打印文字 */
        esc.addText( "Completed!\r\n" );

        /* 开钱箱 */
        esc.addGeneratePlus( LabelCommand.FOOT.F5, (byte) 255, (byte) 255 );
        esc.addPrintAndFeedLines( (byte) 8 );
        /* 加入查询打印机状态，用于连续打印 */
        byte[] bytes = { 29, 114, 1 };
        esc.addUserCommand( bytes );
        Vector<Byte> datas = esc.getCommand();
        /* 发送数据 */
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately( datas );
    }



}
