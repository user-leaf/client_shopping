package com.bjaiyouyou.thismall.model;

/**
 *
 * @author QuXinhang
 *Creare 2016/6/14 14:12
 * 银行卡类
 *
 */
public class BankCard {
    private String bankName;
    private String cardName;
    private String cardId;
    private boolean isQuicklyPayment;

    public BankCard(String bankName, String cardName, String cardId, boolean isQuicklyPayment) {
        this.bankName = bankName;
        this.cardName = cardName;
        this.cardId = cardId;
        this.isQuicklyPayment = isQuicklyPayment;
    }

    public boolean isQuicklyPayment() {
        return isQuicklyPayment;
    }

    public void setQuicklyPayment(boolean quicklyPayment) {
        isQuicklyPayment = quicklyPayment;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
