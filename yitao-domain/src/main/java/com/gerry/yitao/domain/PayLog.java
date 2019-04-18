package com.gerry.yitao.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/15 21:41
 * @Description:
 */
@Data
@Table(name = "tb_pay_log")
public class PayLog implements Serializable {
    private long orderId;
    private Long totalFee;
    private Long userId;
    private String transactionId;
    private Byte status;
    private Byte payType;
    private String bankType;
    private Date createTime;
    private Date payTime;
    private Date closedTime;
    private Date refundTime;

    @Id
    @Column(name = "order_id")
    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    @Basic
    @Column(name = "total_fee")
    public Long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
    }

    @Basic
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "transaction_id")
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Basic
    @Column(name = "status")
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Basic
    @Column(name = "pay_type")
    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    @Basic
    @Column(name = "bank_type")
    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    @Basic
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "pay_time")
    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    @Basic
    @Column(name = "closed_time")
    public Date getClosedTime() {
        return closedTime;
    }

    public void setClosedTime(Date closedTime) {
        this.closedTime = closedTime;
    }

    @Basic
    @Column(name = "refund_time")
    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PayLog payLog = (PayLog) o;
        return orderId == payLog.orderId &&
                Objects.equals(totalFee, payLog.totalFee) &&
                Objects.equals(userId, payLog.userId) &&
                Objects.equals(transactionId, payLog.transactionId) &&
                Objects.equals(status, payLog.status) &&
                Objects.equals(payType, payLog.payType) &&
                Objects.equals(bankType, payLog.bankType) &&
                Objects.equals(createTime, payLog.createTime) &&
                Objects.equals(payTime, payLog.payTime) &&
                Objects.equals(closedTime, payLog.closedTime) &&
                Objects.equals(refundTime, payLog.refundTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, totalFee, userId, transactionId, status, payType, bankType, createTime, payTime, closedTime, refundTime);
    }
}
