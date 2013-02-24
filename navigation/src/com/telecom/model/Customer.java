package com.telecom.model;

public class Customer {
    private String mCustomerName;

    private String mPhoneNumber;

    private String mProdId;

    public String getCustomerName() {
        return mCustomerName;
    }

    public void setCustomerName(String customerName) {
        mCustomerName = customerName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getProdId() {
        return mProdId;
    }

    public void setProdId(String prodId) {
        mProdId = prodId;
    }

}
