package com.xy.util;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(value = { "transTime", "appid", "mchId", "subMchId", "device_info", "transactionId", "outTradeNo",
        "openid", "tradeType", "tradeState", "bankType", "feeType", "totalFee", "mchRedPackageFee", "refundId",
        "outRefundNo", "refundFee", "mchRedPackageRefundFee", "refundType", "refundState", "goodsName", "attach",
        "commissionCharge", "rate" })
public class WechatBillFile {

    // 交易时间,
    private String transTime;
    // 公众账号ID,
    private String appid;
    // 商户号,
    private String mchId;
    // 子商户号,
    private String subMchId;
    // 设备号,
    private String device_info;
    // 微信订单号,
    private String transactionId;
    // 商户订单号,
    private String outTradeNo;
    // 用户标识,
    private String openid;
    // 交易类型,
    private String tradeType;
    // 交易状态,
    private String tradeState;
    // 付款银行,
    private String bankType;
    // 货币种类,
    private String feeType;
    // 总金额,
    private String totalFee;
    // 企业红包金额,
    private String mchRedPackageFee;
    // 微信退款单号,
    private String refundId;
    // 商户退款单号,
    private String outRefundNo;
    // 退款金额,
    private String refundFee;
    // 企业红包退款金额,
    private String mchRedPackageRefundFee;
    // 退款类型,
    private String refundType;
    // 退款状态,
    private String refundState;
    // 商品名称,
    private String goodsName;
    // 商户数据包,
    private String attach;
    // 手续费,
    private String commissionCharge;
    // 费率
    private String rate;

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getSubMchId() {
        return subMchId;
    }

    public void setSubMchId(String subMchId) {
        this.subMchId = subMchId;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getMchRedPackageFee() {
        return mchRedPackageFee;
    }

    public void setMchRedPackageFee(String mchRedPackageFee) {
        this.mchRedPackageFee = mchRedPackageFee;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee;
    }

    public String getMchRedPackageRefundFee() {
        return mchRedPackageRefundFee;
    }

    public void setMchRedPackageRefundFee(String mchRedPackageRefundFee) {
        this.mchRedPackageRefundFee = mchRedPackageRefundFee;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    public String getRefundState() {
        return refundState;
    }

    public void setRefundState(String refundState) {
        this.refundState = refundState;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getCommissionCharge() {
        return commissionCharge;
    }

    public void setCommissionCharge(String commissionCharge) {
        this.commissionCharge = commissionCharge;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }


    @Override
    public String toString() {
        return "WechatBillFile{" +
                "transTime='" + transTime + '\'' +
                ", appid='" + appid + '\'' +
                ", mchId='" + mchId + '\'' +
                ", subMchId='" + subMchId + '\'' +
                ", device_info='" + device_info + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", openid='" + openid + '\'' +
                ", tradeType='" + tradeType + '\'' +
                ", tradeState='" + tradeState + '\'' +
                ", bankType='" + bankType + '\'' +
                ", feeType='" + feeType + '\'' +
                ", totalFee='" + totalFee + '\'' +
                ", mchRedPackageFee='" + mchRedPackageFee + '\'' +
                ", refundId='" + refundId + '\'' +
                ", outRefundNo='" + outRefundNo + '\'' +
                ", refundFee='" + refundFee + '\'' +
                ", mchRedPackageRefundFee='" + mchRedPackageRefundFee + '\'' +
                ", refundType='" + refundType + '\'' +
                ", refundState='" + refundState + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", attach='" + attach + '\'' +
                ", commissionCharge='" + commissionCharge + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }
}
